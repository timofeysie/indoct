package com.businessglue.util;

public class Workaround
{

	public Workaround()
	{}
	
	/**
	 * @deprecated use getParameter()
	<p>Because String file_name = request.getHeader("filename"); returns null,
	this is a dirty workaround just so I can get on with the project.
	*/
	public static String getHeader(String headers)
	{
		int equ = headers.lastIndexOf("=");
		int len = headers.length();
		String key = headers.substring(0,equ);
		String val = headers.substring(equ+1,len);
		String finished_filename = new String();
		while (val.lastIndexOf("%")!=(-1))
		{
			int per = val.lastIndexOf("%");
			String filler = val.substring(per,(per+3));
			if (filler.equals("%20"))
			{
				String first_part = val.substring(0,per);
				String last_part = val.substring(per+3);
				finished_filename = new String(first_part+" "+last_part);
			}
			val=finished_filename;
		}
		finished_filename=val;
		return finished_filename;
	}

}
