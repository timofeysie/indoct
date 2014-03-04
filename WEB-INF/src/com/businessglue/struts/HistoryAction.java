package com.businessglue.struts;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import org.catechis.Storage;
import org.catechis.Transformer;
import org.catechis.admin.FileUserUtilities;
import org.catechis.constants.Constants;
import org.catechis.dto.AllStatsHistory;
import org.catechis.dto.Momento;
import org.catechis.dto.SessionsReport;
import org.catechis.dto.WeeklyReport;
import org.catechis.file.FileTestRecords;
import org.catechis.lists.Sarray;
import org.catechis.lists.WeeklyReports;

/**
 * Load the weekly report and the current sessions status info as well as the the older history file.
 * Put them in the session for the jsp.
 * sessions_report", sessions_report = SessionsReport.
 * session.setAttribute("weekly_reports", weekly_reports =  Hashtable with report name as key, and a WeeklyReport object as a value.
 * session.setAttribute("last_record", last_record = Hashtable.
 * session.setAttribute("all_stats_histories", all_stats_histories = Vector.
 * @author Timothy Curchod
 *
 */
public class HistoryAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		context.log("HistoryAction.perform");
		Storage store = (Storage)session.getAttribute("store");
		String user_id = (String)session.getAttribute("user_id");
		Vector all_stats_histories = store.getAllStatsHistory(user_id);
		String context_path = context.getRealPath("/WEB-INF/");
		FileTestRecords ftr = new FileTestRecords(context_path);
		String subject = Constants.VOCAB;
		Momento old_m = ftr.getStatusRecord(user_id, subject);
	    Hashtable last_record = ftr.getReadingAndWritingLevels();
	    SessionsReport sessions_report = ftr.getSessionsReport();
		WeeklyReports wrs = new WeeklyReports();
		Sarray weekly_reports = new Sarray();
		try
		{
			weekly_reports = wrs.getSortedListOfWeeklyReports(user_id, subject, context_path);
		} catch (java.lang.NullPointerException npe)
		{
			printLog(wrs.getLog(), context);
		}
		printLog(wrs.getLog(), context);
		session.setAttribute("sessions_report", sessions_report);
		session.setAttribute("weekly_reports", weekly_reports);
	    session.setAttribute("last_record", last_record);
		session.setAttribute("all_stats_histories", all_stats_histories);
		return (mapping.findForward("open"));
	}
	
	/*
		try
		{
			all_stats_histories = store.getAllStatsHistory(user_name);
		} catch (java.lang.NumberFormatException nfe)
		{
			context.log("HistoryAction.perform: nfe.  printing store log -------------=--------------");
			printLog(store.getLog(), context);
		}
	 */
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("HistoryAction.log "+(String)log.get(i));
			i++;
		}
	}
	
	/*
	 * Previous notes.
	 * 
	 * This was previously performed on login, but to speed that up,
	* we moved it here, where stats can be calculate on demand*/
	// this should be split up into word stats, in one action
	// and tests stats, which dont really get used anymore.
	// and this history action should remain intact.
	// - take off rate of forgetting chart
	// - instead put one line from the last history entry
	// - add whatever we want to the history file
	// - ie: create of learning/forgetting average, and
	// other metrics like progress  with words in the system.
	//
	// we still need a link to all words on this page
	/*
	FileStorage store = new FileStorage(context_path, context);
	Vector tests = store.getTestCategories(user_name);
	Vector words = store.getWordCategories("primary", user_name);
	AllTestStats all_test_stats = store.getTestStats(tests, user_name);
	AllWordStats all_word_stats = store.getWordStats(words, user_name);
	store.updateHistory(all_test_stats, all_word_stats, user_name);
	
	
	// Sort tests by name
	List list = Arrays.asList(all_test_stats.getTestStats());
	Comparator comparator = new BeanComparator("name");
	Collections.sort(list, comparator);
	*/
	
}
