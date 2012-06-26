package com.rms.collector.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.FactoryConfigurationError;

import com.rms.collector.data.CardDAO;
import com.rms.collector.data.CollectionCardDAO;
import com.rms.collector.data.PriceDAO;
import com.rms.collector.data.RarityDAO;
import com.rms.collector.model.Card;
import com.rms.collector.model.ExternalCardInfo;
import com.rms.collector.model.Price;
import com.rms.collector.model.Rarity;
import com.rms.collector.model.view.CollectionCardView;
import com.rms.collector.model.view.PriceSourceView;
import com.rms.collector.util.Filter;
import com.rms.collector.util.OrderFilter;
import com.rms.collector.util.Util;

public class PriceLookupQueue implements ThreadCompleteListener {
	private static PriceLookupQueue queue = new PriceLookupQueue();
	private List<CollectionCardView> cardsToProcess = new LinkedList<CollectionCardView>();
	private List<CollectionCardView> cardsInProcess;
	private boolean running = false;
	private PriceLookupQueue() {}
	public static PriceLookupQueue getInstance() { return queue; }
	public void addCard(CollectionCardView cc) {
		addCardToList(cc);
		run();
	}
	
	private void run() {
		if (running == false) {
			cardsInProcess = cardsToProcess;
			cardsToProcess = new LinkedList<CollectionCardView>();
			new Thread(new PriceLookupThread(cardsInProcess).addListener(this)).start();
			running = true;
		} else System.out.print("Already running.");
	}
	
	private void addCardToList(CollectionCardView cc) {
		if (cardsToProcess.size() == 0) {
			cardsToProcess.add(cc);
		} else {
			for (CollectionCardView view : cardsToProcess) {
				if (view.getCardId() == cc.getCardId()) return;
			}
			for (CollectionCardView view : cardsInProcess) {
				if (view.getCardId() == cc.getCardId()) return;
			}
			cardsToProcess.add(cc);
		}
	}
	
	public void addCards(List<CollectionCardView> cards) {
		if (cardsToProcess.size() == 0) {
			cardsToProcess = cards;
		} else {
			for (CollectionCardView cc : cards) {
				addCardToList(cc);
			}
		}
		run();
	}
	
	@Override
	public void notifyOfThreadComplete(NotifyingThread thread) {
		running = false;
		if (cardsToProcess.size() > 0) {
			System.out.println("Done processing but we have more!");
			run();
		}
	}
	
	private class PriceLookupThread extends NotifyingThread {
		private List<CollectionCardView> cards;
		
		public PriceLookupThread(List<CollectionCardView> cards) {
			super();
			this.cards = cards;
		}
		
		public void doRun() {
			if (cards != null) {
				for (CollectionCardView ccv : cards) {
					PriceDAO pDAO = new PriceDAO();
					List<Filter> filters = Filter.begin();
					filters.add(new Filter("card_id", ccv.getCardId()));
					filters.add(new OrderFilter("date", OrderFilter.DESC));
					List<Price> priceList = pDAO.find(filters);
					for (Price p : priceList) {
						if (p.getSourceId() != 1 && Util.getCurrentTimestamp().getTime() - p.getDate().getTime() > (long)60*60*24*1000) {
							System.out.println(new Timestamp(Util.getCurrentTimestamp().getTime()));
							System.out.println(new Timestamp(p.getDate().getTime()));
							System.out.println((long)60*60*24*1000);
							System.out.println("");
							//processCard(ccv);
							break;
						}
					}
				}
			}
		}
		
