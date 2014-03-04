package com.businessglue.struts;

import java.io.File;
import java.util.Date;
import java.util.Vector;
import java.util.Locale;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.lang.Float;
import java.lang.ArrayIndexOutOfBoundsException;
import java.text.DateFormat;
import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.catechis.FileStorage;
import org.catechis.Scoring;
import org.catechis.Storage;
import org.catechis.I18NWebUtility;

import org.apache.struts.action.*;
import com.businessglue.util.Domartin;
import com.businessglue.indoct.UserBean;
import com.businessglue.indoct.TestBean;
import com.businessglue.struts.TestForm;
import com.businessglue.util.EncodeString;
import com.businessglue.util.CreateJDOMList;

import org.catechis.dto.WordTestResult;
import org.catechis.dto.WordTestMemory;

// This is for debuggin
import org.catechis.Transformer;

import org.apache.commons.lang.StringUtils;

/**
*<p>This Action grades the test, both as a percentage out of one hundred, and as a
* pass/fail element added to each word element in the tested word file.
* Each word is given a level based on the type of test, such that 'writing-level' is
* created by adding the test type to '-level'.  If this element exists, then it is incremented.
*<p>Warning:  This action is not thread safe!
* </p>
<dl>
	<dt>To Do:</dt>
		<dd>Test resutls</dd>
			<ul>
				<li>if there is a repeat answer, both should be wrong</li>
				<li>give hints about word types (v., adv., conj., inf., etc.</li>
				<li>create a combined index of all levels.</li>
			</ul>
		<dd>Visualizations</dd>
		<dd>What to do with the percentage results?</dd>
		<dd>Seperaste file for all results?</dd>
		<dd>Find a better way to do the getFields method</dd>
</dl>
*/
public class TestResultAction extends Action
{
	// These object are used to add test results to each word in the test file
	private HttpSession session; 
	private TestBean test_bean;
	private UserBean user_bean;
	/** Name of the file that was tested */
	private String file_name;
	private File test_file;
	private String full_test_file_name;
	/** Name of the test properties and results file */
	private String test_name;
	private File test_test;
	private String full_test_test_name;
	private String user_path;
	private String date;
	private CreateJDOMList words_jdom;
	private ServletContext context;
	private CreateJDOMList score_jdom;
	private Hashtable word_grades;
	private Hashtable test_hash;
	private Date full_date;
	private String test_type;
	private Vector word_test_results;
	private WordTestMemory word_test_memory;
	
	// loggin
	private static String original;
	private static byte[] utf8Bytes;
	private static String tempString;
	
	private String new_index;
	
