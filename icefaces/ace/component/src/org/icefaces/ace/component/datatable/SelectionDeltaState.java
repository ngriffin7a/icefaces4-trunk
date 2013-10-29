package org.icefaces.ace.component.datatable;

import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.model.table.RowState;
import org.icefaces.ace.model.table.RowStateMap;
import org.icefaces.ace.model.table.TreeDataModel;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * User: Nils
 * Date: 7/8/13
 * Time: 3:06 PM
 */
public class SelectionDeltaState {
    List<Object> rowsToSelect = new ArrayList<Object>();
    List<Object> rowsToDeselect = new ArrayList<Object>();
    List<CellRecord> cellsToSelect = new ArrayList<CellRecord>();
    List<CellRecord> cellsToDeselect = new ArrayList<CellRecord>();
    boolean clearSelection = false;

    public SelectionDeltaState(FacesContext context, DataTable table) {
        String clientId = table.getClientId(context);
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        String selection = params.get(clientId + "_selection");
        String deselection = params.get(clientId + "_deselection");
        boolean hasChange = (selection != null && selection.length() > 0) || (deselection != null && deselection.length() > 0);

        if (hasChange)
            if (table.isSingleSelectionMode())
                decodeSingleSelection(table, selection);
            else
                decodeMultipleSelection(table, selection, deselection);
    }

    void decodeSingleSelection(DataTable table, String selection) {
        clearSelection = true;

        if (selection != null && selection.length() > 0) {
            // Handle cell selection
            if (table.isCellSelection()) {
                cellsToSelect.add(getCellRecord(table, selection));
            }
            // Handle row selection
            else {
                TreeDataModel treeModel = null;
                Object model = (Object) table.getDataModel();

                // Tree case init
                if (table.hasTreeDataModel())  {
                    treeModel = (TreeDataModel) model;

                    if (treeModel != null & selection.indexOf('.') > 0) {
                        int lastSepIndex = selection.lastIndexOf('.');
                        treeModel.setRootIndex(selection.substring(0, lastSepIndex));
                        selection = selection.substring(lastSepIndex+1);
                    }
                }

                int selectedRowIndex = Integer.parseInt(selection);
                table.setRowIndex(selectedRowIndex);
                Object rowData = table.getRowData();
                rowsToSelect.add(rowData);

                // Cleanup
                if (treeModel != null) treeModel.setRootIndex(null);
                table.setRowIndex(-1);
            }
        }
    }

    void decodeMultipleSelection(DataTable table, String selection, String deselection) {
        Object value = table.getDataModel();
        TreeDataModel model = null;
        if (table.hasTreeDataModel()) model = (TreeDataModel) value;

        // Process selections
        if (selection != null && selection.length() > 0) {
            if (table.isCellSelection()) {
                for (String s : selection.split(",")) {
                    cellsToSelect.add(getCellRecord(table, s));
                }
            } else {
                String[] rowSelectValues = selection.split(",");

                for (String s : rowSelectValues) {
                    // Handle tree case indexes
                    if (s.indexOf(".") != -1 && model != null) {
                        int lastSepIndex = s.lastIndexOf('.');
                        model.setRootIndex(s.substring(0, lastSepIndex));
                        s = s.substring(lastSepIndex+1);
                    }

                    table.setRowIndex(Integer.parseInt(s));

                    rowsToSelect.add(table.getRowData());

                    if (model != null) model.setRootIndex(null);
                }
            }
        }

        // Process deselections
        if (deselection != null && deselection.length() > 0) {
            if (table.isCellSelection()) {
                for (String s : deselection.split(",")) {
                    cellsToDeselect.add(getCellRecord(table, s));
                }
            } else {
                String[] rowDeselectValues = deselection.split(",");

                for (String s : rowDeselectValues) {
                    // Handle tree case indexes
                    if (s.indexOf(".") != -1 && model != null) {
                        int lastSepIndex = s.lastIndexOf('.');
                        model.setRootIndex(s.substring(0, lastSepIndex));
                        s = s.substring(lastSepIndex+1);
                    }

                    table.setRowIndex(Integer.parseInt(s));

                    rowsToDeselect.add(table.getRowData());

                    if (model != null) model.setRootIndex(null);
                }
            }
        }

        table.setRowIndex(-1);
    }

    private CellRecord getCellRecord(DataTable table, String s) {
        String parts[] = s.split("#");

        int rowId = Integer.parseInt(parts[0]);
        int colIndex = Integer.parseInt(parts[1]);

        table.setRowIndex(rowId);
        Object rowData = table.getRowData();
        Column column = table.getColumns().get(colIndex);
        table.setRowIndex(-1);

        return new CellRecord(rowData, column);
    }

    public void apply(DataTable table) {
        RowStateMap map = table.getStateMap();

        if (clearSelection) {
            map.setAllSelected(false);

            for (Object o : map.values())
                ((RowState)o).setSelectedColumnIds(null);
        }

        for (Object o : rowsToSelect) {
            RowState state = map.get(o);
            if (state.isSelectable())
                state.setSelected(true);
        }

        for (Object o : rowsToDeselect) {
            RowState state = map.get(o);
            if (state.isSelectable())
                state.setSelected(false);
        }

        for (CellRecord o : cellsToSelect) {
            RowState state = map.get(o.rowData);
            if (state.isSelectable())
                state.addSelectedColumn(o.column);
        }

        for (CellRecord o : cellsToDeselect) {
            RowState state = map.get(o.rowData);
            if (state.isSelectable())
                state.removeSelectedColumn(o.column);
        }
    }

    private class CellRecord {
        Object rowData;
        Column column;

        private CellRecord(Object rowData, Column column) {
            this.column = column;
            this.rowData = rowData;
        }
    }
}
