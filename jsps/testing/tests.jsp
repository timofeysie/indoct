<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="com.businessglue.indoct.UserBean" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="org.catechis.Domartin" %>
<%@ page language="java" import="org.catechis.dto.AllTestStats" %>
<%@ page language="java" import="org.catechis.dto.AllWordStats" %>
<%@ page language="java" import="org.catechis.dto.TestStats" %>
<%@ page language="java" import="org.catechis.dto.WordStats" %>
<%@ page language="java" import="org.catechis.dto.RatesOfForgetting" %>
<%@ page language="java" import="org.catechis.file.FileCategories" %>
<%@ page language="java" import="java.util.Date" %>
<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="java.text.DecimalFormat" %>
<%@ page language="java" import="java.text.ParsePosition" %>
<%@ page language="java" import="java.text.SimpleDateFormat" %>
<%@ page language="java" import="java.util.List" %>
<%@ page language="java" import="java.util.ArrayList" %>
<%@ page language="java" import="java.util.Hashtable" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>

<TABLE>
  <TR>
    <TH><b>Test</b></TH>
    <TH><b>Total</b></TH>
    <TH><b>Average</b></TH>
    <TH><b>Last Test Date</b></TH>
  </TR>
<%
AllTestStats all_test_stats = (AllTestStats)session.getAttribute("all_test_stats");
TestStats[] test_stats = all_test_stats.getTestStats();
DecimalFormat formatter = new DecimalFormat("###.##");
out.println("<TR><TD><a href=\"/indoct/all_words_test.do?filename=all\">All Tests</a></TD>");
out.println("<TD>"+all_test_stats.getNumberOfTests()+"</TD>");
out.println("<TD>"+formatter.format(all_test_stats.getAverageScore())+"</TD>");
out.println("<TD>last Test n/a</TD></TR>");
// all of the tests would be test_stats.length
// but we limit it to 0-7 for levels 0 to 3 reading.writing levels
try
{
	for (int i = 0; i < 8; i++) 
	{
		TestStats test = test_stats[i];
		String name = test.getName();
		out.println("<TR><TD><a href=\"/indoct/file.do?filename="+name+"\">"+name+"</a></TD>");
		out.println("<TD>"+test.getTotalTests()+"</TD>");
		out.println("<TD>"+formatter.format(test.getAverageScore())+"</TD>");
		try
		{
			out.println("<TD>"+Transformer.simplifyDate(test.getLastDate())+"</TD></TR>");
		} catch (java.lang.NullPointerException npe)
		{
			out.println("<TD>n/a</TD></TR>");
		}
	}
} catch (java.lang.ArrayIndexOutOfBoundsException aioob)
{}
%>
</TABLE>
<p>
<p><a href="/indoct/daily_test.do?test_type=reading"><bean:message key="welcome.daily_reading_test"/></a>
<p>
<p><a href="/indoct/daily_test.do?test_type=writing"><bean:message key="welcome.daily_writing_test"/></a>
<p>
<p><a href="/indoct/created_tests.do"><bean:message key="tests.created_tests"/></a> 
<p>
<a href="/indoct/welcome.jsp"><bean:message key="add_word.return_to_main"/>
</body>
</html>
