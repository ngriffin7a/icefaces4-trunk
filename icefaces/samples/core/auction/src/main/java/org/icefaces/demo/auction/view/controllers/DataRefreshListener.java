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

package org.icefaces.demo.auction.view.controllers;

import org.icefaces.demo.auction.view.names.BeanNames;
import org.icefaces.demo.auction.view.util.FacesUtils;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * This phase listener refreshes the auction data by calling the service layer.
 * It is important that the service layer has a caching layer implemented to insure
 * that a data refresh does not hit the DB or web service directly with each
 * call to refresh.
 *
 * @author ICEsoft Technologies Inc.
 * @since 2.0
 */
public class DataRefreshListener implements PhaseListener {

    public void beforePhase(PhaseEvent phaseEvent) {
        // get instance of controller for the current context.
        AuctionController auctionController = (AuctionController)
                FacesUtils.getManagedBean(BeanNames.AUCTION_CONTROLLER);

        // trigger a refresh, which will get latest data from service, we do
        // this for every request and thus it is important that the service
        // layer uses a caching layer that insures we don't hit the DB or
        // web service on every call. 
        auctionController.refreshAuctionBean();

    }

    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

    public void afterPhase(PhaseEvent phaseEvent) {

    }
}
