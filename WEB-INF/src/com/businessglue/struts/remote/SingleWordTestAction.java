package com.businessglue.struts.remote;

import org.catechis.EncodeString;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
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
 * <p>This action is called from the Android application Stepping Stones known as Wherewithal.
*<p>This can be either a reading or a writing test.
*/
public class SingleWordTestAction extends Action
{
	
	/**
	*<p>Send the AllWordsTest object to the client for a test.
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
		String student_id = request.getParameter("student_id");
		context.log("SingleWordTestAction: student id "+student_id+" locale "+loc.toString());
		String context_path = context.getRealPath("/WEB-INF/");
		context.log("SingleWordTestAction.perform: context_path "+context_path);
		Storage store = new FileStorage(context_path, context);
		context.log("SingleWordTestAction.perform: 1");
		Hashtable student_opts = store.getUserOptions(student_id, context_path);
		context.log("SingleWordTestAction.perform: student_opts "+student_opts.size());
		String subject = Constants.VOCAB;
		try
		{
			subject = (String)student_opts.get("subject");
		} catch (java.lang.NullPointerException npe)
		{
			printLog(store.getLog(), context);
		}
		context.log("SingleWordTestAction.perform: 3");
		FileTestRecords ftr = new FileTestRecords(context_path);
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
			word = wntds.getNextTestWord(student_id, context_path, action_time, subject);
			context.log("IntegratedTestAction.perform: 4");
			category = wntds.getWordCategory();
			action_id = wntds.getWordsNextTextDateFileName();// link to wntd file, same as next ???
			wntd = wntds.getWordNextTestDate();	// the name of the next test date file
			action_type = wntds.getNextTestType();
		} catch (java.lang.NullPointerException npe)
		{
			context.log("SingleWordTestAction.perform: npe from wntd: wntd log --- ");
			printLog(wntds.getLog(), context);
		}
		int grand_test_index = ftr.getGrandIndex();
		context.log("SingleWordTestAction.perform: 5");
		int daily_test_index = ftr.getDailySessionTests();
		context.log("SingleWordTestAction.perform: 6");
		//ftr.setTestStartTime(); // this starts the timer between stating and ending a test (caused an npe)
		//ftr.writeDocument();
		AllWordsTest awt = TestUtility.setupAllWordsTest(word, action_type, grand_test_index);
		context.log("SingleWordTestAction.perform: 7");
		String time_til_next_allowed_test = null;
		if (word.getText().equals(Constants.NA)||word.getDefinition().equals(Constants.NA))
		{
			time_til_next_allowed_test = timeTilNextAllowedTest(word, action_time, student_id, subject,
					ftr, request, response, student_opts, mapping, session); // no words left to test
			awt.setAnswer(time_til_next_allowed_test);
			context.log("SingleWordTestAction.perform: 8");
		}
		I18NWebUtility.forceContentTypeAndLocale(request, response, student_opts);
		xmlResponse(response, awt, grand_test_index);
		return (mapping.findForward("single_word_test"));
	}
	
	private void xmlResponse(HttpServletResponse response, AllWordsTest awt, int grand_test_index)
	{
		ServletContext context = getServlet().getServletContext(); 
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<all_words_test>");
		sb.append("<text>"+EncodeString.encodeThis(awt.getText(),"UTF-8")+"</text>");
		sb.append("<definition>"+EncodeString.encodeThis(awt.getDefinition(),"UTF-8")+"</definition>");
		sb.append("<category>"+awt.getCategory()+"</category>");
		sb.append("<answer>"+awt.getAnswer()+"</answer>");
		context.log("IntegratedTestAction.perform: 9");
		sb.append("<test_type>"+awt.getTestType()+"</test_type>");
		sb.append("<level>"+awt.getLevel()+"</level>");
		sb.append("<daily_test_index>"+awt.getDailyTestIndex()+"</daily_test_index>");
		sb.append("<grand_test_index>"+grand_test_index+"</grand_test_index>");
		sb.append("<id>"+awt.getId()+"</id>");
		sb.append("</all_words_test>");
		response.setContentType("text/xml");
		response.setContentLength(sb.length()); 
		PrintWriter out;
		try 
		{
		    out = response.getWriter();
			out.println(sb.toString()); 
			out.close();
			out.flush();
		} catch (IOException e) 
		{
			context.log("Could not steal output stream");
		}
	}
	
	private String timeTilNextAllowedTest(Word word, String action_time, String user_id, String subject,
			FileTestRecords ftr, HttpServletRequest request, HttpServletResponse response, 
			Hashtable user_opts, ActionMapping mapping, HttpSession session)
	{
		ServletContext context = getServlet().getServletContext();
		String action_id = "000";
		String action_type = "000";
		Momento new_m = new Momento("SingleWordTestAction.Wait", action_time, action_id, action_type);
		ftr.setMomentoObject(user_id, subject, new_m);
		String time_til_next_allowed_test = Domartin.getElapsedTime(word.getDateOfEntry()+"");
		context.log("SingleWordTestAction.perform: user has to wait "+time_til_next_allowed_test);
		session.setAttribute("time_til_next_allowed_test", time_til_next_allowed_test);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return time_til_next_allowed_test;
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
			context1.log("SingleWordTestAction.incrementDailyTestIndex is null");
		}
		return Integer.toString(daily_test_index);
	}
			
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("SingleWordTestAction.log "+(String)log.get(i));
			i++;
		}
	}

}
