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

package org.icefaces.application.showcase.view.bean.examples.component.dataPaginator;

import org.icefaces.application.showcase.view.bean.examples.component.dataTable.DataTableBase;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.HashMap;

/**
 * <p>The DataScrollingModel class is used to show how a dataTable data
 * scrolling can be modfied.  There are three display modes for the table;
 * default or no scrolling, scrolling and paging.</p>
 * <p>Scrolling is handled by an attribute on the dataTable component along with
 * a scroll height specification of the scroll viewport</p>
 * <p>Paging is handle by the dataPaginator component. </p>
 *
 * @since 1.7
 */
@ManagedBean
@ViewScoped
public class DataScrollingModel extends DataTableBase {
    /**
     * dataTable will have no pagging or scrolling enabled.
     */
    public static final String NO_SCROLLING = "none";
    /**
     * dataTable will have scrolling enabled.
     */
    public static final String SCROLLING_SCROLLING = "scrolling";
    /**
     * dataTable will have paging enabled.
     */
    public static final String PAGINATOR_SCROLLING = "paging";

    // currently select scrolling state select by user.
    private String selectedDataScrollMode;
    private static HashMap selectedDataScrollModes;

    /**
     * Creates a new instance where the efault scrolling is none.
     */
    public DataScrollingModel() {

        selectedDataScrollMode = PAGINATOR_SCROLLING;

        selectedDataScrollModes = new HashMap();

        // default data table setting
        selectedDataScrollModes.put(NO_SCROLLING,
                new DataScrollMode(0, false, false));

        // scrolling data table settings
        selectedDataScrollModes.put(SCROLLING_SCROLLING,
                new DataScrollMode(0, true, false));

        // paging data table settings
        selectedDataScrollModes.put(PAGINATOR_SCROLLING,
                new DataScrollMode(9, false, true));
    }

    public String getSelectedDataScrollMode() {
        return selectedDataScrollMode;
    }

    public void setSelectedDataScrollMode(String selectedDataScrollMode) {
        this.selectedDataScrollMode = selectedDataScrollMode;
    }

    public HashMap getSelectedDataScrollModes() {
        return selectedDataScrollModes;
    }

    /**
     * Get all possible records from our service layer.
     */
    protected void init() {
        // build employee list form employee service.
        employees = employeeService.getEmployees(50);
    }

    /**
     * Utility method for storing the states of the different scrolling modes.
     * This class is used alone with standard JSF Map notation to retreive
     * specific properties.
     */
    public class DataScrollMode {
        // number of rows to display when paging, default value (0) shows
        // all records.
        private int rows;
        // scrolling enabled
        private boolean scrollingEnabled;
        // paging enabled.
        private boolean pagingEnabled;

        public DataScrollMode(int rows, boolean scrollingEnabled,
                              boolean pagingEnabled) {
            this.rows = rows;
            this.scrollingEnabled = scrollingEnabled;
            this.pagingEnabled = pagingEnabled;
        }

        public int getRows() {
            return rows;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }

        public boolean isScrollingEnabled() {
            return scrollingEnabled;
        }

        public void setScrollingEnabled(boolean scrollingEnabled) {
            this.scrollingEnabled = scrollingEnabled;
        }

        public boolean isPagingEnabled() {
            return pagingEnabled;
        }

        public void setPagingEnabled(boolean pagingEnabled) {
            this.pagingEnabled = pagingEnabled;
        }

    }
}
