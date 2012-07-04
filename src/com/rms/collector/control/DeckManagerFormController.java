package com.rms.collector.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import com.rms.collector.data.CardDAO;
import com.rms.collector.data.CollectionCardViewDAO;
import com.rms.collector.data.DeckCardDAO;
import com.rms.collector.data.DeckCardViewDAO;
import com.rms.collector.model.Card;
import com.rms.collector.model.view.DeckCardView;
import com.rms.collector.model.view.CollectionCardView;
import com.rms.collector.util.Filter;
import com.rms.collector.util.FilterList;
import com.rms.collector.util.SubFilter;
import com.rms.collector.util.Util;

public class DeckManagerFormController extends GenericForwardComposer<Window> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Listbox collection, deck;
    private Label mesgLbl;
    private Integer deckId;
    private List<Listitem> toRemove= new LinkedList<Listitem>();
    private Combobox cardName;
    
    public void onClick$toDeck(Event event) {
        for (Listitem item : collection.getSelectedItems()) {
        	CollectionCardView card = (CollectionCardView)item.getValue();
        	System.out.println(card.getCardId());
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
        	toRemove.add(item);
        	((ListModelList)deck.getModel()).remove(item.getIndex());
        }
    }
    
    public void onChanging$cardName(InputEvent event) {
    	if (Util.isNotEmpty(event.getValue())) {
    		cardName.getChildren().clear();
	    	CardDAO cDAO = new CardDAO();
	    	System.out.println("Event data: " + event.getValue());
			List<Card> cards = cDAO.find(FilterList.start("name", event.getValue() + "%", Filter.Equality.LIKE).limit(10).getList());
			for (Card card : cards)
				cardName.appendItem(card.getName());
    	}
    }
    
    public void onChanging$searchCollection(InputEvent event) {
		CollectionCardViewDAO dao = new CollectionCardViewDAO();
    	List<CollectionCardView> cards = dao.find(
    			FilterList.start("name", "%" + event.getValue() + "%", Filter.Equality.LIKE)
    				.or("attribute", "%" + event.getValue() + "%", Filter.Equality.LIKE)
    				.and("user_id", UserCredentialManager.getInstance().getUserLogin().getUserId(), 
    							Filter.Equality.EQUALS));
    	collection.setModel(new ListModelList<CollectionCardView>(cards));
    }
    
    public void onClick$addCard() {
		if (!Util.isEmpty(cardName.getValue())) {
			List<Filter> filters = new ArrayList<Filter>();
			filters.add(new Filter("name", cardName.getValue()));
			Card card = new CardDAO().findSingle(filters);
			if (Util.isEmpty(card)) {
				this.alert("Could not find card!");
				return;
			}
			DeckCardView deckCard = new DeckCardView();
        	deckCard.setCardId(card.getId());
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
    
    public void onIncreaseCardCount(ForwardEvent event) {
    	Listitem deckItem = ((Listitem)event.getOrigin().getTarget().getParent().getParent());
    	DeckCardView dc = deckItem.getValue();
    	if (dc.getAmount() + 1 == 0) {
    		((ListModelList)deck.getModel()).remove(deckItem.getIndex());
    	} else if (dc.getAmount() + 1 <= 3) {
	    	dc.setAmount(dc.getAmount() + 1);
			int index = deckItem.getIndex();
			((ListModelList)deck.getModel()).remove(index);
			((ListModelList)deck.getModel()).add(index, dc);
    	}
    }
    
    public void onDecreaseCardCount(ForwardEvent event) {
    	Listitem deckItem = ((Listitem)event.getOrigin().getTarget().getParent().getParent());
    	DeckCardView dc = deckItem.getValue();
    	if (dc.getAmount() - 1 == 0) {
    		((ListModelList)deck.getModel()).remove(deckItem.getIndex());
    	} else if (dc.getAmount() - 1 <= 3) {
	    	dc.setAmount(dc.getAmount() + 1);
			int index = deckItem.getIndex();
			((ListModelList)deck.getModel()).remove(index);
			((ListModelList)deck.getModel()).add(index, dc);
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
	    	for (Listitem item : toRemove) {
	    		DeckCardView dc = (DeckCardView)item.getValue();
	    		if (dao.find(FilterList.start("deck_id", dc.getDeckId(), Filter.Equality.EQUALS).and("card_id", dc.getCardId(), Filter.Equality.EQUALS).getList()).size() > 0) {
	    			dao.delete(dc);
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
        List<CollectionCardView> cards = dao.find(
    			FilterList.start("user_id", UserCredentialManager.getInstance().getUserLogin().getUserId(), 
    							Filter.Equality.EQUALS));
    	collection.setModel(new ListModelList<CollectionCardView>(cards));
    	
    	DeckCardViewDAO deckCardDAO = new DeckCardViewDAO();
    	List<DeckCardView> deckCards = deckCardDAO.find(FilterList.start("deck_id", deckId, Filter.Equality.EQUALS).getList());
    	deck.setModel(new ListModelList<DeckCardView>(deckCards));
    }
}
