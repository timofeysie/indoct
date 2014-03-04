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

import com.businessglue.indoct.UserBean;
import com.businessglue.util.GetUserFiles;
import org.catechis.I18NWebUtility;

// Added for this Action.
import java.util.Locale;
import java.util.Hashtable;
import com.businessglue.util.Domartin;
import com.businessglue.indoct.FileBean;
import com.businessglue.indoct.TestBean;
import com.businessglue.util.OpenTextFile;
import com.businessglue.util.CreateJDOMList;
import com.businessglue.util.ParseTextFile;
import com.businessglue.struts.FilterFileForm;

/**
<p>This class retrieves a filename from the header, parses puts it in a FileBean
 and puts the bean in the session scope.
 <p>Workarounds include, path obtained by dubious methods (done in LoginAction),
 and file_name retrieved by parsing header, and not request.getHeader("filename")which returns null.
 * @author Timothy Curchod 
 */
public class FilterFileAction extends Action
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
	private String test_type;

	/**
	*<p>This class is responsible for deciding what kind of file has been chosen,
	* and where to send the control to next.  The user_bean should be set with any
	* data that will be needed later.
	*<p>Currently there is a little confusion here which should be made more consistent.
	* Previously we send control only to the file.jsp, but with more actions required, we
	* started to go to different jsp's.  This has been remedied somewhat so far.  Check it out.
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		res = response;
		session = request.getSession();
		context = getServlet().getServletContext();
		FilterFileForm filter_file_form = (FilterFileForm)form;
		String min_max = getMinMax(filter_file_form);
		UserBean user_bean = (UserBean)session.getAttribute("USER_KEY");
		FileBean file_bean = (FileBean)session.getAttribute("FILE_BEAN");
		file_name = (String)session.getAttribute("file_name");
		user_name = (String)session.getAttribute("user_name");
		user_path = (String)session.getAttribute("user_path");
		file_chosen = Domartin.createFileFromUserNPath(file_name, user_path);
		Domartin.checkPathPermissions(file_chosen, context);
		String ext = Domartin.getFileExtension(file_name);
		elements = new Vector();
		words_defs = null;
		xmlFilterAction(min_max);
		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("filter_file"));
	}
	
	private String getMinMax(FilterFileForm form)
	{
		FilterFileForm filter_file_form = (FilterFileForm)form;
		String min = filter_file_form.getMin();
		String max = filter_file_form.getMax();
		test_type = filter_file_form.getType();
		String min_max = new String(min+"-"+max);
		return min_max;
	}
	
	private void xmlFilterAction(String _min_max)
	{
		CreateJDOMList jdom = new CreateJDOMList(file_chosen, context);
		context.log("FilterFileAction.xmlFilterAction ");
		String levels = _min_max;
		String index = new String("0");
		String length = new String("all");
		//String test_type = (String)session.getAttribute("test_type");
		Hashtable words_defs = jdom.getFilteredTextDefHash(length, index, levels, test_type);
		FileBean file_bean = new FileBean();
		file_bean.setFileName(file_name);
		file_bean.setUserName(user_name);
		file_bean.setFilePath(user_path);
		file_bean.putElementVector(elements);
		file_bean.putWordsDefs(words_defs);
		session.setAttribute("FILE_KEY", file_bean);
	}

}
