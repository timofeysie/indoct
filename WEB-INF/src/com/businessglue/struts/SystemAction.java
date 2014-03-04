package com.businessglue.struts;

import java.net.URL;
import java.io.File;
import java.util.Vector;
import java.util.Locale;
import java.util.Properties;
import java.util.Enumeration;
import java.text.DateFormat;
import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;
import com.businessglue.indoct.UserBean;
import com.businessglue.util.GetUserFiles;
import com.businessglue.util.EncodeString;

public class SystemAction extends Action
{
	
	private Vector props;
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		props = new Vector();
		String user_path = getUserPath(request);
		String user_locale = getUserLocale(request);
		String user_locales = getUserLocales(request);
		props.add("User Path - "+user_path);
		props.add("Preferred Locale - "+user_locale);
		props.add("Preferred Locales - "+user_locales);
		getAvailableLocales();
		getSysProps();
		HttpSession session = request.getSession();
		session.setAttribute("SYSTEM", props);
		return (mapping.findForward("system"));
	}
	
	private String getUserLocale(HttpServletRequest req)
	{
		Locale user_preferred_locale = req.getLocale();
		String pref_locale = user_preferred_locale.toString();
		return pref_locale;
	}
	
	private String getUserLocales(HttpServletRequest req)
	{
		Enumeration user_preferred_locales = req.getLocales();
		StringBuffer temp_locales = new StringBuffer();
		while(user_preferred_locales.hasMoreElements())
		{
			Locale user_locale = (Locale)user_preferred_locales.nextElement();
			temp_locales.append(user_locale.toString()+", ");
		}
		String locales_string = new String(temp_locales);
		return locales_string;
	}
	
	private void getAvailableLocales()
	{
		Locale list[] = DateFormat.getAvailableLocales();
		for (int i = 0; i < list.length; i++) 
		{
		    props.add("Locale - "+list[i].toString() + " " + list[i].getDisplayName());
		}	
	}

	private String getUserPath(HttpServletRequest req)
	{
		String serv_path = req.getServletPath();
		ServletContext context = getServlet().getServletContext(); 
		String temp_path = context.getRealPath(".");
		return temp_path;
	}
	
	/**
	<p>Currently this method is depedant on the props vector created elswhere.
	<p>To make it generally useful, we need to create a new vector here, and return it.
	*/
	public void getSysProps()
	{
		//Vector sys_props = new Vector();
		Properties properties = System.getProperties();
		Enumeration e = properties.propertyNames();
		while (e.hasMoreElements())
		{
			String key = new String((String)e.nextElement());
			String val = new String(properties.getProperty(key));
			String line = new String(key+" - "+val);
			props.add(line);
		}
		//return sys_props;
		// return props;
	}
}
