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

<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
	<table>
		<tr>
			<td><bean:message key="created_integrated_tests.date"/></td>
			<td><bean:message key="created_integrated_tests.name"/></td>
			<td><bean:message key="created_integrated_tests.type"/></td>
			<td><bean:message key="created_integrated_tests.status"/></td>
			<td><bean:message key="created_integrated_tests.format"/></td>
			<td><bean:message key="created_integrated_tests.creation_time"/></td>
		</tr>
<%		

		SavedTest saved_test = (SavedTest)session.getAttribute("saved_test");
	    String test_id = saved_test.getTestId();
	    String test_date = saved_test.getTestDate();
	    String test_name = saved_test.getTestName();
	    String test_type = saved_test.getTestType();
	    String test_status = saved_test.getTestStatus();
	    String test_format = saved_test.getTestFormat();
	    String creation_time = saved_test.getCreationTime();
		out.println("<tr>");
		out.println("<td>"+Transformer.getDateFromMilliseconds(test_date)+"</td>");
		out.println("<td>"+test_name+"</td>"); 
		out.println("<td>"+test_type+"</td>");
		out.println("<td>"+test_status+"</td>");
		out.println("<td>"+test_format+"</td>");
		out.println("<td>"+Transformer.getDateFromMilliseconds(creation_time)+"</td>");
%>
		 </tr>
	</table>

	<table>
		<tr>
			<td><bean:message key="teacher_create_tests_result.student_id"/></td>
			<td><bean:message key="teacher_create_tests_result.student_name"/></td>
			<td><bean:message key="teacher_create_tests_result.num_of_words_in_test"/></td>
		</tr>

<%
		Hashtable student_test_totals = (Hashtable)session.getAttribute("student_test_totals");
		Hashtable student_test_ids = (Hashtable)session.getAttribute("student_test_ids");
		Hashtable students = (Hashtable)session.getAttribute("students");
		Vector students_names = (Vector)session.getAttribute("students_names");

		Enumeration keys = students.keys();
		while (keys.hasMoreElements())
		{
			String student_id = (String)keys.nextElement();
			String student_name = (String)students.get(student_id);
			String student_test_total = (String)student_test_totals.get(student_id);
			out.println("<tr>");
			out.println("<td>"+student_id+"</td>");
			out.println("<td>"+student_name+"</td>");
			out.println("<td>"+student_test_total+"</td>");
			out.println("</tr>");
		}
%>
	</table>
<p>
<p><a href="/indoct/jsps/testing/create_integrated_test_name.jsp"><bean:message key="tests.create_integrated_test"/></a> 
<p>
<p><p><a href="/indoct/jsps/teacher/class_home.jsp"><bean:message key="teacher.return_to_class_home"/></a>
<p>
<p><a href="/indoct/jsps/teacher/welcome_teacher.jsp"><bean:message key="teacher.return_to_main"/></a>
<p>

</body>
</html>