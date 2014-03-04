<%@ page contentType="text/html; charset=euc-kr" language="java" 
import="com.cascadetg.form.*,
        com.cascadetg.form.renderer.*,
	com.cascadetg.form.test.*,
	com.cascadetg.form.util.*,
	com.cascadetg.form.converter.*,
	com.cascadetg.form.widget.*,
	org.catechis.dto.Word,
	org.apache.commons.beanutils.*" errorPage="" %>
<%
	Word word = new Word();
        FormBean myForm = new FormBean(Word.class,
                new WrapDynaBean(word));

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
<title>Edit</title>
<style type="text/css">
<!--
.foo {
	font-family: "Times New Roman", Times, serif;
}
-->
</style>
</head>

<body>
<p><b>This is the form displayed to the user:</b></p>
<form name="form1" method="post" action="edit.jsp">
<table cellpadding="3" cellspacing="3" border="0" width="100%">
<%=new FormRenderer(myForm).toHTML()%>
</table>
<input name="Submit" type="submit" class="foo" value="Submit"> 
<a href="edit.jsp">clear</a>
</form>
<hr><p><b>This is the object as rendered read-only:</b></p>
<table cellpadding="3" cellspacing="3" border="0" width="100%">
<%=new DisplayRenderer(myForm).toHTML()%>
</table>

<hr><p><b>These are the parameters sent to the system:</b></p>
<table bgcolor="#CCCCCC"><tr><td>Submitted Params:</td></tr>
<% java.util.Enumeration params = request.getParameterNames();
   while(params.hasMoreElements())
   {
   	String param = (String)params.nextElement();
	%>
<tr><td><%= param%></td><td><% for(int i = 0; i < request.getParameterValues(param).length;i++) { %><%= request.getParameterValues(param)[i] %> <%}%></td></tr>
<%	} %>
<tr><td><% if(request.getParameter("Submit") != null) { %>
  Ok to update? <%= okUpdate %><%} %></td></tr>
</table>
</body>
</html>
