package com.businessglue.struts;

import org.catechis.Storage;
import org.catechis.dto.WordFilter;
import org.catechis.dto.AllWordStats;

import java.util.Vector;
import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;
//import com.businessglue.struts.FilterListForm;
import com.businessglue.util.Workaround;
import com.businessglue.util.I18NWebUtility;

/**
*<p>We assume that all words are game for this page, we just need to figure out
* what level and type the user has chosen, then forward to a page to choose other
* filter properties, like exclude recently tested words, etc.
 * @author Timothy Curchod
*/
public class FilterTestAction extends Action
{
	
	/**
	*<p>filename=all&type=w&level=1
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String test_type = request.getParameter("type");
		String level = request.getParameter("level");
		String category_name = request.getParameter("filename");
		String min_max = new String(level+"-"+level);
		context.log("FilterTestAction.perform: recieved "+test_type+" "+level+" "+category_name);
		WordFilter filter = new WordFilter();
		filter.setStartIndex(0);
		filter.setMinMaxRange(min_max);
		filter.setType(test_type);
		filter.setCategory(category_name);
		session.setAttribute("filter", filter);
		return (mapping.findForward("filter_test"));
	}

}
