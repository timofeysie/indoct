package com.businessglue.struts;

import org.catechis.Storage;
import org.catechis.dto.AllWordStats;

import java.util.Vector;
import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.apache.struts.action.*;
//import com.businessglue.struts.FilterListForm;
import com.businessglue.util.Workaround;
import com.businessglue.util.I18NWebUtility;

import org.catechis.dto.WordFilter;
import com.businessglue.struts.FilterTestRunForm;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;

/**
*<p>We assume that all words are game for this page, we just need to figure out
* what level and type the user has chosen, then forward to a page to choose other
* filter properties, like exclude recently tested words, etc.
*<p>The filter object needs to be retrieved from the session and then have properties
* from the previous form added, and then set back into the session.
 * @author Timothy Curchod
*/
public class FilterTestRunAction extends Action
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
		FilterTestRunForm filter_test_run_form = (FilterTestRunForm) form;
		String exclude_time = (String)filter_test_run_form.getExcludeTime();
		context.log("FilterTestRunAction.perform: exclude_time "+exclude_time);
		Storage store = (Storage)session.getAttribute("store");
		String user_name = (String)session.getAttribute("user_name");
		WordFilter filter = (WordFilter)session.getAttribute("filter");
		filter.setExcludeTime(exclude_time);
		session.setAttribute("filter", filter);
		return (mapping.findForward("filter_test_run"));
	}

}
