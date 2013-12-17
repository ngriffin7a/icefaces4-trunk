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

package org.icefaces.ace.model.table;

import org.icefaces.ace.component.column.Column;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DepthFirstHeadTraversal<T, E extends Throwable> {
    private static Logger log = Logger.getLogger(ColumnModel.class.getName()+".traverse");

    private ColumnModel columnModel;
    private ColumnGroupModel.TreeIterator headerIterator;
    private Callback<T, E> callback;
    private T state;

    public DepthFirstHeadTraversal(ColumnModel columnModel, Callback<T,E> callback, T state) {
        this.columnModel = columnModel;
        this.headerIterator = columnModel.getHeaderModel().iterate();
        this.callback = callback;
        this.state = state;
    }

    public ColumnModel getColumnModel() {
        return this.columnModel;
    }

    public ColumnGroupModel.TreeIterator getHeaderTreeIterator() {
        return this.headerIterator;
    }

    public void traverse() throws E {
        Quantity level;
        if (headerIterator.empty()) {
            level = Quantity.NONE;
        } else if (headerIterator.nextPeer(false, false)) {
            level = Quantity.SEVERAL;
        } else {
            level = Quantity.SINGLE;
        }
        log.finer(indent()+"traverse  level: " + level);
        if (callback != null) {
            callback.beforeTraversal(state, level);
        }
        if (!Quantity.NONE.equals(level)) {
            do {
                traverseSubtree(level);
            } while (headerIterator.nextPeer(false, true));
        }
        if (callback != null) {
            callback.afterTraversal(state, level);
        }
    }

    private void traverseSubtree(Quantity level) throws E {
        log.finer(indent()+"traverseSubTree");
        if (callback != null) {
            callback.beforeSubtreeTraversal(state, level);
        }

        int headerIndex;
        int bodyIndex;
        List<Column> columns = headerIterator.columns();
        List<Column> correspondingColumns = null;
        if (columnModel.isHeaderDifferentThanBody()) {
            headerIndex = headerIterator.getUnsortedIndex();
            ColumnGroupModel.TreeIterator bodyIterator = columnModel.
                findCorrespondingBodyColums(headerIterator);
            if (bodyIterator == null) {  // Only is header column
                bodyIndex = -1;
            } else {                     // Found corresponding
                bodyIndex = bodyIterator.getUnsortedIndex();
                correspondingColumns = bodyIterator.columns();
            }
        } else {
            headerIndex = headerIterator.getUnsortedIndex();
            bodyIndex = headerIterator.getUnsortedIndex();
        }

        traverseSpan(level, columns, correspondingColumns, headerIndex, bodyIndex);
        traverseChildren(columns, correspondingColumns);
        
        if (callback != null) {
            callback.afterSubtreeTraversal(state, level);
        }
    }

    private void traverseSpan(Quantity level, List<Column> columns,
            List<Column> correspondingColumns, int headerIndex, int bodyIndex)
            throws E {
        log.finer(indent()+" traverseSpan  columns: " + columns.size() +
            "  headerIndex: " + headerIndex + "  bodyIndex: " + bodyIndex);
        if (callback != null) {
            callback.beforeSpanTraversal(state, level, columns,
                correspondingColumns, headerIndex, bodyIndex);
        }
        final int numColumns = columns.size();
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            Column correspondingColumn = (correspondingColumns == null ? null :
                correspondingColumns.get(i));
            int stackedIndex = ((numColumns > 1) ? i : -1);
            if ((numColumns > 1) && callback != null) {
                callback.beforeStackedTraversal(state, level, column,
                    correspondingColumn, headerIndex, bodyIndex, stackedIndex);
            }
            traverseColumn(level, column, correspondingColumn, headerIndex,
                bodyIndex, stackedIndex);
            if ((numColumns > 1) && callback != null) {
                callback.afterStackedTraversal(state, level, column,
                    correspondingColumn, headerIndex, bodyIndex, stackedIndex);
            }
        }
        if (callback != null) {
            callback.afterSpanTraversal(state, level, columns,
                correspondingColumns, headerIndex, bodyIndex);
        }
    }

    private void traverseColumn(Quantity level, Column column,
            Column correspondingColumn, int headerIndex, int bodyIndex,
            int stackedIndex) throws E {
        if (log.isLoggable(Level.FINER)) {
            log.finer(indent()+"  traverseColumn  " + column.getHeaderText());
        }
        if (callback != null) {
            callback.columnTraversal(state, level, column, correspondingColumn, headerIndex, bodyIndex, stackedIndex);
        }
    }

    private void traverseChildren(List<Column> columns,
            List<Column> correspondingColumns) throws E {
        Quantity level;
        if (!headerIterator.firstChild(true)) {
            level = Quantity.NONE;
        } else if (headerIterator.nextPeer(true, false)) {
            level = Quantity.SEVERAL;
        } else {
            level = Quantity.SINGLE;
        }
        log.finer(indent(level.any()?1:0)+"traverseChildren  level: " + level);
        if (callback != null) {
            callback.beforeChildrenTraversal(state, level, columns,
                correspondingColumns);
        }
        if (!Quantity.NONE.equals(level)) {
            do {
                traverseSubtree(level);
            } while (headerIterator.nextPeer(true, true));
            headerIterator.parent(true);
        }
        if (callback != null) {
            callback.afterChildrenTraversal(state, level, columns,
                correspondingColumns);
        }
    }

    private String indent() {
        return indent(0);
    }

    private String indent(int sub) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < headerIterator.rowIndex()-sub; i++) sb.append("    ");
        return sb.toString();
    }
    

    public interface Callback<T, E extends Throwable> {
        public void beforeTraversal(T state, Quantity level) throws E;
        public void afterTraversal(T state, Quantity level) throws E;

        public void beforeSubtreeTraversal(T state, Quantity level) throws E;
        public void afterSubtreeTraversal(T state, Quantity level) throws E;

        public void beforeSpanTraversal(T state, Quantity level,
            List<Column> columns, List<Column> correspondingColumns,
            int headerIndex, int bodyIndex) throws E;
        public void afterSpanTraversal(T state, Quantity level,
            List<Column> columns, List<Column> correspondingColumns,
            int headerIndex, int bodyIndex) throws E;

        public void beforeStackedTraversal(T state, Quantity level,
            Column column, Column correspondingColumn, int headerIndex,
            int bodyIndex, int stackedIndex) throws E;
        public void afterStackedTraversal(T state, Quantity level,
            Column column, Column correspondingColumn, int headerIndex,
            int bodyIndex, int stackedIndex) throws E;

        public void columnTraversal(T state, Quantity level, Column column,
            Column correspondingColumn, int headerIndex, int bodyIndex,
            int stackedIndex) throws E;

        public void beforeChildrenTraversal(T state, Quantity level,
            List<Column> columns, List<Column> correspondingColumns) throws E;
        public void afterChildrenTraversal(T state, Quantity level,
            List<Column> columns, List<Column> correspondingColumns) throws E;
    }
    

    public static class CallbackAdapter<T, E extends Throwable>
            implements Callback <T, E> {
        public void beforeTraversal(T state, Quantity level) throws E {}
        public void afterTraversal(T state, Quantity level) throws E {}

        public void beforeSubtreeTraversal(T state, Quantity level) throws E {}
        public void afterSubtreeTraversal(T state, Quantity level) throws E {}

        public void beforeSpanTraversal(T state, Quantity level,
            List<Column> columns, List<Column> correspondingColumns,
            int headerIndex, int bodyIndex) throws E {}
        public void afterSpanTraversal(T state, Quantity level,
            List<Column> columns, List<Column> correspondingColumns,
            int headerIndex, int bodyIndex) throws E {}

        public void beforeStackedTraversal(T state, Quantity level,
            Column column, Column correspondingColumn, int headerIndex,
            int bodyIndex, int stackedIndex) throws E {}
        public void afterStackedTraversal(T state, Quantity level,
            Column column, Column correspondingColumn, int headerIndex,
            int bodyIndex, int stackedIndex) throws E {}

        public void columnTraversal(T state, Quantity level, Column column,
            Column correspondingColumn, int headerIndex, int bodyIndex,
            int stackedIndex) throws E {}

        public void beforeChildrenTraversal(T state, Quantity level,
            List<Column> columns, List<Column> correspondingColumns) throws E {}
        public void afterChildrenTraversal(T state, Quantity level,
            List<Column> columns, List<Column> correspondingColumns) throws E {}
    }


    public enum Quantity {
        NONE {
            public boolean any() { return false; }
            public boolean several() { return false; }
        },
        SINGLE {
            public boolean any() { return true; }
            public boolean several() { return false; }
        },
        SEVERAL {
            public boolean any() { return true; }
            public boolean several() { return true; }
        };

        public abstract boolean any();
        public abstract boolean several();
    }
}

