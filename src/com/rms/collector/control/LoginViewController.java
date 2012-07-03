package com.rms.collector.control;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.rms.collector.data.UserLoginDAO;
import com.rms.collector.model.UserLogin;
import com.rms.collector.util.Util;

public class LoginViewController extends GenericForwardComposer<Window> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Textbox nameTxb, passwordTxb;
    
    private Label mesgLbl;
    
    private Button confirmBtn;
    
    public void onClick$confirmBtn() {
        doLogin();
    }
    
    public void onOK$loginWin() {
    	doLogin();
    }
    
    private boolean doLogin() {
    	try {
    		if (Util.isEmpty(passwordTxb.getValue()) | Util.isEmpty(nameTxb.getValue())) return false;
    		MessageDigest md = MessageDigest.getInstance("SHA1");
    		String passwordString = new String(passwordTxb.getValue());
    		md.update(passwordString.getBytes());
    		BigInteger hash = new BigInteger(1, md.digest());
            String hashStr = hash.toString(16);
    		
    		UserLogin userLogin = new UserLogin();
    		userLogin.setUserLogin(nameTxb.getValue());
    		UserLoginDAO userLoginDAO = new UserLoginDAO();
    		userLogin = userLoginDAO.findByLogin(userLogin);
    		
    		if (Util.isEmpty(userLogin)) {
    			mesgLbl.setValue("Wrong username or password!");
    		} else {
	    		if (hashStr.equals(userLogin.getPassword())) {
	    			UserCredentialManager.getInstance().authenticate(userLogin);
	    			confirmBtn.setImage("/images/icons/lock_open.png");
	    			mesgLbl.setValue("Loading... ");
	    			Executions.sendRedirect("/home.zul");
	    		} else {
	    			mesgLbl.setValue("Wrong username or password!");
	    		}
    		}
    		return false;
    	} catch (NoSuchAlgorithmException e) {
    		e.printStackTrace();
    		return false;
    	}
    }
    
    public void doAfterCompose(Window window) throws Exception {
        super.doAfterCompose(window);
        confirmBtn.setImage("/images/icons/lock.png");
    }
}
