/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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
import org.icefaces.ace.component.tableconfigpanel.TableConfigPanel;
import org.icefaces.ace.context.RequestContext;
import org.icefaces.ace.event.SelectEvent;
import org.icefaces.ace.event.UnselectEvent;
import org.icefaces.ace.json.JSONException;
import org.icefaces.ace.json.JSONObject;
import org.icefaces.ace.model.table.*;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataTableDecoder {
    // --------------------------------------------------------------------- //
    // Selection ----------------------------------------------------------- //
    // --------------------------------------------------------------------- //
    static void decodeSelection(FacesContext context, DataTable table) {
        String clientId = table.getClientId(context);
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();

        table.savedSelectionChanges.put(table.getClientId(context), new SelectionDeltaState(context, table));

        queueInstantSelectionEvent(context, table, clientId, params);
    }

    static void queueInstantSelectionEvent(FacesContext context, DataTable table, String clientId, Map<String,String> params) {
        if (table.isInstantSelectionRequest(context)) {
            Object model = table.getDataModel();
            TreeDataModel treeModel = null;
            String selection = params.get(clientId + "_instantSelectedRowIndexes");
            String[] indexes = selection.split(",");
            Object[] objs = new Object[indexes.length];

            for (int i = 0; i < indexes.length; i++) {
                String index = indexes[i];
				if ("".equals(index)) {
					objs[i] = null;
					continue;
				}
                // If selection occurs with a TreeModel and non-root index
                if (table.hasTreeDataModel() && selection.indexOf('.') > 0) {
                    treeModel = (TreeDataModel) model;
                    int lastSepIndex = index.lastIndexOf('.');
                    treeModel.setRootIndex(index.substring(0, lastSepIndex));
                    index = index.substring(lastSepIndex+1);
                }

                table.setRowIndex(Integer.parseInt(index));
                objs[i] = table.getRowData();
            }
            SelectEvent selectEvent = new SelectEvent(table, objs);
            selectEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
            table.queueEvent(selectEvent);

            if (treeModel != null) treeModel.setRootIndex(null);
        }
        else if (table.isInstantUnselectionRequest(context)) {
            Object model = table.getDataModel();
            TreeDataModel treeModel = null;
            String selection = params.get(clientId + "_instantUnselectedRowIndexes");

            // If unselection occurs with a TreeModel and non-root index
            if (table.hasTreeDataModel() && selection.indexOf('.') > 0) {
                treeModel = (TreeDataModel) model;
                int lastSepIndex = selection.lastIndexOf('.');
                treeModel.setRootIndex(selection.substring(0, lastSepIndex));
                selection = selection.substring(lastSepIndex+1);
            }

            int unselectedRowIndex = Integer.parseInt(selection);
            table.setRowIndex(unselectedRowIndex);
            UnselectEvent unselectEvent = new UnselectEvent(table, table.getRowData());
            unselectEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
            table.queueEvent(unselectEvent);

            if (treeModel != null) treeModel.setRootIndex(null);
        }

        table.setRowIndex(-1);
    }

    // --------------------------------------------------------------------- //
    // Filter -------------------------------------------------------------- //
    // --------------------------------------------------------------------- //
    static void decodeFilters(FacesContext context, DataTable table) {
        String clientId = table.getClientId(context);
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        // Ensure this refiltering occurs on the original data
        table.setFirst(0);
        table.setPage(1);

        if (table.isLazy()) {
            String filteredId = params.get(clientId + "_filteredColumn");
            Column filteredColumn = null;

            // If in lazy case, just save change to filter input. Load method must account for the rest.
            Map<String,Column> filterMap = table.getFilterMap();
            filteredColumn = filterMap.get(filteredId);
            if (filteredColumn != null) filteredColumn.setFilterValue(params.get(filteredId));
        } else {
            table.savedFilterState = new FilterState(context, table);
            table.applyFilters();
        }
    }


    
    // --------------------------------------------------------------------- //
    // Sort ---------------------------------------------------------------- //
    // --------------------------------------------------------------------- //
    static void decodeSortRequest(FacesContext context, DataTable table, String clientId, String sortKeysInput) {
        table.savedSortState = sortKeysInput != null
                ? SortState.getSortStateFromRequest(context, table, sortKeysInput)
                : SortState.getSortStateFromRequest(context, table);

        table.applySorting();
    }

    // --------------------------------------------------------------------- //
    // Pagination ---------------------------------------------------------- //
    // --------------------------------------------------------------------- //
    static void decodePageRequest(FacesContext context, DataTable table) {
        table.savedPageState = new PageState(context, table);
    }

    
    
    // --------------------------------------------------------------------- //
    // Column Reorder ------------------------------------------------------ //
    // --------------------------------------------------------------------- //
    static void decodeColumnReorderRequest(FacesContext context, DataTable table) {
        String clientId = table.getClientId(context);
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();

        List<Integer> ordering = table.getColumnOrdering();
        String[] columnTargets = params.get(clientId + "_columnReorder").split("-");
        Integer columnIndex = ordering.remove(Integer.parseInt(columnTargets[0]));
        ordering.add(Integer.parseInt(columnTargets[1]), columnIndex);
        // this call just to indicate a change has taken place to col order, and recalc
        table.setColumnOrdering(ordering);
		table.setLastReordering(System.currentTimeMillis());
    }



    // --------------------------------------------------------------------- //
    // Table Conf ---------------------------------------------------------- //
    // --------------------------------------------------------------------- //    
    static void decodeTableConfigurationRequest(FacesContext context, DataTable table) {
        TableConfigPanel tableConfigPanel = table.findTableConfigPanel(context);
        tableConfigPanel.setForcedRenderCount(tableConfigPanel.getForcedRenderCount()+1);
        decodeColumnConfigurations(context, table, tableConfigPanel);
    }

    static private void decodeColumnConfigurations(FacesContext context, DataTable table, TableConfigPanel panel) {
        TableConfigPanelDecodeState state = new TableConfigPanelDecodeState(
            context.getExternalContext().getRequestParameterMap(),
            table.getColumnModel(),
            panel.isColumnOrderingConfigurable(),
            panel.isColumnNameConfigurable(),
            panel.isColumnVisibilityConfigurable(),
            panel.isColumnSortingConfigurable(),
            panel.getType());
        DepthFirstHeadTraversal.CallbackAdapter<TableConfigPanelDecodeState,
            RuntimeException> callback = new DepthFirstHeadTraversal.
            CallbackAdapter<TableConfigPanelDecodeState, RuntimeException>()
        {
            public void columnTraversal(TableConfigPanelDecodeState state,
                    DepthFirstHeadTraversal.Quantity level, Column column,
                    Column correspondingColumn, int headerIndex, int bodyIndex,
                    int stackedIndex) throws RuntimeException {
                if (column.isConfigurable()) {
                    if (state.visibilityConfigurable) {
                        decodeColumnVisibility(state.params, column, correspondingColumn);
                    }
                    if (state.namingConfigurable) {
                        decodeColumnName(state.params, column);
                    }
                }
            }
        };
        DepthFirstHeadTraversal<TableConfigPanelDecodeState, RuntimeException>
            t = new DepthFirstHeadTraversal<TableConfigPanelDecodeState,
            RuntimeException>(state.columnModel, callback, state);
        t.traverse();
        String clientId = table.getClientId(context);
        if (state.orderingConfigurable) {
            decodeHeaderColumnOrdering(state.params, table, clientId);
            decodeColumnOrdering(state.params, table, clientId);
        }

        if (state.sortingConfigurable) {
            decodeSortRequest(context, table, clientId,
                    processConfigPanelSortKeys(clientId, state.params,
                        state.columnModel));
        }
    }

    static private void decodeColumnName(Map<String, String> params, Column column) {
        String text = params.get(column.getClientId()+TableConfigPanel.COLUMN_HEAD_SUFFIX);
        column.setHeaderText(text);
    }

    static private void decodeHeaderColumnOrdering(Map<String, String> params, DataTable table, String clientId) {
        ArrayList<Integer> indexes = parseIntegerListFromCommaDelimitedStringInMap(
            params, clientId + TableConfigPanel.DATATABLE_HEADER_COLUMN_ORDER_SUFFIX);
        if (indexes != null) {
            table.setHeaderColumnOrdering(indexes);
        }
    }

    static private void decodeColumnOrdering(Map<String, String> params, DataTable table, String clientId) {
        ArrayList<Integer> indexes = parseIntegerListFromCommaDelimitedStringInMap(
            params, clientId + TableConfigPanel.DATATABLE_COLUMN_ORDER_SUFFIX);
        if (indexes != null) {
            table.setColumnOrdering(indexes);
        }
    }

    static private ArrayList<Integer> parseIntegerListFromCommaDelimitedStringInMap(
            Map<String, String> params, String key) {
        String strInput = params.get(key);
        if (strInput != null && strInput.length() > 0) {
            String[] indexes = strInput.split(",");
            ArrayList<Integer> ints = new ArrayList<Integer>(Math.max(1, indexes.length));
            for (String index : indexes) {
                ints.add(Integer.parseInt(index));
            }
            return ints;
        }
        return null;
    }

    static private void decodeColumnVisibility(Map<String, String> params, Column column, Column correspondingColumn) {
        String code = params.get(column.getClientId()+TableConfigPanel.COLUMN_VISIBILITY_SUFFIX);
        boolean rendered = (code != null);
        column.updateRendered(rendered);
        if (correspondingColumn != null) {
            correspondingColumn.updateRendered(rendered);
        }
    }

    // Util ---------------------------------------------------------------- //
    static private String processConfigPanelSortKeys(String clientId,
            Map<String, String> params, ColumnModel columnModel) {
        StringBuilder newSortKeys = new StringBuilder();
        String[] sortKeys = params.get(clientId + "_sortKeys").split(",");
        for (String key : sortKeys) {
            if (key.length() > 0) {
                if (newSortKeys.length() > 0) {
                    newSortKeys.append(",");
                }
                int unsortedIndex = Integer.parseInt(key);
                Column column = columnModel.getHeaderModel().
                    getColumnWithUnsortedIndex(unsortedIndex);
                newSortKeys.append(column.getClientId());
            }
        }
        return newSortKeys.toString();
    }

    public static void decodeColumnPinning(FacesContext context, DataTable table) throws JSONException {
        String clientId = table.getClientId(context);
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        String pinning = params.get(clientId + "_pinning");
        JSONObject pinningState = new JSONObject(pinning);
        List<Column> columns = table.getColumns();

        Integer i = 0;
        for (Column c : columns) {
            Integer pinOrder = null;

            try {
                pinOrder = pinningState.getInt(i.toString()) + 1;
            } catch (JSONException e) {
                // leave pin order null if order missing from json state,
                // to wipe order from column
            }

            c.setPinningOrder(pinOrder);
            i++;
        }
    }

    public static void decodeTrashConfigurationRequest(FacesContext context, DataTable table) {
        TableConfigPanel panel = table.findTableConfigPanel(context);
        panel.setForcedRenderCount(panel.getForcedRenderCount()+1);
        boolean sorting = panel.isColumnSortingConfigurable();
        boolean visibility = panel.isColumnVisibilityConfigurable();
        boolean ordering = panel.isColumnOrderingConfigurable();

        if (ordering) {
            table.setHeaderColumnOrdering((List)null);
            table.setColumnOrdering((List)null);
        }

        if (sorting || visibility) {
            for (Column c : table.getColumns(true)) {
                if (sorting && c.isPropertySet("sortPriority")) c.setSortPriority(null);
                if (visibility) c.setRendered(true);
            }
            for (Column c : table.getColumns(false)) {
                if (sorting && c.isPropertySet("sortPriority")) c.setSortPriority(null);
                if (visibility) c.setRendered(true);
            }
        }
    }


    private static class TableConfigPanelDecodeState {
        Map<String,String> params;
        ColumnModel columnModel;
        boolean orderingConfigurable;
        boolean namingConfigurable;
        boolean visibilityConfigurable;
        boolean sortingConfigurable;
        boolean firstCol;
        boolean lastCol;

        private TableConfigPanelDecodeState(
            Map<String,String> params, ColumnModel columnModel,
            boolean orderingConfigurable, boolean namingConfigurable,
            boolean visibilityConfigurable, boolean sortingConfigurable,
            String panelType)
        {
            this.params = params;
            this.columnModel = columnModel;
            this.orderingConfigurable = orderingConfigurable;
            this.namingConfigurable = namingConfigurable;
            this.visibilityConfigurable = visibilityConfigurable;
            this.sortingConfigurable = sortingConfigurable;
            this.firstCol = panelType.equals("first-col") ;
            this.lastCol = panelType.equals("last-col");
        }
    }
}
