package com.businessglue.util;

import java.io.File;
import java.io.Reader;
import java.util.Vector;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.lang.Character;
import java.util.Enumeration;
import org.xml.sax.InputSource;
import javax.servlet.ServletContext;

import com.businessglue.util.EncodeString;

public class ParseTextFile
{
	private String file_name;
	private InputSource is;
	private Hashtable words_defs;
	
	// Logging
	private ServletContext context;
	private String msg;

	public ParseTextFile(String name)
	{
		file_name = name;
	}
	
	public Hashtable getNameAndDefHashtable(ServletContext _context)
	{
		context = _context;
		String enc;
		StringBuffer buffer = new StringBuffer();
		try
		{
			FileInputStream fis = new FileInputStream(file_name);
			InputStreamReader isr = new InputStreamReader(fis, "euc-kr");
		   	Reader in = new BufferedReader(isr);
			org.xml.sax.InputSource is =  new InputSource(in);
			String elem = new String();String word = new String();String defs = new String();words_defs = new Hashtable();
			String temp = new String();
			boolean key_flag = true;
			int ch;
			while ((ch = in.read()) > -1)
			{
				Integer integ = new Integer(ch);
				String octal = integ.toString(ch, 16);
				byte[] bytes = {integ.byteValue()};
				try
				{
					temp = new String(bytes, "euc-kr");
				}
				catch (java.io.UnsupportedEncodingException uee)
				{
					context.log(uee.toString());
				}
				context.log("char "+temp+" octal "+octal+" = "+integ);
				if (temp.equals(","))
				{
					word = new String(buffer);
					buffer = new StringBuffer();
					key_flag=false;
				} else if (temp.equals("\n"))
				{
					defs = new String(buffer);
					buffer = new StringBuffer();
					String encoded_word = EncodeString.encodeThis(word);
					String encoded_defs = EncodeString.encodeThis(defs);
					words_defs.put(encoded_word,encoded_defs);
					word = new String();
					defs = new String();
					key_flag=true;
					context.log("adding "+encoded_word+" - "+encoded_defs);
				} else
				{
					if (key_flag==true)
					{
						String octalized = new String(octal);
						buffer.append(octalized);
					}
					else
					{
						buffer.append(temp);
					}
				}
			}
			in.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return words_defs;
	}
	
	public Hashtable getNameAndDefHashtableFromString(String string, ServletContext _context)
	{
		context = _context;
		String list = string;
		String elem = new String();
		String word = new String();
		String defs = new String();
		words_defs = new Hashtable();
		StringBuffer buffer = new StringBuffer();
		int i = 0;
		while (i > list.length())
		{
			char ch = list.charAt(i);
			Integer integ = new Integer(ch);
			byte[] bytes = {integ.byteValue()};
			//String temp = new String(bytes, "euc-kr");
			String temp = new String(bytes);
			if (temp.equals(","))
			{
				word = new String(buffer);
				buffer = new StringBuffer();
			} else if (temp.equals("\n"))
			{
				defs = new String(buffer);
				buffer = new StringBuffer();
				String encoded_word = EncodeString.encodeThis(word);
				String encoded_defs = EncodeString.encodeThis(defs);
				words_defs.put(encoded_word,encoded_defs);
				word = new String();
				defs = new String();
				context.log("adding "+encoded_word+" - "+encoded_defs);
			} else
			{
				buffer.append(temp);
			}
			i++;
		}
		return words_defs;
	}		

	
	public Vector getVectorFromHashtable()
	{
		Vector words_vector = new Vector();
		String key = new String();
		String val = new String();
		Enumeration keys = words_defs.keys();
		while (keys.hasMoreElements())
		{
			key = new String((String)keys.nextElement());
			val = new String((String)words_defs.get(key));
			words_vector.add(key+" - "+val);
		}
		return words_vector;
	}

}
