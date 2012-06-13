package com.rms.collector.control;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import com.rms.collector.data.UserDAO;
import com.rms.collector.model.User;
import com.rms.collector.model.UserLogin;

public class UserCredentialManager {
	 
    private static final String KEY_USER_MODEL = UserCredentialManager.class.getName()+"_MODEL";
    private UserDAO userDAO;
    private UserLogin user;
     
    private UserCredentialManager(){
        userDAO = new UserDAO();
    }
     
    public static UserCredentialManager getInstance() {
        return getInstance(Sessions.getCurrent());
    }
    /**
     * 
     * @return
     */
    public static UserCredentialManager getInstance(Session zkSession){
        synchronized(zkSession){
            UserCredentialManager userModel = (UserCredentialManager) zkSession.getAttribute(KEY_USER_MODEL);
            if(userModel==null){
                zkSession.setAttribute(KEY_USER_MODEL, userModel = new UserCredentialManager());
            }
            return userModel;
        }
    }
    
    public void authenticate(UserLogin user) {
    	this.user = user;
    }
    
    public boolean isAuthenticated() {
    	if (user != null) {
    		return true;
    	}
    	return false;
    }
    
    public void logout() {
    	user = null;
    	Executions.sendRedirect("/index.zul");
    }
    
    public UserLogin getUserLogin() {
    	// TODO: Remove this when security is fully implemented.
    	return new UserLogin(5, "atulai", "");
    	//return this.user;
    }
}