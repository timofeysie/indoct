<%@ page contentType="text/html;charset=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html>
<head>
<title><bean:message key="registrationForm.title"/></title>
<html:base/>
</head>
<body bgcolor="white">

<!-- messages present deleted -->

<html:form action="edit_option">
  <html:hidden property="action"/>

<table border="0" width="100%">

<tr>
    <th align="left">
      <bean:message key="options.edit_option.option_name"/>
    </th>
    <td align="left">
      <html:text property="editOption" size="30" maxlength="30"/>
    </td>
  </tr>
 
  <tr>
    <td>
      <html:submit property="submit">
         <bean:message key="button.save"/>
      </html:submit>
      &nbsp;
      <html:reset>
         <bean:message key="button.reset"/>
      </html:reset>
      &nbsp;
      <html:cancel>
         <bean:message key="button.cancel"/>
      </html:cancel>    
    </td>
  </tr>
</table>

</html:form>

</body>
</html:html>
