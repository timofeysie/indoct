package com.businessglue.struts;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

/**
 * 
 * @author Timothy Curchod
 */
public final class AddWordForm extends ActionForm
{

	private String text;
	private String definition;

	public void setText(String _text)
	{
		text = _text;
	}

	public String getText()
	{
		return text;
	}

	public void setDefinition(String _definition)
	{
		definition = _definition;
	}

	public String getDefinition()
	{
		return definition;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors=new ActionErrors();
		if(text == null || definition.equals(""))
		{
			errors.add("Text:",new ActionError("error.text"));
			errors.add("Definition:",new ActionError("error.definition"));
		}
		return errors;
	}
	
	public void reset(ActionMapping mapping, javax.servlet.ServletRequest request)
	{
		this.text = "";
		this.definition = "";
	}
}