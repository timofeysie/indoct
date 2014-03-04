<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="org.catechis.dto.Word" %>
<%@ page language="java" import="org.catechis.constants.Constants" %>
<%@ page language="java" import="org.catechis.dto.WordLastTestDates" %>
<%@ page language="java" import="java.util.ArrayList" %>
<html>
<head><title><%out.println((String)session.getAttribute("user_name"));%></title></head>
<body>
<table>
<%
out.println("<table>");
String type = (String)session.getAttribute("type");
String grand_index = (String)session.getAttribute("grand_index");
WordLastTestDates wltd = (WordLastTestDates)session.getAttribute("wltd");
ArrayList key_list = (ArrayList)session.getAttribute("actual_key_list");
int grand_int = Integer.parseInt(grand_index);
int size = key_list.size();
int i = 0;
while (i<size)
{
	try
	{
		Long actual_key = (Long)key_list.get(i);
		String string_key = actual_key.toString();
		Word word = wltd.getWord(string_key);

		out.println("<tr><td>");
		if (type.equals(Constants.READING))
		{
			out.println((i+1)+"	"+word.getText()+"</td>");
			out.println("<td>	__________________</td>");
			out.println("</tr>	(  )</tr></td>");
		} else if (type.equals(Constants.WRITING))
		{
			out.println((i+1)+"	"+word.getDefinition()+"</td>");
			out.println("<td>	__________________</td>");
			out.println("</tr>	(  )</tr></td>");
		}
	} catch (java.lang.NullPointerException npe)
	{
		
	}
	i++;
}
out.println("</table>");
%>
</body>
</html>
