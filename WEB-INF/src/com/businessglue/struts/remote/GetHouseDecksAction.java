package com.businessglue.struts.remote;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.catechis.FileStorage;
import org.catechis.I18NWebUtility;
import org.catechis.Storage;
import org.catechis.constants.Constants;
import org.catechis.dto.SavedTest;
import org.catechis.dto.SimpleWord;
import org.catechis.dto.UserInfo;
import org.catechis.dto.WordCategory;
import org.catechis.file.FileSaveTests;
import org.catechis.file.FileTestRecords;
import org.catechis.file.FileWordCategoriesManager;
import org.catechis.wherewithal.DeckCard;
import org.catechis.wherewithal.HouseDeck;

import com.businessglue.util.Workaround;

/**
<p>This class accepts a teacher_id and a device_id and loads the device_id.xml file in the house_decks
folder of the teacher's folder, and send the file to the android app.
 * @author Timothy Curchod
 */
public class GetHouseDecksAction extends Action
{
	
	private static String DEBUG_TAG = "GetHouseDecksAction";
	
	/**
	*<p>Load an xml file and set it as a hashtable and a vector in the session.
	*/
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		String method = "perform";
		ServletContext context = getServlet().getServletContext();
		String context_path = context.getRealPath("/WEB-INF/");
		Map pairs = request.getParameterMap();
		context.log(DEBUG_TAG+"."+method+" pairs "+pairs.size());
		printParameters(request);
		FileSaveTests f_save_tests = new FileSaveTests();
		String teacher_id = request.getParameter("teacher_id");
		String device_id = request.getParameter("device_id");
		context.log(DEBUG_TAG+"."+method+" teacher_id "+teacher_id);
		context.log(DEBUG_TAG+"."+method+" device_id "+device_id);
		Storage store = new FileStorage(context_path, context);
		Vector house_decks = f_save_tests.loadHouseDecks(teacher_id, context_path, device_id);
		printHouseDecks(house_decks);
		printLog(f_save_tests.getLog(), context);
		xmlResponse(response, house_decks);
		f_save_tests.saveHouseDecks(teacher_id, context_path, device_id, house_decks);
		return (mapping.findForward("not_used"));
	}
	
	private void xmlResponse(HttpServletResponse response, Vector house_decks)
	{
		ServletContext context = getServlet().getServletContext(); 
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<house_decks>");
		for (int i = 1; i < house_decks.size(); i++)
		{
			HouseDeck house_deck = (HouseDeck)house_decks.get(i);
			sb.append("<house_deck>");
				sb.append("<deck_id>"+house_deck.getDeckId()+"</deck_id>");
				sb.append("<player_id>"+house_deck.getPlayerId()+"</player_id>");
				sb.append("<game_id>"+house_deck.getGameId()+"</game_id>");
				sb.append("<deck_name>"+house_deck.getDeckName()+"</deck_name>");
				Hashtable <String,DeckCard> deck_cards = house_deck.getCards();
				context.log("deck "+house_deck.getDeckName()+" has "+deck_cards.size());
				for (Enumeration keys = deck_cards.keys(); keys.hasMoreElements() ;) 
				{
					String deck_id = (String)keys.nextElement();
					DeckCard deck_card = (DeckCard)deck_cards.get(deck_id);
					sb.append("<deck_card>");
						sb.append("<card_id>"+deck_card.getCardId()+"</card_id>");
						sb.append("<card_name>"+deck_card.getCardName()+"</card_name>");
						sb.append("<status>"+deck_card.getStatus()+"</status>");
						sb.append("<index>"+deck_card.getIndex()+"</index>");
						sb.append("<type>"+deck_card.getType()+"</type>");
					sb.append("</deck_card>");
				}
			sb.append("</house_deck>");
		}
		sb.append("</house_decks>");
		response.setContentType("text/xml");
		response.setContentLength(sb.length()); 
		PrintWriter out;
		try 
		{
		    out = response.getWriter();
			out.println(sb.toString()); 
			out.close();
			out.flush();
		} catch (IOException e) 
		{
			context.log("Could not steal output stream");
		}
	}
	
	private void printHouseDecks(Vector house_decks)
	{
		String method = "printHouseDecks";
		ServletContext context = getServlet().getServletContext();
		for (int i = 1; i < house_decks.size(); i++)
		{
			HouseDeck house_deck = (HouseDeck)house_decks.get(i);
			Hashtable <String,DeckCard> deck_cards = house_deck.getCards();
			context.log(DEBUG_TAG+"."+method+": deck_name "+house_deck.getDeckName()+" cards "+deck_cards.size());
			for (Enumeration keys = deck_cards.keys(); keys.hasMoreElements() ;) 
			{
					String deck_id = (String)keys.nextElement();
					DeckCard deck_card = (DeckCard)deck_cards.get(deck_id);
					context.log(DEBUG_TAG+"."+method+": name "+deck_card.getCardName()+" id "+deck_card.getCardId());
			}
		}
	}
	
	private void printParameters(HttpServletRequest request)
	{
		String method = "printParameters";
		ServletContext context = getServlet().getServletContext();
		Enumeration <String> names = request.getParameterNames();
		while (names.hasMoreElements())
		{
			String parameter = names.nextElement();
			String value = request.getParameter(parameter);
			context.log(DEBUG_TAG+"."+method+" "+parameter+" - "+value);
		}
	}


	private void printLog(Vector log, ServletContext context)
	{
		int total = log.size();
		int i = 0;
		while (i<total)
		{
			context.log(DEBUG_TAG+".log "+(String)log.get(i));
			i++;
		}
	}

}
