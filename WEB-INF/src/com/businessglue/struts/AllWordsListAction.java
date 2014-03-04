package com.businessglue.struts;

import org.catechis.Storage;
import org.catechis.dto.Word;
import org.catechis.dto.AllWordsTest;
import org.catechis.dto.WordTestDates;
import org.catechis.dto.WordTestResult;
import org.catechis.WordTestDateUtility;
import org.catechis.dto.WordLastTestDates;

import org.catechis.I18NWebUtility;
import com.businessglue.struts.AllWordsTestForm;

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
import org.apache.commons.beanutils.PropertyUtils;

/**
*<p>We assume that all words are game for this page, we just need to figure out
* what level and type the user has chosen, then forward to a page to choose other
* filter properties, like exclude recently tested words, etc.
*<p>The filter object needs to be retrieved from the session and then have properties
* from the previous form added, and then set back into the session.
* @author Timothy Curchod
*/
public class AllWordsListAction extends Action
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
		String type = new String("reading"); 
		Word test_word = null;  					// this will hold the next test word
		int number_of_words_to_test = 1; 				// how many words to test per page
		Storage store = (Storage)session.getAttribute("store");
		String user_name = (String)session.getAttribute("user_name");
		ArrayList key_list = (ArrayList)session.getAttribute("actual_key_list"); // if this is null then we create it next
		WordLastTestDates wltd = (WordLastTestDates)session.getAttribute("wltd"); // the list of words and their key associations
		Hashtable user_opts = (Hashtable)session.getAttribute("user_options"); // used to get the exclude level test values
		AllWordsTest awt = (AllWordsTest)session.getAttribute("awt_test_word");	// this object holds the original word tested
		AllWordsTestForm test_form = (AllWordsTestForm)form; 		// test form
		awt.setAnswer(test_form.getField());
		WordTestResult wtr = store.scoreSingleWordTest(awt, user_name);
		Vector elt_vector = getELTVector(user_opts, context);
		int total_words_allowed =100;
		int total_words = 0;
		if (key_list==null)
		{
			wltd = new WordLastTestDates();
			wltd.setExcludeLevelTimes(elt_vector);
			wltd.setType(type);
			wltd.setLimitOfWords(100);
			Vector all_word_categories = store.getWordCategories("exclusive", user_name);
			String subject = (String)user_opts.get("subject");
			key_list = store.getWordLastTestDatesList(wltd, user_name, subject); 
		}
		Long actual_key = (Long)key_list.get(0);
		AllWordsTest awt_test_word = setupAWT(wltd.getWord(actual_key.toString()), type);
		session.setAttribute("awt_test_word", awt_test_word);
		session.setAttribute("actual_key_list", key_list);
		session.setAttribute("wltd", wltd);
		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("all_words_test"));
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
