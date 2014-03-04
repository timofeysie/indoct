package com.businessglue.struts.admin;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public final class UsersOptionsEditForm extends ActionForm
{

	private String property;
	private String value;

	public UsersOptionsEditForm()
	{}

	public void setProperty(String _property)
	{
		property = _property;
	}

	public String getProperty()
	{
		return property;
	}
	
	public void setValue(String _value)
	{
		value = _value;
	}

	public String getValue()
	{
		return value;
	}


}
