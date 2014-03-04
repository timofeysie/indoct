package com.businessglue.struts;

import org.catechis.Storage;
import org.catechis.Domartin;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.util.Vector;
import java.util.Hashtable;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
*<p>This basic action gets called on the daily_test_result jsp page where there 
* are two links: edit text or edit definition.  All this method has to do then
* is make sure the content type and local are set for the next page.
* @author Timothy Curchod 
*/
public class EditWordSetupAction extends Action
{
	
	/**
	*<p>
	*/
	public ActionForward perform(ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String type = (String)request.getParameter("type");
		response.setHeader("type", type);				// pass along edit type: text or def
		context.log("EditWordSetupAction.perform"); 
		String user_name = (String)session.getAttribute("user_name");
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_name, context_path);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("edit_word"));
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
