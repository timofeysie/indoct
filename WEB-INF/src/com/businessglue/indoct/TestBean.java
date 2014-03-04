package com.businessglue.indoct;

import java.util.Hashtable;

public class TestBean
{

	private String file_name;
	private Hashtable options;
	private Hashtable test;
	private String test_file_name;

	public TestBean()
	{}
	
	public void setFileName(String _file)
	{
		file_name = _file;
	}
	
	public String getFileName()
	{
		return file_name;
	}
	
	public void putOptions(Hashtable _options)
	{
		options = _options;
	}
	
	public Hashtable getOptions()
	{
		return options;
	}
	
	public void putTestFile(Hashtable _test)
	{
		options = _test;
	}
	
	public Hashtable getTestFile()
	{
		return test;
	}
	
	public void setTestFileName(String _test_file)
	{
		test_file_name = _test_file;
	}
	
	public String getTestFileName()
	{
		return test_file_name;
	}	
}
