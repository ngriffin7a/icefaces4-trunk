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

package org.icefaces.demo.auction.view.beans;

import org.icefaces.application.PushRenderer;
import org.icefaces.demo.auction.view.controllers.AuctionController;
import org.icefaces.demo.auction.view.controllers.IntervalPushRenderer;
import org.icefaces.demo.auction.view.names.BeanNames;
import org.icefaces.demo.auction.view.util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This object stores a collection of AuctionItemBean as well as state information
 * for sort ordering.  The Bean should live in View or Window scope  so that
 * each browser window can have it's own UI state.  After the bean is initialized
 * an @PostConstruct call initializes the auctionItemBean collection using
 * logic stored in the {@link AuctionController}.
 * <p/>
 * When a user clicks on the sortable headers on the page the sort order is
 * stored in this Bean, sort column name and the order of the sort, descending
 * or ascending.
 *
 * @author ICEsoft Technologies Inc.
 * @since 2.0
 */
@ManagedBean
@ViewScoped
//@CustomScoped(value = "#{window}")
public class AuctionBean implements Serializable {

    private static Logger logger = Logger.getLogger(AuctionBean.class.getName());

    // render group this bean is added to for pushes and
    public static final String AUCTION_RENDER_GROUP = "auctionRenderGroup";

    // columns that can sorted on
    public static final String ITEM_NAME_COLUMN = "nameColumn";
    public static final String PRICE_COLUMN = "priceColumn";
    public static final String BIDS_COLUMN = "bidsColumn";
    public static final String TIME_LEFT_COLUMN = "timeLeftColumn";

    // default sort column
    private String sortColumn = TIME_LEFT_COLUMN;
    private boolean isDescending;

    // list of auction items to display on screen
    private List<AuctionItemBean> auctionItems;

    /**
     * After the bean is created it calls service layer to refresh its
     * auction items.  The refresh will preserve the data view only updating
     * the auctionItems.
     */
    @PostConstruct
    private void setInitialize() {

        // get instance of the service bean and assign
        AuctionController auctionController = (AuctionController)
                FacesUtils.getManagedBean(BeanNames.AUCTION_CONTROLLER);

        // use the controller to apply the update logic.
        auctionController.refreshAuctionBean(this);

        // register with bid renderer, used for bid pushes
        PushRenderer.addCurrentSession(AUCTION_RENDER_GROUP);
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("added current session to renderer 'auctionRenderGroup'");
        }
        // Interval render does pushes every x seconds, to refresh time remaining
        // time stamps and graphics. 
        PushRenderer.addCurrentSession(IntervalPushRenderer.INTERVAL_RENDER_GROUP);
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("added current session to renderer 'auctionInterval'");
        }

    }

    /**
     * Remove this object from the two render groups it is registered with.
     */
    @PreDestroy
    private void destroy() {
        // remove from bid render group
        PushRenderer.removeCurrentView(AUCTION_RENDER_GROUP);
        PushRenderer.removeCurrentView(IntervalPushRenderer.INTERVAL_RENDER_GROUP);
    }


    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public boolean isAscending() {
        return isDescending;
    }

    public void setAscending(boolean descending) {
        isDescending = descending;
    }

    public List<AuctionItemBean> getAuctionItems() {
        return auctionItems;
    }

    public void setAuctionItems(List<AuctionItemBean> auctionItems) {
        this.auctionItems = auctionItems;
    }

    public String getItemNameColumn() {
        return ITEM_NAME_COLUMN;
    }

    public String getPriceColumn() {
        return PRICE_COLUMN;
    }

    public String getBidsColumn() {
        return BIDS_COLUMN;
    }

    public String getTimeLeftColumn() {
        return TIME_LEFT_COLUMN;
    }
}
