<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head><title><bean:message key="app.name"/> <bean:message key="login.title"/></title></head>
<body>
<font color="red"><bean:message key="app.under_construction"/></font>
<jsp:include page="/jsps/includes/start_table.jsp" />

<h3><bean:message key="login.welcome"/></h3>
<html:errors/>
<html:form action="user_login.do">
<table>
	<tr>
		<td>
			<bean:message key="login.user"/>:
		</td>
		<td>
			<html:text property="userName" />
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="login.pass"/>: 
		</td>
		<td>
			<html:password property="password" />
		</td>
	</tr>
</table>
<p>
<html:submit/>
</html:form>
<p><a href="add_new_user_setup.do"><bean:message key="login.new_user"/></a>

<jsp:include page="/jsps/includes/index_table_end.jsp" />

<jsp:include page="logos.jsp" />
<p>
</body>
</html>
