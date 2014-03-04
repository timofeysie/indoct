package com.businessglue.struts;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import org.catechis.dto.Word;
import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.Transformer;
import org.catechis.I18NWebUtility;
import org.catechis.dto.Momento;
import org.catechis.dto.AllWordsTest;
import org.catechis.file.FileTestRecords;
import com.businessglue.util.EncodeString;

import java.util.Date;
import java.util.Vector;
import java.util.Hashtable;

public class RetireWordAction extends Action
{
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext();
		String user_id = (String)session.getAttribute("user_id");	// user name or id
		String context_path = context.getRealPath("/WEB-INF/");
		FileStorage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		String encoding = (String)user_opts.get("encoding");
		//String locale = (String)user_opts.get("locale");
		//String encoding = new String("UTF-8");
		AllWordsTest awt = (AllWordsTest)session.getAttribute("awt_test_word");	// this object holds the original word tested
		Word word = new Word();
		word.setText(awt.getText());
		word.setDefinition(awt.getDefinition());
		word.setId(awt.getId());
		String category = awt.getCategory(); 
		store.retireWord(word, category, user_id, encoding);
		dumpLog(store.getLog(), context);
		I18NWebUtility.forceContentTypeAndLocale(request, response, user_opts);
		try
	        {
				FileTestRecords ftr = new FileTestRecords(context_path);
				String subject = (String)user_opts.get("subject");
	        	Momento old_m = ftr.getMomentoObject(user_id, subject);
	        	String action_name = old_m.getActionName();
	        	context.log("RetireWordAction.perform action_name="+action_name);
	        	Momento new_m = new Momento("IntegratedTestResultAction", Long.toString(new Date().getTime()), "RetireWordAction", "default"); 
	        	ftr.setMomentoObject(user_id, subject, new_m);
	        	if (action_name.equals("IntegratedTestResultAction"))
	        	{
	        		return (mapping.findForward("integrated_test_result"));
	        	}
	        } catch (java.lang.NullPointerException npe)
	        {
	        	context.log("npe getting momento");
	        }
		return (mapping.findForward("daily_test_result"));		// return to daily test results page.
	}
	
	private void dumpLog(Vector log, ServletContext context)
	{
		int size = log.size();
		int i = 0;
		while (i<size)
		{
			context.log("Log: "+log.get(i));
			i++;
		}
	}

}
