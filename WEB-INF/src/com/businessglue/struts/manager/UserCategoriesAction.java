package com.businessglue.struts.manager;

import java.util.Vector;
import java.util.Hashtable;
import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import org.catechis.file.FileCategories;

import org.catechis.file.FileTestRecords;
import org.catechis.dto.Momento;
import java.util.Date;

/**
*This class gets a simple sorted list of categories (xml word files at this point)
* as opposed to CategoriesAxtion which calculates statistics and updates the history file
* and could take a long time for a user with many words and tests.
*/
public class UserCategoriesAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		FileCategories cats = new FileCategories(context_path);
		Hashtable categories = cats.getSortedWordCategories(user_id);
		printLog(cats.getLog(), context);
		context.log("CategoriesSimpleAction.perform: categories "+categories.size());
		session.setAttribute("categories", categories);
		FileStorage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		String locale = (String)user_opts.get("locale");
		String encoding = (String)user_opts.get("encoding");
		String subject = (String)user_opts.get("subject");
		context.log("ImportListAction.perform: user name "+user_id);
		Momento old_m = getMomento(context_path, user_id, subject);
		setMomento(context_path, user_id, subject, "UserCategoriesAction", "default", "default");
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("user_categories"));
	}
	
	private Momento getMomento(String context_path, String user_id, String subject)
	{
		FileTestRecords ftr = new FileTestRecords(context_path);
		Momento old_m = ftr.getMomentoObject(user_id, subject);
		return old_m;
	}
	
	private void setMomento(String context_path, String user_id, String subject, String action_name, String action_id, String action_type)
	{
		Momento new_m = new Momento(action_name, Long.toString(new Date().getTime()), action_id, action_type);
		FileTestRecords ftr = new FileTestRecords(context_path);
		ftr = new FileTestRecords(context_path);
		ftr.setMomentoObject(user_id, subject, new_m);
	}
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("CategoriesSimpleAction.log "+(String)log.get(i)); 
			i++;
		}
	}
	
}
