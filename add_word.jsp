<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<html:form action="add_word.do">
<table>
	<tr>
		<td>
			<bean:message key="add_word.text"/>:
		</td>
		<td>
			<html:text property="text" />
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="add_word.definition"/>: 
		</td>
		<td>
			<html:text property="definition" />
		</td>
	</tr>
</table>
<p>
<html:submit/>
</html:form>
<p>
<p>
<%
String name = (String)session.getAttribute("file_name");
out.println("<a href=\"/indoct/file.do?filename="+name+"\">");
%><bean:message key="add_word.return"/><%
out.println("</a>");
%>

<p>
<%
out.println("<a href=\"/indoct/welcome.jsp\">");
%><bean:message key="add_word.return_to_main"/><%
out.println("</a>");
%>
</body>
</html>
