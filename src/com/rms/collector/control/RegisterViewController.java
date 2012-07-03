package com.rms.collector.control;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.rms.collector.data.UserDAO;
import com.rms.collector.data.UserLoginDAO;
import com.rms.collector.model.User;
import com.rms.collector.model.UserLogin;
import com.rms.collector.util.Util;

public class RegisterViewController extends SelectorComposer<Window> {
	@Wire
    private Textbox firstNameTxb, lastNameTxb, emailTxb, userLoginTxb, passwordTxb;
     
    @Wire
    private Label mesgLbl;
    
    @Listen("onClick=#confirmBtn")
    public void confirm() {
        doRegister();
    }
    
    private void doRegister() {
    	try {
    		if (!Util.isEmpty(firstNameTxb.getValue()) && !Util.isEmpty(lastNameTxb.getValue())
        			&& !Util.isEmpty(userLoginTxb.getValue())
        			&& !Util.isEmpty(passwordTxb.getValue())) {
        		User user = new User(firstNameTxb.getValue(), lastNameTxb.getValue(), emailTxb.getValue());
        		UserDAO userDAO = new UserDAO();
        		userDAO.startTransaction();
        		try {
	        		Integer userId = userDAO.insert(user);
	        		
	        		MessageDigest md = MessageDigest.getInstance("SHA1");
	        		String passwordString = new String(passwordTxb.getValue());
	        		md.update(passwordString.getBytes());
	        		BigInteger hash = new BigInteger(1, md.digest());
	                String hashStr = hash.toString(16);
	        		
	        		UserLogin userLogin = new UserLogin(userId, userLoginTxb.getValue(), hashStr);
	        		UserLoginDAO userLoginDAO = new UserLoginDAO();
	        		userLoginDAO.insert(userLogin);
	        		userDAO.commmitTransaction();
	        		Executions.sendRedirect("/login.zul");
        		} catch (SQLException e) {
        			userDAO.rollbackTransaction();
        			e.printStackTrace();
        		}
        	}
    	} catch (NoSuchAlgorithmException e) {
    		e.printStackTrace();
    	}
    }
}
