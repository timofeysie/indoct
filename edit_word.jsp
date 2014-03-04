<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!--jsp:useBean id="file" class="com.businessglue.indoct.TestBean" scope="session"/-->
<%@ page language="java" import="com.businessglue.util.EncodeString" %>
<%@ page language="java" import="org.catechis.dto.Word" %>
<!-- . -->
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<%
Word word = (Word)session.getAttribute("word");
String question = new String();
String answer = new String();
String edit_type = (String)request.getParameter("type");
if (edit_type.equals("def"))
{
	question = word.getText();
} else if (edit_type.equals("text"))
{
	question = word.getDefinition();
} else
{
	question = "We have a problem";
}
	out.println("<form METHOD=\"post\" ACTION=\"edit_word.do?type="+edit_type+"\" >");
	out.println("<table>");
		out.println("<TR>");
		out.println("<TD><B><h2>");
		out.println(question);
		out.println("</h2></B></TD>");
		out.println("<TD><h2>");
		
		// if the question answer is long, use a text area.
int size = question.length();
if (size<20)
{
	out.println("<input TYPE=\"text\" NAME=\"answer\" SIZE=\"25\" MAXLENGTH=\"30\">");
} else
{
	//int rows = (question/25);
	int rows = 3;
	out.println("<TEXTAREA NAME=\"answer\" COLS=25 ROWS="+rows+"></TEXTAREA>");
}

		out.println("</h2></TD>");
		out.println("</TR>");
	out.println("</table>");
%>
	<html:submit/>
</form>
</body>
</html>
