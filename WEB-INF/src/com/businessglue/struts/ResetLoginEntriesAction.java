package com.businessglue.struts;
import java.util.Vector;
import java.util.Hashtable;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.catechis.admin.FileUserUtilities;

public class ResetLoginEntriesAction extends Action
{
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_name = (String)session.getAttribute("user_name");
		String context_path = context.getRealPath("/WEB-INF/");
		FileUserUtilities fut = new FileUserUtilities(context_path);
		fut.resetLoginEntries(context_path);
		Vector users = (Vector)session.getAttribute("users");
		Hashtable user_logins = fut.getUsersLastLogin(users);
		session.setAttribute("user_logins", user_logins);
		return (mapping.findForward("welcome_admin"));
	}
}
