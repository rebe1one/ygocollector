package com.rms.collector.control;

import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Window;

import com.rms.collector.data.CollectionCardViewDAO;
import com.rms.collector.data.CollectionDAO;
import com.rms.collector.model.Collection;
import com.rms.collector.model.view.CollectionCardView;
import com.rms.collector.util.Util;

public class HomeViewController extends GenericForwardComposer<Borderlayout> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Listbox collectionList, collectionCardList;
	
	private Button addCollectionCard, refreshCollectionPrices;
	
	private Label collectionNameField, collectionTotalValueField, collectionHighestValueField;
	
	private Grid collectionInfo;

	public void onClick$createCollection() {
    	// popup create collection form
		Component comp = Executions.createComponents("createCollection.zul", null, null);
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
	
	public void onClick$refreshCollectionPrices() {
    	// popup create collection form
		HashMap<String, Object> args = new HashMap<String, Object>();
		int id = getSelectedCollectionId();
		args.put("collectionId", id);
		CollectionCardViewDAO dao = new CollectionCardViewDAO();
    	List<CollectionCardView> cards = dao.findByCollectionId(id);
    	CardManager manager = new CardManager(cards);
    	manager.start();
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
	    	
	    	collectionInfo.setVisible(true);
	    	addCollectionCard.setDisabled(false);
	    	refreshCollectionPrices.setDisabled(false);
    	} else {
    		collectionInfo.setVisible(false);
    		addCollectionCard.setDisabled(true);
    		refreshCollectionPrices.setDisabled(true);
    	}
    }
    
    public void doAfterCompose(Borderlayout borderlayout) throws Exception {
        super.doAfterCompose(borderlayout);
        CollectionDAO dao = new CollectionDAO();
    	List<Collection> collections = dao.findByUserId(UserCredentialManager.getInstance().getUserLogin().getUserId());
    	collectionList.setModel(new ListModelList<Collection>(collections));
    }
}
