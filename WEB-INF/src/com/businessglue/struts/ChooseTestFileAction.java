package com.businessglue.struts;

import java.net.URL;
import java.io.File;
import java.util.Vector;
import java.util.Locale;
import java.util.Hashtable;
import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;
import com.businessglue.indoct.UserBean;
import com.businessglue.util.Domartin;
import com.businessglue.util.GetUserFiles;
import com.businessglue.util.EncodeString;
import com.businessglue.util.CreateJDOMList;

/**
 * 
 *@author Timothy Curchod
 */
public class ChooseTestFileAction extends Action
{
	
	private URL url;
	private HttpSession session;
	//private UserBean user;
	//private HttpServletResponse res;
	
	/**
	*<p>This class creates a list of acceptable text files that can be used with a test.
	*<P>The files vector in the user_bean is re-used and must be re-made later so that it
	* can re-scan the users directory for the newly created test results file.
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		context.log("@@@ ChooseTestFileAction<init>");
		UserBean user_bean = (UserBean)session.getAttribute("USER_KEY");
		Vector all_files = user_bean.getFileList();
		Vector test_files = new Vector();
		int size = all_files.size();
		int i = 0;
		while (i<size)
		{
			String file = (String)all_files.get(i);
			String ext = Domartin.getFileExtension(file);
			if (ext.equals(".xml"))
			{
				test_files.add(file);
			}
			i++;
		}
		user_bean.putFileList(test_files);
		setContentTypeAndLocale(request, response);
		return (mapping.findForward("choose_test_file"));
	}
	
	private void setContentTypeAndLocale(HttpServletRequest hs_request, HttpServletResponse hs_response)
	{
		HttpSession h_session = hs_request.getSession();
		String content_type = (String)h_session.getAttribute("content_type");
		Locale locale = (Locale)h_session.getAttribute("locale");
		hs_response.setLocale(locale);
		hs_response.setContentType(content_type);
	}	
}
