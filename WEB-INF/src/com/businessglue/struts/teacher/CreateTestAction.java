package com.businessglue.struts.teacher;

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
import org.catechis.dto.WordCategory;
import org.catechis.dto.WordNextTestDate;
import org.catechis.dto.WordTestDates;
import org.catechis.dto.WordTestResult;
import org.catechis.file.FileCategories;
import org.catechis.file.FileSaveTests;
import org.catechis.file.FileTestRecords;
import org.catechis.file.WordNextTestDates;
import org.catechis.dto.WordLastTestDates;
import org.catechis.admin.FileUserOptions;
import org.catechis.admin.FileUserUtilities;
import org.catechis.constants.Constants;

import com.businessglue.struts.testing.CreateDailyTestForm;
import com.businessglue.struts.testing.CreateIntegratedTestForm;

import java.io.File;
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
Create a set of saved tests for each student in a class.
They can be reading, writing, both or separate.
Both means two tests integrated reading and writing in one test,
separate means two separate tests of the same length.
The number of words for each test can be an integer,
all, or lcd, which stands for lowest common denominator.
The SavedTest object holds the info that is shared for all
the tests in the class.

<daily_test>
	<test_id></test_id>
	<test_name></test_name>
	<test_type></test_type>
	<creation_time></creation_time>
	<test_item>
		<index></index>
		<id></id>
		<category></category>
	</test_item>
</daily_test>
*/
public class CreateTestAction extends Action
{
	
