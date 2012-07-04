package com.rms.collector.control;

import java.sql.Timestamp;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Window;

import com.ebay.sdk.call.FetchTokenCall;
import com.ebay.soap.eBLBaseComponents.FetchTokenRequestType;
import com.ebay.soap.eBLBaseComponents.FetchTokenResponseType;
import com.rms.collector.data.UserEBayInfoDAO;
import com.rms.collector.ebay.AddItem;
import com.rms.collector.model.UserEBayInfo;

public class EbayViewController extends GenericForwardComposer<Window> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private Grid stepGrid;
    
    public void doAfterCompose(Window window) throws Exception {
        super.doAfterCompose(window);
        Row row = new Row();
        row.setValue("Waiting for response...");
        stepGrid.getRows().appendChild(row);
        
        FetchTokenCall tokenCall = new FetchTokenCall(AddItem.getApplicationContext());
        FetchTokenRequestType fetchRequest = new FetchTokenRequestType();
        fetchRequest.setSessionID((String)UserCredentialManager.getInstance().getUserLogin().getTemp("ebaySessionID"));
        FetchTokenResponseType response = (FetchTokenResponseType)tokenCall.execute(fetchRequest);
        
        row = new Row();
        row.setValue("Received authorization!");
        stepGrid.getRows().appendChild(row);
        UserEBayInfoDAO dao = new UserEBayInfoDAO();
        UserEBayInfo info = new UserEBayInfo();
        info.setUserId(UserCredentialManager.getInstance().getUserLogin().getUserId());
        info.setEBayToken(response.getEBayAuthToken().getBytes());
        info.setEBayTokenExpirationDate(new Timestamp(response.getHardExpirationTime().getTimeInMillis()));
        dao.insert(info);
        System.out.println(response.getEBayAuthToken());
        System.out.println(response.getHardExpirationTime().getTime());
        
        row = new Row();
        row.setValue("Redirecting back to Collector...");
        stepGrid.getRows().appendChild(row);
        Executions.sendRedirect("home.zul");
    }
}
