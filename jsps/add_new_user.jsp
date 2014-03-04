<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<html:form action="add_new_user.do">
<table>
	<tr>
		<td>
			<bean:message key="add_new_user.user_name"/>:
		</td>
		<td>
			<html:text property="userName" />
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="add_new_user.password"/>:
		</td>
		<td>
			<html:text property="password" />
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="add_new_user.e_mail"/>:
		</td>
		<td>
			<html:text property="email" />
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="add_new_user.invitation_code"/>:
		</td>
		<td>
			<html:text property="invitationCode" />
		</td>
	</tr>
</table>
<p>
<html:submit/>
</html:form>
<p>
<p>
</body>
</html>
