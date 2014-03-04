package com.businessglue.struts;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import java.util.Vector;
import java.util.Hashtable;
import org.catechis.Storage;
import org.catechis.FileStorage;
import com.businessglue.util.EncodeString;

public class UsersOptionsAction extends Action
{
	
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		String encoding = (String)session.getAttribute("encoding");
		ServletContext context = getServlet().getServletContext();
		context.log("UsersOptionsAction.perform");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		Vector users = (Vector)session.getAttribute("users");
		Vector users_opts = new Vector();
		int size = users.size();
		int i = 0;
		while (i<size)
		{
			String user_name = (String)users.get(i);
			Hashtable user_opts = store.getUserOptions(user_name, context_path);
			users_opts.add(user_opts);
			context.log("user "+user_name+" has "+user_opts.size()+" options");
			i++;
		}
		session.setAttribute("users_opts", users_opts);
		return (mapping.findForward("users_options"));
	}

}
