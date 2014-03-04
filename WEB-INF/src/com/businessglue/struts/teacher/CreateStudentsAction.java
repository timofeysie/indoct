package com.businessglue.struts.teacher;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import java.util.Date;
import java.util.Locale;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import org.catechis.Storage;
import org.catechis.dto.Word;
import org.catechis.Domartin;
import org.catechis.FileStorage;
import org.catechis.admin.FileUserOptions;
import org.catechis.admin.FileUserUtilities;
import org.catechis.filter.ImportListFilter;

import com.businessglue.struts.ImportListForm;
import com.businessglue.util.EncodeString;
import org.catechis.I18NWebUtility;

/**
 * Create a new user for every "user_name=e-mail;" string passed in from the form.
 * The Hashtable user_ids is then set in the session which is the user_name, new_password.
 * The new password should be "user_name+123" where 123 is a random three digit number.
 * @author Timothy Curchod
 *
 */
public class CreateStudentsAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String teacher_id = (String)session.getAttribute("teacher_id");
		String class_id   = (String)session.getAttribute("class_id");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getTeacherOptions(teacher_id, context_path);
		String encoding = (String)user_opts.get("encoding");
		context.log("CreateStudentsAction.perform: user name "+teacher_id);
		ImportListForm f = (ImportListForm) form;
		
		String original = f.getText();
		context.log("CreateStudentsAction.perform: original "+original);
		String char_encoding = request.getCharacterEncoding();
		String locale = request.getLocale().toString();
		context.log("char_encoding "+char_encoding);
		context.log("	    locale "+locale);
		String data = EncodeString.encodeThis(f.getText(),encoding);
		context.log("CreateStudentsAction.perform: encoded "+data);
		context.log("CreateStudentsAction.perform: options -=-=-=-=-=--=");
		printLog(user_opts, context);
		
		String date = new String();
		long long_time = 0;
		try
		{
			date = f.getDate();		// mm/dd/yyyy
			long_time = Domartin.getMillisecondsFromShortDate(date);
		} catch (java.lang.NullPointerException npe)
		{
			// use current date
		}
		String word_separator = ("="); // default values
		String line_separator = (";"); // older users don't have these in their option files.
		if (user_opts.contains("import_word_separator"))
		{
			word_separator = (String)user_opts.get("import_word_separator");
			line_separator = (String)user_opts.get("import_line_separator");
		}
		ImportListFilter ilf = new ImportListFilter();
		Hashtable results = ilf.parse(data, word_separator, line_separator);
		context.log("Results size "+results.size());  // 0
		if (results.size() == 0)
		{
			Hashtable results2 = ilf.parse(original, word_separator, line_separator);
			context.log("Results2 size "+results.size());  // 0
			results = results2;
		}
		printLog(ilf.getLog(), context);
		Hashtable user_ids = createStudents(long_time, results, session, encoding, 
				teacher_id, class_id);
		session.setAttribute("user_ids", user_ids);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("create_students_result")); 
	}
	
	private Hashtable createStudents(long long_time, Hashtable results, HttpSession session, 
			String encoding, String teacher_id, String class_id)
	{
		ServletContext context = getServlet().getServletContext();
		String context_path = context.getRealPath("/WEB-INF/");
		Hashtable user_ids = new Hashtable();
		Vector student_ids = new Vector();
		FileUserUtilities fut = new FileUserUtilities(context_path);
		for (Enumeration e = results.keys() ; e.hasMoreElements() ;) 
		{
			String user_name = (String)e.nextElement();
			user_name = user_name.replaceAll("[\n\r]", "");

			String e_mail = (String)results.get(user_name);
			// check to see if that username already exists
			Vector actual_users = fut.getUsers(new Vector());
			if (actual_users.contains(user_name))
			{
				// skip user.  provide a message if you want...
			}
			String password = createNewPassword(user_name);
			fut.addNewUser(user_name, password, e_mail);
			String new_user_id = fut.getId(user_name);
			student_ids.add(new_user_id);
			context.log("AAddNewUserAction.perform: created "+new_user_id+" "+user_name+" "+password+" "+e_mail);
			FileStorage store = new FileStorage(context_path, context);
			user_ids.put(user_name, password);
			Hashtable user_opts = new Hashtable();
		}
		fut.addStudentsToClass(student_ids, class_id, teacher_id);
		return user_ids;
	}
	
	private String createNewPassword(String user_name)
	{
		String y = Domartin.getNewID()+"";
		String first_char = String.valueOf((y.charAt(0)));
		if (first_char.equals("-"))
		{
			y = y.substring(1, y.length());
		}
		String cheese = y.substring(0,3);
		String me_along = user_name+cheese;
		me_along = me_along.replaceAll("[\n\r]", "");
		return me_along;
	}
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log((String)log.get(i));
			i++;
		}
	}
	
	private void printLog(Hashtable log, ServletContext context)
	{
		for (Enumeration e = log.elements() ; e.hasMoreElements() ;) 
		{
			context.log((String)e.nextElement());
		}
	}	

}
