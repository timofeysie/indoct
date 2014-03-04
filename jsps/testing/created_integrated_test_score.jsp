<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="org.catechis.dto.Word" %>
<%@ page language="java" import="org.catechis.dto.Test" %>
<%@ page language="java" import="org.catechis.constants.Constants" %>
<%@ page language="java" import="org.catechis.dto.SavedTest" %>
<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<html>
<head><title><%out.println((String)session.getAttribute("user_name"));%></title></head>
<body>
<%
// we have to go through all the test entries to get the title?  Isn't there are better way?"
Vector saved_tests = (Vector)session.getAttribute("saved_tests");
String test_id = (String)session.getAttribute("test_id");
int ssize = saved_tests.size();
int si = 0;
while (si<ssize)
{
	try
	{
		SavedTest saved_test = (SavedTest)saved_tests.get(si);
	    String this_test_id = saved_test.getTestId();
	    if (this_test_id.equals(test_id))
	    {
		    String test_date = saved_test.getTestDate();
		    String test_name = saved_test.getTestName();
			out.println("<h2>"+test_name+"</h2>"); 
			out.println("<td>"+Transformer.getDateFromMilliseconds(test_date)+"</td>");
			break;
		}
	} catch (java.lang.NullPointerException npe)
	{
		
	}
	si++;
}

%>
<form action="created_integrated_test_result_score.do" method="post">
<%
out.println("<table>");
Hashtable test_words = (Hashtable)session.getAttribute("test_words"); 
//Enumeration e = test_words.keys();
//while(e.hasMoreElements())
int i = 0;
int size = test_words.size();
while (i<size)
{
	//String key = (String)e.nextElement();
	String key = i+"";
	Word word = (Word)test_words.get(key);
	Test [] tests = word.getTests();
	Test test = tests[0];
	String type = test.getType();
	out.println("<tr><td>");
	if (type.equals(Constants.READING))
	{
		out.println("<td>"+(Integer.parseInt(key)+1)+"	"+word.getText()+"</td>");
		out.println("<td>  </td>");
		out.println("<td>"+word.getDefinition()+"</td>");
		out.println("<td>  </td>");
		out.println("<td> <input type=\"checkbox\" name=\""+word.getId()+"\" value=\"pass\"/></td>");
	} else if (type.equals(Constants.WRITING))
	{
		out.println("<td>"+(Integer.parseInt(key)+1)+" "+word.getDefinition()+"</td>");
		out.println("<td>  </td>");
		out.println("<td>"+"	"+word.getText()+"</td>");
		out.println("<td>  </td>");
		out.println("<td> <input type=\"checkbox\" name=\""+word.getId()+"\" value=\"pass\"/></td>");
	}
	out.println("</tr>");
	i++;
}
out.println("</td>");
out.println("</table>");

%>
<input type="submit" value="Submit" />
</form>
</body>
</html>
