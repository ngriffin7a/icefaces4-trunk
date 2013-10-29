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

package org.icefaces.demo.auction.services.impl;

import org.icefaces.demo.auction.services.AuctionService;
import org.icefaces.demo.auction.services.Comparator.*;
import org.icefaces.demo.auction.services.beans.AuctionItem;
import org.icefaces.demo.auction.view.beans.AuctionBean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Simple in memory representation of the auction items.
 *
 * @author ICEsoft Technologies Inc.
 * @since 2.0
 */
@ManagedBean(eager = true)
@ApplicationScoped
public class AuctionServiceImpl implements AuctionService {

    private ArrayList<AuctionItem> auctionItems;

    /**
     * This initialization procedure is just for this basic service
     * implementation.  In the real work the data was be fetched from a data
     * store or via some web service.
     * <p/>
     * We build up four AuctionItem object and tweak the expiry date as needed
     * to give a compelling demo or test case. 
     */
    @PostConstruct
    public void initializeData() {
        // build  simple list of 4 auction items.
        auctionItems = new ArrayList<AuctionItem>(4);
        // ICE breaker
        auctionItems.add(new AuctionItem(1,
                "ICEsoft Ice Breaker",
                "Used icebreaker with very few dents, comes with manual.",
                "Calgary, Alberta Canada",
                "ICEsoft Technologies, Inc.",
                new GregorianCalendar(),
                5.0, 0, "icebreaker.jpg"));
        auctionItems.get(0).getExpiryDate().add(GregorianCalendar.DAY_OF_YEAR, 2);
        auctionItems.get(0).getExpiryDate().add(GregorianCalendar.HOUR_OF_DAY, 8);
        auctionItems.get(0).getExpiryDate().add(GregorianCalendar.SECOND, 60);
        // ICE Skate
        auctionItems.add(new AuctionItem(2,
                "ICEsoft Ice Skate",
                "A single sharpened ice skate, size 7.",
                "Calgary, Alberta Canada",
                "ICEsoft Technologies, Inc.",
                new GregorianCalendar(),
                100.0, 0, "iceskate.jpg"));
//        auctionItems.get(1).getExpiryDate().add(GregorianCalendar.DAY_OF_YEAR, 5);
        auctionItems.get(1).getExpiryDate().add(GregorianCalendar.HOUR_OF_DAY, 8);
        auctionItems.get(1).getExpiryDate().add(GregorianCalendar.SECOND, 15);
        // ICE Car
        auctionItems.add(new AuctionItem(3,
                "ICEsoft Ice Car",
                "Beautiful ice car with metal car filling.",
                "Calgary, Alberta Canada",
                "ICEsoft Technologies, Inc.",
                new GregorianCalendar(),
                10.0, 0, "icecar.jpg"));
        auctionItems.get(2).getExpiryDate().add(GregorianCalendar.DAY_OF_YEAR, 3);
        auctionItems.get(2).getExpiryDate().add(GregorianCalendar.HOUR_OF_DAY, 5);
        auctionItems.get(2).getExpiryDate().add(GregorianCalendar.SECOND, 10);
        // Ice Sailor
        auctionItems.add(new AuctionItem(4,
                "ICEsoft Ice Sailor",
                "Put him on the ice and watch him go!  Requires food and water.",
                "Calgary, Alberta Canada",
                "ICEsoft Technologies, Inc.",
                new GregorianCalendar(),
                10000.0, 0, "icesailor.jpg"));
        auctionItems.get(3).getExpiryDate().add(GregorianCalendar.DAY_OF_YEAR, 7);
        auctionItems.get(3).getExpiryDate().add(GregorianCalendar.HOUR_OF_DAY, 2);
        auctionItems.get(3).getExpiryDate().add(GregorianCalendar.SECOND, 19);
    }

    /**
     * Utility method that resets the auction item list.  This used for demo
     * purposes as the auction items may need to be reset if the bids become
     * too large or have expired.
     */
    public void resetAuctionItemCache() {
        initializeData();
    }

     /**
     * Executes a bid transaction on the specified auctionItem for the specified
     * amount.
     *
     * @param auctionItem auction item being bid on.
     * @param bid         bid value to execute.
     * @return true if the bid was successful.  False, if another users beats us
     *         in making a equal or larger bid before us.
     */
    public synchronized boolean bidOnAuctionItem(AuctionItem auctionItem, double bid) {
        // check to see if bid is valid, did someone else bid before use?
        for (AuctionItem item : auctionItems) {
            if (item.getId() == auctionItem.getId()) {
                // apply the bid if things match up more or less
                if (item.getPrice() == auctionItem.getPrice()) {
                    item.setPrice(bid);
                    item.setBids(item.getBids() + 1);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Get a snapshot of the auction items in the auction for a given moment
     * in time.  The method is responsible for sorting the data as well, we
     * generally don't won't the UI code to to this as most DB's can do this
     * a lot more effeciently.
     *
     * @param sortColumn  column being sorted, should be constant as defined in
     *                    {@link org.icefaces.demo.auction.view.beans.AuctionBean}
     * @param isAscending true indicates ascending sort, false indicates
     *                    descending sort.
     * @return list of auction items in auction, can be null.
     */
    public List<AuctionItem> getAllAuctionItems(String sortColumn, boolean isAscending) {

        // always return a copy of the core object to insure that the data
        // refresh via push is working as expected.
        ArrayList<AuctionItem> currentAuctionItems =
                new ArrayList<AuctionItem>(auctionItems.size());
        for (AuctionItem auctionItem : auctionItems) {
            currentAuctionItems.add(new AuctionItem(auctionItem));
        }

        // apply single column sorting. 
        if (AuctionBean.ITEM_NAME_COLUMN.equals(sortColumn)) {
            AbstractItemComparator itemNameComparator =
                    new ItemNameComparator(isAscending);
            Collections.sort(currentAuctionItems, itemNameComparator);
        } else if (AuctionBean.PRICE_COLUMN.equals(sortColumn)) {
            AbstractItemComparator itemNameComparator =
                    new ItemPriceComparator(isAscending);
            Collections.sort(currentAuctionItems, itemNameComparator);
        } else if (AuctionBean.BIDS_COLUMN.equals(sortColumn)) {
            AbstractItemComparator itemBidComparator =
                    new ItemBidsComparator(isAscending);
            Collections.sort(currentAuctionItems, itemBidComparator);
        } else if (AuctionBean.TIME_LEFT_COLUMN.equals(sortColumn)) {
            AbstractItemComparator itemExpiresComparator =
                    new ItemExpiresComparator(isAscending);
            Collections.sort(currentAuctionItems, itemExpiresComparator);
        }

        return currentAuctionItems;
    }
}
