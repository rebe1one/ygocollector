package com.rms.collector.control;

import java.math.BigDecimal;
import java.util.List;

import com.rms.collector.data.CollectionCardViewDAO;
import com.rms.collector.data.CollectionDAO;
import com.rms.collector.model.Collection;
import com.rms.collector.model.view.CollectionCardView;
import com.rms.collector.util.Filter;
import com.rms.collector.util.Util;

public class CollectionManager {
	private Collection c;
	
	public CollectionManager(int id) {
		CollectionDAO dao = new CollectionDAO();
		c = dao.findSingle(Filter.simpleFilter("id", id));
	}
	
	public String getCollectionName() {
		return c.getName();
	}
	
	public BigDecimal getTotalPrice() {
		CollectionCardViewDAO ccv = new CollectionCardViewDAO();
		List<CollectionCardView> cards = ccv.findByCollectionId(c.getId());
		BigDecimal total = BigDecimal.ZERO;
		for (CollectionCardView card : cards) {
			if (Util.isNotEmpty(card.getPrice()))
				total = total.add(card.getPrice().multiply(BigDecimal.valueOf(Double.valueOf(card.getAmount()))));
		}
		return total;
	}
	
	public BigDecimal getHighestPrice() {
		CollectionCardViewDAO ccv = new CollectionCardViewDAO();
		List<CollectionCardView> cards = ccv.findByCollectionId(c.getId());
		BigDecimal largest = BigDecimal.ZERO;
		for (CollectionCardView card : cards) {
			if (Util.isNotEmpty(card.getPrice()) && largest.compareTo(card.getPrice()) < 0) {
				largest = card.getPrice();
			}
		}
		return largest;
	}
}
