package com.businessglue.struts.manager;

import java.util.Date;
import java.util.Vector;
import java.util.Hashtable;
import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import org.catechis.Transformer;
import org.catechis.constants.Constants;
import org.catechis.dto.AllStatsHistory;
import org.catechis.dto.AllWordStats;
import org.catechis.dto.AllTestStats;
import org.catechis.dto.WordCategory;
import org.catechis.file.FileCategories;
import org.catechis.file.FileTestRecords;
import org.catechis.file.FileWordCategoriesManager;
import org.catechis.lists.Sarray;
import org.catechis.lists.Sortable;

import com.businessglue.struts.AddCategoryForm;

public class AddTypeCategoryAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_id   = (String)session.getAttribute("user_id");
		context.log("AddCategoryAction.perform: user_id   "+user_id);
		String context_path = context.getRealPath("/WEB-INF/");
		Storage store = new FileStorage(context_path, context);
		Hashtable <String,String> user_opts = store.getUserOptions(user_id, context_path); 
		String subject = (String)user_opts.get("subject");
		String encoding = (String)user_opts.get("encoding");
		AddTypeCategoryForm f = (AddTypeCategoryForm) form;
		String category = f.getCategory();
		String category_type = f.getType();
		if (category_type == null)
		{
			category_type = Constants.NON_EXCLUSIVE;
			context.log("use default type");
		}
		context.log("new category name "+category+" type "+category_type);
		// need to confirm name and proper type also?
		WordCategory cat = new WordCategory();
		cat.setCategoryType(category_type);
		cat.setName(category);
		long now = new Date().getTime();
		cat.setCreationTime(now);
		FileWordCategoriesManager fwcm = new FileWordCategoriesManager();
		fwcm = new FileWordCategoriesManager();
		long cat_id = fwcm.addWordCategory(cat, user_id, subject, context_path, encoding);
		context.log("new id "+cat_id);
		FileWordCategoriesManager fiwocama = new FileWordCategoriesManager();
		Sarray word_catsarray = new Sarray();
		context.log("catsarray size "+word_catsarray.size());
		word_catsarray = fiwocama.getSortedCategories(user_id, subject, context_path, Constants.EXCLUSIVE);		
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		if (category_type.equals(Constants.EXCLUSIVE))
		{
			return (mapping.findForward("exclusive_category"));
		} else
		{
			return (mapping.findForward("non_exclusive_category"));
		}
	}
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log((String)log.get(i));
			i++;
		}
	}
	
}
