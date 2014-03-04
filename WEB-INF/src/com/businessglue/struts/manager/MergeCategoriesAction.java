package com.businessglue.struts.manager;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

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
import org.catechis.dto.WordCategory;
import org.catechis.file.FileWordCategoriesManager;

import com.businessglue.util.Workaround;

/**
<p>This class retrieves a filename from the header loads the file and set it in the session.
 */
public class MergeCategoriesAction extends Action
{
	
	private static String DEBUG_TAG = "MergeCategoriesAction";
	
	/**
	*<p>Load an xml file and set it as a hashtable and a vector in the session.
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		String method = "perform";
		ServletContext context = getServlet().getServletContext();
		String user_id = (String)session.getAttribute("user_id");
		String cat_id = (String)session.getAttribute("category_id");
		//String subject = (String)session.getAttribute(Constants.SUBJECT);
		WordCategory word_cat = (WordCategory)session.getAttribute("category");
		String context_path = context.getRealPath("/WEB-INF/");
		Map cats_to_merge = request.getParameterMap();
		Storage store = new FileStorage(context_path, context);
		Hashtable<String, String> user_opts = store.getUserOptions(user_id, context_path); 
		String subject = user_opts.get("subject");
		String encoding = user_opts.get(Constants.ENCODING);
		context.log(DEBUG_TAG+"."+method+": category_id "+cat_id+" user_id "+user_id+" subject "+subject);
		FileWordCategoriesManager fwcm = new FileWordCategoriesManager();
		Vector <SimpleWord> all_words = new Vector(word_cat.getCategoryWords());
		context.log(DEBUG_TAG+"."+method+": starting with "+all_words.size()+" words");
		int word_count = word_cat.getTotalWordCount();
		context.log(DEBUG_TAG+"."+method+": word count "+word_count);
		Iterator it = cats_to_merge.entrySet().iterator();
		while (it.hasNext())
		{
			 Map.Entry pairs = (Map.Entry)it.next();
			 String this_cat_id = (String)pairs.getKey();
			 System.out.println(this_cat_id+" "+pairs.getValue());
			 WordCategory this_word_cat = fwcm.getWordCategory(Long.parseLong(this_cat_id), user_id, subject, context_path);
			 Vector <SimpleWord> these_words = this_word_cat.getCategoryWords();
			 word_count = word_count + these_words.size();
			 context.log(DEBUG_TAG+"."+method+": adding "+these_words.size());
			 Set <SimpleWord> set = new HashSet <SimpleWord> (these_words);
			 set.addAll(all_words);
			 all_words = new Vector<SimpleWord>(set);
			 context.log(DEBUG_TAG+"."+method+": new total "+all_words.size()+" words");
			 boolean deleted = fwcm.deleteCategory(this_word_cat.getId(), user_id, subject, context_path, encoding);
			 context.log(DEBUG_TAG+"."+method+": deleted "+this_word_cat.getName()+"? "+deleted);
		}
		context.log(DEBUG_TAG+"."+method+": all  words after add "+all_words.size()+" words");
		context.log(DEBUG_TAG+"."+method+": word count after add "+word_count);
		word_cat.setCategoryWords(all_words);
		word_cat.setTotalWordCount(all_words.size());
		fwcm.updateCategory(word_cat, user_id, subject, context_path, encoding);
		printLog(fwcm.getLog(), context);
		session.setAttribute("category", word_cat);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("exclusive_category"));
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
