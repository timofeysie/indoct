package com.businessglue.struts;

import org.catechis.Storage;
import org.catechis.Domartin;
import org.catechis.FileStorage;
import org.catechis.Transformer;
import org.catechis.JDOMSolution;
import org.catechis.WordTestDateUtility;
import org.catechis.dto.Word;
import org.catechis.dto.Momento;
import org.catechis.dto.AllWordsTest;
import org.catechis.dto.WordTestDates;
import org.catechis.dto.WordTestResult;
import org.catechis.dto.WordLastTestDates;
import org.catechis.file.FileTestRecords;
import org.catechis.I18NWebUtility;

import java.io.File;
import java.util.Date;
import java.util.Vector;
import java.util.Hashtable;
import java.util.ArrayList;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
*<p>The Daily Test Action does a lot of work.
<p>It needs to check to see if a list of words to be tested is already in the session.
<p>If its not there, it has to create it, based on various criteria contained in the users option file
<p>For instance, if a word is at level 0, and the level 0 exclude option is set at 1 (day), and
the word has been tested less than a day ago, then that word is excluded from the list.
<p>Then we need to determine if a user has tested one of these words a previous time.
<p>In that case, this action is being forwarded from the daily_test_result.jsp, and
there should be a parameter passed in.
<p>Then we delete the word at 0 in the key_list ArrayList.  If the list is at zero,
then we need to make a new list.
<p> Standard scalable features to limit the amount of objects held in the session:
	<p>The user options should be loaded each time
	<p>The Storage object should be recreated each time
