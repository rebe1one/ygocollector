package com.rms.collector.data;

import java.sql.SQLException;
import java.util.List;

public interface iDAO<T> {
	public List<T> findAll();
	public boolean delete(T entity) throws SQLException;
	public Object insert(T entity) throws SQLException;
	public boolean update(T entity) throws SQLException;
}
