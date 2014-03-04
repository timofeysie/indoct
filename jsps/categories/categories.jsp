<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="com.businessglue.indoct.UserBean" %>
<%@ page language="java" import="org.catechis.Transformer" %>
<%@ page language="java" import="org.catechis.Domartin" %>
<%@ page language="java" import="org.catechis.dto.AllTestStats" %>
<%@ page language="java" import="org.catechis.dto.AllWordStats" %>
<%@ page language="java" import="org.catechis.dto.TestStats" %>
<%@ page language="java" import="org.catechis.dto.WordStats" %>
<%@ page language="java" import="org.catechis.dto.RatesOfForgetting" %>
<%@ page language="java" import="org.catechis.file.FileCategories" %>
<%@ page language="java" import="java.util.Date" %>
<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="java.text.DecimalFormat" %>
<%@ page language="java" import="java.text.ParsePosition" %>
<%@ page language="java" import="java.text.SimpleDateFormat" %>
<%@ page language="java" import="java.util.List" %>
<%@ page language="java" import="java.util.ArrayList" %>
<%@ page language="java" import="java.util.Hashtable" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>

<TABLE>
  <TR>
    <TH><b><bean:message key="app.category"/></b></TH>
    <TH><b><bean:message key="app.total"/></b></TH>
    <TH><b><bean:message key="app.reading"/> <bean:message key="app.average"/></b></TH>
    <TH><b><bean:message key="app.writing"/> <bean:message key="app.average"/></b></TH>
    <TH><b><bean:message key="app.reading"/>-0</b></TH>
    <TH><b><bean:message key="app.writing"/>-0</b></TH>
    <TH><b><bean:message key="app.reading"/>-1</b></TH>
    <TH><b><bean:message key="app.writing"/>-1</b></TH>
    <TH><b><bean:message key="app.reading"/>-2</b></TH>
    <TH><b><bean:message key="app.writing"/>-2</b></TH>
    <TH><b><bean:message key="app.reading"/>-3</b></TH>
    <TH><b><bean:message key="app.writing"/>-3</b></TH>
    <!--TH><b>Last Test</b></TH-->
  </TR>
<%

Hashtable categories = (Hashtable)session.getAttribute("categories");
FileCategories cats = new FileCategories();
ArrayList categories_key_list = cats.getSortedKeys(categories);
int max_levels = 3; // this should come from user preferences
AllWordStats all_word_stats = (AllWordStats)session.getAttribute("all_word_stats");
WordStats[] word_stats = all_word_stats.getWordStats();
String all_name = new String("all");
DecimalFormat formatter = new DecimalFormat("###.##");
Vector all_reading_levels = all_word_stats.getReadingLevels();
Vector all_writing_levels = all_word_stats.getWritingLevels();
out.println("<TR><TD><a href=\"/indoct/list.do?filename=all\">All</a></TD>");
out.println("<TD>"+all_word_stats.getNumberOfWords()+"</TD>");
out.println("<TD>"+formatter.format(all_word_stats.getReadingAverage())+"</TD>");
out.println("<TD>"+formatter.format(all_word_stats.getWritingAverage())+"</TD>");
int level = 0;
while (level<=max_levels)
{
	out.println("<TD>"+all_reading_levels.get(level)+"</TD>");
	out.println("<TD>"+all_writing_levels.get(level)+"</TD>");
	level++;
}

// Since we store the word stats in an array,
// we make a hashtable with the file name as the key.
Hashtable word_stats_hash = new Hashtable();
for (int in = 0; in < word_stats.length; in++) 
{
	WordStats word_stat = word_stats[in];
	word_stats_hash.put(word_stat.getName(), word_stat);
}

int it = 0;
int size = categories_key_list.size();
while (it<size)
{
	String key = (String)categories_key_list.get(it);
	String date = Transformer.getDateFromMilliseconds(key);
	//String cat = (String)categories.get(key);
	//System.out.println("testGetSortedWordCategories "+date+" "+cat);
	//String cat_key = categories_key_list.get(i);
	String name = (String)categories.get(key);
	WordStats word = (WordStats)word_stats_hash.get(name);
	//String name = word.getName();
	Vector reading_levels = word.getReadingLevels();
	Vector writing_levels = word.getWritingLevels();
	out.println("<TR><TD><a href=\"/indoct/file.do?filename="+name+"\">"+name+"</a></TD>");
	out.println("<TD>"+word.getNumberOfWords()+"</TD>");
	out.println("<TD>"+formatter.format(word.getAverageReadingScore())+"</TD>");
	out.println("<TD>"+formatter.format(word.getAverageWritingScore())+"</TD>");
	level = 0;
	while (level<=max_levels)
	{
		out.println("<TD>"+reading_levels.get(level)+"</TD>");
		out.println("<TD>"+writing_levels.get(level)+"</TD>");
		level++;
	}
	it++;
}
%>

<p>
<a href="/indoct/welcome.jsp"><bean:message key="add_word.return_to_main"/>

</body>
</html>
