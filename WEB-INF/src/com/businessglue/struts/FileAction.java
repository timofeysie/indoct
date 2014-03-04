package com.businessglue.struts;

// From LoginAction  Some may not be required...
import java.io.File;
import java.util.Vector;
import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;
import com.businessglue.indoct.UserBean;
import com.businessglue.util.GetUserFiles;

// Added for this Action.
import java.util.Locale;
import java.util.Hashtable;
import com.businessglue.util.Domartin;
import com.businessglue.indoct.FileBean;
import com.businessglue.indoct.TestBean;
import com.businessglue.util.Workaround;
import com.businessglue.util.OpenTextFile;
import com.businessglue.util.CreateJDOMList;
import com.businessglue.util.ParseTextFile;

import org.catechis.FileStorage;
import org.catechis.Storage;
import org.catechis.Transformer;
import org.catechis.I18NWebUtility;
import org.catechis.file.FileCategories;

/**
<p>This class retrieves a filename from the header, parses puts it in a FileBean
 and puts the bean in the session scope.
 <p>Workarounds include, path obtained by dubious methods (done in LoginAction),
 and file_name retrieved by parsing header, and not request.getHeader("filename")which returns null.
 <p>Took out the file_bean attribute words_defs because it caused problems later with
 a class cast exception.
  * @author Timothy Curchod
 */
public class FileAction extends Action
{
	private Vector elements;
	private String user_name;
	private String file_name;
	private String user_path;
	private File file_chosen;
	private ServletContext context;
	private String msg;
	private Hashtable words_defs;
	private HttpSession session;
	private HttpServletResponse res;
	private FileBean file_bean;
	
	/**
	*<p>This class is responsible for deciding what kind of file has been chosen,
	* and where to send the control to next.  The user_bean should be set with any
	* data that will be needed later.
	*<p>Currently there is a little confusion here which should be made more consistent.
	* Previously we send control only to the file.jsp, but with more actions required, we
	* started to go to different jsp's.  This has been remedied somewhat so far.  Check it out.
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		res = response;
		session = request.getSession();
		file_name = Workaround.getHeader(request.getQueryString());
		context = getServlet().getServletContext();
		context.log("FileAction<init> recieved file "+file_name);
		file_bean = new FileBean();
		UserBean user_bean = (UserBean)session.getAttribute("USER_KEY");
		String user_id = (String)session.getAttribute("user_id");
		user_name = (String)session.getAttribute("user_name");
		user_path = (String)session.getAttribute("user_path");
		//user_path = (String)user_bean.getPath();
		file_chosen = Domartin.createFileFromUserNPath(file_name, user_path);
		Domartin.checkPathPermissions(file_chosen, context);
		String ext = Domartin.getFileExtension(file_name);
		elements = new Vector();
		words_defs = null;
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		if (ext.equals(".catalog")){catAction();} 
		else if (ext.equals(".xml")){xmlAction();}
		else if (ext.equals(".txt"))
		{
			checkTypeAction();
			return (mapping.findForward("text"));
		}
		else if (ext.equals(".options"))
		{
			optionsAction();
			return (mapping.findForward("options"));
		}
		else if (ext.equals(".test"))
		{
			testAction();
			return (mapping.findForward("test"));
		}
		return (mapping.findForward("open"));
	}
	
	private void catAction()
	{
		msg = new String("opening catalog file");
		context = getServlet().getServletContext();
		context.log(msg);
		GetUserFiles guf = new GetUserFiles(user_name, user_path);
		elements = guf.getElements();
	}
	
	private void xmlAction()
	{
		msg = new String("opening xml file");
		context = getServlet().getServletContext();
		context.log(msg);
		CreateJDOMList jdom = new CreateJDOMList(file_chosen, context);
		elements = jdom.getTextDefVector();
		words_defs = jdom.getTextDefHash();
		file_bean = new FileBean();
		file_bean.setFileName(file_name);
		file_bean.setUserName(user_name);
		file_bean.setFilePath(user_path);
		session.setAttribute("file_name", file_name);
		session.setAttribute("user_name", user_name);
		session.setAttribute("user_path", user_path);
		session.setAttribute("words_defs", words_defs);
		file_bean.putElementVector(elements);
		file_bean.putWordsDefs(words_defs);
		session.setAttribute("FILE_KEY", file_bean);
	}
	
	/**
	<p>IF file has a previous extension, such as file.list.txt,
	we want to find the previous extension and act accordingly in this method.
	<p>Currently, the .list extension will be a plain text file of a list of 
	comma separated words.
	<p>If there is no extension, file_second_type will be returned as -1, and will default
	to txtAction anyway.
	*/
	private void checkTypeAction()
	{
		String name_wo_ext = new String(Domartin.getFileWithoutExtension(file_name));
		String file_second_type = Domartin.getFileExtension(name_wo_ext);
		if (file_second_type.equals(".list"))
		{
			parseTxtAction();
		}
		else
		{
			txtAction();
		}
	}
	
