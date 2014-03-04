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
import org.catechis.constants.Constants;
import org.catechis.dto.Word;
import org.catechis.Domartin;
import org.catechis.FileStorage;
import org.catechis.admin.FileUserOptions;
import org.catechis.admin.FileUserUtilities;
import org.catechis.file.FileCategories;
import org.catechis.filter.ImportListFilter;

import com.businessglue.struts.ImportListForm;
import com.businessglue.util.EncodeString;
import org.catechis.I18NWebUtility;

public class TeacherImportWordsAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String teacher_id = (String)session.getAttribute("teacher_id");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		String encoding = "euc-kr";
		Hashtable teacher_opts = getDefaultOps();
		try
		{
			teacher_opts = store.getTeacherOptions(teacher_id, context_path); 
			encoding = (String)teacher_opts.get("encoding");
		} catch (java.lang.NullPointerException npe)
		{
			printLog(store.getLog(), context);
		}
		context.log("ImportListAction.perform: user name "+teacher_id);
		ImportListForm f = (ImportListForm) form;
		String original = f.getText();
		context.log("ImportListAction.perform: original "+original);
		String char_encoding = request.getCharacterEncoding();
		String locale = request.getLocale().toString();
		context.log("char_encoding "+char_encoding);
		context.log("	    locale "+locale);
		String data = EncodeString.encodeThis(f.getText(),encoding);
		context.log("ImportListAction.perform: encoded "+data);
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
		String student_id = "";
		String file_name = (String)session.getAttribute("word_list_name");
		Hashtable students = (Hashtable)session.getAttribute("students");
		for (Enumeration e = students.elements() ; e.hasMoreElements() ;) 
		{
			String student_name = (String)e.nextElement();
			FileUserUtilities fut = new FileUserUtilities(context_path);
			student_id = fut.getId(student_name);
			FileCategories fc = new FileCategories();
			fc.addCategory(file_name, context_path, student_id);
			saveWords(student_id, file_name, long_time, results, session, encoding, context);
			FileUserOptions fuo = new FileUserOptions(context_path);
			String subject = Constants.VOCAB;
			fuo.createNewLists(student_id, subject, context_path);
		}
		Hashtable words_defs = store.getWordsDefs(file_name+".xml", student_id);
		session.setAttribute("words_defs", words_defs);
		I18NWebUtility.forceContentTypeAndLocale(request, response, teacher_opts);
		return (mapping.findForward("class_home")); 
	}
	
	private void saveWords(String student_id, String file_name, 
			long long_time, Hashtable results, 
			HttpSession session, String encoding, ServletContext context)
	{
		for (Enumeration e = results.keys() ; e.hasMoreElements() ;) 
		{
			String text = (String)e.nextElement();
			String definition = (String)results.get(text);
			long id = Domartin.getNewID();
			Word word = new Word();
			if (long_time == 0)
			{
				Date date = new Date();
				long_time = date.getTime();
			}
			word.setText(text);
			word.setDefinition(definition);
			word.setDateOfEntry(long_time);
			word.setId(id);
			word.setReadingLevel(0); // should allow for the user to pass this in 
			word.setWritingLevel(0); // but the default is 0
			word.setCategory(file_name);
			String context_path = context.getRealPath("/WEB-INF/");
			FileStorage store = new FileStorage(context_path, context);
			store.addWord(word, file_name, student_id, encoding);
			context.log("new id "+id+ " "+definition);
		}
		//printLog(store.getLog(), context);
	}
	
	private Hashtable getDefaultOps()
	{
		Hashtable teacher_opts = new Hashtable();
		String language = "ko";
		String country = "KR";
		String encoding = "euc-kr";
		String full_locale = language+"_"+country;
		teacher_opts.put("language", language);
		teacher_opts.put("country", country);
		teacher_opts.put("encoding", encoding);
		teacher_opts.put("locale", full_locale);
		return teacher_opts;
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
