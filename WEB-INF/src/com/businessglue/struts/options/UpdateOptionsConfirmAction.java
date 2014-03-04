package com.businessglue.struts.options;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import org.catechis.FileStorage;
import org.catechis.file.FileCategories;
import org.catechis.I18NWebUtility;
import org.catechis.admin.FileUserOptions;
import org.catechis.dto.WordLastTestDates;
import org.catechis.constants.*;
import org.catechis.file.WordNextTestDates;

import java.util.Date;
import java.util.Vector;
import java.util.Hashtable;

/**
*<p>Usually, an action gets called like this: name.do > NameAction > name.jsp, but his action
* is called in reverse, ie name.jsp > NameAction > categories_simple.jsp.
*/
public class UpdateOptionsConfirmAction extends Action
{
	
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_name = (String)session.getAttribute("user_name");
		String user_id   = (String)session.getAttribute("user_id");
		Vector elt_vector = (Vector)session.getAttribute("elt_vector");
		context.log("UpdateOptionsConfirmAction: user_name "+user_name);
		context.log("UpdateOptionsConfirmAction: user_id   "+user_id);
		String context_path = context.getRealPath("/WEB-INF/");
		String encoding = new String("UTF-8");
		
		FileStorage store = new FileStorage(context_path);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		String type = Constants.READING; // WONT BE USED
		String subject = (String)user_opts.get("subject");
		
		Vector categories = store.getWordCategories(subject, user_id);
		WordLastTestDates wltd = new WordLastTestDates();
		FileUserOptions fuo = new FileUserOptions(context_path);
		// save new values first
   		context.log("UpdateOptionsConfirmAction: context_path   "+context_path);
   		try
   		{
   			fuo.editOption("exclude_level0_test", (String)elt_vector.get(0), user_id);
   			fuo.editOption("exclude_level1_test", (String)elt_vector.get(1), user_id);
   			fuo.editOption("exclude_level2_test", (String)elt_vector.get(2), user_id);
   			fuo.editOption("exclude_level3_test", (String)elt_vector.get(3), user_id);
   			context.log("UpdateOptionsConfirmAction: options updated");
   		} catch (java.lang.NullPointerException npe)
   		{
   			context.log("UpdateOptionsConfirmAction: npe in fuo: log --------- ");
   			printLog(fuo.getLog(), context);
   		}
   		context.log("UpdateOptionsConfirmAction: create new lists --------- ");
		// create new list.  fuo will reload the elt values.
		fuo.createNewLists(user_id, subject, context_path);		
	
		// get the number of waiting tests and set it in the options for display on the options page.
		WordNextTestDates wntds = new WordNextTestDates();
		Vector waiting_tests = wntds.getWaitingTests(context_path, user_id, Constants.READING, subject);
		int waiting_reading_tests = waiting_tests.size();
		waiting_tests = wntds.getWaitingTests(context_path, user_id, Constants.WRITING, subject);
		int waiting_writing_tests = waiting_tests.size();
		user_opts.put("waiting_reading_tests", Integer.toString(waiting_reading_tests));
		user_opts.put("waiting_writing_tests", Integer.toString(waiting_writing_tests));
		
		// re-get user_options to be displayed on the options page
		user_opts = store.getUserOptions(user_id, context_path);
		session.setAttribute("user_options", user_opts);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("options"));
	}
	
	private void printLog(Vector log, ServletContext context)
	{
		try
		{
			int total = log.size();
			int i = 0;
			while (i<total)
			{
				context.log("UpdateOptionsConfirmAction.log "+(String)log.get(i));
				i++;
			}
		} catch (java.lang.ClassCastException cce)
		{
			context.log("UpdateOptionsConfirmAction.log cce");
		}
	}

}
