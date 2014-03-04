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
<html:form action="create_integrated_test.do">
<table>
	<tr>
		<td>
			<p><B><bean:message key="create_integrated_test.create_name"/>:</B>
		</td>
		<td>
			<html:text property="testName" />
		</td>
	<tr>
		<td>
			<p><B><bean:message key="create_integrated_test.number_of_words"/>:</B>
		</td>
		<td>
			<html:text property="numberOfWords" />
		</td>
		<td>
			<p><B><bean:message key="create_integrated_test.number_of_words_explanation"/>:</B>
		</td>
	</tr>
	
<%
	out.println("<TR>");
		out.println("<TD><B>");
		%><bean:message key="create_integrated_test.choose_type"/><%
		out.println("<TD><B>");
		out.println("<SELECT NAME=\"testType\" SIZE=\"1\" MULTIPLE>");
		out.println("<OPTION>reading</OPTION>");
		out.println("<OPTION>writing</OPTION>");
		out.println("<OPTION>both</OPTION>");
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