	private final String DEBUG_TAG = "CreateTestAction";
	
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
		context.log(DEBUG_TAG+".perform: welcome to hell.");
		String teacher_id = (String)session.getAttribute("teacher_id");
		String class_id = (String)session.getAttribute("class_id");
		Hashtable students_old = (Hashtable)session.getAttribute("students");
		Vector students_names_old = (Vector)session.getAttribute("students_names");
		Hashtable students = (Hashtable)session.getAttribute("students"); // get the class
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable teacher_opts = store.getTeacherOptions(teacher_id, context_path);
		String encoding = (String)teacher_opts.get("encoding");
		String subject = Constants.VOCAB;
		CreateIntegratedTestForm f = (CreateIntegratedTestForm) form;
		String test_name = f.getTestName();
		String test_type = whatItIs(f.getTestType());
		context.log("returnn type: "+test_type);
		String number_of_words = f.getNumberOfWords();
		context.log(DEBUG_TAG+".perform: number_of_words "+number_of_words);
		String test_id = Domartin.getNewID()+""; // test_ids should all be the same
		String action_time = ""; // this will collect the last action time to be saved for the teacher
		SavedTest saved_test = new SavedTest();
		boolean lcd = false;
		String reading_id = Domartin.getNewID()+"";
		String writing_id = Domartin.getNewID()+"";
		for (Enumeration e = students.keys() ; e.hasMoreElements() ;) 
		{
			String student_id = (String)e.nextElement();
			String student_name = (String)students.get(student_id);
			FileUserUtilities fut = new FileUserUtilities(context_path);
			Hashtable student_names = new Hashtable();
			student_names.put(student_id, student_name); // create a test for the student and save it, maybe return the number of words?
			action_time = Long.toString(new Date().getTime());
			if (number_of_words.equals("lcd")&&test_type.equals("separate"))
			{
				separateLCDTests(students, student_id, encoding, test_name, teacher_id, class_id, 
						session, teacher_opts, mapping, request, response, Constants.READING, reading_id);
				separateLCDTests(students, student_id, encoding, test_name, teacher_id, class_id, 
						session, teacher_opts, mapping, request, response, Constants.WRITING, writing_id);
				lcd = true;
			} else if (test_type.equals("writing_stones"))
			{
				createWritingStonesTests(students, student_id, 
						encoding, test_name, 
						number_of_words, teacher_id, class_id, 
						session, teacher_opts, mapping, 
						request, response, test_id);
			} else
			{	
				WordNextTestDates wntds = new WordNextTestDates();
				if (number_of_words.equals("lcd"))
				{
					number_of_words = wntds.getLowestCommonDenomenator(students, context_path, subject, Constants.READING)+"";
				}
				Hashtable student_test_totals = new Hashtable(); // this will hold student ids - number of words in their test
				Hashtable student_test_ids = new Hashtable(); // this will hold ids - test_id pairs.
				Vector test_words = wntds.getTestWords(student_id, context_path, action_time, subject, test_type, number_of_words);
				context.log("student_id "+student_id+" student_name "+student_name+" test_words.size "+test_words.size());
				context.log("---- saved tests -------");
				dumpSavedTestsList(test_words, context);
				context.log("---- wntd log ----------");
				printLog(wntds.getLog(), context);
				FileSaveTests f_save_tests = new FileSaveTests();
				UserInfo user_info = new UserInfo(encoding, context_path, student_id, subject);
				
				f_save_tests.save(user_info, test_words, test_id); // save the test words
				
				student_test_totals.put(student_id, Integer.toString(test_words.size()));
				student_test_ids.put(student_id, test_id);
				saved_test = createSavedTest(test_type, test_name, test_id, "IntegratedTest", number_of_words);
				
				f_save_tests.addToSavedTestsList(user_info, saved_test, test_id); // create the entry & load all the saved tests and set them in the session so the jsp can display them
				
				session.setAttribute("student_test_totals", student_test_totals);
				session.setAttribute("student_test_ids", student_test_ids);
				session.setAttribute("saved_test", saved_test);
				I18NWebUtility.forceContentTypeAndLocale(request, response, teacher_opts);
			}
		}
		if (lcd)
		{
			saveTestForTeacher(class_id, reading_id, test_name+"_"+Constants.READING, teacher_id, action_time, encoding, context_path);
			saveTestForTeacher(class_id, writing_id, test_name+"_"+Constants.WRITING, teacher_id, action_time, encoding, context_path);
		} if (test_type.equals("writing_stones"))
		{
			long test_id_1 = Long.parseLong(test_id);
			long test_id_2 = test_id_1+1;
			long test_id_3 = test_id_2+1;
			saveTestForTeacher(class_id, test_id_1+"", test_name+"_1st", teacher_id, action_time, encoding, context_path);
			saveTestForTeacher(class_id, test_id_2+"", test_name+"_2nd", teacher_id, action_time, encoding, context_path);
			saveTestForTeacher(class_id, test_id_3+"", test_name+"_3rd", teacher_id, action_time, encoding, context_path);
		} else
		{
			saveTestForTeacher(class_id, test_id, test_name, teacher_id, action_time, encoding, context_path);
		}
		return (mapping.findForward("create_tests_result"));
	}
	
	/**
	 * Get all the test words for the tryptic game Writing Stones by getting double the number of 
	 * reading words passed in, splitting them in half and saving two separate tests with the title
	 * *name*_1st and *name*_2nd, then all the words in one test called *name*_3rd, with test ids
	 * as id, id+1, id+2.
	 * Save three tests, one set of reading words with the number of test words passed in, then
	 * @param students
	 * @param student_id
	 * @param encoding
	 * @param test_name
	 * @param number_of_words
	 * @param teacher_id
	 * @param class_id
	 * @param session
	 * @param teacher_opts
	 * @param mapping
	 * @param request
	 * @param response
	 * @param test_id
	 */
	private void createWritingStonesTests(Hashtable students, String student_id, 
			String encoding, String test_name, 
			String number_of_words, String teacher_id, String class_id,
			HttpSession session, Hashtable teacher_opts, ActionMapping mapping, 
			HttpServletRequest request, HttpServletResponse response, String test_id)
	{
		String method = "createWritingStonesTests";
		Hashtable student_test_totals = new Hashtable(); // this will hold student ids - number of words in their test
		Hashtable student_test_ids = new Hashtable(); // this will hold ids - test_id pairs.
		Hashtable student_names = new Hashtable();
		long test_id_1 = Long.parseLong(test_id);
		long test_id_2 = test_id_1+1;
		long test_id_3 = test_id_2+1;
		ServletContext context = getServlet().getServletContext();
		context.log("CreateTestAction.separateLCDTests");
		String context_path = context.getRealPath("/WEB-INF/");
		String subject = Constants.VOCAB;
		String action_time = Long.toString(new Date().getTime());
		WordNextTestDates wntds = new WordNextTestDates();
		int total_number_of_words = Integer.parseInt(number_of_words)*2;
		context.log(DEBUG_TAG+"."+method+" total_number_of_words = number_of_words * 2 equals "+total_number_of_words);
		int greatest_number_of_words = Integer.parseInt(wntds.getLowestCommonDenomenator(students, context_path, subject, Constants.READING)+"");
		if (total_number_of_words>greatest_number_of_words)
		{
			context.log(DEBUG_TAG+"."+method+" total_number_of_words>greatest_number_of_words");
			number_of_words = (greatest_number_of_words / 2)+"";
			context.log(DEBUG_TAG+"."+method+" reduced greatest_number_of_words / 2 = "+number_of_words);
		} else
		{
			context.log(DEBUG_TAG+"."+method+" greatest_number_of_words "+greatest_number_of_words+" is greater than total_number_of_words "+total_number_of_words );
		}
		context.log(DEBUG_TAG+"."+method+" number_of_words "+number_of_words);
		Vector test_words = wntds.getTestWords(student_id, context_path, action_time, subject, Constants.READING, total_number_of_words+"");
		Vector first_test_words = getPartTestWords(test_words, 0, Integer.parseInt(number_of_words));
		Vector second_test_words = getPartTestWords(test_words, Integer.parseInt(number_of_words), test_words.size());
		context.log(DEBUG_TAG+"."+method+" test_words "+test_words.size());
		context.log(DEBUG_TAG+"."+method+" first_test_words "+first_test_words.size()+" 0 to "+number_of_words);
		context.log(DEBUG_TAG+"."+method+" second_test_words "+second_test_words.size()+" "+number_of_words+" to "+test_words.size());
		FileSaveTests f_save_tests = new FileSaveTests();
		UserInfo user_info = new UserInfo(encoding, context_path, student_id, subject);
		f_save_tests.save(user_info, first_test_words, test_id); // save the test words
		f_save_tests.save(user_info, second_test_words, test_id_2+""); // save the test words
		f_save_tests.save(user_info, test_words, test_id_3+""); // save the test words
		context.log("FileSaveTests Log ==================");
		printLog(f_save_tests.getLog(), context);
		SavedTest saved_test_1 = createSavedTest(Constants.READING, test_name+"_1st", test_id_1+"", Constants.CARD_GAME, first_test_words.size()+"");
		SavedTest saved_test_2 = createSavedTest(Constants.READING, test_name+"_2nd", test_id_2+"", Constants.CARD_GAME, second_test_words.size()+"");
		SavedTest saved_test_3 = createSavedTest(Constants.READING, test_name+"_3rd", test_id+3+"", Constants.WRITING_STONES, test_words.size()+"");
		student_test_totals.put(student_id, Integer.toString(test_words.size()));
		student_test_ids.put(student_id, test_id);
		student_test_ids.put(student_id, test_id+1);
		f_save_tests.addToSavedTestsList(user_info, saved_test_1, test_id_1+"");
		f_save_tests.addToSavedTestsList(user_info, saved_test_2, test_id_2+"");
		f_save_tests.addToSavedTestsList(user_info, saved_test_3, test_id_3+"");
		printLog(f_save_tests.getLog(), context);
		session.setAttribute("student_test_totals", student_test_totals);
		session.setAttribute("student_test_ids", student_test_ids);
		session.setAttribute("saved_test", saved_test_1);
		//session.setAttribute("saved_test", reading_saved_test);
		I18NWebUtility.forceContentTypeAndLocale(request, response, teacher_opts);
	}
	
	/**
	 * Get the range of test words from start to finish and returns those words in the vector only.
	 * @param test_words
	 * @param start
	 * @param finish
	 * @return
	 */
	private Vector getPartTestWords(Vector test_words, int start, int finish)
	{
		String method = "getPartTestWords";
		Vector part_test_words = new Vector();
		for (int i = start; i < finish; i++)	
		{
			try
			{
				part_test_words.add(test_words.get(i));
			} catch (java.lang.ArrayIndexOutOfBoundsException aioob)
			{
				getServlet().getServletContext().log(DEBUG_TAG+"."+method+" aioob at "+i);
				break;
			}
		}
		return part_test_words;
	}
	
	/**
	 * This method is for the situation when a teacher chooses the lowest common denominator for
	 * the number of words, and 'separate' for the type of test.  So first we find the lowest common
	 * denominator for both types, then create the test words, as well as the saved test object which 
	 * has the information shared by all the saved test for each student in a class.
	 * @param students
	 * @param student_id
	 * @param encoding
	 * @param test_name
	 * @param test_id
	 * @param teacher_id
	 * @param class_id
	 * @param session
	 * @param teacher_opts
	 * @param mapping
	 * @param request
	 * @param response
	 */
	private String separateLCDTests(Hashtable students, String student_id, String encoding, String test_name, 
			String teacher_id, String class_id,
			HttpSession session, Hashtable teacher_opts, ActionMapping mapping, 
			HttpServletRequest request, 
			HttpServletResponse response, String type, String test_id)
	{
		Hashtable student_test_totals = new Hashtable(); // this will hold student ids - number of words in their test
		Hashtable student_test_ids = new Hashtable(); // this will hold ids - test_id pairs.
		Hashtable student_names = new Hashtable();
		ServletContext context = getServlet().getServletContext();
		context.log("CreateTestAction.separateLCDTests");
		String context_path = context.getRealPath("/WEB-INF/");
		String subject = Constants.VOCAB;
		String action_time = Long.toString(new Date().getTime());
		WordNextTestDates wntds = new WordNextTestDates();
		int number_of_words = wntds.getLowestCommonDenomenator(students, context_path, subject, type);
		context.log("number_of_"+type+"_words "+number_of_words);
		Vector test_words = wntds.getTestWords(student_id, context_path, action_time, subject, type, number_of_words+"");
		printWords(test_words);
		FileSaveTests f_save_tests = new FileSaveTests();
		context.log(type+" test_id "+test_id);
		UserInfo user_info = new UserInfo(encoding, context_path, student_id, subject);
		f_save_tests.save(user_info, test_words, test_id); // save the test words
		context.log("FileSaveTests Log ==================");
		printLog(f_save_tests.getLog(), context);
		SavedTest saved_test = createSavedTest(type, test_name+"_"+type, test_id, Constants.CARD_GAME, number_of_words+"");
		student_test_totals.put(student_id, Integer.toString(test_words.size()));
		student_test_ids.put(student_id, test_id);
		f_save_tests.addToSavedTestsList(user_info, saved_test, test_id);
		printLog(f_save_tests.getLog(), context);
		session.setAttribute("student_test_totals", student_test_totals);
		session.setAttribute("student_test_ids", student_test_ids);
		session.setAttribute("saved_test", saved_test);
		//session.setAttribute("saved_test", reading_saved_test);
		I18NWebUtility.forceContentTypeAndLocale(request, response, teacher_opts);
		return test_id;
	}
	
	private SavedTest createSavedTest(String test_type, String test_name, String test_id, 
			String test_format, String number_of_words)
	{
		ServletContext context = getServlet().getServletContext();
		context.log("FileSaveTests.createSavedTest: setting now "+number_of_words);
 	    String today_now = new Date().getTime()+"";
		SavedTest saved_test = new SavedTest();
		saved_test.setTestId(test_id);
		saved_test.setTestDate(today_now);
		saved_test.setTestName(test_name);
		saved_test.setTestType(test_type);
		saved_test.setTestStatus("pending");
		saved_test.setTestFormat(test_format);
		saved_test.setCreationTime(today_now);
		saved_test.setNumberOfWords(number_of_words);
		return saved_test;
	}
	
	private void saveTestForTeacher(String class_id, String test_id, String test_name, 
			String teacher_id, String creation_time, String encoding, String context_path)
	{  
		FileSaveTests fst = new FileSaveTests();
		UserInfo user_info = new UserInfo(encoding, context_path, teacher_id, Constants.VOCAB);
		fst.saveClassTest(class_id, test_id, test_name,
				creation_time, user_info);
		ServletContext context = getServlet().getServletContext();
		context.log("FST Log from saveTestForTeacher ==================");
		printLog(fst.getLog(), context);
	}
	
	private String whatItIs(String test_type)
	{
		String of_the_jedi = test_type;
		if (of_the_jedi == null)
		{
			of_the_jedi = Constants.READING;
		} else if (of_the_jedi.equals("both"))
		{
			of_the_jedi = Constants.READING_AND_WRITING;
		}
		return of_the_jedi;
	}
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("CreateTestAction.printLog: "+(String)log.get(i));
			i++;
		}
	}
	
	 private void dumpSavedTestsList(Vector tests_list, ServletContext context)
	    {
		    int i = 0;
		    while (i<tests_list.size())
		    {
		    	try
				{
		    		Word word = (Word)tests_list.get(i);
		    		context.log("------------ "+i);
		    		Test [] tests = word.getTests();
		    		Test test = tests[0];
					String type = test.getType();
					context.log(i+" "+type+" - "+word.getDefinition()+" "+word.getText());
				} catch (java.lang.NullPointerException npe)
				{
					context.log(i+" npe");
				}
			    i++;
		    }

	    }
	 
	 private void smokeHash(Hashtable hash, ServletContext context)
	 {
		    Enumeration keys = hash.keys();
		    while (keys.hasMoreElements())
		    {
			    String key = (String)keys.nextElement();
			    String val = (String)hash.get(key);
			    context.log(key+" - "+val);
		    }
	}
	 
	 public void printWords(Vector log)
	 {
		ServletContext context = getServlet().getServletContext();
		context.log("printWords ==================");
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			Word word = (Word)log.get(i);
			context.log(i+" - "+word.getText()+"	"+word.getDefinition());
			i++;
		}
		context.log("printWords ==================");
	 }

		

}
