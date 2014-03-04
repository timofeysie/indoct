package com.businessglue.struts.teacher;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import java.util.Date;
import java.util.Locale;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import org.catechis.Storage;
import org.catechis.dto.Word;
import org.catechis.Domartin;
import org.catechis.FileStorage;
import org.catechis.admin.FileUserOptions;
import org.catechis.admin.FileUserUtilities;
import org.catechis.filter.ImportListFilter;

import com.businessglue.struts.ImportListForm;
import com.businessglue.util.EncodeString;
import org.catechis.I18NWebUtility;

/**
 * Create a new user for every "user_name=e-mail;" string passed in from the form.
 * The Hashtable user_ids is then set in the session which is the user_name, new_password.
 * The new password should be "user_name+123" where 123 is a random three digit number.
 * @author Timothy Curchod
 *
 */
public class ChooseStudentsToAddToClassAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		context.log("ChooseStudentsToAddToClassAction.perform");
		String teacher_id = (String)session.getAttribute("teacher_id");
		String class_id   = (String)session.getAttribute("class_id");
		String context_path = context.getRealPath("/WEB-INF/");
		Hashtable class_students = (Hashtable)session.getAttribute("students"); 
		FileStorage store = new FileStorage(context_path, context);
		Hashtable <String,String>user_opts = store.getTeacherOptions(teacher_id, context_path);
		String encoding = (String)user_opts.get("encoding");
		FileUserUtilities fut = new FileUserUtilities(context_path);
		Hashtable students_available = fut.getUserIds();
		
		System.out.println("students_available b4 minus -------------------------");
		printLog(students_available, context);
		System.out.println("students_available b4 minus -------------------------");
		
		System.out.println("students_available jsp log b4 minus -------------------------");
		jspLog(students_available, context);
		System.out.println("students_available jsp log b4 minus -------------------------");
		
		System.out.println("fut log -------------------------");
		printLog(fut.getLog(), context);
		System.out.println("fut log -------------------------");
		
		System.out.println("class_students b4 minus -------------------------");
		jspLog(class_students, context);
		System.out.println("class_students b4 minus -------------------------");
		
		students_available = minusCurrentClassMembers(class_students, students_available);
		
		System.out.println("students_available after minus -------------------------");
		printLog(students_available, context);
		System.out.println("students_available after minus -------------------------");
		
		System.out.println("students_available jsp log after -------------------------");
		jspLog(students_available, context);
		System.out.println("students_available jsp log after -------------------------");
		
		context.log("ChooseStudentsToAddToClassAction.perform: students_available "+students_available.size());
		session.setAttribute("students_available", students_available);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("choose_students_to_add_to_class")); 
	}
	
	/**
	 * Go through class students and remove them from the all students list so that we are 
	 * left with a list of students who are not in the class.
	 * @param class_students
	 * @param all_students
	 * @return
	 */
	private Hashtable minusCurrentClassMembers(Hashtable class_students, Hashtable all_students)
	{
		
		ServletContext context = getServlet().getServletContext();
		context.log("ChooseStudentsToAddToClassAction.minusCurrentClassMembers: class_students "+class_students.size());
		context.log("ChooseStudentsToAddToClassAction.minusCurrentClassMembers:   all_students "+all_students.size());
		for (Enumeration e = class_students.elements() ; e.hasMoreElements() ;) 
		{
			String key = (String)e.nextElement();
			String val = (String)class_students.get(key);
			all_students.remove(key);
			all_students.remove("teach");
			all_students.remove("admin");
			all_students.remove("0000000000000000001");
			all_students.remove("-5519451928541341007");
		}
		return all_students;
	}
	
	private void printLog(Hashtable log, ServletContext context)
	{
		for (Enumeration e = log.elements() ; e.hasMoreElements() ;) 
		{
			context.log((String)e.nextElement());
		}
	}	
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("log "+(String)log.get(i));
			i++;
		}
	}
	
	private void jspLog(Hashtable students_available, ServletContext context)
	{
		for (Enumeration e = students_available.keys() ; e.hasMoreElements() ;) 
		{
			String id = (String)e.nextElement();
			String name = (String)students_available.get(id);
			context.log("id "+id+" name   "+name);
		}
	}

}
