<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="org.catechis.FileStorage" %>
<%@ page language="java" import="org.catechis.I18NWebUtility" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%
				Storage store = new FileStorage(current_dir, context);
				Hashtable user_opts = store.getUserOptions(user_id, current_dir);
				I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
%>


<head><title><bean:message key="app.name"/> <bean:message key="login.title"/></title></head>
<body>

<jsp:include page="/jsps/includes/start_table.jsp" />

<h3><bean:message key="app.name"/></h3>
<h2>1.1 'Jenners Moudin'</h2>
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
<p><a href="add_new_user_setup.do"><bean:message key="login.new_user"/></a>
<p>
<jsp:include page="/jsps/includes/index_table_end.jsp" />

<p>
</body>
</html>
