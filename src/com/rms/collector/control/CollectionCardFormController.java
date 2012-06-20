package com.rms.collector.control;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Listbox;

import com.rms.collector.data.CardDAO;
import com.rms.collector.data.CollectionCardDAO;
import com.rms.collector.data.CollectionCardViewDAO;
import com.rms.collector.data.CollectionDAO;
import com.rms.collector.data.LocationDAO;
import com.rms.collector.data.RarityDAO;
import com.rms.collector.model.Card;
import com.rms.collector.model.Collection;
import com.rms.collector.model.CollectionCard;
import com.rms.collector.model.Location;
import com.rms.collector.model.Rarity;
import com.rms.collector.model.view.CollectionCardView;
import com.rms.collector.util.Filter;
import com.rms.collector.util.Util;

public class CollectionCardFormController extends GenericForwardComposer<Window> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Textbox cardName;
	private Listbox rarity, location;
	private Spinner amount;
	private Window createCollectionCardWin;
    private Label mesgLbl;
    
    public void onClick$confirm(Event event) {
        createCollectionCard();
    }
    
    public void onClick$confirmAndContinue(Event event) {
    	createCollectionCardAndContinue();
    }
    
    public void onOK$createCollectionCardWin() {
    	createCollectionCardAndContinue();
    }
    
    private void createCollectionCardAndContinue() {
    	CardDAO cardDAO = new CardDAO();
    	cardDAO.startTransaction();
    	int id = (Integer)this.arg.get("collectionId");
    	try {
			if (!Util.isEmpty(cardName.getValue()) && rarity.getSelectedCount() > 0) {
				if (location.getSelectedCount() == 0) {
					location.setSelectedIndex(0);
				}
				List<Filter> filters = new ArrayList<Filter>();
				filters.add(new Filter("name", cardName.getValue()));
				Card card = cardDAO.findSingle(filters);
	    		CollectionCard cc = new CollectionCard(id, 
	    				card.getId(), amount.getValue(), 
	    				((Rarity)rarity.getSelectedItem().getValue()).getRarity(),
	    				((Location)location.getSelectedItem().getValue()).getId(),
	    				0, "");
	    		CollectionCardDAO ccDAO = new CollectionCardDAO();
	    		filters = new ArrayList<Filter>();
	    		filters.add(new Filter("collection_id", cc.getCollectionId()));
	    		filters.add(Filter.AND);
	    		filters.add(new Filter("card_id", cc.getCardId()));
	    		filters.add(Filter.AND);
	    		filters.add(new Filter("rarity", cc.getRarity()));
	    		CollectionCard collectionCard = ccDAO.findSingle(filters);
	    		CollectionCardView ccv = null;
	    		if (Util.isNotEmpty(collectionCard)) {
	    			collectionCard.setAmount(collectionCard.getAmount() + amount.getValue());
	    			ccDAO.update(collectionCard);
	    			ccv = CollectionCardView.createViewFromCard(collectionCard);
	    		} else {
	    			ccDAO.insert(cc);
	    			ccv = CollectionCardView.createViewFromCard(cc);
	    		}
	    		ccv.setName(cardName.getValue());
	    		PriceLookupQueue.getInstance().addCard(ccv);
	    		cardName.setValue("");
	    		amount.setValue(1);
	    		cardName.setFocus(true);
	    	} else {
	    		mesgLbl.setValue("Please enter a card name.");
	    	}
			cardDAO.commmitTransaction();
			mesgLbl.setValue("");
    	} catch (Exception e) {
    		e.printStackTrace();
    		mesgLbl.setValue("An error has occurred.");
    		cardDAO.rollbackTransaction();
    	}
    }
    
    private void createCollectionCard() {
    	CardDAO cardDAO = new CardDAO();
    	cardDAO.startTransaction();
    	int id = (Integer)this.arg.get("collectionId");
    	try {
			if (!Util.isEmpty(cardName.getValue()) && rarity.getSelectedCount() > 0) {
				if (location.getSelectedCount() == 0) {
					location.setSelectedIndex(0);
				}
				List<Filter> filters = new ArrayList<Filter>();
				filters.add(new Filter("name", cardName.getValue()));
				Card card = cardDAO.findSingle(filters);
	    		CollectionCard cc = new CollectionCard(id, 
	    				card.getId(), amount.getValue(), 
	    				((Rarity)rarity.getSelectedItem().getValue()).getRarity(),
	    				((Location)location.getSelectedItem().getValue()).getId(),
	    				0, "");
	    		CollectionCardDAO ccDAO = new CollectionCardDAO();
	    		filters = new ArrayList<Filter>();
	    		filters.add(new Filter("collection_id", cc.getCollectionId()));
	    		filters.add(Filter.AND);
	    		filters.add(new Filter("card_id", cc.getCardId()));
	    		filters.add(Filter.AND);
	    		filters.add(new Filter("rarity", cc.getRarity()));
	    		CollectionCard collectionCard = ccDAO.findSingle(filters);
	    		CollectionCardView ccv = null;
	    		if (Util.isNotEmpty(collectionCard)) {
	    			collectionCard.setAmount(collectionCard.getAmount() + amount.getValue());
	    			ccDAO.update(collectionCard);
	    			ccv = CollectionCardView.createViewFromCard(collectionCard);
	    		} else {
	    			ccDAO.insert(cc);
	    			ccv = CollectionCardView.createViewFromCard(cc);
	    		}
	    		ccv.setName(cardName.getValue());
	    		PriceLookupQueue.getInstance().addCard(ccv);
	    		createCollectionCardWin.detach();
	    	} else {
	    		mesgLbl.setValue("Please enter a card name.");
	    	}
			cardDAO.commmitTransaction();
			
			CollectionCardViewDAO dao = new CollectionCardViewDAO();
	    	List<CollectionCardView> visits = dao.findByCollectionId(id);
	    	((Listbox)this.arg.get("collectionCardList")).setModel(new ListModelList<CollectionCardView>(visits));
	    	
	    	CollectionManager cm = new CollectionManager(id);
	    	((Label)this.arg.get("collectionTotalValueField")).setValue(cm.getTotalPrice().toPlainString());
	    	((Label)this.arg.get("collectionHighestValueField")).setValue(cm.getHighestPrice().toPlainString());
	    	mesgLbl.setValue("");
    	} catch (Exception e) {
    		e.printStackTrace();
    		mesgLbl.setValue("An error has occurred.");
    		cardDAO.rollbackTransaction();
    	}
    }
    
    @Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		RarityDAO rDAO = new RarityDAO();
		List<Rarity> rarities = rDAO.findAll();
		rarity.setModel(new ListModelList<Rarity>(rarities));
		LocationDAO lDAO = new LocationDAO();
		List<Location> locations = lDAO.findByUserId(UserCredentialManager.getInstance().getUserLogin().getUserId());
		location.setModel(new ListModelList<Location>(locations));
	}
    
}
