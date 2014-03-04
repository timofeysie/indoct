<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="com.businessglue.indoct.UserBean" %>
<%@ page language="java" import="com.businessglue.util.EncodeString" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>

<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>

<%
Hashtable options  = (Hashtable)session.getAttribute("user_opts");

// Exclue Level Test Options -----------------------------------------------------------------------------------------
%>
<p><b><bean:message key="options.exclude_level_test_options_title"/></b></p>
<p>


<p><bean:message key="options.exclude_level"/>&nbsp; 0 <bean:message key="options.exclude_level_time"/>&nbsp; = &nbsp;
<%
out.println(options.get("exclude_level0_test"));
%>

<p><bean:message key="options.exclude_level"/>&nbsp; 1 <bean:message key="options.exclude_level_time"/>&nbsp; = &nbsp;
<%
out.println(options.get("exclude_level1_test"));
%>

<p><bean:message key="options.exclude_level"/>&nbsp; 2 <bean:message key="options.exclude_level_time"/>&nbsp; = &nbsp;
<%
out.println(options.get("exclude_level2_test"));
%>

<p><bean:message key="options.exclude_level"/>&nbsp; 3 <bean:message key="options.exclude_level_time"/>&nbsp; = &nbsp;
<%
out.println(options.get("exclude_level3_test"));
%>

<table>
	<tr>
		<td><p><bean:message key="options.waiting_reading_tests"/></td>
		<td><% out.println((String)options.get("waiting_reading_tests")); %></td>
		<td><p><bean:message key="options.waiting_writing_tests"/></td>
		<td><% out.println((String)options.get("waiting_writing_tests")); %></td>
	</tr>
</table>

<p><a href="/indoct/jsps/options/edit_elt_options.jsp"><bean:message key="options.edit_elt_options_link"/></a></p>
<%
// end exclude level test options ------------------------------------------------------------------------------------

%>
<p><b><bean:message key="options.utilities"/></b></p>
<p><a href="/indoct/clear_missed_words.do?type=reading"><bean:message key="options.clear_reading_missed_words"/></a></p>
<p><a href="/indoct/clear_missed_words.do?type=writing"><bean:message key="options.clear_writing_missed_words"/></a></p>
<p><a href="/indoct/welcome.jsp"><bean:message key="add_word.return_to_main"/></a></p>

<table>
<%
	Enumeration keys = options.keys();
	while (keys.hasMoreElements())
	{
		String key = (String)keys.nextElement();
		String value = (String)options.get(key);
		out.println("<TR>");
				out.println("<TD>");
				out.println(key);
				out.println("</TD>");
				out.println("<TD>");
				out.println(value);
				out.println("</TD>");
				out.println("<TD>");
				out.println("<a href=\"/indoct/edit_option_setup.do?option_name="+key+"\">");
				%>
				<bean:message key="options.edit_option_link"/></a>
				<%
				out.println("</TD>");
		out.println("</TR>");
	}
%>
</table>
</body>
</html>
