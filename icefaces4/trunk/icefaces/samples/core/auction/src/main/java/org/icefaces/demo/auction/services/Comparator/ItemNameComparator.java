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

package org.icefaces.demo.auction.services.Comparator;

import org.icefaces.demo.auction.services.beans.AuctionItem;

/**
 * Sorts an AuctionItem using the field "name" in the order specified by the
 * constructor's ascending attribute.
 *
 * @author ICEsoft Technologies Inc.
 * @since 2.0
 */
public class ItemNameComparator extends AbstractItemComparator {

    public ItemNameComparator(boolean ascending) {
        super(ascending);
    }

    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
     * <p/>
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the
     *         second.
     */
    @Override
    public int compare(AuctionItem o1, AuctionItem o2) {
        if (isAscending) {
            return o1.getName().compareTo(o2.getName());
        } else {
            return o2.getName().compareTo(o1.getName());
        }
    }
}
