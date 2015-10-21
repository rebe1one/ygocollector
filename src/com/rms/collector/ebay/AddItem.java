/*
Copyright (c) 2006 eBay, Inc.

This program is licensed under the terms of the eBay Common Development and 
Distribution License (CDDL) Version 1.0 (the "License") and any subsequent 
version thereof released by eBay.  The then-current version of the License 
can be found at https://www.codebase.ebay.com/Licenses.html and in the 
eBaySDKLicense file that is under the eBay SDK install directory.
 */

package com.rms.collector.ebay;

import java.io.IOException;

import com.ebay.sdk.ApiAccount;
import com.ebay.sdk.ApiCall;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.SdkSoapException;
import com.ebay.sdk.call.GetSessionIDCall;
import com.ebay.sdk.helper.ConsoleUtil;
import com.ebay.sdk.util.eBayUtil;
import com.ebay.soap.eBLBaseComponents.AddFixedPriceItemRequestType;
import com.ebay.soap.eBLBaseComponents.AddFixedPriceItemResponseType;
import com.ebay.soap.eBLBaseComponents.AddItemRequestType;
import com.ebay.soap.eBLBaseComponents.AddItemResponseType;
import com.ebay.soap.eBLBaseComponents.AmountType;
import com.ebay.soap.eBLBaseComponents.BuyerPaymentMethodCodeType;
import com.ebay.soap.eBLBaseComponents.CalculatedShippingRateType;
import com.ebay.soap.eBLBaseComponents.CategoryType;
import com.ebay.soap.eBLBaseComponents.CountryCodeType;
import com.ebay.soap.eBLBaseComponents.CurrencyCodeType;
import com.ebay.soap.eBLBaseComponents.DetailNameCodeType;
import com.ebay.soap.eBLBaseComponents.FeesType;
import com.ebay.soap.eBLBaseComponents.GetMyeBaySellingRequestType;
import com.ebay.soap.eBLBaseComponents.GetMyeBaySellingResponseType;
import com.ebay.soap.eBLBaseComponents.GetSessionIDRequestType;
import com.ebay.soap.eBLBaseComponents.GetSessionIDResponseType;
import com.ebay.soap.eBLBaseComponents.GeteBayDetailsRequestType;
import com.ebay.soap.eBLBaseComponents.GeteBayDetailsResponseType;
import com.ebay.soap.eBLBaseComponents.ItemListCustomizationType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.ListingDurationCodeType;
import com.ebay.soap.eBLBaseComponents.ListingTypeCodeType;
import com.ebay.soap.eBLBaseComponents.PaymentMethodDefinitionType;
import com.ebay.soap.eBLBaseComponents.PaymentTypeCodeType;
import com.ebay.soap.eBLBaseComponents.PictureDetailsType;
import com.ebay.soap.eBLBaseComponents.RefundOptionsCodeType;
import com.ebay.soap.eBLBaseComponents.ReturnPolicyType;
import com.ebay.soap.eBLBaseComponents.ReturnsAcceptedDetailsType;
import com.ebay.soap.eBLBaseComponents.ReturnsAcceptedOptionsCodeType;
import com.ebay.soap.eBLBaseComponents.SellerPaymentProfileType;
import com.ebay.soap.eBLBaseComponents.SellerProfilesType;
import com.ebay.soap.eBLBaseComponents.SellerReturnProfileType;
import com.ebay.soap.eBLBaseComponents.SellerShippingProfileType;
import com.ebay.soap.eBLBaseComponents.ShippingCostPaidByOptionsCodeType;
import com.ebay.soap.eBLBaseComponents.ShippingDetailsType;
import com.ebay.soap.eBLBaseComponents.ShippingRateTypeCodeType;
import com.ebay.soap.eBLBaseComponents.ShippingServiceOptionsType;
import com.ebay.soap.eBLBaseComponents.ShippingTypeCodeType;
import com.rms.collector.control.UserCredentialManager;
import com.rms.collector.data.UserEBayInfoDAO;
import com.rms.collector.model.UserEBayInfo;
import com.rms.collector.model.view.CollectionCardView;

