package com.businessglue.struts.options;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.util.Vector;
import java.util.Hashtable;

import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import org.catechis.Storage;

import com.businessglue.struts.options.EditELTOptionsForm;
import com.businessglue.struts.IndoctAction;

/**
 * Get values for new ELT Vector, and pass them along to a confirmation jsp.
 *
 */
public final class EditELTOptionsAction extends Action 
{

    /**
     * Load user options, extract exclude-level-time Vector and place it in the response.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @return Action to forward to edit_elt_options_confirm.jsp to confirm changes.
     * @exception Exception if an input/output error or servlet exception occurs
     */
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception 
    {
    	HttpSession session = request.getSession();
    	ServletContext context = getServlet().getServletContext();
    	context.log("EditELTOptionsAction.perform");
    	String user_id   = (String)session.getAttribute("user_id");
    	context.log("EditELTOptionsAction.perform: user_id   "+user_id);
    	String context_path = context.getRealPath("/WEB-INF/");
    	String encoding = new String("UTF-8");
    	EditELTOptionsForm f = (EditELTOptionsForm) form;
    	Vector elt_vector = setupNewELTVector(f);
    	Storage store = new FileStorage(context_path, context);
    	Hashtable user_opts = store.getUserOptions(user_id, context_path);
    	I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
    	session.setAttribute("elt_vector", elt_vector);
        return mapping.findForward("edit_elt_options_confirm");
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

}
