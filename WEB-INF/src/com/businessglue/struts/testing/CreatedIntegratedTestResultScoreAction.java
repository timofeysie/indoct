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
import org.catechis.filter.WordListFilter;
import org.catechis.gwangali.RateOfTesting;
import org.catechis.juksong.TestTimeMemory;
import org.catechis.testing.TestUtility;
import org.catechis.dto.WordLastTestDates;
import org.catechis.admin.FileUserUtilities;
import org.catechis.constants.Constants;

import org.catechis.indoct.VillaUtilities;
import com.businessglue.struts.DailyTestForm;
import com.businessglue.struts.testing.CreateDailyTestForm;
import com.businessglue.util.EncodeString;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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
Load a specific test to show to the user so that they can score it.
Exactly the same as the Display Action. 
*/
public class CreatedIntegratedTestResultScoreAction extends Action
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
		String student_id = (String)session.getAttribute("student_id");
		String class_id = (String)session.getAttribute("class_id");
		String context_path = context.getRealPath("/WEB-INF/");
		String test_id = (String)session.getAttribute("test_id");
		context.log("CreateIntegratedTestResultScore.perform: user_id "+user_id+" t "+teacher_id+" s "+student_id+" c "+class_id+" test "+test_id);
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = null;String subject = "wait";String encoding = "for it"; UserInfo user_info = null;
		FileSaveTests f_save_tests = new FileSaveTests();
		if (teacher_id==null)
		{
			user_opts = store.getUserOptions(user_id, context_path);
			subject = (String)user_opts.get("subject");
			encoding = (String)user_opts.get("encoding");
			user_info = new UserInfo(encoding, context_path, user_id, subject);
		} else if (teacher_id!=null)
		{
			user_opts = store.getTeacherOptions(teacher_id, context_path);
			context.log("CreatedIntegratedTestResultScoreAction.perform: teacher_id "+teacher_id+" test_id "+test_id+" student_id "+student_id);
			user_id = student_id; 
			subject = Constants.VOCAB;
		    encoding = (String)user_opts.get("encoding");
		    user_info = new UserInfo(encoding, context_path, student_id, subject);
		}
    	Hashtable test_words = f_save_tests.loadSavedTestWordsForScorring(user_info, test_id);
    	printLog(f_save_tests.getLog(), context);
    	Map passed_words_param_map = request.getParameterMap();
    	context.log("CreateIntegratedTestResultScore.perform: map -----------");
		printMap(passed_words_param_map, context);
    	VillaUtilities vu = new VillaUtilities();
    	try
    	{
    		Hashtable new_test_words = vu.updateScoreAndWords(passed_words_param_map, test_words, 
    				user_id, subject, context_path);
    		double fl_score = (passed_words_param_map.size()/test_words.size()*100); 
    		DecimalFormat df = new DecimalFormat("##");
    		String score = df.format(fl_score);
    		context.log(passed_words_param_map.size()+" / "+test_words.size()+"  "+score);
    		context.log("CreateIntegratedTestResultScore.perform: after vu.updateScoreAndWords log -----------");
    		printLog(vu.getLog(), context);
    		vu.resetLog();
    		SavedTest saved_test = f_save_tests.loadSavedTest(user_info, test_id);
    		saved_test.setTestStatus(Constants.COMPLETE);
    		saved_test.setTestScore(score);
    		Date date = new Date();
       		long time = date.getTime();
			saved_test.setScoreTime(time+"");
			f_save_tests = new FileSaveTests();
			f_save_tests.updateSavedTestStatus(user_info, saved_test); // change the status from pending to complete
			context.log("CreateIntegratedTestResultScore.perform: after vu.updateSavedTestStatus log --------- score "+score);
    		printLog(vu.getLog(), context);
    	} catch (java.lang.NullPointerException npe)
    	{
    		context.log("CreateIntegratedTestResultScore.perform: vu.updateScoreAndWords npe ");
    		printLog(vu.getLog(), context);
    		context.log("CreateIntegratedTestResultScore.perform: map -----------");
    		printMap(passed_words_param_map, context);
    		context.log("user_id "+user_id+" subj "+subject);
    	} catch (java.lang.NumberFormatException nfe)
    	{
    		context.log("CreateIntegratedTestResultScore.perform: vu.updateScoreAndWords nfe ");
    		printLog(vu.getLog(), context);
    	}
    	
    	FileUserUtilities fut = new FileUserUtilities(context_path);
    	Vector student_ids = fut.getStudentIds(teacher_id, class_id);
  		Hashtable class_test_status = new Hashtable();
  		class_test_status = f_save_tests.loadClassTestStatus(user_info, test_id, student_ids);
  	    Hashtable students = fut.getStudents(teacher_id, class_id); // returns stuydent ids-name pairs
		Hashtable class_tests = new Hashtable();
		class_tests = f_save_tests.loadClassTestStatus(user_info, test_id, student_ids);
		session.setAttribute("class_tests", class_tests);
		session.setAttribute("test_words", test_words);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		if (teacher_id!=null)
		{
			refreshClassTests(f_save_tests, user_info, test_id, session);
			return (mapping.findForward("saved_tests_for_classes"));
		}
		FileTestRecords ftr = new FileTestRecords(context_path);
		Momento new_m = new Momento("CreateIntegratedTestScoreAction", (new Date().toString()), "000", "000");
		ftr.setMomentoObject(user_id, subject, new_m);
		return (mapping.findForward("created_integrated_tests"));
	}
	
	private void refreshClassTests(FileSaveTests f_save_tests, UserInfo user_info,
			String test_id, HttpSession session)
	{
		Vector student_ids = (Vector)session.getAttribute("student_ids");
		Hashtable class_tests = class_tests = f_save_tests.loadClassTestStatus(user_info, test_id, student_ids);
		session.setAttribute("student_ids", student_ids);
	}
	
	private void scoreTest(HttpServletRequest request, Hashtable test_words, UserInfo user_info,
			String test_id)
	{
		VillaUtilities vu = new VillaUtilities();
		Map passed_words_param_map = request.getParameterMap();
		Hashtable new_test_words = vu.updateScoreAndWords(passed_words_param_map, test_words, 
				user_info.getUserId(), user_info.getSubject(), user_info.getRootPath());
		ServletContext context = getServlet().getServletContext();
		context.log("CreateIntegratedTestResultScore.perform: after vu.updateScoreAndWords log -----------");
		printLog(vu.getLog(), context);
		vu.resetLog();
		FileSaveTests f_save_tests = new FileSaveTests();
		SavedTest saved_test = f_save_tests.loadSavedTest(user_info, test_id);
		saved_test.setTestStatus(Constants.COMPLETE);
		Date date = new Date();
   		long time = date.getTime();
		saved_test.setScoreTime(time+"");
		f_save_tests = new FileSaveTests();
		f_save_tests.updateSavedTestStatus(user_info, saved_test); // change the status from pending to complete
		context.log("CreateIntegratedTestResultScore.perform: after vu.updateSavedTestStatus log ---------");
		printLog(vu.getLog(), context);
	}
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		context.log("CreateIntegratedTestResultScore.printLog: size "+total);
		int i = 0;
		while (i<total)
		{
			context.log("CreatedIntegratedTestResultScoreAction.log: "+(String)log.get(i));
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
	 
	 private void printMap(Map passed_words_param_map, ServletContext context)
	 {
		 Iterator it = passed_words_param_map.entrySet().iterator();
		 while (it.hasNext())
		 {
			 Map.Entry pairs = (Map.Entry)it.next();
			 System.out.println(pairs.getKey()+" "+pairs.getValue());
		 }
	 }
	 
	//private void printTestWords(test_words); 

}
