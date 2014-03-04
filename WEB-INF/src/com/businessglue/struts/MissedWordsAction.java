package com.businessglue.struts;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import java.util.Hashtable;
import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.filter.WordListFilter;
import org.catechis.I18NWebUtility;

public class MissedWordsAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_name = (String)session.getAttribute("user_name");
		String context_path = context.getRealPath("/WEB-INF/");
		context.log("MissedWordsAction.perform: user name "+user_name);
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_name, context_path);
		WordListFilter wlf = new WordListFilter();
		String reading = new String("reading"); 
		String writing = new String("writing");
		Hashtable r_words_defs = wlf.getMissedWords(reading, context_path, user_name, user_opts);
		Hashtable w_words_defs = wlf.getMissedWords(writing, context_path, user_name, user_opts);
		session.setAttribute("r_words_defs", r_words_defs);
		session.setAttribute("w_words_defs", w_words_defs);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("missed_words"));
	}

}
