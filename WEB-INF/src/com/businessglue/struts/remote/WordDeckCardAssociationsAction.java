package com.businessglue.struts.remote;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import org.catechis.Storage;
import org.catechis.constants.Constants;
import org.catechis.dto.SimpleWord;
import org.catechis.dto.UserInfo;
import org.catechis.dto.WordCategory;
import org.catechis.file.FileSaveTests;
import org.catechis.file.FileTestRecords;
import org.catechis.file.FileWordCategoriesManager;

import com.businessglue.util.Workaround;

/**
<p>This class retrieves a player_id, number_of_words, and i+word_id, i+deck_card_names from the android app
and saves them in the appropriate saved_word.xml file for the user.
 */
public class WordDeckCardAssociationsAction extends Action
{
	
	private static String DEBUG_TAG = "WordDeckCardAssociationsAction";
	
	/**
	*<p>Load an xml file and set it as a hashtable and a vector in the session.
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		String method = "perform";
		ServletContext context = getServlet().getServletContext();
		String context_path = context.getRealPath("/WEB-INF/");
		Map pairs = request.getParameterMap();
		context.log(DEBUG_TAG+"."+method+" pairs "+pairs.size());
		String player_id = request.getParameter("player_id");
		String test_id = request.getParameter("test_id");
		String test_type = request.getParameter("test_type");
		context.log(DEBUG_TAG+"."+method+" player_id "+player_id);
		context.log(DEBUG_TAG+"."+method+" test_id "+test_id);
		context.log(DEBUG_TAG+"."+method+" test_type "+test_type);
		Enumeration <String> names = request.getParameterNames();
		while (names.hasMoreElements())
		{
			String parameter = names.nextElement();
			String value = request.getParameter(parameter);
			context.log(DEBUG_TAG+"."+method+" "+parameter+" - "+value);
		}
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = user_opts = store.getUserOptions(player_id, context_path);
		FileTestRecords ftr = new FileTestRecords(context_path);
		String subject = (String)user_opts.get("subject");
		String encoding = (String)user_opts.get("encoding");	
		UserInfo user_info = new UserInfo(encoding, context_path, player_id, subject);
		getParameterInfo(request, user_info, test_id);		
		return (mapping.findForward("not_used"));
	}
	
	private void getParameterInfo(HttpServletRequest request, UserInfo user_info, String test_id)
	{
		ServletContext context = getServlet().getServletContext();
		String method = "getParameterInfo";
		String user_id = request.getParameter("player_id");
		String house_deck_name = request.getParameter("house_deck_name");
		String house_deck_id = request.getParameter("house_deck_id");
		context.log(DEBUG_TAG+"."+method+": player_id "+user_id);
		context.log(DEBUG_TAG+"."+method+": house_deck_name "+house_deck_name);
		context.log(DEBUG_TAG+"."+method+": house_deck_id "+house_deck_id);
		int number_of_words = 0;
		Hashtable <String,String> word_id_writing_deck_card_names = new Hashtable <String,String> ();
		Hashtable <String,String> word_id_reading_deck_card_names = new Hashtable <String,String> ();
		String number_of_words_string = request.getParameter("number_of_words");
		number_of_words = Integer.parseInt(number_of_words_string);
		context.log(DEBUG_TAG+"."+method+": number_of_words "+number_of_words);
		for (int i = 1; i <= number_of_words*2; i++)
		{
			try
			{
				String word_id = request.getParameter(i+"word_id");
				String word_type = request.getParameter(i+"word_type");
				String deck_card_name = null;
				if (word_type.equals(Constants.READING))
				{
					deck_card_name = request.getParameter(i+"reading_deck_card_name");
					word_id_reading_deck_card_names.put(word_id, deck_card_name);
				} else if (word_type.equals(Constants.WRITING))
				{
					deck_card_name = request.getParameter(i+"writing_deck_card_name");
					word_id_writing_deck_card_names.put(word_id, deck_card_name);
				}
				context.log(DEBUG_TAG+"."+method+": word_id "+word_id+" deck_card_name "+deck_card_name+" type "+word_type);
			} catch (java.lang.ClassCastException cce)
			{
				context.log(DEBUG_TAG+"."+method+": cce getting "+i);
			} catch (java.lang.NullPointerException npe)
			{
				context.log(DEBUG_TAG+"."+method+": npe getting "+i);
			}
		}
		context.log(DEBUG_TAG+"."+method+": word_id_reading_deck_card_names "+word_id_reading_deck_card_names.size()+" writing "+word_id_writing_deck_card_names.size());
		FileSaveTests f_save_tests = new FileSaveTests();
		f_save_tests.addDeckCardAssociations(user_info, test_id, word_id_reading_deck_card_names, word_id_writing_deck_card_names, 
				house_deck_name, house_deck_id);
	}


	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log(DEBUG_TAG+".log "+(String)log.get(i));
			i++;
		}
	}

}
