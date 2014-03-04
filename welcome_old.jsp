<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.text.DecimalFormat" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
 <FORM action="http://211.220.31.50:8080/indoct/remote_login.do" method="post">
    <P>
    <LABEL for="name">name: </LABEL>
              <INPUT type="text" id="name"><BR>
    <LABEL for="id">id: </LABEL>
              <INPUT type="text" id="id"><BR>
    <INPUT type="submit" value="Send">
    </P>
 </FORM>


</body>
</html>
