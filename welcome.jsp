<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.text.DecimalFormat" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>

<jsp:include page="/jsps/includes/start_table.jsp" />
<table>
	<td>
	<!-- inner table for main menu -->
<table>
	<tr>
	    <td>
<p><a href="/indoct/integrated_test.do"><bean:message key="user_login.integrated_test"/></a> 
	    </td>
	    <td>
	    	<IMG SRC="./images/school/pencil-icon.jpg" ALT="" WIDTH=48 HEIGHT=48/>
	    </td>
	</tr>
	<tr>
	    <td>
	    	<p><a href="/indoct/weekly_list.do"><bean:message key="welcome.weekly_list"/></a>
		</td>
	    <td>
	    	<IMG SRC="./images/school/book-icon.jpg" ALT="" WIDTH=48 HEIGHT=48/>
	    </td>
	</tr>
	<tr>
	    <td>
	    	<p><a href="/indoct/categories_simple.do"><bean:message key="welcome.categories"/></a>
		</td>
	    <td>
	    	<IMG SRC="./images/school/apple-icon.jpg" ALT="" WIDTH=48 HEIGHT=48/>
	    </td>
	</tr>
	<tr>
	    <td>
	    	<p><a href="/indoct/tests.do"><bean:message key="welcome.tests"/></a>
		</td>
	    <td>
	    	<IMG SRC="./images/school/skull-icon.jpg" ALT="" WIDTH=48 HEIGHT=48/>
	    </td>
	</tr>
	<tr>
	    <td>
	    	<p><a href="/indoct/word_stats.do"><bean:message key="welcome.word_stats"/></a>
		</td>
	    <td>
	    	<IMG SRC="./images/school/x-icon.jpg" ALT="" WIDTH=48 HEIGHT=48/>
	    </td>
	</tr>
	<tr>
	    <td>
	    	<p>
		<p>
		<p><a href="/indoct/history.do"><bean:message key="welcome.stats_history"/></a>
		</td>
	    <td>
	    	<IMG SRC="./images/school/box-icon.jpg" ALT="" WIDTH=48 HEIGHT=48/>
	    </td>
	</tr>
	<tr>
	    <td>
	    	<p><a href="/indoct/user_options.do"><bean:message key="welcome.user_options"/></a></p>
		</td>
	    <td>
	    	<IMG SRC="./images/Gear-icon.jpg" ALT="" WIDTH=48 HEIGHT=48/>
	    </td>
	</tr>
</table>
<!-- end main menu table -->
	</td>
	
	<td><!-- buffer -->
	</td>
	<td><!-- buffer -->
	</td>
	
	<td>
<!-- begin statistics table -->
<%
Hashtable last_record = (Hashtable)session.getAttribute("last_record");
DecimalFormat formatter = new DecimalFormat("###.##");
%>
<table>
	<tr>
		<td align=center><bean:message key="welcome.stats.header"/></td>
	</tr>
	<tr>
		<td><a href="/indoct/list.do?filename=all"><bean:message key="welcome.stats.number_of_words"/></a></td>
		<td><% out.println(last_record.get("number_of_words")); %></td>
	</tr>
	
	<table bgcolor="#FFFFCC">
	<tr>
		<td><bean:message key="stats.level"/></td>
		<td><bean:message key="app.reading"/></td>
		<td><bean:message key="app.writing"/></td>
	</tr>
	
	<tr>
		<!-- number of words at each level table -->
		<tr>
			<td>0</td>
			<td><% out.println(last_record.get("words_at_reading_level_0")); %></td>
			<td><% out.println(last_record.get("words_at_writing_level_0")); %></td>
		</tr>
		<tr>
			<td>1</td>
			<td><% out.println(last_record.get("words_at_reading_level_1")); %></td>
			<td><% out.println(last_record.get("words_at_writing_level_1")); %></td>
		</tr>
		<tr>
			<td>2</td>
			<td><% out.println(last_record.get("words_at_reading_level_2")); %></td>
			<td><% out.println(last_record.get("words_at_writing_level_2")); %></td>
		</tr>
		<tr>
			<td>3</td>
			<td><% out.println(last_record.get("words_at_reading_level_3")); %></td>
			<td><% out.println(last_record.get("words_at_writing_level_3")); %></td>
		</tr>
		<tr>
			<td><bean:message key="welcome.stats.average"/></td>
			<td><% 
				try
				{
					out.println(formatter.format(Double.parseDouble((String)last_record.get("reading_average")))); 
				} catch (java.lang.NullPointerException npe)
				{
					out.println("n/a");
				}
			%></td>
			<td><%  
				try
				{
					out.println(formatter.format(Double.parseDouble((String)last_record.get("writing_average")))); 					 
				} catch (java.lang.NullPointerException npe)
				{
					out.println("n/a");
				}
			%></td>
		</tr>
		<!-- end number of words at each level table -->
	</tr>
	<tr>
		<td><bean:message key="welcome.retired_words"/></td>
		<td>&nbsp;:&nbsp;
		<%
			try
				{
					out.println((String)last_record.get("number_of_retired_words")); 					 
				} catch (java.lang.NullPointerException npe)
				{
					out.println("n/a");
				}
		%>
		<td>
	</tr>
	<tr>
	<table>
		<td><tr><bean:message key="welcome.stats.date"/>&nbsp;:&nbsp;</tr>
		<tr><%  
				try
				{
					 out.println(Transformer.getDateFromMilliseconds((String)last_record.get("date"))); 					 
				} catch (java.lang.NullPointerException npe)
				{
					out.println("n/a");
				} catch (java.lang.NumberFormatException nfe)
				{
					out.println("now");
				}
		%></tr>
		</td>
		<td>
		<tr><a href="/indoct/recalculate_stats.do"><bean:message key="welcome.stats.recalculate"/></a></tr>
		</td>
	</table>
	
	</tr>
	</table>
</table>
<!-- end statistics table -->
	</td>

<!-- search for word -->

<html:form action="find_word.do">
<table>
	<tr>
		<td>
			<bean:message key="welcome.find_word"/>:
		</td>
		<td>
			<html:text property="category" />
		</td>
	</tr>
</table>
<p>
<html:submit/>
</html:form>

<!-- end sreach word -->

<table>


</table>



<jsp:include page="/jsps/includes/sponsor_end.jsp" />

</body>
</html>