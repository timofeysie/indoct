<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="com.businessglue.indoct.UserBean" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="org.catechis.dto.AllTestStats" %>
<%@ page language="java" import="org.catechis.dto.AllWordStats" %>
<%@ page language="java" import="org.catechis.dto.TestStats" %>
<%@ page language="java" import="org.catechis.dto.WordStats" %>
<%@ page language="java" import="org.catechis.dto.Word" %>
<%@ page language="java" import="java.util.Date" %>
<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="java.text.DecimalFormat" %>
<%@ page language="java" import="java.text.ParsePosition" %>
<%@ page language="java" import="java.text.SimpleDateFormat" %>
<%@ page language="java" import="java.util.List" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>

<TABLE>
<%
String category = (String)session.getAttribute("category");
Vector all_words = (Vector)session.getAttribute("all_words");
for (int i = 0; i < all_words.size(); i++) 
{
	Word word = (Word)all_words.get(i);
	String name = word.getText();
	String def  = word.getDefinition();
	out.println("<tr><TD>"+name+"</TD>");
	out.println("<TD>"+def +"</TD></tr>");
}
%>
</TABLE>
<p>
<%
out.println("<form METHOD=\"post\" ACTION=\"filter_list.do\">");
out.println("<input type=\"hidden\" name=\"category\" value=\""+category+"\">");
out.println("<table>");
	out.println("<TR>");
		out.println("<TD><B>");
		%><bean:message key="file.form.level"/><%
		out.println("</B></TD>");
		out.println("<TD><B>");
		out.println("<SELECT NAME=\"min\" SIZE=\"1\" MULTIPLE>");
		int i=0;
		while (i<=3)
		{
			out.println("<OPTION>"+i+"</OPTION>");
			i++;
		}
		out.println("</TD>");
		out.println("<TD><B>");
		out.println("<SELECT NAME=\"max\" SIZE=\"1\" MULTIPLE>");
		i=0;
		while (i<=3)
		{
			out.println("<OPTION>"+i+"</OPTION>");
			i++;
		}
		out.println("</TD>");
	out.println("</TR>");
	out.println("<TR>");
		out.println("<TD><B>");
		%><bean:message key="file.form.type"/><%
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
</body>
</html>
