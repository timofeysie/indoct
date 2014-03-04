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
test_text = wtr.getText();
test_def  = wtr.getDefinition();
%>
	<table>
			
		<tr>
			<td><bean:message key="daily_test_result.word_lookup"/></td>
		</tr>
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
%>
	</table>
	
	<table>	
		<tr>
			<td>log</td>
		</tr>
<%
Vector log = ww.getLog();
int j = 0;
int jsize = log.size();
while (j<jsize)
{
	out.println("<tr><td>"+j+" "+(String)log.get(j)+"</td></tr>");
	j++;
}
%>
	</table>
	

</body>
</html>
