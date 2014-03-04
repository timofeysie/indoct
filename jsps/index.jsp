<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head><title><bean:message key="app.name"/> <bean:message key="login.title"/></title></head>
<body>

<!-- jsp:include page="/jsps/includes/start_table.jsp" / -->

<h3>Hi Daeyeong!</h3>
<html:errors/>
<html:form action="login.do">
<table>
<tr>
<td>
<table>
	<tr>
		<td>
			<IMG SRC="./images/school/face-icon.jpg" ALT="" WIDTH=48 HEIGHT=48/>
		</td>
		<td>
			<html:text property="userName" />
		</td>
	</tr>
	<tr>
		<td>
			<IMG SRC="./images/school/asterix-icon.jpg" ALT="" WIDTH=48 HEIGHT=48/>
		</td>
		<td>
			<html:password property="password" />
		</td>
	</tr>
</table>
<td>
<IMG SRC="./images/glue.png" ALT="" WIDTH=128 HEIGHT=128/>
<td>
</td>
</table>
<p>
<html:submit/>
</html:form>
<p><a href="add_new_user_setup.do"><bean:message key="login.new_user"/></a>

<!-- jsp:include page="/includes/index_table_end.jsp" / -->
<p>
<!-- a href="http://www.freedomain.co.nr/" target="_blank" title="Free Domain Name"><img src="http://nmraoza.imdrv.net/soof62.gif" width="88" height="31" border="0" alt="Free Domain Name" /></a -->

</body>
</html>