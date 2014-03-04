<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Vector" %>
<html>
<head><title><%out.println(session.getAttribute("file_name"));%></title></head>
<body>
<%
Vector users_options = (Vector)session.getAttribute("users_opts");
Vector users = (Vector)session.getAttribute("users");
out.println("<table><FONT SIZE=\"+2\">");
%>
	<tr>
	<b>
		<td><bean:message key="users_options.users_name"/></td>
		<td><bean:message key="users_options.native_languge"/></td>
		<td><bean:message key="users_options.language_being_learned"/></td>
		<td><bean:message key="users_options.encoding"/></td>
		<td><bean:message key="users_options.locale"/></td>
		<td><bean:message key="users_options.grade_whitespace"/></td>
		<td><bean:message key="users_options.max_level"/></td>
		<td><bean:message key="users_options.exclude_chars"/></td>
		<td><bean:message key="users_options.exclude_level0_test"/></td>
		<td><bean:message key="users_options.exclude_level1_test"/></td>
		<td><bean:message key="users_options.exclude_level2_test"/></td>
		<td><bean:message key="users_options.exclude_level3_test"/></td>
		<td><bean:message key="users_options.daily_words_limit"/></td>
		<td><bean:message key="users_options.record_passed_tests"/></td>
		<td><bean:message key="users_options.record_failed_tests"/></td>
		<td><bean:message key="users_options.record_limit"/></td>
		<td><bean:message key="users_options.record_exclude_level"/></td>
		<td><bean:message key="users_options.weekly_list_remove_repeats"/></td>
	</b>
	</tr>
<%
int size = users_options.size();
int i = 0;
while (i<size)
{
	String user = (String)users.get(i);
	Hashtable user_opts = (Hashtable)users_options.get(i);
		out.println("<TR>");
			out.println("<TD><B>");
			out.println("<p><a href=\"/indoct/users_details.do?user="+user+"\">"+user+"</a>");
			out.println("</B></TD>");
			
			out.println("<TD>");
			out.println((String)user_opts.get("native_languge"));
			out.println("</TD>");
			out.println("<TD>");
			out.println((String)user_opts.get("language_being_learned"));
			out.println("</TD>");
			out.println("<TD>");
			out.println((String)user_opts.get("encoding"));
			out.println("</TD>");
			out.println("<TD>");
			out.println((String)user_opts.get("locale"));
			out.println("</TD>");
			out.println("<TD>");
			out.println((String)user_opts.get("grade_whitespace"));
			out.println("</TD>");
			out.println("<TD>");
			out.println((String)user_opts.get("max_level"));
			out.println("</TD>");
			out.println("<TD>");
			out.println((String)user_opts.get("exclude_chars"));
			out.println("</TD>");
			out.println("<TD>");
			out.println((String)user_opts.get("exclude_level0_test"));
			out.println("</TD>");
			out.println("<TD>");
			out.println((String)user_opts.get("exclude_level1_test"));
			out.println("</TD>");
			out.println("<TD>");
			out.println((String)user_opts.get("exclude_level2_test"));
			out.println("</TD>");
			out.println("<TD>");
			out.println((String)user_opts.get("exclude_level3_test"));
			out.println("</TD>");
			out.println("<TD>");
			out.println((String)user_opts.get("daily_words_limit"));
			out.println("</TD>");
			out.println("<TD>");
			out.println((String)user_opts.get("record_passed_tests"));
			out.println("</TD>");
			out.println("<TD>");
			out.println((String)user_opts.get("record_failed_tests"));
			out.println("</TD>");
			out.println("<TD>");
			out.println((String)user_opts.get("record_limit"));
			out.println("</TD>");
			out.println("<TD>");
			out.println((String)user_opts.get("record_exclude_level"));
			out.println("</TD>");
			out.println("<TD>");
			out.println((String)user_opts.get("weekly_list_remove_repeats"));
			out.println("</TD>");
		out.println("</TR>");
	i++;
}
	out.println("</table></FONT> ");
%>
<p>
<p><b><bean:message key="categories.options"/></b>
<p>
<p><a href="/indoct/jsps/admin/add_users_options.jsp"><bean:message key="admin.add_users_options"/></a>

</body>
</html>
