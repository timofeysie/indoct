package com.businessglue.struts.remote;

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
<p>This class retrieves a player_id, number_of_words, and i+word_id, i+deck_card_names from the android app
and saves them in the appropriate saved_word.xml file for the user.
 */
public class SaveHouseDecksAction extends Action
{
	
	private static String DEBUG_TAG = "WordDeckCardAssociationsAction";
	
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
		String number_of_decks = request.getParameter("number_of_decks");
		String teacher_id = request.getParameter("teacher_id");
		String device_id = request.getParameter("device_id");
		context.log(DEBUG_TAG+"."+method+" teacher_id "+teacher_id);
		context.log(DEBUG_TAG+"."+method+" number_of_decks "+number_of_decks);
		context.log(DEBUG_TAG+"."+method+" device_id "+device_id);
		Storage store = new FileStorage(context_path, context);
		//Hashtable teacher_opts = teacher_opts = store.getTeacherOptions(teacher_id, context_path);
		Vector house_decks = new Vector();
		for (int i = 1; i <= Integer.parseInt(number_of_decks); i++)
		{
			HouseDeck house_deck = getHouseDeck(i, request);
			house_decks.add(house_deck);
		}
		f_save_tests.saveHouseDecks(teacher_id, context_path, device_id, house_decks);
		return (mapping.findForward("not_used"));
	}
	
	private HouseDeck getHouseDeck(int i, HttpServletRequest request)
	{
		String method = "getHouseDeck";
		ServletContext context = getServlet().getServletContext();
		String deck_id = request.getParameter(i+"deck_id");
		String deck_name = request.getParameter(i+"deck_name");
		String player_id = request.getParameter(i+"player_id");
		String game_id = request.getParameter(i+"game_id");
		String number_of_cards = request.getParameter(i+"number_of_cards");
		HouseDeck house_deck = new HouseDeck();
		house_deck.setDeckId(deck_id);
		house_deck.setDeckName(deck_name);
		house_deck.setPlayerId(player_id);
		house_deck.setGameId(game_id);
		Hashtable<String, DeckCard> deck_cards = new Hashtable<String, DeckCard>();
		context.log(DEBUG_TAG+"."+method+" i "+i+" "+deck_id+" "+deck_name+" "+player_id+" "+game_id+" "+number_of_cards);
		for (int j = 1; j < Integer.parseInt(number_of_cards); j++)
		{
			DeckCard deck_card = getDeckCard(i, j, request);
			deck_cards.put(deck_card.getCardId(), deck_card);
		}
		house_deck.setCards(deck_cards);
		return house_deck;
	}
	
	private DeckCard getDeckCard(int i, int j, HttpServletRequest request)
	{
		String method = "getDeckCard";
		ServletContext context = getServlet().getServletContext();
				String card_id = request.getParameter(i+"card"+j+"card_id");
				String card_name = request.getParameter(i+"card"+j+"card_name");
				String status = request.getParameter(i+"card"+j+"status");
				String type = request.getParameter(i+"card"+j+"type");
				String index = request.getParameter(i+"card"+j+"index");
				DeckCard deck_card = new DeckCard();
				deck_card.setCardId(card_id);
				deck_card.setCardName(card_name);
				deck_card.setStatus(status);
				deck_card.setType(type);
				deck_card.setIndex(Integer.parseInt(index));
				context.log(DEBUG_TAG+"."+method+" i"+i+" j "+j+" "+card_id+" "+card_name+" "+status+" "+type+" "+index);
				return deck_card;
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
