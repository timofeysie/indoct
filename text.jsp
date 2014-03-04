<%@ page contentType="text/html; charSet=euc-kr" %>
<!--%@ page contentType="text/html; charSet=utf-8" %-->
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="file" class="com.businessglue.indoct.FileBean" scope="session"/>
<%@ page language="java" import="com.businessglue.indoct.FileBean" %>
<%@ page language="java" import="com.businessglue.util.EncodeString" %>
<%@ page language="java" import="java.util.Vector" %>

<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<p><b><bean:message key="file.title"/></b></p>
<%
FileBean file_bean = (FileBean)session.getAttribute("FILE_KEY");

Vector word_categories = (Vector)session.getAttribute("word_categories");

String user_name = (String)file_bean.getUserName();
String file_name = (String)file_bean.getFileName();

Vector elements  = file_bean.getElementVector();

Vector elements  = word_categories;

int num_of_elements = elements.size();
out.println("<p><b>"+user_name+"</b>");
out.println("<p><b>"+file_name+"</b>");
%>
<table>
<%
	for(int i = 0; i<num_of_elements; i++) 
	{
		out.println("<TR><TD>");
		String name = (String)elements.get(i);
		out.println(name);
		out.println("</TD></TR>");
	}
%>
</table>
</body>
</html>
