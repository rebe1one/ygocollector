package com.rms.collector.util;

import java.sql.Timestamp;
import java.util.Date;

public class DateFilter extends Filter {
	
	private Date toDate = null;

	public DateFilter(String key, Object value) {
		super(key, value);
	}
	
	public DateFilter(String key, Date fromDate, Date toDate) {
		super(key, fromDate);
		this.toDate = toDate;
	}
	
	@Override
	public String toString() {
		Timestamp day = new Timestamp(((Date)value).getTime());
		Timestamp nextDay;
		if (Util.isEmpty(toDate)) {
			nextDay = new Timestamp(((Date)value).getTime() + 86400000);
		} else {
			nextDay = new Timestamp(((Date)toDate).getTime());
		}
		return key + " BETWEEN '" + day + "' AND '" + nextDay + "'";
	}

}
