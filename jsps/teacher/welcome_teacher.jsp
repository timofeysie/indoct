<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="org.catechis.dto.Word" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<p><b><bean:message key="teacher.all_classes"/></b>
<%
Hashtable classes = (Hashtable)session.getAttribute("classes");
for (Enumeration keys = classes.keys(); keys.hasMoreElements() ;) 
{
	String key = (String)keys.nextElement();
	String value = (String)classes.get(key);
        out.println("<p><a href=\"/indoct/class_home.do?class_id="+key+"\">"+value);      
}
%>
<p>
<p><b><bean:message key="categories.options"/></b>
<p>
<p><a href="/indoct/users_options.do"><bean:message key="welcome_admin.users_options"/></a>
<p><a href="/indoct/jsps/teacher/add_class.jsp"><bean:message key="welcome_admin.add_class"/></a>
<p><a href="/indoct/saved_tests_for_classes.do"><bean:message key="welcome_admin.saved_tests_for_classes"/></a>
</body>
</html>
