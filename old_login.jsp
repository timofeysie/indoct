<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head><title><bean:message key="app.name"/> <bean:message key="login.title"/></title></head>
<body>
<h3><bean:message key="login.welcome"/></h3>
<html:errors/>
<html:form action="login.do">
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
<p><a href="validation/registration.jsp"><bean:message key="login.new_user"/></a>
<jsp:include page="logos.jsp" />
<p>
</body>
</html>