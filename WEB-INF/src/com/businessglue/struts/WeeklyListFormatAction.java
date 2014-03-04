package com.businessglue.struts;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import java.util.Date;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import org.catechis.Storage;
import org.catechis.dto.Word;
import org.catechis.Domartin;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import org.catechis.filter.ImportListFilter;
import org.catechis.format.FormatPanel;
import org.catechis.admin.FileUserOptions;
import org.catechis.constants.Constants;

import com.businessglue.util.EncodeString;
//import com.businessglue.util.I18NWebUtility;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
public class WeeklyListFormatAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_name = (String)session.getAttribute("user_name");
		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		String type = request.getParameter("type"); // will be null if the user wants reading and writing lists
		FileStorage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		
		/*
		In a past life we put all the options in one file...
		String format_tr_width = (String)user_opts.get("format_tr_width");	// table row width
		String format_table_width = (String)user_opts.get("format_table_width"); // inner table widths
		String format_table_limit = (String)user_opts.get("format_table_limit"); // number of max rows per table
		String format_tr_pixels = (String)user_opts.get("format_tr_pixels"); // number of max pixels per table row before a wrap
		String format_print_reading = (String)user_opts.get("format_print_reading");		// These options turn on or off the lists that will be printed on the jsp page.
		String format_print_writing = (String)user_opts.get("format_print_writing");
		String format_print_missed_reading = (String)user_opts.get("format_print_missed_reading");
		String format_print_missed_writing = (String)user_opts.get("format_print_missed_writing");
		String daily_list_print_new_words_reading = (String)user_opts.get("daily_list_print_new_words_reading");
		String daily_list_print_new_words_writing = (String)user_opts.get("daily_list_print_new_words_writing");
		*/
		context.log("WeeklyListFormatAction.perfom "+user_id);
		
		Hashtable jsp_options = getJSPOptions(user_id, "weekly_list_format", "vocab", context_path, context); // user name, as of now, is an long id, where it used to be the persons actual name to a folder with their name.
		
		context.log("WeeklyListFormatAction 2");
		String format_tr_pixels = (String)jsp_options.get("format_tr_pixels");
		//context.log(format_print_reading+" "+format_print_writing);
		//context.log(format_print_missed_reading+" "+format_print_missed_writing);
		context.log("WeeklyListFormatAction format_tr_pixels "+format_tr_pixels);	// null !!!
		context.log("WeeklyListFormatAction 3");
		
		Vector r_all_words = (Vector)session.getAttribute("r_all_words");  // retrieve lists
		Vector w_all_words = (Vector)session.getAttribute("w_all_words");
		Hashtable r_words_defs = (Hashtable)session.getAttribute("r_words_defs");
		Hashtable w_words_defs = (Hashtable)session.getAttribute("w_words_defs");
		FormatPanel fp = new FormatPanel(Integer.parseInt(format_tr_pixels));
		
		context.log("r_all_words lengths "+r_all_words.size());
		context.log("w_all_words lengths "+w_all_words.size());
		context.log("r_words_defs lengths "+r_words_defs.size());
		context.log("w_words_defs lengths "+w_words_defs.size());
		fp.setupForAllWidths(r_all_words, w_all_words, r_words_defs, w_words_defs);
		fp.setVisible(true);
		Vector r_increments = fp.getReadingIncrements();
		fp.dispose();
		context.log("r_increments "+r_increments.size());
		printLog(r_increments, context);
		context.log("FormatPanel Log ---------- start");
		printLog(fp.getLog(), context);
		context.log("FormatPanel Log ---------- end");
		Vector w_increments = fp.getWritingIncrements();
		context.log("w_increments Log ---------- start");
		printLog(w_increments, context);
		context.log("w_increments Log ---------- end");
		session.setAttribute("r_increments", r_increments);
		session.setAttribute("w_increments", w_increments);
		
		session.setAttribute("jsp_options", jsp_options);
		//session.setAttribute("r_words_defs", r_words_defs);
		
		/*
		session.setAttribute("format_tr_width", format_tr_width);
		session.setAttribute("format_table_width", format_table_width);
		session.setAttribute("format_table_limit", format_table_limit);
		session.setAttribute("format_print_reading", format_print_reading);
		session.setAttribute("format_print_writing", format_print_writing);
		session.setAttribute("format_print_missed_reading", format_print_missed_reading);
		session.setAttribute("format_print_missed_writing", format_print_missed_writing);
		session.setAttribute("daily_list_print_new_words_reading", format_print_missed_reading);
		session.setAttribute("daily_list_print_new_words_writing", format_print_missed_writing);
		*/
		
		context.log("WeeklyListFormatAction.perform: user name "+user_id);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		if (type==null)
		{
			context.log("WeeklyListFormatAction.perform: type is null - findForward(weekly_list_format)");
			return (mapping.findForward("weekly_list_format"));
		} else if (type.equals(Constants.READING))
		{
			context.log("WeeklyListFormatAction.perform: type reading - findForward(weekly_list_reading)");
			return (mapping.findForward("weekly_list_reading"));
		}
		context.log("WeeklyListFormatAction.perform: - weekly_list_format");
		return (mapping.findForward("weekly_list_format")); 
	}
	
	private Hashtable getJSPOptions(String guest_id, String jsp_name, String subject, String current_dir, ServletContext context)
	{
		//String jsp_name = ("weekly_list_format");
		//String subejct  = ("vocab");
		Hashtable options = new Hashtable();
		FileUserOptions fuo = new FileUserOptions(current_dir);
		try
		{
			//context.log("WeeklyListFormatAction.getJSPOptions"+current_dir);
			options = fuo.getJSPOptions(guest_id, jsp_name, subject);
		} catch (java.lang.NullPointerException npe)
		{
			context.log("WeeklyListFormatAction.getJSPOptions npe: use defaults. fuo.log -----");
			printLog(fuo.getLog(), context);
			options = getDefaults();
		}
		return options;
	}
	
	private Hashtable getDefaults()
	{
		Hashtable defaults = new Hashtable();
		defaults.put("format_tr_width", "125");
		defaults.put("format_table_width", "334");
		defaults.put("format_table_limit", "18");
		defaults.put("format_tr_pixels", "63");
		defaults.put("format_print_reading", "true");
		defaults.put("format_print_writing", "true");
		defaults.put("format_print_missed_reading", "true");
		defaults.put("format_print_missed_writing", "true");
		defaults.put("daily_list_print_new_words_reading", "true");
		defaults.put("daily_list_print_new_words_writing", "true");
		return defaults;
	}
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log(i+" "+(String)log.get(i));
			i++;
		}
	}
	
	private void printLog(Hashtable log, ServletContext context)
	{
		for (Enumeration e = log.elements() ; e.hasMoreElements() ;) 
		{
			context.log((String)e.nextElement());
		}
	}	

}
