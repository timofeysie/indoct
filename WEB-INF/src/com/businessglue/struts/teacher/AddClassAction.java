package com.businessglue.struts.teacher;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import org.catechis.admin.FileUserUtilities;
import org.catechis.file.FileCategories;
import org.catechis.Domartin;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import org.catechis.Storage;

import com.businessglue.struts.AddCategoryForm;

import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;

/**
*<p>From the add_class.jsp in /jsps/teachers/ folder, this action get's called.
*/
public class AddClassAction extends Action
{
	
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String teacher_id   = (String)session.getAttribute("teacher_id");
		context.log("AddClassAction.perform: teacher_id   "+teacher_id);
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable teacher_opts = store.getTeacherOptions(teacher_id, context_path); 
		context.log("store log  -----=----------=");
		printLog(store.getLog(), context);
		context.log("teacher options  -----=----------=--------");
		dumpHash(teacher_opts, context);
		String encoding = (String)teacher_opts.get("encoding");
		AddCategoryForm f = (AddCategoryForm) form;
		FileUserUtilities fut = new FileUserUtilities(context_path);
		String new_class = f.getCategory();
		long new_class_id = fut.createClass(teacher_id, new_class, encoding);
		// now get all the classes, hopefully with the new class added and add it to the session
		// so it's there next time the teacher goes to their classes page.
		Hashtable classes = fut.getClasses(teacher_id);
		context.log("AddClassAction.perform: classes "+classes.size());
		session.setAttribute("classes", classes);
		dumpHash(classes, context);
		// now create blank class info and send the teacher to the blank class page
		// so they can add students to the class
		Hashtable students = new Hashtable();
		printLog(fut.getLog(), context);
		Vector students_names = new Vector();
		Hashtable students_logins = new Hashtable();
		session.setAttribute("class_id", new_class_id+"");
		session.setAttribute("class_name", new_class);
		session.setAttribute("students", students);
		session.setAttribute("students_names", students_names);
		session.setAttribute("students_logins", students_logins);
		try
		{
			I18NWebUtility.forceContentTypeAndLocale(request, response, teacher_opts);
		} catch (java.lang.NullPointerException npe)
		{
			context.log("AddClassAction.perform: npe while trying to force content type and locale");
		}
		return (mapping.findForward("class_home"));
	}
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("AddClassAction.log "+(String)log.get(i));
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
