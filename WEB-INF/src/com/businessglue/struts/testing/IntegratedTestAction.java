package com.businessglue.struts.testing;

import org.catechis.Storage;
import org.catechis.Domartin;
import org.catechis.FileStorage;
import org.catechis.Transformer;
import org.catechis.JDOMSolution;
import org.catechis.WordTestDateUtility;
import org.catechis.dto.Word;
import org.catechis.dto.Momento;
import org.catechis.dto.AllWordsTest;
import org.catechis.dto.WordTestDates;
import org.catechis.dto.WordTestResult;
import org.catechis.dto.WordNextTestDate;
import org.catechis.file.WordNextTestDates;
import org.catechis.dto.WordLastTestDates;
import org.catechis.file.FileTestRecords;
import org.catechis.testing.TestUtility;
import org.catechis.I18NWebUtility;
import org.catechis.gwangali.RateOfTesting;
import org.catechis.constants.Constants;

import java.io.File;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;
import java.util.Hashtable;
import java.util.ArrayList;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// if the word is null
import org.catechis.dto.WordTestResult;
import org.catechis.dto.WordLastTestDates;
import org.catechis.admin.FileUserOptions;
import org.catechis.juksong.TestTimeMemory;

/**
*<p>This can be either a reading or a writing test.
*/
public class IntegratedTestAction extends Action
{
	
	/**
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
		Locale loc = request.getLocale();
		String user_id = (String)session.getAttribute("user_id");
		context.log("IntegratedTestAction.perform: user id "+user_id+" locale "+loc.toString());
		String context_path = context.getRealPath("/WEB-INF/");
		context.log("IntegratedTestAction.perform: context_path "+context_path);
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = null;
		try
		{
			user_opts = store.getUserOptions(user_id, context_path);
		} catch (java.lang.NullPointerException npe)
		{
			// this could happen if a user uses IE and has cookie settings no allowing session cookies.
			context.log("IntegratedTestAction.perform: store.getUserOptions npe --------------------");
			printLog(store.getLog(), context);
		}
		context.log("IntegratedTestAction.perform: store.getUserOptions store log -----------------");
		printLog(store.getLog(), context);
		String subject = (String)user_opts.get("subject");
		FileTestRecords ftr = new FileTestRecords(context_path);
		Momento old_m = ftr.getStatusRecord(user_id, subject);
		if (old_m.getActionName().equals("IntegratedTestAction"))
		{
			// double click, so don't erase old word?
			context.log("IntegratedTestAction.perform: double click?  or user left this action last time...  Momento already says IntegratedTestAction");
		} 
		// load a new word
		String action_time = Long.toString(new Date().getTime());
		WordNextTestDates wntds = new WordNextTestDates();
		Word word = new Word();
		String action_id = "000";
		String action_type = "000";
		WordNextTestDate wntd = new WordNextTestDate();
		String category = "";
		try
		{
			word = wntds.getNextTestWord(user_id, context_path, action_time, subject);
			category = wntds.getWordCategory();
			action_id = wntds.getWordsNextTextDateFileName();// link to wntd file, same as next ???
			wntd = wntds.getWordNextTestDate();	// the name of the next test date file
			action_type = wntds.getNextTestType();
			context.log("IntegratedTestAction.perform: word -------------------- ");
			printLog(Transformer.createTable(word), context);
			context.log("IntegratedTestAction.perform: wntds -------------------- log");
			printLog(wntds.getLog(), context);
			context.log("IntegratedTestAction.perform: wntds -------------------- log");
		} catch (java.lang.NullPointerException npe)
		{
			context.log("IntegratedTestAction.perform: npe from wntd: wntd log --- ");
			printLog(wntds.getLog(), context);
		}
		Momento new_m = new Momento("IntegratedTestAction", action_time, action_id, action_type);
		if (word.getText().equals(Constants.NA)||word.getDefinition().equals(Constants.NA))
		{
			context.log("IntegratedTestAction.perform: no words left to test: word.getText().equals(Constants.NA)||word.getDefinition().equals(Constants.NA) ");
			return (timeTilNextAllowedTest(word, action_time, user_id, subject,
					ftr, request, response, user_opts, mapping, session)); // no words left to test
		}
		ftr.setMomentoObjectUsingSharedDoc(new_m);
		int grand_test_index = ftr.getGrandIndex();
		int daily_test_index = ftr.getDailySessionTests();
		ftr.setTestStartTime(); // this starts the timer between stating and ending a test 
		ftr.writeDocument();
		RateOfTesting rot = new RateOfTesting();			// get ROT values
		Vector rot_reading_vector = rot.getROTVector(Constants.READING, user_id, subject, context_path);
		Vector rot_writing_vector = rot.getROTVector(Constants.WRITING, user_id, subject, context_path);
		AllWordsTest awt = TestUtility.setupAllWordsTest(word, action_type, grand_test_index);
		context.log("AllWordsTest --------"); 
		printLog(Transformer.createTable(awt), context);
		session.setAttribute("category", category);
		session.setAttribute("rot_reading_vector", rot_reading_vector);
		session.setAttribute("rot_writing_vector", rot_writing_vector);
		session.setAttribute("daily_test_index", Integer.toString(daily_test_index));
		session.setAttribute("grand_index", Integer.toString(grand_test_index));
		session.setAttribute("awt_test_word", awt);
		context.log("IntegratedTestAction.perform: new word loaded ");
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		if (word.getId() == 0)
		{
			return (mapping.findForward("integrated_test_null_id"));
		}
		return (mapping.findForward("integrated_test"));
	}
	
	private ActionForward timeTilNextAllowedTest(Word word, String action_time, String user_id, String subject,
			FileTestRecords ftr, HttpServletRequest request, HttpServletResponse response, 
			Hashtable user_opts, ActionMapping mapping, HttpSession session)
	{
		ServletContext context = getServlet().getServletContext();
		String action_id = "000";
		String action_type = "000";
		Momento new_m = new Momento("IntegratedTestActionWait", action_time, action_id, action_type);
		ftr.setMomentoObject(user_id, subject, new_m);
		String time_til_next_allowed_test = Domartin.getElapsedTime(word.getDateOfEntry()+"");
		context.log("IntegratedTestAction.perform: user has to wait "+time_til_next_allowed_test);
		session.setAttribute("time_til_next_allowed_test", time_til_next_allowed_test);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("integrated_test_time_til_next_allowed_test"));
	}
	
	private String incrementDailyTestIndex(HttpSession session)
	{
		int daily_test_index = 0;
		try
		{
			String dti = (String)session.getAttribute("daily_test_index");
			daily_test_index = Integer.parseInt(dti);
			daily_test_index = daily_test_index + 1;
		} catch (java.lang.NumberFormatException nfe)
		{
			ServletContext context1 = getServlet().getServletContext();
			context1.log("IntegratedTestAction.incrementDailyTestIndex is null");
		}
		return Integer.toString(daily_test_index);
	}
			
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("IntegratedTestAction.log "+(String)log.get(i));
			i++;
		}
	}

}