	/**
	*This method takes care of grading the test by creating a Vector answer_fileds
	* (later called test_fileds) which turned out to be inappropriate when trying
	* to reverse a score for a particular word, so we created a Hashtable called
	* text_answers to correlate the text-def-answer combo once something has
	* changed for instance if the user made a type with the original text or def entry.
	*<p>We used a _ (underscore) for the other Hashtable text_answer so that we
	* can tell it is an argument passed into a private method.  Previously the
	* methods in the Action are **not thread safe** because they use member global
	* variables instead of passing around the original.
	* context.log("TestResultAction.perform: ---------------- 1");
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		session = request.getSession();
		context = getServlet().getServletContext(); 
		word_test_memory = new WordTestMemory();
		TestForm test_form = (TestForm)form;
		Vector answer_fields = getFields(test_form);
		test_hash = (Hashtable)session.getAttribute("words_defs");
		test_type = (String)session.getAttribute("test_type");
		new_index = (String)session.getAttribute("new_index");
		String test_cat = (String)session.getAttribute("test_file_name");
		Enumeration test_keys = test_hash.keys();
		setUpForTestRecording();
		word_test_results = new Vector();
		setWordTestMemory(test_type, test_cat, date);
		String string_score = gradeTest(test_keys, answer_fields, test_hash);
		recordGrade(string_score);
		Vector grades = getAllGrades();
		Hashtable text_answers = getTextAnswers(answer_fields, test_hash);
		setAttributes(grades, answer_fields, string_score, text_answers);
		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		if (test_type.equals("writing"))
		{return (mapping.findForward("test_result_writing"));}
		else if (test_type.equals("reading"))
		{return (mapping.findForward("test_result_reading"));}
		else
		{return (mapping.findForward("test_result"));}
	}
	
	/** java.util.NoSuchElementException */
	private Hashtable getTextAnswers(Vector _answer_fields, Hashtable _test_hash)
	{
		Hashtable _text_answers = new Hashtable();
		Enumeration keys = _test_hash.keys();
		int index = 0;
		int size = _answer_fields.size();
		while (index<size)
		{
			try
			{
				String original = new String((String)_answer_fields.elementAt(index));
				String key = (String)keys.nextElement();
				_text_answers.put(key, original);
				index++;
			} catch (NoSuchElementException nsee)
			{
				break;
			}
		}
		return _text_answers;
	}
	
	
	/**
	*<p>Here we grade the test and create a score.</p>
	*<p>We also have to add a test element to the word file tested,
	* one for each word.  To do this a new jdom object is created,
	* which requires the user_bean and the test_bean.</p>
	*<p>This method could be a performance bottle neck,
	* so we use the setUpForTestRecording method to re-use objects.</p>
	*<p>If it is a reading test, then the key from the test_hash 
	* represents the text element, and the question represents the definition
	* of the word held in the text element (text-definition = key-question, 
	* ie: key/test-answer should be the same for a correct answer).
	*<p>If it is a writing test, then we dont need to know the key, so the
	* question is the definition, and the answer is the answer from the test form.
	* (ie: definition-answer should be the same for a correct answer)
	*<p>The addWordTestResult is used to collect info so that the results can
	* be reversed later.  It may seem redundant to record the text property twice, but
	* the original_text property is used later to find the word again, when both may
	* have been changed.  What we go thru when we dont use a db and have no primary indexes...
	*<p>
	*/
	private String gradeTest(Enumeration test_keys, Vector answer_fields, Hashtable test)
	{
		int score = 0;int index = 0;
		String question = new String();String answer = new String();
		String text = new String(); String def = new String();
		Hashtable user_options = (Hashtable)session.getAttribute("user_options");
		while (test_keys.hasMoreElements())
		{
			try
			{
				// writing: def/question - def/answer
				if (test_type.equals("writing"))
				{
					question = (String)test_keys.nextElement();
					answer = (String)answer_fields.elementAt(index);
					def = (String)test.get(question);
					text = question;
				// reading: text/key - answer/def
				} else if (test_type.equals("reading"))
				{
					String key = (String)test_keys.nextElement();
					question = (String)test.get(key);
					answer = (String)answer_fields.elementAt(index);
					text = key;
					def = question;
				}
				String modified_question = applyOptions(user_options, question);
				String modified_answer = applyOptions(user_options, answer);
				String max_level = (String)user_options.get("max_level");
				if (modified_question.equalsIgnoreCase(modified_answer))
				{
					score = score + 1;
					words_jdom.recordWordScore(question, "pass", test_name, date, test_type, max_level);
					String org_level = words_jdom.getOriginalLevel();
					String new_level = words_jdom.getNewLevel();
					addWordTestResults(text, def, answer, "pass", index, org_level, new_level);
				}
				else
				{
					words_jdom.recordWordScore(question, "fail", test_name, date, test_type, max_level);
					String org_level = words_jdom.getOriginalLevel();
					String new_level = words_jdom.getNewLevel();
					addWordTestResults(text, def, answer, "fail", index, org_level, new_level);
				}
				index++;
			}
			catch (java.lang.ArrayIndexOutOfBoundsException e)
			{break;}
		}
		org.catechis.Scoring scorer = new org.catechis.Scoring();
		double d_index = (double)index;
		double d_score = (double)score;
		context.log("TestResultAction.gradeTest: score "+score);
		String percentage_score = scorer.getPercentageScore(d_index, d_score);
		word_test_memory.setScore(Integer.toString(score));
		word_test_memory.setIndex(Integer.toString(index));
		word_test_memory.setTestName((String)session.getAttribute("test_name"));
		return percentage_score;
	}
	
	private String applyOptions(Hashtable user_options, String text)
	{
		String grade_whitespace = (String)user_options.get("grade_whitespace");
		if (grade_whitespace.equals("false"))
		{
			text = StringUtils.deleteWhitespace(text); 
		}
		return text;
	}
	
