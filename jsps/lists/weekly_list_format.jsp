<%@ page contentType="text/html; charSet=euc-kr" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page language="java" import="java.util.Vector" %>
<%@ page language="java" import="java.util.Hashtable" %>
<%@ page language="java" import="java.util.Enumeration" %>
<%@ page language="java" import="org.catechis.dto.Word" %>
<html>
<head><title><bean:message key="app.name"/> <bean:message key="app.slogan"/></title></head>
<body>


<%
int table_index = 0;		// this indicates when to start a new table
//int table_format = 15;		// this variable tells us how many words max per table
//int width = 335;
//String tr_width = "140";
//int ctrs_width = 335;		// change-to-right-side width
boolean left_side = true; //flag to indicate left side of main table
int fold_break = 0; /* this goes from 0,1,2,3 and represents four tables that will be printed on one page, and 
 therefore need thei9r own page breaks, fold breaks, as the paper is intened to be folded
 to fit in the pocket. Go thru reading/writing level 0 words, then reading and writing missed words.*/
Vector r_all_words = (Vector)session.getAttribute("r_all_words");
Vector w_all_words = (Vector)session.getAttribute("w_all_words");
Hashtable r_words_defs = (Hashtable)session.getAttribute("r_words_defs");
Hashtable w_words_defs = (Hashtable)session.getAttribute("w_words_defs");
Vector r_new_words = (Vector)session.getAttribute("r_new_words");
Vector w_new_words = (Vector)session.getAttribute("w_new_words");

/*
options for weekly_list_format.options
*/
Hashtable jsp_options = (Hashtable)session.getAttribute("jsp_options");
String tr_width = (String)jsp_options.get("format_tr_width");
int width = Integer.parseInt((String)jsp_options.get("format_table_width"));
int table_format = Integer.parseInt((String)jsp_options.get("format_table_limit"));
boolean format_print_reading = new Boolean((String)jsp_options.get("format_print_reading")).booleanValue();
boolean format_print_writing = new Boolean((String)jsp_options.get("format_print_writing")).booleanValue();
boolean format_print_missed_reading = new Boolean((String)jsp_options.get("format_print_missed_reading")).booleanValue();
boolean format_print_missed_writing = new Boolean((String)jsp_options.get("format_print_missed_writing")).booleanValue();
boolean daily_list_print_new_words_reading = new Boolean((String)jsp_options.get("daily_list_print_new_words_reading")).booleanValue();
boolean daily_list_print_new_words_writing = new Boolean((String)jsp_options.get("daily_list_print_new_words_writing")).booleanValue();

/*
out.println("<p>r_all_words "+r_all_words.size());
out.println("<p>w_all_words "+w_all_words.size());
out.println("<p>r_all_words "+r_all_words.size());
out.println("<p>r_words_defs "+r_words_defs.size());
out.println("<p>w_words_defs "+w_words_defs.size());
out.println("<p>r_new_words "+r_new_words.size());
out.println("<p>w_new_words "+w_new_words.size());
out.println("<p>format_print_reading "+format_print_reading);
out.println("<p>format_print_writing "+format_print_writing);
out.println("<p>format_print_missed_reading "+format_print_missed_reading);
out.println("<p>format_print_missed_writing "+format_print_missed_writing);
out.println("<p>daily_list_print_new_words_reading "+daily_list_print_new_words_reading);
out.println("<p>daily_list_print_new_words_writing "+daily_list_print_new_words_writing);
*/

Vector r_increments = (Vector)session.getAttribute("r_increments");
Vector w_increments = (Vector)session.getAttribute("w_increments");
int ctrs_width = width;	// the jury is still out on wther the left side and right side tables should have the same width
out.println("<table width=\"100%\">");
out.println("<tbody>");
out.println("<tr><td>");

	out.println("<table width=\""+width+"\">");	// left side table
	out.println("<tbody>");

