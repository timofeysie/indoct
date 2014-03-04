package com.businessglue.struts;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

/**
 * 
 * @author Timothy Curchod
 */
public final class ImportListForm extends ActionForm
{
	/**
	*This field is for a list of words in the folowing formate:
		<p>text=definition;
		<p>text2=definition2;
		<p>...
	*/
	private String text;
	
	/**This member is for a date of entry other than the present date,
	* which will be set automatically if this field is not used.
	*<p>A future date would indicate the word should not appear on a test
	* until after that date.
	*/
	private String date;

	public void setText(String _text)
	{
		text = _text;
	}

	public String getText()
	{
		return text;
	}
	
	public void setDate(String _date)
	{
		date = _date;
	}
	
	public String getDate()
	{
		return date;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors=new ActionErrors();
		if(text == null)
		{
			errors.add("Text:",new ActionError("error.text"));
		}
		return errors;
	}
	
	public void reset(ActionMapping mapping, javax.servlet.ServletRequest request)
	{
		this.text = "";
		this.date = "";
	}
}