<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="org.catechis.dto.Word" %>
<%@ page language="java" import="org.catechis.dto.Test" %>
<%@ page language="java" import="org.catechis.constants.Constants" %>
<%@ page language="java" import="org.catechis.dto.SavedTest" %>
<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<html>
<head><title><%out.println((String)session.getAttribute("user_name"));%></title></head>
<body>
<p>
<p><bean:message key="teacher.choose_students_to_add_to_class"/>
<p>
<form action="add_students_to_class.do" method="post">
<%
out.println("<table>");
Hashtable students_available = (Hashtable)session.getAttribute("students_available"); 
for (Enumeration e = students_available.keys() ; e.hasMoreElements() ;) 
{
	String key = (String)e.nextElement();
	String val = (String)students_available.get(key);
	out.println("<tr><td>");
	out.println("<td>"+val+"</td>");
	out.println("<td>  </td>");
	out.println("<td>  </td>");
	out.println("<td> <input type=\"checkbox\" name=\""+key+"\" value=\""+val+"\"/></td>");
	out.println("</tr>");
}
out.println("</td>");
out.println("</table>");

%>
<input type="submit" value="Submit" />
</form>
</body>
</html>
