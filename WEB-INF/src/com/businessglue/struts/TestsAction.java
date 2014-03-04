package com.businessglue.struts;

import java.util.List;
import java.util.Vector;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Comparator;
import java.util.Collections;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;
import org.apache.commons.beanutils.BeanComparator;

import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import org.catechis.dto.AllWordStats;
import org.catechis.dto.AllTestStats;
import org.catechis.dto.AllStatsHistory;
import org.catechis.file.FileCategories;

public class TestsAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		context.log("TestsAction.perform");
		String user_id = (String)session.getAttribute("user_id");
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		Vector tests = store.getTestCategories(user_id);
		context.log("TestsAction.perform 1");
		AllTestStats all_test_stats = store.getTestStats(tests, user_id);
		context.log("TestsAction.perform 2");
		// Sort tests by name
		List list = Arrays.asList(all_test_stats.getTestStats());
		context.log("TestsAction.perform 3");
		Comparator comparator = new BeanComparator("name");
		Collections.sort(list, comparator);
		context.log("TestsAction.perform 4");
		session.setAttribute("all_test_stats", all_test_stats);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		context.log("TestsAction.perform 5");
		String locale = (String)user_opts.get("locale");
		String encoding = (String)user_opts.get("encoding");
		I18NWebUtility.forceContentTypeAndLocale(request, response, locale, encoding);
		context.log("TestsAction.perform 6");
		return (mapping.findForward("tests"));
	}
	
}
