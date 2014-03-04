package com.businessglue.struts;

import java.util.Date;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.servlet.ServletContext;
import org.apache.struts.action.*;
import org.catechis.file.FileTestRecords;
import org.catechis.dto.Momento;

/**
 * @author Timothy Curchod
 */
public class IndoctAction extends Action
{

	public Momento getMomento(String context_path, String user_id, String subject)
	{
		FileTestRecords ftr = new FileTestRecords(context_path);
		Momento old_m = ftr.getMomentoObject(user_id, subject);
		return old_m;
	}
	
	public void setMomento(String context_path, String user_id, String subject, String action_name, String action_id, String action_type)
	{
		Momento new_m = new Momento(action_name, Long.toString(new Date().getTime()), action_id, action_type);
		FileTestRecords ftr = new FileTestRecords(context_path);
		ftr = new FileTestRecords(context_path);
		ftr.setMomentoObject(user_id, subject, new_m);
	}
	
	public void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log("log "+(String)log.get(i)); 
			i++;
		}
	}
	
	public void printLog(Hashtable log, ServletContext context)
	{
		for (Enumeration e = log.elements() ; e.hasMoreElements() ;) 
		{
			context.log((String)e.nextElement());
		}
	}

}
