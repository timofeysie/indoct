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
<%@ page language="java" import="java.util.List" %>

<%@ page language="java" import="java.util.Date" %>
<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<%@ page language="java" import="java.text.DecimalFormat" %>
<%@ page language="java" import="java.text.ParsePosition" %>
<%@ page language="java" import="java.text.SimpleDateFormat" %>
<%@ page language="java" import="org.catechis.dto.AllStatsHistory" %>
<%@ page language="java" import="org.catechis.dto.WeeklyReport" %>
<%@ page language="java" import="org.catechis.dto.SessionsReport" %>
<%@ page language="java" import="org.catechis.lists.Sarray" %>
<%@ page language="java" import="org.catechis.lists.Sortable" %>

<html>
<head><title><bean:message key="app.name"/></title></head>
<body>

<p><b><bean:message key="history.current_sessions_info"/></b>
<% SessionsReport sessions_report = (SessionsReport)session.getAttribute("sessions_report");%>

<p><b><bean:message key="history.week_of_year"/></b>
<% out.println(sessions_report.getWeekOfYear()); %>

<p><b><bean:message key="history.number_of_sessions"/></b>
<%out.println(sessions_report.getNumberOfSessions());%>

<p></b><bean:message key="history.number_of_tests"/></b>
<%out.println(sessions_report.getNumberOfTestsInWeek());%>

<p><b><bean:message key="history.previous_sessions_info"/></b>
<TABLE>
  <TR>
    <TH><b><bean:message key="history.week"/></b></TH>
    <TH><b><bean:message key="history.session"/></b></TH>
    <TH><b><bean:message key="history.tests"/></b></TH>
    <TH><b><bean:message key="history.time"/></b></TH>
    <TH><b><bean:message key="history.words"/></b></TH>
    <TH><b><bean:message key="history.r_avg"/></b></TH>
    <TH><b><bean:message key="history.w_avg"/></b></TH>
  </TR>
<%
Sarray weekly_reports = (Sarray)session.getAttribute("weekly_reports");
for ( int i = 0 ; i < weekly_reports.size() ; i++ )
{
    Sortable s =(Sortable)weekly_reports.elementAt(i);
    String object_key = (String)s.getKey();
    WeeklyReport wr = (WeeklyReport)s.getObject();
	DecimalFormat format = new DecimalFormat("###.##");	
	String average_time_spent_testing = average_time_spent_testing = Transformer.getMinutesFromLong(wr.getAverageTimeSpentTesting());
	int number_of_sessions = wr.getNumberOfSessions();
	int number_of_tests_in_a_week = wr.getNumberOfTestsInWeek();
	int number_of_words = wr.getNumberOfWords();
	double reading_avg  = wr.getReadingAverage();
	double writing_avg  = wr.getWritingAverage();
	String week_of_year = wr.getWeekOfYear();
	out.println("<TR>");
	out.println("<TD>"+week_of_year+"</TD>");
	out.println("<TD>"+number_of_sessions+"</TD>");
	out.println("<TD>"+number_of_tests_in_a_week+"</TD>");
	out.println("<TD>"+average_time_spent_testing+"</TD>");
	out.println("<TD>"+number_of_words+"</TD>");
	out.println("<TD>"+format.format(reading_avg)+"</TD>");
	out.println("<TD>"+format.format(writing_avg)+"</TD>");
	out.println("</TR>");
}
%>
</TABLE>


<TABLE>
  <TR>
    <TH><b><bean:message key="history.date"/></b></TH>
    <TH><b><bean:message key="history.total_tests"/></b></TH>
    <TH><b><bean:message key="history.average"/></b></TH>
    <TH><b><bean:message key="history.total_words"/></b></TH>
    <TH><b><bean:message key="history.r_avg"/></b></TH>
    <TH><b><bean:message key="history.w_avg"/></b></TH>
    <TH><b><bean:message key="history.r_0"/></b></TH>
    <TH><b><bean:message key="history.w_0"/></b></TH>
    <TH><b><bean:message key="history.r_1"/></b></TH>
    <TH><b><bean:message key="history.w_1"/></b></TH>
    <TH><b><bean:message key="history.r_2"/></b></TH>
    <TH><b><bean:message key="history.w_2"/></b></TH>
    <TH><b><bean:message key="history.r_3"/></b></TH>
    <TH><b><bean:message key="history.w_3"/></b></TH>
  </TR>
<%
Vector all_stats_histories = (Vector)session.getAttribute("all_stats_histories");
int size = all_stats_histories.size();
DecimalFormat formatt = new DecimalFormat("###.##");

for (int i = 0; i < size; i++) 
{
	AllStatsHistory all_stats_history = (AllStatsHistory)all_stats_histories.get(i);
	Vector reading_levels = all_stats_history.getReadingLevels();
	Vector writing_levels = all_stats_history.getWritingLevels();
	out.println("<TR>");
	out.println("<TD>"+Transformer.simplifyDate(all_stats_history.getDate())+"</TD>");
	out.println("<TD>"+all_stats_history.getNumberOfTests()+"</TD>");
	out.println("<TD>"+formatt.format(all_stats_history.getAverageScore())+"</TD>");
	out.println("<TD>"+all_stats_history.getNumberOfWords()+"</TD>");
	out.println("<TD>"+formatt.format(all_stats_history.getReadingAverage())+"</TD>");
	out.println("<TD>"+formatt.format(all_stats_history.getWritingAverage())+"</TD>");
	out.println("<TD>"+reading_levels.get(0)+"</TD>");
	out.println("<TD>"+writing_levels.get(0)+"</TD>");
	out.println("<TD>"+reading_levels.get(1)+"</TD>");
	out.println("<TD>"+writing_levels.get(1)+"</TD>");
	out.println("<TD>"+reading_levels.get(2)+"</TD>");
	out.println("<TD>"+writing_levels.get(2)+"</TD>");
	out.println("<TD>"+reading_levels.get(3)+"</TD>");
	out.println("<TD>"+writing_levels.get(3)+"</TD>");
	out.println("</TR>");
}
%>
</TABLE>


</body>
</html>
