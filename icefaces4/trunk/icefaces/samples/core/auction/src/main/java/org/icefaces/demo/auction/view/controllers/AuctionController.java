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

import org.icefaces.application.PushRenderer;
import org.icefaces.demo.auction.services.AuctionService;
import org.icefaces.demo.auction.services.beans.AuctionItem;
import org.icefaces.demo.auction.view.beans.AuctionBean;
import org.icefaces.demo.auction.view.beans.AuctionItemBean;
import org.icefaces.demo.auction.view.names.BeanNames;
import org.icefaces.demo.auction.view.names.ParameterNames;
import org.icefaces.demo.auction.view.util.FacesUtils;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AuctionController is a stateless UI controller that handles JSF action
 * event generate from the auctionUI.  The controller interacts with the service
 * layer and various model beans for the current request.
 * <p/>
 * The Controller is stateless and all model data should be stored in the
 * {@link org.icefaces.demo.auction.view.beans.AuctionBean} object.  There are
 * two other controller in the application.  The class
 * {@link org.icefaces.demo.auction.view.controllers.IntervalPushRenderer} is
 * responsible for creating and managing a push call that runs at set intervals.
 * The other controller is a page level PhaseListener
 * {@link DataRefreshListener} which is used to updated the auction list before
 * the render response phase.  This is done for every request and it is assumed
 * that the underlying service layer has an advance caching mechanism.
 *
 * @author ICEsoft Technologies Inc.
 * @see org.icefaces.demo.auction.view.components.ColumnSorter.ColumnSortCommand
 *      For more information on how column sorting is executed.
 * @since 2.0
 */
@ManagedBean
@ApplicationScoped
public class AuctionController {

    private static Logger logger = Logger.getLogger(AuctionController.class.getName());

    /**
     * Calls the service layer for new auction data and merges it with the current
     * state of the auctionBean.  This method is called from the
     * {@link DataRefreshListener} for each request/response cycle.
     * <p/>
     * It is important that underlying service call has a caching layer to avoid
     * making costly DB or web services on every call.
     */
    public void refreshAuctionBean() {
        AuctionBean auctionBean = (AuctionBean)
                FacesUtils.getManagedBean(BeanNames.AUCTION_BEAN);
        refreshAuctionBean(auctionBean);
    }

    /**
     * Calls the service layer for new auction data and merges it with the current
     * view/window specific state of the auctionBean.
     *
     * @param auctionBean model data to update/refresh auction item data.
     */
    public void refreshAuctionBean(AuctionBean auctionBean) {
        // get the service
        AuctionService auctionService = (AuctionService)
                FacesUtils.getManagedBean(BeanNames.AUCTION_SERVICE);

        // get latest data from the auction service.
        List<AuctionItem> auctionItems = auctionService.getAllAuctionItems(
                auctionBean.getSortColumn(),
                auctionBean.isAscending());

        // not data so clear our bean and return.
        if (auctionItems == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Service layer contains no auction items. ");
            }
            auctionBean.setAuctionItems(null);
            return;
        }

