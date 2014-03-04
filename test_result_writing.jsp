<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="com.businessglue.indoct.UserBean" %>
<%@ page language="java" import="com.businessglue.indoct.TestBean" %>
<%@ page language="java" import="com.businessglue.util.EncodeString" %>
<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<html>
<head><title><%out.println(session.getAttribute("file_name"));%></title></head>
<body>
<h3><bean:message key="result.welcome.writing"/></h3>
<p>
<%
TestBean test_bean = (TestBean)session.getAttribute("TEST_KEY");
String test_name = (String)test_bean.getFileName();
String test_file_name = (String)test_bean.getTestFileName();
Hashtable test_options  = test_bean.getOptions();
String test_type = (String)test_options.get("type");
String string_len = (String)test_options.get("length");
Object obj_score = session.getAttribute("test_score");
String string_score = obj_score.toString();
out.println("<p><b><bean:message key=\"test.score\"/></b>"+string_score+"%</b></p>");
out.println("<p>");
String str_len = (String)session.getAttribute("test_length");
int test_length = Integer.parseInt(str_len);
int test_i = 0;
out.println("<table>");
	Hashtable test_file = (Hashtable)session.getAttribute("words_defs");
	Hashtable word_grades = (Hashtable)session.getAttribute("word_grades");
	Hashtable text_answers = (Hashtable)session.getAttribute("text_answers");
	Enumeration keys = test_file.keys(); 
	int index = 0;
	while (keys.hasMoreElements())
	{
		//String original = new String((String)test_fields.elementAt(test_i)); 
		String key = (String)keys.nextElement();
		String value = (String)test_file.get(key);
		String answer = (String)text_answers.get(key);
		String word_grade = (String)word_grades.get(key);
		out.println("<TR>");
			out.println("<TD><B><font color=\"black\">");
			out.println(value);
			out.println("</B></TD>");
			out.println("<TD>");
			out.println(key);
			out.println("</TD>");
			out.println("<TD></font>");
			if (key.equals(answer))
			{
				out.println("<font color=\"green\">");
				out.println(answer);
				out.println("</font>");	
			} else
			{
				out.println("<font color=\"red\">");
				out.println(answer);
				out.println("</font>");
			}
			out.println("</TD>");
			out.println("<TD>");
			out.println(word_grade);
			out.println("</TD>");
			out.println("<TD><a href=\"/indoct/change_test.do?index="
				+index+"\">Change</a></TD>");
		out.println("</TR>");
		test_i++; index++;
		if (test_i>=test_length)
		{break;}
	}
out.println("</table>");
out.println("<p></p>");
out.println("<p><b>");
out.println("<p><b>");
String name = (String)session.getAttribute("test_name");
out.println("<a href=\"/indoct/file.do?filename="+name+"\">");
%>
<bean:message key="test_result.return_to_tests_page"/>
<%
out.println("</a>");
out.println("<p></p>");
%><bean:message key="test.grades"/><%
out.println("</b></p>");
out.println("<p></p>");
out.println("<table>");
	Vector grades  = (Vector)session.getAttribute("test_grades");
	int num_of_grades = grades.size();
	for(int i = 0; i<num_of_grades; i++) 
	{
		out.println("<TR><TD>");
		String grade = (String)grades.get(i);
		out.println(grade);
		out.println("</TD></TR>");
	}
	out.println("/<table>");
	/*
	out.println("<p><a href=\"/indoct/run_test.do?filename=");
	String name = (String)session.getAttribute("test_name");
	byte[] bytes = name.getBytes();
	String encoded_name = new String(bytes, "ms949");
	out.println(encoded_name+"\">"+%><bean:message key="test.re-run"/><%+"</a>");
	*/
%>
<a href="/indoct/stats.do">Statistics</a>
</body>
</html>
