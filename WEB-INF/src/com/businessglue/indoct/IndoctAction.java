package com.businessglue.indoct;

import org.catechis.Storage;
import org.catechis.FileStorage;
import org.catechis.dto.Word;
import org.catechis.dto.Momento;
import org.catechis.admin.FileUserOptions;
import org.catechis.file.FileTestRecords;

import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;
	
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;	

/**
 * 
 * @author Timothy Curchod
 */
public class IndoctAction extends Action
{
	private Hashtable getUserOptions(String context_path, String user_id, ServletContext context)
	{
		Storage store = new FileStorage(context_path, context);
		Hashtable user_opts = store.getUserOptions(user_id, context_path);
		return user_opts;
	}
	
	private Hashtable getJSPOptions(String guest_id, String jsp_name, String subject, String current_dir, ServletContext context)
	{
		FileUserOptions fuo = new FileUserOptions(current_dir);
		Hashtable options = fuo.getJSPOptions(guest_id, jsp_name, subject);
		printLog(fuo.getLog(), context);
		return options;
	}
	
	private void getMomento(String context_path, String user_id, String subject)
	{
		FileTestRecords ftr = new FileTestRecords(context_path);
		Momento old_m = ftr.getMomentoObject(user_id, subject);
	}
	
	private void setMomento(FileTestRecords ftr, String user_id, String subject, String action_name, String action_id, String action_type)
	{
			String action_time = Long.toString(new Date().getTime());
			Word word = new Word();
			if (action_id == null)
			{
				action_id = "000";
			}
			if (action_type == null)
			{
				action_type = "000";
			}
			Momento new_m = new Momento(action_name, action_time, action_id, action_type);
			ftr.setMomentoObject(user_id, subject, new_m);
	}
	
	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("DailyTestAction "+(String)log.get(i));
			i++;
		}
	}
	
	public void printHash(Hashtable hash, ServletContext context)
	{
	    Enumeration keys = hash.keys();
	    while (keys.hasMoreElements())
	    {
		    String key = (String)keys.nextElement();
		    String val = (String)hash.get(key);
		    context.log(key+" - "+val);
	    }
	}
	
}
