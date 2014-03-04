package com.businessglue.struts;

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

import com.businessglue.util.Domartin;

/**
*This class gets a simple sorted list of categories (xml word files at this point)
* as opposed to CategoriesAxtion which calculates statistics and updates the history file
* and could take a long time for a user with many words and tests.
* @author Timothy Curchod
*/
public class CategoriesSimpleAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		context.log("CategoriesSimpleAction.perform");
		String user_id = (String)session.getAttribute("user_id");
		String user_name = (String)session.getAttribute("user_name");
		context.log("CategoriesSimpleAction.perform: user_name "+user_name);
		context.log("CategoriesSimpleAction.perform: user_id   "+user_id);
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		Hashtable user_opts = new Hashtable();
		try
		{
			user_opts = store.getUserOptions(user_id, context_path);
		} catch (java.lang.NullPointerException npe)
		{
			printLog(store.getLog(), context);
		}
		String encoding = (String)user_opts.get("encoding");
		FileCategories cats = new FileCategories(context_path);
		Hashtable categories = new Hashtable();
		try
		{
			categories = cats.getSortedWordCategories2(user_id, encoding);
		} catch (java.lang.NullPointerException npe)
		{
			printLog(cats.getLog(), context);
		}
		context.log("CategoriesSimpleAction.perform: categories "+categories.size());
		session.setAttribute("categories", categories);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("categories_simple"));
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
