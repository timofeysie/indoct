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
import org.catechis.dto.AllStatsHistory;
import org.catechis.dto.AllWordStats;
import org.catechis.dto.AllTestStats;
import org.catechis.file.FileCategories;
import org.catechis.file.FileTestRecords;

/**
 * 
 * @author Timothy Curchod
 */
public class CategoriesAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		//Storage store = (Storage)session.getAttribute("store");
		String user_name = (String)session.getAttribute("user_name");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		Vector tests = store.getTestCategories(user_name);
		Vector words = store.getWordCategories("primary", user_name);
		AllTestStats all_test_stats = store.getTestStats(tests, user_name);
		AllWordStats all_word_stats = store.getWordStats(words, user_name);
		store.updateHistory(all_test_stats, all_word_stats, user_name);
		FileTestRecords ftr = new FileTestRecords();
		Hashtable last_record = new Hashtable();
		try
		{
			last_record = ftr.getLastUserHistoryHash(user_name, context_path);
		} catch (java.lang.NullPointerException npe)
		{
		}
		context.log("CategoriesAction.perform: records "+last_record.size()); 
		session.setAttribute("last_record", last_record);
		FileCategories cats = new FileCategories(context_path);
		Hashtable categories = cats.getSortedWordCategories(user_name);
		session.setAttribute("all_test_stats", all_test_stats);
		session.setAttribute("all_word_stats", all_word_stats);
		session.setAttribute("categories", categories);
		Hashtable user_opts = store.getUserOptions(user_name, context_path);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("categories"));
	}
	
}
