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

import org.icefaces.demo.auction.services.beans.AuctionItem;

import java.util.Calendar;
import java.io.Serializable;

/**
 * Wraps an instance of the AuctionItem DAO object returned from the service
 * layer.  This wrapper introduces some view or window level state;
 * <ul>
 * <li>bid - bid value that has yet to be submitted to server.</li>
 * <li>showBidInput - boolean flag to render the bid input
 * field. This flag is togged by the controller class.</li>
 * <li>showExtendedDescription - boolean flag to render the extended auctionItem
 * description information. </li>
 * <li>newBidPrice - boolean flag to used ot quickly emphasise a AuctionItem
 * bid value has change by the correct user or a server push.  </li>
 * </ul>
 * This class is a model bean and it's state should only be changed by the
 * {@link org.icefaces.demo.auction.view.controllers.AuctionController}.
 *
 * @author ICEsoft Technologies Inc.
 * @since 2.0
 */
public class AuctionItemBean implements Serializable  {

    // time left constants.
    public static final int TIME_LEFT_5_SEC = 0;
    public static final int TIME_LEFT_10_SEC = 1;
    public static final int TIME_LEFT_15_SEC = 2;
    public static final int TIME_LEFT_30_SEC = 3;
    public static final int TIME_LEFT_45_SEC = 4;
    public static final int TIME_LEFT_HOURS = 5;
    public static final int TIME_LEFT_DAYS = 6;
    public static final int TIME_LEFT_EXPIRED = 7;
    public static final int TIME_LEFT_NA = 8;

    // broken down time constants.
    public static final int DAY_COMPONENT = 0;
    public static final int HOUR_COMPONENT = 1;
    public static final int MINUTE_COMPONENT = 2;
    public static final int SECOND_COMPONENT = 3;

    // time constants.
    private static final int TIME_DAYS = 24 * 60 * 60 * 1000;
    private static final int TIME_HOURS = 60 * 60 * 1000;
    private static final int TIME_MINUTES = 60 * 1000;

    private AuctionItem auctionItem;

    private double bid;

    private boolean showBidInput;

    private boolean showExtendedDescription;

    private boolean newBidPrice;

    public AuctionItemBean(AuctionItem auctionItem) {
        this.auctionItem = auctionItem;
    }

    public AuctionItem getAuctionItem() {
        return auctionItem;
    }

    public void setAuctionItem(AuctionItem auctionItem) {
        this.auctionItem = auctionItem;
    }

    public boolean isShowBidInput() {
        return showBidInput;
    }

    public void setShowBidInput(boolean showBidInput) {
        this.showBidInput = showBidInput;
    }

    public boolean isShowExtendedDescription() {
        return showExtendedDescription;
    }

    public void setShowExtendedDescription(boolean showExtendedDescription) {
        this.showExtendedDescription = showExtendedDescription;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public boolean isNewBidPrice() {
        return newBidPrice;
    }

    public void setNewBidPrice(boolean newBidPrice) {
        this.newBidPrice = newBidPrice;
    }

    /**
     * Utility to split in the expiry date into number of days, minutes
     * and seconds.  This method should never be called via JSF value binding.
     * It should instead be called only from a JSF converter or Auction event
     * to insure that logic is only called a minimum number of times during
     * a JSF lifecycle execution.
     *
     * @return a long array made up of four elements.  Each index in the array
     *         is represented by the name value paring constants, DAY_COMPONENT,
     *         HOUR_COMPONENT, MINUTE_COMPONENT and SECOND_COMPONENT.
     */
    public long[] getTimeLeftBrokenDown() {
        if (auctionItem.getExpiryDate() != null) {
            long left, days, hours, minutes, seconds;
            left = auctionItem.getExpiryDate().getTime().getTime()
                    - Calendar.getInstance().getTime().getTime();
            days = left / TIME_DAYS;
            left = left - days * TIME_DAYS;
            hours = left / TIME_HOURS;
            left = left - hours * TIME_HOURS;
            minutes = left / TIME_MINUTES;
            left = left - minutes * TIME_MINUTES;
            seconds = left / 1000;
            return new long[]{days, hours, minutes, seconds};
        } else {
            return null;
        }
    }

    /**
     * Utility to determine if the countDownTime parameter is paste due or expired.
     * This method should not be called via regular value binding instead it should
     * only be called from validator ar converter calls.
     *
     * @param countDownTime array of 4 digits representing the number of day,
     *                      hours, minutes and seconds remaining before the item expires.
     * @return true if the auction item has expired, otherwise false, the auction
     *         is still ongoing.
     * @see #getTimeLeftBrokenDown()
     */
    public boolean isExpired(long[] countDownTime) {
        return countDownTime[AuctionItemBean.DAY_COMPONENT] <= 0 &&
                countDownTime[AuctionItemBean.HOUR_COMPONENT] <= 0 &&
                countDownTime[AuctionItemBean.MINUTE_COMPONENT] <= 0 &&
                countDownTime[AuctionItemBean.SECOND_COMPONENT] <= 0;
    }

    /**
     * Utility to determine if the expiry date is paste due or expired.  This
     * method should not be called via regular value binding instead it should
     * only be called from validator ar converter calls.
     * @return true if the auction item has expired, otherwise false, the auction
     *         is still ongoing.
     */
    public boolean isExpired() {
        return isExpired(getTimeLeftBrokenDown());
    }

    /**
     * Gets the time left type which can be any class constants starting with
     * TIME_LEFT_*.  This should only be called via a JSF converter.  The
     * returned strings are used by the view to show different icons based on
     * the TIME_LEFT_* type.
     *
     * @return string represented the time left constant.  
     */
    public String getTimeLeftType() {
        // convert the date and subtract the current date.
        if (auctionItem.getExpiryDate() != null) {
            long[] countDownTime = getTimeLeftBrokenDown();
            // convert to d HH:MM:SS for days > 0
            if (countDownTime[DAY_COMPONENT] > 0) {
                return String.valueOf(TIME_LEFT_DAYS);
            }
            // convert to MM:SS for days == 0 and hours > 0
            else if (countDownTime[HOUR_COMPONENT] > 0) {
                return String.valueOf(TIME_LEFT_HOURS);
            }
            // convert to SS sec for hours = 0
            else if (countDownTime[SECOND_COMPONENT] >= 0) {
                long seconds = countDownTime[SECOND_COMPONENT];
                if (seconds < 60 && seconds >= 45) {
                    return String.valueOf(TIME_LEFT_45_SEC);
                } else if (seconds < 45 && seconds >= 30) {
                    return String.valueOf(TIME_LEFT_30_SEC);
                } else if (seconds < 30 && seconds >= 15) {
                    return String.valueOf(TIME_LEFT_15_SEC);
                } else if (seconds < 15 && seconds >= 10) {
                    return String.valueOf(TIME_LEFT_15_SEC);
                } else if (seconds < 10 && seconds >= 5) {
                    return String.valueOf(TIME_LEFT_10_SEC);
                } else if (seconds < 5 && seconds > 0) {
                    return String.valueOf(TIME_LEFT_5_SEC);
                } else {
                    return String.valueOf(TIME_LEFT_EXPIRED);
                }
            } else {
                return String.valueOf(TIME_LEFT_EXPIRED);
            }
        } else {
            return String.valueOf(TIME_LEFT_NA);
        }
    }
}
