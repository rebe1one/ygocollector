package com.rms.collector.control;

import java.util.LinkedList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Box;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
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
    
    private void createCollection() {
    	try {
			if (!Util.isEmpty(collectionName.getValue()) ) {
	    		Collection collection = new Collection(collectionName.getValue(), UserCredentialManager.getInstance().getUserLogin().getUserId());
	    		CollectionDAO collectionDAO = new CollectionDAO();
	    		Integer id = (Integer)collectionDAO.insert(collection);
	    		collectionWin.detach();
	    		Listbox collectionList = (Listbox)this.arg.get("collectionList");
	    		CollectionDAO dao = new CollectionDAO();
	        	List<Collection> collections = dao.findByUserId(UserCredentialManager.getInstance().getUserLogin().getUserId());
	        	ListModelList<Collection> model = new ListModelList<Collection>(collections);
	        	Collection selected = null;
	        	for (Collection c : model) {
	        		if (c.getId() == id) selected = c;
	        	}
	    		model.addToSelection(selected);
	        	collectionList.setModel(model);
	        	CollectionCardViewDAO ccvDAO = new CollectionCardViewDAO();
		    	List<CollectionCardView> visits = ccvDAO.findByCollectionId(id);
		    	((Listbox)this.arg.get("collectionCardList")).setModel(new ListModelList<CollectionCardView>(visits));
		    	
		    	CollectionManager cm = new CollectionManager(id);
		    	((Label)this.arg.get("collectionNameField")).setValue(cm.getCollectionName());
		    	((Label)this.arg.get("collectionTotalValueField")).setValue(cm.getTotalPrice().toPlainString());
		    	((Label)this.arg.get("collectionHighestValueField")).setValue(cm.getHighestPrice().toPlainString());
		    	((Box)this.arg.get("emptyBox")).setVisible(false);
		    	((Box)this.arg.get("collectionBox")).setVisible(true);
	    	} else {
	    		mesgLbl.setValue("Please enter a collection name.");
	    	}
    	} catch (Exception e) {
    		mesgLbl.setValue("An error has occurred.");
    		e.printStackTrace();
    	}
    }
}
