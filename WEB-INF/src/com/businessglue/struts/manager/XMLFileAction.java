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

/**
<p>This class retrieves a filename from the header loads the file and set it in the session.
 */
public class XMLFileAction extends Action
{
	
	/**
	*<p>Load an xml file and set it as a hashtable and a vector in the session.
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		String file_name = Workaround.getHeader(request.getQueryString());
		ServletContext context = getServlet().getServletContext();
		context.log("XMLFileAction.perform: recieved file "+file_name);
		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable<String, String> user_opts = store.getUserOptions(user_id, context_path); 
		String path_to_files = Domartin.createStringFromUserNPath("files", context_path);
		String path_to_user = Domartin.createStringFromUserNPath(user_id, path_to_files);
		File file_chosen = Domartin.createFileFromUserNPath(file_name, path_to_user);
		context.log("XMLFileAction.perform: created "+file_chosen.getAbsolutePath());
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		xmlAction(file_chosen, file_name, session);
		return (mapping.findForward("open"));
	}

	private void xmlAction(File file_chosen, String file_name, HttpSession session)
	{
		String msg = new String("opening xml file");
		ServletContext context = getServlet().getServletContext();
		context = getServlet().getServletContext();
		context.log(msg);
		CreateJDOMList jdom = new CreateJDOMList(file_chosen, context);
		Hashtable words_defs = jdom.getTextDefHash();
		session.setAttribute("file_name", file_name);
		session.setAttribute("words_defs", words_defs);
	}

	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("AddCategoryAction.log "+(String)log.get(i));
			i++;
		}
	}

}
