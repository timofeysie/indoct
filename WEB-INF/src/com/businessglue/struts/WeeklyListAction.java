package com.businessglue.struts;

import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.Transformer;
import org.catechis.dto.Word;
import org.catechis.dto.WordFilter;
import org.catechis.dto.AllWordsTest;
import org.catechis.dto.WordTestDates;
import org.catechis.dto.WordTestResult;
import org.catechis.dto.WordLastTestDates;
import org.catechis.dto.WordTestRecordOptions;
import org.catechis.WordTestDateUtility;
import org.catechis.file.FileTestRecords;
import org.catechis.constants.Constants;
import org.catechis.filter.WordListFilter;
import org.catechis.admin.FileUserOptions;

import org.catechis.I18NWebUtility;
import com.businessglue.struts.AllWordsTestForm;

import java.util.Vector;
import java.util.Hashtable;
import java.util.ArrayList;

import java.io.File;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;

/**
*<p>We assume that all words are game for this page, we just need to figure out
* what level and type the user has chosen, then forward to a page to choose other
* filter properties, like exclude recently tested words, etc.
*<p>The filter object needs to be retrieved from the session and then have properties
* from the previous form added, and then set back into the session.
*/
public class WeeklyListAction extends Action
{
	
	/**
	*<p>filename=all&type=w&level=1
	*/
	public ActionForward perform(ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		context.log("WeeklyListAction.perform: user id "+user_id); 
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = getUserOptions(user_id, context_path, store, context);
		String subject = Constants.VOCAB;
		FileUserOptions fuo = new FileUserOptions(context_path);
		Hashtable jsp_options = fuo.getJSPOptions(user_id, "weekly_list_format", subject);
		String daily_list_print_new_words_reading = (String)jsp_options.get("daily_list_print_new_words_reading");
		String daily_list_print_new_words_writing = (String)jsp_options.get("daily_list_print_new_words_writing");
		String format_print_missed_reading = (String)jsp_options.get("format_print_missed_reading");
		String format_print_missed_writing = (String)jsp_options.get("format_print_missed_writing");
		WordListFilter wlf = new WordListFilter();
		String reading = new String("reading"); String writing = new String("writing"); 
		String weekly_list_remove_repeats = (String)user_opts.get("weekly_list_remove_repeats");
		Vector r_new_words = getNewWords(daily_list_print_new_words_reading, user_id, store, Constants.READING, context);
		Vector w_new_words = getNewWords(daily_list_print_new_words_writing, user_id, store, Constants.WRITING, context);
		context.log(daily_list_print_new_words_reading+" r_new_words "+r_new_words.size()); 
		context.log(daily_list_print_new_words_writing+" w_new_words "+w_new_words.size()); 
		Vector r_all_words = new Vector(); Hashtable r_words_defs = new Hashtable();
		Vector w_all_words = new Vector(); Hashtable w_words_defs = new Hashtable();
		context.log("WeeklyListAction.perform: user id "+user_id); 
		if (weekly_list_remove_repeats.equals("true"))
		{
			r_all_words = wlf.getLevelZeroWords(user_id, reading, store);
			w_all_words = wlf.getFilteredLevelZeroWords(user_id, reading, r_all_words, store);
			Vector all_words = new Vector();
			all_words.addAll(r_all_words);
			all_words.addAll(w_all_words);
			Hashtable blank_missed_words = new Hashtable();	// first time thru pass in only a blank list
			// next check if we need to print missed words
			if (format_print_missed_reading.equals("true"))
			{
				r_words_defs = wlf.getNoRepeatsMissedWords1(reading, context_path, user_id, user_opts, blank_missed_words, all_words);
			}
			if (format_print_missed_writing.equals("true"))
			{
				w_words_defs = wlf.getNoRepeatsMissedWords1(writing, context_path, user_id, user_opts, r_words_defs, all_words);	// 2nd time thru, use the previous list to exclude.
			}
		} else
		{
			r_all_words =  wlf.getLevelZeroWords(user_id, reading, store);
			r_words_defs = wlf.getMissedWords(reading, context_path, user_id, user_opts);
			w_all_words =  wlf.getLevelZeroWords(user_id, writing, store);
			w_words_defs = wlf.getMissedWords(writing, context_path, user_id, user_opts);
			//r_new_words = store.getNewWordsList(Constants.READING, user_id);
			//w_new_words = store.getNoRepeatsNewWordsList(Constants.WRITING, r_new_words, user_id);
		}
		// get users real name for the title bar
		String user_name = "default";
		try
		{
			user_name = fuo.getUserName(user_id);
		} catch (java.lang.NullPointerException npe)
		{
			context.log("WeeklyListAction.perform fuo.getUserName npe ----- log");
			printLog(fuo.getLog(), context);
		}
		context.log("WeeklyListAction: user name "+user_name);
		//printLog(fuo.getLog(), context); 
		session.setAttribute("user_name", user_name);
		session.setAttribute("r_all_words", r_all_words);
		session.setAttribute("r_words_defs", r_words_defs);
		session.setAttribute("w_all_words", w_all_words);
		session.setAttribute("w_words_defs", w_words_defs);
		session.setAttribute("r_new_words", r_new_words);
		session.setAttribute("w_new_words", w_new_words);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("weekly_list"));
	}
	
	private Hashtable getUserOptions(String user_id, String context_path, Storage store, ServletContext context)
	{
		Hashtable user_opts = null;
		try
		{
			user_opts = store.getUserOptions(user_id, context_path);
		} catch (java.lang.NullPointerException npe)
		{
			printLog(store.getLog(), context);
			String true_str = "true";
			String weekly_list_remove_repeats = "weekly_list_remove_repeats";
		}
		return user_opts;
	}
	
	private Vector getNewWords(String daily_list_print_new_words, String user_id,
			Storage store, String type, ServletContext context)
	{
		Vector new_words = new Vector();
		if (daily_list_print_new_words.equals(Constants.TRUE))
		{
			new_words = store.getNewWordsList(type, user_id, Constants.VOCAB);
			printLog(store.getLog(), context);
		}
		return new_words;
	}
	
	/**
	*<p>Get words that have been tested and found wanting.
	*<p>This is controlled by settings in the users .option file in their folder.
	*<p>Typically settings would be for only words at a level above 0 that 
	* have been failed recently.
	*/
	private Hashtable getMissedWords(String type, String root_path, String user_id, Hashtable user_opts)
	{
	    boolean record_failed_tests = Boolean.valueOf((String)user_opts.get("record_failed_tests")).booleanValue();
	    boolean record_passed_tests = Boolean.valueOf((String)user_opts.get("record_passed_tests")).booleanValue();
	    String record_exclude_level = (String)user_opts.get("record_exclude_level");
	    int record_limit = Integer.parseInt((String)user_opts.get("record_limit"));
	    WordTestRecordOptions wtro = new WordTestRecordOptions();
	    wtro.setType(type);
	    wtro.setUserName(user_id);
	    wtro.setRootPath(root_path);
	    wtro.setRecordFailedTests(record_failed_tests);
	    wtro.setRecordPassedTests(record_passed_tests);
	    wtro.setRecordExcludeLevel(record_exclude_level);
	    wtro.setRecordLimit(record_limit);
	    FileTestRecords ftr = new FileTestRecords();
	    Hashtable words_defs = ftr.getSpecificDailyTestRecords(wtro);
	    return words_defs;
	}
	
	/**
	*Get a list of words with levels of a certain type at level 0.
	*/
	private Vector getLevelZeroWords(String user_id, String test_type, Storage store)
	{
		String min_max = new String("0-0");
		String category_name = new String("all");
		WordFilter word_filter = new WordFilter();
		Vector all_words =  new Vector();
		Vector all_word_categories = store.getWordCategories("exclusive", user_id);
		word_filter.setStartIndex(0);
		word_filter.setMinMaxRange(min_max);
		word_filter.setType(test_type);
		word_filter.setCategory(category_name);
		for (int i = 0; i < all_word_categories.size(); i++) 
		{
			String category = (String)all_word_categories.get(i);
			word_filter.setCategory(category);  // The filter needs a new name.
			Vector category_words = store.getFilteredWordObjects(word_filter, user_id);
			all_words.addAll(category_words);
		}
		return all_words;
	}
	
	private String getMinMax(String min, String max)
	{
		String min_max = new String(min+"-"+max);
		return min_max;
	}
	
	private void getWeeklyWordList()
	{
		/*
		ArrayList key_list = (ArrayList)session.getAttribute("actual_key_list"); // if this is null then we create it next
		WordLastTestDates wltd = (WordLastTestDates)session.getAttribute("wltd"); // the list of words and their key associations
		Hashtable user_opts = (Hashtable)session.getAttribute("user_options"); // used to get the exclude level test values
		AllWordsTest awt = (AllWordsTest)session.getAttribute("awt_test_word");	// this object holds the original word tested
		AllWordsTestForm test_form = (AllWordsTestForm)form; 
		Vector elt_vector = getELTVector(user_opts, context);
		int total_words_allowed =100;
		int total_words = 0;
		if (key_list==null||key_list.size()==0)
		{
			wltd = new WordLastTestDates();
			wltd.setExcludeLevelTimes(elt_vector);
			wltd.setType(type);
			wltd.setLimitOfWords(100);
			Vector all_word_categories = store.getWordCategories("exclusive", user_id);
			key_list = store.getWordLastTestDatesList(wltd, user_id); 
		}
		Long actual_key = (Long)key_list.get(0);
		
		AllWordsTest awt_test_word = setupAWT(wltd.getWord(actual_key.toString()), type);
		session.setAttribute("awt_test_word", awt_test_word);
		session.setAttribute("actual_key_list", key_list);
		session.setAttribute("wltd", wltd);
		*/
	}
	
	private Vector getELTVector(Hashtable user_opts, ServletContext context)
	{
		Vector elt_vector = new Vector();
		elt_vector.add(user_opts.get("exclude_level0_test"));
		elt_vector.add(user_opts.get("exclude_level1_test"));
		elt_vector.add(user_opts.get("exclude_level2_test"));
		elt_vector.add(user_opts.get("exclude_level3_test"));
		printLog(elt_vector, context);
		return elt_vector;
	}
	
	private AllWordsTest setupAWT(Word test_word, String type)
	{
		AllWordsTest awt_test_word = new AllWordsTest();
		awt_test_word.setText(test_word.getText());
		awt_test_word.setDefinition(test_word.getDefinition());
		awt_test_word.setCategory(test_word.getCategory());
		awt_test_word.setTestType(type);
		return awt_test_word;
	}
	
	/**Returns a new blank WordTestResult object if there was nothing in the field
	*/
	private WordTestResult scoreWordTest(AllWordsTestForm test_form)
	{
		WordTestResult wtr = new WordTestResult();
		// check if both field is blank, then return
		// grade test
		// update levels
		// re wtr properties
		return wtr;
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

}
