package com.businessglue.struts.teacher;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
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
public class AddStudentsToClassAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String teacher_id = (String)session.getAttribute("teacher_id");
		String class_id   = (String)session.getAttribute("class_id");
		Hashtable class_students = (Hashtable)session.getAttribute("students"); // needed to check for repeats
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getTeacherOptions(teacher_id, context_path);
		String encoding = (String)user_opts.get("encoding");
		context.log("AddStudentsToClassAction: teacher name "+teacher_id+" class_id "+class_id);
		FileUserUtilities fut = new FileUserUtilities(context_path);
		Map <String,String[]> items = request.getParameterMap();
		Vector <String> student_ids = new Vector();
		for (Map.Entry<String,String[]> entry : items.entrySet())
		{
			String student_id = entry.getKey();
			String val[] = entry.getValue();
			int len = val.length;
			int i = 0;
			while (i<len)
			{
				context.log("student_id "+student_id+" student_name "+val[i]);
				if (!class_students.contains(student_id))
				{
					student_ids.add(student_id);
				}
				i++;
			}
		}
		fut.addStudentsToClass(student_ids, class_id, teacher_id);
		context.log("AddStudentsToClassAction: --- fut log after addStudentsToClass--- ");
		printLog(fut.getLog(), context);
		context.log("AddStudentsToClassAction: --- fut log --- end");
		fut.resetLog();
		Hashtable students = fut.getStudents(teacher_id, class_id);
		context.log("AddStudentsToClassAction: --- fut log after getStudents--- ");
		printLog(fut.getLog(), context);
		context.log("AddStudentsToClassAction: --- fut log --- end for getStudents");
		printLog(students, context);
		Vector students_names = getStudentNames(students);
		session.setAttribute("students", students);
	    session.setAttribute("students_names", students_names);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("class_home")); 
	}
	
	/**
	*We assume this is a pair of student id-name pairs.
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

	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log((String)log.get(i));
			i++;
		}
	}
	
	private void printLog(Hashtable log, ServletContext context)
	{
		for (Enumeration e = log.elements() ; e.hasMoreElements() ;) 
		{
			context.log((String)e.nextElement());
		}
	}	

}
