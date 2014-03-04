package com.businessglue.struts;

// From LoginAction  Some may not be required...
import java.io.File;
import java.util.Vector;
import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;
import org.catechis.FileStorage;
import org.catechis.Storage;
import org.catechis.I18NWebUtility;

import com.businessglue.indoct.UserBean;
import com.businessglue.util.GetUserFiles;

// Added for this Action.
import java.util.Locale;
import java.util.Hashtable;
import com.businessglue.util.Domartin;
import com.businessglue.indoct.FileBean;
import com.businessglue.indoct.TestBean;
import com.businessglue.util.Workaround;
import com.businessglue.util.OpenTextFile;
import com.businessglue.util.CreateJDOMList;
import com.businessglue.util.ParseTextFile;

/**
<p>We get the rest of the information 
 */
public class StatsAction extends Action
{
	private Vector elements;
	private String user_name;
	private String file_name;
	private String user_path;
	private File file_chosen;
	private ServletContext context;
	private String msg;
	private Hashtable words_defs;
	private HttpSession session;
	private HttpServletResponse res;
	
	private Hashtable reading_levels;
	private Hashtable writing_levels;
	
	/**
	*<p>Show statistics on a words-definitions xml file.
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		res = response;
		session = request.getSession();
		context = getServlet().getServletContext();
		getStats();

		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("stats"));
	}
	
	private void getStats()
	{
		msg = new String("StatsAction: getting file info");
		context.log(msg);
		file_name = (String)session.getAttribute("file_name");
		user_name = (String)session.getAttribute("user_name");
		user_path = (String)session.getAttribute("user_path");
		context.log("StatsAction.getStats: file_name "+file_name+" user_path "+user_path);
		file_chosen = Domartin.createFileFromUserNPath(file_name, user_path);
		CreateJDOMList jdom = new CreateJDOMList(file_chosen, context);
		elements = jdom.getTextDefVector();
		words_defs = jdom.getTextDefHash();
		reading_levels = jdom.getWhateverHash("word", "text", "reading-level");
		writing_levels = jdom.getWhateverHash("word", "text", "writing-level");
		
		session.setAttribute("reading_levels_hash", reading_levels);
		session.setAttribute("writing_levels_hash", writing_levels);
		
		// These should already be set in the session.
		session.setAttribute("file_name", file_name);
		// session.setAttribute("user_name", user_name);
		session.setAttribute("user_path", user_path);
		// session.setAttribute("words_defs", words_defs);
	}

}
