package com.rms.collector.control;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Box;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.rms.collector.data.CollectionCardViewDAO;
import com.rms.collector.data.CollectionDAO;
import com.rms.collector.data.PriceDAO;
import com.rms.collector.data.SourceDAO;
import com.rms.collector.model.Collection;
import com.rms.collector.model.Price;
import com.rms.collector.model.Source;
import com.rms.collector.model.view.CollectionCardView;
import com.rms.collector.model.view.PriceSourceView;
import com.rms.collector.util.Util;

public class PriceFormController extends GenericForwardComposer<Window> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Textbox price;
	private Window newPriceWin;
    private Combobox set;
    private Listbox source;
    
    public void onClick$confirm(Event event) {
    	CollectionCardView ccv = (CollectionCardView)this.arg.get("collectionCardView");
        PriceDAO priceDAO = new PriceDAO();
        Price p = new Price();
        p.setCardId(ccv.getCardId());
        p.setEdition("UNL");
        p.setPrice(BigDecimal.valueOf((Double.valueOf(price.getValue()))));
        p.setRarity(ccv.getRarity());
        String setId = null;
        if (Util.isNotEmpty(set.getSelectedItem())) {
        	setId = ((PriceSourceView)set.getSelectedItem().getValue()).getSetId();
        } else {
        	setId = set.getValue();
        }
        p.setSetId(setId);
        p.setSourceId(((Source)source.getSelectedItem().getValue()).getId());
        p.setDate(Util.getCurrentTimestamp());
        try {
			priceDAO.insert(p);
			Listbox priceList = (Listbox)this.arg.get("priceList");
			List<PriceSourceView> prices = new PriceDAO().findLatestCardPrices(ccv.getCardId(), ccv.getRarity());
			ListModelList<PriceSourceView> priceModel = new ListModelList<PriceSourceView>(prices);
			List<PriceSourceView> priceSelection = new LinkedList<PriceSourceView>();
			for (PriceSourceView psv : priceModel) {
				if (psv.getSetId().equals(ccv.getSetId())) {
					priceSelection.add(psv);
				}
			}
			priceModel.setSelection(priceSelection);
			priceList.setModel(priceModel);
			newPriceWin.detach();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    @Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		CollectionCardView ccv = (CollectionCardView)this.arg.get("collectionCardView");
		List<PriceSourceView> prices = new PriceDAO().findLatestCardPrices(ccv.getCardId(), ccv.getRarity());
		ListModelList<PriceSourceView> priceModel = new ListModelList<PriceSourceView>(prices);
		List<PriceSourceView> priceSelection = new LinkedList<PriceSourceView>();
		for (PriceSourceView p : priceModel) {
			if (p.getSetId().equals(ccv.getSetId())) {
				priceSelection.add(p);
			}
		}
		priceModel.setSelection(priceSelection);
		set.setModel(priceModel);
		
		List<Source> sources = new SourceDAO().findAll();
		ListModelList<Source> sourceModel = new ListModelList<Source>(sources);
		List<Source> sourcesSelection = new LinkedList<Source>();
		for (Source s : sourceModel) {
			if (s.getName().equals("Custom")) {
				sourcesSelection.add(s);
			}
		}
		sourceModel.setSelection(sourcesSelection);
		source.setModel(sourceModel);
    }
}
