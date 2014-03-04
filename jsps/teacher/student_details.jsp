<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.text.DecimalFormat" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>

<%
Hashtable last_record = (Hashtable)session.getAttribute("last_record");
DecimalFormat formatter = new DecimalFormat("###.##");
%>
<table>
	<tr>
		<td align=center><bean:message key="welcome.stats.header"/></td>
	</tr>
	<tr>
		<td><a href="/indoct/list.do?filename=all"><bean:message key="welcome.stats.number_of_words"/></a></td>
		<td><% out.println(last_record.get("number_of_words")); %></td>
	</tr>
	
	<table bgcolor="#FFFFCC">
	<tr>
		<td><bean:message key="stats.level"/></td>
		<td><bean:message key="app.reading"/></td>
		<td><bean:message key="app.writing"/></td>
	</tr>
	
	<tr>
		<!-- number of words at each level table -->
		<tr>
			<td>0</td>
			<td><% out.println(last_record.get("words_at_reading_level_0")); %></td>
			<td><% out.println(last_record.get("words_at_writing_level_0")); %></td>
		</tr>
		<tr>
			<td>1</td>
			<td><% out.println(last_record.get("words_at_reading_level_1")); %></td>
			<td><% out.println(last_record.get("words_at_writing_level_1")); %></td>
		</tr>
		<tr>
			<td>2</td>
			<td><% out.println(last_record.get("words_at_reading_level_2")); %></td>
			<td><% out.println(last_record.get("words_at_writing_level_2")); %></td>
		</tr>
		<tr>
			<td>3</td>
			<td><% out.println(last_record.get("words_at_reading_level_3")); %></td>
			<td><% out.println(last_record.get("words_at_writing_level_3")); %></td>
		</tr>
		<tr>
			<td><bean:message key="welcome.stats.average"/></td>
			<td><% 
				try
				{
					out.println(formatter.format(Double.parseDouble((String)last_record.get("reading_average")))); 
				} catch (java.lang.NullPointerException npe)
				{
				}
			%></td>
			<td><% 
				try
				{ 
					out.println(formatter.format(Double.parseDouble((String)last_record.get("writing_average"))));  
				} catch (java.lang.NullPointerException npe)
				{
				}
			%></td>
		</tr>
		<!-- end number of words at each level table -->
	</tr>
	</table>
	
	<table>
		<td><tr><bean:message key="welcome.stats.date"/>&nbsp;:&nbsp;</tr>
		<tr><%  
				try
				{
					out.println(Transformer.simplifyDate((String)last_record.get("date"))); 
				} catch (java.lang.NullPointerException npe)
				{
				}
		%></tr>
		<tr><a href="/indoct/recalculate_stats.do"><bean:message key="welcome.stats.recalculate"/></a></tr></td>
	</table>
</table>
<p>
<p><a href="/indoct/jsps/testing/create_daily_test_name.jsp"><bean:message key="tests.create_daily_test"/></a>
<p>
<jsp:include page="/jsps/includes/sponsor_end.jsp" />
<p>
<p><p><a href="/indoct/jsps/teacher/class_home.jsp"><bean:message key="teacher.return_to_class_home"/></a>
<p>
<p><a href="/indoct/jsps/teacher/welcome_teacher.jsp"><bean:message key="teacher.return_to_main"/></a>
<p>
</body>
</html>
