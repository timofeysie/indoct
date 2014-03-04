package com.businessglue.struts;

import java.net.URL;
import java.io.File;
import java.util.Vector;
import java.util.Locale;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

// these are all legacy objects
import com.businessglue.indoct.UserBean;
import com.businessglue.util.GetUserFiles;
import com.businessglue.util.EncodeString;
import com.businessglue.util.CreateJDOMList;

import org.catechis.I18NWebUtility;
import org.catechis.Storage;
import org.catechis.Domartin;
import org.catechis.FileStorage;
import org.catechis.constants.Constants;
import org.catechis.dto.AllWordStats;
import org.catechis.dto.AllTestStats;
import org.catechis.dto.Momento;
import org.catechis.dto.SessionsReport;
import org.catechis.file.FileCategories;
import org.catechis.file.FileTestRecords;
import org.catechis.lists.WeeklyReports;
import org.catechis.admin.FileUserUtilities;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import org.apache.commons.beanutils.BeanComparator;

/**
 * 
 * @author Timothy Curchod
 */
public class LoginAction extends Action
{
	
	//private URL url;
	//private UserBean user;
	//private HttpServletResponse res;
	//private String content_type;
	//private Locale locale;
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		//res = response;
		MessageResources message_resources = getResources();
		//message_resources.
		LoginForm f = (LoginForm) form;
		String user_name = f.getUserName();
		String password = f.getPassword();
		File files_folder = getFilesFolder(response);
		ServletContext context = getServlet().getServletContext(); 
		GetUserFiles guf = new GetUserFiles();
		Hashtable passwords = guf.getUsersPasswords(files_folder, context); // name-password list 
		String file_password = (String)passwords.get(user_name);
		String current_dir = context.getRealPath("/WEB-INF/");
		FileUserUtilities fut = new FileUserUtilities(current_dir);
		context.log("LoginAction.perform: "+user_name);
		printLocale(request);
		fut.addLoginEntry(user_name, current_dir);
		Vector teachers = fut.getTeachers();
		context.log("LoginAction.perform: teachers "+teachers.size());
		dumpLog(teachers,context);
		Vector admins = getAdmins();
		context.log("LoginAction.perform: 5");
		if (admins.contains(user_name)&&file_password.equals(password))
		{
			context.log("ActionForward.perform: loggin in admin");
			loginAdmin(user_name, passwords, request, response);
			return (mapping.findForward("admin"));
		} else if (file_password.equals(password))
		{
			String user_id = fut.getId(user_name);
			context.log("ActionForward.perform: user_id "+user_id);
			if (teachers.contains(user_id))
			{
				context.log("ActionForward.perform: loggin in teacher");
				// teachers is a list of teacher ids.
				loginTeacher(user_name, user_id, request, response);
				return (mapping.findForward("teacher"));
			} else
			{
				context.log("ActionForward.perform: loggin in user");
				// this gets the id as a legacy patch
				loginUser(user_id, user_name, request, response);
				Storage store = new FileStorage(current_dir, context);
				Hashtable user_opts = store.getUserOptions(user_id, current_dir);
				I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
				return (mapping.findForward("success"));
			}
		}
		context.log("ActionForward.perform: login fail - name "+user_name+" pass "+password);
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
		//printLocale(encccoding, request);
		session.setAttribute("users", users);
		session.setAttribute("user_logins", user_logins);
	}
	
	/**
	*<p>Load a list of the teachers classes, set it in the session and return.
	*/
	private void loginTeacher(String user_name, String teacher_id, HttpServletRequest req, HttpServletResponse res)
	{
		HttpSession session = req.getSession();
		ServletContext context = getServlet().getServletContext();
		context.log("LoginAction.loginTeacher "+teacher_id);
		String serv_path = req.getServletPath();
		String context_path = context.getRealPath("/WEB-INF/");
		FileUserUtilities fut = new FileUserUtilities(context_path);
		Hashtable classes = fut.getClasses(teacher_id);
		context.log("LoginAction.loginTeacher: classes "+classes.size());
		dumpLog(fut.getLog(), context);
		session.setAttribute("classes", classes);
		session.setAttribute("teacher_name", user_name);
		session.setAttribute("teacher_id", teacher_id);
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
		HttpSession session = req.getSession();
		ServletContext context = getServlet().getServletContext();
		context.log("LoginAction.loginUser");
		String serv_path = req.getServletPath();
		String context_path = context.getRealPath("/WEB-INF/");
		MessageResources messageResources = getResources();
		FileStorage store = new FileStorage(context_path, context);
		user_name = Domartin.checkOldUser(user_name, user_id, context_path);
		String user_folder = (context_path+File.separator+"files"+File.separator+user_name);
		String file_name = new String(user_name+".options");
		File file_chosen = Domartin.createFileFromUserNPath(file_name, user_folder);
		String file_path = file_chosen.getAbsolutePath();
		Hashtable user_opts = getOptions(file_chosen);
		String encoding = (String)user_opts.get("encoding");
		context.log("LoginAction.loginUser: encoding "+encoding);
		context.log("LoginAction.loginUser: logged in "+user_name);
		Hashtable last_record = savePreviousSession(store, user_id, context_path, Constants.VOCAB, user_opts);
		session.setAttribute("user_name", user_name);			// this used to be used as the id attribute.  everything else can be gotten with this key.
		session.setAttribute("user_path", user_folder);
		session.setAttribute("store", store);
		session.setAttribute("user_options", user_opts);
		// in the future this will be the only thing in the session
		session.setAttribute("user_id", user_id);		// the only thing needed in the future
		session.setAttribute("daily_test_index", "0");		// session counter for tests
		session.setAttribute("last_record", last_record);
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
	
	/*
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
	*/
	
	private Hashtable getOptions(File file)
	{
		ServletContext context = getServlet().getServletContext(); 
		CreateJDOMList jdom = new CreateJDOMList(file, context);
		Hashtable options = jdom.getOptionsHash();
		return options;
	}
	
	/*
	private void setLocale(String full_locale, String encoding, HttpServletResponse res)
	{
		int underscore = full_locale.lastIndexOf("_");
		String language = full_locale.substring(0,underscore);
		String country = full_locale.substring((underscore+1),full_locale.length());
		ServletContext context = getServlet().getServletContext();
		context.log("Encoding set to: Language - "+language+" Country - "+country);
		//locale = new Locale(language, country);
		//content_type = new String("text/html;charset="+encoding);
		//res.setLocale(locale);
		//res.setContentType(content_type);
	}
	*/
	
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
	
	
	private Vector getAdmins()
	{
		Vector admins = new Vector();
		admins.add("admin");
		return admins;
	}
	
	/**
	 * Check to see if 24 hours have passed since a saved session login time..
	 * If yes, then save the session times and number of tests during the period.
	 * The session time will be reset and the number of tests will also be rest.
	 * @param store
	 * @param user_id
	 * @param context_path
	 * @param subject
	 */
	private Hashtable savePreviousSession(Storage store, String user_id, String context_path,
			String subject, Hashtable user_opts)
	{
		ServletContext context = getServlet().getServletContext();
		Date date = new Date();
		long daily_session_end_time = date.getTime();
		Calendar right_now = Calendar.getInstance();
		int year = right_now.get(Calendar.YEAR);
		int week = right_now.get(Calendar.WEEK_OF_YEAR);
		long DAY = 86400000;
		int hour = right_now.get(Calendar.HOUR_OF_DAY);
		FileTestRecords ftr = new FileTestRecords(context_path);
		Momento old_m = ftr.getStatusRecord(user_id, subject);
		int grand_test_index = ftr.getGrandIndex();
		int daily_test_index = ftr.getDailySessionTests();
	    Hashtable last_record = ftr.getReadingAndWritingLevels();
		long daily_session_start_time = ftr.getDailySessionStartTimeUsingSharedDoc();
		int number_of_session_tests = ftr.getDailySessionTests();
		ftr.incrementSessionCount();
		long elapsed_time = daily_session_end_time-daily_session_start_time;
		context.log("LoginAction.savePreviousSession: elapsed_time "+elapsed_time+" > DAY "+DAY+"? or hour is > 4 ? "+hour);
		int saved_week = Integer.parseInt((String)last_record.get("week_of_year"));
		Calendar last_time = Calendar.getInstance();
		last_time.setTimeInMillis(daily_session_start_time);
		int previous_year = right_now.get(Calendar.YEAR);
		// if more than a week: call WeeklyReoprts.saveWeeklyReport
		// then reset all applicable info in ftr.
		context.log("year: "+year+" previous_year: "+previous_year+" week: "+week+" saved_week: "+saved_week);
		if (year>previous_year)
		{
			SessionsReport sr = ftr.getSessionsReport();
			WeeklyReports wr = new WeeklyReports();
			boolean wrote = wr.saveWeeklyReport(user_id, subject, context_path, last_record, sr);
			ftr.resetWeeklySessionStatus(); 
			context.log("LoginAction.savePreviousSession: year>previous_year");
		} else if (week>saved_week||saved_week>52)
		{
			SessionsReport sr = ftr.getSessionsReport();
			WeeklyReports wr = new WeeklyReports();
			boolean wrote = wr.saveWeeklyReport(user_id, subject, context_path, last_record, sr);
			ftr.resetWeeklySessionStatus(); // this is assuming the doc is still live
			context.log("LoginAction.savePreviousSession: week>saved_week||saved_week>52");
		} else if (week==0 && saved_week>0)
		{
			SessionsReport sr = ftr.getSessionsReport(); // after a new year, same as above.  how do we do this in one if?
			WeeklyReports wr = new WeeklyReports();
			boolean wrote = wr.saveWeeklyReport(user_id, subject, context_path, last_record, sr);
			ftr.resetWeeklySessionStatus(); // this is assuming the doc is still live
			context.log("LoginAction.savePreviousSession: week==0 && saved_week>0");
		} else
		{
			SessionsReport sr = ftr.getSessionsReport();
			WeeklyReports wr = new WeeklyReports();
			boolean wrote = wr.saveWeeklyReport(user_id, subject, context_path, last_record, sr);
			ftr.resetWeeklySessionStatus(); 
			context.log("LoginAction.savePreviousSession: not sure why, but we need to start a new year.");
		}
		if (elapsed_time>DAY)
		{
			// if it's more than 24 hours since the daily session login, or after 4 am, save session info
			ftr.getAndResetTestsStatusUsingSharedDoc();
			daily_session_start_time = ftr.getDailySessionStartTime();
			number_of_session_tests = ftr.getDailySessionTests();
			// get test history and word levels
			Vector tests = store.getTestCategories(user_id);
			Vector words = store.getWordCategories("primary", user_id);
			AllTestStats all_test_stats = store.getTestStats(tests, user_id);
			AllWordStats all_word_stats = store.getWordStats(words, user_id);
			// get session start time and number of tests and grand total of tests.
			all_test_stats.setSessionStart(daily_session_start_time);
			all_test_stats.setSessionEnd(daily_session_end_time);
			all_test_stats.setNumberOfSessionTests(number_of_session_tests);
			all_test_stats.setNumberOfTests(grand_test_index);
			store.saveSessionHistory(all_test_stats, all_word_stats, user_id);
			context.log("LoginAction.savePreviousSession: elapsed_time>DAY");
			//printLog(store.getLog());
		}
		ftr.writeDocument();
		// loggins
		context.log("daily_session_start_time "+daily_session_start_time);
		context.log("number_of_session_tests "+number_of_session_tests);
		context.log("elapsed_time "+elapsed_time);
		context.log("number_of_session_tests "+number_of_session_tests);
		context.log("number_of_session_tests "+number_of_session_tests);
		//printLog(ftr.getLog());
		return last_record;
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
}
