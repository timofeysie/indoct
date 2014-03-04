<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.ArrayList" %>
<%@ page language="java" import="java.util.Enumeration" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<p>
<p><b><bean:message key="admin.logins_records_title"/></b>
<p>
<table>
<%
Hashtable logins = (Hashtable)session.getAttribute("logins");
ArrayList sorted_keys = (ArrayList)session.getAttribute("login_keys");
int size = sorted_keys.size();
int i = 0;
while (i<size)
{
	String key = (String)sorted_keys.get(i);
	String user = (String)logins.get(key);
	String date = Transformer.getLongDateFromMilliseconds(key);
	out.println("<tr>");
	out.println("<td>");
	out.println(date);
	out.println("</td>");
	out.println("<td>");
	out.println(user);
	out.println("</td>");
	out.println("</tr>");
	i++;
}
%>
</table>
</body>
</html>
