package com.businessglue.struts.manager;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import org.catechis.dto.Word;
import org.catechis.file.FileCategories;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import org.catechis.Storage;
import org.catechis.Transformer;

import com.businessglue.struts.AddCategoryForm;

import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;

/**
*<p>Usually, an action gets called like this: name.do > NameAction > name.jsp, but his action
* is called in reverse, ie name.jsp > NameAction > categories_simple.jsp.
*/
public class FindWordAction extends Action
{
	
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_name = (String)session.getAttribute("user_name");
		String user_id   = (String)session.getAttribute("user_id");
		context.log("FindWordAction.perform: user_name "+user_name);
		context.log("FindWordAction.perform: user_id   "+user_id);
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path); 
		String encoding = (String)user_opts.get("encoding");
		AddCategoryForm f = (AddCategoryForm) form;
		String text_or_def = f.getCategory();
		Vector categories = store.getWordCategories("", user_id);
		Vector found_words = findWords(user_id,categories, text_or_def, context_path);
		session.setAttribute("found_words", found_words);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("found_words"));
	}
	
	private Vector findWords(String user_id, Vector categories, String text_or_def, String context_path)
	{
		FileStorage store = new FileStorage(context_path);
		Vector found_words = new Vector();
		int i = 0;
		int size = categories.size();
		while (i<size)
		{
			String category = (String)categories.get(i);
			Vector words = store.getWordObjects(category, user_id);
			int j = 0;
			int j_size = words.size();
			while (j<j_size)
			{
				Word word = (Word)words.get(j);
				String text = word.getText();
				String def = word.getDefinition();
				if (text_or_def.contains(text)||text_or_def.contains(def))
				{
					found_words.add(word); // collect all words matching.
				}
				j++;
			}
			i++;
		} 
		return found_words;
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
	
	private void dumpHash(Hashtable hash, ServletContext context)
	{
	    Enumeration keys = hash.keys();
	    while (keys.hasMoreElements())
	    {
		    String key = (String)keys.nextElement();
		    String val = (String)hash.get(key);
		    context.log(key+" - "+val);
	    }
	}

}
