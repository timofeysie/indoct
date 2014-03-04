package com.businessglue.struts;

import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.dto.WordStats;
import org.catechis.dto.WordFilter;
import org.catechis.dto.AllWordStats;

import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;
import com.businessglue.struts.FilterListForm;
import com.businessglue.util.Workaround;
import org.catechis.I18NWebUtility;

/**
<p>This class retrieves a filename from the header, parses puts it in a FileBean
 and puts the bean in the session scope.
 <p>Workarounds include, path obtained by dubious methods (done in LoginAction),
 and file_name retrieved by parsing header, and not request.getHeader("filename")which returns null.
 <p>Took out the file_bean attribute words_defs because it caused problems later with
 a class cast exception.
 * @author Timothy Curchod 
 */
public class FilterListAction extends Action
{
	
	/**
	*<p>Take a word category or all words and put the list in the session context.
	*<p>Maybe we don't need to pass the entire WordFilter bean to the jsp page,
	* because we only use the type attribute at this point.
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		FilterListForm filter_list_form = (FilterListForm)form;
		String min_max = getMinMax(filter_list_form);
		String test_type = filter_list_form.getType();
		String category_name = filter_list_form.getCategory();
		//Storage store = (Storage)session.getAttribute("store");
		String user_name = (String)session.getAttribute("user_name");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_name, context_path);
		WordFilter word_filter = new WordFilter();
		Vector all_words =  new Vector();
		if (category_name.equals("all"))
		{
			context.log("FilterListAction.perform: filtering");
			Vector all_word_categories = store.getWordCategories("exclusive", user_name);
			word_filter.setStartIndex(0);
			word_filter.setMinMaxRange(min_max);
			word_filter.setType(test_type);
			word_filter.setCategory(category_name);
			for (int i = 0; i < all_word_categories.size(); i++) 
			{
				String category = (String)all_word_categories.get(i);
				word_filter.setCategory(category);  // The filter needs a new name.
				Vector category_words = store.getFilteredWordObjects(word_filter, user_name);
				all_words.addAll(category_words);
				context.log("ListAction.perform: added "+category_words.size()+" words for "+category);
			}
		}
		else
		{
			context.log("ListAction.perform: single category "+category_name);
			word_filter.setStartIndex(0);
			word_filter.setMinMaxRange(min_max);
			word_filter.setType(test_type);
			word_filter.setCategory(category_name);
			all_words = store.getFilteredWordObjects(word_filter, user_name);
		}
		context.log("ListAction.perform: total "+all_words.size()+" words for "+category_name);
		session.setAttribute("filtered_words", all_words);
		session.setAttribute("word_filter", word_filter);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("filter_list"));
	}
	
	private String getMinMax(FilterListForm form)
	{
		FilterListForm filter_list_form = (FilterListForm)form;
		String min = filter_list_form.getMin();
		String max = filter_list_form.getMax();
		String min_max = new String(min+"-"+max);
		return min_max;
	}

}
