<%@ page contentType="text/html; charSet=euc-kr" %>
<!--%@ page contentType="text/html; charSet=utf-8" %-->
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="file" class="com.businessglue.indoct.FileBean" scope="session"/>
<%@ page language="java" import="com.businessglue.indoct.FileBean" %>
<%@ page language="java" import="com.businessglue.util.EncodeString" %>
<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<html>
<head><title><%out.println(session.getAttribute("file_name"));%></title></head>
<body>
<%
// FileBean file_bean = (FileBean)session.getAttribute("FILE_KEY");
// Hashtable text_defs  = file_bean.getWordsDefs();
Hashtable text_defs = (Hashtable)session.getAttribute("words_defs");
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
out.println("<form METHOD=\"post\" ACTION=\"filter_file.do\">");
out.println("<table>");
	out.println("<TR>");
		out.println("<TD><B>");
		out.println("");%><bean:message key="file.form.level"/><%
		out.println("</B></TD>");
		out.println("<TD><B>");
		out.println("<SELECT NAME=\"min\" SIZE=\"1\" MULTIPLE>");
		int i=0;
		while (i<=10)
		{
			out.println("<OPTION>"+i+"</OPTION>");
			i++;
		}
		out.println("</TD>");
		out.println("<TD><B>");
		out.println("<SELECT NAME=\"max\" SIZE=\"1\" MULTIPLE>");
		i=0;
		while (i<=10)
		{
			out.println("<OPTION>"+i+"</OPTION>");
			i++;
		}
		out.println("</TD>");
	out.println("</TR>");
	out.println("<TR>");
		out.println("<TD><B>");
		out.println("");%><bean:message key="file.form.type"/><%
		out.println("<TD><B>");
		out.println("<SELECT NAME=\"type\" SIZE=\"1\" MULTIPLE>");
		out.println("<OPTION>reading</OPTION>");
		out.println("<OPTION>writing</OPTION>");
		out.println("<TD><B>");
	out.println("</TR>");
out.println("</table>");
%>
<html:submit/>
</form>
<p><a href="/indoct/stats.do"><bean:message key="file.stats_link"/></a>
<p><a href="/indoct/add_word.do"><bean:message key="file.add_word"/></a>
<p><a href="/indoct/jsps/categories/import_list.jsp"><bean:message key="file.import_list"/></a>
<p><a href="/indoct/jsps/categories/categories_simple.jsp"><bean:message key="welcome.categories"/></a>
<p><a href="/indoct/welcome.jsp"><bean:message key="add_word.return_to_main"/></a>
</body>
</html>
