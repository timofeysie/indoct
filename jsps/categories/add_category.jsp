<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="org.catechis.dto.Word" %>
<%@ page language="java" import="org.catechis.EncodeString" %>

<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<html:form action="add_category.do">
<table>
	<tr>
		<td>
			<bean:message key="add_category.message"/>:
		</td>
		<td>
			<html:text property="category" />
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
