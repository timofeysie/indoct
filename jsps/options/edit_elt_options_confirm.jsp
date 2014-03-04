<%@ page contentType="text/html;charset=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="java.util.Vector" %>

<html:html>
<head>
<title><bean:message key="registrationForm.title"/></title>
<html:base/>
</head>
<body bgcolor="white">
<p><bean:message key="options.edit_exclude_level_confirm"/>

<%
Vector elt_vector =  (Vector)session.getAttribute("elt_vector");
int i = 0;
int size = elt_vector.size();
while (i<size)
{
	%>
	<p><bean:message key="options.exclude_level"/>&nbsp;
	<% 
	out.println(i); 
	%>&nbsp;
	<% 
	out.println(elt_vector.get(i)); 
	i++;
}
%>
<p>
<a href="/indoct/edit_elt_options.jsp"><bean:message key="options.re-edit"/>
<p>
<a href="/indoct/update_options_confirm.do"><bean:message key="options.edit_exclude_level_confirm_and_change"/>

</body>
</html:html>
