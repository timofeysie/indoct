<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<font color="red"><bean:message key="app.under_construction"/></font>

<p><bean:message key="import_list.input_text_and_def"/>
<html:form action="user_import_words.do">
<p><bean:message key="import_list.input_text"/>
			<html:textarea property="text" rows="10" cols="30"/>
<p>

<p><bean:message key="import_list.input_date"/>
			<html:text property="date" />
</p>
    
<html:submit/>
</html:form>
<p>
<%
String name = (String)session.getAttribute("file_name");
out.println("<a href=\"/indoct/user_file.do?filename="+name+"\">");
%><bean:message key="add_word.return"/><%
out.println("</a>");
%>

<p>
<%
out.println("<a href=\"/indoct/user_welcome.jsp\">");
%><bean:message key="add_word.return_to_main"/><%
out.println("</a>");
%>
</body>
</html>
