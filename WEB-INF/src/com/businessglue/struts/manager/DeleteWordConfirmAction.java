package com.businessglue.struts.manager;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Map;

import org.catechis.admin.FileUserOptions;
import org.catechis.constants.Constants;
import org.catechis.dto.Word;
import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;

public class DeleteWordConfirmAction extends Action
{
	
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String word_id = request.getParameter("word_id");
		String category = request.getParameter("word_category");
		context.log(this.getClass().getName()+".perform: word_id "+word_id+" cat "+category);
		String user_id = (String)session.getAttribute("user_id");	// this is the only thing that should be in the session
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		Word word = store.getWordObject("id", word_id, category, user_id);
		//Word word = new Word(); // to save disk calls we should use store.deleteWordFromId
		//word.setId(Long.parseLong(word_id));
		//word.setCategory(category);
		context.log(user_id+" is deleting "+word.getText()+" "+word.getDefinition());
		String encoding = (String)user_opts.get("encoding");
		store.deleteWord(word, category, user_id, encoding);
		FileUserOptions fuo = new FileUserOptions(context_path);
		fuo.createNewLists(user_id, Constants.VOCAB, context_path);	 // the other option would be to save wntd keys in each word obejct as the lists are created, and delete only those file, which would impact the system every time the lists are regenerated.
		session.setAttribute("deleted_word", word);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("categories_simple"));
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