package com.businessglue.struts.remote;

import org.catechis.Storage;
import org.catechis.Domartin;
import org.catechis.FileStorage;
import org.catechis.Transformer;
import org.catechis.JDOMSolution;
import org.catechis.WordTestDateUtility;
import org.catechis.dto.Word;
import org.catechis.dto.Momento;
import org.catechis.dto.AllWordsTest;
import org.catechis.dto.WordTestDates;
import org.catechis.dto.WordTestResult;
import org.catechis.dto.WordNextTestDate;
import org.catechis.file.WordNextTestDates;
import org.catechis.dto.WordLastTestDates;
import org.catechis.file.FileTestRecords;
import org.catechis.testing.TestUtility;
import org.catechis.I18NWebUtility;
import org.catechis.gwangali.RateOfTesting;
import org.catechis.constants.Constants;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;
import java.util.Hashtable;
import java.util.ArrayList;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// if the word is null
import org.catechis.dto.WordTestResult;
import org.catechis.dto.WordLastTestDates;
import org.catechis.admin.FileUserOptions;
import org.catechis.juksong.TestTimeMemory;

/**
*<p>This can be either a reading or a writing test.
*/
public class StudentOptionsAction extends Action
{
	
	/**
	*<p>Send the student options to the client.
	*<student_options>
	*   <option_name>value</option_name>
	*</student_options>
	*/
	public ActionForward perform(ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		Locale loc = request.getLocale();
		String student_id = request.getParameter("student_id");
		context.log("StudentOptionsAction.perform: student id "+student_id+" locale "+loc.toString());
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(student_id, context_path);
		xmlResponse(response, user_opts);
		return (mapping.findForward("student_options"));
	}
	
	private void xmlResponse(HttpServletResponse response, Hashtable students)
	{
		ServletContext context = getServlet().getServletContext(); 
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<student_options>");
		for (Enumeration keys = students.keys(); keys.hasMoreElements() ;) 
		{
			String key = (String)keys.nextElement();
			String val = (String)students.get(key);
			sb.append("<"+key+">"+val+"</"+key+">");
		}
		sb.append("</student_options>");
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
			
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("StudentOptionsAction.log "+(String)log.get(i));
			i++;
		}
	}

}
