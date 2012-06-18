package com.rms.collector.control;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listitem;
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

public class EditCollectionCardFormController extends GenericForwardComposer<Window> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Label cardName;
	private Listbox rarity, location;
	private Spinner amount;
	private Window editCollectionCardWin;
    private Label mesgLbl;
    
    public void onClick$confirm(Event event) {
        editCollectionCard();
    }
    
    public void onOK$editCollectionCardWin() {
    	editCollectionCard();
    }
    
    private void editCollectionCard() {
    	CardDAO cardDAO = new CardDAO();
    	cardDAO.startTransaction();
    	int id = (Integer)this.arg.get("collectionId");
    	final CollectionCardView ccv = (CollectionCardView)this.arg.get("collectionCardView");
    	try {
			if (!Util.isEmpty(cardName.getValue()) && rarity.getSelectedCount() > 0) {
				List<Filter> filters = new ArrayList<Filter>();
				filters.add(new Filter("name", cardName.getValue()));
	    		CollectionCardDAO ccDAO = new CollectionCardDAO();
	    		filters = new ArrayList<Filter>();
	    		filters.add(new Filter("collection_id", id));
	    		filters.add(Filter.AND);
	    		filters.add(new Filter("card_id", ccv.getCardId()));
	    		filters.add(Filter.AND);
	    		filters.add(new Filter("rarity", ccv.getRarity()));
	    		CollectionCard collectionCard = ccDAO.findSingle(filters);
	    		if (Util.isNotEmpty(collectionCard)) {
	    			collectionCard.setAmount(amount.getValue());
	    			collectionCard.setLocationId(((Location)location.getSelectedItem().getValue()).getId());
	    			collectionCard.setRarity(((Rarity)rarity.getSelectedItem().getValue()).getRarity());
	    			ccDAO.update(collectionCard);
	    		} else {
	    			mesgLbl.setValue("Could not find card.");
	    		}
	    		//new CardManager(ccv.getName()).start();
	    		editCollectionCardWin.detach();
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
    	} catch (Exception e) {
    		e.printStackTrace();
    		mesgLbl.setValue("An error has occurred.");
    		cardDAO.rollbackTransaction();
    	}
    }
    
    @Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		final CollectionCardView ccv = (CollectionCardView)this.arg.get("collectionCardView");
		RarityDAO rDAO = new RarityDAO();
		List<Rarity> rarities = rDAO.findAll();
		ListModelList<Rarity> model = new ListModelList<Rarity>(rarities);
		List<Rarity> selected = new LinkedList<Rarity>();
		for (Rarity r : model) {
			if (r.getRarity().equals(ccv.getRarity())) {
				selected.add(r);
			}
		}
		model.setSelection(selected);
		rarity.setModel(model);
		LocationDAO lDAO = new LocationDAO();
		List<Location> locations = lDAO.findByUserId(UserCredentialManager.getInstance().getUserLogin().getUserId());
		ListModelList<Location> locationModel = new ListModelList<Location>(locations);
		List<Location> locationSelection = new LinkedList<Location>();
		for (Location l : locationModel) {
			if (l.getName().equals(ccv.getLocationName())) {
				locationSelection.add(l);
			}
		}
		locationModel.setSelection(locationSelection);
		location.setModel(locationModel);
		cardName.setValue(ccv.getName());
		amount.setValue(ccv.getAmount());
	}
    
}
