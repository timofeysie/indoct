<%@ page contentType="text/html;charset=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="app.name"/></title>
</head>
<body bgcolor="white">
<p><bean:message key="options.edit_exclude_level_instrucations"/>

<html:form action="update_options.do">
<table border="0" width="100%">

<!--
These are in the <user_id>/<user_id>.options file:
exclude_level0_test
exclude_level1_test
exclude_level2_test
exclude_level3_test
--!>
		
<tr>
    <th align="left">
      <bean:message key="options.edit_exclude_level_test"/>&nbsp;0
    </th>
    <td align="left">
      <html:text property="excludeLevel0Test" size="30" maxlength="30"/>
    </td>
</tr>
<tr>
    <th align="left">
      <bean:message key="options.edit_exclude_level_test"/>&nbsp;1
    </th>
    <td align="left">
      <html:text property="excludeLevel1Test" size="30" maxlength="30"/>
    </td>
</tr>
<tr>
    <th align="left">
      <bean:message key="options.edit_exclude_level_test"/>&nbsp;2
    </th>
    <td align="left">
      <html:text property="excludeLevel2Test" size="30" maxlength="30"/>
    </td>
</tr>
<tr>
    <th align="left">
      <bean:message key="options.edit_exclude_level_test"/>&nbsp;3
    </th>
    <td align="left">
      <html:text property="excludeLevel3Test" size="30" maxlength="30"/>
    </td>
</tr>
 
  <tr>
    <td>
      <html:submit/> 
    </td>
  </tr>
</table>

</html:form>


</body>
</html>
