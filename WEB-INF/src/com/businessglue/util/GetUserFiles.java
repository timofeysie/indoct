package com.businessglue.util;

import java.io.*;
import java.util.*;
import java.io.File;
import java.net.URL;
import java.util.Vector;
import java.util.Properties;
import java.util.Enumeration;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.apache.commons.digester.*;
import com.businessglue.util.EncodeString;

import com.businessglue.temp.Article;
import com.businessglue.temp.Book;
import com.businessglue.temp.Catalog;
import com.businessglue.temp.Magazine;

/**
*<p>This class provides utilities for getting user data.  It should be replaced
* when a database is introduced into the web application.
*/
public class GetUserFiles
{

	private String path;
	private Vector user_files;
	private Vector elements;
	private Properties properties;
	private java.net.URL url;
	
	private Vector log;

	public GetUserFiles()
	{
		log = new Vector();
	}

	public GetUserFiles(String _user)
	{
		String bin_path = System.getProperty("user.dir");
		properties = System.getProperties();
		String sep = new String(properties.getProperty("file.separator"));
		int last_slash = bin_path.lastIndexOf(sep);
		String app_path = bin_path.substring(0, last_slash);
		path = new String(app_path+sep+"webapps"+sep+"indoct"+sep+"files");
		String user_path = new String(path+sep+_user);
		log.add("path: "+path);
		File user_files_dir = new File(user_path);
		log.add("exists? "+user_files_dir.exists());
		String[] files = user_files_dir.list();
		user_files = new Vector();
		EncodeString encoder = new EncodeString();
		for (int i = 0; i < files.length; i++) 
		{
			String file = encoder.encodeThis(files[i]);
			user_files.add(file);
		}
	}
	
	public GetUserFiles(String _user, String _path)
	{
		String user = _user;
		String path = _path;
		try 
		{
			Digester digester = new Digester();
			digester.setValidating( false );

			digester.addObjectCreate( "catalog", com.businessglue.temp.Catalog.class );

			digester.addObjectCreate( "catalog/book", com.businessglue.temp.Book.class );
			digester.addBeanPropertySetter( "catalog/book/author", "author" );
			digester.addBeanPropertySetter( "catalog/book/title", "title" );
			digester.addSetNext( "catalog/book", "addBook" );

			digester.addObjectCreate( "catalog/magazine", com.businessglue.temp.Magazine.class );
			digester.addBeanPropertySetter( "catalog/magazine/name", "name" );

			digester.addObjectCreate( "catalog/magazine/article", com.businessglue.temp.Article.class );
			digester.addSetProperties( "catalog/magazine/article", "page", "page" );
			digester.addBeanPropertySetter( "catalog/magazine/article/headline" ); 
			digester.addSetNext( "catalog/magazine/article", "addArticle" );

			digester.addSetNext( "catalog/magazine", "addMagazine" );

			properties = System.getProperties();
			String sep = new String(properties.getProperty("file.separator"));
			String full_path = new String(path+sep+user);
			File input = new File(full_path);
			Catalog c = (Catalog)digester.parse( input );
			elements = c.toVector();
		} catch( Exception exc ) 
		{
			exc.printStackTrace();
		}
	
	}
	
	
	public Vector getUserFiles()
	{
		return user_files;
	}
	
	public String getPath()
	{
		return path;
	}
	
	public Vector getElements()
	{
		return elements;
	}
	
	public Vector getSysProps()
	{
		Vector props = new Vector();
		Enumeration e = properties.propertyNames();
		while (e.hasMoreElements())
		{
			String key = new String((String)e.nextElement());
			String val = new String(properties.getProperty(key));
			String line = new String(key+" - "+val);
			props.add(line);
		}
		return props;
		
	}
	
	public String findUserPath(String user, ServletContext context)
	{
		context.log("User - " + user);
		try
		{
			url = context.getResource("/login.jsp");
		}
		catch (java.net.MalformedURLException e)
		{
			context.log("Malformed! - "+e.toString());
		}
		//String sep = new String(System.getProperties().getProperty("file.separator"));
		String sep = new String("/");
		String res_path = url.getPath();
		context.log("Resuoucre - " + res_path);
		int last_slash = res_path.lastIndexOf(sep);
		if (last_slash==-1) last_slash = res_path.length();
		String raw_path = res_path.substring(0, last_slash);
		context.log("Paw Path - " + raw_path);
		String user_fol = new String(raw_path+sep+"files"+sep+user);
		context.log("Opening - " + user_fol);
		return raw_path;
	}
	
	public Vector getFolderFiles(String folder)
	{
		File user_files_dir = new File(folder);
		String[] files_array = user_files_dir.list();
		Vector files = new Vector();
		EncodeString encoder = new EncodeString();
		for (int i = 0; i < files_array.length; i++) 
		{
			String file = encoder.encodeThis(files_array[i]);
			files.add(file);
		}
		return files;
	}
	
	/**
	*<p>This method takes a File pointing to the user.passes file, from which
	* it extracts the name-password list returned in a Hashtable.
	*/
	public Hashtable getUsersPasswords(File _files_folder, ServletContext context)
	{
		File files_folder = _files_folder;
		context.log("GetUserFiles.getUsersPasswords: folder- "+files_folder.getAbsolutePath());
		CreateJDOMList jdom_passwords = new CreateJDOMList(files_folder, context);
		Hashtable passwords_hash = jdom_passwords.getWhateverHash("user","name","password");
		append(jdom_passwords.getLog(), context);
		context.log("GetUserFiles.getUsersPasswords: returning- "+passwords_hash.size()+" passes");
		return passwords_hash;
	}
	
	public void append(Vector v,  ServletContext context)
	{
		int size = v.size();
		int i = 0;
		while (i<size)
		{
			context.log(v.get(i)+"");
			i++;
		}
	}

}
