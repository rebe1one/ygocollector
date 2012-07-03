package com.rms.collector.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rms.collector.model.Card;
import com.rms.collector.util.Filter;
import com.rms.collector.util.Util;

public class ViewDAO extends DAO {
	
	public boolean run(String sql) throws SQLException {
		return execute(sql);
	}
}