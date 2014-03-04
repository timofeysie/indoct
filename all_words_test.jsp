<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="com.businessglue.indoct.UserBean" %>
<%@ page language="java" import="org.catechis.dto.Word" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="org.catechis.dto.AllWordsTest" %>
<%@ page language="java" import="org.catechis.dto.WordTestResult" %>
<%@ page language="java" import="org.catechis.dto.WordLastTestDates" %>
<%@ page language="java" import="java.util.List" %>

<%@ page language="java" import="java.util.Date" %>
<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.ArrayList" %>
<%@ page language="java" import="java.util.Enumeration" %>
<%@ page language="java" import="java.text.DecimalFormat" %>
<%@ page language="java" import="java.text.ParsePosition" %>
<%@ page language="java" import="java.text.SimpleDateFormat" %>

<%@ page language="java" import="org.catechis.dto.WordFilter" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<%
Hashtable all_words_indexed = (Hashtable)session.getAttribute("all_words_indexed");
ArrayList actual_key_list = (ArrayList)session.getAttribute("actual_key_list");
AllWordsTest awt_test_word = (AllWordsTest)session.getAttribute("awt_test_word");
WordLastTestDates wltd = (WordLastTestDates)session.getAttribute("wltd");
out.println("<p>"++awt_test_word.getCategory());
out.println("<form METHOD=\"post\" ACTION=\"all_words_test.do\">");
	out.println("<table>");
	String key = (String)keys.nextElement();
	String value = (String)test_file.get(key);
	out.println("<TR>");
	out.println("<TD><B>");
	out.println(value);
	out.println("</B></TD>");
	out.println("<TD>");
	out.println("<input TYPE=\"text\" NAME=\"field"+test_i+"\" SIZE=\"10\" MAXLENGTH=\"10\">");
	out.println("</TD>");
	out.println("</TR>");
	out.println("</table>");
%>
<html:submit/>
</form>
<p>
<p><a href="/indoct/all_words_test.do">skip word</a>
<p><a href="/indoct/all_words_test.do">return to main menu</a>
</body>
</html>
