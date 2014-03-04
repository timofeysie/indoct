package com.businessglue.struts;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Map;

import org.catechis.dto.Word;
import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;

/**
 * 
* @author Timothy Curchod
 *
 */
public class DeleteWordAction extends Action
{
	
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		String file_name = (String)session.getAttribute("file_name");	// This should come from the request
		ServletContext context = getServlet().getServletContext();
		String values = request.getParameter("text");
		int div = values.indexOf(";");
		int end = values.length();
		String text = values.substring(0,div);
		String def_sub  = values.substring(div+1,end);
		div = def_sub.indexOf("=");
		end = def_sub.length();
		String def  = def_sub.substring(div+1,end);
		context.log(this.getClass().getName()+".perform");
		String user_name = (String)session.getAttribute("user_name");	// this is the only thing that should be in the session
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_name, context_path);
		Word word = new Word();
		word.setText(text);
		word.setDefinition(def);
		context.log("text "+text);
		context.log("def  "+def);
		context.log("user "+user_name);
		context.log("context "+context_path);
		String encoding = (String)user_opts.get("encoding");
		store.deleteWord(word, file_name, user_name, encoding);
		printLog(store.getLog(), context);
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