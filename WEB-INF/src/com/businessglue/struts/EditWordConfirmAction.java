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
import org.catechis.Transformer;
import com.businessglue.util.EncodeString;

import java.util.Vector;
import java.util.Hashtable;

/**
 * 
 * @author Timothy Curchod
 *
 */
public class EditWordConfirmAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_name = (String)session.getAttribute("user_name");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_name, context_path);
		//String encoding = (String)user_opts.get("encoding");
		String encoding = new String("UTF-8");
		AddWordForm f = (AddWordForm)form;
		//String text = EncodeString.encodeThis(f.getText(),encoding);
		String text = Transformer.getStringFromBytesString((String)session.getAttribute("text"));
		String text_bytes = Transformer.getStringFromBytesString((String)session.getAttribute("text_bytes"));
		String definition = EncodeString.encodeThis(f.getDefinition(),encoding);
		String file_name = (String)session.getAttribute("file_name");
		String word_text_chosen = (String)session.getAttribute("word_chosen");
		Word edit_word = (Word)session.getAttribute("word");
		context.log("EditWordConfirmAction.perform");
		context.log("t "+text);
		context.log("tb "+text_bytes);
		context.log("d "+definition);
		context.log("f "+file_name);
		context.log("wtc "+word_text_chosen);
		Word old_word = store.getWordWithoutTests(word_text_chosen, file_name, user_name);
		Word new_word = new Word();
		new_word.setText(EncodeString.encodeThis(text, encoding));
		new_word.setDefinition(EncodeString.encodeThis(definition, encoding));
		store.editWord(edit_word, new_word, file_name, user_name, encoding);
		session.setAttribute("word", new_word);
		context.log("new text "+text);
		context.log("new def "+definition);
		dumpLog(store.getLog(), context);
		String locale = (String)user_opts.get("locale");
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("edit_word_confirm"));
	}
	
	private void dumpLog(Vector log, ServletContext context)
	{
		int size = log.size();
		int i = 0;
		while (i<size)
		{
			context.log("Log: "+log.get(i));
			i++;
		}
	}

}
