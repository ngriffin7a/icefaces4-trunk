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

import org.icefaces.ace.component.column.IProxiableColumn;
import org.icefaces.ace.model.table.RowStateMap;

import java.util.List;

public class DataTableRendererUtil {
    protected static boolean isNextColumnRowSpanEqual(IProxiableColumn column, IProxiableColumn nextCol) {
        return (nextCol.getRowspan() == column.getRowspan());
    }

    protected static boolean areBothSingleColumnSpan(IProxiableColumn column, IProxiableColumn nextCol) {
        return (nextCol.getColspan() == 1) && (column.getColspan() == 1);
    }

    protected static boolean isCurrColumnStacked(List comps, IProxiableColumn currCol) {
        // The first column can not be stacked, only subsequent ones can be
        // stacked under it
        int index = comps.indexOf(currCol);
        if (index == 0) {
            return false;
        }
        if (!currCol.isStacked()) {
            return false;
        }
        while (--index >= 0) {
            IProxiableColumn prevCol = (IProxiableColumn) comps.get(index);
            boolean stacked = prevCol.isStacked();
            boolean rendered = prevCol.isRendered();
            if (!stacked && !rendered) {
                return false;
            }
            if (stacked && !rendered) {
                continue;
            }
            break;
        }
        return true;
    }

    protected static boolean isNextStacked(List comps, IProxiableColumn currCol) {
        int index = comps.indexOf(currCol);
        while (++index < comps.size()) {
            IProxiableColumn nextCol = (IProxiableColumn) comps.get(index);
            if (nextCol.isRendered()) {
                return nextCol.isStacked();
            } else if (!nextCol.isStacked()) {
                return false;
            }
        }
        return false;
    }

    protected static IProxiableColumn getNextColumn(IProxiableColumn column, List columnSiblings) {
        int index = columnSiblings.indexOf(column);
        if (index >= 0) {
            if ((index + 1) < columnSiblings.size()) {
                Object next = columnSiblings.get(index + 1);
                if (next instanceof IProxiableColumn) {
                    return (IProxiableColumn) next;
                }
            }
        }
        return null;
    }

    protected static boolean isNextColumnGrouped(IProxiableColumn column) {
        DataTable dataTable = column.findParentTable();
        int currentRow = dataTable.getRowIndex();
        Object currentValue = column.getGroupBy();

        if (currentValue != null) {
            dataTable.setRowIndex(currentRow + 1);
            Object nextValue = column.getGroupBy();

            dataTable.setRowIndex(currentRow);

            return currentValue.equals(nextValue);
        }

        return false;
    }

    protected static int findCurrGroupLength(IProxiableColumn column) {
        DataTable dataTable = column.findParentTable();

        RowStateMap stateMap;
        int result = 0; // isNextColumnGrouped == true is known
        int currentRow = dataTable.getRowIndex();
        int rows = dataTable.getRows();
        Object rowData = dataTable.getRowData();
        boolean keepCounting = true;
        Object currentValue = column.getGroupBy();

        // If this row doesn't break its group by rendering a conditional row after itself or
        // by being expanded, span more than this row
        stateMap = dataTable.getStateMap();
        boolean notExpanded  = !stateMap.get(rowData).isExpanded();
        boolean noTailingRows = dataTable.getConditionalRows(currentRow, false).size() == 0;
        boolean lastExpanded = false;
        if (notExpanded && noTailingRows)
            while (keepCounting) {
                dataTable.setRowIndex(currentRow + result + 1);

                if (!dataTable.isRowAvailable()) break;
                if (rows > 0 && result > rows) break;

                boolean expanded = stateMap.get(dataTable.getRowData()).isExpanded();
                boolean hasConditionalRows = dataTable.getConditionalRows(currentRow + result + 1, true).size() > 0 || dataTable.getConditionalRows(currentRow + result, false).size() > 0;;

                if (currentValue.equals(column.getGroupBy()) && !lastExpanded && !hasConditionalRows) {
                    lastExpanded = expanded;
                    result++;
                }
                else keepCounting = false;
            }

        dataTable.setRowIndex(currentRow);
        column.setCurrGroupLength(result);
        return result;  //To change body of created methods use File | Settings | File Templates.
    }

    protected static boolean isLastGroupDifferent(IProxiableColumn column) {
        DataTable dataTable = column.findParentTable();
        int currentRow = dataTable.getRowIndex();
        Object currentValue = column.getGroupBy();

        if (currentRow == 0) return true;

        if (currentValue != null) {
            dataTable.setRowIndex(currentRow - 1);
            Object lastValue = column.getGroupBy();

            dataTable.setRowIndex(currentRow);

            return !currentValue.equals(lastValue);
        }

        return true;
    }
}
