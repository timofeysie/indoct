<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="com.businessglue.util.EncodeString" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<html>
<head><title><%out.println(session.getAttribute("file_name"));%></title></head>
<body>
<%
Hashtable text_defs  = (Hashtable)session.getAttribute("words_defs");
Hashtable writing_levels  = (Hashtable)session.getAttribute("writing_levels_hash");
Hashtable reading_levels  = (Hashtable)session.getAttribute("reading_levels_hash");
out.println("<table><FONT SIZE=\"+2\">");
%>
	<tr>
		<td><bean:message key="stats.text"/></td>
		<td>&nbsp;</td>
		<td><bean:message key="stats.def"/></td>
		<td><bean:message key="stats.writing"/></td>
		<td><bean:message key="stats.reading"/></td>
	</tr>
<%
String r_level = new String();
String w_level = new String();
Enumeration keys = text_defs.keys(); 
Enumeration level_keys = writing_levels.keys();
while (keys.hasMoreElements())
{
		String key = (String)keys.nextElement();
		String value = (String)text_defs.get(key);
		String level_key = (String)level_keys.nextElement();
		try
		{
			w_level = (String)writing_levels.get(level_key);
			r_level = (String)reading_levels.get(level_key);
		} catch (java.lang.NullPointerException n)
		{
			w_level = "null";
			r_level = "null";
		}
		out.println("<TR>");
			out.println("<TD><B>");
			out.println("<a href=\"/indoct/stats_word.do?word="+value+"&text="+Transformer.getByteString(level_key));
			out.println("\">"+value+"</a>");
			out.println("</B></TD>");
			out.println("<TD>&nbsp;</TD>");
			out.println("<TD><B>");
			out.println(level_key);
			out.println("</TD></B>");
			
			out.println("<TD><B>");
			out.println(w_level);
			out.println("</B></TD>");
			out.println("<TD><B>");
			out.println(r_level);
			out.println("</B></TD>");
			
		out.println("</TR>");
	}
	out.println("</table></FONT> ");
%>

</body>
</html>
