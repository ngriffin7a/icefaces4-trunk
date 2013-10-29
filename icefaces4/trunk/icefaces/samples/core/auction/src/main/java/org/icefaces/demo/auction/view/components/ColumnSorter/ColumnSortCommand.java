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

package org.icefaces.demo.auction.view.components.ColumnSorter;

import org.icefaces.demo.auction.view.beans.AuctionBean;
import org.icefaces.demo.auction.view.controllers.AuctionController;
import org.icefaces.demo.auction.view.names.BeanNames;
import org.icefaces.demo.auction.view.names.ParameterNames;
import org.icefaces.demo.auction.view.util.FacesUtils;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

/**
 * This class handles when a users clicks on the composite component's
 * ColumnSorter.  In short it handles the column sorting.
 * <p/>
 * &lt;util:columnSorter<br />
 *       ascending="#{auctionBean.ascending}"<br />
 *       sortColumn="#{auctionBean.sortColumn}"<br />
 *       columnName="#{auctionBean.itemNameColumn}"<br />
 *       columnLabel="Item Name"<br />
 *       columnClass="nameColumn"&gt;<br />
 *   &lt;f:actionListener for="sortColumnEvent"<br />
 *                     type="org.icefaces.demo.auction.view.components.ColumnSorter.ColumnSortCommand"/&gt;<br />
 * &lt;/util:columnSorter&gt;<br />
 *
 * @author ICEsoft Technologies Inc.
 * @since 2.0
 */
public class ColumnSortCommand implements ActionListener {

    /**
     * Sorts the list of AuctionItemBeans stored in the AuctionBean for the
     * column specified by the requestParameter ParameterNames.SORT_COLUMN_NAME.
     * <p/>
     * If the column name has not changed then the sort order is toggled.  If the
     * column name has changed then we leave the sort order the same. The final
     * state is pushed back into the AuctionBean.
     *
     * @param actionEvent JSF action event.
     */
    public void processAction(ActionEvent actionEvent) throws AbortProcessingException {
        // get request parameter for initial column sort click.
        String sortColumn =
                FacesUtils.getRequestParameter(ParameterNames.SORT_COLUMN_NAME);

        // auction model bean.
        AuctionBean auctionBean = (AuctionBean)
                FacesUtils.getManagedBean(BeanNames.AUCTION_BEAN);

        // auction controller bean
        AuctionController auctionController = (AuctionController)
                FacesUtils.getManagedBean(BeanNames.AUCTION_CONTROLLER);

        // test if we should toggle the sort order,  if the column name is the
        // same we do but otherwise we leave it the same as we're sorting on
        // a different column.
        if (sortColumn.equals(auctionBean.getSortColumn())) {
            auctionBean.setAscending(!auctionBean.isAscending());
        } else {
            // assign sort column name
            auctionBean.setSortColumn(sortColumn);
        }

        // call service layer to get new sorted list
        auctionController.refreshAuctionBean(auctionBean);
    }
}
