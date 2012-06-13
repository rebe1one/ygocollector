package com.rms.collector.view;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class CardValidator extends AbstractValidator {

	public void validate(ValidationContext ctx) {
		String name = (String)ctx.getProperties("name")[0].getValue();
		Integer cardNumber = (Integer)ctx.getProperties("cardNumber")[0].getValue();
		Integer level = (Integer)ctx.getProperties("level")[0].getValue();
		String description = (String)ctx.getProperties("description")[0].getValue();
		String attribute = (String)ctx.getProperties("attribute")[0].getValue();
		String setId = (String)ctx.getProperties("setId")[0].getValue();
		
		if(name == null || "".equals(name))
			this.addInvalidMessage(ctx, "name", "You must enter a name");
		
		if(cardNumber == null || cardNumber == 0)
			this.addInvalidMessage(ctx, "cardNumber", "You must enter a card number");
		
		if(attribute == null || "".equals(attribute))
			this.addInvalidMessage(ctx, "name", "You must enter an attribute");
	}
}
