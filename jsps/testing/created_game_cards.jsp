<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="org.catechis.dto.Word" %>
<%@ page language="java" import="org.catechis.dto.Test" %>
<%@ page language="java" import="org.catechis.constants.Constants" %>
<%@ page language="java" import="org.catechis.dto.SavedTest" %>
<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<html>
<head><title><%out.println((String)session.getAttribute("user_name"));%></title></head>
<body>
<%
// we have to go through all the test entries to get the title?  Isn't there are better way?"
String user_name = (String)session.getAttribute("user_name");
Vector saved_tests = (Vector)session.getAttribute("saved_tests");
String test_format = Constants.READING;
String test_id = (String)session.getAttribute("test_id");
String test_date = "";
String test_name = "";
int ssize = saved_tests.size();
int si = 0;
while (si<ssize)
{
	try
	{
	    SavedTest saved_test = (SavedTest)saved_tests.get(si);
	    test_format = saved_test.getTestFormat();
	    String this_test_id = saved_test.getTestId();
	    if (this_test_id.equals(test_id))
	    {
		    test_date = Transformer.getDateFromMilliseconds(saved_test.getTestDate());
		    test_name = saved_test.getTestName();
			break;
		}
	} catch (java.lang.NullPointerException npe)
	{
		
	}
	si++;
}
Hashtable test_words = (Hashtable)session.getAttribute("test_words"); 
Hashtable word_deck_card_names = new Hashtable();
int i = 0;
int size = test_words.size();
Vector text_and_def = new Vector();
while (i<size)
{
	String key = i+"";
	Word word = (Word)test_words.get(key);
	Test test = word.getTests(0);
	String reading_deck_card_name = test.getName();
	String writing_deck_card_name = test.getLevel();
	if (test_format.equals(Constants.WRITING_STONES))
	{
		// writing tests have only the word definitions.
		text_and_def.add(word.getDefinition());
		// word_deck_card_names.put(word.getDefinition(), writing_deck_card_name); npe
	} else
	{
		text_and_def.add(word.getText());
		text_and_def.add(word.getDefinition());
	}
	if (reading_deck_card_name != null)
	{
		word_deck_card_names.put(word.getText(), reading_deck_card_name);
		word_deck_card_names.put(word.getDefinition(), writing_deck_card_name);
	} else
	{
		word_deck_card_names.put(word.getText(), "");
		word_deck_card_names.put(word.getDefinition(), "");
	}
	i++;
}
out.println((String)session.getAttribute("house_deck_name"));
i = 0;
size = text_and_def.size();
boolean  new_table = true;
int row = 0;
int col = 0;
int number_of_tables = 1;
while (i<size)
{
	if (new_table)
	{
		out.println("<p>&nbsp;.");
		out.println("<p>&nbsp;.");
		out.println("<p>&nbsp;.");
		for (int t = 0; t < number_of_tables; t++)
		{
			out.println("<p>&nbsp;.");
		}
		if (number_of_tables < 4)
		{
		number_of_tables++;
		}
		out.println("<center><table style=\"border:1px solid black;border-collapse:collapse;table-layout:fixed;\" border=\"1\" cellpadding=\"1\" cellspacing=\"2\" bgcolor=\"white\">");
		new_table = false;
	} 
	if (col>5)
	{
		col = 0;
		row = 0;
		out.println("</tr></table></center>");
		out.println("<p>&nbsp;.");
		out.println("<p>&nbsp;.");
		out.println("<p>&nbsp;.");
		new_table = true;
	} else
		{	
		if (row==0)
		{
			out.println("<tr height=\"321\">");
		}
		out.println("<td width=\"221\" height=\"321\">");
		out.println("<table style=\"table-layout:fixed;overflow: hidden;word-wrap:break-word\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" height=\"100%\">");
		out.println("<tr>");
		
		out.println("<td>");
		out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"2\" width=\"100%\">");
                                        out.println("<tr>");
                                            out.println("<td>");
                                                out.println("<div align=\"left\">");
                                                    out.println("&nbsp;&nbsp;"+user_name);
                                            out.println("</td>");
                                            out.println("<td>");
                                                out.println("<div align=\"right\">");
                                                    out.println(word_deck_card_names.get((String)text_and_def.get(i))+"&nbsp;&nbsp;</div>"); // deck card names go here
                                            out.println("</td>");
                                        out.println("</tr>");
                                   out.println("</table>");
		out.println("</td>");
		
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td>");
		out.println("<center>");
		out.println("<center><b><h1>"+(String)text_and_def.get(i)+"</h1></b></center>");
		out.println("</td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td>");
		out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"2\" width=\"100%\">");
                                        out.println("<tr>");
                                            out.println("<td>");
                                                out.println("<div align=\"left\">");
                                                    out.println("&nbsp;&nbsp;"+test_name);
                                            out.println("</td>");
                                            out.println("<td>");
                                                out.println("<div align=\"right\">");
                                                    out.println(test_date+"&nbsp;&nbsp;</div>");
                                            out.println("</td>");
                                        out.println("</tr>");
                                   out.println("</table>");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");	
		out.println("</td>");
		i++;
		row++;
		col++;
		if (row > 2)
		{
			out.println("</tr>");
			row = 0;
		}
	}
}	
%>

<a href="/indoct/testing/created_reading_game_cards.jsp"><bean:message key="created_game_cards_reading"/>

</body>
</html>