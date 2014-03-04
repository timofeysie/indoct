<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="org.catechis.dto.WordCategory" %>
<%@ page language="java" import="org.catechis.dto.SimpleWord" %>

<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<%
WordCategory word_cat = (WordCategory)session.getAttribute("category");
out.println("<p>"+word_cat.getName());
out.println("<table>");
Vector words = word_cat.getCategoryWords();
for (int i = 0; i < words.size(); i++)
{
	out.println("<tr>");
	SimpleWord word = (SimpleWord)words.get(i);
	out.println("<td>"+word.getText()+"</td>");
	out.println("<td>"+word.getDefinition()+"</td>");
	out.println("</tr>");
}
%>
</table>
<p>Exclusive Category
to do: list of words in this category.

<p><p><a href="/indoct/add_categories.do"><bean:message key="categories.add_categories_link"/></a></p>
<p><a href="/indoct/manage_categories.do"><bean:message key="categories.manage_categories_link"/></a></p>
<a href="/indoct/welcome.jsp"><bean:message key="add_word.return_to_main"/>

</body>
</html>