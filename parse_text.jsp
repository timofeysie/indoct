<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="file" class="com.businessglue.indoct.FileBean" scope="session"/>
<%@ page language="java" import="com.businessglue.indoct.FileBean" %>
<%@ page language="java" import="com.businessglue.util.EncodeString" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>

<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<%
FileBean file_bean = (FileBean)session.getAttribute("FILE_KEY");
String user_name = (String)file_bean.getUserName();
String file_name = (String)file_bean.getFileName();
Hashtable words_defs  = file_bean.getWordsDefs();
out.println("<p><b>"+user_name+"</b>");
out.println("<p><b>"+file_name+"</b>");
%>
<table>
<%
	Enumeration keys = words_defs.keys();
	while (keys.hasMoreElements())
	{
		out.println("<TR><TD>");
		String key = (String)keys.nextElement();
		String val = (String)words_defs.get(key);
		byte key_bytes[] = key.getBytes("euc-kr");
		String enc_key = new String(key_bytes, "euc-kr");
%>
	<p><%=enc_key+" - "+val%>
<%
		out.println("</TD></TR>");
	}
%>
</table>
<p>Octal&nbsp<%="Suggest - \uad8c\ud558\ub2e4"%>
</body>
</html>
