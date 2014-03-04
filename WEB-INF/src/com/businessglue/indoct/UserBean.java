package com.businessglue.indoct;

import java.util.Vector;
import java.util.Hashtable;

public class UserBean
{
	
	private Vector files;
	private String user_name;
	private String user_path;
	private Hashtable options;
	
	public UserBean()
	{
	
	}
	
	public void putFileList(Vector _files)
	{
		files = _files;
	}
	
	public Object getFile(int i)
	{
		return files.get(i);
	}
	
	public void setFile(int i, Object f)
	{
		files.set(i, f);
	}
	
	public Vector getFileList()
	{
		return files;
	}
	
	public void setUserName(String _user)
	{
		user_name = _user;
	}
	
	public String getUserName()
	{
		return user_name;
	}
	
	public void setPath(String _path)
	{
		user_path = _path;
	}
	
	public String getPath()
	{
		return user_path;
	}
	
	public void putOptions(Hashtable _options)
	{
		options = _options;
	}
	
	public Hashtable getOptions()
	{
		return options;
	}
	
	public String getOption(String opt)
	{
		return (String)options.get(opt);
	}
	
	public void setOption(String opt, String val)
	{
		options.remove(opt);
		options.put(opt, val);
	}
}
