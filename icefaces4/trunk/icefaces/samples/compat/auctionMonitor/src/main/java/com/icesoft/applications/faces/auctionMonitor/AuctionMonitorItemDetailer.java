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

package com.icesoft.applications.faces.auctionMonitor;

import com.icesoft.applications.faces.auctionMonitor.beans.AuctionBean;
import com.icesoft.applications.faces.auctionMonitor.beans.AuctionMonitorItemBean;
import com.icesoft.applications.faces.auctionMonitor.stubs.ItemType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class used to populate auction items with information on urls, descriptions,
 * etc. All item detailing is done in a separate thread so user interaction is
 * not blocked
 */
public class AuctionMonitorItemDetailer implements Runnable {
    private static Log log =
            LogFactory.getLog(AuctionMonitorItemDetailer.class);
    private AuctionMonitorItemBean[] searchItemBeans;
    private AuctionBean auctionBean;

    public AuctionMonitorItemDetailer(AuctionBean auctionBean,
                                      AuctionMonitorItemBean[] searchItemBeans) {
        this.auctionBean = auctionBean;
        this.searchItemBeans =
                (AuctionMonitorItemBean[]) searchItemBeans.clone();
    }

    public void run() {
        AuctionMonitorItemBean itemBean;
        for (int i = 0, max = searchItemBeans.length; i < max; i++) {
            try {
                itemBean = searchItemBeans[i];
                ItemType item = auctionBean.getItem(itemBean.getItemID());
                itemBean.setDescription(item.getDescription());
                itemBean.setSeller(item.getSeller());
                itemBean.setLocation(item.getLocation());
            } catch (NullPointerException npe) {
                // intentionally left blank
            } catch (Exception e) {
                if (log.isWarnEnabled()) {
                    log.warn("A threaded item detailer failed because of " + e);
                }
            }
        }
    }
}