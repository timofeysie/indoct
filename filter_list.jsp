<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="com.businessglue.indoct.UserBean" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="org.catechis.dto.AllTestStats" %>
<%@ page language="java" import="org.catechis.dto.AllWordStats" %>
<%@ page language="java" import="org.catechis.dto.WordFilter" %>
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
Vector filtered_words = (Vector)session.getAttribute("filtered_words");
WordFilter word_filter = (WordFilter)session.getAttribute("word_filter");
String type = word_filter.getType();
for (int i = 0; i < filtered_words.size(); i++) 
{
	Word word = (Word)filtered_words.get(i);
	String name = word.getText();
	String def  = word.getDefinition();
	if (type.equals("reading"))
	{
		out.println("<tr><TD>"+name+"</TD>");
		out.println("<TD>"+def +"</TD></tr>");
	} else
	{
		out.println("<tr><TD>"+def +"</TD>");
		out.println("<TD>"+name+"</TD></tr>");
	}
}
%>
</TABLE>
<p>

</body>
</html>