	private void parseTxtAction()
	{
		context.log("parsing text file");
		String name = new String(Domartin.createStringFromUserNPath(file_name, user_path));
		ParseTextFile text_parser = new ParseTextFile(name);
		words_defs = text_parser.getNameAndDefHashtable(context);
		elements = text_parser.getVectorFromHashtable();
	}
	
	private void txtAction()
	{
		context.log("opening text file");
		String name = new String(Domartin.createStringFromUserNPath(file_name, user_path));
		OpenTextFile vampires = new OpenTextFile(name, context);
		StringBuffer sometimes = vampires.getTextBuffer();
		elements = new Vector();
		String elems = new String(sometimes);
		elements.add(elems);
		FileBean file_bean = new FileBean();
		file_bean.setFileName(file_name);
		file_bean.setUserName(user_name);
		file_bean.setFilePath(user_path);
		file_bean.putElementVector(elements);
		session.setAttribute("FILE_KEY", file_bean);
	}
	
	private void optionsAction()
	{
		FileBean file_bean = new FileBean();
		CreateJDOMList jdom = new CreateJDOMList(file_chosen, context);
		Hashtable options = jdom.getOptionsHash();
		file_bean.putOptions(options);
		file_bean.setFileName(file_name);
		file_bean.setUserName(user_name);
		file_bean.setFilePath(user_path);
		session.setAttribute("FILE_KEY", file_bean);
	}
	
	private void testAction()
	{
		FileBean file_bean = new FileBean();
		CreateJDOMList jdom = new CreateJDOMList(file_chosen, context);
		Hashtable test = jdom.getWhateverHash("option", "name", "value");
		String length = 	(String)test.get("length");
		String index = 		(String)test.get("index");
		String levels = 	(String)test.get("levels");
		String type = 		(String)test.get("type");
		String shuffle = 	(String)test.get("shuffle");
		context.log("FileAction.testAction: lenght-"+length+" index-"+index+" levels "+levels+" type "+type);
		TestBean test_bean = new TestBean();
		test_bean.putOptions(test);
		test_bean.setFileName(file_name);
		context.log("FileAction.testAction: ------------");
		printLog(Transformer.createTable(test_bean));
		// added after welcome page overhaul
		File path_file = new File("");
	        String current_dir = path_file.getAbsolutePath();
		String context_path = context.getRealPath("/WEB-INF/");
	        FileCategories cats = new FileCategories(context_path);
		context.log("FileAction.testAction: ------------1");
		Vector word_categories = cats.getFilteredFiles(user_name, ".xml", "files");
		session.setAttribute("word_categories", word_categories);
		context.log("FileAction.testAction: cats ------------");
		printLog(cats.getLog());
		/* Using beans causes null pointer exceptions later, so all the
		* needed variables have been taken out and put in the session.
		* Later we may decide to use beans again if the problem is solved.
		*/
		session.setAttribute("TEST_KEY", test_bean);
		session.setAttribute("test_options", test);
		session.setAttribute("test_name", file_name);
		 
		session.setAttribute("test_length", length);
		session.setAttribute("test_index", index);
		session.setAttribute("test_levels", levels);
		session.setAttribute("test_type", type);
		session.setAttribute("test_shuffle", shuffle);
	}
	
	private void printLog(Vector log)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("AddCategoryAction.log "+(String)log.get(i));
			i++;
		}
	}

}
