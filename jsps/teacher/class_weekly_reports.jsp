<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<%@ page language="java" import="java.text.DecimalFormat" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="org.catechis.dto.WeeklyReport" %>

<%
String class_name = (String)session.getAttribute("class_name");
System.out.println("<html>");
System.out.println("<head><title>"+class_name+"</title></head><body>");
Hashtable student_records = (Hashtable)session.getAttribute("student_records");
%>
<table>
		<tr>
			<td><bean:message key="teacher.student_id"/></td>
			<td><bean:message key="teacher.week_of_year"/></td>
			<td><bean:message key="teacher.number_of_sessions"/></td>
			<td><bean:message key="teacher.average_time_spent_testing"/></td>
			<td><bean:message key="teacher.number_of_tests_in_a_week"/></td>
			<td><bean:message key="teacher.number_of_words"/></td>
			<td><bean:message key="teacher.reading_average"/></td>
			<td><bean:message key="teacher.writing_average"/></td>
			<td>r0</td>
			<td>r1</td>
			<td>r2</td>
			<td>r3</td>
			<td>w0</td>
			<td>w1</td>
			<td>w2</td>
			<td>w3</td>
		</tr>
<%
Enumeration keys = student_records.keys();
while (keys.hasMoreElements())
{
	String key = (String)keys.nextElement();
	WeeklyReport wr = (WeeklyReport)student_records.get(key);
	Vector reading_levels = wr.getReadingLevels();
	Vector writing_levels = wr.getWritingLevels();
	
%>
		<tr>
			<td><%System.out.println(key);%></td>
			<td><%System.out.println(wr.getWeekOfYear());%></td>
			<td><%System.out.println(wr.getNumberOfSessions());%></td>
			<td><%System.out.println(wr.getAverageTimeSpentTesting());%></td>
			<td><%System.out.println(wr.getNumberOfTestsInWeek());%></td>
			<td><%System.out.println(wr.getNumberOfWords());%></td>
			<td><%System.out.println(wr.getReadingAverage());%></td>
			<td><%System.out.println(wr.getWritingAverage());%></td>
			<td><%System.out.println((String)reading_levels.get(0));%></td>
			<td><%System.out.println((String)reading_levels.get(1));%></td>
			<td><%System.out.println((String)reading_levels.get(2));%></td>
			<td><%System.out.println((String)reading_levels.get(3));%></td>
			<td><%System.out.println((String)reading_levels.get(0));%></td>
			<td><%System.out.println((String)reading_levels.get(1));%></td>
			<td><%System.out.println((String)reading_levels.get(2));%></td>
			<td><%System.out.println((String)reading_levels.get(3));%></td>
		</tr>
<%
}
%>
</table>
<p>
<p><p><a href="/indoct/jsps/teacher/class_home.jsp"><bean:message key="teacher.return_to_class_home"/></a>
<p>
<p><a href="/indoct/jsps/teacher/welcome_teacher.jsp"><bean:message key="teacher.return_to_main"/></a>
<p>
<jsp:include page="/jsps/includes/sponsor_end.jsp" />

</body>
</html>
