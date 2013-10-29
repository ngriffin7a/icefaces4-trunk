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
import org.icefaces.ace.component.columngroup.ColumnGroup;
import org.icefaces.ace.component.row.Row;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SortState {
    Map<Column, ColumnState> stateMap = new HashMap<Column, ColumnState>();

    class ColumnState implements Serializable {
        Integer priority;
        Boolean ascending;

        ColumnState(Integer sortPriority, Boolean ascending) {
            this.priority = sortPriority;
            this.ascending = ascending;
        }
    }

    public SortState() {}

    public static SortState getSortState(DataTable table) {
        SortState state = new SortState();

        List<Column> columnList = table.getColumns(true);
        for (Column column : columnList)
            state.saveState(column);

        return state;
    }

    public static SortState getSortStateFromRequest(FacesContext context, DataTable table) {
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        String clientId = table.getClientId(context);
        SortState self = new SortState();
        self.initFromRequest(context, table, params.get(clientId + "_sortKeys"));
        return self;
    }

    public static SortState getSortStateFromRequest(FacesContext context, DataTable table, String sortKeyString) {
        SortState self = new SortState();
        self.initFromRequest(context, table, sortKeyString);
        return self;
    }

    private void initFromRequest(FacesContext context, DataTable table, String sortKeyString) {
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        ColumnGroup group = table.getColumnGroup("header");
        Column sortColumn = null;
        String clientId = table.getClientId(context);
        String[] sortKeys = sortKeyString.split(",");
        String[] sortDirs = params.get(clientId + "_sortDirs").split(",");
        List<Column> columns = table.getColumns(true);

        if (sortKeys[0].equals("")) {
            return;
        }

        int i = 0;
        for (String sortKey : sortKeys) {
            if (group != null) {
                outer: for (UIComponent child : group.getChildren()) {
                    for (UIComponent headerRowChild : ((Row)child).getChildren()) {
                        if (headerRowChild instanceof Column)
                            if (headerRowChild.getClientId(context).equals(sortKey)) {
                                sortColumn = (Column) headerRowChild;
                                break outer;
                            }
                    }
                }
            } else {
                for (Column column : table.getColumns()) {
                    if (column.getClientId(context).equals(sortKey)) {
                        sortColumn = column;
                        break;
                    }
                }
            }

            saveState(sortColumn, i+1, Boolean.parseBoolean(sortDirs[i]));
            i++;
        }
    }

    public void saveState(Column column) {
        stateMap.put(column, new ColumnState(column.getSortPriority(), column.isSortAscending()));
    }

    public void saveState(Column column, Integer priority, Boolean ascending) {
        stateMap.put(column, new ColumnState(priority, ascending));
    }

    private void restoreState(Column column) {
        ColumnState state = stateMap.get(column);
        if (state != null) {
            column.setSortPriority(state.priority);
            column.setSortAscending(state.ascending);
        }
    }

    public void apply(DataTable table) {
        List<Column> columnList = table.getColumns(true);

        for (Column column : columnList) {
            column.setSortPriority(null);
            restoreState(column);
        }
    }
}
