/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.demo.auction.view.validators;

import org.icefaces.demo.auction.view.beans.AuctionItemBean;
import org.icefaces.demo.auction.view.names.BeanNames;
import org.icefaces.demo.auction.view.names.ParameterNames;
import org.icefaces.demo.auction.view.util.FacesUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.PropertyResourceBundle;

/**
 * Pretty simple validator that checks to see if the auction bid is within
 * an acceptable bid range.  For demo purposes we don't want to let people make
 * really big bids as well as bids that are smaller then the current bid.
 *
 * @author ICEsoft Technologies Inc.
 * @since 2.0
 */
@FacesValidator(value = "auctionItemBidValidator")
public class BidValidator implements Validator {

    // cap the size of a bid that can be made over the the original bid value.
    public static final int MAX_BID_INCREASE = 10000;
    // max bid that can be mad, period.
    public static final long MAX_BID = 1000000;

    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value)
            throws ValidatorException {

        // get instance of item so we can set a custom message.
        // Grab the data object for the row that was clicked
        ExternalContext externalContext =
                FacesContext.getCurrentInstance().getExternalContext();

        // ge the item that initiated the request.
        AuctionItemBean auctionMonitorItemBean = (AuctionItemBean)
                externalContext.getRequestMap().get(ParameterNames.AUCTION_ITEM);
        // get the original bid value
        double originalBid = auctionMonitorItemBean.getAuctionItem().getPrice();
        // get the new value
        double newBid = (Double) value;

        // make sure the item hasn't expired.
        if (auctionMonitorItemBean.isExpired()) {
            FacesMessage message = new FacesMessage();
            message.setDetail("Item has expired, bids are closed");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }

        // retrieve our message bundle from the context.
        PropertyResourceBundle msgs = (PropertyResourceBundle)
                        FacesUtils.getManagedBean(BeanNames.MSGS_BEAN);

        // we don't use the jsf message system as we are pushing and the messages
        // will be cleared on the next push. However we thought the event
        // to keep the invoke application from running.
        if (newBid <= originalBid) {
            // set the bean message holder.
            FacesMessage message = new FacesMessage();
            message.setDetail(msgs.getString("auction.validator.bidLow"));
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        } else if (newBid > MAX_BID ||
                newBid - originalBid > MAX_BID_INCREASE) {
            // set the bean message holder.
            FacesMessage message = new FacesMessage();
            message.setDetail(msgs.getString("auction.validator.bidHigh"));
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }

    }
}
