package com.businessglue.struts.login;

import java.net.URL;
import java.io.File;
import java.util.Date;
import java.util.Vector;
import java.util.Locale;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

// these are all legacy objects
import com.businessglue.indoct.UserBean;
import com.businessglue.util.GetUserFiles;
import com.businessglue.util.EncodeString;
import com.businessglue.util.CreateJDOMList;
import com.businessglue.struts.LoginForm;


import org.catechis.Storage;
import org.catechis.Domartin;
import org.catechis.FileStorage;
import org.catechis.dto.AllWordStats;
import org.catechis.dto.AllTestStats;
import org.catechis.dto.Momento;
import org.catechis.file.FileCategories;
import org.catechis.file.FileTestRecords;
import org.catechis.admin.FileUserUtilities;
import org.catechis.constants.Constants;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import org.apache.commons.beanutils.BeanComparator;

public class UserLoginAction extends Action
{
	
	private URL url;
	private UserBean user;
	//private HttpServletResponse res;
	private String content_type;
	private Locale locale;
	
	// org.apache.jsp.user_005flogin_jsp._jspService(user_005flogin_jsp.java:123)
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		//res = response;
		LoginForm f = (LoginForm) form;
		String user_name = f.getUserName();
		String password = f.getPassword();
		File files_folder = getFilesFolder(response);
		ServletContext context = getServlet().getServletContext(); 
		context.log("ActionForward.perform: Starting login with passes in "+files_folder.getAbsolutePath());
		GetUserFiles guf = new GetUserFiles();
		Hashtable passwords = guf.getUsersPasswords(files_folder, context);
		String file_password = (String)passwords.get(user_name);
		String current_dir = context.getRealPath("/WEB-INF/"); 
		FileUserUtilities fut = new FileUserUtilities(current_dir);
		fut.addLoginEntry(user_name, current_dir);
		Vector admins = getAdmins();
		if (admins.contains(user_name)&&file_password.equals(password))
		{
			loginAdmin(user_name, passwords, request, response);
			return (mapping.findForward("admin"));
		} else if (file_password.equals(password))
		{
			context.log("ActionForward.perform: loggin in user");
			// this gets the id as a legacy patch
			String user_id = fut.getId(user_name);
			loginUser(user_id, user_name, request, response);
			return (mapping.findForward("success"));
		}
		return (mapping.findForward("failure"));
	}

	/**
	*<p>And admin needs a list of users to work with.
	*<p>The passwords are left behind.
	*<p>To get an admin view of user sessions:
	*<p>jasonb -  You can put, say, an ArrayList as a context attribute, 
	and the ArrayList can store all of the user names.  
	And, then, within any given request, a JSP, servlet, 
	or filter could access that ArrayList.
	This is probably most efficient done in the database, 
	where you can run one query and either get a row or no rows.
	(From #tomcat on FreeNode servers)
	*/
	private void loginAdmin(String user_name, Hashtable passwords, HttpServletRequest req, HttpServletResponse res)
	{
		HttpSession session = req.getSession();
		ServletContext context = getServlet().getServletContext();
		context.log("LoginAction.loginAdmin "+user_name);
		String serv_path = req.getServletPath();
		String context_path = context.getRealPath("/WEB-INF/");
		Vector users = new Vector();
		Vector admins = getAdmins();
		for (Enumeration e = passwords.keys() ; e.hasMoreElements() ;)
		{
			String user = (String)e.nextElement();
			if (admins.contains(user))
			{
				// dont add admins
			} else
			{
				users.add(user);
				context.log("add "+user);
			}
		}
		FileStorage store = new FileStorage(context_path, context);
		
		String user_folder = (context_path+File.separator+"files"+File.separator+user_name);
		String file_name = new String(user_name+".options");
		
		File file_chosen = Domartin.createFileFromUserNPath(file_name, user_folder);
		String file_path = file_chosen.getAbsolutePath();
		Hashtable user_opts = getOptions(file_chosen);
		FileUserUtilities fut = new FileUserUtilities(context_path);
		Hashtable logins = fut.getLoginEntries(context_path);
		Hashtable user_logins = fut.getUsersLastLogin(users);
		String encoding = (String)user_opts.get("encoding");
		setLocale((String)user_opts.get("locale"), encoding, res);
		session.setAttribute("users", users);
		session.setAttribute("user_logins", user_logins);
	}
	
	private String getUserPath(HttpServletRequest req)
	{
		String serv_path = req.getServletPath();
		ServletContext context = getServlet().getServletContext(); 
		String temp_path = context.getRealPath(".");
		return temp_path;
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
	* This is as legacy as this code gets.  This was  one of the first methods written, and has
	* all sorts of problems, which I am about to begin the process of fixing.
	* First off, take out the statistics and put it in the getStats method.
	* This was taking a hell of a time to login, which means that really,
	* the stats methods themselves suck, but that comes later, as it really should
	* be done in a db, a la hibernate and mysql...
	*/
	public void loginUser(String user_id, String user_name, HttpServletRequest req, HttpServletResponse res)
	{
		String subject = Constants.VOCAB;
		HttpSession session = req.getSession();
		ServletContext context = getServlet().getServletContext();
		context.log("LoginAction.loginUser");
		String serv_path = req.getServletPath();
		String context_path = context.getRealPath("/WEB-INF/");
		
		FileStorage store = new FileStorage(context_path, context);
		user_name = Domartin.checkOldUser(user_name, user_id, context_path);
		String user_folder = (context_path+File.separator+"files"+File.separator+user_name);
		String file_name = new String(user_name+".options");
		File file_chosen = Domartin.createFileFromUserNPath(file_name, user_folder);
		String file_path = file_chosen.getAbsolutePath();
		Hashtable user_opts = getOptions(file_chosen);
		String encoding = (String)user_opts.get("encoding");
		setLocale((String)user_opts.get("locale"), encoding, res);
		File user_files_dir = new File(user_folder);
		String[] files_array = user_files_dir.list();
		Vector files = new Vector();
		EncodeString encoder = new EncodeString();
		for (int i = 0; i < files_array.length; i++) 
		{
			String file = encoder.encodeThis(files_array[i]);
			files.add(file);
		}
		context.log("LoginAction.loginUser: logged in "+user_name);
		FileTestRecords ftr = new FileTestRecords();
		Hashtable last_record = new Hashtable();
		try
		{
			last_record = ftr.getLastUserHistoryHash(user_name, context_path);
			dumpLog(ftr.getLog(), context);
		} catch (java.lang.NullPointerException npe)
		{
			dumpLog(ftr.getLog(), context);
		} catch (java.lang.IndexOutOfBoundsException ioob)
		{
			dumpLog(ftr.getLog(), context);
		}
		session.setAttribute("session_object", last_record);	// this object can be whatever the jsp in going to need, in this case, a Hash hold the last record of history file.
		session.setAttribute(Constants.USER_ID, user_id);		// the only thing needed in the future
		session.setAttribute(Constants.SUBJECT, Constants.VOCAB);
		Momento new_m = new Momento("UserLoginAction", Long.toString(new Date().getTime()), "default", "default");
		ftr = new FileTestRecords(context_path);
		ftr.setMomentoObject(user_id, subject, new_m);		// rather than session bloat, and for session ressurrection, we store a momento.
	}
	
	/*	Moved to Domartin
	*If the folder is named after the user_id, then the user_name needs to be
	* set as the id, as the user_name used to be used as the id.
	*<p>Returns the users name if there is a folder under the users name.
	*<p>Returns the users id if there is no folder under their name, cause its named after their id.
	*
	private String checkOldUser(String user_name, String user_id, String context_path)
	{
		String user_folder = (context_path+File.separator+"files"+File.separator+user_name);
		File folder = new File(user_folder);
		if (folder.exists())
		{
			return user_name;
		}
		return user_id;
	}*/
	
	private void loginGuest(String guest, HttpServletRequest request)
	{
		ServletContext context = getServlet().getServletContext();
		try
		{
			url = context.getResource("/login.jsp");
		}
		catch (java.net.MalformedURLException e)
		{
			context.log("Malformed! - "+e.toString());
		}
		context.log("Guest - " + url.getPath());
		GetUserFiles guf = new GetUserFiles();
		String folder = guf.findUserPath(guest, context);
		Vector files = guf.getFolderFiles(folder);
		user = new UserBean();
		user.setUserName(guest);
		user.setPath(folder);
		user.putFileList(files);
		HttpSession session = request.getSession();
		session.setAttribute("USER_KEY", user);
		session.setAttribute("user_name", guest);
		session.setAttribute("user_path", folder);
	}
	
	private Hashtable getOptions(File file)
	{
		ServletContext context = getServlet().getServletContext(); 
		CreateJDOMList jdom = new CreateJDOMList(file, context);
		Hashtable options = jdom.getOptionsHash();
		return options;
	}
	
	private void setLocale(String full_locale, String encoding, HttpServletResponse res)
	{
		int underscore = full_locale.lastIndexOf("_");
		String language = full_locale.substring(0,underscore);
		String country = full_locale.substring((underscore+1),full_locale.length());
		ServletContext context = getServlet().getServletContext();
		context.log("Encoding set to: Language - "+language+" Country - "+country);
		locale = new Locale(language, country);
		content_type = new String("text/html;charset="+encoding);
		res.setLocale(locale);
		res.setContentType(content_type);
	}
	
	private Vector getAdmins()
	{
		Vector admins = new Vector();
		admins.add("admin");
		return admins;
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