// check for new words
if (r_new_words.size()>0||w_new_words.size()>0)
{
	// print new reading words if needed	
	if (format_print_missed_reading==true)
	{

		// new reading missed words
		out.println("<tr><td>");
		%>
		<b><bean:message key="weekly_list.new"/>&nbsp;
		<bean:message key="app.small_reading"/></b>
		<%
		out.println("</td><td>");
		%>
		<b><bean:message key="weekly_list.list"/></b>
		<%
		out.println("</td></tr>");
		int new_words_r_size = r_new_words.size();
		int n_r_i = 0;
		while (n_r_i < new_words_r_size)
		{
			Word n_r_word = (Word)r_new_words.get(n_r_i);
			out.println("<tr><td>");
			out.println("<p>"+n_r_word.getText());
			out.println("</td>");
			out.println("<td>");
			out.println("<p>"+n_r_word.getDefinition()+"</p>");
			out.println("</td></tr>");
			n_r_i++;
			}
	}
	
	// print new writing words if needed	
	if (format_print_missed_writing==true)
	{

		// new writing missed words
		out.println("<tr><td>");
		%>
		<b><bean:message key="weekly_list.new"/>&nbsp;
		<bean:message key="app.small_writing"/></b>
		<%
		out.println("</td><td>");
		%>
		<b><bean:message key="weekly_list.list"/></b>
		<%
		out.println("</td></tr>");
		int new_words_r_size = r_new_words.size();
		int n_r_i = 0;
		while (n_r_i < new_words_r_size)
		{
			Word n_r_word = (Word)r_new_words.get(n_r_i);
			out.println("<tr><td>");
			out.println("<p>"+n_r_word.getText());
			out.println("</td>");
			out.println("<td>");
			out.println("<p>"+n_r_word.getDefinition()+"</p>");
			out.println("</td></tr>");
			n_r_i++;
			}	
	}
}
	
if (format_print_reading==true)
{
	// reading level 0 words
	int r_size = r_all_words.size();
	int r_i = 0; // reading index
	while (r_i<r_size)
	{
		Word r_word = (Word)r_all_words.get(r_i);
		out.println("<tr><td width=\""+tr_width+"\">");
		out.println("<p>"+r_word.getText());
		out.println("</td>");
		out.println("<td width=\""+tr_width+"\">");
		out.println("<p>"+r_word.getDefinition()+"</p>");
		out.println("</td></tr>");
		
		int r_increment = 0;
		try
		{
			r_increment = Integer.parseInt((String)r_increments.get(r_i));
		} catch (java.lang.ArrayIndexOutOfBoundsException aioob)
		{
			// cant do shit
		}
		
		r_i++;
		table_index = table_index + 1 + r_increment;  // if there is no wrap expected, the inc will be 0

		// check switch from right side/left side table
		if (table_index>table_format)
		{
			table_index = 0;
			if (left_side == true)
			{
				//change to right side
				left_side = false;
				out.println("</tbody>");
				out.println("</table>");
				out.println("</td><td>");
				fold_break++;
				out.println("<table width=\""+ctrs_width+"\">");
				out.println("<tbody>");
			} else
			{
				// end of right side, start new table
				left_side = true;
				out.println("</td></tr>");
				out.println("</tbody>");
				out.println("</table>");
				out.println("</td></tr>");
				out.println("</tbody>");
				out.println("</table>");
				fold_break++;
				if (fold_break<4)
				{
					out.println("<p>");
					out.println("<p>");
				} else
				{
					fold_break = 0;
				}
				out.println("<table width=\"100%\">");
				out.println("<tbody>");
				out.println("<tr><td>");
				out.println("<table width=\""+width+"\">");
				out.println("<tbody>");
				out.println("<tr><td>");
			}
		}
	}
}

if (format_print_writing==true)
{
	// writing level 0 words
	int size = w_all_words.size();
	int i = 0; // writing index
	while (i<size)
	{	
		Word word = (Word)w_all_words.get(i);
		out.println("<tr><td width=\""+tr_width+"\">");
		out.println("<p>"+word.getDefinition()+"</p>");
		out.println("</td>");
		out.println("<td width=\""+tr_width+"\">");
		out.println("<p>"+word.getText());
		out.println("</td></tr>");
		
		int w_increment = 0;
		try
		{
			w_increment = Integer.parseInt((String)w_increments.get(i));
		} catch (java.lang.ArrayIndexOutOfBoundsException aioob)
		{
			// cant do shit
		}

		i++;
		table_index = table_index + 1 + w_increment;  // if there is no wrap expected, the inc will be 0
		System.out.println("table_index : "+table_index);
		
		if (table_index>table_format)
		{
			table_index = 0;
			if (left_side == true)
			{
				//change to right side
				left_side = false;
				out.println("</tbody>");
				out.println("</table>");
				fold_break++;
				out.println("</td><td>");
				out.println("<table width=\"100%\">");
				out.println("<tbody>");
			} else
			{
				// end of right side, start new table
				left_side = true;
				out.println("</td></tr>");
				out.println("</tbody>");
				out.println("</table>");
				out.println("</td></tr>");
				out.println("</tbody>");
				out.println("</table>");
				fold_break++;
				if (fold_break<4)
				{
					out.println("<p>");
					out.println("<p>");
				} else
				{
					fold_break = 0;
				}
				out.println("<table width=\"100%\">");
				out.println("<tbody>");
				out.println("<tr><td>");
				out.println("<table width=\""+width+"\">");
				out.println("<tbody>");
				out.println("<tr><td>");
			}
		}
		
	}
}
	

	// reading/writing missed words are nly prinetd if there are any
	if (r_words_defs.size()>0||w_words_defs.size()>0)
	{
		// missed words marker
		out.println("<tr><td>");
		%>
		<p><b><bean:message key="weekly_list.words_defs"/></b>
		<%
	}
	out.println("<td/>");
	out.println("<td></td></tr>");
	
