package com.rms.collector.model.view;

import com.rms.collector.model.Price;

public class PriceSourceView extends Price {
	private String sourceName;

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
}
