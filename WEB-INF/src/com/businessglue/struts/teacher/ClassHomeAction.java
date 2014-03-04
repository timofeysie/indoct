package com.businessglue.struts.teacher;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
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
public class ClassHomeAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		context.log("ClassHomeAction.perform");
		String teacher_id = (String)session.getAttribute("teacher_id");
		String context_path = context.getRealPath("/WEB-INF/");
		String class_id = request.getParameter("class_id");
		FileUserUtilities fut = new FileUserUtilities(context_path);
		context.log("ClassHomeAction.perform: teacher_id: "+teacher_id+" class "+class_id+" log +|+|+|+|+");
		Hashtable students = fut.getStudents(teacher_id, class_id); // returns stuydent ids-name pairs
		
		printLog(students, context, "students hash");
		printLog(fut.getLog(), context, "fut log after getStudents");
		fut.resetLog();
		
		Vector students_names = getStudentNames(students); // creates a simple vecor of names
		
		printLog(students_names, context, "students_names");
		printLog(fut.getLog(), context, "fut log after getStudents");
		fut.resetLog();
		
		Hashtable students_logins = fut.getUsersLastLogin(students_names);
		printLog(students_logins, context, "students_getUsersLastLogin");
		printLog(fut.getLog(), context, "fut log after getUsersLastLogin");
		fut.resetLog();
		
		Hashtable classes = fut.getClasses(teacher_id); 
		
		printLog(classes, context, "fut.getClasses(teacher_id)");
		
		String class_name = (String)classes.get(class_id);
		
		context.log("ClassHomeAction.perform class_name "+class_name+" (from request)class_id "+class_id);
		
		session.setAttribute("class_name", class_name);
		session.setAttribute("class_id", class_id);
		session.setAttribute("students", students);
		session.setAttribute("students_names", students_names);
		session.setAttribute("students_logins", students_logins);
		return (mapping.findForward("class_home"));
	}
	
	/**
	*I assume this is a pair of student id-name pairs.
	*/
	private Vector getStudentNames(Hashtable students)
	{
		Vector names = new Vector();
		for (Enumeration keys = students.keys(); keys.hasMoreElements() ;) 
		{
			String key = (String)keys.nextElement();
			String name = (String)students.get(key);
        		names.add(name);
		}
		return names;
	}
	
	private void printLog(Vector log, ServletContext context, String title)
	{
		context.log("=================_ "+title+" _=================");
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("log "+(String)log.get(i));
			i++;
		}
	}
	
	private void printLog(Hashtable log, ServletContext context, String title)
	{
		context.log("=================_ "+title+" _=================");
		for (Enumeration e = log.elements() ; e.hasMoreElements() ;) 
		{
			String key = (String)e.nextElement();
			String val = (String)log.get(key);
			context.log(key+" - "+val);
		}
	}	


}
