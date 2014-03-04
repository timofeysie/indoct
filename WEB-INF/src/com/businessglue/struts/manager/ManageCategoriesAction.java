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
import org.catechis.Transformer;
import org.catechis.constants.Constants;
import org.catechis.dto.AllStatsHistory;
import org.catechis.dto.AllWordStats;
import org.catechis.dto.AllTestStats;
import org.catechis.dto.WordCategory;
import org.catechis.file.FileCategories;
import org.catechis.file.FileTestRecords;
import org.catechis.file.FileWordCategoriesManager;
import org.catechis.lists.Sarray;
import org.catechis.lists.Sortable;

public class ManageCategoriesAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_id = (String)session.getAttribute(Constants.USER_ID);
		String subject = (String)session.getAttribute(Constants.SUBJECT);
		if (subject==null)
			subject = Constants.VOCAB;
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		FileWordCategoriesManager fiwocama = new FileWordCategoriesManager();
		Sarray word_catsarray = new Sarray();
		try
		{
			word_catsarray = fiwocama.getSortedCategories(user_id, subject, context_path, Constants.EXCLUSIVE);
		} catch (java.lang.NullPointerException npe)
		{
			context.log("ManageCategoriesAction.perform npe from fwcm.getSortedCategories");
		}
		printLog(fiwocama.getLog(), context);
		session.setAttribute("categories", word_catsarray);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("manage_categories"));
	}
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log((String)log.get(i));
			i++;
		}
	}
	
}
