package com.rms.collector.control;

import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.rms.collector.data.DeckDAO;
import com.rms.collector.model.Collection;
import com.rms.collector.model.Deck;
import com.rms.collector.util.Util;

public class DeckFormController extends GenericForwardComposer<Window> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Textbox deckName;
	private Window deckWin;
    private Label mesgLbl;
    
    public void onClick$confirm(Event event) {
        createDeck();
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void createDeck() {
    	try {
			if (!Util.isEmpty(deckName.getValue()) ) {
	    		Deck deck = new Deck(deckName.getValue(), UserCredentialManager.getInstance().getUserLogin().getUserId());
	    		DeckDAO deckDAO = new DeckDAO();
	    		Integer id = (Integer)deckDAO.insert(deck);
	    		deckWin.detach();
	    		Listbox deckList = (Listbox)this.arg.get("deckList");
	    		deck.setId(id);
	    		((ListModelList)deckList.getModel()).add(deck);
	    		((ListModelList)deckList.getModel()).clearSelection();
	    		//collectionList.selectItem(collectionList.getItemAtIndex(collectionList.getItemCount() - 1));
	    		((ListModelList)deckList.getModel()).addToSelection(deck);
	    		Listitem ref = deckList.getSelectedItems().iterator().next();
	    		ref.setValue(deck);
	    		Events.sendEvent(deckList, new SelectEvent<Listitem, Collection>(Events.ON_SELECT, deckList, deckList.getSelectedItems(), ref));
	    	} else {
	    		mesgLbl.setValue("Please enter a deck name.");
	    	}
    	} catch (Exception e) {
    		mesgLbl.setValue("An error has occurred.");
    		e.printStackTrace();
    	}
    }
}
