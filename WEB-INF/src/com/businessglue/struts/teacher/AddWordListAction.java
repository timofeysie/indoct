package com.businessglue.struts.teacher;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import org.catechis.admin.FileUserUtilities;
import org.catechis.file.FileCategories;
import org.catechis.Domartin;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import org.catechis.Storage;

import com.businessglue.struts.AddCategoryForm;

import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;

/**
*<p>From the add_word_list.jsp in /jsps/teachers/ folder, this method get's called.
*We retrieve the word list name, create the blank file under the newly created word list id,
*and send the user to the next form teacher_import_words.jsp to populate the list. 
*/
public class AddWordListAction extends Action
{
	
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String teacher_id   = (String)session.getAttribute("teacher_id");
		context.log("AddWordListAction.perform: teacher_id   "+teacher_id);
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable teacher_opts = store.getTeacherOptions(teacher_id, context_path); 
		String encoding = (String)teacher_opts.get("encoding");
		AddCategoryForm f = (AddCategoryForm) form;
		FileUserUtilities fut = new FileUserUtilities(context_path);
		String word_list_name = f.getCategory();
		//long new_word_list_id = fut.createNewEntry(teacher_id, new_word_list_name, encoding);
		session.setAttribute("word_list_name", word_list_name);
		try
		{
			I18NWebUtility.forceContentTypeAndLocale(request, response, teacher_opts);
		} catch (java.lang.NullPointerException npe)
		{
			context.log("AddWordListAction.perform: npe while trying to force content type and locale");
		}
		return (mapping.findForward("teacher_import_words"));
	}
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("AddClassAction.log "+(String)log.get(i));
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
