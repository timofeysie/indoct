<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!--jsp:useBean id="file" class="com.businessglue.indoct.TestBean" scope="session"/-->
<%@ page language="java" import="com.businessglue.util.EncodeString" %>
<%@ page language="java" import="org.catechis.dto.WordTestResult" %>
<%@ page language="java" import="org.catechis.dto.AllWordsTest" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<%@ page language="java" import="org.catechis.dto.Word" %>
<%@ page language="java" import="org.catechis.juksong.WebWord" %>
<!-- . -->
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>

<table>
	<tr>
		<td>
		
<%
// options for this page
Hashtable jsp_options = (Hashtable)session.getAttribute("jsp_options");

// test results
// questsion - correct answer - users answer - change_test.do link
// reading
// test - def - users answer - new level - change_test.do link
// questsion - correct answer - users answer - change_test.do link
AllWordsTest awt = (AllWordsTest)session.getAttribute("awt_test_word");
WordTestResult wtr = (WordTestResult)session.getAttribute("wtr");
String test_text   = new String();
String test_def    = new String();
String test_result = new String();
String index 	   = new String("-1");
String answer      = wtr.getAnswer();
// if theres no answer, give us something to turn red
if (answer.equals(""))
{
	answer = "???";
	}
if (awt.getTestType().equals("reading"))
{
	test_text = wtr.getText();
	test_def  = wtr.getDefinition();
} else
{
	test_text = wtr.getDefinition();
	test_def  = wtr.getText();
}
out.println("<font color=\"black\">");
	out.println("<table>");
		out.println("<TR>");
		// index of thw words place in the daily test list
		out.println("<TD>");
		out.println((String)session.getAttribute("daily_test_index"));
		out.println("</TD>");
		// question
		out.println("<TD>");
		out.println(test_text);
		out.println("</TD>");
		// correct answer
		out.println("<TD>");
		out.println(test_def);
		out.println("</TD>");
		out.println("</TR>");
		
		// old word level
		out.println("<TR>");
		out.println("<TD>");
		%><bean:message key="daily_test_result.old_level"/><%
		out.println("</TD>");
		out.println("<TD>");
		if (awt.getTestType().equals("reading"))
		{
			%><bean:message key="stats.reading"/><%
		} else
		{
			%><bean:message key="stats.writing"/><%
		}
		out.println(" "+wtr.getOriginalLevel());
		out.println("</TD>");
		
		// user answer
		out.println("<TD>");
		if (wtr.getGrade().equals("pass"))
		{
			out.println("<font color=\"green\">");
		} else
		{
			out.println("<font color=\"red\">");
		}
		out.println(answer);
		out.println("</font>");
		out.println("</TD>");
		out.println("<font color=\"black\">");
		out.println("</TR>");
		
		// new word level
		out.println("<TR>");
		out.println("<TD>");
		%><bean:message key="daily_test_result.new_level"/><%
		out.println("</TD>");
		out.println("<TD>");
		if (awt.getTestType().equals("reading"))
		{
			%><bean:message key="stats.reading"/><%
		} else
		{
			%><bean:message key="stats.writing"/><%
		}
		out.println(" "+wtr.getLevel());
		out.println("</TD>");
		out.println("</TR>");
		// test file
		out.println("<TR>");
		out.println("<TD>");
		%><bean:message key="daily_test_result.category"/><%
		out.println("</TD>");
		out.println("<TD>");
		out.println(awt.getCategory());
		out.println("</TD>");
		out.println("</TR>");
	out.println("</table>");
%>
<p>
<a href="/indoct/daily_test.do"><bean:message key="welcome.daily_test"/></a>
<p>
<a href="/indoct/change_update_score.do"><bean:message key="daily_test_result.change_test"/></a>
<p>
<a href="/indoct/edit_text_or_def_setup.do?type=text"><bean:message key="daily_test_result.edit_text"/></a>&nbsp;&nbsp;
<a href="/indoct/edit_text_or_def_setup.do?type=def"><bean:message key="daily_test_result.edit_def"/></a>
<p>
<a href="/indoct/retire_word.do"><bean:message key="daily_test_result.retire_word_link"/></a>
<p>
<a href="/indoct/jsps/testing/daily_test_quest_word_lookup.jsp"><bean:message key="daily_test_result.question_word_lookup"/></a>
<p>
<a href="/indoct/welcome.jsp"><bean:message key="add_word.return_to_main"/>

<p>
<%
// contains missed words
out.println("<table>");
		
Hashtable r_words_defs = (Hashtable)session.getAttribute("r_words_defs");
Hashtable w_words_defs = (Hashtable)session.getAttribute("w_words_defs");
if (r_words_defs.size()>0||w_words_defs.size()>0)
{
	// missed words marker
	out.println("<tr><td>");
	%>
	<p><b><bean:message key="weekly_list.words_defs"/></b>
	<%
}
out.println("<td/>");
out.println("<td></td></tr>");
for (Enumeration r_e = r_words_defs.keys() ; r_e.hasMoreElements() ;)
	    {
		    String r_key = (String)r_e.nextElement();
		    String r_val = (String)r_words_defs.get(r_key);
		    out.println("<tr><td>");
		    out.println("<p>"+r_key);
		    out.println("</td>");
		    out.println("<td>");
		    out.println("<p>"+r_val);
		    out.println("</td></tr>");
	    }
for (Enumeration e = w_words_defs.keys() ; e.hasMoreElements() ;) 
	    {
		    String key = (String)e.nextElement();
		    String val = (String)w_words_defs.get(key);
		    out.println("<tr><td>");
		    out.println("<p>"+val);
		    out.println("</td>");
		    out.println("<td>");
		    out.println("<p>"+key);
		    out.println("</td></tr>");
	    }
out.println("</table>");

if (awt.getTestType().equals("reading"))
{
%>
<p><a href="/indoct/clear_missed_words.do?type=reading"><bean:message key="options.clear_reading_missed_words"/></a></p>
<%
} else if (awt.getTestType().equals("writing"))
{
%>
<p><a href="/indoct/clear_missed_words.do?type=writing"><bean:message key="options.clear_writing_missed_words"/></a></p>
<%
}

// options for word lookup.
String word_lookup_for_pass = (String)jsp_options.get("word_lookup_for_pass");
String word_lookup_for_fail = (String)jsp_options.get("word_lookup_for_fail");
if ((word_lookup_for_pass.equals("true") && wtr.getGrade().equals("pass"))||(word_lookup_for_fail.equals("true")&&wtr.getGrade().equals("fail")))
{
	
%>

		</td>
		<td>
		<table>
			<tr><bean:message key="daily_test_result.word_lookup"/></tr>
<%
WebWord ww = new WebWord();
Vector web_words = ww.parseImpactDictionary(test_text, "euc-kr");
int i = 0;
int size = web_words.size();
while (i<size)
{
	out.println("<tr><td>"+i+" "+(String)web_words.get(i)+"</td></tr>");
	i++;
}

} // end word lookup for tests
%>
		</td>
		</table>
		</td>
	</tr>
</table>
</body>
</html>
