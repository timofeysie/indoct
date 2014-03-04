<%@ page contentType="text/html; charset=euc-kr" language="java" 
import="com.cascadetg.form.*,
        com.cascadetg.form.renderer.*,
	com.cascadetg.form.test.*,
	com.cascadetg.form.util.*,
	com.cascadetg.form.converter.*,
	com.cascadetg.form.widget.*,
	org.catechis.dto.ChangeTestForm,
	org.catechis.dto.WordTestMemory,
	org.catechis.dto.WordTestResult,
	org.apache.commons.beanutils.*" errorPage="" %>
<%
	EditForm edit_form = (EditForm)session.getAttribute("edit_form");
        FormBean myForm = new FormBean(EditForm.class,
                new WrapDynaBean(edit_form));
		
	boolean okUpdate = false;
	if(request.getParameter("Submit") != null)
	{
		okUpdate = myForm.updateValue(request, response);
	}
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><bean:message key="change_test.title"/></title>
<style type="text/css">
<!--
.foo {
	font-family: "Times New Roman", Times, serif;
}
-->
</style>
</head>

<body>
<p><b><bean:message key="change_word.info"/></b></p>
<form name="form1" method="post" action="edit_word_confirm.do">
<table cellpadding="3" cellspacing="3" border="0" width="100%">
<%=new FormRenderer(myForm).toHTML()%>
</table>
<input name="Submit" type="submit" class="foo" value="Submit"> 
<a href="edit_singe_property.jsp"><bean:message key="change_test.clear"/></a>
</form>
</body>
</html>
