package com.businessglue.struts;

import java.util.Date;
import java.util.Vector;
import java.lang.Integer;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.servlet.http.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;
import org.catechis.Storage;
import org.catechis.Transformer;
import org.catechis.FileStorage;
import org.catechis.EncodeString;
import org.catechis.file.ROTFileStorage;
import org.catechis.file.FileTestRecords;
import org.catechis.constants.Constants;
import org.catechis.dto.Momento;
import org.catechis.dto.AllWordsTest;
import org.catechis.dto.ChangeTestForm;
import org.catechis.dto.WordTestMemory;
import org.catechis.dto.WordTestResult;
import org.catechis.dto.WordTestRecordOptions;
//import com.businessglue.util.I18NWebUtility;
import org.catechis.I18NWebUtility;
import org.catechis.juksong.ThePatches;
import org.catechis.juksong.TestTimeMemory;
import org.catechis.admin.FileUserOptions;

/**
*<p>See perform action for notes.
*@author Timothy Curchod
*/
public class ChangeUpdateScoreAction extends Action
{
	
	/**
	*<p>We have to take the request parameter index=particular test word, retrieve
	* the save WordTestResult with using the index/id, and populate the WordTestForm
	* with the word and test particulars.
	*<p>This object originated in ChangeUpdateAction.
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		ServletContext context = getServlet().getServletContext();
		HttpSession session = request.getSession();
		context.log("ChangeUpdateScoreAction.perform");
		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);	
		Hashtable user_opts = new Hashtable();
		try
		{
			user_opts = store.getUserOptions(user_id, context_path);
		} catch (java.lang.NullPointerException npe)
		{
			dumpLog(store.getLog(), context);
		}
		String subject = (String)user_opts.get("subject");
		FileTestRecords ftr = new FileTestRecords(context_path);
		Momento old_m = updateLevel(request, response, context, session);
		String action_name = old_m.getActionName();
		String action_id = old_m.getActionId(); // this is to carry on any info set by the likes on IntegratedTestAction
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		context.log("ChangeUpdateScoreAction.perform: action_name "+action_name);
		if (action_name.equals("IntegratedTestResultAction")||action_id.equals("ChangeUpdateScoreAction")||action_id==null)
		{
			return (mapping.findForward("integrated_test_result")); // change came from IntegreatedTestResultAction
		}
		return (mapping.findForward("daily_test_result")); // change came from DailyTestResultAtion
	}
	
	/**
	*<p>Here goes
	WordTestMemory
	private String category;  	***
	private String type;		***
	private String date;		***
	private String score;		SET LATer
	private String index;		*** -1
	private String number_correct;	?
	private String level;		?
	private String test_name;	needed 
	long word_id
	
	<p>WordTestResult
	private String text;
	private String definition;
	private String answer;
	private String grade;
	private String level;
	private int id;
	private String original_text;
	private String original_definition;
	private String original_level;
	private String encoding;
	private String date;
	long word_id
	
	Objects already in the session:
	ArrayList key_list = (ArrayList)session.getAttribute("actual_key_list"); // if this is null then we create it next
	WordLastTestDates wltd = (WordLastTestDates)session.getAttribute("wltd"); // the list of words and their key assosiations
	
	DailyTestAction attributes set:
	session.setAttribute("daily_test_index", "0")
	* session.setAttribute("awt_test_word", awt_test_word);
	//session.setAttribute("actual_key_list", key_list);
	//session.setAttribute("wltd", wltd);
	
	* session.setAttribute("awt_test_word", awt);
	session.setAttribute("actual_key_list", key_list);
	session.setAttribute("wltd", wltd);
	* session.setAttribute("wtr", wtr);
	
	objects actually needed in the daily_test_result.jsp:
	AllWordsTest awt = (AllWordsTest)session.getAttribute("awt_test_word");
	WordTestResult wtr = (WordTestResult)session.getAttribute("wtr");
	(String)session.getAttribute("daily_test_index")
	*/
	private Momento updateLevel(HttpServletRequest request, HttpServletResponse response, 
		ServletContext context, HttpSession session)
	{
		//String user_name = (String)session.getAttribute("user_name");
		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		context.log("ChangeUpdateScoreAction.updateLevel: user_opts --------");
		dumpHash(user_opts, context);
		
		String encoding = (String)user_opts.get("encoding");
		AllWordsTest awt = null;
		awt = (AllWordsTest)session.getAttribute("awt_test_word");
		context.log("ChangeUpdateScoreAction.updateLevel: AllWordsTest --------");dumpLog(Transformer.createTable(awt), context);
		String test_type = awt.getTestType();
		WordTestResult word_test_result = (WordTestResult)session.getAttribute("wtr"); // even I don't know what the id wtr member is.
		String previous_level = word_test_result.getOriginalLevel();
		String level_before_reverse = word_test_result.getLevel();
		context.log("ChangeUpdateScoreAction.updateLevel: word_test_results -------- previous level "+previous_level);		dumpLog(Transformer.createTable(word_test_result), context);
		String grade = word_test_result.getGrade();
		context.log("ChangeUpdateScoreAction.updateLevel: 1");
		WordTestMemory word_test_memory = new WordTestMemory();
		word_test_memory.setType(awt.getTestType()); // from AllWordsTest
		context.log("ChangeUpdateScoreAction.updateLevel: 2");
		// word_test_memory.setType(test_type);  // from the session
		word_test_memory.setCategory(awt.getCategory());
		word_test_memory.setDate(word_test_result.getDate()); // this is used to locate the test in test.records
		word_test_memory.setIndex("-1");
		String test_name = ("level "+word_test_result.getLevel()+" "+awt.getTestType()+".test");
		word_test_memory.setTestName(test_name);	
		context.log("ChangeUpdateScoreAction.updateLevel: wtm --------");dumpLog(Transformer.createTable(word_test_memory), context);
		WordTestRecordOptions wtro = new WordTestRecordOptions(test_type, user_id, context_path, 
		user_opts, word_test_result.getWordId());
		String org_text = EncodeString.encodeThis(word_test_result.getOriginalText(), encoding);
		String org_def  = EncodeString.encodeThis(word_test_result.getOriginalDefinition(), encoding);
		String new_word_level = word_test_result.getOriginalLevel();   // set default
		context.log("new_word_level (never used from word_test_result.getOriginalLevel()) "+new_word_level);
		word_test_result  = reverseGrade(word_test_result, word_test_memory, 
		store, user_id, grade, encoding, context); // modifies the word file
		word_test_result.setWordId(awt.getId());
		wtro.setWordId(word_test_result.getWordId());
		wtro.setType(test_type);
		wtro.setUserName(user_id);	// not sure why these were comming up null...oh, something is not getting the ids in JDOMSolution...
		FileTestRecords ftr = new FileTestRecords(context_path);
		ftr.setEncoding(encoding);
		ftr.reverseTestRecord(word_test_result, wtro);	// delete or add a daily test.records entry
		String new_level = word_test_result.getLevel();
		Momento old_m = ftr.getStatusRecord(user_id, Constants.VOCAB); // now the status record is available, which include the word levels.
		ftr.updateWordLevels(level_before_reverse, new_level, test_type);
		dumpLog(ftr.getLog(), context);
		// for the rot reverse
		FileUserOptions fuo = new FileUserOptions(context_path);
		Vector elt_vector = fuo.getELTVector(user_opts);
		TestTimeMemory old_ttm = (TestTimeMemory)session.getAttribute("test_time_memory");
		TestTimeMemory new_ttm = revrseUpdateTestDateRecords(user_id, awt.getCategory(), (String)user_opts.get("subject"), 
		 	old_ttm, word_test_result, context_path, elt_vector);
            	session.setAttribute("test_time_memory", new_ttm);
		session.setAttribute("wtr", word_test_result);
		context.log("ChangeUpdateScoreAction.updateLevel: new level "+new_level);
		Hashtable last_record = ftr.getReadingAndWritingLevels();
		String action_id = old_m.getActionId();
		Momento new_m = new Momento("IntegratedTestResultAction", Long.toString(new Date().getTime()), action_id, "ChangeUpdateScoreAction");
		ftr.setMomentoObjectUsingSharedDoc(new_m);;
		session.setAttribute("last_record", last_record);
		ftr.writeDocument();
		return old_m; // so that we can branch based on where we came from
	}
	
