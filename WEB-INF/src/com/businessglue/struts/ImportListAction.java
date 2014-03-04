package com.businessglue.struts;

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
import org.catechis.dto.AllTestStats;
import org.catechis.dto.AllWordStats;
import org.catechis.dto.Momento;
import org.catechis.dto.Word;
import org.catechis.Domartin;
import org.catechis.FileStorage;
import org.catechis.admin.FileUserOptions;
import org.catechis.file.FileTestRecords;
import org.catechis.file.FileWordCategoriesManager;
import org.catechis.filter.ImportListFilter;
import com.businessglue.util.EncodeString;
import org.catechis.I18NWebUtility;

/**
 * 
 * @author Timothy Curchod
 */
public class ImportListAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		String encoding = (String)user_opts.get("encoding");
		String subject = (String)user_opts.get("subject");
		context.log("ImportListAction.perform: user name "+user_id);
		ImportListForm f = (ImportListForm) form;
		String original = f.getText();
		context.log("ImportListAction.perform: original "+original);
		String char_encoding = request.getCharacterEncoding();
		String locale = request.getLocale().toString();
		context.log("char_encoding "+char_encoding);
		context.log("	    locale "+locale);
		String data = EncodeString.encodeThis(f.getText(),encoding);
		context.log("ImportListAction.perform: encoded "+data);
		context.log("ImportListAction.perform: options -=-=-=-=-=--=");
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
		saveWords(subject, long_time, results, session, encoding, context);
		String file_name = (String)session.getAttribute("file_name");
		// recalculate stats and update word count
		Hashtable last_record = updateStats(store, user_opts, user_id, context_path);
		// generate new test date files		
		FileUserOptions fuo = new FileUserOptions(context_path);
		fuo.createNewLists(user_id, subject, context_path);
		Hashtable words_defs = store.getWordsDefs(file_name, user_id);
		session.setAttribute("last_record", last_record);
		session.setAttribute("words_defs", words_defs);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("file")); 
	}
	
	private void saveWords(String subject, long long_time, Hashtable results, HttpSession session, String encoding, ServletContext context)
	{
		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		String file_name = (String)session.getAttribute("file_name");
		Vector <Word> words = new Vector<Word>();
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
			FileStorage store = new FileStorage(context_path, context);
			store.addWord(word, file_name, user_id, encoding);
			context.log("new id "+id+ " "+definition);
			words.add(word);
		}
		FileWordCategoriesManager fwcm = new FileWordCategoriesManager();
		long cat_id = fwcm.getCategoryId(file_name, user_id, subject, context_path);
		context.log("saveWords: cat_id "+cat_id+" log --- ");
		printLog(fwcm.getLog(), context);
		fwcm.resetLog();
		try
		{
			fwcm.addWordsToCategory(user_id, cat_id, subject, context_path, words, encoding);
		} catch (java.lang.NullPointerException npe)
		{
			context.log("saveWords: addWordsToCategory NPE!");
		}
		context.log("saveWords: add words  log ---------- ");
		printLog(fwcm.getLog(), context);
	}
	
	private Hashtable updateStats(FileStorage store, Hashtable user_opts, String user_id, String context_path)
	{
		ServletContext context = getServlet().getServletContext();
		Vector tests = store.getTestCategories(user_id);
		Vector words = store.getWordCategories("primary", user_id);
		AllTestStats all_test_stats = store.getTestStats(tests, user_id);
		AllWordStats all_word_stats = store.getWordStats(words, user_id);
		//store.updateHistory(all_test_stats, all_word_stats, user_id);
		long time = Long.parseLong("0");
		try
		{
			time = store.updateHistory(all_test_stats, all_word_stats, user_id);
			context.log("RecalculateStatsAction.perform: time "+time);
		} catch (java.lang.NoClassDefFoundError ncdfe)
		{
			context.log("RecalculateStatsAction.perform: ncdfe");
			printLog(store.getLog(), context);
		}
		context.log("RecalculateStatsAction.perform: go to teacher version");
		FileTestRecords ftr = new FileTestRecords(context_path);
		Momento old_m = ftr.getStatusRecord(user_id, Constants.VOCAB);
		Hashtable last_record = ftr.getReadingAndWritingLevels();
		return last_record;
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
