<%@ page contentType="text/html; charSet=euc-kr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="org.catechis.dto.Word" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>
<p><b><bean:message key="users_options_edit.instructions"/></b>
<html:form action="users_options_edit.do">
<table>
	<tr>
		<td>
			<bean:message key="users_options_edit.select"/>: 
		</td>
		<td>
			<select name="property">
				<option value="format_print_reading">format_print_reading</option>
				<option value="format_print_writing">format_print_writing</option>
				<option value="format_print_missed_reading">format_print_missed_reading</option>
				<option value="format_print_missed_writing">format_print_missed_writing</option>
				<option value="record_failed_tests">ecord_failed_tests</option>
				<option value="grade_whitespace">grade_whitespace</option>
				<option value="encoding">encoding</option>
				<option value="exclude_area_begin_char">exclude_area_begin_char</option>
				<option value="record_exclude_level">record_exclude_level</option>
				<option value="exclude_area_end_char">exclude_area_end_char</option>
				<option value="record_limit">record_limit</option>
				<option value="subject">subject</option>
				<option value="daily_words_limit">daily_words_limit</option>
				<option value="native_languge">native_languge</option>
				<option value="exclude_area">exclude_area</option>
				<option value="exclude_level0_test">exclude_level0_test</option>
				<option value="weekly_list_remove_repeats">weekly_list_remove_repeats</option>
				<option value="exclude_chars">exclude_chars</option>
				<option value="exclude_level1_test">exclude_level1_test</option>
				<option value="language_being_learned">format_print_reading</option>
				<option value="record_passed_tests">record_passed_tests</option>
				<option value="locale">locale</option>
				<option value="max_level">max_level</option>
				<option value="exclude_level2_test">exclude_level2_test</option>
				<option value="exclude_level3_test">exclude_level3_test</option>
				<option value="alternate_answer_separator">alternate_answer_separator</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="users_options_edit.value"/>: 
		</td>
		<td>
			<html:text property="value" />
		</td>
	</tr>
</table>
<p>
<html:submit/>
</html:form>
<p>
<p><a href="/indoct/jsps/admin/admin_welcome.jsp"><bean:message key="users_options_edit.return_to_admin_welcome"/></a>
<p>
</body>
</html>
