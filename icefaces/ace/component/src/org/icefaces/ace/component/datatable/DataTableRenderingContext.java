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
import org.icefaces.ace.model.table.RowStateMap;

import java.util.List;
import java.util.Map;

public class DataTableRenderingContext {
    private DataTable table;

    private RowStateMap stateMap;

    private List<Column> columns;
    private Integer rows;
    private Integer firstRowIndex;
    private Integer pagPose;
    private Integer scrollHeight;
    private String rowStateVar;
    private String selectionMode;
    private String var;
    private String paginatorPosition;
    private String rowIndexVar;
    private boolean paginator;
    private boolean scrollable;
    private boolean staticHeaders;
    private boolean resizableColumns;
    private boolean firstColumn;
    private boolean lastColumn;
    private boolean inHeaderSubrows;
    private boolean reorderableColumns;
    private boolean columnSortable;
    private boolean columnFilterable;
    private boolean columnPinningEnabled;
    private int tabIndex;
    private boolean showPinControl;

    public DataTableRenderingContext(DataTable table) {
        this.table = table;

        paginatorPosition = table.getPaginatorPosition();
        paginator = table.isPaginator();
        scrollable = table.isScrollable();
        staticHeaders = table.isStaticHeaders();
        stateMap = table.getStateMap();
        rowIndexVar = table.getRowIndexVar();
        rows = table.getRows();
        firstRowIndex = table.getFirst();
        pagPose = table.getPage();
        columns = table.getColumns();
        rowStateVar = table.getRowStateVar();
        selectionMode = table.getSelectionMode();
        resizableColumns = table.isResizableColumns();
        scrollHeight = table.getScrollHeight();
        reorderableColumns = table.isReorderableColumns();
        var = table.getVar();
        tabIndex = table.getTabIndex();
        columnPinningEnabled = table.isColumnPinning();
        showPinControl = table.isColumnPinningControlsInHeader();
    }

    public DataTable getTable() {
        return table;
    }

    public String getPaginatorPosition() {
        return paginatorPosition;
    }

    public boolean isPaginator() {
        return paginator;
    }

    public boolean isScrollable() {
        return scrollable;
    }

    public boolean isStaticHeaders() {
        return staticHeaders &&  isScrollable();
    }

    public RowStateMap getStateMap() {
        return stateMap;
    }

    public String getRowIndexVar() {
        return rowIndexVar;
    }

    public Integer getRows() {
        return rows;
    }

    public Integer getFirstRowIndex() {
        return firstRowIndex;
    }

    public Integer getPagPose() {
        return pagPose;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public String getRowStateVar() {
        return rowStateVar;
    }

    public String getSelectionMode() {
        return selectionMode;
    }

    public boolean isResizableColumns() {
        return resizableColumns;
    }

    public Integer getScrollHeight() {
        return scrollHeight;
    }

    public String getVar() {
        return var;
    }

    public void setFirstColumn(boolean firstColumn) {
        this.firstColumn = firstColumn;
    }

    public boolean isFirstColumn() {
        return firstColumn;
    }

    public void setLastColumn(boolean b) {
        this.lastColumn = b;
    }

    public boolean isLastColumn() {
        return lastColumn;
    }

    public void setInHeaderSubrows(boolean headerSubrows) {
        this.inHeaderSubrows = headerSubrows;
    }

    public boolean isInHeaderSubrows() {
        return inHeaderSubrows;
    }

    public boolean isReorderableColumns() {
        return reorderableColumns;
    }

    public void setReorderableColumns(boolean reorderableColumns) {
        this.reorderableColumns = reorderableColumns;
    }

    public void setColumnSortable(boolean columnSortable) {
        this.columnSortable = columnSortable;
    }

    public boolean isColumnSortable() {
        return columnSortable;
    }

    public void setColumnFilterable(boolean columnFilterable) {
        this.columnFilterable = columnFilterable;
    }

    public boolean isColumnFilterable() {
        return columnFilterable;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public boolean isColumnPinningEnabled() {
        return columnPinningEnabled;
    }

    public boolean showPinningControls() {
        return showPinControl;
    }
}