<p>These are obtained by the user_id alone, which should be held in the users session.
<p>In the past, these were held in the session, and for the time being, they are still there
<p>So that the legacy code will continue to function.
<p>When the new code is complete, they will not be added to the session, so that more users
<p>Will not create a large burden on the virtual machines memory. 
*@author Timothy Curchod
*/
public class DailyTestAction extends Action
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
		String type = (String)request.getParameter("test_type");
		context.log("DailyTestAction.perform");
		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		FileTestRecords ftr = new FileTestRecords(context_path);
		String subject = "vocab";					// this is the only subject so far...
		int grand_index = 0;
		try
		{
			grand_index = ftr.getTestsStatus(user_id, subject);	// this gets the the grand total of tests
			context.log("DailyTestAction.perform: grand_index is "+grand_index);
		} catch (java.lang.NullPointerException npe)
		{
			//printLog(ftr.getLog(), context);
			context.log("DailyTestAction.perform: grand_index is null");
		}
		Vector elt_vector = getELTVector(user_opts, context);		// exclude-level-times eliminate recently tested words from the list
		int daily_test_index = 0;					// all words test index
		int daily_words_limit = Integer.parseInt((String)user_opts.get("daily_words_limit")); // daily_words_limit
		ArrayList key_list = (ArrayList)session.getAttribute("actual_key_list"); // if this is null then we create it next
		WordLastTestDates wltd = (WordLastTestDates)session.getAttribute("wltd"); // the list of words and their key assosiations
		if (type!=null)
		{
			session.setAttribute("test_type", type);		//user has come here for the first time from the main page link
		} else
		{
			type = (String)session.getAttribute("test_type");	//user has come back after previous test
			String dti = (String)session.getAttribute("daily_test_index");
			daily_test_index = Integer.parseInt(dti);
			context.log("DailyTestAction.perform: what 7");
			daily_test_index = daily_test_index + 1;
			session.setAttribute("daily_test_index", Integer.toString(daily_test_index)); // increment index
		}
		if (key_list == null) 
		{
			key_list = new ArrayList();
			daily_test_index = 0;
		}
		context.log("DailyTestAction.perform: what 8");
		if (key_list.size() != 0)
		{
			try
			{
				context.log("DailyTestAction.perform: dti = "+daily_test_index);
				wltd.removeWord((Long)key_list.get(0));		// remove first word since it should have been tested
			} catch (java.lang.NullPointerException npe) {}
			try
			{
				context.log("DailyTestAction.perform:11 try");
				key_list.remove(0);
			} catch (java.lang.IndexOutOfBoundsException ioobe)
			{
				context.log("DailyTestAction.perform:11 ioobe");
				// this happened after only 5 daily tests
				// somehow there are no more tests
			} catch (java.lang.NullPointerException npe)
			{
				context.log("DailyTestAction.perform:11 npe");
				// if there are no tests yest
			}
		} else
		{
			wltd = new WordLastTestDates();				// create new list of words to be tested
			context.log("DailyTestAction.perform: what 9");
			wltd.setExcludeLevelTimes(elt_vector);
			wltd.setType(type);
			wltd.setLimitOfWords(daily_words_limit);
			try
			{
				// wltd is the place to save the daily test mark
				// it happens inside the storage object.
				// if i and j are 0:0 after this, the all the words
				// available to be tested have been, so we need to 
				// look in tests.records, so that after all the list
				// has been tested, the user recieves notification
				// that their work is done for the day.
				key_list = store.getWordLastTestDatesList(wltd, user_id, subject);
				context.log("DailyTestAction.perform: what 91");
				//printLog(store.getLog(), context);
			} catch (java.lang.NullPointerException npe)
			{
				//printLog(wltd.getLog(), context);
				context.log("DailyTestAction.perform: what 92");
			}
			session.setAttribute("daily_test_index", "0"); // reset the daily test index.
			context.log("DailyTestAction.perform: what 93");
		}
		Long actual_key = new Long("0");
		try
		{
			actual_key = (Long)key_list.get(0);
			context.log("DailyTestAction.perform: what 94");
		} catch (java.lang.IndexOutOfBoundsException aioob)
		{
			context.log("DailyTestAction.perform: aioob, but why???  I hope this method is someday studied and understood because the roayal we would like to reaqd that document..."); // just get the first bloody word we can find, for chrissake...
			wltd = new WordLastTestDates();				// create new list of words to be tested
			context.log("DailyTestAction.perform: what 95");
			
			wltd.setExcludeLevelTimes(elt_vector);
			wltd.setType(type);
			wltd.setLimitOfWords(daily_words_limit);
			try
			{
				// wltd is the place to save the daily test mark
				// it happens inside the storage object.
				// if i and j are 0:0 after this, the all the words
				// available to be tested have been, so we need to 
				// look in tests.records, so that after all the list
				// has been tested, the user recieves notification
				// that their work is done for the day.
				key_list = store.getWordLastTestDatesList(wltd, user_id, subject);
				context.log("DailyTestAction.perform: what 96");
				//printLog(store.getLog(), context);
			} catch (java.lang.NullPointerException npe)
			{
				//printLog(wltd.getLog(), context);
				context.log("DailyTestAction.perform: oh my god, what do we do now?");
				context.log("DailyTestAction.perform: what 97");
			}
			session.setAttribute("daily_test_index", "0"); // reset the daily test index.
			context.log("DailyTestAction.perform: what 98");
			// this is the same as the last if try catch catch catch block.
		}
		context.log("DailyTestAction.perform:14");
		String string_key = new String();
		try
		{
			string_key = actual_key.toString();
			context.log("DailyTestAction.perform.key_list.size() "+key_list.size());
		} catch (java.lang.NullPointerException npe)
		{
			// this is getting ridiculous...
			wltd = new WordLastTestDates();				// create new list of words to be tested
			context.log("DailyTestAction.perform: what 9");
			wltd.setExcludeLevelTimes(elt_vector);
			wltd.setType(type);
			wltd.setLimitOfWords(daily_words_limit);
			key_list = store.getWordLastTestDatesList(wltd, user_id, subject);
			string_key = actual_key.toString();
			context.log("DailyTestAction.perform: what 99");
			//printLog(store.getLog(), context);
		}
		Word first_word = setupFirstWord(wltd, string_key, context);
		context.log("DailyTestAction.perform: what 100");
		AllWordsTest awt_test_word = setupAWT(first_word, type, context);
		context.log("DailyTestAction.perform: added word id "+awt_test_word.getId());
		session.setAttribute("awt_test_word", awt_test_word);
		session.setAttribute("actual_key_list", key_list);
		session.setAttribute("wltd", wltd);
		session.setAttribute("grand_index", Integer.toString(grand_index));
		setMomento(ftr, user_id, subject, "DailyTestAction", null, null);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("daily_test"));
	}
	
	private Word setupFirstWord(WordLastTestDates wltd, String string_key, ServletContext context)
	{
		Word first_word = new Word();
		try
		{
			first_word = wltd.getWord(string_key);			// this was giving us an error before
			context.log("DailyTestAction.perform first word is not null");
			//printLog(Transformer.createTable(first_word), context);
			try
			{
				context.log("DailyTestAction.perform first word text "+first_word.getText());
			} catch (java.lang.NullPointerException npe1)
			{
				context.log("DailyTestAction.perform first word text is null");   // thatś cuz the word is blank, idiota
			}
		} catch (java.lang.NullPointerException npe)
		{
			context.log("DailyTestAction.perform first word is null");
			context.log("string key "+string_key);
			//printLog(wltd.getLog(), context);
		}
		return first_word;
	}
	
	private Vector getELTVector(Hashtable user_opts, ServletContext context)
	{
		Vector elt_vector = new Vector();
		elt_vector.add(user_opts.get("exclude_level0_test"));
		elt_vector.add(user_opts.get("exclude_level1_test"));
		elt_vector.add(user_opts.get("exclude_level2_test"));
		elt_vector.add(user_opts.get("exclude_level3_test"));
		return elt_vector;
	}
	
	private AllWordsTest setupAWT(Word test_word, String type, ServletContext context)
	{
		context.log("DailyTestAction.perform: what 101");
		AllWordsTest awt_test_word = new AllWordsTest();
		awt_test_word.setText(test_word.getText());
		awt_test_word.setDefinition(test_word.getDefinition());
		context.log("DailyTestAction.perform: what 102");
		awt_test_word.setCategory(test_word.getCategory());
		context.log("DailyTestAction.perform: what 103");
		awt_test_word.setTestType(type);
		context.log("DailyTestAction.perform: what 104");
		String level = new String();
		if (type.equals("reading"))
		{
			try
			{
				level = Integer.toString(test_word.getReadingLevel());
			} catch (java.lang.NullPointerException npe)
			{
				level = "0";
			}
		} else
		{
			try
			{
				level = Integer.toString(test_word.getWritingLevel());
			} catch (java.lang.NullPointerException npe)
			{
				level = "0";
			}
		}
		awt_test_word.setLevel(level);
		context.log("DailyTestAction.perform: what 105");
		awt_test_word.setId(test_word.getId());
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
	
	// move to interface
	
	private void setMomento(FileTestRecords ftr, String user_id, String subject, String action_name, String action_id, String action_type)
	{
			String action_time = Long.toString(new Date().getTime());
			Word word = new Word();
			if (action_id == null)
			{
				action_id = "000";
			}
			if (action_type == null)
			{
				action_type = "000";
			}
			Momento new_m = new Momento(action_name, action_time, action_id, action_type);
			ftr.setMomentoObject(user_id, subject, new_m);
	}
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("DailyTestAction "+(String)log.get(i));
			i++;
		}
	}

}
