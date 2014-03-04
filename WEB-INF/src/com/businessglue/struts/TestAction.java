package com.businessglue.struts;

// From LoginAction  Some may not be required...
import java.io.File;
import java.util.List;
import java.util.Vector;
import java.util.Random;
import java.util.Collections;
import java.util.Enumeration;

// this is just for testing
import java.util.Enumeration;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;
import com.businessglue.util.GetUserFiles;

// Added for this Action.
import java.util.Locale;
import java.util.Hashtable;
import com.businessglue.util.Domartin;
import com.businessglue.indoct.FileBean;
import com.businessglue.indoct.TestBean;
import com.businessglue.indoct.UserBean;
import com.businessglue.util.Workaround;
import com.businessglue.util.OpenTextFile;
import com.businessglue.util.ParseTextFile;

import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.legacy.CreateJDOMList;
import org.catechis.I18NWebUtility;

/**
<p>This class retrieves a filename from the header, parses puts it in a FileBean
 and puts the bean in the session scope.
 <p>Workarounds include, path obtained by dubious methods (done in LoginAction),
 and file_name retrieved by parsing header, and not request.getHeader("filename")which returns null.
 * The test_file_name used to be in the TestBean, but this cause null pointer exceptions
 * for some reason, so all the variables needed are put in the session.
 */
public class TestAction extends Action
{
	private Vector elements;
	private String user_name;
	private String file_name;
	private String user_path;
	private File file_chosen;
	//private ServletContext context;
	private String msg;
	private Hashtable words_defs;
	private HttpSession session;
	private HttpServletResponse res;
	
	/**
	*<p>This class recieves a file name to run a test on.  The test options should already
	* be loaded in the session inside the test_bean object.
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		res = response;
		session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String headers = request.getQueryString();
		file_name = Workaround.getHeader(headers);
		TestBean test_bean = (TestBean)session.getAttribute("TEST_KEY");
		UserBean user_bean = (UserBean)session.getAttribute("USER_KEY");
		String user_path = (String)user_bean.getPath();
		file_chosen = Domartin.createFileFromUserNPath(file_name, user_path);
		String test_name = test_bean.getFileName();
		
		String user_name = (String)session.getAttribute("user_name");
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_name, context_path);
		Vector elt_vector = getELTVector(user_opts, context);
		
		CreateJDOMList jdom = new CreateJDOMList(file_chosen, context);
		String test_levels = (String)session.getAttribute("test_levels");
		String test_index = (String)session.getAttribute("test_index");
		String test_length = (String)session.getAttribute("test_length");
		String test_type = (String)session.getAttribute("test_type");
		Hashtable un_shuffled_words_defs = jdom.getFilteredTextDefHash2(test_length, test_index, test_levels, test_type, elt_vector);
		context.log("TestAction.perform.jdom.ex_word:----------- ");
		dumpLog(jdom.getLog(), context);
		
		words_defs = un_shuffled_words_defs;
		String new_index = jdom.getNewIndex();
		test_bean.putTestFile(words_defs);
		test_bean.setTestFileName(file_name);
		session.setAttribute("TEST_KEY", test_bean);
		session.setAttribute("words_defs", words_defs);
		session.setAttribute("new_index", new_index);
		session.setAttribute("test_file_name", file_name);
		String user_id = (String)session.getAttribute("user_id");
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		if (test_type.equals("writing"))
		{return (mapping.findForward("run_writing_test"));}
		else if (test_type.equals("reading"))
		{return (mapping.findForward("run_reading_test"));}
		else
		{return (mapping.findForward("run_test"));}
	}
	
	private Vector getELTVector(Hashtable user_opts, ServletContext context)
	{
		Vector elt_vector = new Vector();
		elt_vector.add(user_opts.get("exclude_level0_test"));
		elt_vector.add(user_opts.get("exclude_level1_test"));
		elt_vector.add(user_opts.get("exclude_level2_test"));
		elt_vector.add(user_opts.get("exclude_level3_test"));
		//printLog(elt_vector, context);
		return elt_vector;
	}
	
	private void dumpLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log(" "+log.get(i));
			i++;
		}
	}
	
	/**
	* This method uses two separate Hastables to store key-value pairs
	* in a guaranteed order, which none of the Map interfaces or implementations
	* seem to support.  It seems that the Enumerator and Iterator class is the
	* main problem, as they don't provide the random ordering needed here.
	* If all we wanted was ordered, the stanard one Hastable would have been ok.
	*
	private Hashtable shuffleList(Hashtable un_shuffled_words_defs)
	{
		String test_shuffle = (String)session.getAttribute("test_shuffle");
		Hashtable shuffled_words_defs = new Hashtable();
		Hashtable return_hash = new Hashtable();
		Hashtable i_keys = new Hashtable();
		Hashtable i_vals = new Hashtable();
		Vector keys_list = new Vector();
		for (Enumeration keys = un_shuffled_words_defs.keys(); keys.hasMoreElements() ;) 
		{
			keys_list.add(keys.nextElement());
		}
		if (test_shuffle.equals("yes"))
		{
			context.log("TestAction.shuffleList: shuffling list");
			Collections.shuffle(keys_list, new Random());
			int size = keys_list.size();
			int i = 0;
			while (i<size)
			{
				String key = (String)keys_list.get(i);
				i_keys.put(i, key);
				i_vals.put(i, un_shuffled_words_defs.get(key));
				return_hash.put(key, un_shuffled_words_defs.get(key));
				context.log("TestAction.shuffleList: "+key+" "+un_shuffled_words_defs.get(key));
				i++;
			}
		}
		else
		{
			Enumeration keys = un_shuffled_words_defs.keys();
			int i = 0;
			while (keys.hasMoreElements())
			{
				String key = (String)keys.nextElement();
				String val = (String)un_shuffled_words_defs.get(key);
				i_keys.put(i, key);
				i_vals.put(i, val);
				i++;
			}
			return_hash = un_shuffled_words_defs;
		}
		return return_hash;
	}
	*/

}
