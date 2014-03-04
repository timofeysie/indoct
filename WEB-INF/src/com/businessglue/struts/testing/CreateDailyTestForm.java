package com.businessglue.struts.testing;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public final class CreateDailyTestForm extends ActionForm
{

	private String test_name;
	private String test_type;

	public void setTestName(String _test_name)
	{
		test_name = _test_name;
	}

	public String getTestName()
	{
		return test_name;
	}
	
	public void setTestType(String _test_type)
	{
		test_type = _test_type;
	}

	public String getTestType()
	{
		return test_type;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors=new ActionErrors();
		if(test_name== null || test_name.equals(""))
		{
			errors.add("test_name:",new ActionError("error.text"));
		}
		return errors;
	}
	
	public void reset(ActionMapping mapping, javax.servlet.ServletRequest request)
	{
		this.test_name = "";
	}
}