	private TestTimeMemory revrseUpdateTestDateRecords(String user_id, String category,
            String subject, TestTimeMemory old_ttm, WordTestResult wtr, String context_path,
            Vector elt_vector)
        {
        	ThePatches tp = new ThePatches();
        	TestTimeMemory new_ttm = tp.revrseUpdateTestDateRecords(user_id, category, subject, 
        	old_ttm, wtr, context_path, elt_vector);
            return new_ttm;
        }
	
	private void updateDailyTestsRecords(String context_path, WordTestResult wtr, 
			WordTestRecordOptions wtro, String encoding, ServletContext context)
	{
		context.log("XhangeUpdateScoreAction.updateDailyTestsRecords ---------- wtr "); 
		dumpLog(Transformer.createTable(wtr), context);
		context.log("XhangeUpdateScoreAction.updateDailyTestsRecords ---------- wtr0");
		dumpLog(Transformer.createTable(wtro), context);
		FileTestRecords ftr = new FileTestRecords(context_path);
		ftr.setEncoding(encoding);
		ftr.reverseTestRecord(wtr, wtro);	// delete or add a daily test.records entry
		context.log("XhangeUpdateScoreAction.updateDailyTestsRecords ---------- log ");
		dumpLog(ftr.getLog(), context);
	}
	
