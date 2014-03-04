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
<p>Delete the test the students are signed up to take.
*/
public class DeleteClassTestsAction extends IndoctAction
{
	
	private String DEBUG_TAG = "DeleteClassTestsAction";
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String method = "perform";
		context.log(DEBUG_TAG+" "+method);
		String teacher_id = (String)session.getAttribute("teacher_id");
		// String class_id = (String)session.getAttribute("class_id");
		Vector student_ids = (Vector)session.getAttribute("student_ids");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		String test_id = (String)session.getAttribute("test_id");
		Hashtable teacher_opts = store.getTeacherOptions(teacher_id, context_path);
		String encoding = (String)teacher_opts.get("encoding");
		String subject = Constants.VOCAB;
		FileSaveTests f_save_tests = new FileSaveTests();
  		UserInfo teacher_info = new UserInfo(encoding, context_path, teacher_id, subject);
  		FileUserUtilities fut = new FileUserUtilities(context_path);
  		for (int i = 0;i < student_ids.size(); i++)
  		{
  			String student_id = (String)student_ids.get(i);
  			UserInfo student_info = new UserInfo(encoding, context_path, student_id, subject);
  			try
  			{
  				boolean deleted_user_files = f_save_tests.deleteSavedTestList(test_id, student_info);
  				context.log("DeleteClassTestsAction.perform: deleted_user_files "+deleted_user_files);
  			} catch (java.lang.NullPointerException npe1)
  			{
  				context.log(DEBUG_TAG+" "+method+" npe "+npe1.toString());
  				npe1.printStackTrace();
  				printLog(f_save_tests.getLog(), context);
  			} catch (java.lang.IndexOutOfBoundsException aioobe)
  			{
  				context.log(DEBUG_TAG+" "+method+" aioobe: "+aioobe.toString());
  				aioobe.printStackTrace();
  				printLog(f_save_tests.getLog(), context);
  			}
  		}
  		try
  		{
  			boolean deleted_teacher_files = f_save_tests.deleteSavedClassTestFromList(teacher_info, test_id);
  			context.log("DeleteClassTestsAction.perform: "+deleted_teacher_files);
  		} catch (java.lang.NullPointerException npe2)
  		{
  			context.log(DEBUG_TAG+" "+method+" "+npe2.toString());
  			printLog(f_save_tests.getLog(), context);
  		}
  		Vector saved_class_tests = f_save_tests.loadClassTestList(teacher_info);
  		session.setAttribute("saved_class_tests", saved_class_tests);
		return (mapping.findForward("saved_tests_for_classes"));
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
