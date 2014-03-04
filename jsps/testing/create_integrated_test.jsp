<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="org.catechis.dto.Word" %>
<%@ page language="java" import="org.catechis.constants.Constants" %>
<%@ page language="java" import="org.catechis.dto.Test" %>
<%@ page language="java" import="java.util.Vector" %>
<html>
<head><title><%out.println((String)session.getAttribute("user_name"));%></title></head>
<body>
<%
out.println("<table>");
Vector test_words = (Vector)session.getAttribute("test_words");
int size = test_words.size();
int i = 0;
while (i<size)
{
	try
	{
		Word word = (Word)test_words.get(i);
		Test [] tests = word.getTests();
		Test test = tests[0];
		String type = test.getType();
		out.println("<tr><td>");
		if (type.equals(Constants.READING))
		{
			out.println((i+1)+"	"+word.getText()+"</td>");
			out.println("<td>	__________________</td>");
			out.println("<td>	(  )</td><td>&nbsp</td><td>  (  )</td>");
		} else if (type.equals(Constants.WRITING))
		{
			out.println((i+1)+"	"+word.getDefinition()+"</td>");
			out.println("<td>	__________________</td>");
			out.println("<td>	(  )</td><td>&nbsp</td><td>  (  )</td>");
		}
		out.println("</tr>");
	} catch (java.lang.NullPointerException npe)
	{
		
	}
	i++;
}
out.println("</table>");

%>

<p>
<a href="/indoct/create_integrated_test_save.do"><bean:message key="create_integrated_test.created_integrated_tests_save"/></a>
<p>
<a href="/indoct/welcome.jsp"><bean:message key="add_word.return_to_main"/>
</body>
</html>
