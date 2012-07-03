package com.rms.collector.control;

import java.util.LinkedList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Box;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.rms.collector.data.CollectionCardViewDAO;
import com.rms.collector.data.CollectionDAO;
import com.rms.collector.model.Collection;
import com.rms.collector.model.view.CollectionCardView;
import com.rms.collector.util.Util;

public class CollectionFormController extends GenericForwardComposer<Window> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Textbox collectionName;
	private Window collectionWin;
    private Label mesgLbl;
    
    public void onClick$confirm(Event event) {
        createCollection();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void createCollection() {
    	try {
			if (!Util.isEmpty(collectionName.getValue()) ) {
	    		Collection collection = new Collection(collectionName.getValue(), UserCredentialManager.getInstance().getUserLogin().getUserId());
	    		CollectionDAO collectionDAO = new CollectionDAO();
	    		Integer id = (Integer)collectionDAO.insert(collection);
	    		collectionWin.detach();
	    		Listbox collectionList = (Listbox)this.arg.get("collectionList");
	    		collection.setId(id);
	    		((ListModelList)collectionList.getModel()).add(collection);
	    		((ListModelList)collectionList.getModel()).clearSelection();
	    		//collectionList.selectItem(collectionList.getItemAtIndex(collectionList.getItemCount() - 1));
	    		((ListModelList)collectionList.getModel()).addToSelection(collection);
	    		Listitem ref = collectionList.getSelectedItems().iterator().next();
	    		ref.setValue(collection);
	    		Events.sendEvent(collectionList, new SelectEvent<Listitem, Collection>(Events.ON_SELECT, collectionList, collectionList.getSelectedItems(), ref));
	    	} else {
	    		mesgLbl.setValue("Please enter a collection name.");
	    	}
    	} catch (Exception e) {
    		mesgLbl.setValue("An error has occurred.");
    		e.printStackTrace();
    	}
    }
}
