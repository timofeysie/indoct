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
Load a specific test for display/printing//scoring.

<daily_test>
	<test_id></test_id>
	<test_name></test_name>
	<test_type></test_type>
	<creation_time></creation_time>
	<test_item>
		<index></index>
		<id></id>
		<category></category>
	</test_item>
</daily_test>
*/
public class CreateIntegratedTestDisplayAction extends Action
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
		context.log("CreateIntegratedTestAction.perform: user_id "+user_id);
		String teacher_id = (String)session.getAttribute("teacher_id");
		String context_path = context.getRealPath("/WEB-INF/");
		String test_id = request.getParameter("test_id");
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		String subject = (String)user_opts.get("subject");
	    String encoding = (String)user_opts.get("encoding");
	    UserInfo user_info = new UserInfo(encoding, context_path, user_id, subject);
		FileSaveTests f_save_tests = new FileSaveTests();
    	Hashtable test_words = f_save_tests.load(user_info, test_id);
		
		FileTestRecords ftr = new FileTestRecords(context_path);
		Momento new_m = new Momento("CreateIntegratedTestDisplayAction", (new Date().toString()), "000", "000");
		ftr.setMomentoObject(user_id, subject, new_m);
		session.setAttribute("test_words", test_words);
		session.setAttribute("test_id", test_id); // so we can retrieve the test entry in the jsp from a list of entries already in the session
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		//if (teacher_id != null)
		//{
			//context.log("DailyTestAction.perform: go to teacher version");
			//return (mapping.findForward("teacher_create_integrated_test"));  // go to a teachers version
		//}
		return (mapping.findForward("created_integrated_test_display"));
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
