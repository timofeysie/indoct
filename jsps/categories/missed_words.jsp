<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<%@ page language="java" import="org.catechis.dto.Word" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<table>
<%
Hashtable r_words_defs = (Hashtable)session.getAttribute("r_words_defs");
Hashtable w_words_defs = (Hashtable)session.getAttribute("w_words_defs");
out.println("<tr><td>");
%>
<p><b><bean:message key="weekly_list.words_defs"/></b>
<%
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
int total_words =  r_words_defs.size() + w_words_defs.size();
%>
<p><b><bean:message key="weekly_list.total_words"/></b>&nbsp;:&nbsp;
<%
out.println(total_words);
%>
</body>
</html>
