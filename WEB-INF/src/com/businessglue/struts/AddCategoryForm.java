package com.businessglue.struts;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

/**
 * 
 * @author Timothy Curchod
 */
public final class AddCategoryForm extends ActionForm
{

	private String category;

	public void setCategory(String _category)
	{
		category = _category;
	}

	public String getCategory()
	{
		return category;
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