<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!--jsp:useBean id="file" class="com.businessglue.indoct.TestBean" scope="session"/-->
<%@ page language="java" import="com.businessglue.util.EncodeString" %>
<%@ page language="java" import="org.catechis.dto.AllWordsTest" %>
<%@ page language="java" import="java.lang.Integer" %>
<!-- . -->
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<%
/*
String text;
String definition;
String test_type;
String level;
int daily_test_index;
long id;*/
AllWordsTest awt_test_word = (AllWordsTest)session.getAttribute("awt_test_word");
String grand_index = (String)session.getAttribute("grand_index");
String daily_test_index = (String)session.getAttribute("daily_test_index");
int dsi = Integer.parseInt(daily_test_index)+1;
daily_test_index = Integer.toString(dsi);
String test_type = awt_test_word.getTestType();
String question = new String();
String answer = new String();
if (test_type.equals("reading"))
{
	question = awt_test_word.getText();
	answer = awt_test_word.getDefinition();
} else
{
	question = awt_test_word.getDefinition();
	answer = awt_test_word.getText();
}
	out.println("<form METHOD=\"post\" ACTION=\"integrated_test_result.do\" autocomplete=\"off\">");
	out.println("<table>");
		out.println("<TR>");
		out.println(daily_test_index+" - "+grand_index);
		out.println("</TR>");
		out.println("<TR>");
		out.println("<B><h2><font size=\"20\" face=\"gothic\">");
		out.println(question);
		out.println("</h2></B></font>");
		out.println("<h2></TR>");
		
		// if the question answer is long, use a text area.
		try
		{
			int size = answer.length();
			if (size<20)
			{
				out.println("<TR><input TYPE=\"text\" NAME=\"answer\" SIZE=\"25\" MAXLENGTH=\"30\">");
			} else
			{
				//int rows = (question/25);
				int rows = 3;
				out.println("<TEXTAREA NAME=\"answer\" COLS=25 ROWS="+rows+"></TEXTAREA>");
			}
		} catch (java.lang.NullPointerException npe)
		{
			String category = (String)session.getAttribute("category"); // debugging
			out.println("You've got problems! "+category);
			//out.println("You've got problems! "+category);
		}
			


		out.println("</h2>");
		out.println("</TR>");
	out.println("</table>");
%>
	<html:submit/>
</form>
</body>
</html>
