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
 * Take the
 * 
 * <daily_test> <test_id></test_id> <test_name></test_name>
 * <test_type></test_type> <creation_time></creation_time> <test_item>
 * <index></index> <id></id> <category></category> </test_item> </daily_test>
 */
public class CreateIntegratedTestAction extends Action 
{

	/**
	 *<p>
	 * filename=all&type=w&level=1
	 */
	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_id = (String) session.getAttribute("user_id");
		context.log("CreateIntegratedTestAction.perform: user_id " + user_id);
		String teacher_id = (String) session.getAttribute("teacher_id");
		String context_path = context.getRealPath("/WEB-INF/");
		// FileTestRecords ftr = new FileTestRecords(context_path);
		// Momento old_m = ftr.getMomentoObject(user_id, subject);
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		context.log("CreateIntegratedTestAction.perform: user_ops "
				+ user_opts.size());
		String subject = (String) user_opts.get("subject");
		CreateIntegratedTestForm f = (CreateIntegratedTestForm) form;
		String test_name = f.getTestName();
		String test_type = f.getTestType();
		String number_of_words = f.getNumberOfWords();
		context.log("CreateIntegratedTestAction.perform: test_name "
				+ test_name + " type " + test_type + " words "
				+ number_of_words);
		if (test_type == null) {
			test_type = Constants.READING;
			context.log("CreateIntegratedTestAction: USE DEFAULT TYPE "
					+ test_type);
		} else if (test_type.equals("both")) {
			test_type = Constants.READING_AND_WRITING;
			context.log("CreateIntegratedTestAction: USE DEFAULT TYPE or Both "
					+ test_type);
		}
		// create the test info object
		String str_id = Domartin.getNewID() + "";
		String today_now = new Date().getTime() + "";
		context.log("CreateIntegratedTestAction: arg");
		WordNextTestDates wntds = new WordNextTestDates();
		SavedTest saved_test = new SavedTest();
		try {
			saved_test.setTestId(str_id);
			saved_test.setTestDate(today_now);
			saved_test.setTestName(test_name);
			saved_test.setTestType(test_type);
			saved_test.setTestStatus("pending");
			saved_test.setTestFormat("IntegratedTest");
			saved_test.setCreationTime(today_now);
		} catch (java.lang.NullPointerException npe) {
			context
					.log("CreateIntegratedTestAction.perform: saved_test bean creation npe");
		}
		String action_id = "000";
		String action_type = "000";
		String action_time = Long.toString(new Date().getTime());
		Vector test_words = new Vector();
		try {
			context
					.log("CreateIntegratedTestAction.perform: starting wntds.getNextTestWords");
			test_words = wntds.getTestWords(user_id, context_path, action_time,
					subject, test_type, number_of_words);
			context.log("wntds log ------------------------------ ");
			printLog(wntds.getLog(), context);
		} catch (java.lang.NullPointerException npe) {
			context
					.log("CreateIntegratedTestAction.perform: wntds npe: printing log ----- ");
			printLog(wntds.getLog(), context);
		}
		context.log("CreateIntegratedTestAction.perform: words "
				+ test_words.size());
		dumpSavedTestsList(test_words, context);
		FileTestRecords ftr = new FileTestRecords(context_path);
		Momento new_m = new Momento("CreateIntegratedTestAction", action_time,
				action_id, action_type);
		ftr.setMomentoObject(user_id, subject, new_m);
		session.setAttribute("test_words", test_words);
		session.setAttribute("saved_test", saved_test);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		if (teacher_id != null) {
			context
					.log("CreateIntegratedTestAction.perform: go to teacher version");
			return (mapping.findForward("teacher_create_integrated_test")); // go
			// to
			// a
			// teachers
			// version
		}
		return (mapping.findForward("create_integrated_test"));
	}

	private void printLog(Vector log, ServletContext context) {
		int total = log.size();
		int i = 0;
		while (i < total) {
			context.log("CreateIntegratedTestAction.printLog: "
					+ (String) log.get(i));
			i++;
		}
	}

	private void dumpSavedTestsList(Vector tests_list, ServletContext context) {
		int i = 0;
		while (i < tests_list.size()) {
			try {
				Word word = (Word) tests_list.get(i);
				context.log("------------ " + i);
				Test[] tests = word.getTests();
				Test test = tests[0];
				String type = test.getType();
				context.log(i + " " + type + " - " + word.getDefinition() + " "
						+ word.getText());
			} catch (java.lang.NullPointerException npe) {
				context.log(i + " npe");
			}
			i++;
		}

	}

}
