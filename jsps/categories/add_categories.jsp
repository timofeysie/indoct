<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="org.catechis.dto.WordCategory" %>
<%@ page language="java" import="org.catechis.lists.Sortable" %>
<%@ page language="java" import="org.catechis.lists.Sarray" %>

<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<%
WordCategory main_word_cat = (WordCategory)session.getAttribute("category");
String cat = main_word_cat.getName(); 
out.println("<p>"+cat);
%>
<p>Exclusive Category
<form action="merge_categories.do" method="post">
<TABLE>
  <TR>
    <TH><b><bean:message key="stats_word.date_of_entry"/></b></TH>
    <TH><b><bean:message key="categories.categories"/></b></TH>
    <TH><b><bean:message key="history.words"/></b></TH>
    <TH><b><bean:message key="label.select"/></b></TH>
  </TR>
  
<%
Sarray word_catsarray = (Sarray)session.getAttribute("categories");
        for (int i = 0; i < word_catsarray.size(); i++)
        {
            Sortable s = (Sortable)word_catsarray.elementAt(i);
            String object_key = (String)s.getKey();
            WordCategory word_cat = (WordCategory)s.getObject();
            if (word_cat.getName().equals(cat))
            {
				// dont show the cat that the user is adding cats to
			} else
			{
	            out.println("<TR><TH>"+Transformer.getKoreanDateFromMilliseconds(Long.toString(word_cat.getCreationTime()))
+"</TH><TH><a href=\"/indoct/merge_categories.do?filename="+word_cat.getName()+"\">"+word_cat.getName()+"</a>"
		+"</TH><TH>"+word_cat.getTotalWordCount()+"</TH>");
		out.println("<td> <input type=\"checkbox\" name=\""+word_cat.getId()+"\" value=\"merge\"/></td></TR>");
			}	
        }
%>
</TABLE>
<input type="submit" value="Submit" />
<p>
<a href="/indoct/welcome.jsp"><bean:message key="add_word.return_to_main"/>

</body>
</html>