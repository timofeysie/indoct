package com.businessglue.struts.testing;

import org.catechis.Storage;
import org.catechis.Domartin;
import org.catechis.FileStorage;
import org.catechis.Transformer;
import org.catechis.JDOMSolution;
import org.catechis.I18NWebUtility;
import org.catechis.WordTestDateUtility;
import org.catechis.dto.Momento;
import org.catechis.dto.SavedTest;
import org.catechis.dto.Test;
import org.catechis.dto.UserInfo;
import org.catechis.dto.Word;
import org.catechis.dto.AllWordsTest;
import org.catechis.dto.WordNextTestDate;
import org.catechis.dto.WordTestDates;
import org.catechis.dto.WordTestResult;
import org.catechis.file.FileSaveTests;
import org.catechis.file.FileTestRecords;
import org.catechis.file.WordNextTestDates;
import org.catechis.dto.WordLastTestDates;
import org.catechis.constants.Constants;
import com.businessglue.struts.testing.CreateDailyTestForm;

import java.io.File;
import java.util.Date;
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

/**
Load a specific test to show to the user so that they can score it with a checkbox form.
Exactly the same as the Display Action.
*/
public class CreatedIntegratedTestScoreAction extends Action
{
	
	/**
	*<p>filename=all&type=w&level=1
	*/
	public ActionForward perform(ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_id = (String)session.getAttribute("user_id");
		String teacher_id = (String)session.getAttribute("teacher_id");
		context.log("CreateIntegratedTestScoreAction.perform: user_id "+user_id+" or teacher_id "+teacher_id);
		String context_path = context.getRealPath("/WEB-INF/");
		String test_id = (String)session.getAttribute("test_id");
		String student_id = request.getParameter("student_id"); // if the student id is present, we came from saved_class_tests
		Storage store = new FileStorage(context_path, context);
		FileSaveTests f_save_tests = new FileSaveTests();
		Hashtable user_opts = new Hashtable();
		String subject = "";
	    String encoding = "";
		if (teacher_id==null)
		{
			user_opts = store.getUserOptions(user_id, context_path);
			FileTestRecords ftr = new FileTestRecords(context_path);
			subject = (String)user_opts.get("subject");
		    encoding = (String)user_opts.get("encoding");
			Momento new_m = new Momento("CreateIntegratedTestScoreAction", (new Date().toString()), "000", "000");
			ftr.setMomentoObject(user_id, subject, new_m);
		} else
		{
			user_opts = store.getTeacherOptions(teacher_id, context_path);
			context.log("CreateIntegratedTestScoreAction.perform: teacher_id "+teacher_id+" test_id "+test_id+" student_id "+student_id);
			user_id = student_id; // this is the class branch just to save the teacher time from going back to the class each time when scoring a lot of tests.
			subject = Constants.VOCAB;
		    encoding = (String)user_opts.get("encoding");
		    try
		    {
		    	Vector saved_tests = f_save_tests.getSavedTestsList(new UserInfo(encoding, context_path, student_id, subject));
		    	session.setAttribute("saved_tests", saved_tests);
		    	session.setAttribute("student_id", student_id);
		    	context.log("CreateIntegratedTestScoreAction.perform: test_size "+saved_tests.size());
		    	printLog(f_save_tests.getLog(), context);
		    } catch (java.lang.NullPointerException npe)
		    {
		    	context.log("CreateIntegratedTestScoreAction.perform: fst log ++++ npe");
		    	printLog(f_save_tests.getLog(), context);
		    }
		}
	    UserInfo user_info = new UserInfo(encoding, context_path, user_id, subject);
		context.log("test_id "+test_id+"encoding "+encoding+" subject "+subject+" student_id "+student_id);
    	Hashtable test_words = f_save_tests.load(user_info, test_id);
    	context.log("test_words "+test_words.size()+" ==-=----=-== create table & log ==-=----=-==");
		printLog(Transformer.createTable(user_info), context);
		printLog(f_save_tests.getLog(), context);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		session.setAttribute("test_words", test_words);
		return (mapping.findForward("created_integrated_test_score"));
	}
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("CreateIntegratedTestScoreAction "+(String)log.get(i));
			i++;
		}
	}
	
	 private void dumpSavedTestsList(Vector tests_list, ServletContext context)
	    {
		    int i = 0;
		    while (i<tests_list.size())
		    {
			    Word word = (Word)tests_list.get(i);
			    context.log("------------ "+i);
			    Test [] tests = word.getTests();
				Test test = tests[0];
				String type = test.getType();
				System.out.println(i+" "+type+" - "+word.getDefinition()+" "+word.getText());
			    i++;
		    }

	    }

}
