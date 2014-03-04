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
<%@ page language="java" import="org.catechis.dto.Word" %>
<%@ page language="java" import="java.util.Date" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<%@ page language="java" import="java.text.DecimalFormat" %>
<%@ page language="java" import="java.text.ParsePosition" %>
<%@ page language="java" import="java.text.SimpleDateFormat" %>
<%@ page language="java" import="java.util.List" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<%
	Hashtable text_defs = (Hashtable)session.getAttribute("words_defs");
	out.println("<table><FONT SIZE=\"+2\">");
	Enumeration keys = text_defs.keys(); 
	while (keys.hasMoreElements())
	{
		String key = (String)keys.nextElement();
		String value = (String)text_defs.get(key);
		out.println("<TR>");
			out.println("<TD><B>");
			out.println(value);
			out.println("</B></TD>");
			out.println("<TD>&nbsp;</TD>");
			out.println("<TD><B>");
			out.println(key);
			out.println("</TD></B>");
		out.println("</TR>");
	}
	out.println("</table></FONT> ");
%>
<p>
<p><a href="/indoct/jsps/teacher/create_test_name.jsp"><bean:message key="tests.create_integrated_test"/></a> 
<p>
<p><a href="/indoct/reset_login_entries.do"><bean:message key="teacher.return_to_class"/></a>
<p>
<p><a href="/indoct/jsps/teacher/welcome_teacher.jsp"><bean:message key="teacher.return_to_main"/></a>

</body>
</html>
