<%@ page contentType="text/html; charSet=euc-kr" %>
<!--%@ page contentType="text/html; charSet=utf-8" %-->
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="file" class="com.businessglue.indoct.FileBean" scope="session"/>
<%@ page language="java" import="com.businessglue.indoct.FileBean" %>
<%@ page language="java" import="com.businessglue.util.EncodeString" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<html>
<head><title><%out.println(session.getAttribute("file_name"));%></title></head>
<body>
<%
FileBean file_bean = (FileBean)session.getAttribute("FILE_KEY");
Hashtable text_defs  = file_bean.getWordsDefs();
out.println("<table><FONT SIZE=\"+2\">");
	Enumeration keys = text_defs.keys(); 
	while (keys.hasMoreElements())
	{
		String key = (String)keys.nextElement();
		String value = (String)text_defs.get(key);
		out.println("<TR>");
			out.println("<TD><B>");
			out.println(value);
			out.println("</B></TD>");
			out.println("<TD>&nbsp;</TD>");
			out.println("<TD><B>");
			out.println(key);
			out.println("</TD></B>");
		out.println("</TR>");
	}
	out.println("</table></FONT> ");
%>

</body>
</html>
