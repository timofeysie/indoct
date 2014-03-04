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
//import com.businessglue.util.EncodeString;
import org.catechis.constants.*;
import org.catechis.EncodeString;
import org.catechis.file.WordNextTestDates;

public class UserOptionsAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		String encoding = (String)session.getAttribute("encoding");
		ServletContext context = getServlet().getServletContext();
		context.log("UsersOptionsAction.perform");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		String user_id = (String)session.getAttribute("user_id");
		context.log("UsersOptionsAction.perform user_id "+user_id);
		Hashtable user_opts = new Hashtable();
		try
		{
			user_opts = store.getUserOptions(user_id, context_path);
		} catch (java.lang.NullPointerException npe)
		{
			context.log("UsersOptionsAction.perform store log start (((");
			dumpLog(store.getLog(), context);
			context.log("UsersOptionsAction.perform store log end )))))");
		}
		// get the number of waiting tests and set it in the options for display on the options page.
		String subject = (String)user_opts.get("subject");
		WordNextTestDates wntds = new WordNextTestDates();
		Vector waiting_tests = wntds.getWaitingTests(context_path, user_id, Constants.READING, subject);
		int waiting_reading_tests = waiting_tests.size();
		waiting_tests = wntds.getWaitingTests(context_path, user_id, Constants.WRITING, subject);
		int waiting_writing_tests = waiting_tests.size();
		user_opts.put("waiting_reading_tests", Integer.toString(waiting_reading_tests));
		user_opts.put("waiting_writing_tests", Integer.toString(waiting_writing_tests));
		session.setAttribute("user_opts", user_opts);
		return (mapping.findForward("options"));
	}
	
	private void dumpLog(Vector log, ServletContext context)
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
