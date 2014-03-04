package com.businessglue.struts.testing;

import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import org.catechis.dto.UserInfo;
import org.catechis.file.FileSaveTests;

import java.util.Vector;
import java.util.Hashtable;

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

<daily_test>
	<test_id></test_id>
	<test_type></test_type>
	<creation_time></creation_time>
	<test_item>
		<index></index>
		<id></id>
		<category></category>
	</test_item>
</daily_test>
 * @author Timothy Curchod
*/
public class CreatedTestsAction extends Action
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
		context.log("CreatedTestsAction.perform: user_id "+user_id);
		String teacher_id = (String)session.getAttribute("teacher_id");
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		String subject = (String)user_opts.get("subject");
		String encoding = (String)user_opts.get("encoding");
		UserInfo user_info = new UserInfo(encoding, context_path, user_id, subject);
		FileSaveTests f_save_tests = new FileSaveTests();
		// load all the saved tests and set them in the session so the jsp can display them
		Vector saved_tests = f_save_tests.getSavedTestsList(user_info);
		context.log("CreatedTestsAction.perform: fst log ----- ");
		printLog(f_save_tests.getLog(), context);
		context.log("CreateTestsAction.perform: saved_tests size "+saved_tests.size());
		session.setAttribute("saved_tests", saved_tests);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		if (teacher_id != null)
		{
			context.log("CreatedTestsAction.perform: go to teacher version");
			return (mapping.findForward("teacher_created_integrated_tests"));  // go to a teachers version
		}
		return (mapping.findForward("created_integrated_tests"));
	}
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("CreatedTestsAction.log: "+(String)log.get(i));
			i++;
		}
	}

}
