package com.businessglue.struts.testing;

import org.catechis.Storage;
import org.catechis.Domartin;
import org.catechis.FileStorage;
import org.catechis.Transformer;
import org.catechis.JDOMSolution;
import org.catechis.I18NWebUtility;
import org.catechis.WordTestDateUtility;
import org.catechis.dto.Word;
import org.catechis.dto.AllWordsTest;
import org.catechis.dto.WordTestDates;
import org.catechis.dto.WordTestResult;
import org.catechis.file.FileTestRecords;
import org.catechis.dto.WordLastTestDates;
import org.catechis.constants.Constants;
import com.businessglue.struts.testing.CreateDailyTestForm;

import java.io.File;
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
*<p>To do:
done - get the user to input a unique name and type with add_category and list.jsp page forms.
- can we set the default to reading in the CreatDailyTestForm.java?
- is the r/w list going to fill the form?
- give the test an id.
- save some info about the whole test, such as creation date, type, etc.
- save the test with word order, word id/category, and ?
- the format could be like this:

<daily_test>
	<test_id></test_id>
	<test_type></test_type>
	<creation_time></creation_time>
	<test_item>
		<index></index>
		<id></id>
		<category></category>
	</test_item>
</daily_test>

- format the test and create options for the formatting.
- create an Action and methods for loading a test, scoring it, updating the files, etc.
- also need delete the tests later.
- sounds like a lot of work.  don;t think 'we're going to finish it all tonight...'
- Oh, and we changed WLTD and FileStorage for your Jukseong crew.

*<p>Based on the bloated and failing DailyTestAction, the worst but most important method this 'team'
* has ever written.  Unlike that class, this one doesn't need to keep track of tested words and remove them
* from the list.
*<p>Here are the notes for this inglorious mess:
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
<p>These are obtained by the user_name alone, which should be held in the users session.
<p>In the past, these were held in the session, and for the time being, they are still there
<p>So that the legacy code will continue to function.
<p>When the new code is complete, they will not be added to the session, so that more users
<p>Will not create a large burden on the virtual machines memory. 
 * @author Timothy Curchod
*/
public class CreateDailyTestAction extends Action
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
		String subject = "vocab";	// this is the only subject so far...
		//String user_name = (String)session.getAttribute("user_name");
		String user_id = (String)session.getAttribute("user_id");
		context.log("CreateDailyTestAction.perform: user_id "+user_id);
		String teacher_id = (String)session.getAttribute("teacher_id");
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		CreateDailyTestForm f = (CreateDailyTestForm) form;
		String test_name = f.getTestName();
		String test_type = f.getTestType();
		if (test_type == null)
		{
			test_type = Constants.READING;
			context.log("DailyTestAction.perform: USE DEFAULT TYPE ");
		}
		context.log("CreateDailyTestAction.perform: test_name "+test_name);
		context.log("CreateDailyTestAction.perform: test_type "+test_type);
		FileTestRecords ftr = new FileTestRecords(context_path);
		int grand_index = ftr.getTestsStatus(user_id, subject);	// this gets the the grand total of tests
		Vector elt_vector = getELTVector(user_opts, context);		// exclude-level-times eliminate recently tested words from the list
		int daily_test_index = 0;					// all words test index
		int daily_words_limit = 40;	// should be total number of words
		WordLastTestDates wltd = new WordLastTestDates();		// create new list of words to be tested
		wltd.setExcludeLevelTimes(elt_vector);
		wltd.setType(test_type);
		wltd.setLimitOfWords(daily_words_limit);
		// ArrayList key_list = wltd.getSortedWLTDKeys();  // not sure why this was used.
		ArrayList key_list = store.getWordLastTestDatesList(wltd, user_id, subject);
		printLog(wltd.getLog(), context);
		context.log("DailyTestAction.perform: key_list size "+key_list.size()); // is zero
		session.setAttribute("actual_key_list", key_list);
		session.setAttribute("type", test_type);
		session.setAttribute("wltd", wltd);
		session.setAttribute("grand_index", Integer.toString(grand_index));
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		if (teacher_id != null)
		{
			context.log("DailyTestAction.perform: go to teacher version");
			return (mapping.findForward("teacher_create_daily_test"));  // go to a teachers version
		}
		return (mapping.findForward("create_daily_test"));
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
