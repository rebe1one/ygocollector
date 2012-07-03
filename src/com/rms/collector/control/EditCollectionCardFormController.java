package com.rms.collector.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.SelectionEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Listbox;

import com.rms.collector.data.CardDAO;
import com.rms.collector.data.CardImageDAO;
import com.rms.collector.data.CollectionCardDAO;
import com.rms.collector.data.CollectionCardViewDAO;
import com.rms.collector.data.CollectionDAO;
import com.rms.collector.data.LocationDAO;
import com.rms.collector.data.PriceDAO;
import com.rms.collector.data.RarityDAO;
import com.rms.collector.model.Card;
import com.rms.collector.model.CardImage;
import com.rms.collector.model.Collection;
import com.rms.collector.model.CollectionCard;
import com.rms.collector.model.Location;
import com.rms.collector.model.Price;
import com.rms.collector.model.Rarity;
import com.rms.collector.model.view.CollectionCardView;
import com.rms.collector.model.view.PriceSourceView;
import com.rms.collector.util.Filter;
import com.rms.collector.util.FilterList;
import com.rms.collector.util.Util;

public class EditCollectionCardFormController extends GenericForwardComposer<Window> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Label cardName;
	private Listbox rarity, location, priceList;
	private Spinner amount;
	private Window editCollectionCardWin;
    private Label mesgLbl;
    private Image cardImage;
    
    public void onClick$confirm(Event event) {
        editCollectionCard();
    }
    
    public void onClick$newPrice(Event event) {
    	HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("collectionCardView", this.arg.get("collectionCardView"));
		args.put("priceList", priceList);
		Executions.createComponents("createPrice.zul", null, args);
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
	    		CollectionCardDAO ccDAO = new CollectionCardDAO();
	    		ccDAO.delete(ccv);
	    		ccv.setAmount(amount.getValue());
	    		ccv.setLocationId(((Location)location.getSelectedItem().getValue()).getId());
	    		if (Util.isNotEmpty(priceList.getSelectedItem())) {
	    			PriceSourceView psv = (PriceSourceView)priceList.getSelectedItem().getValue();
	    			ccv.setPriceSourceId(psv.getSourceId());
	    			ccv.setSetId(psv.getSetId());
	    		}
	    		ccv.setRarity(((Rarity)rarity.getSelectedItem().getValue()).getRarity());
	    		ccDAO.insert(ccv);
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
    
    private static String generateImageUrl(int card_id) {
    	CardImageDAO dao = new CardImageDAO();
    	CardImage image = dao.findSingle(FilterList.start("card_id", card_id, Filter.Equality.EQUALS).getList());
    	if (Util.isNotEmpty(image)) {
    		StringBuilder b = new StringBuilder("images/cards/");
        	b.append(image.getImageFileName());
        	return b.toString();
    	} else {
    		return "images/no_card.jpg";
    	}
    }
    
    public void onSelect$rarity(Event e) {
    	CollectionCardView ccv = (CollectionCardView)this.arg.get("collectionCardView");
    	List<PriceSourceView> prices = new PriceDAO().findLatestCardPrices(ccv.getCardId(), ((Rarity)rarity.getSelectedItem().getValue()).getRarity());
		ListModelList<PriceSourceView> priceModel = new ListModelList<PriceSourceView>(prices);
		List<PriceSourceView> priceSelection = new LinkedList<PriceSourceView>();
		for (PriceSourceView p : priceModel) {
			if (p.getSetId().equals(ccv.getSetId())) {
				priceSelection.add(p);
			}
		}
		priceModel.setSelection(priceSelection);
		priceList.setModel(priceModel);
    }
    
    @Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		CollectionCardView ccv = (CollectionCardView)this.arg.get("collectionCardView");
		cardImage.setSrc(generateImageUrl(ccv.getCardId()));
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
		
		List<PriceSourceView> prices = new PriceDAO().findLatestCardPrices(ccv.getCardId(), ccv.getRarity());
		ListModelList<PriceSourceView> priceModel = new ListModelList<PriceSourceView>(prices);
		List<PriceSourceView> priceSelection = new LinkedList<PriceSourceView>();
		for (PriceSourceView p : priceModel) {
			if (p.getSetId().equals(ccv.getSetId())) {
				priceSelection.add(p);
			}
		}
		priceModel.setSelection(priceSelection);
		priceList.setModel(priceModel);
	}
    
}
