<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="org.catechis.dto.Word" %>
<%@ page language="java" import="org.catechis.EncodeString" %>

<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<html:form action="create_daily_test.do">
<table>
	<tr>
		<td>
			<B><bean:message key="create_daily_test.create_name"/>:</B>
		</td>
		<td>
			<html:text property="testName" />
		</td>
	</tr>
	
<%
	out.println("<TR>");
		out.println("<TD><B>");
		%><bean:message key="file.form.type"/><%
		out.println("<TD><B>");
		out.println("<SELECT NAME=\"testType\" SIZE=\"1\" MULTIPLE>");
		out.println("<OPTION>reading</OPTION>");
		out.println("<OPTION>writing</OPTION>");
		out.println("<TD></B>");
	out.println("</TR>");
out.println("</table>");
%>
<html:submit/>
</html:form>
</table>
<p>
<p>
<p>
</body>
</html>
