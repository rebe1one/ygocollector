package com.rms.collector.control;

import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Initiator;

public class CheckForLogin implements Initiator {
	@Override
	public void doInit(Page page, Map<String, Object> args) throws Exception {
		if (!UserCredentialManager.getInstance().isAuthenticated()) {
			Executions.sendRedirect("login.zul");
		}
	}
}
