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

package com.icesoft.applications.faces.auctionMonitor.comparator;

import com.icesoft.applications.faces.auctionMonitor.beans.AuctionMonitorItemBean;

import java.util.Comparator;

import java.io.Serializable;

/**
 * General comparator used as a base class for all other comparators Overall
 * this class performs a comparison of two auction items
 */
public abstract class AuctionMonitorItemComparator implements Comparator, Serializable {
    public boolean isAscending = true;

    public int compare(Object o1, Object o2) {
        if ((!(o1 instanceof AuctionMonitorItemBean))) {
            throw new ClassCastException(
                    "AuctionMonitorItemBean comparator compare invoked on arbitrary object");
        }

        AuctionMonitorItemBean item1 = (AuctionMonitorItemBean) o1;
        AuctionMonitorItemBean item2 = (AuctionMonitorItemBean) o2;

        if (isAscending) {
            return (compare(item1, item2));
        }

        return (compare(item2, item1));
    }

    public abstract int compare(AuctionMonitorItemBean item1,
                                AuctionMonitorItemBean item2);
}
