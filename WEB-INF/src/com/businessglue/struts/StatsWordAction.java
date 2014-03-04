package com.businessglue.struts;

import java.util.Vector;
import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;
import com.businessglue.indoct.UserBean;
import com.businessglue.util.GetUserFiles;

// Added for File Action.
import java.util.Locale;
import java.util.Hashtable;
import org.catechis.Domartin;
import com.businessglue.indoct.FileBean;
import com.businessglue.indoct.TestBean;
import com.businessglue.util.Workaround;
import com.businessglue.util.OpenTextFile;
import com.businessglue.util.CreateJDOMList;
import com.businessglue.util.ParseTextFile;

// Added for this action
import java.util.List;
import java.util.Date;
import java.util.Enumeration;
import java.util.Collections;

// refactored imports
import org.catechis.constants.Constants;
import org.catechis.dto.Test;
import org.catechis.dto.Word;
import org.catechis.JDOMSolution;
import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.Transformer;
import org.catechis.I18NWebUtility;

/**
<p>We get the rest of the information 
 */
public class StatsWordAction extends Action
{
	/*
	private String user_name;
	private String file_name;
	private String user_path;
	private File file_chosen;
	//private ServletContext context;
	private String msg;
	private Hashtable words_defs;
	//private HttpSession session;
	//private HttpServletResponse res;
	
	private String word_chosen;
	private String word_level;
	
	private Hashtable word_w_tests;
	private Hashtable word_r_tests;
	*/
	
	/**
	*<p>Show statistics on a words-definitions xml file.
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		//word_chosen = Workaround.getHeader(request.getQueryString());
		String definition = (String)request.getParameter("word");
		String text = (String)request.getParameter("text");
		String text_bytes = Transformer.getByteString(text);
		String file_name = (String)session.getAttribute("file_name");
		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		String search_property = "definition";
		String search_value = definition;
		String category = file_name;
		Word word = store.getWordObject(search_property, search_value, category, user_id);
		session.setAttribute("word", word);
		//getWordStats(session, file_name, context_path, word);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("stats_word"));
	}
	
	private void getWordStats(HttpSession session, String file_name, String context_path, Word word)
	{
		ServletContext context = getServlet().getServletContext();
		context.log("StatsWordAction: getting word info");
		Vector word_w_tests = new Vector();
		Vector word_r_tests = new Vector();
		Test [] tests = word.getTests();
		int i = 0;
		int l = tests.length;
		while (i<l)
		{
			Test test = tests[i];
			String full_date = test.getDate();
			long long_date = Domartin.getMilliseconds(full_date);
			String short_date = Transformer.getDateFromMilliseconds(long_date+"");
			String grade = test.getGrade();
			String type = Domartin.getTestType(test.getName());
			String level = Domartin.getTestLevel(test.getName());
			String result = short_date+"\t"+grade+"\t"+level;
			if (type.equals(Constants.READING))
			{
				word_r_tests.add(result);
			} else if (type.equals(Constants.WRITING))
			{
				word_w_tests.add(result);
			}
			i++;
		}
		//Vector sorted_tests = new Vector(sortTests(context)); 
		//session.setAttribute("word_w_tests", word_w_tests);
		//session.setAttribute("word_r_tests", word_r_tests);
		//session.setAttribute("sorted_dates", sorted_tests);
		// session.setAttribute("file_name", file_name); // already there
		// session.setAttribute("user_name", user_name);
		// session.setAttribute("user_path", user_path);
		// session.setAttribute("words_defs", words_defs);
	}
	
	/**
	* HEY- This method happens in the CreateJDOMList Object.
	* Talk about foresight when namming objects!  Did I hear "refactor"?
	* In this method we need to get a Hashtable from a word element like this:
	* We get a List of tests, and return a Hastable with the date as the key,
	* and the file and grade elements glued together with a @@@ separator.
	<word>
		<text>??</text>
		<definition>To put in</definition>
		<writing-level>0</writing-level>
		<reading-level>1</reading-level>

		<test>
			<date>Thu Mar 03 11:21:44 PST 2005</date>
			<file>level 0 writing.test</file>
			<grade>fail</grade>
		</test>
	</word>
	private Hashtable getDatedTestHash(Vector tests)
	{
	}
	*/
	
	/**
	* We have to turn the test hashtable keys into date objects, put them all
	*in a list and then sort them.
	*
	private Vector sortTests(ServletContext context)
	{
		Vector dates = new Vector();
		// Cast strings to dates
		Enumeration w_keys = word_w_tests.keys();
		while (w_keys.hasMoreElements())
		{
			String s_date = (String)w_keys.nextElement();
			try
			{
				Date date = new Date(s_date);
				dates.add(date);
				context.log("StatsWordAction.sortDates: added old "+s_date);
				context.log("StatsWordAction.sortDates: added new "+date.toString());
				context.log("StatsWordAction.sortDates:");
			} catch (java.lang.IllegalArgumentException iae)
			{
				context.log("StatsWordAction.sortDates: could not parse "+s_date);
			}
		}
		Enumeration r_keys = word_r_tests.keys();
		while (r_keys.hasMoreElements())
		{
			String s_date = (String)r_keys.nextElement();
			try
			{
				Date date = new Date(s_date);
				dates.add(date);
				context.log("StatsWordAction.sortDates: added old "+s_date);
				context.log("StatsWordAction.sortDates: added new "+date.toString());
				context.log("StatsWordAction.sortDates:");
			} catch (java.lang.IllegalArgumentException iae)
			{
				context.log("StatsWordAction.sortDates: could not parse "+s_date);
			}
		}
		Collections.sort(dates);
		return dates;
	}
	*/

}
