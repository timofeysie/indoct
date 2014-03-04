package com.businessglue.struts;

import java.util.Vector;
import javax.servlet.http.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;
import org.catechis.dto.ChangeTestForm;
import org.catechis.dto.WordTestMemory;
import org.catechis.dto.WordTestResult;

/**
*<p>See perform action.
*@author Timothy Curchod
*/
public class ChangeTestAction extends Action
{
	
	/**
	*<p>We have to take the request parameter index=particular test word, retrieve
	* the save WordTestResult with using the index/id, and populate the WordTestForm
	* with the word and test particulars.
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		ServletContext context = getServlet().getServletContext();
		HttpSession session = request.getSession();
		String str_index = (String)request.getParameter("index");
		int index = 0;
		WordTestResult word_test_result = new WordTestResult();
		ChangeTestForm change_test_form = new ChangeTestForm();
		Vector word_test_results = (Vector)session.getAttribute("word_test_results");
		if (str_index.equals("-1"))
		{
			word_test_result = (WordTestResult)session.getAttribute("wtr");
		} else
		{
			index = Integer.parseInt(str_index);
			word_test_result = (WordTestResult)word_test_results.elementAt(index);
		}
		change_test_form.setText(word_test_result.getText());
		change_test_form.setDefinition(word_test_result.getDefinition());
		change_test_form.setAnswer(word_test_result.getAnswer());
		change_test_form.setGrade(word_test_result.getGrade());
		session.setAttribute("test_index", str_index);
		session.setAttribute("change_test_form", change_test_form);
		return mapping.findForward("change_test");
	}

}
