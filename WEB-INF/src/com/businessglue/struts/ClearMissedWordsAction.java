package com.businessglue.struts;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import java.util.Vector;
import java.util.Hashtable;

import org.catechis.I18NWebUtility;
import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.file.FileTestRecords;
import org.catechis.dto.Momento;

/**
 * 
 *@author Timothy Curchod
 */
public class ClearMissedWordsAction extends Action
{
	
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		String type = request.getParameter("type");
		String subject = "vocab";
		String encoding = (String)session.getAttribute("encoding");
		ServletContext context = getServlet().getServletContext();
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		String user_id = (String)session.getAttribute("user_id");
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		context.log("ClearMissedWordsAction.perform "+user_id);
		FileTestRecords ftr = new FileTestRecords(context_path);
	        ftr.clearTestRecord(type, user_id, context_path);
	        try
	        {
	        	Momento old_m = ftr.getMomentoObject(user_id, subject);
	        	String action_name = old_m.getActionName();
	        	context.log("ClearMissedWordsAction.perform action_name="+action_name);
	        	if (action_name.equals("DailyTestResultAction"))
	        	{
	        		context.log("ClearMissedWordsAction.perform return to daily tests");
	        		context.log("if ("+action_name+" equals('DailyTestResultAction'))");
	        		Hashtable blank_words_defs = new Hashtable();
	        		session.setAttribute("w_words_defs", blank_words_defs);
	        		session.setAttribute("r_words_defs", blank_words_defs);
	        		return (mapping.findForward("daily_test_result"));
	        	} else if (action_name.equals("IntegratedTestResultAction"))
	        	{
	        		context.log("if ("+action_name+" equals('IntegratedTestResultAction'))");
	        		Hashtable blank_words_defs = new Hashtable();
	        		session.setAttribute("w_words_defs", blank_words_defs);
	        		session.setAttribute("r_words_defs", blank_words_defs);
	        		return (mapping.findForward("integrated_test_result"));
			} else
	        	{
				context.log("else "+action_name+" returns to options)");
	        		return (mapping.findForward("users_options"));
	        	}
	        } catch (java.lang.NullPointerException npe)
	        {
	        	context.log("npe getting momento");
	        	printLog(ftr.getLog(), context);
	        }
		return (mapping.findForward("users_options"));
	}
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("ClearMissedWordsAction.log "+(String)log.get(i));
			i++;
		}
	}

}
