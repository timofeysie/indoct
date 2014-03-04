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
import org.catechis.admin.FileUserOptions;
import com.businessglue.struts.admin.AddUsersOptionsForm;
import com.businessglue.struts.AddWordForm;
import com.businessglue.util.EncodeString;

/**
<p>This action gets called from add_users_options.jsp which users the form to get
* an option name and an initial value, which will be added to all the users options files.
*/
public class AddUsersOptionsAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		String encoding = (String)session.getAttribute("encoding");
		ServletContext context = getServlet().getServletContext();
		context.log("UsersOptionsAction.perform");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		//Vector users = (Vector)session.getAttribute("users");
		Vector users_opts = new Vector();
		FileUserUtilities fut = new FileUserUtilities(context_path);
		Vector admins = new Vector();
		admins.add("admin");
		Vector users = fut.getUsers(admins);
		AddWordForm auof = (AddWordForm)form;
		String option = auof.getText();
		String initial_value = auof.getDefinition();
		FileUserOptions fuo = new FileUserOptions(context_path);
		fuo.addUsersOptions(option, initial_value, users);
		return (mapping.findForward("welcome_admin"));
	}

}
