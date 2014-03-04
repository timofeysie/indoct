package com.businessglue.struts;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

/**
 * 
 * @author Timothy Curchod
 */
public final class AddNewUserForm extends ActionForm
{

	private String user_name;
	private String password;
	private String e_mail;
	private String invitation_code;

	public void setUserName(String _user_name)
	{
		user_name = _user_name;
	}

	public String getUserName()
	{
		return user_name;
	}

	public void setPassword(String _password)
	{
		password = _password;
	}

	public String getPassword()
	{
		return password;
	}

	public void setEmail(String _e_mail)
	{
		e_mail = _e_mail;
	}

	public String getEmail()
	{
		return e_mail;
	}
	
	public void setInvitationCode(String _invitation_code)
	{
		invitation_code = _invitation_code;
	}

	public String getInvitationCode()
	{
		return invitation_code;
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors=new ActionErrors();
		if(user_name == null || password.equals("") || invitation_code == null)
		{
			errors.add("Name:",new ActionError("error.user_name"));
			errors.add("Password:",new ActionError("error.password"));
			errors.add("Invitation Code:",new ActionError("error.invitation_code"));
		}
		return errors;
	}
	
	public void reset(ActionMapping mapping, javax.servlet.ServletRequest request)
	{
		this.user_name = "";
		this.password = "";
		this.invitation_code = "";
	}
}