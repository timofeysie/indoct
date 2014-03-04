<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="com.businessglue.indoct.UserBean" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="org.catechis.Domartin" %>
<%@ page language="java" import="org.catechis.dto.AllTestStats" %>
<%@ page language="java" import="org.catechis.dto.AllWordStats" %>
<%@ page language="java" import="org.catechis.dto.TestStats" %>
<%@ page language="java" import="org.catechis.dto.WordStats" %>
<%@ page language="java" import="org.catechis.dto.RatesOfForgetting" %>
<%@ page language="java" import="org.catechis.file.FileCategories" %>
<%@ page language="java" import="java.util.Date" %>
<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="java.text.DecimalFormat" %>
<%@ page language="java" import="java.text.ParsePosition" %>
<%@ page language="java" import="java.text.SimpleDateFormat" %>
<%@ page language="java" import="java.util.List" %>
<%@ page language="java" import="java.util.ArrayList" %>
<%@ page language="java" import="java.util.Hashtable" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>


<TABLE>
  <TR>
    <TH><b><bean:message key="stats.level"/></b></TH>
    <TH><b><bean:message key="stats.rate_of_forgetting"/></b></TH>
  </TR>
<%
AllWordStats all_word_stats = (AllWordStats)session.getAttribute("all_word_stats");
RatesOfForgetting rof = all_word_stats.getRatesOfForgetting();
		Vector r_rof = rof.getReadingRateOfForgetting();
		Vector w_rof = rof.getWritingRateOfForgetting();
		int total = r_rof.size();
		int i = 0;
		out.println("<TR>");
		out.println("<TD></TD>");
		out.println("<TD><CENTER>");
			%><bean:message key="app.reading"/><%
		out.println("</CENTER></TD>");
		out.println("</TR>");
		while (i<total)
		{
			out.println("<TR>");
			out.println("<TD>"+i+"</TD>");
			out.println("<TD>"+Domartin.getElapsedTime((String)r_rof.get(i))+"</TD>");
			out.println("</TR>");
			i++;
		}
		total = w_rof.size();
		i = 0;
		out.println("<TR>");
		out.println("<TD></TD>");
		out.println("<TD><CENTER>");
			%><bean:message key="stats.rate_of_forgetting"/><%
		out.println("</CENTER></TD>");
		out.println("</TR>");
		while (i<total)
		{
			out.println("<TR>");
			out.println("<TD>"+i+"</TD>");
			out.println("<TD>"+Domartin.getElapsedTime((String)w_rof.get(i))+"</TD>");
			out.println("</TR>");
			i++;
		}

%>
</TABLE>

<p>
<a href="/indoct/welcome.jsp"><bean:message key="add_word.return_to_main"/>

</body>
</html>
