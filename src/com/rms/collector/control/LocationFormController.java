package com.rms.collector.control;

import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.rms.collector.data.CollectionDAO;
import com.rms.collector.data.LocationDAO;
import com.rms.collector.model.Collection;
import com.rms.collector.model.Location;
import com.rms.collector.util.Util;

public class LocationFormController extends GenericForwardComposer<Window> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Textbox locationName;
	private Window locationWin;
    private Label mesgLbl;
    
    public void onClick$confirm(Event event) {
        createCollection();
    }
    
    private void createCollection() {
    	try {
			if (!Util.isEmpty(locationName.getValue()) ) {
	    		Location location = new Location(UserCredentialManager.getInstance().getUserLogin().getUserId(), locationName.getValue());
	    		LocationDAO locationDAO = new LocationDAO();
	    		locationDAO.insert(location);
	    		locationWin.detach();
	    		Listbox locationList = (Listbox)this.arg.get("locationList");
	    		LocationDAO dao = new LocationDAO();
	        	List<Location> locations = dao.findByUserId(UserCredentialManager.getInstance().getUserLogin().getUserId());
	        	locationList.setModel(new ListModelList<Location>(locations));
	    	} else {
	    		mesgLbl.setValue("Please enter a location name.");
	    	}
    	} catch (Exception e) {
    		mesgLbl.setValue("An error has occurred.");
    		e.printStackTrace();
    	}
    }
}
