package com.businessglue.struts.remote;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.catechis.Domartin;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import org.catechis.Storage;
import org.catechis.admin.FileUserUtilities;
import org.catechis.constants.Constants;
import org.catechis.dto.AllTestStats;
import org.catechis.dto.AllWordStats;
import org.catechis.dto.Momento;
import org.catechis.dto.SavedTest;
import org.catechis.dto.SessionsReport;
import org.catechis.dto.UserInfo;
import org.catechis.file.FileSaveTests;
import org.catechis.file.FileTestRecords;
import org.catechis.file.WordNextTestDates;
import org.catechis.lists.WeeklyReports;

import com.businessglue.util.CreateJDOMList;
import com.businessglue.util.GetUserFiles;

/**
 * FileUserUtilities fut = new FileUserUtilities(context_path);
		Hashtable classes = fut.getClasses(teacher_id); // get the class_id-class_name pairs 
 * @author timmy
 *
 */
public class  SavedTestsListAction extends Action
{
	
	//private URL url;
	//private UserBean user;
	//private HttpServletResponse res;
	//private String content_type;
	//private Locale locale;
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		ServletContext context = getServlet().getServletContext(); 
		String context_path = context.getRealPath("/WEB-INF/");
		String teacher_name = request.getParameter("name");
		String password = request.getParameter("pass");
		String method = "perform"; String this_class = "SavedTestsListAction";
		context.log(this_class+"."+method+": teacher "+teacher_name+" pass "+password);
		File files_folder = getFilesFolder(response);
		GetUserFiles guf = new GetUserFiles();
		Hashtable passwords = guf.getUsersPasswords(files_folder, context); // name-password list 
		context.log(this_class+"."+method+": passwords "+passwords.size());
		String file_password = (String)passwords.get(teacher_name);
		FileUserUtilities fut = new FileUserUtilities(context_path);
		Hashtable name_ids = fut.getUserIds();
		String teacher_id = "0000000000000000001";
		try
		{
			confirmTeacher(name_ids, teacher_name);
		} catch (java.lang.NullPointerException npe )
		{
			teacher_name = "teach";
		}
		printLocale(request);
		if (teacher_id != null)
		{
			fut.addLoginEntry(teacher_name, context_path);
			FileStorage store = new FileStorage(context_path, context);
			String test_id = request.getParameter("test_id");
			Hashtable teacher_opts = store.getTeacherOptions(teacher_id, context_path);
			String encoding = (String)teacher_opts.get("encoding");
			String subject = Constants.VOCAB;
			UserInfo user_info = new UserInfo(encoding, context_path, teacher_id, subject);
			FileSaveTests fst = new FileSaveTests();
			Vector tests = fst.loadClassTestList(user_info);
			xmlResponse(response, tests, teacher_id);
			return (mapping.findForward("success"));
		}
		return (mapping.findForward("failure"));
	}
	
	private String confirmTeacher(Hashtable name_ids, String teacher_name)
	{
		String teacher_id = null;
		for (Enumeration e = name_ids.elements() ; e.hasMoreElements() ;) 
		{
			String key = (String)e.nextElement();
			String val = (String)name_ids.get(key);
			if (val.equals(teacher_name));
			{
				teacher_name = key;
			}
		}
		return teacher_id;
	}
	
	private File getFilesFolder(HttpServletResponse response)
	{
		ServletContext context = getServlet().getServletContext(); 
		String context_path = context.getRealPath("/WEB-INF/");
		String files_path = (context_path+File.separator+"files"
			+File.separator+"user.passes");
		File files_file = new File (files_path);
		return files_file;
	}
	
	/**
	 * Construct the xml response that has a users status info.  After this method, nothing else matters,
	 * as the client will ignore all after the stream is closed.
	 * @param response
	 * @param last_record
	 * @param user_id
	 */
	private void xmlResponse(HttpServletResponse response, Vector tests, String user_id)
	{
		ServletContext context = getServlet().getServletContext(); 
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<saved_tests>");
		for (int i=0;i<tests.size();i++)
		{
			SavedTest st = (SavedTest)tests.get(i);
			sb.append("<saved_test>");
			sb.append("<test_id>"+st.getTestId()+"</test_id>");
			sb.append("<test_name>"+st.getTestName()+"</test_name>");
			sb.append("<creation_time>"+st.getCreationTime()+"</creation_time>");
			sb.append("</saved_test>");
		}
		sb.append("</saved_tests>");
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
			context.log("Could not create response output stream");
		}
	}
	
	private void printLocale(HttpServletRequest request)
	{
		ServletContext context = getServlet().getServletContext();
		Locale locale = request.getLocale();
		String full_locale = locale.toString();
		int underscore;String language;String country;
		try
		{
			underscore = full_locale.lastIndexOf("_");
			try
			{
				language = full_locale.substring(0,underscore);
			} catch (java.lang.StringIndexOutOfBoundsException sioobe)
			{
				language = "sioobe";
			}
			try
			{
				country = full_locale.substring((underscore+1),full_locale.length());
			} catch (java.lang.StringIndexOutOfBoundsException sioobe)
			{
				country = "sioobe";
			}
			context.log("LoginAction.printLocale. found encoding : Language - "+language+" Country - "+country);
		} catch (java.lang.NullPointerException npe)
		{
			context.log("LoginAction.printLocale: npe");
		}
		//content_type = new String("text/html;charset="+encoding);
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
	
	private void printLog(Vector log)
	{
		ServletContext context = getServlet().getServletContext();
		int size = log.size();
		int i = 0;
		while (i<size)
		{
			context.log("Log: "+log.get(i));
			i++;
		}
	}
	
	public void printLog(Hashtable log, ServletContext context, String title)
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
