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
	String [] values = new String[] {"yes", "no"};

	ChangeTestForm change_test_form = (ChangeTestForm)session.getAttribute("change_test_form");
	change_test_form.setReverseGrade("no");
	change_test_form.setAddAnswer("no");
        FormBean myForm = new FormBean(ChangeTestForm.class,
                new WrapDynaBean(change_test_form));

	com.cascadetg.form.converter.Converter myConverter = 
		new ObjectPropertyConverter(ChangeTestForm.class, "addAnswer");
		
	myForm.getFormElement("reverseGrade").setWidgetType(SelectWidget.MULTIPLESELECT);
        myForm.getFormElement("reverseGrade").setConverter(myConverter);
        myForm.getFormElement("reverseGrade").setOptionLabels(values);
        myForm.getFormElement("reverseGrade").setOptionValues(values);
	
	myForm.getFormElement(SelectWidget.MULTIPLESELECT);
	myForm.getFormElement("addAnswer").setWidgetType(SelectWidget.MULTIPLESELECT);
	myForm.getFormElement("addAnswer").setConverter(myConverter);
        myForm.getFormElement("addAnswer").setOptionLabels(values);
        myForm.getFormElement("addAnswer").setOptionValues(values);
		
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
<form name="form1" method="post" action="change_update.do">
<table cellpadding="3" cellspacing="3" border="0" width="100%">
<%=new FormRenderer(myForm).toHTML()%>
</table>
<input name="Submit" type="submit" class="foo" value="Submit"> 
<a href="change_test.jsp"><bean:message key="change_test.clear"/></a>
</form>
</body>
</html>
