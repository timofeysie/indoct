<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="com.businessglue.util.EncodeString" %>
<%@ page language="java" import="java.util.Date" %>
<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="org.catechis.Domartin" %>
<%@ page language="java" import="org.catechis.dto.Word" %>
<%@ page language="java" import="org.catechis.dto.Test" %>
<%@ page language="java" import="org.catechis.constants.Constants" %>
<html>
<head><title><%out.println(session.getAttribute("chosen_word"));%></title></head>
<body>
<%
Vector found_words = (Vector)session.getAttribute("found_words");
int index = 0;
int size = found_words.size();
while (index<size)
{
	Word word = (Word)found_words.get(index);
	String text = word.getText();
	String def = word.getDefinition();
	String word_id = Long.toString(word.getId());
	String category = word.getCategory();
	String reading_level = Integer.toString(word.getReadingLevel());
	String writing_level = Integer.toString(word.getReadingLevel());
	int number = (index+1);
	out.println("<p><b>"+number+"</b>");
	
	out.println("<table>");
	out.println("<tr><td>");
	out.println(text);
	out.println("</td><td>");
	out.println(def);
	out.println("</td></tr>");
	%>
	<tr><td><bean:message key="stats.reading"/></td><td>
	<% out.println(reading_level); %>
	</td><td>&nbsp;</td>
	<td><td><bean:message key="stats.writing"/></td><td>
	<% out.println(writing_level); %>
	</td></tr>
	<tr><td><bean:message key="daily_test_result.category"/>
	</td><td>&nbsp;</td>
	<% out.println(category); %>
	</td></tr>
	<tr><td><bean:message key="stats_word.date_of_entry"/></td><td>&nbsp;</td>
	<td><%out.println("<p>"+Transformer.getDateFromMilliseconds(word.getDateOfEntry()+""));%></td>
	</tr>
	</table>

	<p>
	<p>
	<a href="/indoct/edit_word_setup.do?type=text"><bean:message key="daily_test_result.edit_text"/></a>
	<p>
	<a href="/indoct/edit_word_setup.do?type=def"><bean:message key="daily_test_result.edit_def"/></a>
	<p>
	<%out.println("<a href=\"/indoct/delete_word_confirm.do?word_id="+word_id+"&word_category="+category+"\">");%>
	<bean:message key="file.delete_word"/>
	<%out.println("</a>");%>
	<p>
	<b><bean:message key="stats.word.test_results"/></b>
	<p> 
	<%
			// get tests and create a line to print and sort by type
			Vector word_w_tests = new Vector();
			Vector word_r_tests = new Vector();
			Test [] tests = word.getTests();
			int i = 0;
			int l = tests.length;
			while (i<l)
			{
				Test test = tests[i];
				String full_date = test.getDate();
				long long_date = Domartin.getMilliseconds(full_date);
				String short_date = Transformer.getDateFromMilliseconds(long_date+"");
				String grade = test.getGrade();
				String type = Domartin.getTestType(test.getName());
				String level = Domartin.getTestLevel(test.getName());
				String result = short_date+"\t"+grade+"\t"+level;
				if (type.equals(Constants.READING))
				{
					word_r_tests.add(result);
				} else if (type.equals(Constants.WRITING))
				{
					word_w_tests.add(result);
				}
				i++;
			}
	%>
	
	<b><bean:message key="stats.word.reading"/></b>
	<%
	int readingsize = word_r_tests.size();
	int k = 0;
	out.println("<table>");
	while (k<readingsize)
	{
			out.println("<TR>");
			out.println("<td>"+(String)word_r_tests.get(k)+"</td>");
			out.println("</TR>");
			k++;
	}
	out.println("</table>");
	%>
	
	<b><bean:message key="stats.word.writing"/></b>
	<%
	int writingsize = word_w_tests.size();
	int j = 0;
	out.println("<table>");
	while (j<writingsize)
	{
			out.println("<TR>");
			out.println("<td>"+(String)word_w_tests.get(j)+"</td>");
			out.println("</TR>");
			j++;
	}
	out.println("</table>");
	index++;
}
%>

<p>
<a href="/indoct/welcome.jsp"><bean:message key="add_word.return_to_main"/>

</body>
</html>
