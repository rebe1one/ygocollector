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
	private BigDecimal totalPrice;
	private BigDecimal largestPrice;
	private Integer totalCards;
	
	public CollectionManager(int id) {
		CollectionDAO dao = new CollectionDAO();
		c = dao.findSingle(Filter.simpleFilter("id", id));
		CollectionCardViewDAO ccv = new CollectionCardViewDAO();
		List<CollectionCardView> cards = ccv.findByCollectionId(c.getId());
		totalPrice = BigDecimal.ZERO;
		largestPrice = BigDecimal.ZERO;
		totalCards = 0;
		for (CollectionCardView card : cards) {
			if (Util.isNotEmpty(card.getPrice()))
				totalPrice = totalPrice.add(card.getPrice().multiply(BigDecimal.valueOf(Double.valueOf(card.getAmount()))));
			if (Util.isNotEmpty(card.getPrice()) && largestPrice.compareTo(card.getPrice()) < 0)
				largestPrice = card.getPrice();
			totalCards += card.getAmount();
		}
	}
	
	public String getCollectionName() {
		return c.getName();
	}
	
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	
	public BigDecimal getHighestPrice() {
		return largestPrice;
	}
	
	public Integer getTotalNumberOfCards() {
		return totalCards;
	}
}