		private void processCard(CollectionCardView ccv) {
			List<ExternalCardInfo> eci = getOnlinePrices(ccv.getName());
			CardDAO cardDAO = new CardDAO();
			cardDAO.startTransaction();
			for (ExternalCardInfo i : eci) {
				try {
					LinkedList<Filter> cardFilter = new LinkedList<Filter>();
					cardFilter.add(new Filter("name", i.getName()));
					Card card = cardDAO.findSingle(cardFilter);
					RarityDAO rarityDAO = new RarityDAO();
					LinkedList<Filter> rarityFilter = new LinkedList<Filter>();
					rarityFilter.add(new Filter("description", i.getRarity().trim()));
					Rarity r = rarityDAO.findSingle(rarityFilter);
					PriceDAO dao = new PriceDAO();
					Price p = new Price(card.getId(), 0, "UNL", i.getPrice(), Util.getCurrentTimestamp(), r.getRarity(), i.getSetId());
					dao.insert(p);
					if (ccv.getRarity().equals(p.getRarity())) {
						CollectionCardDAO ccDAO = new CollectionCardDAO();
						ccDAO.delete(ccv);
						ccv.setPriceSourceId(p.getSourceId());
						ccv.setSetId(p.getSetId());
						ccDAO.insert(ccv);
					}
				} catch (SQLException e) {
					cardDAO.rollbackTransaction();
					e.printStackTrace();
					return;
				}
			}
			cardDAO.commmitTransaction();
		}
		
		public List<ExternalCardInfo> getOnlinePrices(String cardName) {
			List<ExternalCardInfo> cardInfos = new LinkedList<ExternalCardInfo>();
			String searchWords = cardName;
			searchWords = searchWords.replace(" ", "+");
			URL yahoo;
			try {
				yahoo = new URL("http://trollandtoad.com/products/search.php?search_words=" + searchWords + "&Image1.x=0&Image1.y=0&search_category=&searchmode=basic");
			
	        URLConnection yc = yahoo.openConnection();
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                yc.getInputStream()));
	        String inputLine;
	        Pattern regularCardExpr = Pattern.compile("<li style=[\"']font-size:13.5px;margin-bottom:5px;[\"']><a href=[\"']/([a-z0-9\\.]+)[\"']><b>([a-zA-Z0-9 \\-,]+) - ([A-Z0-9]+\\-[A-Z]*[0-9]+) - ([a-zA-Z ]+)</b></a> \\(<a href=[\"'][a-zA-Z0-9\\!\\-&\\./]+[\"']>[a-zA-Z0-9 :\\-\\!'(),]+</a>\\)  \\(\\$([0-9\\.]+)\\)");
	        Pattern ultimateCardExpr = Pattern.compile("<li style=[\"']font-size:13.5px;margin-bottom:5px;[\"']><a href=[\"']/([a-z0-9\\.]+)[\"']><b>([a-zA-Z ]+) - ([a-zA-Z0-9 \\-,]+) - ([A-Z0-9]+\\-[A-Z]*[0-9]+)</b></a> \\(<a href=[\"'][a-zA-Z0-9\\!\\-&\\./]+[\"']>[a-zA-Z0-9 :\\-\\!'(),]+</a>\\)  \\(\\$([0-9\\.]+)\\)");
	        while ((inputLine = in.readLine()) != null) {
	        	Matcher regexMatcher = regularCardExpr.matcher(inputLine);
	        	while (regexMatcher.find()) {
	        		if (regexMatcher.group(2).toLowerCase().startsWith(cardName.toLowerCase())) {
	        			ExternalCardInfo eci = new ExternalCardInfo(regexMatcher.group(2), BigDecimal.valueOf(Double.valueOf(regexMatcher.group(5))), regexMatcher.group(3), regexMatcher.group(4));
	        			cardInfos.add(eci);
	        			System.out.println(eci);
	        		}
	        	}
	        	regexMatcher = ultimateCardExpr.matcher(inputLine);
	        	while (regexMatcher.find()) {
	        		if (regexMatcher.group(3).toLowerCase().startsWith(cardName.toLowerCase())) {
	        			ExternalCardInfo eci = new ExternalCardInfo(regexMatcher.group(3), BigDecimal.valueOf(Double.valueOf(regexMatcher.group(5))), regexMatcher.group(4), regexMatcher.group(2));
	        			cardInfos.add(eci);
	        			System.out.println(eci);
	        		}
	        	}
	        }
	        System.out.println("Finished!");
	        in.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (FactoryConfigurationError e) {
				e.printStackTrace();
			}
			return cardInfos;
		}
	}
}
