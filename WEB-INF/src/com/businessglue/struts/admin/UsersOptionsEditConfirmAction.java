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
import org.catechis.admin.FileUserUtilities;
import com.businessglue.util.EncodeString;

/**
*
* session.setAttribute("users", users);
* session.setAttribute("user_logins", user_logins);
*/
public class UsersOptionsEditConfirmAction extends Action
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
		String property = (String)session.getAttribute("property");
		String value = (String)session.getAttribute("value");
		Vector user_ids = (Vector)session.getAttribute("users");
		context.log("property "+property);
		context.log("value "+value);
		FileUserUtilities fut = new FileUserUtilities(context_path);
		context.log("UsersOptionsEditConfirmAction.perform1");
		Vector users = fut.getUsers(getAdmins());
		context.log("UsersOptionsEditConfirmAction.perform1.5");
		fut.changeOption(users, property, value);
		dumpUsers(fut.getLog(), context);
		context.log("UsersOptionsEditConfirmAction.perform2");
		dumpUsers(users, context);
		return (mapping.findForward("users_options_edit"));
	}
	
	/**
	*Users (such as yourself) to exclude from the admins users list.
	* This is why people get paid to write software.
	*/
	private Vector getAdmins()
	{
		Vector admins = new Vector();
		admins.add("admin");
		admins.add("guest");
		admins.add("-5519451928541341468");	// t
		admins.add("-1135042482416826298");	// asdf
		admins.add("-5519451928541341407");	// guest
		admins.add("-5519451928541341007");	// admin
		admins.add("-5810489237107178737");	// jessie
		admins.add("3654028632667884215");	// parkie
		admins.add("230025377069534176");	// buster
		return admins;
	}
	
	private void dumpUsers(Vector log, ServletContext context)
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
