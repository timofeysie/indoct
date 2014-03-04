<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="org.catechis.dto.Word" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<html>
<%
Hashtable classes = (Hashtable)session.getAttribute("classes");
String class_id = (String)session.getAttribute("class_id");
String class_name = (String)classes.get(class_id);
out.println("<head><title>"+class_name+"</title></head>");
%>
<body>
<p><b><bean:message key="teacher.class_home"/></b>
<%
Hashtable students = (Hashtable)session.getAttribute("students");
Vector students_names = (Vector)session.getAttribute("students_names");
Hashtable students_logins = (Hashtable)session.getAttribute("students_logins");

Enumeration keys = students.keys();
while (keys.hasMoreElements())
{
	String student_id = (String)keys.nextElement();
	String student_name = (String)students.get(student_id);
	out.println("<p><a href=\"/indoct/student_details.do?user="+student_id+"\">"+student_name+"</a>&nbsp;");
	try
	{
		String date = (String)students_logins.get(student_name);
		String students_login = Transformer.getLongDateFromMilliseconds(date);
		out.println(students_login);
	} catch (java.lang.NumberFormatException nfe)
	{
		out.println(" --- ");
	}
}


%>
<p>
<p><b><bean:message key="categories.options"/></b>
<p>
<p><a href="/indoct/class_details.do"><bean:message key="teacher.student_details"/></a>
<p>
<p><a href="/indoct/class_weekly_reports.do"><bean:message key="teacher.student_weekly_reports"/></a>
<p>
<p><a href="/indoct/users_options.do"><bean:message key="welcome_admin.users_options"/></a>
<p>
<p><a href="/indoct/choose_students_to_add_to_class.do"><bean:message key="teacher.class_home_choose_students_to_add_to_class"/></a>
<p>
<p><a href="/indoct/jsps/teacher/create_students.jsp"><bean:message key="teacher.create_students"/></a>
<p>
<p><a href="/indoct/jsps/teacher/add_word_list.jsp"><bean:message key="teacher.import_word_list"/></a>
<p>
<p><a href="/indoct/jsps/teacher/create_test_name.jsp"><bean:message key="teacher.create_tests"/></a>
<p>
<p><a href="/indoct/logins_records.do"><bean:message key="admin.logins_records_title"/></a>
<p>
<p><a href="/indoct/logins_refresh.do"><bean:message key="admin.logins_refresh_link"/></a>
<p>
<p><a href="/indoct/reset_login_entries.do"><bean:message key="admin.reset_login_entries_link"/></a>
<p>
<p><a href="/indoct/jsps/teacher/welcome_teacher.jsp"><bean:message key="teacher.return_to_main"/></a>

</body>
</html>
