package com.businessglue.util;

import java.util.List;
import java.util.Date;
import java.text.DateFormat;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Enumeration;
import org.jdom.Element;
import java.io.File;
import java.lang.SecurityManager;
import java.lang.SecurityException;
import javax.servlet.ServletContext;
import java.lang.NullPointerException;
import com.businessglue.util.CreateJDOMList;

/**
 * @deprecated use Catechis version.
<p>This class contains general purpose static methods for all commoners.</p>
<p>The name reflects the Commons of Domartin in Switzerland when the Authors ancestors live.</p>
<p>To Do:</p>
<p>Create an exception for getFileExtension in the case that there is no extension.</p>
<p>@author Timothy Butler Curchod</p>
<p>@version September 11, 2004</p>
*/
public class Domartin
{

	public Domartin()
	{
	}
	
	/**
	*@param user Name of the user which equates to a folder in the 'files' directory.
	*@param path Path that is added to the name from which the file is create.
	*@return A file that is created by adding the path to the user separeted by the system separator.
	*/
	public static File createFileFromUserNPath(String user, String path)
	{
		Properties properties = System.getProperties();
		String sep = new String(properties.getProperty("file.separator"));
		String full_path = new String(path+sep+user);
		File file = new File(full_path);
		return file;
	}
	
	public static String createStringFromUserNPath(String user, String path)
	{
		Properties properties = System.getProperties();
		String sep = new String(properties.getProperty("file.separator"));
		String full_path = new String(path+sep+user);
		return full_path;
	}	
	
	public static String getFileExtension(String file_name)
	{
		String ext = new String();
		try
		{
			int dot = file_name.lastIndexOf(".");
			int len = file_name.length();
			ext = file_name.substring(dot,len);
		}
		catch (java.lang.StringIndexOutOfBoundsException e)
		{
			ext = "-1";
		}
		return ext;
	}
	
	/**
	<p>This method was created when we were trying to load a file from a folder
	created manually in the webapps directory because we had tomcat set to not
	expand .war files, which is faster.  It may still be possible to work with
	the file system when using an unexpanded war file, but the problem turned out
	to have nothing to do with permissions.</p>
	*/
	public static void checkPathPermissions(File _file, ServletContext _context)
	{
		String file_path = _file.getAbsolutePath();
		ServletContext context = _context;
		String msg = new String();
		SecurityManager security = System.getSecurityManager();
		if (security != null) 
		{
			try
			{
				security.checkRead(file_path);
			}
			catch (java.lang.NullPointerException ne)
			{
				msg = ("null security "+ ne.toString());
				context.log(msg);
			}
			catch (SecurityException se)
			{
				msg = ("security exception "+se.toString());
				context.log(msg);
			}
		}
		else
		{
			msg = ("Security is null:  What should we do?");
			context.log(msg);
		}
	}
	
	/**
	<p>This method could return -1 if you pass in a string with no '.'</p>
	*/
	public static String getFileWithoutExtension(String file_w_ext)
	{
		String file_wo_ext = new String();
		try
		{
			int dot = file_w_ext.lastIndexOf(".");
			file_wo_ext = file_w_ext.substring(0, dot);
		}
		catch (java.lang.StringIndexOutOfBoundsException e)
		{
			file_wo_ext = "-1";
		}
		return file_wo_ext;
	}
	
	/**
	*<p>This method needs to know what range of words has been tested, beacue we only want
	* to evaluate the levels of those words.</p>
	*<p>Right now this is not possible as the number of words is fixed at 10,
	* and each test only tests ten words, which most test files have anyway.</p>
	*<p>Currently we have to cycle thru all the words, and look for a "level" child and create
	* it if it doesn't already exist.</p>
	*<p>Then we have to look at the most recent test, and adjust the level if it meets
	* the criteria of the options file level settings.  For example, if the word was answered
	* correctly, then a "pass" is registered in the test sub-element.  Then, if we find the
	* word is at level 0, or if there is no level sub-element, we create it, and check to options
	* level, which, if level-1 is set to one, that means one passed test is enough to advance the
	* level to 1.</p>
	*/
	public static Hashtable evaluateWordLevels(CreateJDOMList levels_jdom, String test_name)
	{
		Hashtable new_word_levels = new Hashtable();
		List word_list = levels_jdom.getWhateverElementsList("word");
		Iterator words = word_list.iterator();
		while (words.hasNext())
		{
			Element word = (Element)words.next();
			String current_level = levels_jdom.ifXDontExistCreateIt(word, "level", "0");
			List tests = word.getChildren("test");
			Iterator all_tests = tests.iterator();
			String recent_result = mostRecentTestResult(all_tests, test_name);
			String new_level = new String();
			if (recent_result.equals("pass"))
				new_level = incrementWordLevel(current_level);
			else
				new_level = decrementWordLevel(current_level);
			Element level = word.getChild("level");
			level.setText(new_level);
			String word_text = word.getChildText("text");
			new_word_levels.put(word_text,new_level);
		}
		return new_word_levels;
	}
	
	/**
	*<p>Here we check for tests from only the given test file passed in as an argument.</p>
	*<p>We return only the pass/fail string of the most recent test.</p>
	*/
	private static String mostRecentTestResult(Iterator all_tests, String test_name)
	{
		String most_recent_result = new String();
		DateFormat df = DateFormat.getDateInstance();
		Date recent_date = null;
		while (all_tests.hasNext())
		{
			Element test = (Element)all_tests.next();
			String file_name = test.getChildText("file");
			if (file_name.equals(test_name))
			{
				// find most recent test date.
			}
		}
		return most_recent_result;
	}
	
	public static String incrementWordLevel(String current_level)
	{
		Integer current_int = Integer.valueOf(current_level);
		int current = current_int.intValue();
		int new_level = (current+1);
		String return_level = Integer.toString(new_level);
		return return_level;
	}
	
	public static String decrementWordLevel(String current_level)
	{
		Integer current_int = Integer.valueOf(current_level);
		int current = current_int.intValue();
		int new_level = (current-1);
		if (new_level<0)
		{
			new_level = 0;
		}
		String return_level = Integer.toString(new_level);
		return return_level;
	}	

}
