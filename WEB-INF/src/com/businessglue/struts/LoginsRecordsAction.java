package com.businessglue.struts;

import java.net.URL;
import java.io.File;
import java.util.Vector;
import java.util.Locale;
import java.util.Hashtable;
import java.util.Enumeration;
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

import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.dto.AllWordStats;
import org.catechis.dto.AllTestStats;
import org.catechis.file.FileCategories;
import org.catechis.file.FileTestRecords;
import org.catechis.admin.FileUserUtilities;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import org.apache.commons.beanutils.BeanComparator;

public class LoginsRecordsAction extends Action
{
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_name = (String)session.getAttribute("user_name");
		String context_path = context.getRealPath("/WEB-INF/");
		FileUserUtilities fut = new FileUserUtilities(context_path);
		Hashtable logins = fut.getLoginEntries(context_path);
		ArrayList login_keys = fut.getSortedLoginEntries();
		session.setAttribute("logins", logins);
		session.setAttribute("login_keys", login_keys);
		return (mapping.findForward("logins_records"));
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
}