        // apply the new data against the current view data.
        if (auctionBean.getAuctionItems() != null) {
            auctionBean.setAuctionItems(mergeAuctionData(auctionItems,
                    auctionBean.getAuctionItems()));
        }
        // no auction items means this is our first time making the refresh call.
        else {
            // wrap data
            List<AuctionItemBean> auctionItemBeans =
                    new ArrayList<AuctionItemBean>(auctionItems.size());
            for (AuctionItem auction : auctionItems) {
                auctionItemBeans.add(new AuctionItemBean(auction));
            }
            auctionBean.setAuctionItems(auctionItemBeans);
        }
    }

    /**
     * Utility method to do a bruit force merge of current view data with data
     * retrieved from the service layer.  Auction item id's are used to do the
     * merge and it is assumed that auction list ordering is preserved.
     *
     * @param newAuctionItems     list of latest AuctionItems.
     * @param currentAuctionItems current model to merge with latest AuctionItems.
     * @return merged list of AuctionItems beans, sort order is preserved.
     */
    private List<AuctionItemBean> mergeAuctionData(
            List<AuctionItem> newAuctionItems, List<AuctionItemBean> currentAuctionItems) {

        List<AuctionItemBean> currentAuctionItemBeans =
                new ArrayList<AuctionItemBean>(newAuctionItems.size());
        // bruit force merge/sync of the auctionItem data, respect the
        // new auctionItems order as it may have changed.
        double oldPrice;
        double newPrice;
        for (AuctionItem auctionItem : newAuctionItems) {
            for (AuctionItemBean auctionItemBean : currentAuctionItems) {
                if (auctionItemBean.getAuctionItem().getId() ==
                        auctionItem.getId()) {
                    oldPrice = auctionItemBean.getAuctionItem().getPrice();
                    newPrice = auctionItem.getPrice();
                    auctionItemBean.setAuctionItem(auctionItem);
                    currentAuctionItemBeans.add(auctionItemBean);
                    if (newPrice > oldPrice) {
                        auctionItemBean.setNewBidPrice(true);
                    } else {
                        auctionItemBean.setNewBidPrice(false);
                    }
                }
            }
        }
//        currentAuctionItems.clear();
        return currentAuctionItemBeans;
    }

    /**
     * Place a bid on the current auction item input bid field.  It is necessary
     * to get the actionItem that represent this row so that we only apply
     * the update to the current row, we don't want to update any other row
     * mistakenly with the bid action form submit.
     *
     * @param event JSF action event.
     */
    public void placeBid(ActionEvent event) {
        // get the service.
        AuctionService auctionService = (AuctionService)
                FacesUtils.getManagedBean(BeanNames.AUCTION_SERVICE);

        // get the auction item that initiated the bid.
        AuctionItemBean auctionItemBean = (AuctionItemBean)
                FacesUtils.getRequestMapValue(ParameterNames.AUCTION_ITEM);

        // bid value will already be validate by JSF so go ahead and apply the
        // new bid
        boolean success = auctionService.bidOnAuctionItem(
                auctionItemBean.getAuctionItem(),
                auctionItemBean.getBid());

        // someone beat us to it so show the error
        if (!success) {
            // retrieve our message bundle from the context.
            PropertyResourceBundle msgs = (PropertyResourceBundle)
                    FacesUtils.getManagedBean(BeanNames.MSGS_BEAN);
            FacesMessage message = new FacesMessage();
            message.setDetail(msgs.getString("auction.bid.rebid.error"));
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }

        // finally get the auctionBean model and update it with the new service
        // layer bid information.
        AuctionBean auctionBean = (AuctionBean)
                FacesUtils.getManagedBean(BeanNames.AUCTION_BEAN);
        refreshAuctionBean(auctionBean);

        // do a push out to anyone that is viewing the auction.
        PushRenderer.render(AuctionBean.AUCTION_RENDER_GROUP);
    }

    /**
     * Toggles visibility state of the bid button for the auctionItem id specified
     * by the context ParameterNames.AUCTION_ITEM.
     *
     * @param event JSF action event.
     */
    public void toggleBidInput(ActionEvent event) {
        // auctionItem param is actually the var name that was used by the
        // ui:repeat.  We always get respective value of the row that was
        // clicked on.
        AuctionItemBean auctionItemBean = (AuctionItemBean)
                FacesUtils.getRequestMapValue(ParameterNames.AUCTION_ITEM);
        auctionItemBean.setShowBidInput(!auctionItemBean.isShowBidInput());

        // hide the others.
        AuctionBean auctionBean = (AuctionBean)
                FacesUtils.getManagedBean(BeanNames.AUCTION_BEAN);
        List<AuctionItemBean> auctionItems = auctionBean.getAuctionItems();
        for (AuctionItemBean auctionItem : auctionItems) {
            if (!auctionItem.equals(auctionItemBean)) {
                auctionItem.setShowBidInput(false);
            }
        }

        // assign view scoped bid with the current bid value plus a little extra
        auctionItemBean.setBid(auctionItemBean.getAuctionItem().getPrice() + 5);
    }

    /**
     * Toggles visibility state of the extended auction description for the
     * auctionItem id specified by the context ParameterNames.AUCTION_ITEM.
     *
     * @param event JSF action event.
     */
    public void toggleExtendedDescription(ActionEvent event) {
        // auctionItem param is actually the var name that was used by the
        // ui:repeat.  We always get respective value of the row that was
        // clicked on.
        AuctionItemBean auctionItemBean = (AuctionItemBean)
                FacesUtils.getRequestMapValue(ParameterNames.AUCTION_ITEM);
        auctionItemBean.setShowExtendedDescription(
                !auctionItemBean.isShowExtendedDescription());
    }
}
