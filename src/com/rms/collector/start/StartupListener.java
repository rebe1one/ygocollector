package com.rms.collector.start;

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;

import com.rms.collector.model.view.Views;

public class StartupListener implements javax.servlet.ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// nothing atm
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			Views.generate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
