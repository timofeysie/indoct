<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<%@ page language="java" import="org.catechis.dto.Word" %>
<html>
<head><title><%out.println((String)session.getAttribute("user_name"));%></title></head>
<body>
<table>
<%
out.println("<table>");
// check for new words
Vector r_new_words = (Vector)session.getAttribute("r_new_words");
Vector w_new_words = (Vector)session.getAttribute("w_new_words");
if (r_new_words.size()>0||w_new_words.size()>0)
{
	// new reading missed words
	out.println("<tr><td>");
	%>
	<b><bean:message key="weekly_list.new"/>&nbsp;
	<bean:message key="app.small_reading"/></b>
	<%
	out.println("</td><td>");
	%>
	<b><bean:message key="weekly_list.list"/></b>
	<%
	out.println("</td></tr>");
	int new_words_r_size = r_new_words.size();
	int n_r_i = 0;
	while (n_r_i < new_words_r_size)
	{
		Word n_r_word = (Word)r_new_words.get(n_r_i);
		out.println("<tr><td>");
		out.println("<p>"+n_r_word.getText());
		out.println("</td>");
		out.println("<td>");
		out.println("<p>"+n_r_word.getDefinition()+"</p>");
		out.println("</td></tr>");
		n_r_i++;
	}
	
	// new writing words
	out.println("<tr><td>");
	%>
	<p><b><bean:message key="weekly_list.new"/>&nbsp;
	<bean:message key="app.small_writing"/>
	<%
	out.println("</td><td>");
	%>
	<b><bean:message key="weekly_list.list"/></b>
	<%
	out.println("</td></tr>");
	int new_words_w_size = w_new_words.size();
	int n_w_i = 0;
	while (n_w_i < new_words_w_size)
	{
		Word n_w_word = (Word)w_new_words.get(n_w_i);
		out.println("<tr><td>");
		out.println("<p>"+n_w_word.getDefinition()+"</p>");
		out.println("</td>");
		out.println("<td>");
		out.println("<p>"+n_w_word.getText());
		out.println("</td></tr>");
		n_w_i++;
	}
	
}

// level zero words reading words
out.println("<tr><td>");
%>
<p><b><bean:message key="weekly_list.level_zero"/>&nbsp;
<bean:message key="app.small_reading"/>
<%
out.println("</td><td>");
%>
<b><bean:message key="weekly_list.list"/></b>
<%
out.println("</td></tr>");
Vector r_all_words = (Vector)session.getAttribute("r_all_words");
int r_size = r_all_words.size();
int r_i = 0;
while (r_i<r_size)
{
	Word r_word = (Word)r_all_words.get(r_i);
	out.println("<tr><td>");
	out.println("<p>"+r_word.getText());
	out.println("</td>");
	out.println("<td>");
	out.println("<p>"+r_word.getDefinition()+"</p>");
	out.println("</td></tr>");
	r_i++;
}

// level zero words writing words
out.println("<tr><td>");
%>
<p><b><bean:message key="weekly_list.level_zero"/>&nbsp;
<bean:message key="app.small_writing"/>
<%
out.println("</td><td>");
%>
<b><bean:message key="weekly_list.list"/></b>
<%
out.println("</td></tr>");
Vector w_all_words = (Vector)session.getAttribute("w_all_words");
int size = w_all_words.size();
int i = 0;
while (i<size)
{
	Word word = (Word)w_all_words.get(i);
	out.println("<tr><td>");
	out.println("<p>"+word.getDefinition()+"</p>");
	out.println("</td>");
	out.println("<td>");
	out.println("<p>"+word.getText());
	out.println("</td></tr>");
	i++;
}

Hashtable r_words_defs = (Hashtable)session.getAttribute("r_words_defs");
Hashtable w_words_defs = (Hashtable)session.getAttribute("w_words_defs");
if (r_words_defs.size()>0||w_words_defs.size()>0)
{
	// missed words marker
	out.println("<tr><td>");
	%>
	<p><b><bean:message key="weekly_list.words_defs"/></b>
	<%
}
out.println("<td/>");
out.println("<td></td></tr>");
for (Enumeration r_e = r_words_defs.keys() ; r_e.hasMoreElements() ;)
	    {
		    String r_key = (String)r_e.nextElement();
		    String r_val = (String)r_words_defs.get(r_key);
		    out.println("<tr><td>");
		    out.println("<p>"+r_key);
		    out.println("</td>");
		    out.println("<td>");
		    out.println("<p>"+r_val);
		    out.println("</td></tr>");
	    }
for (Enumeration e = w_words_defs.keys() ; e.hasMoreElements() ;) 
	    {
		    String key = (String)e.nextElement();
		    String val = (String)w_words_defs.get(key);
		    out.println("<tr><td>");
		    out.println("<p>"+val);
		    out.println("</td>");
		    out.println("<td>");
		    out.println("<p>"+key);
		    out.println("</td></tr>");
	    }
out.println("</table>");
int total_words = r_all_words.size() + w_all_words.size() + r_words_defs.size() + w_words_defs.size();
%>
<p><b><bean:message key="weekly_list.total_words"/></b>&nbsp;:&nbsp;
<%
out.println(total_words);
%>
<p><a href="/indoct/weekly_list_format.do"><bean:message key="welcome.weekly_list_format"/></a>
<p><a href="/indoct/weekly_list_format.do?type=reading"><bean:message key="welcome.weekly_list_format"/>&nbsp;<bean:message key="app.small_reading"/></a>
</body>
</html>
