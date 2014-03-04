package com.businessglue.struts.teacher;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.text.DecimalFormat;

import org.catechis.constants.Constants;
import org.catechis.dto.Momento;
import org.catechis.dto.SessionsReport;
import org.catechis.dto.WeeklyReport;
import org.catechis.dto.Word;
import org.catechis.Storage;
import org.catechis.Transformer;
import org.catechis.FileStorage;
import org.catechis.file.FileTestRecords;
import org.catechis.lists.WeeklyReports;

import com.businessglue.util.EncodeString;

import java.util.Date;

public class ClassWeeklyReportAction extends Action
{
	
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		String teacher_id = (String)session.getAttribute("teacher_id");
		String class_id = (String)session.getAttribute("class_id");
		Hashtable students = (Hashtable)session.getAttribute("students");
		//Vector students_names = session.getAttribute("students_names", students_names);
		//Hashtable students_logins = (String)session.getAttribute("students_logins");
		ServletContext context = getServlet().getServletContext(); 
		context.log("UsersDetailsAction.perform "+teacher_id);
		String context_path = context.getRealPath("/WEB-INF/");
		Enumeration student_ids = students.keys();
		Hashtable student_records = new Hashtable();
		while (student_ids.hasMoreElements())
		{
			String student_id = (String)student_ids.nextElement();
			try
			{
				FileTestRecords ftr = new FileTestRecords(context_path);
				Momento old_m = ftr.getStatusRecord(student_id, Constants.VOCAB);
				WeeklyReports wrs = new WeeklyReports();
				WeeklyReport wr = new WeeklyReport();
				Vector reports = wrs.getListOfWeeklyReports(student_id, Constants.VOCAB, context_path);
				printLog(reports, context, "weekly reports");
				printLog(wrs.getLog(), context, "wrs.getListOfWeeklyReports log");
				try
				{
					int reports_size = reports.size();
					int last_week = reports_size;
					wr = wrs.getWeeklyReport(student_id, Constants.VOCAB, context_path, last_week+"");
				} catch (java.lang.NullPointerException npe)
				{
					wr = setupBlankReport();
				}
				student_records.put(student_id, wr);
			} catch (java.lang.NullPointerException npe)
			{
				context.log("UsersDetailsAction.perform:npe for "+student_id);
			}
		}
		session.setAttribute("student_records", student_records);
		return (mapping.findForward("class_details"));
	}
	
	private String createRecordString(Hashtable last_record, Momento m, SessionsReport sr,
			int grand_test_index, int daily_test_index)
	{
		DecimalFormat formatter = new DecimalFormat("###.##");
		String number_of_words = (String)last_record.get("number_of_words");
		String words_at_reading_level_0 = (String)last_record.get("words_at_reading_level_0");
		String words_at_writing_level_0 = (String)last_record.get("words_at_writing_level_0");
		String words_at_reading_level_1 = (String)last_record.get("words_at_writing_level_1");
		String words_at_writing_level_1 = (String)last_record.get("words_at_writing_level_1");
		String words_at_reading_level_2 = (String)last_record.get("words_at_reading_level_2");
		String words_at_writing_level_2 = (String)last_record.get("words_at_writing_level_2");
		String words_at_reading_level_3 = (String)last_record.get("words_at_reading_level_3");
		String words_at_writing_level_3 = (String)last_record.get("words_at_writing_level_3");
		String reading_average = 
			(String)formatter.format(Double.parseDouble((String)last_record.get("reading_average"))); 
		String writing_average = 
			(String)formatter.format(Double.parseDouble((String)last_record.get("writing_average")));  
		String date = (String)Transformer.simplifyDate((String)last_record.get("date"));
		String action_name = m.getActionName();
		String action_time = Transformer.simplifyDate(m.getActionTime());
		String sessions = sr.getNumberOfSessions()+"";
		String tests = sr.getNumberOfTestsInWeek()+"";
		String week = sr.getWeekOfYear()+"";
		String separator = " - ";
		String record_string = (date+separator+number_of_words+separator
			+words_at_reading_level_0+separator+words_at_writing_level_0+separator
			+words_at_reading_level_1+separator+words_at_writing_level_1+separator
			+words_at_reading_level_2+separator+words_at_writing_level_2+separator
			+words_at_reading_level_3+separator+words_at_writing_level_3+separator
			+reading_average+separator+writing_average
			+separator+action_name
			+separator+action_time
			+separator+sessions
			+separator+tests
			+separator+week
			+separator+daily_test_index
			+separator+grand_test_index);
		return record_string;
	}
	
	private WeeklyReport setupBlankReport()
	{
		WeeklyReport wr = new WeeklyReport();
		Vector levels = new Vector();
		levels.add("0");
		levels.add("0");
		levels.add("0");
		levels.add("0");
		wr.setAverageTimeSpentTesting(Long.parseLong("0"));
		wr.setNumberOfSessions(Integer.parseInt("0"));
		wr.setNumberOfTestsInWeek(Integer.parseInt("0"));
		wr.setNumberOfWords(Integer.parseInt("0"));
		wr.setReadingAverage(Float.parseFloat("0.0"));
		wr.setWeekOfYear("0");
		wr.setWritingAverage(Float.parseFloat("0.0"));
		wr.setWritingLevels(levels);
		wr.setReadingLevels(levels);
		return wr;
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
