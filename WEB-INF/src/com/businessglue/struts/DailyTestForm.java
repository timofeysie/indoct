package com.businessglue.struts;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

/**
 * 
 *@author Timothy Curchod 
 */
@SuppressWarnings("serial")
public final class DailyTestForm extends ActionForm
{

	private String answer;

	public void setAnswer(String _answer)
	{
		answer = _answer;
	}

	public String getAnswer()
	{
		return answer;
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors=new ActionErrors();
		return errors;
	}
}
