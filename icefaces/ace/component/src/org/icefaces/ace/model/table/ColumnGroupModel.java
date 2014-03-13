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

package org.icefaces.ace.model.table;

import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.component.columngroup.ColumnGroup;
import org.icefaces.ace.component.row.Row;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ColumnGroupModel {
    private static Logger log = Logger.getLogger(ColumnModel.class.getName());

    private ColumnGroup columnGroup;
    private ArrayList<RowAndCells> expandedModel;

    public ColumnGroupModel() {
        this(null);
    }

    public ColumnGroupModel(ColumnGroup columnGroup) {
        this.columnGroup = columnGroup;
        expandedModel = new ArrayList<RowAndCells>();
    }

    public ConstructionState construct(List<Integer> columnOrdering) {
        return new ConstructionState(columnOrdering);
    }

    public TreeIterator iterate() {
        return new TreeIterator();
    }

    public int getRows() {
        return expandedModel.size();
    }
    public int getColumns() {
        return (expandedModel.size() > 0) ? expandedModel.get(0).cells.size() : 0;
    }

    public ColumnGroup getColumnGroup() {
        return columnGroup;
    }
    
    public Row getRow(int rowIndex) {
        return expandedModel.get(rowIndex).row;
    }

    public Column getColumnWithUnsortedIndex(int unsortedIndex) {
        for (RowAndCells rac : expandedModel) {
            for (Cell cell : rac.cells) {
                if (cell.indexAll <= unsortedIndex &&
                    unsortedIndex < (cell.indexAll + cell.columns.size())) {
                    return cell.columns.get(unsortedIndex - cell.indexAll);
                }
            }
        }
        return null;
    }

    protected Cell getCell(int rowIndex, int columnIndex) {
        return expandedModel.get(rowIndex).cells.get(columnIndex);
    }


    protected void addRow(Row row) {
        int numRows = expandedModel.size();
        if (numRows > 0)
            expandedModel.add(new RowAndCells(row, expandedModel.get(numRows-1)));
        else
            expandedModel.add(new RowAndCells(row));
    }

    protected void addColumn(ConstructionState state, Column column,
            int indexRow, int indexAll) {
        if (column.isStacked()) {
            addStackedColumn(state, column);
        } else {
            addUnstackedColumn(state, column, indexRow, indexAll);
        }
    }

    protected void addStackedColumn(ConstructionState state, Column column) {
        // Not looping to set stacked column in colspan Cell(s), since we assume stacked have colspan=1
        getLastRowCells().get(state.prevPosition).columns.add(column);
    }

    protected void addUnstackedColumn(ConstructionState state, Column column,
            int indexRow, int indexAll) {
        ArrayList<Cell> cells = getLastRowCells();
        while (state.position < cells.size() && cells.get(state.position) != null) {
            state.position++;
        }
        int colspan = column.getColspan();
        int rowspan = column.getRowspan();
        state.minRowspanDown = Math.min(rowspan-1, state.minRowspanDown);
        // Ensure we have colspan more spaces in columns, then set c in them
        for (int i = 0; i < colspan; i++) {
            Cell cell = new Cell(column, i, colspan-i-1, 0, rowspan-1,
                indexRow, indexAll);
            if ((state.position+i) >= cells.size()) {
                cells.add(cell);
            } else {
                cells.set(state.position+i, cell);
            }
        }
        state.prevPosition = state.position;
        state.position += colspan;
    }

    protected void endRow(ConstructionState state) {
        if (state.minRowspanDown != Integer.MAX_VALUE) {
            for (Cell cell : getLastRowCells()) {
                if (cell != null) {
                    cell.rowspanDown -= state.minRowspanDown;
                }
            }
        }
        
        java.util.ListIterator<Cell> it = getLastRowCells().listIterator();
        while (it.hasNext()) {
            if (it.next() == null) {
                it.set(Cell.EMPTY);
            }
        }

        if (log.isLoggable(Level.FINE)) {
            StringBuilder sb = new StringBuilder();
            for (Cell cell : getLastRowCells()) {
                if (cell != null)
                    sb.append(cell.debugString());
                else
                    sb.append("null");
                sb.append(", ");
            }
            log.fine("Row of columns:\n" + sb.toString());
        }
    }

    protected ArrayList<Cell> getLastRowCells() {
        return expandedModel.get(expandedModel.size()-1).cells;
    }

    /**
     * In case we added columns as we went, and previous rows have less
     * columns, pad them out with empty ones.
     */
    protected void endConstruction() {
        int maxCols = 0;
        for (int r = expandedModel.size()-1; r >= 0; r--) {
            int numCols = expandedModel.get(r).cells.size();
            maxCols = Math.max(maxCols, numCols);
            for (int c = numCols; c < maxCols; c++) {
                expandedModel.get(r).cells.add(Cell.EMPTY);
            }
        }
    }


    public class ConstructionState {
        protected int minRowspanDown;
        protected int position;
        protected int prevPosition;

        protected List<Integer> columnOrdering;
        protected int columnOrderingIndex;
        protected int columnsFromPreviousRows;
        protected ArrayList<Column> unsortedColumnsForRow;

        protected ConstructionState(List<Integer> columnOrdering) {
            this.columnOrdering = columnOrdering;
            this.columnOrderingIndex = 0;
            this.columnsFromPreviousRows = 0;
        }

        public void addRow(Row row) {
            minRowspanDown = Integer.MAX_VALUE;
            position = 0;
            prevPosition = position;

            unsortedColumnsForRow = new ArrayList<Column>();

            ColumnGroupModel.this.addRow(row);
        }

        public void addUnsortedColumnForRow(Column column) {
            unsortedColumnsForRow.add(column);
        }

        public void endRow() {
            // We're not iterating over the columns themselves, but through their count
            int numColumnsThisRow = unsortedColumnsForRow.size();
            for (int i = 0; i < numColumnsThisRow; i++) {
                int columnIndex =
                    (columnOrdering != null && columnOrdering.size() > columnOrderingIndex) ?
                        columnOrdering.get(columnOrderingIndex) : columnOrderingIndex;
                int columnIndexWithinRow = columnIndex - columnsFromPreviousRows;
                if (log.isLoggable(Level.FINER)) {
                    log.finer("columnOrderingIndex: " + columnOrderingIndex +
                        "  columnIndex: " + columnIndex +
                        "  columnIndexWithinRow: " + columnIndexWithinRow + "  columnOrdering: " + columnOrdering);
                    log.finer("    headerText: " + unsortedColumnsForRow.
                        get(columnIndexWithinRow).getHeaderText());
                }
                ColumnGroupModel.this.addColumn(this, unsortedColumnsForRow.
                    get(columnIndexWithinRow), columnIndexWithinRow, columnIndex);
                columnOrderingIndex++;
            }
            columnsFromPreviousRows += numColumnsThisRow;

            ColumnGroupModel.this.endRow(this);
        }

        public void end() {
            ColumnGroupModel.this.endConstruction();
        }
    }

    private static class RowAndCells {
        protected Row row;
        protected ArrayList<Cell> cells;

        RowAndCells(Row row) {
            this.row = row;
            cells = new ArrayList<Cell>();
        }

        RowAndCells(Row row, RowAndCells clone) {
            this.row = row;
            int sz = clone.cells.size();
            cells = new ArrayList<Cell>(Math.max(1,sz));
            for (int i = 0; i < sz; i++) {
                Cell toDuplicate = clone.cells.get(i);
                cells.add(toDuplicate == null ? null : toDuplicate.duplicateDownward());
            }
        }
    }

    private static class Cell {
        protected static final Cell EMPTY = new Cell();
        
        protected List<Column> columns;
        protected int colspanLeft;
        protected int colspanRight;
        protected int rowspanUp;
        protected int rowspanDown;
        protected int indexRow;
        protected int indexAll;

        protected Cell(List<Column> columns, int colspanLeft, int colspanRight,
                int rowspanUp, int rowspanDown, int indexRow, int indexAll) {
            this.columns = new ArrayList<Column>(
                Math.max(1,columns.size()));
            this.columns.addAll(columns);
            this.colspanLeft = colspanLeft;
            this.colspanRight = colspanRight;
            this.rowspanUp = rowspanUp;
            this.rowspanDown = rowspanDown;
            this.indexRow = indexRow;
            this.indexAll = indexAll;
        }

        protected Cell(Column column, int colspanLeft, int colspanRight,
                int rowspanUp, int rowspanDown, int indexRow, int indexAll) {
            this.columns = new ArrayList<Column>(2);
            this.columns.add(column);
            this.colspanLeft = colspanLeft;
            this.colspanRight = colspanRight;
            this.rowspanUp = rowspanUp;
            this.rowspanDown = rowspanDown;
            this.indexRow = indexRow;
            this.indexAll = indexAll;
        }

        protected Cell() {
            columns = Collections.emptyList();
            colspanLeft = 0;
            colspanRight = 0;
            rowspanUp = 0;
            rowspanDown = 0;
            indexRow = -1;
            indexAll = -1;
        }

        protected boolean haveColumns() {
            return columns != null && columns.size() > 0;
        }

        /**
         * Used to determine if an adjacent cell is a colspan extension of the
         * exact same column(s). Plural columns if stacked.
         */
        protected boolean sameColumns(Cell other) {
            boolean have = haveColumns();
            boolean otherHave = (other != null) && other.haveColumns();
            if (have != otherHave) {
                return false;
            }
            if (!have) {
                return true;
            }
            return columns.equals(other.columns);
        }

        protected Cell duplicateDownward() {
            if (rowspanDown > 0) {
                return new Cell(columns, colspanLeft, colspanRight,
                    rowspanUp+1, rowspanDown-1, indexRow, indexAll);
            }
            return null;
        }

        /**
         * Used to determine if one Cell from one ColumnGroupModel is
         * sufficiently similar to another Cell from another ColumnGroupModel,
         * that if one was a header Cell and the other was a body Cell, that
         * they could correspond to each other, based on Cell widths and
         * column counts.
         * @param cell Another Cell from another ColumnGroupModel
         * @return If the have equal widths and column counts
         */
        protected boolean potentiallyCorrespondingCell(Cell cell) {
            return (cell != null &&
                this.colspanLeft == cell.colspanLeft &&
                this.colspanRight == cell.colspanRight &&
                this.columns.size() == cell.columns.size());
        }

        protected String debugString() {
            StringBuilder sb = new StringBuilder();
            sb.append("<"+colspanLeft + ":" + colspanRight+">^" + rowspanUp+":"+rowspanDown+"v");
            if (columns.size() > 1)
                sb.append("[");
            for (Column c : columns) {
                sb.append(c.getHeaderText()+", ");
            }
            if (columns.size() > 1)
                sb.append("]");
            return sb.toString();
        }
    }

    public class TreeIterator {
        private ArrayList<Integer> positions;

        private TreeIterator() {
            positions = new ArrayList<Integer>(ColumnGroupModel.this.getRows());
            positions.add(0);
        }

        public ColumnGroupModel getColumnGroupModel() {
            return ColumnGroupModel.this;
        }

        public boolean empty() {
            return ColumnGroupModel.this.getRows() == 0 || ColumnGroupModel.this.getColumns() == 0;
        }

        public ColumnGroup columnGroup() {
            return ColumnGroupModel.this.getColumnGroup();
        }
        
        public Row row() {
            int row = positions.size()-1;
            return ColumnGroupModel.this.getRow(row);
        }

        public List<Column> columns() {
            return cell().columns;
        }

        protected Cell cell() {
            int row = positions.size()-1;
            int col = positions.get(row);
            return ColumnGroupModel.this.getCell(row, col);
        }

        public boolean nextPeer(boolean sameParent, boolean moveToIt) {
            int row = positions.size()-1;
            int col = positions.get(row);
            Cell cell = ColumnGroupModel.this.getCell(row, col);
            int columns = ColumnGroupModel.this.getColumns();
            // Skip past columns not originating from this row
            Cell nextCell;
            int nextCol = col;
            do {
                nextCol = nextCol + cell.colspanRight + 1;
                if (nextCol >= columns) {
                    return false;
                }
                nextCell = ColumnGroupModel.this.getCell(row, nextCol);
            } while (nextCell.rowspanUp != 0);
            if (!nextCell.haveColumns()) {
                return false;
            }
            if (sameParent && row > 0 && getTopLeftCellOfParent(row,col) !=
                getTopLeftCellOfParent(row,nextCol)) {
                return false;
            }
            if (moveToIt) {
                positions.set(row, nextCol);
            }
            return true;
        }

        private Cell getTopLeftCellOfParent(int row, int col) {
            // Start with the current cell, then go to its parent
            Cell cell = ColumnGroupModel.this.getCell(row, col);
            int newRow = row - cell.rowspanUp - 1;
            if (newRow < 0) {
                return null;
            }
            cell = ColumnGroupModel.this.getCell(newRow, col);
            // Then go to the top left cell of the parent column
            newRow -= cell.rowspanUp;
            int newCol = col - cell.colspanLeft;
            if (newRow < 0 || newCol < 0) {
                return null;
            }
            cell = ColumnGroupModel.this.getCell(newRow, newCol);
            return cell;
        }

        public boolean nextRow(boolean firstStartingColumn, boolean moveToIt) {
            int origCol = positions.get(positions.size()-1);
            int row = positions.size();
            int col = 0;
            while (row < ColumnGroupModel.this.getRows()) {
                // Scan across this row to find a column that starts in this row and isn't from a previous rowspan
                if (col >= ColumnGroupModel.this.getColumns()) {
                    row++;
                    col = 0;
                    continue;
                }
                Cell cell = ColumnGroupModel.this.getCell(row, col);
                if (firstStartingColumn && cell.rowspanUp > 0) {
                    col++;
                    continue;
                }
                if (moveToIt) {
                    while (positions.size() < row) {
                        positions.add(origCol);
                    }
                    if (positions.size() == row) {
                        positions.add(col);
                    }
                }
                return true;
            }
            return false;
        }

        public boolean nextRendered(boolean moveToIt) {
            int origRow = positions.size()-1;
            int origCol = positions.get(origRow);
            int col = origCol + ColumnGroupModel.this.getCell(origRow, origCol).colspanRight + 1;
            for (int row = origRow; row < ColumnGroupModel.this.getRows(); row++) {
                while (col < ColumnGroupModel.this.getColumns()) {
                    Cell cell = ColumnGroupModel.this.getCell(row, col);
                    if (cell != null && cell.rowspanUp == 0) {
                        for (Column column : cell.columns) {
                            if (column.isRendered()) {
                                if (moveToIt) {
                                    while (positions.size() < row) {
                                        positions.add(origCol);
                                    }
                                    if (positions.size() == row) {
                                        positions.add(col);
                                    } else {
                                        positions.set(row, col);
                                    }
                                }
                                return true;
                            }
                        }
                    }
                    col = col + (cell == null ? 0 : cell.colspanRight) + 1;
                }
                col = 0;
            }
            return false;
        }

        public boolean firstChild(boolean moveToIt) {
            int row = positions.size()-1;
            int col = positions.get(row);
            Cell cell = ColumnGroupModel.this.getCell(row, col);
            int newCol = col - cell.colspanLeft;
            int newRow = row + cell.rowspanDown + 1;
            if (newCol < 0 || newRow >= ColumnGroupModel.this.getRows()) {
                return false;
            }
            Cell newCell = ColumnGroupModel.this.getCell(newRow, newCol);
            if (newCell == null) {
                return false;
            }
			// make sure we haven't already visited this child, otherwise move to next to the right
			if (newCell.colspanLeft > 0) {
				newCol = newCol + newCell.colspanRight + 1;
				if (ColumnGroupModel.this.getCell(newRow, newCol) == null) {
					return false;
				}
				Cell parentCell = getTopLeftCellOfParent(newRow, newCol);
				if (parentCell != cell) {
					return false;
				}
			}
            if (moveToIt) {
                for (int i = row + 1; i <= newRow; i++) {
                    positions.add(newCol);
                }
            }
            return true;
        }

        public boolean parent(boolean moveToIt) {
            int row = positions.size()-1;
            int col = positions.get(row);
            Cell cell = ColumnGroupModel.this.getCell(row, col);
            int newRow = row - cell.rowspanUp - 1;
            if (newRow < 0) {
                return false;
            }
            Cell newCell = ColumnGroupModel.this.getCell(newRow, col);
            newRow -= newCell.rowspanUp;
            int newCol = col - newCell.colspanLeft;
            if (newRow < 0 || newCol < 0) {
                return false;
            }
            if (moveToIt) {
                for (int i = row; i > newRow; i--) {
                    positions.remove(i);
                }
                positions.set(newRow, newCol);
            }
            return true;
        }

        /**
         * Invoked on the Header ColumnGroupModel's TreeIterator
         * @param bodyIterator Body ColumnGroupModel's TreeIterator, whose current location is used
         * @param moveTo If the corresponding cell is found, reposition to it
         * @return If the corresponding cell was found
         */
        boolean correspondingColumnInHeaderForBody(
                TreeIterator bodyIterator, boolean moveTo) {
            // Validate that each TreeIterator actually has rows, and there's
            // a body Cell to work with
            if (ColumnGroupModel.this.getRows() == 0 ||
                bodyIterator.positions.size() == 0) {
                return false;
            }
            Cell bodyCell = bodyIterator.cell();
            if (bodyCell == null) {
                return false;
            }

            // Start out on the bottom row at the same cell index
            int row = ColumnGroupModel.this.getRows() - 1;
            int col = bodyIterator.positions.get(bodyIterator.positions.size()-1);
            if (col >= ColumnGroupModel.this.getColumns()) {
                return false;
            }

            // Scan up rows to find corresponding cell. Usually already at it
            while (true) {
                if (row < 0) {
                    return false;
                }
                if (bodyCell.potentiallyCorrespondingCell(
                    ColumnGroupModel.this.getCell(row,col))) {
                    break;
                }
                row--;
            }

            if (moveTo) {
                positions.clear();
                positions.addAll(Collections.nCopies(row+1, col));
            }

            return true;
        }

        /**
         * Invoked on the Body ColumnGroupModel's TreeIterator
         * @param headerIterator Header ColumnGroupModel's TreeIterator, whose current location is used
         * @param moveTo If the corresponding cell is found, reposition to it
         * @return If the corresponding cell was found
         */
        boolean correspondingColumnInBodyForHeader(
                TreeIterator headerIterator, boolean moveTo) {
            // Validate that each TreeIterator actually has rows, and there's
            // a header Cell to work with
            if (ColumnGroupModel.this.getRows() == 0 ||
                headerIterator.positions.size() == 0) {
                return false;
            }

            int headerRow = headerIterator.positions.size()-1;
            int col = headerIterator.positions.get(headerRow);
            Cell headerCell = headerIterator.getColumnGroupModel().getCell(
                headerRow, col);
            if (headerCell == null) {
                return false;
            }
            if (col >= ColumnGroupModel.this.getColumns()) {
                return false;
            }
            Cell bodyCell = getCell(0, col);
            if (bodyCell == null) {
                return false;
            }
            if (!bodyCell.potentiallyCorrespondingCell(headerCell)) {
                return false;
            }

            // Don't use this header column if there's a better fitting one on a lower level
            for (int scanHeaderRow = headerIterator.getColumnGroupModel().
                    getRows()-1; scanHeaderRow >= 0 &&
                    scanHeaderRow != headerRow;) {
                Cell scanHeaderCell = headerIterator.getColumnGroupModel().getCell(scanHeaderRow, col);
                if (scanHeaderCell != null) {
                    scanHeaderRow = scanHeaderRow - scanHeaderCell.rowspanUp;
                    if (scanHeaderRow == headerRow) {
                        break;
                    }
                    scanHeaderCell = headerIterator.getColumnGroupModel().getCell(scanHeaderRow, col);
                }
                if (bodyCell.potentiallyCorrespondingCell(scanHeaderCell)) {
                    return false;
                }
                scanHeaderRow = scanHeaderRow - scanHeaderCell.rowspanUp - 1;
            }

            if (moveTo) {
                positions.clear();
                positions.add(col);
            }
            return true;
        }

        int getUnsortedIndexRow() {
            return cell().indexRow;
        }

        public int getUnsortedIndex() {
            return cell().indexAll;
        }

        public int rowIndex() { return positions.size()-1; }
    }
}