	/**
	*<p>
	*<p>This method finds the pass or fail score of the particular word, and has to change
	* both the words reading or writing level and the overall percentage score which needs
	* to be re-calculated from the number of words tested, and the number of words correct
	* held in the i_score variable.
	*<p>
	*/
	private WordTestResult reverseGrade(WordTestResult word_test_result, WordTestMemory word_test_memory, 
		Storage store, String user_name, String grade, String encoding, ServletContext context)
	{
			word_test_memory.setScore("-1");
			context.log("XhangeUpdateScoreAction.perform reverseGrade");
			if (grade.equals("pass"))
			{
				word_test_result.setGrade("fail");
			}
			else
			{
				word_test_result.setGrade("pass");
			}
			context.log("XhangeUpdateScoreAction.perform reverseGrade 1");
		String new_word_level = store.updateWordLevel(word_test_memory, word_test_result, user_name, encoding);
		context.log("XhangeUpdateScoreAction.perform reverseGrade 2");
		word_test_result.setLevel(new_word_level);
		return word_test_result;
	}
	
	// ----- Debugging methods ----------------------------
	
	private void dumpLog(Vector log, ServletContext context)
	{
	    int i = 0;
	    while (i<log.size())
	    {
		    context.log("ChangeUpdateAction.log "+i+" "+log.get(i));
		    i++;
	    }
	}
	
	private void dumpHash(Hashtable hash, ServletContext context)
	{
	    Enumeration keys = hash.keys();
	    while (keys.hasMoreElements())
	    {
		    String key = (String)keys.nextElement();
		    String val = (String)hash.get(key);
		    context.log("ChangeUpdateAction.log "+key+" - "+val);
	    }
	}

}

			/*
			We replaced the test_fields Vector with a text_answers Hashtable.
			
			This was really frustrating.  After getting stuck on this for a few weeks
			we commented it out.  It turned out the way the test_results jsp matches
			answers (in the test_fields vector) with words_defs cannot be re-created
			easily.
			
			Hashtable test_file = (Hashtable)session.getAttribute("words_defs");
			Hashtable word_grades = (Hashtable)session.getAttribute("word_grades");
			Vector test_fields = (Vector)session.getAttribute("test_fields");
			Enumeration keys = test_file.keys();
		
			reading
			=======
			String original = new String((String)test_fields.elementAt(test_i));
			String key = (String)keys.nextElement();
			String value = (String)test_file.get(key);
			String word_grade = (String)word_grades.get(value);
			
			writing
			=======
			String key = (String)keys.nextElement();
			String value = (String)test_file.get(key);
			
			if (value.equalsIgnoreCase(original)) red / green
			test_file(key)=value == test_fields.elementAt(test_i);
			
			if (test_type.equals("reading"))
			{
				Hashtable results = addAnswer(test_file, test_fields, session, word_test_result, word_test_memory, 
				org_text, store, text, encoding, definition, answer, user_name);
				word_test_result = (WordTestResult)results.get("wtr");
				test_file = (Hashtable)results.get("tf");
			} else
			{
				// the text element gets un-encoded, so until we can solve this,
				// only reading tests are modifiable
			}
			*/
