<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="org.catechis.dto.Word" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<p><b><bean:message key="welcome_admin.all_users"/></b>
<%
Vector users = (Vector)session.getAttribute("users");
Hashtable user_logins = (Hashtable)session.getAttribute("user_logins");

int size = users.size();
int count = 0;
while (count<size)
{
	String user = (String)users.get(count);
	out.println("<p><a href=\"/indoct/users_details.do?user="+user+"\">"+user+"</a>&nbsp;");
	try
	{
		String date = (String)user_logins.get(user);
		String last_login = Transformer.getLongDateFromMilliseconds(date);
		out.println(last_login);
	} catch (java.lang.NumberFormatException nfe)
	{
		out.println(" --- ");
	}
	count++;
}
%>
<p>
<p><b><bean:message key="categories.options"/></b>
<p>
<p><a href="/indoct/users_options.do"><bean:message key="welcome_admin.users_options"/></a>
<p>
<p><a href="/indoct/jsps/admin/users_options_edit.jsp"><bean:message key="welcome_admin.users_options_edit"/></a>
<p>
<p><a href="/indoct/logins_records.do"><bean:message key="admin.logins_records_title"/></a>
<p>
<p><a href="/indoct/logins_refresh.do"><bean:message key="admin.logins_refresh_link"/></a>
<p>
<p><a href="/indoct/reset_login_entries.do"><bean:message key="admin.reset_login_entries_link"/></a>
</body>
</html>
