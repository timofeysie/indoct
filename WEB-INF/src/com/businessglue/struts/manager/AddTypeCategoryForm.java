package com.businessglue.struts.manager;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public final class AddTypeCategoryForm extends ActionForm
{

	private String category;
	private String type;

	public void setCategory(String _category)
	{
		category = _category;
	}

	public String getCategory()
	{
		return category;
	}
	
	public void setType(String _type)
	{
		type = _type;
	}

	public String getType()
	{
		return type;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors=new ActionErrors();
		if(category== null || category.equals(""))
		{
			errors.add("category:",new ActionError("error.text"));
		}
		return errors;
	}
	
	public void reset(ActionMapping mapping, javax.servlet.ServletRequest request)
	{
		this.category = "";
	}
}