<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="com.businessglue.indoct.UserBean" %>
<%@ page language="java" import="org.catechis.dto.Word" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="org.catechis.dto.AllWordsTest" %>
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
out.println("<p>"+awt_test_word.getText());
out.println("		"+awt_test_word.getDefinition());
out.println("		"+awt_test_word.getCategory());
int size = actual_key_list.size();
int i = 0;
out.println("<table>");
while (i<size)
{
	try
	{
		Long actual_key = (Long)actual_key_list.get(i);
		String actual_str_key = actual_key.toString(); 
		Word next_word = wltd.getWord(actual_str_key);
		out.println("<tr><td>"+i+"</TD>");
		out.println("<td>"+next_word.getText()+"</TD>");
		out.println("<td>"+next_word.getDefinition()+"</TD>");
		out.println("<td>"+next_word.getCategory()+"</TD>");
		out.println("<td>"+Transformer.getDateFromMilliseconds(actual_str_key)+"</TD></tr>");
	} catch (java.lang.NullPointerException npe)
	{}
	i++;
}
out.println("</table>");
%>
<!--form method="post" action="/indoct/filter_test_run.do">
	<input type"text" name="exclude_time" size="10" maxlength="10"/>
	<html:submit/> 
</form-->
<p>
<p><a href="/indoct/all_words_test.do">skip word</a>
<p><a href="/indoct/all_words_test.do">return to main menu</a>
</body>
</html>
