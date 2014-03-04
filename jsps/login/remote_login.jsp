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

<%
String user_id = (String)session.getAttribute("user_id");
out.println("&lt;user&gt;<br>");
out.println("&lt;user_id&gt;"+user_id+"&lt;/user_id&gt;<br>");
Hashtable last_record = (Hashtable)session.getAttribute("last_record");
DecimalFormat formatter = new DecimalFormat("###.##");
out.println("&lt;number_of_words&gt;"+last_record.get("number_of_words")+"&lt;/number_of_words&gt;<br>"); 

out.println("&lt;words_at_reading_level_0&gt;"+last_record.get("words_at_reading_level_0")+"&lt;words_at_reading_level_0&gt;<br>");
out.println("&lt;words_at_writing_level_0&gt;"+last_record.get("words_at_writing_level_0")+"&lt;words_at_writing_level_0&gt;<br>"); 

out.println("&lt;words_at_reading_level_1&gt;"+last_record.get("words_at_reading_level_1")+"&lt;words_at_reading_level_1&gt;<br>");
out.println("&lt;words_at_writing_level_1&gt;"+last_record.get("words_at_writing_level_1")+"&lt;words_at_writing_level_1&gt;<br>"); 

out.println("&lt;words_at_reading_level_2&gt;"+last_record.get("words_at_reading_level_2")+"&lt;words_at_reading_level_2&gt;<br>"); 
out.println("&lt;words_at_writing_level_2&gt;"+last_record.get("words_at_writing_level_2")+"&lt;words_at_writing_level_2&gt;<br>");

out.println("&lt;words_at_reading_level_3&gt;"+last_record.get("words_at_reading_level_3")+"&lt;words_at_reading_level_3&gt;<br>"); 
out.println("&lt;words_at_writing_level_3&gt;"+last_record.get("words_at_writing_level_3")+"&lt;words_at_writing_level_3&gt;<br>");
try
{
	out.println("&lt;reading_average&gt;");
	out.println(formatter.format(Double.parseDouble((String)last_record.get("reading_average")))); 
	out.println("&lt;/reading_average&gt;<br>");
} catch (java.lang.NullPointerException npe)
{
	out.println("n/a");
}
try
{
	out.println("&lt;writing_average&gt;");
	out.println(formatter.format(Double.parseDouble((String)last_record.get("writing_average")))); 			
	out.println("&lt;/writing_average&gt;<br>");		 
} catch (java.lang.NullPointerException npe)
{
	out.println("n/a");
}

try
{
	out.println("&lt;number_of_retired_words&gt;");
	out.println((String)last_record.get("number_of_retired_words")); 		
	out.println("&lt;/number_of_retired_words&gt;<br>");			 
} catch (java.lang.NullPointerException npe)
{
	out.println("n/a");
}
out.println("&lt;user&gt;<br>");
%>
</body>
</html>
