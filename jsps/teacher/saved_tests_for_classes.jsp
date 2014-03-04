<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="org.catechis.dto.SavedTest;" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<p><b><bean:message key="welcome_admin.saved_tests_for_classes"/></b>
<table>
<tr>
	<td><b><bean:message key="saved_tests_for_classes.test_name"/></td>
	<td><b><bean:message key="saved_tests_for_classes.date"/></td>
</tr>
<%
		Vector saved_class_tests = (Vector)session.getAttribute("saved_class_tests");
		int i = 0;
	    while (i<saved_class_tests.size())
	    {
		    SavedTest saved_test = (SavedTest)saved_class_tests.get(i);
		    out.println("<tr><td>");
		    out.println("<a href=\"/indoct/saved_class_tests.do?test_id="+saved_test.getTestId()+"\">"+saved_test.getTestName()+"</a>"); 
		    out.println("</td><td>");
			out.println(Transformer.getDateFromMilliseconds(saved_test.getCreationTime()));
		    out.println("</td></tr>");
		    i++;
	    }
%>
</tr>
</table>
<p>
<p><b><bean:message key="categories.options"/></b>
<p>
<p><a href="/indoct/users_options.do"><bean:message key="welcome_admin.users_options"/></a>
<p><a href="/indoct/jsps/teacher/add_class.jsp"><bean:message key="welcome_admin.add_class"/></a>
<p><a href="/indoct/saved_tests_for_classes.do"><bean:message key="welcome_admin.saved_tests_for_classes"/></a>
</body>
</html>