	private Vector getFields(TestForm test_form)
	{
		String enc = new String("euc-kr");
		Vector retrieved_fields = new Vector();
		String field0 = EncodeString.encodeThis(test_form.getField0(),enc);
		retrieved_fields.add(field0);
		String field1 = EncodeString.encodeThis(test_form.getField1(),enc);
		retrieved_fields.add(field1);
		String field2 = EncodeString.encodeThis(test_form.getField2(),enc);
		retrieved_fields.add(field2);
		String field3 = EncodeString.encodeThis(test_form.getField3(),enc);
		retrieved_fields.add(field3);
		String field4 = EncodeString.encodeThis(test_form.getField4(),enc);
		retrieved_fields.add(field4);
		String field5 = EncodeString.encodeThis(test_form.getField5(),enc);
		retrieved_fields.add(field5);
		String field6 = EncodeString.encodeThis(test_form.getField6(),enc);
		retrieved_fields.add(field6);
		String field7 = EncodeString.encodeThis(test_form.getField7(),enc);
		retrieved_fields.add(field7);
		String field8 = EncodeString.encodeThis(test_form.getField8(),enc);
		retrieved_fields.add(field8);
		String field9 = EncodeString.encodeThis(test_form.getField9(),enc);
		retrieved_fields.add(field9);
		return retrieved_fields;
	}
	
	/**
	*<p>When we write the file, we are writing the test_file, as opposed to the word_file which
	* is only written after all the words have had their pass/fail elements added.</p>
	*/
	private void recordGrade(String score)
	{
		String encoding = (String)session.getAttribute("encoding");
		score_jdom = new CreateJDOMList(test_test, context);
		score_jdom.addElement("score", "grade", score, "date", date);
		score_jdom.setElement("option", "name", "index", "value", new_index);
		score_jdom.writeDocument(full_test_test_name);
		words_jdom.writeDocument(full_test_file_name, encoding);
	}
	
	/**
	* word_grades is the only global variable not needed to be pass in.
	*/
	private void setAttributes(Vector _grades, Vector _answer_fields, 
				   String _string_score, Hashtable _text_answers)
	{
		session.setAttribute("test_grades", _grades);
		session.setAttribute("test_fields", _answer_fields);
		session.setAttribute("test_score", _string_score);
		session.setAttribute("word_grades", word_grades);
		dumpGrades(Transformer.createTable(word_test_memory));
		session.setAttribute("word_test_memory", word_test_memory);
		session.setAttribute("word_test_results", word_test_results);
		session.setAttribute("text_answers", _text_answers);
	}

	/**
	*<p>In this method we get all the percentage scores from the test file,
	* and each individual word-level-achieved from the tested word file.</p>
	*<p>We only return the grades, and use the word_grades 'global' variable
	* to pass back the word-level scores.</p>
	*/
	private Vector getAllGrades()
	{
		Vector grades = score_jdom.getWhateverVector("score", "date", "grade");
		word_grades = words_jdom.getWordLevels();
		return grades;
	}

	private void setUpForTestRecording()
	{
		test_bean = (TestBean)session.getAttribute("TEST_KEY");
		user_bean = (UserBean)session.getAttribute("USER_KEY");
		file_name = (String)test_bean.getTestFileName();
		test_name = (String)test_bean.getFileName();
		user_path = (String)user_bean.getPath();
		test_file = Domartin.createFileFromUserNPath(file_name, user_path);
		test_test = Domartin.createFileFromUserNPath(test_name, user_path);
		full_test_file_name = test_file.getAbsolutePath();
		full_test_test_name = test_test.getAbsolutePath();
		words_jdom = new CreateJDOMList(test_file, context);
		full_date = new Date();
		date = full_date.toString();
	}
	
	/**Create a new WordTestMemory and set it with important info to change the
	* test results if the user wants to after seeing the test results, ie: if
	* there is a typo in the text-def, etc.
	*/
	private void setWordTestMemory(String test_type, String test_cat, String date)
	{
		//WordTestMemory wtm = new WordTestMemory();
		word_test_memory.setType(test_type);
		word_test_memory.setCategory(test_cat);
		word_test_memory.setDate(date);
		//return wtm;
	}
	
	// (text, def, answer, "fail", index)
	private void addWordTestResults(String text,String def,String answer,String grade, int index, String org_level, String new_level)
	{
		WordTestResult wtr = new WordTestResult();
		wtr.setText(text);
		wtr.setDefinition(def);
		wtr.setAnswer(answer);
		wtr.setGrade(grade);
		wtr.setLevel(new_level);
		wtr.setId(index);
		wtr.setOriginalText(text);
		wtr.setOriginalDefinition(def);
		wtr.setOriginalLevel(org_level);
		word_test_results.add(wtr);
	}
	
	private void dumpGrades(Vector grades)
	{
		ServletContext context = getServlet().getServletContext();
		int num_of_grades = grades.size();
		for(int i = 0; i<num_of_grades; i++) 
		{
			context.log("TestResultAction: "+(String)grades.get(i));
		}
	}

}
