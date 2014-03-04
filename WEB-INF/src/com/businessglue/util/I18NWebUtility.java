package com.businessglue.util;

import java.util.Locale;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
*<p>This Internationalization utility sets the content type and locale.
*<p>The values must already exist in the session.
*<p>Additions to it would use the constructor to pass in the request and response
* so that it could be overridden for other applications use.
*/
public class I18NWebUtility
{

	public I18NWebUtility()
	{}
	
	public static void setContentTypeAndLocale(HttpServletRequest hs_request, HttpServletResponse hs_response)
	{
		HttpSession h_session = hs_request.getSession();
		String content_type = (String)h_session.getAttribute("content_type");
		Locale locale = (Locale)h_session.getAttribute("locale");
		hs_response.setLocale(locale);
		hs_response.setContentType(content_type);
	}

}
