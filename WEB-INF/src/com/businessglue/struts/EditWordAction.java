package com.businessglue.struts;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import org.catechis.dto.Word;
import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import com.businessglue.util.EncodeString;

import java.util.Vector;
import java.util.Hashtable;

public class EditWordAction extends Action
{
	
	private Vector log;
	
	/**
	*<p>This method came from EditWordAction and DailyTestAction, which is why
	the new text or def is called answer.
    * @author Timothy Curchod
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		context.log("EditWordAction.perform"); 
		String user_name = (String)session.getAttribute("user_name");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_name, context_path);
		String encoding = (String)user_opts.get("encoding");
		String type = (String)request.getParameter("type");	// text or def
		DailyTestForm test_form = (DailyTestForm)form; 		// test form used here to edit
		String answer = EncodeString.encodeThis(test_form.getAnswer(),encoding);
		Word old_word = (Word)session.getAttribute("word");	// this object holds the original word
		Word new_word = populateNewWord(old_word, answer, type);
		String file_name = (String)session.getAttribute("file_name");
		try
		{
			store.editWord(old_word, new_word, file_name, user_name, encoding);
		} catch (java.lang.NullPointerException npe)
		{
			printLog(store.getLog(), context);
		}
		session.setAttribute("word", new_word);
		String word_text_chosen = (String)session.getAttribute("word_chosen");
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("stats_word"));
	}

	private Word populateNewWord(Word old_word, String answer, String type)
	{
		Word word = new Word();
		if (type.equals("def"))
		{
			word.setText(old_word.getText());
			word.setDefinition(answer);
		} else if (type.equals("text"))
		{
			word.setText(answer);
			word.setDefinition(old_word.getDefinition());
		}
		return word;
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