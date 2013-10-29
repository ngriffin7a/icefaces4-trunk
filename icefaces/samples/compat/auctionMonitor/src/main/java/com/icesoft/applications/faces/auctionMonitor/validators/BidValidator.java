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

package com.icesoft.applications.faces.auctionMonitor.validators;

import com.icesoft.applications.faces.auctionMonitor.beans.AuctionMonitorItemBean;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Pretty simple validator that checks to see if the auction bid is within
 * an acceptable bid range.  For demo purposes we don't want to let people make
 * really big bids as well as bids that are smaller then the current bid.
 */
public class BidValidator implements Validator {
    
    // cap the size of a bid that can be made over the the original bid value.
    public static final int MAX_BID_INCREASE = 10000;
    // max bid that can be made, period.
    public static final long MAX_BID = 1000000000;

    public static final String ERROR_BID_LOW = "<br />Bid declined, too low.";
    public static final String ERROR_BID_HIGH = "<br />Bid declined, too high.";

    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value)
            throws ValidatorException {

        // get instance of item so we can set a custom message.
        // Grab the data object for the row that was clicked
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

        // ge the item that initiated the request.
        AuctionMonitorItemBean auctionMonitorItemBean = (AuctionMonitorItemBean)
                externalContext.getRequestMap().get("item");
        // get the original bid value
        double originalBid = auctionMonitorItemBean.getCurrentPrice();
        // get the new value
        double newBid = (Double)value;

        // we don't use the jsf message system as we are pushing and the messages
        // will be cleared on the next push. However we thought the event
        // to keep the invoke application from running.
        if (newBid <= originalBid){
            // set the bean message holder. 
            auctionMonitorItemBean.setBidMessage(ERROR_BID_LOW);
            FacesMessage message = new FacesMessage();
            message.setDetail(ERROR_BID_LOW);
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        } else if (newBid >= MAX_BID ||
            newBid - originalBid > MAX_BID_INCREASE) {
            // set the bean message holder.
            auctionMonitorItemBean.setBidMessage(ERROR_BID_HIGH);
            FacesMessage message = new FacesMessage();
            message.setDetail(ERROR_BID_HIGH);
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        } else{
            // nothing wrong clear message.
            // set the bean message holder.
            auctionMonitorItemBean.setBidMessage("");
        }

    }
}
