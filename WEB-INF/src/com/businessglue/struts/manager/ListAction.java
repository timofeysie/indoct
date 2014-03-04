package com.businessglue.struts.manager;

import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.dto.WordStats;
import org.catechis.dto.AllWordStats;
import org.catechis.I18NWebUtility;
// From LoginAction  Some may not be required...
import java.io.File;
import java.util.Vector;
import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;
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
<p>This class retrieves a filename from the header, parses puts it in a FileBean
 and puts the bean in the session scope.
 <p>Workarounds include, path obtained by dubious methods (done in LoginAction),
 and file_name retrieved by parsing header, and not request.getHeader("filename")which returns null.
 <p>Took out the file_bean attribute words_defs because it caused problems later with
 a class cast exception.
 */
public class ListAction extends Action
{
	
	/**
	*<p>Take a word category or all words and put the list in the session context.
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		String category_name = Workaround.getHeader(request.getQueryString());
		ServletContext context = getServlet().getServletContext();
		//Storage store = (Storage)session.getAttribute("store");
		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		Vector all_words =  new Vector();
		if (category_name.equals("all"))
		{
			Vector all_word_categories = store.getWordCategories("exclusive", user_id);
			for (int i = 0; i < all_word_categories.size(); i++) 
			{
				String category = (String)all_word_categories.get(i);
				Vector category_words = store.getWordObjects(category, user_id);
				all_words.addAll(category_words);
			}
		}
		else
		{
			context.log("ListAction.perform: geting "+category_name);
		}
		session.setAttribute("all_words", all_words);
		session.setAttribute("category", category_name);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("list"));
	}

}
