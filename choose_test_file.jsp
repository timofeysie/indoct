<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="user" class="com.businessglue.indoct.UserBean" scope="session"/>
<%@ page language="java" import="com.businessglue.indoct.UserBean" %>
<%@ page language="java" import="java.util.Vector" %>

<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<%
UserBean user_bean = (UserBean)session.getAttribute("USER_KEY");
Vector files = user_bean.getFileList();
int num_of_files = files.size();
%>
<TABLE>
  <TR>
    <TH><p><b><bean:message key="test.run"/></b></p></TH>
  </TR>
<%
for(int i = 0; i<num_of_files; i++) 
{
	out.println("<TR><TD><a href=\"/indoct/run_test.do?filename=");
	String name = (String)files.get(i);
	byte[] bytes = name.getBytes();
	String file = new String(bytes, "ms949");
	out.println(file+"\">"+file+"</a>");
	out.println("</TD></TR>");
}
%>
</TABLE>
</body>
</html>
