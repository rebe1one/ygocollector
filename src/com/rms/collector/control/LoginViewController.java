package com.rms.collector.control;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.rms.collector.data.UserLoginDAO;
import com.rms.collector.model.UserLogin;

public class LoginViewController extends SelectorComposer<Window> {
	@Wire
    private Textbox nameTxb, passwordTxb;
    
    @Wire
    private Label mesgLbl;
    
    @Listen("onClick=#confirmBtn")
    public void confirm() {
        doLogin();
    }
    
    private boolean doLogin() {
    	try {
    		MessageDigest md = MessageDigest.getInstance("SHA1");
    		String passwordString = new String(passwordTxb.getValue());
    		md.update(passwordString.getBytes());
    		BigInteger hash = new BigInteger(1, md.digest());
            String hashStr = hash.toString(16);
    		
    		UserLogin userLogin = new UserLogin();
    		userLogin.setUserLogin(nameTxb.getValue());
    		UserLoginDAO userLoginDAO = new UserLoginDAO();
    		userLogin = userLoginDAO.findByLogin(userLogin);
    		
    		if (userLogin.getPassword().equals(hashStr)) {
    			UserCredentialManager.getInstance().authenticate(userLogin);
    			mesgLbl.setValue("Success!");
    			Executions.sendRedirect("/home.zul");
    		}
    		return false;
    	} catch (NoSuchAlgorithmException e) {
    		e.printStackTrace();
    		return false;
    	}
    }
    
    public void doAfterCompose(Window window) throws Exception {
        super.doAfterCompose(window);
     
        //to be implemented, let’s check for a login
    }
}
