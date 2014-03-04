<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<%@ page language="java" import="org.catechis.dto.SavedTest" %>
<%@ page language="java" import="org.catechis.dto.Word" %>
<%@ page language="java" import="org.catechis.EncodeString" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="org.catechis.file.FileSaveTests" %>
<%@ page language="java" import="org.catechis.constants.Constants" %>

<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<%
String class_name = (String)session.getAttribute("class_name");
out.println("<p><b>"+class_name+"</b>");
%>
	<table>
		<tr>
			<td><b><bean:message key="teacher_create_tests_result.student_name"/></b></td>
			<td><b><bean:message key="created_integrated_tests.status"/></b></td>
			<td><b><bean:message key="created_integrated_tests.score"/></td>
		</tr>
<%		
		Hashtable students = (Hashtable)session.getAttribute("students");
		Vector student_ids = (Vector)session.getAttribute("student_ids");
		Hashtable class_tests = (Hashtable)session.getAttribute("class_tests");
		String test_name = "";
		String class_test_date = "";
		String class_test_type = "";
		String class_test_format = "";
		String class_test_creation_time = "";
		for (Enumeration keys = class_tests.keys(); keys.hasMoreElements() ;) 
		{
			String student_id = (String)keys.nextElement();
			String student_name = (String)students.get(student_id);
			SavedTest saved_test = (SavedTest)class_tests.get(student_id);
	    	String test_id = saved_test.getTestId();
	    	String test_date = saved_test.getTestDate();
	    	test_name = saved_test.getTestName();
	    	String test_type = saved_test.getTestType();
	    	String test_status = saved_test.getTestStatus();
	    	String test_format = saved_test.getTestFormat();
	    	String creation_time = saved_test.getCreationTime();
	    	String test_score = saved_test.getTestScore();
	    	// save variables that are the same for all test to display after
	    	class_test_date = test_date;
			class_test_type = test_type;
			class_test_format = test_format;
			class_test_creation_time = creation_time;
			out.println("<tr>");
			out.println("<td>");
			out.println("<a href=\"/indoct/created_game_cards.do?student_id="+student_id+"\">"+student_name);
			out.println("</td>");
			out.println("<td>"+test_status+"</td>"); 
			if (test_status.equals(Constants.PENDING))
			{
				%>
					<td>
					<%
						out.println("<a href=\"/indoct/created_integrated_test_score.do?student_id="+student_id+"\">");
					%>
					<bean:message key="created_integrated_tests.score_test"/></a></td>
				<%
			} else
			{
				out.println("<td>"+test_score+"%</td>"); 
			}
			out.println("</tr>");
		}
out.println("</table>");
out.println("<p>");

%>
<bean:message key="saved_class_tests.all_tests_properties"/>
<table>
	<tr>
		<td><b><bean:message key="created_integrated_tests.date"/></b></td>
		<td><b><bean:message key="created_integrated_tests.type"/></b></td>
		<td><b><bean:message key="created_integrated_tests.format"/></b></td>
		<td><b><bean:message key="created_integrated_tests.creation_time"/></b></td>
	</td>
<%
out.println("<tr><td>"+Transformer.getDateFromMilliseconds(class_test_date)+"</td>");
out.println("<td>"+class_test_type+"</td>");
out.println("<td>"+class_test_format+"</td>");
try
{
	out.println("<td>"+Transformer.getDateFromMilliseconds(class_test_creation_time)+"</b></td></tr>");
} catch (java.lang.NumberFormatException nfe)
{
	out.println("<td>nfe</td></tr>");
}
%>
</table>
<p>
<p><a href="/indoct/delete_saved_class_test.do"><bean:message key="saved_class_tests.delete_this_test"/></a>
<p>
<p><a href="/indoct/jsps/testing/create_integrated_test_name.jsp"><bean:message key="tests.create_integrated_test"/></a> 
<p>
<p><p><a href="/indoct/jsps/teacher/class_home.jsp"><bean:message key="teacher.return_to_class_home"/></a>
<p>
<p><a href="/indoct/jsps/teacher/welcome_teacher.jsp"><bean:message key="teacher.return_to_main"/></a>
<p>
<p>
 
</body>
</html>