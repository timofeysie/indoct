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
Hashtable user_ids = (Hashtable)session.getAttribute("user_ids");
String class_name = (String)session.getAttribute("class_name");
String class_id = (String)session.getAttribute("class_id");
out.println("<head><title>"+class_name+"</title></head>");
%>
<body>
<p><b><bean:message key="teacher.class_home"/></b>
<%

Enumeration keys = user_ids.keys();
while (keys.hasMoreElements())
{
	String student_name = (String)keys.nextElement();
	String student_pass = (String)user_ids.get(student_name);
	out.println("<p>"+student_name+" "+student_pass);
}


%>
<p>
<p><b><bean:message key="categories.options"/></b>
<p>
<p><a href="/indoct/jsps/teacher/teacher_import_words.jsp"><bean:message key="teacher.import_word_list"/></a>
<p>
<p><a href="/indoct/class_details.do"><bean:message key="teacher.student_details"/></a>
<p>
<p><a href="/indoct/users_options.do"><bean:message key="welcome_admin.users_options"/></a>
<p>
<p><a href="/indoct/jsps/teacher/create_students.jsp"><bean:message key="teacher.create_students"/></a>
<p>
<p><a href="/indoct/reset_login_entries.do"><bean:message key="teacher.return_to_class"/></a>
<p>
<p>
<p><p><a href="/indoct/jsps/teacher/class_home.jsp"><bean:message key="teacher.return_to_class_home"/></a>
<p>
<p><a href="/indoct/jsps/teacher/welcome_teacher.jsp"><bean:message key="teacher.return_to_main"/></a>
</body>
</html>
