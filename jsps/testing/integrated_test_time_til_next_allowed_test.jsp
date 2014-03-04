<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!--jsp:useBean id="file" class="com.businessglue.indoct.TestBean" scope="session"/-->
<%@ page language="java" import="com.businessglue.util.EncodeString" %>
<%@ page language="java" import="org.catechis.dto.AllWordsTest" %>
<!-- . -->
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<p><bean:message key="integrated_test_time_til_next_allowed_test.waiting_time"/>
<%
String integrated_test_time_til_next_allowed_test = (String)session.getAttribute("time_til_next_allowed_test");
out.println("<B><h2>"+integrated_test_time_til_next_allowed_test);
%>
<p>
<a href="/indoct/welcome.jsp"><bean:message key="add_word.return_to_main"/>
</body>
</html>
