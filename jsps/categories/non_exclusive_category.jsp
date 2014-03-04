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
<p>Non Exclusive Category
<TABLE>
  <TR>
    <TH><b><bean:message key="categories.categories"/></b></TH>
  </TR>
  
<%
Sarray word_catsarray = (Sarray)session.getAttribute("categories");
        for (int i = 0; i < word_catsarray.size(); i++)
        {
            Sortable s = (Sortable)word_catsarray.elementAt(i);
            String object_key = (String)s.getKey();
            WordCategory word_cat = (WordCategory)s.getObject();
            out.println("<TR><TH>"+Transformer.getKoreanDateFromMilliseconds(Long.toString(word_cat.getCreationTime()))
+"</TH><TH><a href=\"/indoct/xml_file.do?filename="+word_cat.getName()+"\">"+word_cat.getName()+"</a>"
		+"</TH><TH>"+word_cat.getTotalWordCount()+"</TH></TR>");
        }
%>
</TABLE>

<p>
<a href="/indoct/welcome.jsp"><bean:message key="add_word.return_to_main"/>

</body>
</html>