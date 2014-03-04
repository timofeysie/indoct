package com.businessglue.struts;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

/**
 * 
 * @author Timothy Curchod
 *
 */
public final class FilterListForm extends ActionForm
{

	private String min;
	private String max;
	private String type;
	private String category;

	public FilterListForm()
	{}

	public void setMin(String _min)
	{
		min = _min;
	}

	public String getMin()
	{
		return min;
	}
	
	public void setMax(String _max)
	{
		max = _max;
	}

	public String getMax()
	{
		return max;
	}
	
	public void setType(String _type)
	{
		type = _type;
	}
	
	public String getType()
	{
		return type;
	}
	
	public void setCategory(String _category)
	{
		category = _category;
	}
	
	public String getCategory()
	{
		return category;
	}

}
