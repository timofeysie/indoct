package com.businessglue.indoct;

import java.util.Vector;
import java.util.Hashtable;

public class FileBean
{
	
	private Vector elements;
	private String user_name;
	private String file_name;
	private String file_path;
	private Hashtable words_defs;
	private Hashtable options;

	public FileBean()
	{
	
	}
	
	public void putElementVector(Vector _elements)
	{
		elements = _elements;
	}
	
	public Vector getElementVector()
	{
		return elements;
	}	
	
	public Object getElement(int i)
	{
		return elements.get(i);
	}
	
	public void setElement(int i, Object f)
	{
		elements.set(i, f);
	}
	
	public void putWordsDefs(Hashtable _words_defs)
	{
		words_defs = _words_defs;
	}
	
	public Hashtable getWordsDefs()
	{
		return words_defs;
	}
	
	public void setFileName(String _file)
	{
		file_name = _file;
	}
	
	public String getFileName()
	{
		return file_name;
	}	
	
	public void setUserName(String _user)
	{
		user_name = _user;
	}
	
	public String getUserName()
	{
		return user_name;
	}
	
	public void setFilePath(String _path)
	{
		file_path = _path;
	}
	
	public String getFilePath()
	{
		return file_path;
	}
	
	public void putOptions(Hashtable _options)
	{
		options = _options;
	}
	
	public Hashtable getOptions()
	{
		return options;
	}	
}
