package com.businessglue.struts;

import java.util.Vector;
import java.util.Hashtable;
import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import org.catechis.constants.Constants;
import org.catechis.dto.AllStatsHistory;
import org.catechis.dto.AllWordStats;
import org.catechis.dto.AllTestStats;
import org.catechis.dto.Momento;
import org.catechis.file.FileCategories;
import org.catechis.file.FileTestRecords;

public class RecalculateStatsAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		context.log("RecalculateStatsAction.perform: yes it is.");
		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		Vector tests = store.getTestCategories(user_id);
		Vector words = store.getWordCategories("primary", user_id);
		AllTestStats all_test_stats = store.getTestStats(tests, user_id);
		AllWordStats all_word_stats = store.getWordStats(words, user_id);
		//store.updateHistory(all_test_stats, all_word_stats, user_id);
		long time = Long.parseLong("0");
		try
		{
			time = store.updateHistory(all_test_stats, all_word_stats, user_id);
			context.log("RecalculateStatsAction.perform: time "+time);
		} catch (java.lang.NoClassDefFoundError ncdfe)
		{
			context.log("RecalculateStatsAction.perform: ncdfe");
			printLog(store.getLog());
		}
		context.log("RecalculateStatsAction.perform: go to teacher version");
		FileTestRecords ftr = new FileTestRecords(context_path);
		Momento old_m = ftr.getStatusRecord(user_id, Constants.VOCAB);
		Hashtable last_record = ftr.getReadingAndWritingLevels();
		session.setAttribute("last_record", last_record);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		String teacher_id = (String)session.getAttribute("teacher_id");
		if (old_m == null)
		{
			context.log("RecalculateStatsAction.perform: go to teacher version "+teacher_id+" "+old_m);
			return (mapping.findForward("student_details"));  // go to a teachers version
		}
		return (mapping.findForward("welcome"));
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
