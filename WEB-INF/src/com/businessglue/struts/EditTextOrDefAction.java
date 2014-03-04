package com.businessglue.struts;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import org.catechis.dto.Word;
import org.catechis.dto.Momento;
import org.catechis.dto.AllWordsTest;
import org.catechis.dto.WordTestResult;
import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import org.catechis.file.FileTestRecords;
import com.businessglue.util.EncodeString;

import java.util.Date;
import java.util.Vector;
import java.util.Hashtable;

/**
 * 
 * @author Timothy Curchod
 *
 */
public class EditTextOrDefAction extends Action
{
	
	private Vector log;
	
	/**
	*<p>This method came from EditWordAction and DailyTestAction, which is why
	the new text or def was called answer but is now edit.
	*
	private String text;
	private String definition;
	private String category;
	private String test_type;
	private String level;
	private int daily_test_index;
	private String answer;
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_name = (String)session.getAttribute("user_name");
		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_name, context_path);
		String encoding = (String)user_opts.get("encoding");
		String edit_type = (String)request.getParameter("type");	// text or def
		//String test_type = (String)request.getParameter("test_type");	// reading or writing - should be the same as wtr test_type
		//context.log("EditWordAction.perform: test_type "+test_type);
		context.log("EditWordAction.perform: edit_type "+edit_type);
		DailyTestForm test_form = (DailyTestForm)form; 		// test form used here to edit
		String edit = EncodeString.encodeThis(test_form.getAnswer(),encoding);
		AllWordsTest awt = (AllWordsTest)session.getAttribute("awt_test_word");	// this object holds the original word tested
		String test_type = awt.getTestType();
		WordTestResult wtr = (WordTestResult)session.getAttribute("wtr");
		context.log("EditWordAction.perform: awt test_type "+awt.getTestType());
		// set up for making the edit
		Word old_word = populateOldWord(awt);
		Word new_word = populateNewWord(awt, edit, edit_type, test_type);
		String file_name = awt.getCategory();
		try
		{
			store.editWord(old_word, new_word, file_name, user_name, encoding);
		} catch (java.lang.NullPointerException npe)
		{
			printLog(store.getLog(), context);
		}
		AllWordsTest new_awt = resetAWT(awt, edit, edit_type, test_type);
		WordTestResult new_wtr = resetWTR(wtr, edit, edit_type, test_type);
		session.setAttribute("awt_test_word", new_awt);
		session.setAttribute("wtr", new_wtr);
		String word_text_chosen = (String)session.getAttribute("word_chosen");
		context.log("EditWordAction.perform");
		context.log("f "+file_name);
		context.log("wtc "+word_text_chosen);
		Word word = store.getWordWithoutTests(word_text_chosen, file_name, user_name);
		session.setAttribute("word", word);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		// get the momento object to find out where the update came from
		String subject = (String)user_opts.get("subject");
		FileTestRecords ftr = new FileTestRecords(context_path);
		Momento old_m = ftr.getMomentoObject(user_id, subject);
		String action_name = old_m.getActionName();
		context.log("EditTextOrDefAction.perform: action_name "+action_name);
		//updateLevel(request, response, context, session, action_name);  what action did this come from?
		Momento new_m = new Momento("EditTextOrDefAction", Long.toString(new Date().getTime()), "default", "default");
		ftr = new FileTestRecords(context_path);
		ftr.setMomentoObject(user_id, subject, new_m);
		if (action_name.equals("IntegratedTestResultAction")||action_name.equals("ChangeUpdateScoreAction"))
		{
			return (mapping.findForward("integrated_test_result"));		
		}
		return (mapping.findForward("daily_test_result"));
	}
	
	private Word populateOldWord(AllWordsTest awt)
	{
		Word word = new Word();
		word.setText(awt.getText());
		word.setDefinition(awt.getDefinition());
		return word;
	}
	
	/**
	*0 	reading edit def 	reading edit text
	*<p>If this is a writing test, then the question is definition, or text, 
	* and the answer would be the text, which is the opposite of reading.
	*/
	private Word populateNewWord(AllWordsTest awt, String edit, String edit_type, String test_type)
	{
		Word word = new Word();
		if (test_type.equals("reading"))
		{
			if (edit_type.equals("text"))
			{
				word.setText(edit);
				word.setDefinition(awt.getDefinition());
			} else if (edit_type.equals("def"))
			{
				word.setText(awt.getText());
				word.setDefinition(edit);
			}
		} else if (test_type.equals("writing"))
		{
			if (edit_type.equals("text"))
			{
				word.setText(edit);
				word.setDefinition(awt.getDefinition());
			} else if (edit_type.equals("def"))
			{
				word.setText(awt.getText());
				word.setDefinition(edit);
			}
		}
		return word;
	}
	
	/*
	private Word populateNewWord(AllWordsTest awt, String edit, String edit_type, String test_type)
	{
		Word word = new Word();
		if (edit_type.equals("def"))
		{
			word.setText(awt.getText());
			word.setDefinition(edit);
		} else if (edit_type.equals("text"))
		{
			word.setText(awt.getDefinition());
			word.setDefinition(edit);
		}
		return word;
	}
	*/
	
	private AllWordsTest resetAWT(AllWordsTest awt, String edit, String edit_type, String test_type)
	{
		if (test_type.equals("reading"))
		{
			if (edit_type.equals("text"))
			{
				awt.setText(edit);
			} else if (edit_type.equals("def"))
			{
				awt.setDefinition(edit);
			}
		} else if (test_type.equals("writing"))
		{
			if (edit_type.equals("text"))
			{
				awt.setText(edit);
			} else if (edit_type.equals("def"))
			{
				awt.setDefinition(edit);
			}
		}
		return awt;
	}
	
	private WordTestResult resetWTR(WordTestResult wtr, String edit, String edit_type, String test_type)
	{
		if (test_type.equals("reading"))
		{
			if (edit_type.equals("text"))
			{
				wtr.setText(edit);
			} else if (edit_type.equals("def"))
			{
				wtr.setDefinition(edit);
			}
		} else if (test_type.equals("writing"))
		{
			if (edit_type.equals("text"))
			{
				wtr.setText(edit);
			} else if (edit_type.equals("def"))
			{
				wtr.setDefinition(edit);
			}
		}
		return wtr;
	}
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("AddCategoryAction.log "+(String)log.get(i));
			i++;
		}
	}

}