/**
 * 
 * A simple item listing sample, show basic flow to list an item to eBay Site
 * using eBay SDK.
 * 
 * this sample does not use AddItemCall, it use raw ApiCall and
 * AddItemRequestType/AddItemResponseType directly.
 * 
 * @author boyang
 * 
 */
public class AddItem {

	private static String RUName = "";

	public static void main(String[] args) {
		// addItem();
	}
	
	public static ApiContext getApplicationContext() {
		ApiContext apiContext = new ApiContext();

		ApiCredential cred = apiContext.getApiCredential();
		ApiAccount account = cred.getApiAccount();
		account.setApplication("");
		account.setCertificate("");
		account.setDeveloper("");

		// set Api Server Url
		apiContext.setApiServerUrl("https://api.sandbox.ebay.com/wsapi");

		return apiContext;
	}

	public static String ebayLoginURL() {
		try {
			GetSessionIDCall apiCall = new GetSessionIDCall(getApplicationContext());
	
			GetSessionIDRequestType session = new GetSessionIDRequestType();
			session.setRuName(RUName);
			GetSessionIDResponseType idResponse;
		
			idResponse = (GetSessionIDResponseType) apiCall.execute(session);
			UserCredentialManager.getInstance().getUserLogin().setTemp("ebaySessionID", idResponse.getSessionID());
			return "https://signin.sandbox.ebay.com/ws/eBayISAPI.dll?SignIn&RuName=" + RUName + "&SessID=" + idResponse.getSessionID();
		} catch (ApiException e) {
			e.printStackTrace();
		} catch (SdkSoapException e) {
			e.printStackTrace();
		} catch (SdkException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void addItem(CollectionCardView ccv) {

		try {

			System.out.print("\n");
			System.out.print("+++++++++++++++++++++++++++++++++++++++\n");
			System.out.print("+ Welcome to eBay SDK for Java Sample +\n");
			System.out.print("+  - ConsoleAddItem                   +\n");
			System.out.print("+++++++++++++++++++++++++++++++++++++++\n");
			System.out.print("\n");

			// [Step 1] Initialize eBay ApiContext object
			System.out.println("===== [1] Account Information ====");
			ApiContext apiContext = getApiContext();

			// [Step 2] Create a new item object.
			System.out.println("===== [2] Item Information ====");

			// [Step 3] Create call object and execute the call.
			System.out.println("===== [3] Execute the API call ====");
			System.out.println("Begin to call eBay API, please wait ...");
			// Create a raw ApiCall object.
			ApiCall api = new ApiCall(apiContext);
			ItemType item = buildItem(api, ccv);

			GetMyeBaySellingRequestType sellReq = new GetMyeBaySellingRequestType();
			sellReq.setActiveList(new ItemListCustomizationType());
			GetMyeBaySellingResponseType resp2 = (GetMyeBaySellingResponseType) api.execute(sellReq);

			// Create a raw API request object and config it.
			AddFixedPriceItemRequestType req = new AddFixedPriceItemRequestType();
			req.setItem(item);
			// Execute the API call.
			AddFixedPriceItemResponseType resp = (AddFixedPriceItemResponseType) api.execute(req);
			// Read response.
			FeesType fees = resp.getFees();
			System.out.println("End to call eBay API, show call result ...");
			System.out.println();

			// [Setp 4] Display results.
			System.out.println("The list was listed successfully!");

			double listingFee = eBayUtil.findFeeByName(fees.getFee(), "ListingFee").getFee().getValue();
			System.out.println("Listing fee is: " + new Double(listingFee).toString());
			System.out.println("Listed Item ID: " + resp.getItemID());
			System.out.println(resp.toString());
		} catch (Exception e) {
			System.out.println("Fail to list the item.");
			e.printStackTrace();
		}
	}

	/**
	 * Build a sample item
	 * 
	 * @return ItemType object
	 * @throws SdkException
	 * @throws SdkSoapException
	 * @throws ApiException
	 */
	private static ItemType buildItem(ApiCall api, CollectionCardView card) throws IOException, ApiException, SdkSoapException, SdkException {

		ItemType item = new ItemType();

		// item title
		item.setTitle(card.getName() + " " + card.getSetId() + " " + card.getRarity());
		// item description
		item.setDescription("This auction is for a near mint " + card.getName() + " " + card.getSetId() + " " + card.getRarity());

		// listing type
		item.setListingType(ListingTypeCodeType.FIXED_PRICE_ITEM);
		// listing price
		item.setCurrency(CurrencyCodeType.USD);
		AmountType amount = new AmountType();
		amount.setValue(card.getPrice().doubleValue());
		amount.setCurrencyID(CurrencyCodeType.USD);
		// item.setBuyItNowPrice(amount);
		item.setStartPrice(amount);
		PictureDetailsType pic = new PictureDetailsType();
		pic.setExternalPictureURL(new String[] { "http://collector.zapto.org:8080/collector/images/cards/" + card.getImageFileName() });
		item.setPictureDetails(pic);

		// listing duration
		item.setListingDuration(ListingDurationCodeType.DAYS_3.value());

		// item location and country
		item.setLocation("TODO: Location");
		item.setCountry(CountryCodeType.US);

		item.setPaymentMethods(new BuyerPaymentMethodCodeType[] { BuyerPaymentMethodCodeType.PAY_PAL });
		item.setPayPalEmailAddress("andrei.tulai@gmail.com");

		GeteBayDetailsRequestType detReq = new GeteBayDetailsRequestType();
		detReq.setDetailName(new DetailNameCodeType[] { DetailNameCodeType.SHIPPING_SERVICE_DETAILS, DetailNameCodeType.RETURN_POLICY_DETAILS });
		GeteBayDetailsResponseType response = (GeteBayDetailsResponseType) api.execute(detReq);
		ShippingDetailsType ship = new ShippingDetailsType();
		ShippingServiceOptionsType shipType = new ShippingServiceOptionsType();
		shipType.setShippingService(response.getShippingServiceDetails()[1].getShippingService());

		ReturnPolicyType ret = new ReturnPolicyType();
		ret.setReturnsAcceptedOption(ReturnsAcceptedOptionsCodeType.RETURNS_ACCEPTED.value());
		ret.setRefundOption(RefundOptionsCodeType.MONEY_BACK.value());
		ret.setShippingCostPaidByOption(ShippingCostPaidByOptionsCodeType.BUYER.value());
		item.setReturnPolicy(ret);

		AmountType shipCost = new AmountType();
		shipCost.setValue(3.00);
		shipType.setShippingServiceCost(shipCost);
		ship.setShippingServiceOptions(new ShippingServiceOptionsType[] { shipType });
		ship.setDefaultShippingCost(shipCost);
		ship.setShippingType(ShippingTypeCodeType.FLAT);
		item.setShippingDetails(ship);
		item.setDispatchTimeMax(1);

		// listing category
		CategoryType cat = new CategoryType();
		cat.setCategoryID("31395");
		item.setPrimaryCategory(cat);

		// item quality
		item.setQuantity(card.getAmount());

		// item condition, New
		// item.setConditionID(1000);

		/*
		 * The Business Policies API and related Trading API fields are
		 * available in sandbox. It will be available in production for a
		 * limited number of sellers with Version 775. 100 percent of sellers
		 * will be ramped up to use Business Polcies in July 2012
		 */

		return item;
	}

	/**
	 * Populate eBay SDK ApiContext object with data input from user
	 * 
	 * @return ApiContext object
	 */
	private static ApiContext getApiContext() throws IOException {

		String input;
		ApiContext apiContext = new ApiContext();

		// set Api Token to access eBay Api Server
		ApiCredential cred = apiContext.getApiCredential();
		UserEBayInfo info = new UserEBayInfoDAO().findById(UserCredentialManager.getInstance().getUserLogin().getUserId());
		cred.seteBayToken(info.getEBayToken());

		// set Api Server Url
		apiContext.setApiServerUrl("https://api.sandbox.ebay.com/wsapi");

		return apiContext;
	}

}