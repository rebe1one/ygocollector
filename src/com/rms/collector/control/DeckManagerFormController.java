package com.rms.collector.control;

import java.sql.SQLException;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import com.rms.collector.data.CollectionCardViewDAO;
import com.rms.collector.data.DeckCardDAO;
import com.rms.collector.data.DeckCardViewDAO;
import com.rms.collector.model.view.DeckCardView;
import com.rms.collector.model.view.CollectionCardView;
import com.rms.collector.util.Filter;
import com.rms.collector.util.FilterList;

public class DeckManagerFormController extends GenericForwardComposer<Window> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Listbox collection, deck;
    private Label mesgLbl;
    private Integer deckId;
    
    public void onClick$toDeck(Event event) {
        for (Listitem item : collection.getSelectedItems()) {
        	CollectionCardView card = (CollectionCardView)item.getValue();
        	DeckCardView deckCard = new DeckCardView();
        	deckCard.setCardId(card.getCardId());
        	deckCard.setDeckId(deckId);
        	deckCard.setAmount(1);
        	deckCard.setName(card.getName());
        	deckCard.setAttribute(card.getAttribute());
        	
        	for (Listitem deckItem : deck.getItems()) {
        		DeckCardView dc = (DeckCardView)deckItem.getValue();
        		if (deckCard.getCardId() == dc.getCardId()) {
        			if (dc.getAmount() < 3) {
	        			dc.setAmount(dc.getAmount() + 1);
	        			int index = deckItem.getIndex();
	        			System.out.println(index);
	        			((ListModelList)deck.getModel()).remove(index);
	        			((ListModelList)deck.getModel()).add(index, dc);
        			}
        			return;
        		}
        	}
        	Listitem deckCardItem = new Listitem();
        	deckCardItem.setValue(deckCard);
        	((ListModelList)deck.getModel()).add(deckCard);
        }
    }
    
    public void onClick$fromDeck(Event event) {
        for (Listitem item : deck.getSelectedItems()) {
        	deck.removeChild(item);
        }
    }
    
    public void onClick$save(Event event) {
    	DeckCardDAO dao = new DeckCardDAO();
    	try {
    		dao.startTransaction();
	    	for (Listitem deckItem : deck.getItems()) {
	    		DeckCardView dc = (DeckCardView)deckItem.getValue();
	    		if (dao.find(FilterList.start("deck_id", dc.getDeckId(), Filter.Equality.EQUALS).and("card_id", dc.getCardId(), Filter.Equality.EQUALS).getList()).size() > 0) {
	    			dao.update(dc);
	    		} else {
	    			dao.insert(dc);
	    		}
	    	}
	    	dao.commmitTransaction();
    	} catch (SQLException e) {
    		dao.rollbackTransaction();
			e.printStackTrace();
		}
    }
    
    public void doAfterCompose(Window window) throws Exception {
        super.doAfterCompose(window);
        deckId = (Integer)this.arg.get("deckId");
        CollectionCardViewDAO dao = new CollectionCardViewDAO();
    	List<CollectionCardView> cards = dao.findByCollectionId(3);
    	collection.setModel(new ListModelList<CollectionCardView>(cards));
    	
    	DeckCardViewDAO deckCardDAO = new DeckCardViewDAO();
    	List<DeckCardView> deckCards = deckCardDAO.find(FilterList.start("deck_id", deckId, Filter.Equality.EQUALS).getList());
    	deck.setModel(new ListModelList<DeckCardView>(deckCards));
    }
}
