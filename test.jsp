<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="com.businessglue.indoct.TestBean" %>
<%@ page language="java" import="com.businessglue.indoct.UserBean" %>
<%@ page language="java" import="com.businessglue.util.EncodeString" %>
<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>

<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<%
TestBean test_bean = (TestBean)session.getAttribute("TEST_KEY");
String file_name = (String)test_bean.getFileName();
Hashtable options  = test_bean.getOptions();
out.println("<p><b>"+file_name+"</b>");
%>
<table>
<%
	Enumeration keys = options.keys(); 
	while (keys.hasMoreElements())
	{
		String key = (String)keys.nextElement();
		String value = (String)options.get(key);
		out.println("<TR>");
				out.println("<TD><B>");
				if (key.equals("type"))
				{%><bean:message key="test.type"/><%}
				if (key.equals("length"))
				{%><bean:message key="test.length"/><%}
				if (key.equals("description"))
				{%><bean:message key="test.description"/><%}
				if (key.equals("index"))
				{%><bean:message key="test.index"/><%}
				if (key.equals("levels"))
				{%><bean:message key="test.levels"/><%}
				if (key.equals("shuffle"))
				{%><bean:message key="test.shuffle"/><%}
				out.println("</B></TD>");
				out.println("<TD>");
				out.println(value);
				out.println("</TD>");
		out.println("</TR>");
	}
%>
</table>
<%
// UserBean user_bean = (UserBean)session.getAttribute("USER_KEY");
Vector word_categories = (Vector)session.getAttribute("word_categories");
int num_of_files = word_categories.size();
%>
<TABLE>
  <TR>
    <TH><p><b><bean:message key="test.run"/></b></p></TH>
  </TR>
<%
for(int i = 0; i<num_of_files; i++) 
{
	out.println("<TR><TD><a href=\"/indoct/run_test.do?filename=");
	String name = (String)word_categories.get(i);
	byte[] bytes = name.getBytes();
	String encoded_name = new String(bytes, "ms949");
	out.println(encoded_name+"\">"+encoded_name+"</a>");
	out.println("</TD></TR>");
}
%>
</TABLE>

<p>
<%
out.println("<a href=\"/indoct/welcome.jsp\">");
%><bean:message key="add_word.return_to_main"/><%
out.println("</a>");
%>

</body>
</html>
