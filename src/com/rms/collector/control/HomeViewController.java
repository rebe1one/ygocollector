package com.rms.collector.control;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Box;
import org.zkoss.zul.Button;
import org.zkoss.zul.Center;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.rms.collector.data.CardDAO;
import com.rms.collector.data.CollectionCardDAO;
import com.rms.collector.data.CollectionCardViewDAO;
import com.rms.collector.data.CollectionDAO;
import com.rms.collector.data.LocationDAO;
import com.rms.collector.data.UserDAO;
import com.rms.collector.model.Collection;
import com.rms.collector.model.CollectionCard;
import com.rms.collector.model.Location;
import com.rms.collector.model.User;
import com.rms.collector.model.view.CollectionCardView;
import com.rms.collector.util.Filter;
import com.rms.collector.util.Util;

public class HomeViewController extends GenericForwardComposer<Borderlayout> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Listbox collectionList, locationList, collectionCardList;
	
	private Button addCollectionCard, refreshCollectionPrices, removeCollection;
	
	private Label collectionNameField, collectionTotalValueField, collectionHighestValueField, collectionTotalNumberField, welcomeLabel;
	
	private Grid collectionInfo;
	
	private Box collectionBox, emptyBox;
	
	private void showCollectionInfo() {
		emptyBox.setVisible(false);
		collectionBox.setVisible(true);
	}
	
	private void hideCollectionInfo() {
		emptyBox.setVisible(true);
		collectionBox.setVisible(false);
	}

	public void onClick$createCollection() {
    	// popup create collection form
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("collectionList", collectionList);
		args.put("collectionCardList", collectionCardList);
		args.put("collectionNameField", collectionNameField);
		args.put("collectionTotalValueField", collectionTotalValueField);
		args.put("collectionHighestValueField", collectionHighestValueField);
		args.put("emptyBox", emptyBox);
		args.put("collectionBox", collectionBox);
		Component comp = Executions.createComponents("createCollection.zul", null, args);
		if(comp instanceof Window) {
            ((Window)comp).doModal();
        }
    }
	
	public void onClick$logout() {
		UserCredentialManager.getInstance().logout();
	}
	
	public void onClick$createLocation() {
    	// popup create collection form
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("locationList", locationList);
		Component comp = Executions.createComponents("createLocation.zul", null, args);
		if(comp instanceof Window) {
            ((Window)comp).doModal();
        }
    }
	
	public void onClick$addCollectionCard() {
    	// popup create collection form
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("collectionId", getSelectedCollectionId());
		args.put("collectionCardList", collectionCardList);
		args.put("collectionTotalValueField", collectionTotalValueField);
		args.put("collectionHighestValueField", collectionHighestValueField);
		Component comp = Executions.createComponents("addCollectionCard.zul", null, args);
		if(comp instanceof Window) {
            ((Window)comp).doModal();
        }
    }
	
	public void onClick$removeCollection() {
    	// popup create collection form
		Messagebox.show("Are you sure you want to remove this collection?", 
			    "Remove Collection?", Messagebox.OK | Messagebox.CANCEL,
			    Messagebox.QUESTION,
			        new EventListener<Event>(){
			            public void onEvent(Event e){
			                if(Messagebox.ON_OK.equals(e.getName())){
			                    CollectionCardDAO ccDAO = new CollectionCardDAO();
			                    CollectionDAO cDAO = new CollectionDAO();
			                    ccDAO.startTransaction();
			                    List<CollectionCard> cards = ccDAO.find(Filter.simpleFilter("collection_id", getSelectedCollectionId()));
		                    	try {
				                    for (CollectionCard card : cards) {
										ccDAO.delete(card);
				                    }
				                    Collection c = cDAO.findSingle(Filter.simpleFilter("id", getSelectedCollectionId()));
				                    cDAO.delete(c);
				                    ccDAO.commmitTransaction();
								} catch (SQLException e1) {
									ccDAO.rollbackTransaction();
									e1.printStackTrace();
								}
		                    	collectionCardList.setModel(new ListModelList<CollectionCardView>());
		                    	List<Collection> collections = cDAO.findByUserId(UserCredentialManager.getInstance().getUserLogin().getUserId());
		                    	collectionList.setModel(new ListModelList<Collection>(collections));
		                    	hideCollectionInfo();
			                }else if(Messagebox.ON_CANCEL.equals(e.getName())){
			                    //Cancel is clicked
			                }
			            }
			        }
			    );
    }
	
	public void onClick$refreshAllCollectionPrices() {
    	// popup create collection form
		HashMap<String, Object> args = new HashMap<String, Object>();
		int id = getSelectedCollectionId();
		args.put("collectionId", id);
		CollectionCardViewDAO dao = new CollectionCardViewDAO();
    	List<CollectionCardView> cards = dao.findByCollectionId(id);
    	PriceLookupQueue.getInstance().addCards(cards);
    }
	
	public void onClick$refreshMissingCollectionPrices() {
    	// popup create collection form
		HashMap<String, Object> args = new HashMap<String, Object>();
		int id = getSelectedCollectionId();
		args.put("collectionId", id);
		CollectionCardViewDAO dao = new CollectionCardViewDAO();
    	List<CollectionCardView> cards = dao.findByCollectionId(id);
    	LinkedList<CollectionCardView> noPrice = new LinkedList<CollectionCardView>();
    	for (CollectionCardView ccv : cards)
    		if (Util.isEmpty(ccv.getPrice()))
    			noPrice.add(ccv);
    	PriceLookupQueue.getInstance().addCards(noPrice);
    }
	
	private int getSelectedCollectionId() {
		return Integer.valueOf(((Listcell) collectionList.getSelectedItem().getFirstChild()).getLabel());
	}
	
	public void onClick$collectionList() {
    	if (Util.isNotEmpty(collectionList.getSelectedItem())) {
	    	int id = getSelectedCollectionId();
	    	CollectionCardViewDAO dao = new CollectionCardViewDAO();
	    	List<CollectionCardView> visits = dao.findByCollectionId(id);
	    	collectionCardList.setModel(new ListModelList<CollectionCardView>(visits));
	    	
	    	CollectionManager cm = new CollectionManager(id);
	    	collectionTotalValueField.setValue(cm.getTotalPrice().toPlainString());
	    	collectionHighestValueField.setValue(cm.getHighestPrice().toPlainString());
	    	collectionNameField.setValue(cm.getCollectionName());
	    	collectionTotalNumberField.setValue(cm.getTotalNumberOfCards().toString());
	    	
	    	showCollectionInfo();
    	} else {
    		hideCollectionInfo();
    	}
    }
    
    public void doAfterCompose(Borderlayout borderlayout) throws Exception {
        super.doAfterCompose(borderlayout);
        CollectionDAO dao = new CollectionDAO();
    	List<Collection> collections = dao.findByUserId(UserCredentialManager.getInstance().getUserLogin().getUserId());
    	collectionList.setModel(new ListModelList<Collection>(collections));
    	LocationDAO locationDAO = new LocationDAO();
    	List<Location> locations = locationDAO.findByUserId(UserCredentialManager.getInstance().getUserLogin().getUserId());
    	locationList.setModel(new ListModelList<Location>(locations));
    	User user = new UserDAO().findSingle(Filter.simpleFilter("id", UserCredentialManager.getInstance().getUserLogin().getUserId()));
    	welcomeLabel.setValue("Sup " + user.getFirstName() + "!");
    }
}
