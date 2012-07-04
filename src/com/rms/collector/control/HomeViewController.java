package com.rms.collector.control;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Box;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.rms.collector.data.CardDAO;
import com.rms.collector.data.CollectionCardDAO;
import com.rms.collector.data.CollectionDAO;
import com.rms.collector.data.DeckCardDAO;
import com.rms.collector.data.DeckDAO;
import com.rms.collector.data.LocationDAO;
import com.rms.collector.data.UserDAO;
import com.rms.collector.ebay.AddItem;
import com.rms.collector.model.Card;
import com.rms.collector.model.Collection;
import com.rms.collector.model.CollectionCard;
import com.rms.collector.model.Deck;
import com.rms.collector.model.DeckCard;
import com.rms.collector.model.Location;
import com.rms.collector.model.User;
import com.rms.collector.util.Filter;
import com.rms.collector.util.FilterList;
import com.rms.collector.util.Util;

public class HomeViewController extends GenericForwardComposer<Borderlayout> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Listbox collectionList, deckList, locationList;
	private Box workbench;
	private Toolbarbutton deleteDecks, deleteCollections, deleteLocations;
	private Label welcomeLabel;
	private Menu adminMenu;

	public void onClick$createCollection() {
		// popup create collection form
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("collectionList", collectionList);
		Component comp = Executions.createComponents("createCollection.zul", null, args);
		if (comp instanceof Window) {
			((Window) comp).doModal();
		}
	}

	public void onClick$createDeck() {
		// popup create collection form
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("deckList", deckList);
		Component comp = Executions.createComponents("createDeck.zul", null, args);
		if (comp instanceof Window) {
			((Window) comp).doModal();
		}
	}
	
	public void onClick$changePassword() {
		Component comp = Executions.createComponents("adminChangePass.zul", null, null);
		if (comp instanceof Window) {
			((Window) comp).doModal();
		}
	}

	public void onClick$logout() {
		UserCredentialManager.getInstance().logout();
	}

	public void onClick$createLocation() {
		// popup create collection form
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("locationList", locationList);
		Component comp = Executions.createComponents("createLocation.zul", null, args);
		if (comp instanceof Window) {
			((Window) comp).doModal();
		}
	}
	
	public void onClick$wizardStep1() {
		startWizard();
	}
	
	public void onClick$ebay() {
		String loginURL = AddItem.ebayLoginURL();
		System.out.println(loginURL);
		Executions.sendRedirect(loginURL);
	}
	
	private void startWizard() {
		Component comp = Executions.createComponents("startWizard.zul", null, null);
		if (comp instanceof Window) {
			((Window) comp).doModal();
		}
	}

	public void onClick$deleteCollections() {
		Messagebox.show("Are you sure you want to remove this collection?", "Remove Collection?", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
			public void onEvent(Event e) {
				if (Messagebox.ON_OK.equals(e.getName())) {
					CollectionCardDAO collectionCardDAO = new CollectionCardDAO();
					try {
						collectionCardDAO.startTransaction();
						LinkedList<Collection> toRemove = new LinkedList<Collection>();
						for (Listitem item : collectionList.getSelectedItems()) {
							Collection deck = (Collection) item.getValue();
							List<CollectionCard> cards = collectionCardDAO.find(FilterList.start("collection_id", deck.getId(), Filter.Equality.EQUALS).getList());
							for (CollectionCard card : cards) {
								collectionCardDAO.delete(card);
							}
							new CollectionDAO().delete(deck);
							toRemove.add(deck);
						}
						collectionCardDAO.commmitTransaction();
						((ListModelList) collectionList.getModel()).removeAll(toRemove);
						deleteCollections.setDisabled(true);
						clearWorkbench();
					} catch (SQLException ex) {
						collectionCardDAO.rollbackTransaction();
						ex.printStackTrace();
					} catch (IndexOutOfBoundsException ex) {
						ex.printStackTrace();
					}
				} else if (Messagebox.ON_CANCEL.equals(e.getName())) {
					// Cancel is clicked
				}
			}
		});
	}

	public void onClick$getCardImages() {
		// popup create collection form
		CardDAO dao = new CardDAO();
		List<Card> cards = dao.findAll();
		ImageLookupQueue.getInstance().addCards(cards);
	}

	public void onClick$cleanupSomeShit() {
		// popup create collection form
		HashMap<String, Object> args = new HashMap<String, Object>();
		int id = getSelectedCollectionId();
		args.put("collectionId", id);
		LinkedList<Filter> filters = new LinkedList<Filter>();
		filters.add(new Filter("set_id", "", Filter.Equality.NOTEQUALS));
		CollectionCardDAO dao = new CollectionCardDAO();
		List<CollectionCard> cards = dao.find(filters);
		for (CollectionCard card : cards) {
			filters = new LinkedList<Filter>();
			filters.add(new Filter("card_id", card.getCardId()));
			filters.add(Filter.AND);
			filters.add(new Filter("collection_id", card.getCollectionId()));
			filters.add(Filter.AND);
			filters.add(new Filter("amount", card.getAmount()));
			filters.add(Filter.AND);
			filters.add(new Filter("rarity", card.getRarity()));
			filters.add(Filter.AND);
			filters.add(new Filter("location_id", card.getLocationId()));
			filters.add(Filter.AND);
			filters.add(new Filter("price_source_id", card.getPriceSourceId()));
			filters.add(Filter.AND);
			filters.add(new Filter("set_id", ""));
			List<CollectionCard> cards2 = dao.find(filters);
			for (CollectionCard c : cards2) {
				System.out.println(c.toString());
				try {
					dao.delete(c);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			System.out.println("DONE");
		}
	}

	private int getSelectedCollectionId() {
		return ((Collection) collectionList.getSelectedItem().getValue()).getId();
	}
	
	private void clearDecks() {
		deckList.clearSelection();
		deleteDecks.setDisabled(true);
	}
	
	private void clearCollections() {
		collectionList.clearSelection();
		deleteCollections.setDisabled(true);
	}
	
	private void clearLocations() {
		locationList.clearSelection();
		deleteLocations.setDisabled(true);
	}

	public void onSelect$collectionList(SelectEvent<Listitem, Collection> event) {
		clearDecks();
		clearLocations();
		if (collectionList.getSelectedItems().size() > 0) {
			deleteCollections.setDisabled(false);
			int id = ((Collection) event.getReference().getValue()).getId();
			HashMap<String, Object> args = new HashMap<String, Object>();
			args.put("id", id);
			if (Util.isNotEmpty(workbench.getFellowIfAny("Manager")))
				workbench.getFellow("Manager").detach();
			Executions.createComponents("collectionManager.zul", workbench, args);
		} else {
			deleteCollections.setDisabled(true);
			if (Util.isNotEmpty(workbench.getFellowIfAny("Manager")))
				workbench.getFellow("Manager").detach();
		}
	}

	public void onSelect$deckList(SelectEvent<Listitem, Deck> event) {
		clearCollections();
		clearLocations();
		if (deckList.getSelectedItems().size() > 0) {
			deleteDecks.setDisabled(false);
			int id = ((Deck) event.getReference().getValue()).getId();
			HashMap<String, Object> args = new HashMap<String, Object>();
			args.put("deckId", id);
			if (Util.isNotEmpty(workbench.getFellowIfAny("Manager")))
				workbench.getFellow("Manager").detach();
			Executions.createComponents("deckManager.zul", workbench, args);
		} else {
			deleteDecks.setDisabled(true);
			if (Util.isNotEmpty(workbench.getFellowIfAny("Manager")))
				workbench.getFellow("Manager").detach();
		}
	}

	public void clearWorkbench() {
		if (Util.isNotEmpty(workbench.getFellowIfAny("Manager")))
			workbench.getFellow("Manager").detach();
	}

	public void onClick$deleteDecks(Event event) {
		Messagebox.show("Are you sure you want to remove this deck?", "Remove Deck?", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
			public void onEvent(Event e) {
				if (Messagebox.ON_OK.equals(e.getName())) {
					DeckCardDAO deckCardDAO = new DeckCardDAO();
					try {
						deckCardDAO.startTransaction();
						LinkedList<Deck> toRemove = new LinkedList<Deck>();
						for (Listitem item : deckList.getSelectedItems()) {
							Deck deck = (Deck) item.getValue();
							List<DeckCard> cards = deckCardDAO.find(FilterList.start("deck_id", deck.getId(), Filter.Equality.EQUALS).getList());
							for (DeckCard card : cards) {
								deckCardDAO.delete(card);
							}
							new DeckDAO().delete(deck);
							toRemove.add(deck);
						}
						deckCardDAO.commmitTransaction();
						((ListModelList) deckList.getModel()).removeAll(toRemove);
						deleteDecks.setDisabled(true);
						clearWorkbench();
					} catch (SQLException ex) {
						deckCardDAO.rollbackTransaction();
						ex.printStackTrace();
					}
				} else if (Messagebox.ON_CANCEL.equals(e.getName())) {
					// Cancel is clicked
				}
			}
		});
	}

	public void doAfterCompose(Borderlayout borderlayout) throws Exception {
		super.doAfterCompose(borderlayout);

		User user = new UserDAO().findSingle(Filter.simpleFilter("id", UserCredentialManager.getInstance().getUserLogin().getUserId()));
		welcomeLabel.setValue("Sup " + user.getFirstName() + "!");
		
		if (UserCredentialManager.getInstance().getUserLogin().getUserLogin().equals("atulai"))
			adminMenu.setVisible(true);
		
		EventQueues.lookup("homeRefreshEventQueue", EventQueues.DESKTOP, true)
		.subscribe(new EventListener<Event>(){
			public void onEvent(Event event) throws Exception {
				CollectionDAO dao = new CollectionDAO();
				List<Collection> collections = dao.findByUserId(UserCredentialManager.getInstance().getUserLogin().getUserId());
				ListModelList<Collection> collectionModel = new ListModelList<Collection>(collections);
				collectionModel.setMultiple(true);
				collectionList.setModel(collectionModel);

				DeckDAO deckDAO = new DeckDAO();
				List<Deck> decks = deckDAO.findByUserId(UserCredentialManager.getInstance().getUserLogin().getUserId());
				ListModelList<Deck> deckModel = new ListModelList<Deck>(decks);
				deckModel.setMultiple(true);
				deckList.setModel(deckModel);

				LocationDAO locationDAO = new LocationDAO();
				List<Location> locations = locationDAO.findByUserId(UserCredentialManager.getInstance().getUserLogin().getUserId());
				ListModelList<Location> locationModel = new ListModelList<Location>(locations);
				locationModel.setMultiple(true);
				locationList.setModel(locationModel);
				
				if (collections.isEmpty() && locations.isEmpty()) {
					startWizard();
				}
			}
		});
		
		EventQueues.lookup("homeRefreshEventQueue", EventQueues.DESKTOP, true)
			.publish(new Event("onChangeBackend", null, null));
	}
}
