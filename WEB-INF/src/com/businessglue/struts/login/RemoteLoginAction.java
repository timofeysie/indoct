package com.businessglue.struts.login;

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
import org.catechis.dto.SessionsReport;
import org.catechis.file.FileTestRecords;
import org.catechis.file.WordNextTestDates;
import org.catechis.lists.WeeklyReports;

import com.businessglue.util.CreateJDOMList;
import com.businessglue.util.GetUserFiles;

/**
 * We take a name and pass in the header and check a student is registered,
 * and send back an xml document with their status info.
 * The previous session info and state is saved, and a weekly record is made if it has been
 * a week since last login.
 * This method was based on the LoginAction.java file.
 * @author Timothy Curchod
 *
 */
public class  RemoteLoginAction extends Action
{
	
	//private URL url;
	//private UserBean user;
	//private HttpServletResponse res;
	//private String content_type;
	//private Locale locale;
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		ServletContext context = getServlet().getServletContext(); 
		String user_name = request.getParameter("name");
		String password = request.getParameter("pass");
		// parseData(request);
		context.log("name "+user_name+" pass "+password);
		File files_folder = getFilesFolder(response);
		///context.log("RemoteLoginAction.perform: files_folder exists? "+files_folder.exists());
		GetUserFiles guf = new GetUserFiles();
		Hashtable passwords = new Hashtable();
		passwords.put("t","t");
		try
		{
			passwords = guf.getUsersPasswords(files_folder, context); // name-password list 
		} catch (java.lang.NullPointerException nope)
		{
			context.log("RemoteLoginAction.perform: NoPE inf GUF");
		}
		context.log("RemoteLoginAction.perform: passwords "+passwords.size());
		String file_password = (String)passwords.get(user_name);
		String current_dir = context.getRealPath("/WEB-INF/");
		FileUserUtilities fut = new FileUserUtilities(current_dir);
		//context.log("RemoteLoginAction.perform: "+user_name);
		printLocale(request);
		fut.addLoginEntry(user_name, current_dir);
		if (file_password.equals(password))
		{
			String user_id = fut.getId(user_name);
			//context.log("RemoteLoginAction.perform: logging in user_id "+user_id);
			loginUser(user_id, user_name, request, response); // this method closes the stream after it sends the xml.
			return (mapping.findForward("success"));
		}
		//context.log("RemoteLoginAction.perform: login fail - name "+user_name+" pass "+password);
		return (mapping.findForward("failure"));
	}
	
	private void parseData(HttpServletRequest request)
	{
		  ServletContext context = getServlet().getServletContext();
		  StringBuffer jb = new StringBuffer();
		  String line = null;
		  try 
		  {
			  BufferedReader reader = request.getReader();
			  while ((line = reader.readLine()) != null)
		      jb.append(line);
			  //context.log("parseData.line: "+new String(line));
		  } catch (Exception e) 
		  { 
			  /*report an error*/ 
		  }
		  Enumeration keys = request.getParameterNames();
		  while (keys.hasMoreElements())
		    {
			    String key = (String)keys.nextElement();
			    String val = (String)request.getParameter(key);
			    //context.log("key - "+key+" val - "+val);
		    }
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
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		user_name = Domartin.checkOldUser(user_name, user_id, context_path);
		String user_folder = (context_path+File.separator+"files"+File.separator+user_name);
		String file_name = new String(user_name+".options");
		File file_chosen = Domartin.createFileFromUserNPath(file_name, user_folder);
		Hashtable user_opts = getOptions(file_chosen);
		String encoding = (String)user_opts.get("encoding");
		//context.log("LoginAction.loginUser: encoding "+encoding);
		//context.log("LoginAction.loginUser: logged in "+user_name);
		Hashtable last_record = savePreviousSession(store, user_id, context_path, Constants.VOCAB, user_opts);
		xmlResponse(res, last_record, user_id);
	}
	
	/**
	 * Construct the xml response that has a users status info.  After this method, nothing else matters,
	 * as the client will ignore all after the stream is closed.
	 * @param response
	 * @param last_record
	 * @param user_id
	 */
	private void xmlResponse(HttpServletResponse response, Hashtable last_record, String user_id)
	{
		WordNextTestDates wntd = new WordNextTestDates();
		ServletContext context = getServlet().getServletContext(); 
		String context_path = context.getRealPath("/WEB-INF/");
		int waiting_reading_tests = wntd.getWaitingTests(context_path, user_id, Constants.READING, Constants.VOCAB).size();
		int waiting_writing_tests = wntd.getWaitingTests(context_path, user_id, Constants.WRITING, Constants.VOCAB).size();
		//context.log("RemoteLoginAction.xmlResponse: wntd log ---=-----=------=------=------=---");
		//printLog(wntd.getLog());
		//context.log("RemoteLoginAction.xmlResponse: wntd log ---=-----=------=------=------=---");
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<user>");
		sb.append("<user_id>"+user_id+"</user_id>");
		sb.append(convertLastRecord(last_record));
		sb.append("<waiting_reading_tests>"+waiting_reading_tests+"</waiting_reading_tests>");
		sb.append("<waiting_writing_tests>"+waiting_writing_tests+"</waiting_writing_tests>");
		sb.append("</user>");
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
	
	private StringBuffer convertLastRecord(Hashtable last_record)
	{
		StringBuffer sb = new StringBuffer();
		DecimalFormat formatter = new DecimalFormat("###.##");
		sb.append("<number_of_words>"+last_record.get("number_of_words")+"</number_of_words>"); 
		sb.append("<words_at_reading_level_0>"+last_record.get("words_at_reading_level_0")+"</words_at_reading_level_0>");
		sb.append("<words_at_writing_level_0>"+last_record.get("words_at_writing_level_0")+"</words_at_writing_level_0>"); 
		sb.append("<words_at_reading_level_1>"+last_record.get("words_at_reading_level_1")+"</words_at_reading_level_1>");
		sb.append("<words_at_writing_level_1>"+last_record.get("words_at_writing_level_1")+"</words_at_writing_level_1>"); 
		sb.append("<words_at_reading_level_2>"+last_record.get("words_at_reading_level_2")+"</words_at_reading_level_2>"); 
		sb.append("<words_at_writing_level_2>"+last_record.get("words_at_writing_level_2")+"</words_at_writing_level_2>");
		sb.append("<words_at_reading_level_3>"+last_record.get("words_at_reading_level_3")+"</words_at_reading_level_3>"); 
		sb.append("<words_at_writing_level_3>"+last_record.get("words_at_writing_level_3")+"</words_at_writing_level_3>");
		sb.append("<reading_average>");
		try
		{
			sb.append(formatter.format(Double.parseDouble((String)last_record.get("reading_average")))); 
		} catch (java.lang.NullPointerException npe)
		{
			sb.append("n/a"); 
		}
		sb.append("</reading_average>");
		sb.append("<writing_average>");
		try
		{
			sb.append(formatter.format(Double.parseDouble((String)last_record.get("writing_average")))); 			
		} catch (java.lang.NullPointerException npe)
		{
			sb.append("n/a"); 
		}
		sb.append("</writing_average>");
		return sb;
	}
	
	private Hashtable getOptions(File file)
	{
		ServletContext context = getServlet().getServletContext(); 
		CreateJDOMList jdom = new CreateJDOMList(file, context);
		Hashtable options = jdom.getOptionsHash();
		return options;
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
		int week = right_now.get(Calendar.WEEK_OF_YEAR);
		int hour = right_now.get(Calendar.HOUR_OF_DAY);
		FileTestRecords ftr = new FileTestRecords(context_path);
		Momento old_m = ftr.getStatusRecord(user_id, subject);
		//context.log("savePreviousSession 0");
		int grand_test_index = ftr.getGrandIndex();
		//context.log("savePreviousSession 1");
		int daily_test_index = ftr.getDailySessionTests();
		//context.log("savePreviousSession 2");
	    Hashtable last_record = ftr.getReadingAndWritingLevels();
	    //printLog(last_record, context, "last_record");
	    long daily_session_start_time = daily_session_end_time +1;
	    //context.log("savePreviousSession 3");
	    try
	    {
	    	daily_session_start_time = ftr.getDailySessionStartTimeUsingSharedDoc();
	    } catch (java.lang.NullPointerException NoPE)
	    {
	    	context.log("savePreviousSession: NoPE after 3: ftr log ------------------");
	    	printLog(ftr.getLog());
	    }
		//context.log("savePreviousSession 4");
		int number_of_session_tests = ftr.getDailySessionTests();
		//context.log("savePreviousSession 5");
		try
		{
			ftr.incrementSessionCount();
		} catch (java.lang.NullPointerException NoPE)
		{
			context.log("savePreviousSession: ftr NoPE after 5: log ------------------");
			printLog(ftr.getLog());
		}
		long elapsed_time = daily_session_end_time - daily_session_start_time;
		long DAY = 86400000;
		//context.log("LoginAction.savePreviousSession: elapsed_time "+elapsed_time+" > DAY "+DAY+"? or hour is > 4 "+hour);
		int saved_week = Integer.parseInt((String)last_record.get("week_of_year"));
		// if more than a week: call WeeklyReoprts.saveWeeklyReport
		// then reset all applicable info in ftr.
		if (week>saved_week||saved_week>52)
		{
			SessionsReport sr = ftr.getSessionsReport();
			//context.log("savePreviousSession 6");
			WeeklyReports wr = new WeeklyReports();
			boolean wrote = wr.saveWeeklyReport(user_id, subject, context_path, last_record, sr);
			ftr.resetWeeklySessionStatus(); // this is assuming the doc is still live
		} else if (week==0 && saved_week>0)
		{
			SessionsReport sr = ftr.getSessionsReport(); // after a new year, same as above.  how do we do this in one if?
			//context.log("savePreviousSession 7");
			WeeklyReports wr = new WeeklyReports();
			boolean wrote = wr.saveWeeklyReport(user_id, subject, context_path, last_record, sr);
			ftr.resetWeeklySessionStatus(); // this is assuming the doc is still live
		}
		if (elapsed_time>DAY)
		{
			// if it's more than 24 hours since the daily session login, or after 4 am, save session info
			ftr.getAndResetTestsStatusUsingSharedDoc();
			//context.log("savePreviousSession 8");
			daily_session_start_time = ftr.getDailySessionStartTime();
			//context.log("savePreviousSession 9");
			number_of_session_tests = ftr.getDailySessionTests();
			//context.log("savePreviousSession 10");
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
			//context.log("LoginAction.savePreviousSession: elapsed_time>DAY");
			//printLog(store.getLog());
		}
		ftr.writeDocument();
		// loggins
		//context.log("daily_session_start_time "+daily_session_start_time);
		//context.log("number_of_session_tests "+number_of_session_tests);
		//context.log("elapsed_time "+elapsed_time);
		//context.log("number_of_session_tests "+number_of_session_tests);
		//context.log("number_of_session_tests "+number_of_session_tests);
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
