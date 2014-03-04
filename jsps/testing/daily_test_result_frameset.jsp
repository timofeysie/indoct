<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<!-- . -->

<html>

<head>
	<title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title>
</head>

<body>

	<frameset cols="*,*">
		<frame src="/indoct/jsps/testing/daily_test_answer.jsp" name="left">
		<frame src="/indoct/jsps/testing/daily_test_quest_word_lookup.jsp" name="right">
	</frameset> 

</body>
</html>
