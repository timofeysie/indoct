<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<p>
<bean:message key="delete_word.delete"/>
<p>
<%
String word = (String)session.getAttribute("word_chosen");
String name = (String)session.getAttribute("file_name");
out.println(word);
out.println("&nbsp");
%>
<bean:message key="delete_word.delete"/>
<%
out.println("&nbsp");
out.println(name);
out.println("\n");
out.println("<a href=\"/indoct/file.do?filename="+name+"\">");
%><bean:message key="add_word.return"/><%
out.println("</a>");

	out.println("<p><a href=\"/indoct/run_test.do?filename=");
	String name = (String)session.getAttribute("test_name");
	byte[] bytes = name.getBytes();
	String encoded_name = new String(bytes, "ms949");
	out.println(encoded_name+"\">Re-run test</a>");

%>
</body>
</html>
