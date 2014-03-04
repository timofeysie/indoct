<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="java.util.Vector" %>

<html>
<head><title><bean:message key="welcome.system"/></title></head>
<body>
<p><b><bean:message key="welcome.system"/></b></p>
<%
Vector props = (Vector)session.getAttribute("SYSTEM");
int num_of_props = props.size();
String elem = new String();
for(int i = 0; i<num_of_props; i++)
{
	elem = ((String)props.elementAt(i));
	out.println("<p>"+elem+"</p>");
}
%>
</body>
</html>
