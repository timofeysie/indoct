<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<p><b><bean:message key="users_options_edit.instructions"/></b>
<p>
<%
String property = (String)session.getAttribute("property");
String value = (String)session.getAttribute("value");
%>
<bean:message key="users_options_edit_confirm.property"/>
<p><%out.println(property);%> &nbsp <bean:message key="users_options_edit_confirm.value"/>
<p><%out.println(value);%> &nbs  <bean:message key="users_options_edit.instructions"/>
<p><a href="/indoct/users_options_edit_confirm.do"><bean:message key="users_options_edit_confirm.confirmation"/></a>
<p>
<p><a href="/indoct/jsps/admin/admin_welcome.jsp"><bean:message key="users_options_edit.return_to_admin_welcome"/></a>
<p>
</body>
</html>
