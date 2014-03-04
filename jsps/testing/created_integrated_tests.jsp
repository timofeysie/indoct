<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="java.util.Vector" %>
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
Vector saved_tests = (Vector)session.getAttribute("saved_tests");
int size = saved_tests.size();
int i = 0;
while (i<size)
{
	try
	{
		SavedTest saved_test = (SavedTest)saved_tests.get(i);
	    String test_id = saved_test.getTestId();
	    String test_date = saved_test.getTestDate();
	    String test_name = saved_test.getTestName();
	    String test_type = saved_test.getTestType();
	    String test_status = saved_test.getTestStatus();
	    String test_format = saved_test.getTestFormat();
	    String creation_time = saved_test.getCreationTime();
		out.println("<tr>");
		out.println("<td>"+Transformer.getDateFromMilliseconds(test_date)+"</td>");
		out.println("<td><a href=\"/indoct/created_integrated_test_display.do?test_id="+test_id+"\">"+test_name+"</a></td>"); 
		out.println("<td>"+test_type+"</td>");
		out.println("<td>"+test_status+"</td>");
		out.println("<td>"+test_format+"</td>");
		out.println("<td>"+Transformer.getDateFromMilliseconds(creation_time)+"</td>");
		out.println("<td><a href=\"/indoct/created_integrated_test_score.do?test_id="+test_id+"\">");
			%> <bean:message key="created_integrated_tests.score_test"/></a></td> <%	
		out.println("</tr>");
	} catch (java.lang.NullPointerException npe)
	{
		
	}
	i++;
}
out.println("</table>");

%>

<p>
<p><a href="/indoct/jsps/testing/create_integrated_test_name.jsp"><bean:message key="tests.create_integrated_test"/></a> 
<p>
<p>
<a href="/indoct/welcome.jsp"><bean:message key="add_word.return_to_main"/>
</body>
</html>