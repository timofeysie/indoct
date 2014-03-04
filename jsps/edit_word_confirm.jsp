<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="org.catechis.dto.Word" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<%
Word word = (Word)session.getAttribute("word");
//String encoding = (String)session.getAttribute("encoding");    
//String encoding = new String("UTF-8");
String text = word.getText();
String definition = word.getDefinition();
String name = (String)session.getAttribute("file_name");
out.println("<p>"+text+"</p>");
out.println("<p>"+definition+"</p>");
out.println("<a href=\"/indoct/file.do?filename="+name+"\">");
%>
<p><bean:message key="add_word.return"/><%
out.println("</a>");
%>
</body>
</html>
