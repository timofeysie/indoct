package com.businessglue.struts.options;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import org.catechis.file.FileCategories;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import com.businessglue.struts.options.EditELTOptionsForm;

import java.util.Date;
import java.util.Vector;
import java.util.Hashtable;

/**
*<p>Usually, an action gets called like this: name.do > NameAction > name.jsp, but his action
* is called in reverse, ie name.jsp > NameAction > categories_simple.jsp.
*/
public class UpdateOptionsAction extends Action
{
	
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_id   = (String)session.getAttribute("user_id");
		context.log("AddCategoryAction.perform: user_id   "+user_id);
		String context_path = context.getRealPath("/WEB-INF/");
		EditELTOptionsForm f = (EditELTOptionsForm)form;
		Vector elt_vector = setupNewELTVector(f);
		session.setAttribute("elt_vector", elt_vector);
		context.log("UpdateOptionsAction.perform: elt_options "+elt_vector.size());
		printLog(elt_vector, context);
		FileStorage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		return (mapping.findForward("edit_elt_options_confirm"));
	}
	
	private Vector setupNewELTVector(EditELTOptionsForm f)
    {
    	Vector elt_vector = new Vector();
    	elt_vector.add(f.getExcludeLevel0Test());
    	elt_vector.add(f.getExcludeLevel1Test());
    	elt_vector.add(f.getExcludeLevel2Test());
    	elt_vector.add(f.getExcludeLevel3Test());
    	return elt_vector;
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
