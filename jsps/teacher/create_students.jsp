<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<p><bean:message key="teacher.input_student_names"/>
<html:form action="create_students.do">
<p><bean:message key="import_list.input_text"/>
			<html:textarea property="text" rows="10" cols="30"/>
<p>

<p><bean:message key="import_list.input_date"/>
			<html:text property="date" />
</p>
    
<html:submit/>
</html:form>
<p>

<p>
<%
out.println("<a href=\"/indoct/welcome_teacher.jsp\">");
%><bean:message key="teacher.return_to_main"/><%
out.println("</a>");
%>
</body>
</html>
