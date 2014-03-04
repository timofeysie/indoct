package com.businessglue.struts.remote;

import org.catechis.EncodeString;
import org.catechis.Storage;
import org.catechis.Domartin;
import org.catechis.FileStorage;
import org.catechis.Transformer;
import org.catechis.JDOMSolution;
import org.catechis.I18NWebUtility;
import org.catechis.WordTestDateUtility;
import org.catechis.dto.Momento;
import org.catechis.dto.SavedTest;
import org.catechis.dto.Test;
import org.catechis.dto.UserInfo;
import org.catechis.dto.Word;
import org.catechis.dto.AllWordsTest;
import org.catechis.dto.WordNextTestDate;
import org.catechis.dto.WordTestDates;
import org.catechis.dto.WordTestResult;
import org.catechis.file.FileSaveTests;
import org.catechis.file.FileTestRecords;
import org.catechis.file.WordNextTestDates;
import org.catechis.dto.WordLastTestDates;
import org.catechis.constants.Constants;
import com.businessglue.struts.testing.CreateDailyTestForm;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
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
Send a list of words from a saved test.
Test words are saved like this:

<test_words>
	<test_word>
		<index>0</index>
		<word_id>-4011783267950267722</word_id>
		<category>2010 Fall Random.xml</category>
		<text>žžŸà</text>
		<definition>in case</definition>
		<testing_type>reading</testing_type>
		<wntd_file_key>1341142256335</wntd_file_key>
		<deck_card_name>
	</test_word>
	
	The test type is held in a single blank test under they type member.
	The word index is held in the reading type member variable.
*/
public class GetTestWordsAction extends Action
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
		String player_id = request.getParameter("player_id");
		context.log("GetTestWordsAction.perform: user_id "+player_id+" or teacher_id ");
		String context_path = context.getRealPath("/WEB-INF/");
		String test_id = request.getParameter("test_id");
		String number_of_words = request.getParameter("number_of_words");
		Storage store = new FileStorage(context_path, context);
		FileSaveTests f_save_tests = new FileSaveTests();
		Hashtable user_opts = user_opts = store.getUserOptions(player_id, context_path);
		FileTestRecords ftr = new FileTestRecords(context_path);
		String subject = (String)user_opts.get("subject");
		String encoding = (String)user_opts.get("encoding");	
		context.log("GetTestWordsAction.perform: encoding: "+encoding);
	    UserInfo user_info = new UserInfo(encoding, context_path, player_id, subject);
    	Hashtable test_words = f_save_tests.load(user_info, test_id);
    	String house_deck_name =  f_save_tests.getHouseDeckName();
    	String house_deck_id =  f_save_tests.getHouseDeckId();
    	context.log("test_words "+test_words.size()+" ==-=----=-== create table & log ==-=----=-== house_deck_name "+house_deck_name);
		printLog(Transformer.createTable(user_info), context);
		printLog(f_save_tests.getLog(), context);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		session.setAttribute("house_deck_name", house_deck_name);
		session.setAttribute("test_words", test_words);
		xmlResponse(response, test_words, encoding, number_of_words, house_deck_id);
		return (mapping.findForward("created_integrated_test_score"));
	}
	
	/**
	 * The information stored in the first test object is as follows:
	 * test.setType(testing_type);
	 * test.setDate(wntd_file_key);
	 * test.setGrade(grade);
	 * test.setName(deck_card_name);
	 * test.setLevel(deck_card_word_type);
	 * @param response
	 * @param test_words
	 * @param encoding
	 * @param number_of_word_string
	 */
	private void xmlResponse(HttpServletResponse response, Hashtable test_words, String encoding, 
			String number_of_word_string, String house_deck_id)
	{
		ServletContext context = getServlet().getServletContext(); 
		context.log("GetTestWordsAction.xmlResponse");
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\""+encoding+"\"?>");
		sb.append("<test_words>");
		sb.append("<house_deck_id>"+house_deck_id+"</house_deck_id>");
		int i = 0;
		int extra_length = 0;
		Enumeration e = test_words.keys();
		while (e.hasMoreElements())
		{
			String key = (String)e.nextElement();
			Word word = (Word)test_words.get(key);
			Test [] tests = word.getTests();
			Test test = tests[0];
			String type = test.getType();
			String wntd_file_key = test.getDate();
			String grade = 	test.getGrade();
			String reading_deck_card_name = test.getName();
			String writing_deck_card_name = test.getLevel();
			context.log("type "+type+" wntd_file_key "+wntd_file_key+" grade "+grade+" reading_deck_card_name "+reading_deck_card_name+" writing_deck_card_name "+writing_deck_card_name);
			sb.append("<test_word>");
			sb.append("<id>"+word.getId()+"</id>");
			sb.append("<text>"+EncodeString.encodeThis(word.getText())+"</text>");
			sb.append("<definition>"+EncodeString.encodeThis(word.getDefinition())+"</definition>");
			sb.append("<type>"+type+"</type>");
			sb.append("<category>"+word.getCategory()+"</category>");
			sb.append("<grade>"+grade+"</grade>");
			sb.append("<reading_deck_card_name>"+reading_deck_card_name+"</reading_deck_card_name>"); // yep, that's what we did
			sb.append("<writing_deck_card_name>"+writing_deck_card_name+"</writing_deck_card_name>"); // instead of creating a TestWord object.
			sb.append("<index>"+word.getReadingLevel()+"</index>");
			sb.append("</test_word>");
			extra_length = extra_length + word.getText().length();;
			i++;
		}
		sb.append("</test_words>");
		response.setContentType("text/xml");
		response.setContentLength(sb.length()+i); 
		context.log("GetTestWordsAction.xmlResponse: sb.length "+sb.length());
		String output = new String(sb);
		context.log("GetTestWordsAction.xmlResponse: output.length "+output.length());
		context.log("output: "+output);
		PrintWriter out;
		try 
		{
		    out = response.getWriter();
			out.println(sb.toString()); 
			out.close();
			out.flush();
		} catch (IOException ioe) 
		{
			context.log("Could not steal output stream");
		}
	}
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("CreateIntegratedTestScoreAction "+(String)log.get(i));
			i++;
		}
	}
	
	 private void dumpSavedTestsList(Vector tests_list, ServletContext context)
	    {
		    int i = 0;
		    while (i<tests_list.size())
		    {
			    Word word = (Word)tests_list.get(i);
			    context.log("------------ "+i);
			    Test [] tests = word.getTests();
				Test test = tests[0];
				String type = test.getType();
				System.out.println(i+" "+type+" - "+word.getDefinition()+" "+word.getText());
			    i++;
		    }

	    }

}