if (format_print_missed_reading==true)
{
	for (Enumeration r_e = r_words_defs.keys() ; r_e.hasMoreElements() ;)
	{
		    	String r_key = (String)r_e.nextElement();
		    	String r_val = (String)r_words_defs.get(r_key);
		    	out.println("<tr><td width=\""+tr_width+"\">");
		    	out.println("<p>"+r_key);
		    	out.println("</td>");
		    	out.println("<td width=\""+tr_width+"\">");
		    	out.println("<p>"+r_val);
		    	out.println("</td></tr>");
		    
		    // check switch from right side/left side table
		table_index++;
		if (table_index>table_format)
		{
			table_index = 0;
			if (left_side == true)
			{
				//change to right side
				left_side = false;
				out.println("</tbody>");
				out.println("</table>");
				fold_break++;
				out.println("</td><td>");
				out.println("<table width=\"100%\">");
				out.println("<tbody>");
			} else
			{
				// end of right side, start new table
				left_side = true;
				out.println("</td></tr>");
				out.println("</tbody>");
				out.println("</table>");
				out.println("</td></tr>");
				out.println("</tbody>");
				out.println("</table>");
				fold_break++;
				if (fold_break<4)
				{
					out.println("<p>");
					out.println("<p>");
				} else
				{
					fold_break = 0;
				}
				out.println("<table width=\"100%\">");
				out.println("<tbody>");
				out.println("<tr><td>");
				out.println("<table width=\""+width+"\">");
				out.println("<tbody>");
				out.println("<tr><td>");
			}
		}
	    }
}

if (format_print_missed_writing==true)
{
	    for (Enumeration e = w_words_defs.keys() ; e.hasMoreElements() ;) 
	    {
		    	String key = (String)e.nextElement();
		    	String val = (String)w_words_defs.get(key);
		    	out.println("<tr><td width=\""+tr_width+"\">");
		    	out.println("<p>"+val);
		    	out.println("</td>");
		    	out.println("<td width=\""+tr_width+"\">");
		    	out.println("<p>"+key);
		    	out.println("</td></tr>");
		    
		    //end right side/left side table
		    table_index++;
		    if (table_index>table_format)
		    {
			table_index = 0;
			if (left_side == true)
			{
				//change to right side
				left_side = false;
				out.println("</tbody>");
				out.println("</table>");
				out.println("</td><td>");
				out.println("<table width=\"100%\">");
				out.println("<tbody>");
			} else
			{
				// end of right side, start new table
				left_side = true;
				out.println("</td></tr>");
				out.println("</tbody>");
				out.println("</table>");
				out.println("</td></tr>");
				out.println("</tbody>");
				out.println("</table>");
				out.println("<p>");
				out.println("<table width=\"100%\">");
				out.println("<tbody>");
				out.println("<tr><td>");
				out.println("<table width=\""+width+"\">");
				out.println("<tbody>");
				out.println("<tr><td>");
			}
		   }
	      }
}
		   
	    // and end main table
	    out.println("</table>");
	    out.println("</tr>");
out.println("</tbody>");
out.println("</table>");
int total_words = r_all_words.size() + w_all_words.size() + r_words_defs.size() + w_words_defs.size();
%>
<p><b><bean:message key="weekly_list.total_words"/></b>&nbsp;:&nbsp;
<%
out.println(total_words);
%>
</body>
</html>
