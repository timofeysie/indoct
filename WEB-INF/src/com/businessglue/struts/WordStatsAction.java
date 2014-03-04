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

public class WordStatsAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_name = (String)session.getAttribute("user_name");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		Vector tests = store.getTestCategories(user_name);
		Vector words = store.getWordCategories("primary", user_name);
		AllWordStats all_word_stats = store.getWordStats(words, user_name);
		session.setAttribute("all_word_stats", all_word_stats);
		Hashtable user_opts = store.getUserOptions(user_name, context_path);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("word_stats"));
	}
	
}
