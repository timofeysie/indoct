<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<%@ page language="java" import="java.text.DecimalFormat" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<%
String class_name = (String)session.getAttribute("class_name");
System.out.println("<html>");
System.out.println("<head><title>"+class_name+"</title></head><body>");
Hashtable student_records = (Hashtable)session.getAttribute("student_records");
%>
<table>
<%
Enumeration keys = student_records.keys();
while (keys.hasMoreElements())
{
	String key = (String)keys.nextElement();
	String val = (String)student_records.get(key);
%>
		<tr>
			<td><%System.out.println(key);%></td>
			<td><%System.out.println(val);%></td>
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
