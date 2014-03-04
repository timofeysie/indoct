package com.businessglue.struts.teacher;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;

import java.util.Vector;
import java.util.Hashtable;

import org.catechis.dto.UserInfo;
import org.catechis.dto.Word;
import org.catechis.Storage;
import org.catechis.Transformer;
import org.catechis.FileStorage;
import org.catechis.file.FileSaveTests;
import org.catechis.file.FileTestRecords;
import com.businessglue.util.EncodeString;

import java.util.Date;

public class SavedTestsForClassesAction extends Action
{
	
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, 				HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletContext(); 
		context.log("SavedTestsForClassesAction.perform");
		String context_path = context.getRealPath("/WEB-INF/");
		String teacher_id = (String)session.getAttribute("teacher_id");
		Storage store = new FileStorage(context_path, context);
		Hashtable teacher_opts = store.getTeacherOptions(teacher_id, context_path);
		String encoding = (String)teacher_opts.get("encoding");
		FileSaveTests fst = new FileSaveTests();
		UserInfo user_info = new UserInfo(encoding, context_path, teacher_id, "vocab");
		Vector saved_class_tests = fst.loadClassTestList(user_info);
		session.setAttribute("saved_class_tests", saved_class_tests);
		return (mapping.findForward("saved_tests_for_classes"));
	}
	
	private void dumpLog(Vector log, ServletContext context)
	{
		int size = log.size();
		int i = 0;
		while (i<size)
		{
			context.log("Log: "+log.get(i));
			i++;
		}
	}

}
