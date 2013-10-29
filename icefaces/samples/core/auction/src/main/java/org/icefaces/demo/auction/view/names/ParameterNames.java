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

package org.icefaces.demo.auction.view.names;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 * Constants class representing context parameters used in the view.
 *
 * @author ICEsoft Technologies Inc.
 * @since 2.0
 */
@ManagedBean
@ApplicationScoped
public class ParameterNames {

    public static final String AUCTION_ITEM = "auctionItem";

    public static final String ICE_SUBMIT_TYPE = "ice.submit.type";

    public static final String SORT_COLUMN_NAME = "columnName";

    public String getAuctionItem() {
        return AUCTION_ITEM;
    }

    public String getClientRequest() {
        return ICE_SUBMIT_TYPE;
    }

    public String getSortColumnName() {
        return SORT_COLUMN_NAME;
    }
}
