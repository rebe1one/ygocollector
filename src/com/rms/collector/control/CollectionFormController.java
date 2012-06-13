package com.rms.collector.control;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.rms.collector.data.CollectionDAO;
import com.rms.collector.model.Collection;
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
    
    private void createCollection() {
    	try {
			if (!Util.isEmpty(collectionName.getValue()) ) {
	    		Collection collection = new Collection(collectionName.getValue(), UserCredentialManager.getInstance().getUserLogin().getUserId());
	    		CollectionDAO collectionDAO = new CollectionDAO();
	    		collectionDAO.insert(collection);
	    		collectionWin.detach();
	    	} else {
	    		mesgLbl.setValue("Please enter a collection name.");
	    	}
    	} catch (Exception e) {
    		mesgLbl.setValue("An error has occurred.");
    	}
    }
}
