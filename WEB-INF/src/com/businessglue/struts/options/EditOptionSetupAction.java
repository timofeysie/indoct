package com.businessglue.struts.options;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import org.catechis.dto.Word;
import org.catechis.dto.AllWordsTest;
import org.catechis.dto.WordTestResult;
import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import com.businessglue.util.EncodeString;

import java.util.Vector;
import java.util.Hashtable;

public class EditOptionSetupAction extends Action
{
	
	private Vector log;
	
	/**
	*<p>This action prepares everything needed to edit a user option.
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_name = (String)session.getAttribute("user_name");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_name, context_path);
		String encoding = (String)user_opts.get("encoding");
		String option_name = (String)request.getParameter("option_name");	
		context.log("EditOptionSetupAction.perform: option_name "+option_name);
		session.setAttribute("option_name", option_name);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("edit_option"));
	}
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("EditOptionSetupAction.log "+(String)log.get(i));
			i++;
		}
	}

}
