package com.rms.collector.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.FactoryConfigurationError;

import com.rms.collector.data.CardImageDAO;
import com.rms.collector.model.Card;
import com.rms.collector.model.CardImage;
import com.rms.collector.util.Filter;
import com.rms.collector.util.FilterList;
import com.rms.collector.util.Util;

public class ImageLookupQueue implements ThreadCompleteListener {
	private static ImageLookupQueue queue = new ImageLookupQueue();
	private List<Card> cardsToProcess = new LinkedList<Card>();
	private List<Card> cardsInProcess;
	private boolean running = false;
	private ImageLookupQueue() {}
	public static ImageLookupQueue getInstance() { return queue; }
	public void addCard(Card cc) {
		addCardToList(cc);
		run();
	}
	
	private void run() {
		if (running == false) {
			cardsInProcess = cardsToProcess;
			cardsToProcess = new LinkedList<Card>();
			new Thread(new ImageLookupThread(cardsInProcess).addListener(this)).start();
			running = true;
		} else System.out.print("Already running.");
	}
	
	private void addCardToList(Card cc) {
		if (cardsToProcess.size() == 0) {
			cardsToProcess.add(cc);
		} else {
			for (Card view : cardsToProcess) {
				if (view.getId() == cc.getId()) return;
			}
			for (Card view : cardsInProcess) {
				if (view.getId() == cc.getId()) return;
			}
			cardsToProcess.add(cc);
		}
	}
	
	public void addCards(List<Card> cards) {
		if (cardsToProcess.size() == 0) {
			cardsToProcess = cards;
		} else {
			for (Card cc : cards) {
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
	
	private class ImageLookupThread extends NotifyingThread {
		private List<Card> cards;
		
		public ImageLookupThread(List<Card> cards) {
			super();
			this.cards = cards;
		}
		
		public void doRun() {
			if (cards != null) {
				for (int i = 0; i < cards.size(); i++) {
					System.out.println("ON IMAGE: " + i + " / " + cards.size());
					processCard(cards.get(i));
				}
			}
		}
		
		private void processCard(Card c) {
			CardImageDAO imageDAO = new CardImageDAO();
			CardImage image = imageDAO.findSingle(FilterList.start("card_id", c.getId(), Filter.Equality.EQUALS).getList());
			try {
				if (!Util.isEmpty(image)) {
					File file=new File("/home/andrei/subzero/ygo/collector/WebContent/images/cards/" + image.getImageFileName());
					if (!file.exists()) {
						String fileName = getImage(c);
						imageDAO.update(new CardImage(c.getId(), fileName));
					}
				}
			} catch (Exception e) {
				try {
					imageDAO.update(new CardImage(c.getId(), ""));
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		
		public String getImage(Card card) throws Exception {
			String searchWords = card.getName();
			searchWords = searchWords.replace(" ", "_");
			String fileName = "/home/andrei/subzero/ygo/collector/WebContent/images/cards/" + card.getId();
			URL rdfFeed;
			BufferedReader in = null;
			BufferedReader in2 = null;
			String type = null;
			try {
				rdfFeed = new URL("http://yugioh.wikia.com/wiki/Special:ExportRDF/" + searchWords);
			
		        URLConnection yc = rdfFeed.openConnection();
		        in = new BufferedReader(
		                                new InputStreamReader(
		                                yc.getInputStream()));
		        String inputLine;
		        Pattern regularCardExpr = Pattern.compile("<property:Card_Image rdf:resource=\"([a-zA-Z0-9\\-:./\"\\!'%()_&;,]+)\"/>");
		        while ((inputLine = in.readLine()) != null) {
		        	Matcher regexMatcher = regularCardExpr.matcher(inputLine);
		        	while (regexMatcher.find()) {
		        		if (Util.isNotEmpty(regexMatcher.group(1))) {
		        			String url = regexMatcher.group(1);
		        			url = url.replace("&wiki;", "http://yugioh.wikia.com/wiki/Special:URIResolver/");
		        			URLConnection picPage = new URL(url).openConnection();
		        	        in2 = new BufferedReader(
		        	                                new InputStreamReader(
		        	                                		picPage.getInputStream()));
		        	        String inputLine2;
		        	        Pattern regularCardExpr2 = Pattern.compile("<div class=\"fullImageLink\" id=\"file\"><a href=\"([a-zA-Z0-9\\-:./\"\\!'%()_&;,]+)\"><img alt=\"");
		        	        while ((inputLine2 = in2.readLine()) != null) {
		        	        	Matcher regexMatcher2 = regularCardExpr2.matcher(inputLine2);
		        	        	while (regexMatcher2.find()) {
		        	        		if (Util.isNotEmpty(regexMatcher2.group(1))) {
		        	        			if (regexMatcher2.group(1).endsWith(".jpg") || regexMatcher2.group(1).endsWith(".jpeg")) {
		        	        				type = ".jpg";
		        	        				saveImage(regexMatcher2.group(1), fileName + ".jpg");
		        	        			} else if (regexMatcher2.group(1).endsWith(".png")) {
		        	        				saveImage(regexMatcher2.group(1), fileName + ".png");
		        	        				type = ".png";
		        	        			} else if (regexMatcher2.group(1).endsWith(".gif")) {
		        	        				saveImage(regexMatcher2.group(1), fileName + ".gif");
		        	        				type = ".gif";
		        	        			}
		        	        		}
		        	        	}
		        	        }
		        		}
		        	}
		        }
			} finally {
				try {
					if (in != null) in.close();
					if (in2 != null) in2.close();
					else System.out.println("DEAD CARD: " + card.getName());
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
			return card.getId() + type;
		}
		
		public void saveImage(String imageUrl, String destinationFile) throws IOException {
			URL url = new URL(imageUrl);
			InputStream is = url.openStream();
			OutputStream os = new FileOutputStream(destinationFile);

			byte[] b = new byte[2048];
			int length;

			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}
			
			is.close();
			os.close();
		}

	}
}
