package com.businessglue.struts.testing;

import java.awt.Color;
import java.io.File;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import org.catechis.Storage;
import org.catechis.Transformer;
import org.catechis.admin.FileUserOptions;
import org.catechis.constants.Constants;
import org.catechis.dto.AllWordsTest;
import org.catechis.dto.Momento;
import org.catechis.dto.Test;
import org.catechis.dto.Word;
import org.catechis.dto.WordLastTestDates;
import org.catechis.dto.WordTestRecordOptions;
import org.catechis.dto.WordTestResult;
import org.catechis.file.FileJDOMWordLists;
import org.catechis.file.FileTestRecords;
import org.catechis.file.WordNextTestDates;
import org.catechis.filter.WordListFilter;
import org.catechis.gwangali.RateOfTesting;
import org.catechis.juksong.FarmingTools;
import org.catechis.juksong.TestTimeMemory;
import org.catechis.testing.TestUtility;

import com.businessglue.struts.DailyTestForm;
import com.businessglue.util.CreateJDOMList;
import org.catechis.Domartin;
import com.businessglue.util.EncodeString;

/**
*<p>The Daily Test Result scores a daily test and forwards to the daily_test_result jsp.
*/
public class IntegratedTestResultAction extends Action
{
	
	/**
	*<p>This method scores a single reading or writing word test.
	*<p>
	*<p>The Momento Object has the following meanings:
	*<p>action_name = Name of the action used in the web app
	*<p>action_time = Time the action was initiated
	*<p>action_id   = uses this id to get the wntd object, which has a link to the wltd so it can erase those files and create new ones
	*<p>action_type = Extra info, such as the test type, or whatever the action chooses to use this info for
	*<p>
	*<p>
	*<p>AllWordsTest is the only object in the session and has the following member variables.
	*<p>data kept on xml file
	*<p>String text;
	*<p>String definition;
	*<p>String category;
	*<p>String test_type;
	*<p>String level;
	*<p>int daily_test_index;
	*<p>long id;
	*<p>This was used to store the users answer in the past.
	*<p>String answer;
	*/
	public ActionForward perform(ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_id = (String)session.getAttribute("user_id");
		String grand_index = (String)session.getAttribute("grand_index");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);	
		context.log("IntegratedTestResultAction.perform: user_id "+user_id+".");
		AllWordsTest awt = (AllWordsTest)session.getAttribute("awt_test_word"); // this object holds the original word tested
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		Hashtable jsp_options = getJSPOptions(user_id, "daily_test_result", "vocab", context_path, context); 
		String subject = (String)user_opts.get("subject");
		String max_level = (String)user_opts.get("max_level");
		String encoding = (String)user_opts.get("encoding");
		FileTestRecords ftr = new FileTestRecords(context_path);
		Momento old_m = ftr.getStatusRecord(user_id, subject);
		ftr.updateSessionStatus();
		String test_type = awt.getTestType();
		String test_name = new String("level "+awt.getLevel()+" "+test_type+".test");
		DailyTestForm test_form = (DailyTestForm)form; 			// get the test form with the answer
		String answer = EncodeString.encodeThis(test_form.getAnswer(),encoding);
		awt.setAnswer(answer);
		context.log("IntegratedTestResultAction.perform 2 test type "+test_type);
		// update the last/next test date lists
		FileUserOptions fuo = new FileUserOptions();
		Vector elt_vector = fuo.getELTVector(user_opts);	
		// exclude-level-times eliminate recently tested words from the list
		WordLastTestDates wltd = new WordLastTestDates();
		wltd.setExcludeLevelTimes(elt_vector);
		wltd.setType(test_type);
		wltd.setLimitOfWords(100); // probably don't need this for integrated tests.
		// get first word to test
		String [] files = wltd.getWNTFiles(user_id, context_path);
		String file = files[0];
		String file_w_ext = file;
		String file_wo_ext = Domartin.getFileWithoutExtension(file_w_ext);
		String file_date = Transformer.getLongDateFromMilliseconds(file_wo_ext);
		context.log("IntegratedTestResultAction.perform 3");
		WordTestResult wtr = store.scoreSingleWordTest(awt, user_id);
		printLog(store.getLog(), context); store.resetLog();
		wtr.setAnswer(answer); // this is done in store.scoreSingleWordTest in the Scorer object.
		context.log("IntegratedTestResultAction.perform: wtr.getAnswer "+wtr.getAnswer());
		context.log("IntegratedTestResultAction.perform: answer "+answer);
		wtr.setEncoding(encoding);
		String new_level = store.recordWordTestScore(wtr, awt, max_level, test_name, user_id); // this updates the words level
		context.log("IntegratedTestResultAction.perform: new_level "+new_level);
		context.log("IntegratedTestResultAction.perform: wtr.getOriginalLevel "+wtr.getOriginalLevel());
		context.log("IntegratedTestResultAction.perform: wtr.getLevel "+wtr.getLevel());
		String old_level = wtr.getOriginalLevel();
		if (!new_level.equals("0")&&!old_level.equals("0"))
		{
			context.log("IntegratedTestResultAction.perform: if (!new_level.equals(0)&&!old_level.equals(0))");
			try
			{
				ftr.updateWordLevels(old_level, new_level, test_type); // only do this if there is a change in levels
			} catch (java.lang.NullPointerException npe)
			{
				context.log("IntegratedTestResultAction.perform: updateWordLevels cuases an npe.  Level 4 anyone?");
			}
			context.log("IntegratedTestResultAction.perform: ftr log ++++++++++ ");
			printLog(ftr.getLog(), context);
		} else
		{
			context.log("else ( this didn't happed: (!new_level.equals(0)&&!old_level.equals(0))");
			//ftr.updateWordLevels(old_level, new_level, test_type); // only do this if there is a change in levels
			context.log("IntegratedTestResultAction.perform: ftr log ++++++++++ ");
			printLog(ftr.getLog(), context);
		}
		wtr.setLevel(new_level);
		Date date = new Date();
		long time = date.getTime();
		String str_date = date.toString();
		wtr.setDate(str_date);
		// next, add a record in the daily *type* tests.record file. 
		// this also removes the word from the new word list if it is a pass test, 
		// and of course if the word in on the new <type> words.list
		TestUtility tu = new TestUtility();
		tu.addDailyTestRecordIfNeeded(test_type, wtr, user_id, context_path, user_opts, awt.getId(), encoding);		
		context.log("IntegratedTestResultAction.perform 4");
		//WordTestResult wtr = new WordTestResult();
		//wtr = new WordTestResult();
		wtr.setWntdName(old_m.getActionId()+".xml");			
		// was file[0] in test, now action_id... seems messy to add the xml ext here...
		wtr.setWordId(awt.getId());
		TestTimeMemory ttm = wltd.updateTestDateRecordsAndReturnMemory(user_id, context_path, wtr);		
		// this will also save the list files for a possible undo
		context.log("IntegratedTestResultAction.perform: TestTimeMemory -------- ");
		printLog(Transformer.createTable(ttm), context);
		String org_wltd = Transformer.getDateFromMilliseconds(ttm.getOriginalWLTD());
		String org_wntd = Transformer.getDateFromMilliseconds(Domartin.getFileWithoutExtension(ttm.getOriginalWNTD()));
		String new_wntd = Transformer.getDateFromMilliseconds(ttm.getNewWNTD());
		context.log("IntegratedTestResultAction.perform: org_wltd "+org_wltd);
		context.log("IntegratedTestResultAction.perform: org_wntd "+org_wntd);
		context.log("IntegratedTestResultAction.perform: new_wntd "+new_wntd);
		files = wltd.getWNTFiles(user_id, context_path);
		RateOfTesting rot = new RateOfTesting();			// update ROT and get new values
		rot.updateRateOfTesting(test_type, awt.getLevel(), wtr.getGrade(), user_id, subject, context_path);
		Vector rot_vector = rot.getROTVector(test_type, user_id, subject, context_path);
		String rot_name = "rot_"+test_type+"_vector";
		WordNextTestDates wntd = new WordNextTestDates();
		int waiting_reading_tests = wntd.getWaitingTests(context_path, user_id, Constants.READING, subject).size();
		int waiting_writing_tests = wntd.getWaitingTests(context_path, user_id, Constants.WRITING, subject).size();
		String word_color = scoreWordByColor(store, awt.getId(), awt.getCategory(), user_id, awt.getCategory(), context_path);
		session.setAttribute("word_color", word_color);
		session.setAttribute("waiting_reading_tests", Integer.toString(waiting_reading_tests));
		session.setAttribute("waiting_writing_tests", Integer.toString(waiting_writing_tests));
		session.setAttribute("test_time_memory", ttm);
		session.setAttribute(rot_name, rot_vector);
		context.log("IntegratedTestResultAction.perform rot "+rot_name+" awt.getLevel() "
			+awt.getLevel()+" wtr.getGrade() "+wtr.getGrade());
		// should we merg awt into wtr?
		session.setAttribute("awt_test_word", awt);
		// session.setAttribute("wltd", wltd);
		session.setAttribute("wtr", wtr);
		context.log("IntegratedTestResultAction.perform 6");
		getAndSetMissedWords(session, context_path, user_id, jsp_options, test_type); //  if the word is failed asd is above level 0, the default behavior is to save a record in the .record file then set the missed words list in the session
		session.setAttribute("jsp_options", jsp_options);
		// create new momento object
		// we need to think about the actual meaning of the memento object, which is to facilitate states, undo, cancel, etc.
		// so a manager object needs to save a copy of these, as well as copies of the last/next list files
		// as well as new words/ missed words, etc...blah blah blah I want hot coco!
		Momento new_m = new Momento("IntegratedTestResultAction", Long.toString(new Date().getTime()),
			 "IntegratedTestResultAction", "default"); 
		// the third arg is set so that ChangeUpdateScore action knows where to return to after 
		// the user has clicked reverse two or more times.
		ftr.setMomentoObjectUsingSharedDoc(new_m);
		Hashtable last_record = ftr.getReadingAndWritingLevels();
		session.setAttribute("last_record", last_record);
		ftr.writeDocument();
		context.log("IntegratedTestResultAction.perform 7");
		// we should also have a counter for words left to test, grand index, etc, updated.
		// and if we are going to have lookup lists for level 0 words, then update the count from that,
		// otherwise update a copund in the record file.
		// regardless we should store the # of words at level 1,2,3 r & w
		// and update it here so that there is no need to regenerate such information.
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("integrated_test_result"));
	}
	
	private String scoreWordByColor(Storage store, long word_id, String category, String user_id, 
			String type_wanted, String context_path)
	{
		ServletContext context = getServlet().getServletContext();
		FileJDOMWordLists fjdomwl = new FileJDOMWordLists();
		Word word = fjdomwl.getSpecificWord(word_id+"", category, user_id, context_path);
		//Word word = store.getWordObject("id", word_id+"", category, user_name);
		context.log("IntegratedTestResultAction word ---");
		printLog(Transformer.createTable(word));
		context.log("IntegratedTestResultAction fjdomwl log ---");
		printLog(fjdomwl.getLog());
		Test tests [] = word.getTests();
		context.log("IntegratedTestResultAction tests "+tests.length);
		//Color color = ratePerformancebyLightColor(word, type_wanted);
		FarmingTools farming = new FarmingTools();
		try
		{
			Color color = farming.ratePerformancebyLightColor(word, type_wanted);
			int red = color.getRed();
			int gre = color.getGreen();
			int blu = color.getBlue();
			String color_string = "rgb("+red+","+gre+","+blu+")";
			return color_string;
		} catch (java.lang.NullPointerException npe)
		{
			context.log("IntegratedTestResultAction: npe");
			printLog(farming.getLog());
		} catch (java.lang.NumberFormatException nfe)
		{
			context.log("IntegratedTestResultAction: nfe");
			//printLog(farming.getLog());
		}
		printLog(farming.getLog());
		return "rgb(255,255,255)";
	}
	
	/*
	public Color ratePerformancebyLightColor(Word word, String type_wanted)
	{
		ServletContext context = getServlet().getServletContext();
		context.log("ratePerformancebyLightColor");
		int green = 200;
		int red = 200;
		int blue = 200;
		int increment = 1;
		Color colour = new Color(0,0,0);
		Test tests[] = word.getTests();
		context.log("ratePerformancebyLightColor: tests "+tests.length);
		String test_name = "";
		for (int i = 0;i<tests.length;i++)
		{
			try
			{
				Test test = tests[i];
				if (test == null)
				{
					context.log("ratePerformancebyLightColor: test "+i+" is null");
					continue;
				}
				context.log("ratePerformancebyLightColor: 1");
				test_name = test.getName();
				context.log("ratePerformancebyLightColor: name "+test_name);
				int level = Integer.parseInt(Domartin.getTestLevel(test_name));
				context.log("ratePerformancebyLightColor: level "+level);
				String type = Domartin.getTestType(test_name);
				context.log("ratePerformancebyLightColor: type "+type);
				String grade = test.getGrade();
				grade.trim();
				context.log("ratePerformancebyLightColor: grade "+grade+".");
				increment = level+increment;
				context.log("test "+test_name+" l "+level+" inc "+increment);
				if (type.equals(type_wanted))
				{
					if (grade.equals(Constants.PASS)||grade=="pass")
					{
						blue = blue + increment;
						blue = limitOver(blue, increment);
						green = green + increment;
						green = limitUnder(green, increment);
						context.log("ratePerformancebyLightColor: "+i+" pass - green "+green+" blue "+blue);
					} else if (grade.equals(Constants.FAIL)||grade=="fail")
					{
						blue = blue - increment;
						blue = limitUnder(blue, increment);
						green = green - increment;
						green = limitUnder(green, increment);
						context.log("ratePerformancebyLightColor: "+i+" fail - green "+green+" blue "+blue);
					}
				} 	
			} catch (java.lang.NullPointerException npe)
			{
				context.log("ratePerformancebyLightColor: npe at "+i+" "+test_name);
			}
		}
		colour = new Color(red,green,blue);
		context.log("FarmingTools.ratePerformancebyColor: "+word.getText()+" "+word.getDefinition()+" - red "+red+" green "+green+" blue "+blue+" increment "+increment);
		return colour;
	}
	
	private int limitOver(int color, int increment)
	{
		if (color>255)
		{
			color = 255;
		}
		return color;
	}
	
	private int limitUnder(int color, int increment)
	{
		if (color<0)
		{
			color = 0;
		}
		return color;
	}
	*/
	
	/**
	*This method should be moved into the interface for all actions, as all pages should let the user set options
	*It appears here and in DailyTestResultAction, and where else?
	*/
	private Hashtable getJSPOptions(String guest_id, String jsp_name, String subject, String current_dir, ServletContext context)
	{
		Hashtable options = new Hashtable();
		FileUserOptions fuo = new FileUserOptions(current_dir);
		try
		{
			context.log("IntegratedTestResultAction.getJSPOptions"+current_dir);
			options = fuo.getJSPOptions(guest_id, jsp_name, subject);
			context.log("IntegratedTestResultAction.getJSPOptions fuo log -----");
		} catch (java.lang.NullPointerException npe)
		{
			options.put("format_print_missed_reading", "true");
			options.put("format_print_missed_writing", "true");
			options.put("record_failed_tests", "true");
			options.put("record_passed_tests", "true");
			options.put("record_exclude_level", "0");
			options.put("record_limit", "17");
			options.put("word_lookup_for_pass", "false");
			options.put("word_lookup_for_fail", "false");
			context.log("getJSPOptions: npe - id "+guest_id+" jsp_name "+jsp_name+" dir "+ current_dir);
			context.log("getJSPOptions: npe - subject "+subject);
		}
		printLog(fuo.getLog(), context);
		return options;
	}
	
	/**
	*The jsp_options hold on/off values, such as printdon print missed words by type.
	*/
	private void getAndSetMissedWords(HttpSession session, String context_path, 
		String user_id, Hashtable jsp_options, String test_type)
	{
		ServletContext context = getServlet().getServletContext();
		context.log("IntegratedTestResultAction.getAndSetMissedWords");
		String format_print_missed_reading = (String)jsp_options.get("format_print_missed_reading");
		context.log("IntegratedTestResultAction.getAndSetMissedWords: format_print_missed_reading "+format_print_missed_reading);
		String format_print_missed_writing = (String)jsp_options.get("format_print_missed_writing");
		context.log("IntegratedTestResultAction.getAndSetMissedWords: format_print_missed_reading "+format_print_missed_writing);
		Hashtable r_words_defs = new Hashtable();
		Hashtable w_words_defs = new Hashtable();
		WordListFilter wlf = new WordListFilter();
		try
		{
			if (test_type.equals(Constants.READING) && format_print_missed_reading.equals("true"))
			{
				context.log("test_type.equals(Constants.READING) && format_print_missed_reading.equals(true)");
				r_words_defs = wlf.getMissedWords(Constants.READING, context_path, user_id, jsp_options);
				context.log("IntegratedTestResultAction.getAndSetMissedWords reading "+test_type);
				context.log("IntegratedTestResultAction.getAndSetMissedWords reading "+r_words_defs.size());
			}
			if (test_type.equals(Constants.WRITING) && format_print_missed_writing.equals("true"))
			{
				context.log("test_type.equals(Constants.WRITING) && format_print_missed_writing.equals(true)");
				w_words_defs = wlf.getMissedWords(Constants.WRITING, context_path, user_id, jsp_options);
				context.log("IntegratedTestResultAction.getAndSetMissedWords writing "+test_type);
				context.log("IntegratedTestResultAction.getAndSetMissedWords writing "+w_words_defs.size());
			}
		} catch (java.lang.NullPointerException npe)
		{
			context.log("IntegratedTestResultAction.getAndSetMissedWords casued an NPE!!!");
		}
		session.setAttribute("r_words_defs", r_words_defs);
		session.setAttribute("w_words_defs", w_words_defs);
	}
	
	/**
	*I guess we don't need this, as an incremented grand index gets set in 
	* AllWordsTest awt = TestUtility.setupAllWordsTest(word, action_type, grand_test_index);
	*
	private String getNewGrandIndex(String old_grand_index, String user_id, String subject, FileTestRecords ftr, ServletContext context)
	{
		int grand_index = 0;
		try
		{
			grand_index = ftr.getTestsStatus(user_id, subject);	// this gets the the grand total of tests
			context.log("DailyTestAction.perform: grand_index is "+grand_index);
		} catch (java.lang.NullPointerException npe)
		{
			//printLog(ftr.getLog(), context);
			context.log("DailyTestAction.perform: grand_index is null");
		}
		return grand_index;
	}
	*/
	
	/**
	*<p>record the test in daily *type* tests.record file
	*this ads an element to the daily *type* tests.record file.
	*<p>The WordTestRecordOption object holds all the on off switches
	* for what should be recorded in the file.  
	*<p>The FileTestRecords object does all the word of checking these
	* options and then saving a record in the apporpriate file.
	*/
	private void addDailyTestRecordIfNeeded(String type, 
		WordTestResult wtr, String user_id, String root_path, 
		Hashtable user_opts, long id, String encoding, ServletContext context)
	{
	    boolean record_failed_tests = Boolean.valueOf((String)user_opts.get("record_failed_tests")).booleanValue();
	    boolean record_passed_tests = Boolean.valueOf((String)user_opts.get("record_passed_tests")).booleanValue();
	    String record_exclude_level = (String)user_opts.get("record_exclude_level");
	    int record_limit = Integer.parseInt((String)user_opts.get("record_limit"));
	    WordTestRecordOptions wtro = new WordTestRecordOptions();
	    wtro.setType(type);
	    wtro.setUserName(user_id);
	    wtro.setRootPath(root_path);
	    wtro.setRecordFailedTests(record_failed_tests);
	    wtro.setRecordPassedTests(record_passed_tests);
	    wtro.setRecordExcludeLevel(record_exclude_level);
	    wtro.setRecordLimit(record_limit);
	    wtro.setWordId(id);
	    String pass_fail = wtr.getGrade();
	    if (pass_fail.equals("pass"))
	    {
		    FileJDOMWordLists fjdomwl =  new FileJDOMWordLists(root_path, Constants.READING, user_id);
		    try
		    {
			    fjdomwl.removeWordFromNewWordsList(Long.toString(id), encoding);
		    } catch (java.lang.NullPointerException npe)
		    {
			    // dont know why this should be null...
		    }
	    }
	    FileTestRecords ftr = new FileTestRecords(root_path);
	    ftr.setEncoding(encoding);
	    ftr.addDailyTestRecord(wtr, wtro);
	}
	
	/**
	<p>This method should be moved into the Storgae object,
	<p>The CreateJDOMList is legacy, so the getOptionsHash should be moved into
	<p>The JDOMSolution object in the Catechis package.
	*<p>If context is only used for debuggin, it should net be passed into the Storage object.
	*/
	private Hashtable getUserOptions(String user_id, String context_path, ServletContext context)
	{
		// from LoginAction.loginUser method
		String user_folder = (context_path+File.separator+"files"+File.separator+user_id);
		String file_name = new String(user_id+".options");
		File file_chosen = Domartin.createFileFromUserNPath(file_name, user_folder);
		String file_path = file_chosen.getAbsolutePath();
		Hashtable options = new Hashtable();
		// Hashtable user_opts = getOptions(file_chosen);
		
		// from LoginAction.getOptions method
		// ServletContext context = getServlet().getServletContext(); 
		CreateJDOMList jdom = new CreateJDOMList(file_chosen, context);
		options = jdom.getOptionsHash();
		return options;
	}
	
	// debuggin
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log((String)log.get(i));
			i++;
		}
	}
	
	private void printLog(Vector log)
	{
		ServletContext context = getServlet().getServletContext();
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log((String)log.get(i));
			i++;
		}
	}

	private void printLog(Hashtable log, ServletContext context)
	{
		for (Enumeration e = log.elements() ; e.hasMoreElements() ;) 
		{
			context.log((String)e.nextElement());
		}
	}

}
