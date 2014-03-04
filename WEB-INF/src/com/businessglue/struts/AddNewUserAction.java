package com.businessglue.struts;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import org.catechis.file.FileCategories;
import org.catechis.admin.FileUserUtilities;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import com.businessglue.struts.AddNewUserForm;

import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;

/**
*<p>Usually, an action gets called like this: name.do > NameAction > name.jsp, but his action
* is called in reverse, ie name.jsp > NameAction > categories_simple.jsp.
* @author Timothy Curchod
*/
public class AddNewUserAction extends Action
{
	
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		context.log("AddNewUserAction.perform");
		String context_path = context.getRealPath("/WEB-INF/");
		AddNewUserForm f = (AddNewUserForm) form;
		String user_name = f.getUserName();
		String password = f.getPassword();
		String e_mail = f.getEmail();
		String invitation_code = f.getInvitationCode();
		FileUserUtilities fut = new FileUserUtilities(context_path);
		// check to see if that username already exists
		Vector actual_users = fut.getUsers(new Vector());
		if (actual_users.contains(user_name))
		{
			context.log("fail: user already exists");
			return (mapping.findForward("fail"));
		}
		// check for invitation
		fut = new FileUserUtilities(context_path);
		boolean allowed = fut.checkInvitationCode(context_path, invitation_code);
		if (allowed == false)
		{
			context.log("fail:a wrong invitation code");
			return (mapping.findForward("fail"));
		}
		fut.addNewUser(user_name, password, e_mail);
		String new_user_id = fut.getId(user_name);
		context.log("AAddNewUserAction.perform: created "+new_user_id+" "+user_name+" "+password+" "+e_mail);
		FileStorage store = new FileStorage(context_path, context);
		//Hashtable user_opts = store.getUserOptions(user_name, context_path);
		Hashtable user_opts = new Hashtable();
		try
		{
			user_opts = store.getUserOptions(new_user_id, context_path);
			context.log("AAddNewUserAction.perform: new user options ---=--------=----------");
			dumpHash(user_opts, context);
			context.log("AAddNewUserAction.perform: new user options ---=--------=----------");
		} catch (java.lang.NullPointerException npe)
		{
			context.log("AAddNewUserAction.perform: store log                       ---=--------=----------");
			printLog(store.getLog(), context);
			context.log("AAddNewUserAction.perform: failed getting new user options ---=--------=----------");
			//dumpHash(user_opts, context);
			//context.log("AAddNewUserAction.perform: new user options created ---=--------=----------");
		}
		Hashtable last_record = setupFirstHistory();
		session.setAttribute("last_record", last_record);
		session.setAttribute("user_id", new_user_id);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		if (actual_users.contains(user_name))
		{
			context.log("fail");
			return (mapping.findForward("fail"));
		}
		context.log("pass");
		return (mapping.findForward("pass"));
	}
	
	private Hashtable setupFirstHistory()
	{
		Hashtable last_record = new Hashtable();
		last_record.put("number_of_words", "0");
		last_record.put("words_at_reading_level_0", "0");
		last_record.put("words_at_writing_level_0", "0");
		last_record.put("words_at_reading_level_1", "0");
		last_record.put("words_at_writing_level_1", "0");
		last_record.put("words_at_reading_level_2", "0");
		last_record.put("words_at_writing_level_2", "0");
		last_record.put("words_at_reading_level_3", "0");
		last_record.put("words_at_writing_level_3", "0");
		last_record.put("reading_average", "0");
		last_record.put("writing_average", "0");
		return last_record;
	}
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("AddNewUserAction.log "+(String)log.get(i));
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