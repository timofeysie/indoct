package com.businessglue.struts;

import java.util.Date;
import java.util.Vector;
import java.lang.Integer;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.servlet.http.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;
import org.catechis.Storage;
import org.catechis.Transformer;
import org.catechis.FileStorage;
import org.catechis.EncodeString;
import org.catechis.file.FileTestRecords;
import org.catechis.dto.AllWordsTest;
import org.catechis.dto.ChangeTestForm;
import org.catechis.dto.WordTestMemory;
import org.catechis.dto.WordTestResult;
import org.catechis.dto.WordTestRecordOptions;
import org.catechis.I18NWebUtility;

/**
*<p>See perform action.
*@author Timothy Curchod
*/
public class ChangeUpdateAction extends Action
{
	
	/**
	*<p>We have to take the request parameter index=particular test word, retrieve
	* the save WordTestResult with using the index/id, and populate the WordTestForm
	* with the word and test particulars.
	<p>******************************************************************************
	*  ******  If the user chooses addAnser but not reverseGrade, or vice versa *****
	*  ******  then the jsp data files will be incongruent!!!!!!!!!!!!!!!!!!!!! *****
	*  ******************************************************************************
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		ServletContext context = getServlet().getServletContext();
		HttpSession session = request.getSession();
		String test_type = updateTestWordAndOrLevel(request, response, context, session);
		String test_index = (String)session.getAttribute("test_index");
		context.log("ChangeUpdateAction.perform test_index = "+test_index);
		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		if (test_index.equals("-1"))
		{
			return (mapping.findForward("daily_test_result"));
		}
		if (test_type.equals("writing"))
		{
			return mapping.findForward("test_result_writing");
		} else if (test_type.equals("reading"))
		{
			return mapping.findForward("test_result_reading");
		}
		return mapping.findForward("test_result");
	}
	
	/**
	*<p>Here we retrieve the sizable datum needed to perform the undo.
	*<p>IF the user has chosen add answer in the change_test.jsp, then that 
	* operation happens in the addAnswer method.
	*<p>IF the user has chosen reverse grade in the change_test.jsp, then that 
	* operation happens in the reverseGrade method.
	WordTestMemory
	private String category;  	***
	private String type;		***
	private String date;		***
	private String score;		SET LATer
	private String index;		*** -1
	private String number_correct;	?
	private String level;		?
	private String test_name;	needed 
	
	<p>WordTestResult
	private String text;
	private String definition;
	private String answer;
	private String grade;
	private String level;
	private int id;
	private String original_text;
	private String original_definition;
	private String original_level;
	private String encoding;
	private String date;
	*/
	private String updateTestWordAndOrLevel(HttpServletRequest request, HttpServletResponse response, ServletContext context, HttpSession session)
	{
		// JSP data
		Hashtable test_file = (Hashtable)session.getAttribute("words_defs");
		Hashtable word_grades = (Hashtable)session.getAttribute("word_grades");
		Hashtable text_answers = (Hashtable)session.getAttribute("text_answers");
		// Additional data used to make the undo
		String add_answer = (String)request.getParameter("addAnswer");
		String reverse_grade = (String)request.getParameter("reverseGrade");
		Vector word_test_results = (Vector)session.getAttribute("word_test_results");
		String user_name = (String)session.getAttribute("user_name");
		String encoding = (String)session.getAttribute("encoding");
		String grade = (String)request.getParameter("grade");
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_name, context_path);
		String text = EncodeString.encodeThis((String)request.getParameter("text"), encoding);
		String definition = EncodeString.encodeThis((String)request.getParameter("definition"), encoding);
		String answer = EncodeString.encodeThis((String)request.getParameter("answer"), encoding);
		String test_type = (String)session.getAttribute("test_type");
		int test_index = Integer.parseInt((String)session.getAttribute("test_index"));
		WordTestResult word_test_result = new WordTestResult();
		WordTestMemory word_test_memory = new WordTestMemory();
		AllWordsTest awt = (AllWordsTest)session.getAttribute("awt_test_word");
		if (test_index == -1)
		{
			word_test_result = (WordTestResult)session.getAttribute("wtr");
			word_test_memory.setType(awt.getTestType());		// a test_index of -1 indicates a single word test.
			word_test_memory.setCategory(awt.getCategory());
			word_test_memory.setDate(word_test_result.getDate()); // this is used to locate the test in test.records
			word_test_memory.setIndex("-1");
			String test_name = ("level "+word_test_result.getLevel()+" "+awt.getTestType()+".test");
			word_test_memory.setTestName(test_name);
			FileTestRecords ftr = new FileTestRecords(context_path);
			WordTestRecordOptions wtro = new WordTestRecordOptions(test_type, user_name, context_path, user_opts);
			ftr.reverseTestRecord(word_test_result, wtro);		// delete or add a daily test.records entry
		} else
		{
			word_test_result = (WordTestResult)word_test_results.elementAt(test_index);
			word_test_memory = (WordTestMemory)session.getAttribute("word_test_memory");
		}
		String org_text = EncodeString.encodeThis(word_test_result.getOriginalText(), encoding);
		String org_def  = EncodeString.encodeThis(word_test_result.getOriginalDefinition(), encoding);
		String percentage_score = word_test_memory.getScore();  // set default
		String new_word_level = word_test_result.getOriginalLevel();   // set default
		//outputOriginalData(context, test_file, word_grades, text_answers, word_test_result, word_test_memory);
		if (add_answer.equals("yes"))
		{
			/*
			Hashtable results = addAnswer(test_file, text_answers, session, word_test_result, word_test_memory, 
			org_text, store, text, encoding, definition, answer, user_name);
			word_test_result = (WordTestResult)results.get("wtr");
			test_file = (Hashtable)results.get("tf");
			*/
		}
		if (reverse_grade.equals("yes"))
		{
			Hashtable results = reverseGrade(word_grades, word_test_result, word_test_memory, store, user_name, 
			org_text, org_def, text, definition, grade, percentage_score, test_type, context, encoding);
			word_test_memory = (WordTestMemory)results.get("wtm");
			word_test_result = (WordTestResult)results.get("wtr");
			percentage_score = (String)results.get("ps");
			new_word_level = (String)results.get("nwl");
		}
		context.log("---- new word_test_memory");
		dumpLog(Transformer.createTable(word_test_memory), context);
		context.log("---- new word_test_result");
		dumpLog(Transformer.createTable(word_test_result), context);
		context.log("---- percentage_score "+ percentage_score);
		//context.log("---- new_word_level "+new_word_level);
		//text_answers = resetTextAnswers(text_answers, org_text, text, org_def, definition, test_type, answer, encoding);
		word_grades = resetWordGrades(org_text, org_def, text, definition, new_word_level, test_type, word_grades, encoding, test_index);
		context.log("---- new_word_level "+new_word_level);
		//test_file   = resetTestFile(test_file, org_text, text, definition, encoding);
		//word_test_results = resetWordTestResults(word_test_result, word_test_results, test_index);
		//outputChangedData(context, test_file, word_grades, text_answers, word_test_result, word_test_memory);
		setAttributes(test_file, text_answers, word_grades, word_test_result, word_test_memory, 
			percentage_score, session, word_test_results);
		context.log("ChangeUpdateAction.updateTestWordAndOrLevel returning");
		return test_type;
	}
	
	private Hashtable resetTextAnswers(Hashtable text_answers, String org_text, String text, 
		String org_def, String definition, String test_type, String answer, String encoding)
	{
		text_answers.remove(text);
		text_answers.put(EncodeString.encodeThis(text, encoding), answer);
		return text_answers;
	}
	
	private Hashtable resetWordGrades(String orig_text, String orig_def, String text, String definition, 
		String new_word_level, String test_type, Hashtable word_grades, String encoding, int test_index)
	{
		if (test_index == -1)
		{
			return word_grades;
			//context.log("ChangeUpdateAction.resetWordGrades returning with test_index -1");
		}
		if (test_type.equals("writing"))
		{
			//word_grades.remove(text);
			//word_grades.put(EncodeString.encodeThis(text, encoding), new_word_level);
			word_grades.put(text, new_word_level);
		} else
		{
			//word_grades.remove(definition);
			//word_grades.put(EncodeString.encodeThis(definition, encoding), new_word_level);
			word_grades.put(definition, new_word_level);
		}
		return word_grades;
	}
	
	private Hashtable resetTestFile(Hashtable test_file, String org_text, String text, String definition, String encoding)
	{
		test_file.remove(org_text);
		test_file.put(EncodeString.encodeThis(text, encoding), EncodeString.encodeThis(definition, encoding));
		return test_file;
	}
	
	private Vector resetWordTestResults(WordTestResult word_test_result, Vector word_test_results, int test_index)
	{
		word_test_results.remove(test_index);
		word_test_results.add(test_index, word_test_result);
		return word_test_results;
	}
	
	/**
	*<p>***This method is disable right now because the text and answer fields get
	* unencoded somehow.***
	*<p>Add answer needs a god-awful amount of arguments to do its work.  But thats what we 
	* assume it takes to keep methods close to being under 20 lines each, a recommendations of
	* good programming style.  Basically we replace the text-definition values of a word
	* with those from the jsp page.
	*<p>And shouldn't the new_word_level be taken care of in the reverseGrade method?
	*It's true that the word info is available thru the updateWord method, but thats still presumptuous.
	*/
	private Hashtable addAnswer(Hashtable test_file, Hashtable text_answers, HttpSession session, 
		WordTestResult word_test_result, WordTestMemory word_test_memory, String org_text, 
		Storage store, String text, String encoding, String definition, String answer, String  user_name)
	{
		Hashtable results = new Hashtable();
		word_test_result.setText(EncodeString.encodeThis(text, encoding));
		word_test_result.setDefinition(EncodeString.encodeThis(definition, encoding));
		word_test_result.setAnswer(EncodeString.encodeThis(answer, encoding));
		store.updateWord(word_test_memory, word_test_result, user_name, encoding);
		results.put("wtr", word_test_result);
		results.put("tf", test_file);
		return results;
	}
	
	/**
	*<p>
	*<p>This method finds the pass or fail score of the particular word, and has to change
	* both the words reading or writing level and the overall percentage score which needs
	* to be re-calculated from the number of words tested, and the number of words correct
	* held in the i_score variable.
	*<p>
	String percentage = new String();
		String new_word_level = new String();
		if (test_index == -1)
	*/
	private Hashtable reverseGrade(Hashtable word_grades, WordTestResult word_test_result, WordTestMemory word_test_memory, Storage store, 
		String user_name, String org_text, String org_def, String text, String definition, String grade, 
		String percentage_score, String test_type, ServletContext context, String encoding)
	{
		String percentage = new String();
		//String new_word_level = new String();
		if (word_test_memory.getIndex() == "-1")
		{
			word_test_memory.setScore("-1");
			word_grades = new Hashtable();
			if (grade.equals("pass"))
			{
				//i_score--;
				word_test_result.setGrade("fail");
			}
			else
			{
				//i_score++;
				word_test_result.setGrade("pass");
			}
		} else
		{
			Double dou_index = new Double(word_test_memory.getIndex());
			double d_index = dou_index.doubleValue();
			int i_score = Integer.parseInt(word_test_memory.getScore());
			if (grade.equals("pass"))
			{
				i_score--;
				word_test_result.setGrade("fail");
			}
			else
			{
				i_score++;
				word_test_result.setGrade("pass");
			}
			Double dou_score = new Double(Integer.toString(i_score));
			double d_score = dou_score.doubleValue();
			org.catechis.Scoring scorer = new org.catechis.Scoring();
			percentage = scorer.getPercentageScore(d_index, d_score);
			String number_correct = Integer.toString(i_score);
			word_test_memory.setScore(number_correct);
			store.updateTest(word_test_memory, Double.toString(d_score), user_name); 
		}
		String new_word_level = store.updateWordLevel(word_test_memory, word_test_result, user_name, encoding);
		word_test_result.setLevel(new_word_level);
		context.log("ChangeUpdateAction.reverseGrade");
		dumpLog(store.getLog(), context);
		return setReverseGradeRsults(word_test_memory, word_test_result, percentage, word_grades, new_word_level, context);
	}
		
	private Hashtable setReverseGradeRsults(WordTestMemory word_test_memory, WordTestResult word_test_result, 
		String percentage, Hashtable word_grades, String new_word_level, ServletContext context)
	{
		Hashtable results = new Hashtable();
		results.put("wtm", word_test_memory);
		results.put("wtr", word_test_result);
		results.put("ps", percentage);
		results.put("wg", word_grades);
		results.put("nwl", new_word_level);
		return results;
	}
	
	/**
	*This method sets the changed data back into the session.
	*/
	private void setAttributes(Hashtable test_file, Hashtable text_answers, Hashtable word_grades,
		WordTestResult word_test_result, WordTestMemory word_test_memory, 
		String percentage_score, HttpSession session, Vector word_test_results)
	{
		session.setAttribute("word_test_result", word_test_result);
		session.setAttribute("word_test_memory", word_test_memory);
		session.setAttribute("test_score", percentage_score);
		session.setAttribute("words_defs", test_file);
		session.setAttribute("word_grades", word_grades);
		session.setAttribute("text_answers", text_answers);
		session.setAttribute("word_test_results", word_test_results);
	}
	
	// ----- Debugging methods ----------------------------
	
	private void dumpLog(Vector log, ServletContext context)
	{
	    int i = 0;
	    while (i<log.size())
	    {
		    context.log("ChangeUpdateAction.log "+i+" "+log.get(i));
		    i++;
	    }
	}
	
	/**Vector test_fields replaced with Hashtable text_answers*/
	private void outputOriginalData(ServletContext context, 
		Hashtable test_file, Hashtable word_grades, Hashtable text_answers,
		WordTestResult word_test_result, WordTestMemory word_test_memory)
	{
		context.log("ChangeUpdateAction.log: original test file -----------");dumpHash(test_file, context);
		context.log("ChangeUpdateAction.log: original word grades -----------");dumpHash(word_grades, context);
		context.log("ChangeUpdateAction.log: original text answers -----------");dumpHash(text_answers, context);
		context.log("ChangeUpdateAction.log.: original wtm -----------");dumpLog(Transformer.createTable(word_test_memory), context);
		context.log("ChangeUpdateAction.log.: original wtr -----------");dumpLog(Transformer.createTable(word_test_result), context);
	}
	
	private void outputChangedData(ServletContext context, 
		Hashtable test_file, Hashtable word_grades, Hashtable text_answers,
		WordTestResult word_test_result, WordTestMemory word_test_memory)
	{
		context.log("ChangeUpdateAction.log: changed test file -----------");dumpHash(test_file, context);
		context.log("ChangeUpdateAction.log: changed word grades -----------");dumpHash(word_grades, context);
		context.log("ChangeUpdateAction.log: changed text answers -----------");dumpHash(text_answers, context);
		context.log("ChangeUpdateAction.log: changed wtm -----------");dumpLog(Transformer.createTable(word_test_memory), context);
		context.log("ChangeUpdateAction.log: changed wtr -----------");dumpLog(Transformer.createTable(word_test_result), context);	
	}
	
	private void dumpHash(Hashtable hash, ServletContext context)
	{
	    Enumeration keys = hash.keys();
	    while (keys.hasMoreElements())
	    {
		    String key = (String)keys.nextElement();
		    String val = (String)hash.get(key);
		    context.log("ChangeUpdateAction.log "+key+" - "+val);
	    }
	}

}

			/*
			We replaced the test_fields Vector with a text_answers Hashtable.
			
			This was really frustrating.  After getting stuck on this for a few weeks
			we commented it out.  It turned out the way the test_results jsp matches
			answers (in the test_fields vector) with words_defs cannot be re-created
			easily.
			
			Hashtable test_file = (Hashtable)session.getAttribute("words_defs");
			Hashtable word_grades = (Hashtable)session.getAttribute("word_grades");
			Vector test_fields = (Vector)session.getAttribute("test_fields");
			Enumeration keys = test_file.keys();
		
			reading
			=======
			String original = new String((String)test_fields.elementAt(test_i));
			String key = (String)keys.nextElement();
			String value = (String)test_file.get(key);
			String word_grade = (String)word_grades.get(value);
			
			writing
			=======
			String key = (String)keys.nextElement();
			String value = (String)test_file.get(key);
			
			if (value.equalsIgnoreCase(original)) red / green
			test_file(key)=value == test_fields.elementAt(test_i);
			
			if (test_type.equals("reading"))
			{
				Hashtable results = addAnswer(test_file, test_fields, session, word_test_result, word_test_memory, 
				org_text, store, text, encoding, definition, answer, user_name);
				word_test_result = (WordTestResult)results.get("wtr");
				test_file = (Hashtable)results.get("tf");
			} else
			{
				// the text element gets un-encoded, so until we can solve this,
				// only reading tests are modifiable
			}
			*/
