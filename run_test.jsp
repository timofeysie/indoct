<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="file" class="com.businessglue.indoct.TestBean" scope="session"/>
<%@ page language="java" import="com.businessglue.indoct.UserBean" %>
<%@ page language="java" import="com.businessglue.indoct.TestBean" %>
<%@ page language="java" import="com.businessglue.util.EncodeString" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<!-- Currently the length of the test is hard coded at 10.  Getting it from the
test_options hashtable returns null. -->
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<%
TestBean test_bean = (TestBean)session.getAttribute("TEST_KEY");
String test_name = (String)test_bean.getFileName();
String test_file_name = (String)test_bean.getTestFileName();
Hashtable test_options  = test_bean.getOptions();
String test_type = (String)test_options.get("type");
// getting the length from the test_options bean didn't work,
// so to get things cooking, we put the length in the session scope.
//String string_len = (String)test_options.get("length");
String str_len = (String)session.getAttribute("test_length");
String str_ind = (String)session.getAttribute("test_index");
int test_length = Integer.parseInt(str_len);
int test_index = Integer.parseInt(str_ind);
int test_i = 0;
out.println("<form METHOD=\"post\" ACTION=\"test_result.do\">");
	out.println("<table>");
		Hashtable test_file = (Hashtable)session.getAttribute("words_defs");
		Enumeration keys = test_file.keys(); 
		while (keys.hasMoreElements())
		{
			String key = (String)keys.nextElement();
			String value = (String)test_file.get(key);
			out.println("<TR>");
				out.println("<TD><B>");
				out.println(value);
				out.println("</B></TD>");
				out.println("<TD>");
				out.println("<input TYPE=\"text\" NAME=\"field"+test_i+"\" SIZE=\"10\" MAXLENGTH=\"10\">");
				out.println("</TD>");
			out.println("</TR>");
			test_i++;
			if (test_i>=test_length)
			{break;}
		}
	out.println("</table>");
	// The following had no result
	//<!--BUTTON NAME=submit VALUE=submit><bean:message key="label.submit"/></BUTTON-->
%>
	<html:submit/>
</form>
<TABLE>
  <TR>
    <TD>
    	<p><b><bean:message key="test.test_file"/></a>
    </TD>
    <TD>
    	<%=test_name%>
    </TD>
    <TD>
    	<p><b><bean:message key="test.testing_file"/></a>
    </TD>
    <TD>
    	<%=test_file_name%>
    </TD>
  </TR>
</TABLE>
</body>
</html>
