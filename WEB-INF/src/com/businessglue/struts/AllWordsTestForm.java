package com.businessglue.struts;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

/**
 * 
 * @author Timothy Curchod
 */
public final class AllWordsTestForm extends ActionForm
{

	private String field;

	public void setField0(String _field)
	{
		field = _field;
	}

	public String getField()
	{
		return field;
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors=new ActionErrors();
		return errors;
	}
}
