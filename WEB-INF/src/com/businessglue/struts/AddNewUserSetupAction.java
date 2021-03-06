package com.businessglue.struts;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import org.catechis.file.FileCategories;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import org.catechis.Storage;

import com.businessglue.struts.AddCategoryForm;

import java.util.Date;
import java.util.Vector;
import java.util.Hashtable;

/**
*<p>Usually, an action gets called like this: name.do > NameAction > name.jsp, but his action
* is called in reverse, ie name.jsp > NameAction > categories_simple.jsp.
* @author Timothy Curchod
*/
public class AddNewUserSetupAction extends Action
{
	
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		context.log("AddNewUserSetupAction.perform");
		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("add_new_user"));
	}
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("AddCategoryAction.log "+(String)log.get(i));
			i++;
		}
	}

}