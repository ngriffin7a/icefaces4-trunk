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

package org.icefaces.demo.auction.services.beans;

import java.util.GregorianCalendar;
import java.io.Serializable;

/**
 * AuctionItem represents one auction item that is contained in an an auction.
 * An item stores descriptive information about the item being auctioned as
 * well as the bid price and number of bids made on the item.  The last very
 * important piece of information is when the auction will expire.
 * <p/>
 * Most instance variables in the is class are immutable except for the bid
 * count and the bid price.
 *
 * @author ICEsoft Technologies Inc.
 * @since 2.0
 */
public class AuctionItem implements Serializable  {

    private long id;
    private String name;
    private String description;
    private String sellerLocation;
    private String sellerName;
    private GregorianCalendar expiryDate;
    private double price;
    private int bids;
    private String imageName;

    /**
     * Creates a new instance an auction item given the new parameter values.
     *
     * @param id              id that uniquely identifies the item.
     * @param name            name of item
     * @param description     description of item
     * @param sellerLocation  location of the seller of this item.
     * @param sellerName      sellers name for this item.
     * @param expiryDate      date of expiry.
     * @param currentPidPrice current bid price.
     * @param bids            number of bids made on item.
     * @param imageName       item image file name.
     */
    public AuctionItem(long id, String name, String description,
                       String sellerLocation, String sellerName,
                       GregorianCalendar expiryDate, double currentPidPrice, int bids,
                       String imageName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sellerLocation = sellerLocation;
        this.sellerName = sellerName;
        this.expiryDate = expiryDate;
        this.price = currentPidPrice;
        this.bids = bids;
        this.imageName = imageName;
    }

    /**
     * Creates a new instance of AuctionItem containing a copy of the
     * AuctionItem properties specified as a parameter.
     * specified
     * <p/>
     * The method insures that we have different instances of each auctionItem
     * for each user of the system. This model is consistent with most
     * persistence frameworks.
     *
     * @param auctionItem auctionTime to copy properties from.
     */
    public AuctionItem(AuctionItem auctionItem) {
        this.id = auctionItem.id;
        this.name = auctionItem.name;
        this.description = auctionItem.description;
        this.sellerLocation = auctionItem.sellerLocation;
        this.sellerName = auctionItem.sellerName;
        this.expiryDate = auctionItem.expiryDate;
        this.price = auctionItem.price;
        this.bids = auctionItem.bids;
        this.imageName = auctionItem.imageName;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }

    public void setBids(int bids) {
        this.bids = bids;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSellerLocation() {
        return sellerLocation;
    }

    public String getSellerName() {
        return sellerName;
    }

    public GregorianCalendar getExpiryDate() {
        return expiryDate;
    }

    public double getPrice() {
        return price;
    }

    public int getBids() {
        return bids;
    }

    public String getImageName() {
        return imageName;
    }
}
