package com.businessglue.struts;

import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.Transformer;
import org.catechis.WordTestDateUtility;
import org.catechis.dto.Word;
import org.catechis.dto.Momento;
import org.catechis.dto.AllWordsTest;
import org.catechis.dto.WordTestDates;
import org.catechis.dto.WordTestResult;
import org.catechis.dto.WordTestMemory;
import org.catechis.dto.WordLastTestDates;
import org.catechis.dto.WordTestRecordOptions;
import org.catechis.file.FileTestRecords;
import org.catechis.file.FileJDOMWordLists;
import org.catechis.file.WordNextTestDates;
import org.catechis.admin.FileUserOptions;
import org.catechis.constants.Constants;
import org.catechis.filter.WordListFilter;
import org.catechis.juksong.TestTimeMemory;

import com.businessglue.util.Domartin;
import com.businessglue.util.EncodeString;
import com.businessglue.util.I18NWebUtility;
import com.businessglue.util.CreateJDOMList;
import com.businessglue.struts.AllWordsTestForm;

import java.io.File;
import java.util.Date;
import java.util.Vector;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;

/**
*<p>The DailY Test Result scores a daily test and forwards to the daily_test_result jsp.
* @author Timothy Curchod
*/
public class DailyTestResultAction extends Action
{
	
