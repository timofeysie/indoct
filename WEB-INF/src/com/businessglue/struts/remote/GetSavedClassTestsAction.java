package com.businessglue.struts.remote;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.admin.FileUserUtilities;
import org.catechis.admin.FileUserOptions;
import org.catechis.constants.Constants;
import org.catechis.dto.SavedTest;
import org.catechis.dto.UserInfo;
import org.catechis.file.FileSaveTests;

import com.businessglue.indoct.IndoctAction;
import com.businessglue.struts.admin.AddUsersOptionsForm;
import com.businessglue.struts.AddWordForm;
import com.businessglue.util.EncodeString;

/**
*<p>This class receives the test id and returns a list of students who are singed up for that test.
*  The jsp saved_tests_for_classes has all the info about the tests and a link to each student's version.
 * @author Timothy Curchod
*/
public class GetSavedClassTestsAction extends IndoctAction
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		context.log("GetSavedClassTestsAction.perform");
		//String teacher_id = (String)session.getAttribute("teacher_id");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		String test_id = request.getParameter("test_id");
		String teacher_id = request.getParameter("teacher_id");
		String password = request.getParameter("pass");
		context.log("GetSavedClassTestsAction.perform: test_id "+test_id);
		context.log("GetSavedClassTestsAction.perform: teacher_id "+teacher_id);
		context.log("GetSavedClassTestsAction.perform: password "+password);
		//Hashtable teacher_opts = store.getTeacherOptions(teacher_id, context_path);
		//String encoding = (String)teacher_opts.get("encoding");
		String subject = Constants.VOCAB;
		FileSaveTests f_save_tests = new FileSaveTests();
  		UserInfo user_info = new UserInfo("euc-kr", context_path, teacher_id, subject);
  		FileUserUtilities fut = new FileUserUtilities(context_path);
  		SavedTest saved_test = new SavedTest();
  		try
  		{
  			saved_test = f_save_tests.loadClassTest(user_info, test_id);
  		} catch (java.lang.NullPointerException npe)
  		{
  			printLog(f_save_tests.getLog(), context, "f_save_tests NPEEE!");
  		}
  		//printSavedTest(saved_test, context);
  		//printLog(f_save_tests.getLog(), context, "f_save_tests");
  		String class_id = f_save_tests.getClassId();
  		Vector student_ids = fut.getStudentIds(teacher_id, class_id);
  		Hashtable class_test_status = f_save_tests.loadClassTestStatus(user_info, test_id, student_ids);
  	    Hashtable students = fut.getStudents(teacher_id, class_id); // returns stuydent ids-name pairs
		Hashtable class_tests = f_save_tests.loadClassTestStatus(user_info, test_id, student_ids);
		context.log("GetSavedClassTestsAction.perform class_tests "+class_tests.size());
		Hashtable classes = fut.getClasses(teacher_id); // get the class_id-class_name pairs 
		String class_name = (String)classes.get(class_id);
		context.log("GetSavedClassTestsAction.perform: class_name "+class_name);
		context.log("GetSavedClassTestsAction.perform: test_id "+test_id);
  		context.log("GetSavedClassTestsAction.perform: class_id "+class_id);
  		xmlResponse(response, class_test_status, students, student_ids, class_tests, class_id);
		return (mapping.findForward("saved_tests_for_classes"));
	}
	
	/**
	 * Construct the xml response that has a list of SavedTests and shared test info.
	 * <saved_test>
	 *   <student_id>									added for this method only
	 * 	 <student_name>									added for this method only
	 *   <test_id>-1279361629089452921</test_id>	  	sent in previous method
	 *   <test_date>1275197364509</test_date>	
	 *   <test_name>omg</test_name>					 	 sent in previous method
	 *   <test_type>reading</test_type>
	 *   <test_status>pending</test_status>
	 *   <test_format>IntegratedTest</test_format>
	 *   <creation_time>1275197364509</creation_time> 	sent in previous method
	 *   and number_of_words
	 * </saved_test>
	 * @param response
	 * @param last_record
	 * @param user_id
	 */
	private void xmlResponse(HttpServletResponse response, Hashtable class_test_status, Hashtable students,
			Vector student_ids, Hashtable class_tests, String class_id)
	{
		ServletContext context = getServlet().getServletContext(); 
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<saved_tests>");
		for (Enumeration keys = class_tests.keys(); keys.hasMoreElements() ;) 
		{
			String student_id = (String)keys.nextElement();
			String student_name = (String)students.get(student_id);
			SavedTest saved_test = (SavedTest)class_tests.get(student_id);
			sb.append("<saved_test>");
			sb.append("<student_id>"+student_id+"</student_id>");
			sb.append("<class_id>"+class_id+"</class_id>");
			sb.append("<student_name>"+student_name+"</student_name>");
			sb.append("<test_date>"+saved_test.getTestDate()+"</test_date>");
			sb.append("<test_type>"+saved_test.getTestType()+"</test_type>");
			sb.append("<test_status>"+saved_test.getTestStatus()+"</test_status>");
			sb.append("<test_format>"+saved_test.getTestFormat()+"</test_format>");
			sb.append("<number_of_words>"+saved_test.getNumberOfWords()+"</number_of_words>");
			sb.append("</saved_test>");
		}
		sb.append("</saved_tests>");
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
	
	/**
	 * The Hashtable has student ids and names as a key pairs, so we just turn a
	 * key set into a vector with a time wasting method becuase the gf wants
	 * to go out and look at cherry blossums immediately.
	 * @param students
	 * @return
	 */
	private Vector getStudentIds(Hashtable  students)
	{
		Vector student_ids = new Vector();
		for (Enumeration keys = students.keys(); keys.hasMoreElements() ;) 
		{
			String id = (String)keys.nextElement();
        	student_ids.add(id);
		}
		return student_ids;
	}
	
	/**
	*I assume this is a pair of student id-name pairs.
	*/
	private Vector getStudentNames(Hashtable students)
	{
		Vector names = new Vector();
		for (Enumeration keys = students.keys(); keys.hasMoreElements() ;) 
		{
			String key = (String)keys.nextElement();
			String name = (String)students.get(key);
        		names.add(name);
		}
		return names;
	}
	
	private void printLog(Vector log, ServletContext context)
	{
		context.log("SavedClassTestsAction. log (((((((((((  ");
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("log "+(String)log.get(i));
			i++;
		}
	}
	
	private void printLog(Vector log, ServletContext context, String title)
	{
		context.log("SavedClassTestsAction. log ((((((((((( "+title+" ))))))))))))))");
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("log "+(String)log.get(i));
			i++;
		}
	}
	
	private void printSavedTest(SavedTest saved_test, ServletContext context)
	{
		context.log("SavedClassTestsAction.printSavedTest(((((((((((  ");
			context.log("CreationTime "+saved_test.getCreationTime());
			context.log("ScoreTime "+saved_test.getScoreTime());
			context.log("TestDate "+saved_test.getTestDate());
			context.log("TestFormat "+saved_test.getTestFormat());
			context.log("TestI "+saved_test.getTestId());
			context.log("TestName "+saved_test.getTestName());
			context.log("TestScore "+saved_test.getTestScore());
			context.log("TestStatus "+saved_test.getTestStatus());
			context.log("TestType "+saved_test.getTestType());
			context.log("NumberOfWords "+saved_test.getNumberOfWords());
		context.log("SavedClassTestsAction.printSavedTest(((((((((((  ");
	}
	
	private void printSavedTests(Vector saved_tests, ServletContext context)
    {
	    int i = 0;
	    context.log("saved_tests ==========================");
	    while (i<saved_tests.size())
	    {
	    	context.log("test "+i+" -----=");
	    	SavedTest saved_test = (SavedTest)saved_tests.get(i);
		    printSavedTest(saved_test, context);
		    context.log("test --------=");
		    i++;
	    }
	    context.log("saved_tests ==========================");
    }

	private void printStudents(Hashtable  students,  ServletContext context, String title)
	{
		context.log(title+" ========================== start");
		for (Enumeration keys = students.keys(); keys.hasMoreElements() ;) 
		{
			String id = (String)keys.nextElement();
        	String name = (String)students.get(id);
		}
		context.log(title+" ========================== end");
	}
	
	private void printClassTests(Hashtable  students,  ServletContext context, String title)
	{
		context.log(title+" ========================== start");
		for (Enumeration keys = students.keys(); keys.hasMoreElements() ;) 
		{
			String id = (String)keys.nextElement();
        	SavedTest saved_test = (SavedTest)students.get(id);
        	printSavedTest(saved_test, context);
		}
		context.log(title+" ========================== end");
	}
	
}
