package com.rms.collector.control;

import java.sql.SQLException;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.rms.collector.data.CollectionDAO;
import com.rms.collector.data.LocationDAO;
import com.rms.collector.model.Collection;
import com.rms.collector.model.Location;
import com.rms.collector.util.Util;

public class StartWizardFormController extends GenericForwardComposer<Window> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Textbox collectionName;
	private Window startWizard;
	private Vlayout step1, step2, step3;
	private Image checkImg;
	private Progressmeter wizardProgress;
    
    public void onClick$next(Event event) {
    	CollectionDAO dao = new CollectionDAO();
    	Collection entity = new Collection();
    	entity.setName(collectionName.getValue());
    	entity.setUserId(UserCredentialManager.getInstance().getUserLogin().getUserId());
    	try {
			dao.insert(entity);
			Label name = new Label(entity.getName());
			name.setParent(collectionName.getParent());
			collectionName.detach();
			checkImg.setVisible(true);
			wizardProgress.setValue(50);
			step1.setVisible(false);
			step2.setVisible(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    private Textbox locationName;
	private Listbox locationList;
    
    public void onClick$next2(Event event) {
    	LocationDAO dao = new LocationDAO();
    	Location entity = new Location();
    	entity.setUserId(UserCredentialManager.getInstance().getUserLogin().getUserId());
    	try {
    		for (Listitem item : locationList.getItems()) {
    			entity.setName(item.getLabel());
    			dao.insert(entity);
    		}
    		wizardProgress.setValue(100);
    		step2.setVisible(false);
			step3.setVisible(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public void onClick$next3(Event event) {
    	startWizard.detach();
		EventQueues.lookup("homeRefreshEventQueue", EventQueues.DESKTOP, true)
			.publish(new Event("onChangeBackend", null, null));
    }
    
    public void onClick$createLocation(Event event) {
    	if (Util.isNotEmpty(locationName.getValue())) {
	    	Listitem item = new Listitem(locationName.getValue());
	    	locationList.appendChild(item);
	    	locationName.setValue("");
	    	locationName.setFocus(true);
    	}
    }
    
    @Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
	}
    
}
