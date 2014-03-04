<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="com.businessglue.indoct.UserBean" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="org.catechis.dto.AllTestStats" %>
<%@ page language="java" import="org.catechis.dto.AllWordStats" %>
<%@ page language="java" import="org.catechis.dto.TestStats" %>
<%@ page language="java" import="org.catechis.dto.WordStats" %>
<%@ page language="java" import="java.util.List" %>

<%@ page language="java" import="java.util.Date" %>
<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="java.text.DecimalFormat" %>
<%@ page language="java" import="java.text.ParsePosition" %>
<%@ page language="java" import="java.text.SimpleDateFormat" %>

<%@ page language="java" import="org.catechis.dto.WordFilter" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
Hey
<%
WordFilter filter = (WordFilter)session.getAttribute("filter");
out.println("<p>start index    - "+filter.getStartIndex());
out.println("<p>min, max range - "+filter.getMinMaxRange());
out.println("<p>type           - "+filter.getType());
out.println("<p>cateogry       - "+filter.getCategory());
%>
<form method="post" action="/indoct/filter_test_run.do">
	<input type"text" name="exclude_time" size="10" maxlength="10"/>
	<html:submit/> 
</form>
<p>
<p><a href="/indoct/all_words_test.do">Statistics History</a>
</body>
</html>
