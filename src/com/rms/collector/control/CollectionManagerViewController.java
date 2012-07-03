package com.rms.collector.control;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import com.rms.collector.data.CollectionCardViewDAO;
import com.rms.collector.model.view.CollectionCardView;
import com.rms.collector.util.Util;

public class CollectionManagerViewController extends GenericForwardComposer<Window> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Listbox collectionCardList;
	private Label collectionNameField, collectionTotalValueField, collectionHighestValueField, collectionTotalNumberField;
	
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
    		if (Util.isEmpty(ccv.getPrice())) {
    			noPrice.add(ccv);
    		}
    	PriceLookupQueue.getInstance().addCards(noPrice);
    }
	
	private int getSelectedCollectionId() {
		return (Integer)this.arg.get("id");
	}
    
    public void doAfterCompose(Window window) throws Exception {
        super.doAfterCompose(window);
        int id = getSelectedCollectionId();
    	CollectionCardViewDAO dao = new CollectionCardViewDAO();
    	List<CollectionCardView> visits = dao.findByCollectionId(id);
    	collectionCardList.setModel(new ListModelList<CollectionCardView>(visits));
    	
    	CollectionManager cm = new CollectionManager(id);
    	collectionTotalValueField.setValue(cm.getTotalPrice().toPlainString());
    	collectionHighestValueField.setValue(cm.getHighestPrice().toPlainString());
    	collectionNameField.setValue(cm.getCollectionName());
    	collectionTotalNumberField.setValue(cm.getTotalNumberOfCards().toString());
    }
}
