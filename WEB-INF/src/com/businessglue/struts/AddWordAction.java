package com.businessglue.struts;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import org.catechis.dto.Word;
import org.catechis.file.FileWordCategoriesManager;
import org.catechis.Domartin;
import org.catechis.Storage;
import org.catechis.FileStorage;
import com.businessglue.util.EncodeString;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

/**
 * 
 * @author Timothy Curchod
 */
public class AddWordAction extends Action
{
	
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext(); 
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		String user_id = (String)session.getAttribute("user_id");
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		String encoding = (String)session.getAttribute("encoding");
		AddWordForm f = (AddWordForm)form;
		String text = EncodeString.encodeThis(f.getText(),encoding);
		String definition = EncodeString.encodeThis(f.getDefinition(),encoding);
		if (text != null || definition != null)
		{
			Word word = new Word();
			Date date = new Date();
			long time = date.getTime();
			word.setText(text);
			word.setDefinition(definition);
			word.setDateOfEntry(time);
			String file_name = (String)session.getAttribute("file_name");
			store = new FileStorage(context_path, context);
			long word_id = Domartin.getNewID();
			form.reset(mapping, request);
			String subject = (String)user_opts.get("subject");
			FileWordCategoriesManager fwcm = new FileWordCategoriesManager();
			long cat_id = fwcm.getCategoryId(file_name, user_id, subject, context_path);
			Vector category_words = new Vector();
			Word simple_word1 = new Word();
			simple_word1.setDefinition(text);
			simple_word1.setText(definition);
			simple_word1.setId(word_id);
			simple_word1.setCategory(file_name);
			category_words.add(simple_word1);
			fwcm.addWordsToCategory(user_id, cat_id, subject, context_path, category_words, encoding);
		}
		return (mapping.findForward("add_word"));
	}

}