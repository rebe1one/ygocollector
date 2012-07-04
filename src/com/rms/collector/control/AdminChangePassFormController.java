package com.rms.collector.control;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Listbox;

import com.rms.collector.data.CardDAO;
import com.rms.collector.data.CollectionCardDAO;
import com.rms.collector.data.CollectionCardViewDAO;
import com.rms.collector.data.CollectionDAO;
import com.rms.collector.data.LocationDAO;
import com.rms.collector.data.UserDAO;
import com.rms.collector.data.UserLoginDAO;
import com.rms.collector.data.UserLoginViewDAO;
import com.rms.collector.model.Card;
import com.rms.collector.model.Collection;
import com.rms.collector.model.CollectionCard;
import com.rms.collector.model.Location;
import com.rms.collector.model.UserLogin;
import com.rms.collector.model.UserLoginView;
import com.rms.collector.model.view.CollectionCardView;
import com.rms.collector.util.Filter;
import com.rms.collector.util.FilterList;
import com.rms.collector.util.Util;

public class AdminChangePassFormController extends GenericForwardComposer<Window> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Listbox user;
	private Textbox password;
	private Window adminChangePass;
    
    public void onClick$confirm(Event event) {
    	UserLoginView ulv = user.getSelectedItem().getValue();
    	UserDAO userDAO = new UserDAO();
		userDAO.startTransaction();
		try {
    		MessageDigest md = MessageDigest.getInstance("SHA1");
    		String passwordString = new String(password.getValue());
    		md.update(passwordString.getBytes());
    		BigInteger hash = new BigInteger(1, md.digest());
            String hashStr = hash.toString(16);
    		
            ulv.setPassword(hashStr);
    		UserLoginDAO userLoginDAO = new UserLoginDAO();
    		userLoginDAO.update(new UserLogin(ulv.getId(), ulv.getUserLogin(), hashStr));
    		userDAO.commmitTransaction();
    		adminChangePass.detach();
		} catch (Exception e) {
			userDAO.rollbackTransaction();
			e.printStackTrace();
		}
    }
    
    
    @Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		UserLoginViewDAO dao = new UserLoginViewDAO();
		List<UserLoginView> rarities = dao.findAll();
		user.setModel(new ListModelList<UserLoginView>(rarities));
	}
    
}
