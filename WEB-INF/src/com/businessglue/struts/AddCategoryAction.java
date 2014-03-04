package com.businessglue.struts;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import org.catechis.constants.Constants;
import org.catechis.dto.WordCategory;
import org.catechis.file.FileCategories;
import org.catechis.file.FileWordCategoriesManager;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import org.catechis.Storage;

import com.businessglue.struts.AddCategoryForm;

import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;

/**
*<p>Usually, an action gets called like this: name.do > NameAction > name.jsp, but his action
* is called in reverse, ie name.jsp > NameAction > categories_simple.jsp.
* @author Timothy Curchod
*/
public class AddCategoryAction extends Action
{
	
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_name = (String)session.getAttribute("user_name");
		String user_id   = (String)session.getAttribute("user_id");
		context.log("AddCategoryAction.perform: user_name "+user_name);
		context.log("AddCategoryAction.perform: user_id   "+user_id);
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path); 
		String subject = (String)user_opts.get("subject");
		String encoding = (String)user_opts.get("encoding");
		AddCategoryForm f = (AddCategoryForm) form;
		String category = f.getCategory();
		FileCategories cats = new FileCategories();
		context.log("AddCategoryAction.perform: recieved a request to create "+category);
		cats.addCategory(category, context_path, user_id);
		context.log("AddCategoryAction.perform: cats cats cats");
		printLog(cats.getLog(), context);
		context.log("AAddCategoryAction.perform: cats cats cats");
		cats = new FileCategories(context_path);
		Hashtable categories = cats.getSortedWordCategories2(user_id, encoding);
		String now = Long.toString(new Date().getTime());
		//categories.put(now, category+".xml");
		long new_id = createNewCategory(store, user_id, category, now, subject, context_path, encoding);
		session.setAttribute("categories", categories);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("categories_simple"));
	}
	
	private long createNewCategory(Storage store, String user_id, String cat_name, String now, String subject,
			String context_path, String encoding)
	{
		Vector words = new Vector();
		WordCategory word_cat = new WordCategory();
		word_cat.setCategoryType(Constants.EXCLUSIVE);
		word_cat.setName(cat_name);
		word_cat.setCreationTime(Long.parseLong(now));
		word_cat.setCategoryWords(words);
		FileWordCategoriesManager fwcm = new FileWordCategoriesManager();
		long cat_id = fwcm.addWordCategory(word_cat, user_id, subject, context_path, encoding);
		return cat_id;
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
