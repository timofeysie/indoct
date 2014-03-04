package com.businessglue.struts.remote;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.admin.FileUserUtilities;
import org.catechis.admin.FileUserOptions;
import org.catechis.dto.SavedTest;

import com.businessglue.struts.admin.AddUsersOptionsForm;
import com.businessglue.struts.AddWordForm;
import com.businessglue.util.EncodeString;

/**
<p>This action gets called from add_users_options.jsp which users the form to get
* an option name and an initial value, which will be added to all the users options files.
*/
public class StudentNamesAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		context.log("ClassHomeAction.perform");
		String context_path = context.getRealPath("/WEB-INF/");
		String teacher_id = request.getParameter("teacher_id");
		String class_id = request.getParameter("class_id");
		FileUserUtilities fut = new FileUserUtilities(context_path);
		context.log("ClassHomeAction.perform: teacher_id: "+teacher_id+" class "+class_id+" log +|+|+|+|+");
		Hashtable students = fut.getStudents(teacher_id, class_id); // returns stuydent ids-name pairs
		session.setAttribute("students", students);
		xmlResponse(response, students);
		return (mapping.findForward("student_names"));
	}
	
	private void xmlResponse(HttpServletResponse response, Hashtable students)
	{
		ServletContext context = getServlet().getServletContext(); 
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<students>");
		for (Enumeration keys = students.keys(); keys.hasMoreElements() ;) 
		{
			String student_id = (String)keys.nextElement();
			String student_name = (String)students.get(student_id);
			sb.append("<student>");
			sb.append("<student_id>"+student_id+"</student_id>");
			sb.append("<student_name>"+student_name+"</student_name>");
			sb.append("</student>");
		}
		sb.append("</students>");
		response.setContentType("text/xml");
		response.setContentLength(sb.length()); 
		PrintWriter out;
		try 
		{
		    out = response.getWriter();
			out.println(sb.toString()); 
			out.close();
			out.flush();
		} catch (IOException e) 
		{
			context.log("Could not steal output stream");
		}
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