	/**
	*<p>This method scores a single word test and returns the results for the jsp.
	*<p>
	WordTestResult includes the Strings text, definition, answer, grade, level,
	original_text, original_definition, original_level and an int id.
		
	AllWordsTest includes Strings text, definition, category, test_type
		level and the user answer.
	*/
	public ActionForward perform(ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);	context.log("DailyTestResultAction.perform for "+user_id);
		ArrayList key_list = (ArrayList)session.getAttribute("actual_key_list"); // if this is null then we create it next
		WordLastTestDates wltd = (WordLastTestDates)session.getAttribute("wltd"); // the list of words and their key associations
		AllWordsTest awt = (AllWordsTest)session.getAttribute("awt_test_word");	// this object holds the original word tested
		String grand_index = (String)session.getAttribute("grand_index");	// this is the grand number of tests the student has ever taken
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		Hashtable jsp_options = getJSPOptions(user_id, "daily_test_result", "vocab", context_path, context); // user name, as of now, is an long id, where it used to be the persons actual name to a folder with their name.
		String subject = "vocab";
		String max_level = (String)user_opts.get("max_level");
		String encoding = (String)user_opts.get("encoding");
		String test_level = awt.getLevel();
		String test_type = awt.getTestType();
		String test_name = new String("level "+test_level+" "+test_type+".test");
		DailyTestForm test_form = (DailyTestForm)form; 			// test form
		String answer = EncodeString.encodeThis(test_form.getAnswer(),encoding);
		awt.setAnswer(answer);
		WordTestResult wtr = store.scoreSingleWordTest(awt, user_id);
		printLog(store.getLog(), context);
		store.resetLog();
		wtr.setAnswer(answer);
		wtr.setEncoding(encoding);
		String new_level = store.recordWordTestScore(wtr, awt, max_level, test_name, user_id); // this updates the words level
		wtr.setLevel(new_level);
		String date = new Date().toString();
		wtr.setDate(date);
		int total_words = 0;
		Long key = (Long)key_list.get(0);				// get key for word tested
		wltd.removeWord(key);						// remove the word object from the store of words
		key_list.remove(key);						// remove the key from the key list
		addDailyTestRecordIfNeeded(test_type, wtr, user_id, context_path, user_opts, awt.getId(), encoding, context);		// adds a record in the daily *type* tests.record file. this also removes the word from the new word list if it is a pass test, and of course if the word in on the new <type> words.list
		FileTestRecords ftr = new FileTestRecords(context_path);	
		context.log("DailyTestResultAction.perform wtr ============= word_test_result.getWordId() "+wtr.getWordId()); 
		printLog(Transformer.createTable(wtr), context);
		ftr.updateTestsStatus(user_id, subject);
		setMomento(ftr, user_id, subject, "DailyTestResultAction", null, null);
		removeWordFromNewWordsList(context_path, wtr.getWordId(), test_type, encoding, user_id, context);
		// this comes from the more recent IntegratedTestResultAction, and is for backwards compatability
		FileUserOptions fuo = new FileUserOptions(context_path);
		Vector elt_vector = fuo.getELTVector(user_opts);
		wltd = new WordLastTestDates();
		wltd.setExcludeLevelTimes(elt_vector);
		wltd.setType(test_type);
		wltd.setLimitOfWords(100); // probably don't need this for integrated tests.
		TestTimeMemory ttm = wltd.updateTestDateRecordsAndReturnMemory(user_id, context_path, wtr);
		session.setAttribute("test_time_memory", ttm);
		// original session objects
		session.setAttribute("jsp_options", jsp_options);
		session.setAttribute("awt_test_word", awt);
		session.setAttribute("actual_key_list", key_list);
		session.setAttribute("wltd", wltd);
		session.setAttribute("wtr", wtr);
		getAndSetMissedWords(session, context_path, user_id, jsp_options, test_type);
		I18NWebUtility.setContentTypeAndLocale(request, response);
		String word_lookup_for_pass = (String)jsp_options.get("word_lookup_for_pass");
		String word_lookup_for_fail = (String)jsp_options.get("word_lookup_for_fail");
		if ((word_lookup_for_pass.equals("true") && wtr.getGrade().equals("pass"))||(word_lookup_for_fail.equals("true")&&wtr.getGrade().equals("fail")))
		{
			return (mapping.findForward("daily_test_frameset"));
		}
		return (mapping.findForward("daily_test_result"));
	}
	
	/**
	*The jsp_options hold on/off values, such as printdon print missed words by type.
	*/
	private void getAndSetMissedWords(HttpSession session, String context_path, 
		String user_id, Hashtable jsp_options, String test_type)
	{
		String format_print_missed_reading = (String)jsp_options.get("format_print_missed_reading");
		String format_print_missed_writing = (String)jsp_options.get("format_print_missed_writing");
		Hashtable r_words_defs = new Hashtable();
		Hashtable w_words_defs = new Hashtable();
		WordListFilter wlf = new WordListFilter();
		if (test_type.equals(Constants.READING) && format_print_missed_reading.equals("true"))
		{
			r_words_defs = wlf.getMissedWords(Constants.READING, context_path, user_id, jsp_options);
		}
		if (test_type.equals(Constants.WRITING) && format_print_missed_writing.equals("true"))
		{
			w_words_defs = wlf.getMissedWords(Constants.WRITING, context_path, user_id, jsp_options);
		}
		session.setAttribute("r_words_defs", r_words_defs);
		session.setAttribute("w_words_defs", w_words_defs);
	}
	
	/**
	*We moved this to FileTestRecords.
	*<p>record the test in daily *type* tests.record file
	*this ads an element to the daily *type* tests.record file.
	*<p>The WordTestRecordOption object holds all the on off switches
	* for what should be recorded in the file.  
	*<p>The FileTestRecords object does all the word of checking these
	* options and then saving a record in the appropriate file.
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
		    removeWordFromNewWordsList(root_path, id, type, encoding, user_id, context);
	    }
	    FileTestRecords ftr = new FileTestRecords(root_path);
	    ftr.setEncoding(encoding);
	    ftr.addDailyTestRecord(wtr, wtro);
	}
	
	/**
	<p>This method should be moved into the Storage object,
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
	
	private void removeWordFromNewWordsList(String context_path, long search_id, String type, 
			String encoding, String user_id, ServletContext context)
	{
		context.log("search id "+search_id);
		if (type.equals(Constants.READING))
		{
			FileJDOMWordLists fjdomwl =  new FileJDOMWordLists(context_path, Constants.READING, user_id, "vocab");
			printLog(fjdomwl.getLog(), context); 
			//fjdomwl.resetLog();
			fjdomwl.removeWordFromNewWordsList(Long.toString(search_id), encoding);
			printLog(fjdomwl.getLog(), context);
		} else
		{
			FileJDOMWordLists fjdomwl =  new FileJDOMWordLists(context_path, Constants.WRITING, user_id, "vocab");
			printLog(fjdomwl.getLog(), context);
			// fjdomwl.resetLog();
			fjdomwl.removeWordFromNewWordsList(Long.toString(search_id), encoding);
			printLog(fjdomwl.getLog(), context);
		}
	}
	
	/**
	*This method should be moved into the interface for all actions, as all pages should let the user set options
	*/
	private Hashtable getJSPOptions(String guest_id, String jsp_name, String subject, String current_dir, ServletContext context)
	{
		Hashtable options = new Hashtable();
		FileUserOptions fuo = new FileUserOptions(current_dir);
		try
		{
			context.log("DailyTestResultAction.getJSPOptions"+current_dir);
			options = fuo.getJSPOptions(guest_id, jsp_name, subject);
			context.log("DailyTestResultAction.getJSPOptions fuo log -----");
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
	
	private void setMomento(FileTestRecords ftr, String user_id, String subject, String action_name, String action_id, String action_type)
	{
			String action_time = Long.toString(new Date().getTime());
			Word word = new Word();
			if (action_id == null)
			{
				action_id = "000";
			}
			if (action_type == null)
			{
				action_type = "000";
			}
			Momento new_m = new Momento(action_name, action_time, action_id, action_type);
			ftr.setMomentoObject(user_id, subject, new_m);
	}
	
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
	
	private void printLog(Hashtable log, ServletContext context)
	{
		for (Enumeration e = log.elements() ; e.hasMoreElements() ;) 
		{
			context.log((String)e.nextElement());
		}
	}

	/*
	try
		{
			max_level = (String)user_opts.get("max_level");
		} catch (java.lang.NullPointerException npe)
		{
			max_level =new String("3");
			context.log("DailyTestResultAction.perform max_level is null");
		}
		*/
	
	
}
