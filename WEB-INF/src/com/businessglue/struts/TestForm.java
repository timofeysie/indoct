package com.businessglue.struts;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public final class TestForm extends ActionForm
{

	private String field0;
	private String field1;
	private String field2;
	private String field3;
	private String field4;
	private String field5;
	private String field6;
	private String field7;
	private String field8;
	private String field9;

	public void setField0(String _field)
	{
		field0 = _field;
	}

	public String getField0()
	{
		return field0;
	}
	
	public void setField1(String _field)
	{
		field1 = _field;
	}

	public String getField1()
	{
		return field1;
	}	

	public void setField2(String _field)
	{
		field2 = _field;
	}

	public String getField2()
	{
		return field2;
	}

	public void setField3(String _field)
	{
		field3 = _field;
	}

	public String getField3()
	{
		return field3;
	}	

	public void setField4(String _field)
	{
		field4 = _field;
	}

	public String getField4()
	{
		return field4;
	}

	public void setField5(String _field)
	{
		field5 = _field;
	}

	public String getField5()
	{
		return field5;
	}

	public void setField6(String _field)
	{
		field6 = _field;
	}

	public String getField6()
	{
		return field6;
	}

	public void setField7(String _field)
	{
		field7 = _field;
	}

	public String getField7()
	{
		return field7;
	}
	
	public void setField8(String _field)
	{
		field8 = _field;
	}

	public String getField8()
	{
		return field8;
	}

	public void setField9(String _field)
	{
		field9 = _field;
	}

	public String getField9()
	{
		return field9;
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors=new ActionErrors();
		return errors;
	}
}
