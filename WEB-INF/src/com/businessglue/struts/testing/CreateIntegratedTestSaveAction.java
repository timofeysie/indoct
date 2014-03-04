package com.businessglue.struts.testing;

import org.catechis.Storage;
import org.catechis.Domartin;
import org.catechis.FileStorage;
import org.catechis.Transformer;
import org.catechis.JDOMSolution;
import org.catechis.I18NWebUtility;
import org.catechis.WordTestDateUtility;
import org.catechis.dto.SavedTest;
import org.catechis.dto.UserInfo;
import org.catechis.dto.Word;
import org.catechis.dto.AllWordsTest;
import org.catechis.dto.WordTestDates;
import org.catechis.dto.WordTestResult;
import org.catechis.file.FileSaveTests;
import org.catechis.file.FileTestRecords;
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
*<p>Save the created and named integrated test.
*<p>
*<p><daily_test>
*<p>	<test_id></test_id>
*<p>	<test_type></test_type>
*<p>	<creation_time></creation_time>
*<p>	<test_item>
*<p>		<index></index>
*<p>		<id></id>
*<p>		<category></category>
*<p>	</test_item>
*<p></daily_test>
*/
public class CreateIntegratedTestSaveAction extends Action
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
		context.log("CreateIntegratedTestSaveAction.perform: user_id "+user_id);
		String teacher_id = (String)session.getAttribute("teacher_id");
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		// save the test created in the previous action and add an entry in the saved tests list
		String subject = (String)user_opts.get("subject");
		String encoding = (String)user_opts.get("encoding");
		UserInfo user_info = new UserInfo(encoding, context_path, user_id, subject);
		FileSaveTests f_save_tests = new FileSaveTests();
		Vector test_words = (Vector)session.getAttribute("test_words");
		SavedTest saved_test = (SavedTest)session.getAttribute("saved_test");
		String test_id = saved_test.getTestId();
		f_save_tests.save(user_info, test_words, test_id); // save the test words
		context.log("CreateIntegratedTestSaveAction.perform: saved_test saved with test_id "+test_id);
 	    f_save_tests.addToSavedTestsList(user_info, saved_test, test_id); // create the entry
		// now load all the saved tests and set them in the session so the jsp can display them
		Vector saved_tests = f_save_tests.getSavedTestsList(user_info);
		context.log("CreateIntegratedTestSaveAction.perform: saved_tests size "+saved_tests.size());
		session.setAttribute("saved_tests", saved_tests);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		if (teacher_id != null)
		{
			context.log("DailyTestAction.perform: go to teacher version");
			return (mapping.findForward("teacher_created_integrated_tests"));  // go to a teachers version
		}
		return (mapping.findForward("created_integrated_tests"));
	}
	
	private Vector getELTVector(Hashtable user_opts, ServletContext context)
	{
		Vector elt_vector = new Vector();
		elt_vector.add(user_opts.get("exclude_level0_test"));
		elt_vector.add(user_opts.get("exclude_level1_test"));
		elt_vector.add(user_opts.get("exclude_level2_test"));
		elt_vector.add(user_opts.get("exclude_level3_test"));
		return elt_vector;
	}
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("DailyTestAction "+(String)log.get(i));
			i++;
		}
	}

}
