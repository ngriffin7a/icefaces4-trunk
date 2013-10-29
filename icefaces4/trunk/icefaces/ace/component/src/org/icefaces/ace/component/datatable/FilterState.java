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

package org.icefaces.ace.component.datatable;

import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.context.RequestContext;

import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterState {
    Map<Column, String> valueMap = new HashMap<Column, String>();

    public FilterState() {}

    /* Create comprehensive current filter state */
    public FilterState(DataTable table) {
        List<Column> columnList = table.getColumns(true);
        for (Column column : columnList)
            saveState(column);
    }

    /* Create delta state from incoming filter input */
    public FilterState(FacesContext context, DataTable table) {
        String clientId = table.getClientId(context);
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        String filteredId = params.get(clientId + "_filteredColumn");
        Column filteredColumn = null;

        Map<String,Column> filterMap = table.getFilterMap();

        // If applying a new filter, save the value to the column
        filteredColumn = filterMap.get(filteredId);

        if (filteredColumn != null)
            saveState(filteredColumn, params.get(filteredId).toLowerCase());
    }

    public void saveState(Column column) {
        valueMap.put(column, column.getFilterValue());
    }

    public void saveState(Column column, String value) {
        valueMap.put(column, value);
    }

    private void restoreState(Column column) {
        String val = valueMap.get(column);
        if (val != null)
            column.setFilterValue(val);
    }

    public void apply(DataTable table) {
        List<Column> columnList = table.getColumns(true);
        for (Column column : columnList)
            restoreState(column);
    }
}
