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
Word deleted_word = (Word)session.getAttribute("deleted_word");
%>
<p><bean:message key="add_word.return_to_main"/>
<p>
<a href="/indoct/delete_word_undo"><bean:message key="delete_word_confirm.undo_delete"/>
<p>
<a href="/jsps/categories/found_words.jsp"><bean:message key="delete_word_confirm.return_to_found_words"/>
<p>
<a href="/indoct/welcome.jsp"><bean:message key="add_word.return_to_main"/>

</body>
</html>
