package com.businessglue.struts.admin;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public final class AddUsersOptionsForm extends ActionForm
{

	private String option_name;
	private String initial_value;

	public void setOptionName(String _option_name)
	{
		option_name = _option_name;
	}

	public String getOptionName()
	{
		return option_name;
	}

	public void setInitialValue(String _initial_value)
	{
		initial_value = _initial_value;
	}

	public String getInitialValue()
	{
		return initial_value;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors=new ActionErrors();
		if(option_name == null || initial_value.equals(""))
		{
			errors.add("Text:",new ActionError("error.option_name"));
			errors.add("Definition:",new ActionError("error.initial_value"));
		}
		return errors;
	}
	
	public void reset(ActionMapping mapping, javax.servlet.ServletRequest request)
	{
		this.option_name = "";
		this.initial_value = "";
	}
}
