package com.businessglue.struts.teacher;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

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
<p>Get a list of students that are signed up to take this test.
*/
public class SavedClassTestsAction extends IndoctAction
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		context.log("SavedClassTestsAction.perform");
		String teacher_id = (String)session.getAttribute("teacher_id");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		String test_id = request.getParameter("test_id");
		Hashtable teacher_opts = store.getTeacherOptions(teacher_id, context_path);
		String encoding = (String)teacher_opts.get("encoding");
		String subject = Constants.VOCAB;
		FileSaveTests f_save_tests = new FileSaveTests();
  		UserInfo user_info = new UserInfo(encoding, context_path, teacher_id, subject);
  		FileUserUtilities fut = new FileUserUtilities(context_path);
  		SavedTest saved_test = f_save_tests.loadClassTest(user_info, test_id);
  		printSavedTest(saved_test, context);
  		printLog(f_save_tests.getLog(), context, "f_save_tests");
  		String class_id = f_save_tests.getClassId();
  		Vector student_ids = fut.getStudentIds(teacher_id, class_id);
  		Hashtable class_test_status = f_save_tests.loadClassTestStatus(user_info, test_id, student_ids);
  	    Hashtable students = fut.getStudents(teacher_id, class_id); // returns stuydent ids-name pairs
		Hashtable class_tests = f_save_tests.loadClassTestStatus(user_info, test_id, student_ids);
		context.log("SavedClassTestsAction.perform class_tests "+class_tests.size());
		Hashtable classes = fut.getClasses(teacher_id); // get the class_id-class_name pairs 
		String class_name = (String)classes.get(class_id);
		context.log("SavedClassTestsAction.perform: class_name "+class_name);
		context.log("SavedClassTestsAction.perform: test_id "+test_id);
  		context.log("SavedClassTestsAction.perform: class_id "+class_id);
  		printStudents(students, context, "students: student/id pairs");
  		printLog(student_ids, context, "student_ids");
  		printClassTests(class_tests, context, "class_tests: student_id key and a saved_test");
  		printLog(f_save_tests.getLog(), context, "f_save_tests log");
  		printLog(fut.getLog(), context, "fut log");
		session.setAttribute("class_id", class_id);
		session.setAttribute("test_id", test_id);
		session.setAttribute("students", students);
		session.setAttribute("student_ids", student_ids);
		session.setAttribute("class_tests", class_tests);
		session.setAttribute("class_name", class_name); 
		return (mapping.findForward("saved_tests_for_classes"));
	}
	
	/**
	 * The Hashtable has student ids and names as a key pairs, so we just turn a
	 * key set into a vector with a time wasting method because the gf wants
	 * to go out and look at cherry blossoms immediately.
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
	*We assume this is a pair of student id-name pairs.
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
