package com.businessglue.struts;

import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author Timothy Curchod
 * 
 */
public final class FilterTestRunForm extends ActionForm
{

	private String exclude_time;
	

	public void setExcludeTime(String _exclude_time)
	{
		exclude_time = _exclude_time;
	}
	
	public String getExcludeTime()
	{
		return exclude_time;
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors=new ActionErrors();
		if(exclude_time == null || exclude_time.equals(""))
		{
			errors.add("Exclude Time:",new ActionError("exclude time not set"));
		}
		return errors;
	}
}
