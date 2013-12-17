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
import org.icefaces.ace.component.tableconfigpanel.TableConfigPanel;
import org.icefaces.ace.model.table.ColumnModel;
import org.icefaces.ace.model.table.DepthFirstHeadTraversal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * If a header ColumnGroup is used, with multiple row(s), there can be a
 * hierarchy of header columns, where a parent with its colspan can stretch
 * over several child ones below. In that case, if the parent ceases to be
 * rendered, then none of the children or offspring should be rendered.
 * Likewise, if every child is not rendered, then the parent should nt be
 * either. And if several, but not all, children are not rendered, then the
 * parent should be rendered, but have a lesser colspan to account for only
 * rendering over the remaining columns. This makes all of these necessary
 * adjustments.
 */
public class AutoAdjustRenderedColspan {
    public static Map<String, AdjustedRenderedColspan> adjustIfAllowed(
            TableConfigPanel panel, ColumnModel columnModel) {
        if (panel != null && panel.isColumnVisibilityConfigurable()) {
            return adjust(columnModel);
        }
        return null;
    }

    /**
     * @param columnModel
     * @return Map<String headerColumnClientId, AdjustedRenderedColspan>
     */
    public static Map<String, AdjustedRenderedColspan> adjust(ColumnModel columnModel) {
        AdjustingRenderedColspanState state = new AdjustingRenderedColspanState();
        DepthFirstHeadTraversal.CallbackAdapter<AdjustingRenderedColspanState,
            RuntimeException> callback = new DepthFirstHeadTraversal.
            CallbackAdapter<AdjustingRenderedColspanState, RuntimeException>()
        {
            public void afterSpanTraversal(AdjustingRenderedColspanState state,
                    DepthFirstHeadTraversal.Quantity level,
                    List<Column> columns, List<Column> correspondingColumns,
                    int headerIndex, int bodyIndex) throws RuntimeException {
                final boolean parentRendered = state.stack.isEmpty() ? true :
                    state.stack.get(state.stack.size()-1).isRendered();
                List<AdjustedRenderedColspan> list =
                    new ArrayList<AdjustedRenderedColspan>();
                for (Column column : columns) {
                    AdjustedRenderedColspan adj = new AdjustedRenderedColspan(
                        parentRendered && column.isRendered(), column.getColspan());
                    list.add(adj);
                    state.map.put(column.getClientId(), adj);
                }
                state.stack.add(new AdjustedRenderedColspanSummary(list));
            }

            public void afterChildrenTraversal(AdjustingRenderedColspanState state,
                    DepthFirstHeadTraversal.Quantity level, List<Column> columns,
                    List<Column> correspondingColumns) throws RuntimeException {
                AdjustedRenderedColspanSummary curr = state.stack.remove(
                    state.stack.size()-1);
                for (int i = 0; i < columns.size(); i++) {
                    boolean r = curr.getList().get(i).isRendered();
                    columns.get(i).updateRendered(r);
                    if (correspondingColumns != null) {
                        Column corr = correspondingColumns.get(i);
                        if (!corr.isLikelySpecifiedRendered()) {
                            corr.updateRendered(r);
                        }
                    }
                }

                if (!state.stack.isEmpty()) {
                    AdjustedRenderedColspanSummary above = state.stack.get(
                        state.stack.size()-1);
                    above.applyChildAdjustment(curr);
                }
            }
        };
        DepthFirstHeadTraversal<AdjustingRenderedColspanState, RuntimeException>
            t = new DepthFirstHeadTraversal<AdjustingRenderedColspanState,
            RuntimeException>(columnModel, callback, state);
        t.traverse();
        return state.map;
    }


    public static class AdjustedRenderedColspan {
        private boolean rendered;
        private int colspan;
        private int adjustedColspan;

        AdjustedRenderedColspan(boolean rendered, int colspan) {
            this.rendered = rendered && colspan > 0;
            this.colspan = colspan;
            this.adjustedColspan = rendered ? colspan : 0;
        }
        boolean isRendered() { return rendered; }
        int getColspan() { return colspan; }
        int getAdjustedColspan() { return adjustedColspan; }

        // deltaColspan : <= 0 : adjustedColspan - colspan
        void applyChildAdjustment(int deltaColspan) {
            if (rendered) {
                adjustedColspan = Math.max(0, adjustedColspan +
                    deltaColspan);
                if (adjustedColspan == 0) {
                    rendered = false;
                }
            }
        }
    }

    private static class AdjustedRenderedColspanSummary {
        private List<AdjustedRenderedColspan> list;
        private boolean anyRendered;

        AdjustedRenderedColspanSummary(List<AdjustedRenderedColspan> list) {
            this.list = list;
            anyRendered = false;
            for (AdjustedRenderedColspan adj : list) {
                anyRendered |= adj.isRendered();
            }
        }
        public boolean isRendered() { return anyRendered; }
        public List<AdjustedRenderedColspan> getList() { return list; }

        // Negative or zero
        private int getDeltaColspan() {
            int maxColspan = 0;
            int maxAdjustedColspan = 0;
            for (AdjustedRenderedColspan adj : list) {
                maxColspan = Math.max(maxColspan, adj.getColspan());
                maxAdjustedColspan = Math.max(maxAdjustedColspan,
                    adj.getAdjustedColspan());
            }
            return maxAdjustedColspan - maxColspan;
        }

        public void applyChildAdjustment(AdjustedRenderedColspanSummary below) {
            int deltaColspan = below.getDeltaColspan();
            for (AdjustedRenderedColspan adj : list) {
                adj.applyChildAdjustment(deltaColspan);
            }
        }
    }

    private static class AdjustingRenderedColspanState {
        HashMap<String, AdjustedRenderedColspan> map;
        ArrayList<AdjustedRenderedColspanSummary> stack;

        AdjustingRenderedColspanState() {
            map = new HashMap<String, AdjustedRenderedColspan>();
            stack = new ArrayList<AdjustedRenderedColspanSummary>();
        }
    }
}
