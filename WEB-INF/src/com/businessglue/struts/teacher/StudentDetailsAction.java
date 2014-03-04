package com.businessglue.struts.teacher;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import java.util.Vector;
import java.util.Hashtable;

import org.catechis.dto.Word;
import org.catechis.Storage;
import org.catechis.Transformer;
import org.catechis.FileStorage;
import org.catechis.file.FileTestRecords;
import com.businessglue.util.EncodeString;

import java.util.Date;

public class StudentDetailsAction extends Action
{
	
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, 				HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		String user = request.getParameter("user");
		ServletContext context = getServlet().getServletContext(); 
		context.log("StudentDetailsAction.perform recieved user "+user);
		String context_path = context.getRealPath("/WEB-INF/");
		FileTestRecords ftr = new FileTestRecords();
		Hashtable last_record = new Hashtable();
		try
		{
			last_record = ftr.getLastUserHistoryHash(user, context_path);
			dumpLog(Transformer.createTable(last_record), context);
		} catch (java.lang.NullPointerException npe)
		{
			context.log("UsersDetailsAction.perform: ");
			dumpLog(ftr.getLog(), context);
		}
		session.setAttribute("last_record", last_record);
		session.setAttribute("user_id", user);
		String encoding = (String)session.getAttribute("encoding");
		return (mapping.findForward("student_details"));
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
