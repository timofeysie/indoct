package com.businessglue.util;

import java.io.File;
import java.io.Reader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.xml.sax.InputSource;
import javax.servlet.ServletContext;

public class OpenTextFile
{
	
	private InputSource is;
	private static StringBuffer buffer;
	
	// Logging
	private ServletContext context;
	private String msg;

	public OpenTextFile(String name)
	{
		String file_name = name;
		String enc;
		System.out.println("Recieved "+file_name);
		buffer = new StringBuffer();
		try
		{
			FileInputStream fis = new FileInputStream(file_name);
			InputStreamReader isr = new InputStreamReader(fis, "ms949");
		   	Reader in = new BufferedReader(isr);
		   	is =  new InputSource(in);
		   	enc = is.getEncoding();
			int ch;
			while ((ch = in.read()) > -1)
			{
				buffer.append((char)ch);
			}
			in.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public OpenTextFile(String name, ServletContext context)
	{
		String file_name = name;
		String enc;
		System.out.println("Recieved "+file_name);
		buffer = new StringBuffer();
		try
		{
			FileInputStream fis = new FileInputStream(file_name);
			InputStreamReader isr = new InputStreamReader(fis, "ms949");
		   	Reader in = new BufferedReader(isr);
		   	is =  new InputSource(in);
		   	enc = is.getEncoding();
			int ch;
			while ((ch = in.read()) > -1)
			{
				buffer.append((char)ch);
			}
			in.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		String intro = new String("Trying to load file: ");
		msg = new String(intro+buffer);
		context.log(msg);
	}	
	
	public StringBuffer getTextBuffer()
	{
		return buffer;
	}

}
