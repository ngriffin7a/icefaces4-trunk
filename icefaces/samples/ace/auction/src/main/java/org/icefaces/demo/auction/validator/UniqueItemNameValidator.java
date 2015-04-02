package org.icefaces.demo.auction.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.icefaces.demo.auction.service.AuctionService;
import org.icefaces.demo.auction.util.FacesUtils;

@FacesValidator(value="UniqueItemNameValidator")
public class UniqueItemNameValidator implements Validator {
	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
		if (o != null) {
			String value = o.toString();
			
			AuctionService service = (AuctionService)FacesUtils.getManagedBean(AuctionService.BEAN_NAME);
			
			if (!service.isUniqueItemName(value)) {
	            FacesMessage message = new FacesMessage("Auction item '" + value + "' already exists, please choose another name.");
	            message.setSeverity(FacesMessage.SEVERITY_ERROR);
	            throw new ValidatorException(message);
			}
		}
	}
}
