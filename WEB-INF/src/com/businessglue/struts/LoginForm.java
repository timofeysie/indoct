package com.businessglue.struts;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public final class LoginForm extends ActionForm
{

	private String userName;
	private String password;

	public void setUserName(String argUserName)
	{
		userName = argUserName;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setPassword(String argPassword)
	{
		password = argPassword;
	}

	public String getPassword()
	{
		return password;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors=new ActionErrors();
		if(userName == null || password.equals(""))
		{
			errors.add("User Name:",new ActionError("error.userName"));
			errors.add("Password:",new ActionError("error.password"));
		}
		return errors;
	}
}