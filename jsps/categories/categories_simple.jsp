<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="org.catechis.file.FileCategories" %>
<%@ page language="java" import="org.catechis.Domartin" %>
<%@ page language="java" import="java.util.List" %>
<%@ page language="java" import="java.util.ArrayList" %>
<%@ page language="java" import="java.util.Hashtable" %>

<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>

<p><b><bean:message key="categories.categories"/></b></p>

<%
Hashtable categories = (Hashtable)session.getAttribute("categories");
FileCategories cats = new FileCategories();
ArrayList categories_key_list = cats.getSortedKeys(categories);
int it = 0;
int size = categories_key_list.size();
while (it<size)
{
	String key = (String)categories_key_list.get(it);
	String name = (String)categories.get(key);
	String name_wo_ext = Domartin.getFileWithoutExtension(name);
	out.println("<p><a href=\"/indoct/xml_file.do?filename="+name+"\">"+name_wo_ext+"</a></p>");
	it++;
}
%>
<p></p>
<p><b><bean:message key="categories.options"/></b></p>
<p><a href="/indoct/manage_categories.do"><bean:message key="categories.manage_categories_link"/></a></p>
<p><a href="/indoct/missed_words.do"><bean:message key="categories.missed_words_link"/></a></p>
<p><a href="/indoct/categories.do"><bean:message key="categories.statistics"/></a></p>
<p><a href="/indoct/jsps/categories/add_category.jsp"><bean:message key="categories.add_category"/></a></p>
<p><bean:message key="categories.search_for_word"/></p>
<p></p>
<a href="/indoct/welcome.jsp"><bean:message key="add_word.return_to_main"/></a>

</body>
</html>
