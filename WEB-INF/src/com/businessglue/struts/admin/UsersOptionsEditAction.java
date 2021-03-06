package com.businessglue.struts.admin;

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

/**
*
* session.setAttribute("users", users);
* session.setAttribute("user_logins", user_logins);
*/
public class UsersOptionsEditAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		String encoding = (String)session.getAttribute("encoding");
		ServletContext context = getServlet().getServletContext();
		context.log("UsersOptionsEditAction.perform");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		String admin_id = (String)session.getAttribute("user_name");
		//Hashtable user_opts = store.getUserOptions(admin_id, context_path);
		UsersOptionsEditForm uoef = (UsersOptionsEditForm)form;
		String property = uoef.getProperty();
		String value = uoef.getValue();
		context.log("property "+property);
		context.log("value "+value);
		session.setAttribute("property", property);
		session.setAttribute("value", value);
		return (mapping.findForward("users_options_edit_confirm"));
	}

}
