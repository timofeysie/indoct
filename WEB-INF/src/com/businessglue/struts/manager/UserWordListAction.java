package com.businessglue.struts.manager;

// From LoginAction  Some may not be required...
import java.io.File;
import java.util.Vector;
import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import java.util.Hashtable;
import com.businessglue.util.CreateJDOMList;
import com.businessglue.struts.IndoctAction;

import org.catechis.Domartin;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import org.catechis.file.FileCategories;
import org.catechis.dto.Momento;

/**
<p>Load a file clicked on in user_categories and set the text-definition pairs in the session.
 */
public class UserWordListAction extends IndoctAction
{
	
	/**
	*<p>Take care of business here.
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpServletResponse res = response;
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String context_path = context.getRealPath("/WEB-INF/");
		String user_id = (String)session.getAttribute("user_id");
		String file_name =  (String)request.getParameter("filename");
		context.log("UserWordListAction.perform: "+user_id+" opening file "+file_name);
		FileStorage store = new FileStorage(context_path, context); 
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		String subject = (String)user_opts.get("subject");
		Momento old_m = getMomento(context_path, user_id, subject);
		String file_path = context_path+File.separator+"files"+File.separator+user_id+File.separator+file_name;
		//File file_chosen = Domartin.createFileFromUserNPath(file_name, context_path);
		File file_chosen = new File(file_path);
		CreateJDOMList jdom = new CreateJDOMList(file_chosen, context);
		Hashtable words_defs = jdom.getTextDefHash();
		session.setAttribute("file_name", file_name);
		session.setAttribute("words_defs", words_defs);
		setMomento(context_path, user_id, subject, "UserWordListAction", file_name, "default");
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("open"));
	}

}
