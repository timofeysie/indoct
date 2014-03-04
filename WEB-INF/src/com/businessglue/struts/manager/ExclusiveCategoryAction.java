package com.businessglue.struts.manager;

import java.io.File;
import java.util.Vector;
import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;
import java.util.Hashtable;
import org.catechis.Domartin;
import com.businessglue.util.Workaround;
import com.businessglue.util.CreateJDOMList;
import org.catechis.FileStorage;
import org.catechis.Storage;
import org.catechis.I18NWebUtility;
import org.catechis.dto.WordCategory;
import org.catechis.file.FileWordCategoriesManager;

/**
<p>This class retrieves a filename from the header loads the file and set it in the session.
 */
public class ExclusiveCategoryAction extends Action
{
	
	private static String DEBUG_TAG = "ExclusiveCategoryAction";
	
	/**
	*<p>Load an xml file and set it as a hashtable and a vector in the session.
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		String method = "perform";
		String cat_id = Workaround.getHeader(request.getQueryString());
		ServletContext context = getServlet().getServletContext();
		context.log(DEBUG_TAG+"."+method+": recieved category_id "+cat_id);
		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable<String, String> user_opts = store.getUserOptions(user_id, context_path); 
		String subject = user_opts.get("subject");
		FileWordCategoriesManager fwcm = new FileWordCategoriesManager();
		WordCategory word_cat = fwcm.getWordCategory(Long.parseLong(cat_id), user_id, subject, context_path);
		printLog(fwcm.getLog(), context);
		session.setAttribute("category", word_cat);
		session.setAttribute("category_id", cat_id);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("exclusive_category"));
	}


	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log(DEBUG_TAG+".log "+(String)log.get(i));
			i++;
		}
	}

}
