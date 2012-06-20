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

public class CollectionViewController extends GenericForwardComposer<Borderlayout> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Listbox collectionCardList;
	
	private Label collectionNameField, collectionTotalValueField, collectionHighestValueField, collectionTotalNumberField;
	
    public void doAfterCompose(Borderlayout borderlayout) throws Exception {
        super.doAfterCompose(borderlayout);
        if (Util.isNotEmpty(execution.getParameter("id"))) {
	        int id = Integer.valueOf(execution.getParameter("id"));
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
}
