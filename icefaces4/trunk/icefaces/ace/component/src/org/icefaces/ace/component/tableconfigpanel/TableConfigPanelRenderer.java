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

package org.icefaces.ace.component.tableconfigpanel;

import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.component.datatable.DataTable;
import org.icefaces.ace.component.datatable.DataTableConstants;
import org.icefaces.ace.model.table.ColumnModel;
import org.icefaces.ace.model.table.DepthFirstHeadTraversal;
import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.JSONBuilder;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.lang.String;
import java.lang.System;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class TableConfigPanelRenderer extends CoreRenderer implements 
        DepthFirstHeadTraversal.Callback<TableConfigPanelRenderer.
            TableConfigPanelRenderState, IOException> {
    private static final String SORTABLE_CONTAINER_SUFFIX = "_tcp_children";
    private static final String ORDERING_HEAD_COLUMN_ATTR = "data-tableconf-headcol";
    private static final String ORDERING_COLUMN_ATTR = "data-tableconf-col";
    private static final String ACE_MESSAGES_BUNDLE = "org.icefaces.ace.resources.messages";
    private static final String TABLECONFIG_KEY_PREFIX = "org.icefaces.ace.component.tableconfigpanel.";

    @Override
    public void decode(FacesContext context, UIComponent component) {
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        TableConfigPanel panel = (TableConfigPanel)component;
        String event = params.get("javax.faces.behavior.event");
        String source = params.get("javax.faces.source");
        if (source != null && event != null && source.equals(component.getClientId()) && event.equals("cancel")) {
            panel.setForcedRenderCount(panel.getForcedRenderCount()+1);
        }

        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        TableConfigPanel panel = (TableConfigPanel)component;

        encodePopup(context, panel);
        super.encodeEnd(context, component);
    }
    private void encodePopup(FacesContext context, TableConfigPanel component) throws IOException {
        DataTable table = (DataTable)component.getTargetedDatatable();
        String tableId = table.getClientId(context);
        String clientId = component.getClientId(context);
        String jsId = this.resolveWidgetVar(component);
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement(HTML.DIV_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        ComponentUtils.enableOnElementUpdateNotify(writer, clientId);

        writer.writeAttribute(HTML.CLASS_ATTR, "ui-tableconf ui-widget", null);
        writer.writeAttribute(HTML.STYLE_ATTR, "display:none;", null);

        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, "ui-tableconf-header ui-widget-header ui-corner-tr ui-corner-tl", null);
        ResourceBundle bundle = getComponentResourceBundle(context, ACE_MESSAGES_BUNDLE);
        String columnSettingsTitle = getLocalisedMessageFromBundle(bundle, TABLECONFIG_KEY_PREFIX, "COLUMN_SETTINGS_TITLE", "Column Settings");
        writer.writeText(columnSettingsTitle, null);

        String okTitle = getLocalisedMessageFromBundle(bundle, TABLECONFIG_KEY_PREFIX, "OK_BUTTON_TITLE", "Save Changes");
        String closeTitle = getLocalisedMessageFromBundle(bundle, TABLECONFIG_KEY_PREFIX, "CLOSE_BUTTON_TITLE", "Cancel Changes");
        String trashTitle = getLocalisedMessageFromBundle(bundle, TABLECONFIG_KEY_PREFIX, "RESET_BUTTON_TITLE", "Reset To Original Settings");

        writer.startElement(HTML.SPAN_ELEM, null);
		writer.writeAttribute(HTML.STYLE_ATTR, "float:right;", null);
        writeConfigPanelTrashButton(writer, clientId, trashTitle);
        writeConfigPanelCloseButton(writer, clientId, closeTitle);
        writeConfigPanelOkButton(writer, clientId, okTitle);
        writer.endElement(HTML.SPAN_ELEM);

        writer.endElement(HTML.DIV_ELEM);

        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR,  "ui-tableconf-body ui-widget-content ui-corner-br ui-corner-bl", null);

        TableConfigPanelRenderState state = new TableConfigPanelRenderState(
            writer,
            table.getColumnModel(),
            component.getClientId(),
            component.isColumnOrderingConfigurable(),
            component.isColumnNameConfigurable(),
            component.isColumnVisibilityConfigurable(),
            component.isColumnSortingConfigurable(),
            component.isHideDisabledRows(),
            component.getType());
        writeHeaderRow(state, context);
        writeColumnConfigRows(state);

        writer.endElement(HTML.DIV_ELEM);

        boolean isSingleSort = table.isSingleSort();
        writeJavascript(state, clientId, tableId, component, isSingleSort);

        writer.startElement(HTML.INPUT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "hidden", null);
        writer.writeAttribute(HTML.NAME_ATTR, "absRend", null);
        writer.writeAttribute(HTML.VALUE_ATTR, component.getForcedRenderCount(), null);
        writer.writeAttribute(HTML.AUTOCOMPLETE_ATTR, "off", null);
        writer.endElement(HTML.INPUT_ELEM);
        writer.endElement(HTML.DIV_ELEM);

        if (component.isModal()) {
            writer.startElement(HTML.DIV_ELEM, null);
            writer.writeAttribute(HTML.CLASS_ATTR,  "ui-tableconf-modal", null);
            writer.endElement(HTML.DIV_ELEM);
        }
    }

    public void beforeTraversal(TableConfigPanelRenderState state,
            DepthFirstHeadTraversal.Quantity level) throws IOException {
        //System.out.println("vvv traversal");
        state.writer.startElement(HTML.DIV_ELEM, null);
        if (level.any()) {
            String styleClass = deriveStyleClassFromClientId(
                state.tableConfigPanelClientId) + SORTABLE_CONTAINER_SUFFIX;
            state.writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
            state.sortableContainerIds.add(styleClass);
        }
    }

    public void afterTraversal(TableConfigPanelRenderState state,
            DepthFirstHeadTraversal.Quantity level) throws IOException {
        //System.out.println("^^^ traversal");
        state.writer.endElement(HTML.DIV_ELEM);
    }

    public void beforeSubtreeTraversal(TableConfigPanelRenderState state,
            DepthFirstHeadTraversal.Quantity level) throws IOException {
        //System.out.println("vvv subtree");
        state.writer.startElement(HTML.DIV_ELEM, null);
        state.writer.writeAttribute(HTML.CLASS_ATTR, "ui-tableconf-subtree", null);
    }

    public void afterSubtreeTraversal(TableConfigPanelRenderState state,
            DepthFirstHeadTraversal.Quantity level) throws IOException {
        //System.out.println("^^^ subtree");
        state.writer.endElement(HTML.DIV_ELEM);
    }

    public void beforeSpanTraversal(TableConfigPanelRenderState state,
            DepthFirstHeadTraversal.Quantity level, List<Column> columns,
            List<Column> correspondingColumns, int headerIndex, int bodyIndex)
            throws IOException {
        //System.out.println("vvv span");
        if (columns.size() == 1) {        // Unstacked
            boolean columnConfigurable = columns.get(0).isConfigurable();
            state.spanConfigurable = columnConfigurable;
            StringBuilder styleClass = new StringBuilder("ui-tableconf-item ui-state-default");
            if (!columnConfigurable) {
                styleClass.append(" ui-disabled ui-opacity-40");
            }
            state.writer.startElement(HTML.DIV_ELEM, null);
            state.writer.writeAttribute(HTML.CLASS_ATTR, styleClass.toString(), null);
            if (!columnConfigurable && state.hideDisabledRows) {
                state.writer.writeAttribute(HTML.STYLE_ATTR, "display:none;", null);
            }
            if (headerIndex >= 0) {
                state.writer.writeAttribute(ORDERING_HEAD_COLUMN_ATTR,
                    Integer.toString(headerIndex), null);
            }
            if (bodyIndex >= 0) {
                state.writer.writeAttribute(ORDERING_COLUMN_ATTR,
                    Integer.toString(bodyIndex), null);
            }
        }  else if (columns.size() > 1) {  // Stacked
            final int numStacked = columns.size();
            int numConfigurable = 0;
            for (Column column : columns) {
                if (column.isConfigurable()) numConfigurable++;
            }
            StringBuilder styleClass = new StringBuilder("ui-tableconf-item ui-state-default");
            state.spanConfigurable = numConfigurable == numStacked;
            if (numConfigurable < numStacked) {
                styleClass.append(" ui-disabled"); // ui-opacity-40
            }
            state.writer.startElement(HTML.DIV_ELEM, null);
            state.writer.writeAttribute(HTML.CLASS_ATTR, styleClass.toString(), null);
            if (numConfigurable == 0 && state.hideDisabledRows) {
                state.writer.writeAttribute(HTML.STYLE_ATTR, "display:none;", null);
            }
            state.firstStackedShown = true;
        }
    }

    public void afterSpanTraversal(TableConfigPanelRenderState state,
            DepthFirstHeadTraversal.Quantity level, List<Column> columns,
            List<Column> correspondingColumns, int headerIndex, int bodyIndex)
            throws IOException {
        //System.out.println("^^^ span");
        state.writer.endElement(HTML.DIV_ELEM);
    }

    public void beforeStackedTraversal(TableConfigPanelRenderState state,
            DepthFirstHeadTraversal.Quantity level, Column column,
            Column correspondingColumn, int headerIndex, int bodyIndex,
            int stackedIndex) throws IOException {
        //System.out.println("vvv stacked");
        boolean columnConfigurable = column.isConfigurable();
        boolean shown = columnConfigurable || !state.hideDisabledRows;
        boolean unstackedOrFirstStacked = shown && state.firstStackedShown;
        if (shown && state.firstStackedShown) {
            state.firstStackedShown = false;
        }
        StringBuilder styleClass = new StringBuilder();
        if (!columnConfigurable) {
            styleClass.append("ui-disabled ui-opacity-40 ");
        }
        if (unstackedOrFirstStacked) {
            styleClass.append("stacked-first");
        } else {
            styleClass.append("stacked-subsequent");
        }
        state.writer.startElement(HTML.DIV_ELEM, null);
        state.writer.writeAttribute(HTML.CLASS_ATTR,
            styleClass.toString(), null);
        if (!shown) {
            state.writer.writeAttribute(HTML.STYLE_ATTR, "display:none;", null);
        }
        if (headerIndex >= 0) {
            state.writer.writeAttribute(ORDERING_HEAD_COLUMN_ATTR,
                Integer.toString(headerIndex+stackedIndex), null);
        }
        if (bodyIndex >= 0) {
            state.writer.writeAttribute(ORDERING_COLUMN_ATTR,
                Integer.toString(bodyIndex+stackedIndex), null);
        }
    }

    public void afterStackedTraversal(TableConfigPanelRenderState state,
            DepthFirstHeadTraversal.Quantity level, Column column,
            Column correspondingColumn, int headerIndex, int bodyIndex,
            int stackedIndex) throws IOException {
        //System.out.println("^^^ stacked");
        state.writer.endElement(HTML.DIV_ELEM);
    }

    public void columnTraversal(TableConfigPanelRenderState state,
            DepthFirstHeadTraversal.Quantity level, Column column,
            Column correspondingColumn, int headerIndex, int bodyIndex,
            int stackedIndex) throws IOException {
        //System.out.println("=== column");
        String columnClientId = column.getClientId();
        boolean columnConfigurable = column.isConfigurable();
        boolean isCurrRendered = column.isRendered();

        if (state.orderingConfigurable) {
            boolean unstackedOrFirstStacked = (stackedIndex <= 0);
            writeColumnOrderingControl(state, column, level.several(),
                unstackedOrFirstStacked);
        }
        writeColumnNameControl(state.writer, column, columnClientId,
            columnConfigurable, state.namingConfigurable);
        if (state.visibilityConfigurable) {
            writeColumnVisibilityControl(state.writer, column, columnClientId,
                columnConfigurable, isCurrRendered);
        }
        if (state.sortingConfigurable) {
            boolean isBodyColumn = correspondingColumn != null ||
                !state.columnModel.isHeaderDifferentThanBody();
            writeSortControl(state, column, isBodyColumn);
        }
    }

    public void beforeChildrenTraversal(TableConfigPanelRenderState state,
            DepthFirstHeadTraversal.Quantity level, List<Column> columns,
            List<Column> correspondingColumns) throws IOException {
      //System.out.println("vvv children");
        if (level.any()) {
            state.writer.startElement(HTML.DIV_ELEM, null);
            if (level.several()) {
                String styleClass = deriveStyleClassFromClientId(
                    columns.get(0).getClientId()) + SORTABLE_CONTAINER_SUFFIX;
                state.writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
                state.sortableContainerIds.add(styleClass);
            }
        }
    }

    public void afterChildrenTraversal(TableConfigPanelRenderState state,
            DepthFirstHeadTraversal.Quantity level, List<Column> columns,
            List<Column> correspondingColumns) throws IOException {
        //System.out.println("^^^ children");
        if (level.any()) {
            state.writer.endElement(HTML.DIV_ELEM);
        }
    }

    private void writeColumnConfigRows(TableConfigPanelRenderState state)
            throws IOException {
        DepthFirstHeadTraversal<TableConfigPanelRenderState, IOException> t =
            new DepthFirstHeadTraversal<TableConfigPanelRenderState, IOException>
            (state.columnModel, this, state);
        state.t = t;
        t.traverse();
    }

    /*
    <div class="ui-tableconf-subtree">
        <div class="ui-tableconf-item ui-state-default">
            if not stacking:
                writeColumnInfo

            else:
                for Column : stack

                    if first Column:
            <div class="stacked-first">
                        writeColumnInfo
            </div>

                    else:
            <div class="stacked-subsequent">
                        writeColumnInfo
            </div>
        </div>

        if have children:
        <div id="new_sortable_children_0">
            <div class="ui-tableconf-subtree">
            ...
        </div>
    </div>

    writeColumnInfo:
    <span class="handle-container ordering">
        if (!havePeers) Add "ui-disabled" to anchor class
        if (unstacked or first stacked) [Not subsequent stacked] Add "ui-state-default" to anchor class
        <a class="ui-disabled ui-state-default ui-corner-all ui-sortable-handle" href="#">
            <span class="ui-icon ui-icon-arrowthick-2-n-s">table</span></a>
        <span class="ui-icon icon-spacer"/>
        <span class="ui-icon icon-spacer"/>
    </span>
    <span class="name">
        <input type="text" name="j_idt16_head_0" value="Track"/>
    </span>
    <span class="visibility">
        <input type="checkbox" name="j_idt16_colvis_0" checked="checked"/>
    </span>
    <span class="sorting">
    </span>
    */

    private void writeHeaderRow(TableConfigPanelRenderState state, FacesContext context) throws IOException {
        state.writer.startElement(HTML.DIV_ELEM, null);
        state.writer.writeAttribute(HTML.CLASS_ATTR, "ui-state-default", null);
        ResourceBundle bundle = getComponentResourceBundle(context, ACE_MESSAGES_BUNDLE);
        String orderLabel = getLocalisedMessageFromBundle(bundle, TABLECONFIG_KEY_PREFIX, "ORDER_LABEL", "Order");
        String nameLabel = getLocalisedMessageFromBundle(bundle, TABLECONFIG_KEY_PREFIX, "NAME_LABEL", "Name");
        String visibleLabel  = getLocalisedMessageFromBundle(bundle, TABLECONFIG_KEY_PREFIX, "VISIBLE_LABEL", "Visible");
        String sortLabel = getLocalisedMessageFromBundle(bundle, TABLECONFIG_KEY_PREFIX, "SORT_LABEL", "Sort");
        if (state.orderingConfigurable) {
            state.writer.startElement(HTML.SPAN_ELEM, null);
            state.writer.writeAttribute(HTML.CLASS_ATTR, "ordering", null);
            state.writer.writeText(orderLabel, null);
            state.writer.endElement(HTML.SPAN_ELEM);
        }

        state.writer.startElement(HTML.SPAN_ELEM, null);
        state.writer.writeAttribute(HTML.CLASS_ATTR, "name", null);
        state.writer.writeText(nameLabel, null);
        state.writer.endElement(HTML.SPAN_ELEM);

        if (state.visibilityConfigurable) {
            state.writer.startElement(HTML.SPAN_ELEM, null);
            state.writer.writeAttribute(HTML.CLASS_ATTR, "visibility", null);
            state.writer.writeText(visibleLabel, null);
            state.writer.endElement(HTML.SPAN_ELEM);
        }

        if (state.sortingConfigurable) {
            state.writer.startElement(HTML.SPAN_ELEM, null);
            state.writer.writeAttribute(HTML.CLASS_ATTR, "sorting", null);
            state.writer.writeText(sortLabel, null);
            state.writer.endElement(HTML.SPAN_ELEM);
        }

        state.writer.endElement(HTML.DIV_ELEM);
    }

    private void writeConfigPanelTrashButton(ResponseWriter writer, String clientId, String title) throws IOException {
        writer.startElement(HTML.SPAN_ELEM, null);

        writer.startElement(HTML.ANCHOR_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, "ui-state-default ui-corner-all ui-tableconf-head-button", null);
        writer.writeAttribute(HTML.HREF_ATTR, "#", null);
        writer.writeAttribute(HTML.TITLE_ATTR, title, null);
		writer.writeAttribute(HTML.ONCLICK_ATTR, "return false;", null);
        writer.writeAttribute(HTML.ID_ATTR, clientId +"_tableconf_trash", null);

        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, "ui-icon ui-icon-trash", null);

        writer.writeText("table", null);

        writer.endElement(HTML.SPAN_ELEM);
        writer.endElement(HTML.ANCHOR_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
    }

    private void writeConfigPanelOkButton(ResponseWriter writer, String clientId, String title) throws IOException {
        writer.startElement(HTML.SPAN_ELEM, null);

        writer.startElement(HTML.ANCHOR_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, "ui-state-default ui-corner-all ui-tableconf-head-button", null);
        writer.writeAttribute(HTML.HREF_ATTR, "#", null);
        writer.writeAttribute(HTML.TITLE_ATTR, title, null);
		writer.writeAttribute(HTML.ONCLICK_ATTR, "return false;", null);
        writer.writeAttribute(HTML.ID_ATTR, clientId +"_tableconf_ok", null);

        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, "ui-icon ui-icon-check", null);

        writer.writeText("table", null);

        writer.endElement(HTML.SPAN_ELEM);
        writer.endElement(HTML.ANCHOR_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
    }

    private void writeConfigPanelCloseButton(ResponseWriter writer, String clientId, String title) throws IOException {
        writer.startElement(HTML.SPAN_ELEM, null);

        writer.startElement(HTML.ANCHOR_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, "ui-state-default ui-corner-all ui-tableconf-head-button", null);
        writer.writeAttribute(HTML.HREF_ATTR, "#", null);
        writer.writeAttribute(HTML.TITLE_ATTR, title, null);
		writer.writeAttribute(HTML.ONCLICK_ATTR, "return false;", null);
        writer.writeAttribute(HTML.ID_ATTR, clientId +"_tableconf_close", null);

        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, "ui-icon ui-icon-close", null);

        writer.writeText("table", null);

        writer.endElement(HTML.SPAN_ELEM);
        writer.endElement(HTML.ANCHOR_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
    }

    private void writeJavascript(TableConfigPanelRenderState state,
            String clientId, String tableId, TableConfigPanel component,
            boolean isSingleSort) throws IOException {
        String jsId = this.resolveWidgetVar(component);
        String handle = component.getDragHandle();
        Integer left = component.getOffsetLeft();
        Integer top = component.getOffsetTop();

        state.writer.startElement(HTML.SCRIPT_ELEM, null);
        state.writer.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);

        JSONBuilder json = new JSONBuilder()
                .initialiseVar(jsId).beginFunction("ice.ace.create")
                .item("TableConf").beginArray().item(clientId)
                .beginMap();

        if (handle != null && handle.length() > 0)
            json.entry("handle", handle);

        if (state.orderingConfigurable)
            json.entry("reorderable", state.orderingConfigurable);

        if (state.sortingConfigurable)
            json.entry("sortable", state.sortingConfigurable);

        if (isSingleSort)
            json.entry("singleSort", isSingleSort);

        if (left != null)
            json.entry("left", left);

        if (top != null)
            json.entry("top", top);

        json.entry("tableId", tableId);

        json.entry("sortableContainerIds", state.sortableContainerIds, true, true);

        encodeClientBehaviors(FacesContext.getCurrentInstance(), component, json);

        json.endMap().endArray().endFunction();

        state.writer.write(json.toString());

        state.writer.endElement(HTML.SCRIPT_ELEM);
    }

    private void writeSortControl(TableConfigPanelRenderState state,
            Column column, boolean isBodyColumn) throws IOException {
        state.writer.startElement(HTML.SPAN_ELEM, null);
        state.writer.writeAttribute(HTML.CLASS_ATTR, "sorting", null);
        if (isBodyColumn && column.getValueExpression("sortBy") != null) {
            Integer sortPriority = column.getSortPriority();
            if (sortPriority != null && sortPriority.intValue() == 0)
                sortPriority = null;
            Boolean sortAscending = column.isSortAscending();
            
            state.writer.startElement(HTML.SPAN_ELEM, null);
            state.writer.writeAttribute(HTML.CLASS_ATTR, "ui-tableconf-sort-cont", null);

            state.writer.startElement(HTML.SPAN_ELEM, null);
            state.writer.writeAttribute(HTML.CLASS_ATTR, DataTableConstants.SORTABLE_COLUMN_CONTROL_CLASS, null);

            // Write carats
            state.writer.startElement(HTML.SPAN_ELEM, null);
            state.writer.writeAttribute(HTML.CLASS_ATTR, DataTableConstants.SORTABLE_COLUMN_ICON_CONTAINER, null);

            state.writer.startElement(HTML.ANCHOR_ELEM, null);
            if (sortPriority != null && sortAscending)
                state.writer.writeAttribute(HTML.CLASS_ATTR, DataTableConstants.SORTABLE_COLUMN_ICON_UP_CLASS + " ui-toggled", null);
            else
                state.writer.writeAttribute(HTML.CLASS_ATTR, DataTableConstants.SORTABLE_COLUMN_ICON_UP_CLASS, null);
            state.writer.writeAttribute(HTML.TABINDEX_ATTR, 0, null);
            state.writer.endElement(HTML.ANCHOR_ELEM);

            state.writer.startElement(HTML.ANCHOR_ELEM, null);
            if (sortPriority != null && !sortAscending)
                state.writer.writeAttribute(HTML.CLASS_ATTR, DataTableConstants.SORTABLE_COLUMN_ICON_DOWN_CLASS + " ui-toggled", null);
            else
                state.writer.writeAttribute(HTML.CLASS_ATTR, DataTableConstants.SORTABLE_COLUMN_ICON_DOWN_CLASS, null);
            state.writer.writeAttribute(HTML.TABINDEX_ATTR, 0, null);
            state.writer.endElement(HTML.ANCHOR_ELEM);

            state.writer.endElement(HTML.SPAN_ELEM);

            // Write Sort Order Integer
            state.writer.startElement(HTML.SPAN_ELEM, null);
            state.writer.writeAttribute(HTML.CLASS_ATTR, DataTableConstants.SORTABLE_COLUMN_ORDER_CLASS, null);
            if (sortPriority != null)
                state.writer.writeText(sortPriority, null);
            else
                state.writer.write(HTML.NBSP_ENTITY);
            state.writer.endElement(HTML.SPAN_ELEM);

            state.writer.endElement(HTML.SPAN_ELEM);
            state.writer.endElement(HTML.SPAN_ELEM);
        }
        state.writer.endElement(HTML.SPAN_ELEM);
    }

    private void writeColumnOrderingControl(TableConfigPanelRenderState state,
            Column column, boolean havePeers, boolean unstackedOrFirstStacked)
            throws IOException {
        state.writer.startElement(HTML.SPAN_ELEM, null);
        state.writer.writeAttribute(HTML.CLASS_ATTR, "handle-container ordering", null);
        int numHeaderLevels = state.t.getHeaderTreeIterator().getColumnGroupModel().getRows();
        int headerLevel = state.t.getHeaderTreeIterator().rowIndex();
        for (int level = 0; level < numHeaderLevels; level++) {
            if (level == headerLevel) {
                state.writer.startElement(HTML.ANCHOR_ELEM, null);
                StringBuilder styleClass = new StringBuilder(64);
                if (!havePeers || !state.spanConfigurable) {
                    styleClass.append("ui-disabled "); //TODO  ui-opacity-40
                }
                if (unstackedOrFirstStacked) {
                    styleClass.append("ui-state-default ");
                }
                styleClass.append("ui-corner-all ui-sortable-handle");
                state.writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
                state.writer.writeAttribute(HTML.HREF_ATTR, "#", null);

                state.writer.startElement(HTML.SPAN_ELEM, null);
                state.writer.writeAttribute(HTML.CLASS_ATTR, "ui-icon ui-icon-arrowthick-2-n-s", null);
                // On IE7 the ui-icon text-indent was keeping stacked from showing
                state.writer.writeAttribute(HTML.STYLE_ATTR, "text-indent:0px;", null);
                state.writer.endElement(HTML.SPAN_ELEM);

                state.writer.endElement(HTML.ANCHOR_ELEM);
            } else {
                state.writer.startElement(HTML.SPAN_ELEM, null);
                state.writer.writeAttribute(HTML.CLASS_ATTR, "ui-icon icon-spacer", null);
                // On IE7 the ui-icon text-indent was keeping stacked from showing
                state.writer.writeAttribute(HTML.STYLE_ATTR, "text-indent:0px;", null);
                state.writer.endElement(HTML.SPAN_ELEM);
            }
        }
        state.writer.endElement(HTML.SPAN_ELEM);
    }
    private void writeColumnVisibilityControl(ResponseWriter writer,
            Column column, String columnClientId, boolean columnConfigurable,
            boolean columnRendered) throws IOException {
        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, "visibility", null);
        writer.startElement(HTML.INPUT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "checkbox", null);
        if (!columnConfigurable)
            writer.writeAttribute(HTML.DISABLED_ATTR, "disabled", null);
        writer.writeAttribute(HTML.NAME_ATTR, columnClientId+TableConfigPanel.COLUMN_VISIBILITY_SUFFIX, null);
        if (columnRendered)
            writer.writeAttribute(HTML.CHECKED_ATTR, "checked", null);
        writer.endElement(HTML.INPUT_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
    }
    private void writeColumnNameControl(ResponseWriter writer, Column column,
            String columnClientId, boolean columnConfigurable, boolean naming)
            throws IOException {
        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, "name", null);
        writer.startElement(HTML.INPUT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "text", null);
        if (!naming || !columnConfigurable)
            writer.writeAttribute(HTML.DISABLED_ATTR, "disabled", null);
        writer.writeAttribute(HTML.NAME_ATTR, columnClientId+TableConfigPanel.COLUMN_HEAD_SUFFIX, null);
        writer.writeAttribute(HTML.VALUE_ATTR, column.getHeaderText(), null);
        writer.endElement(HTML.INPUT_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
    }

    private static String deriveStyleClassFromClientId(String clientId) {
        return clientId.replaceAll("-|" + UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance()), "_");
    }


    public static class TableConfigPanelRenderState {
        ResponseWriter writer;
        ColumnModel columnModel;
        String tableConfigPanelClientId;
        boolean orderingConfigurable;
        boolean namingConfigurable;
        boolean visibilityConfigurable;
        boolean sortingConfigurable;
        boolean hideDisabledRows;
        boolean firstCol;
        boolean lastCol;
        boolean spanConfigurable;
        boolean firstStackedShown;
        List<String> sortableContainerIds;
        DepthFirstHeadTraversal<TableConfigPanelRenderState, IOException> t;

        private TableConfigPanelRenderState(
            ResponseWriter writer, ColumnModel columnModel,
            String tableConfigPanelClientId,
            boolean orderingConfigurable, boolean namingConfigurable,
            boolean visibilityConfigurable, boolean sortingConfigurable,
            boolean hideDisabledRows, String panelType)
        {
            this.writer = writer;
            this.columnModel = columnModel;
            this.tableConfigPanelClientId = tableConfigPanelClientId;
            this.orderingConfigurable = orderingConfigurable;
            this.namingConfigurable = namingConfigurable;
            this.visibilityConfigurable = visibilityConfigurable;
            this.sortingConfigurable = sortingConfigurable;
            this.hideDisabledRows = hideDisabledRows;
            this.firstCol = panelType.equals("first-col") ;
            this.lastCol = panelType.equals("last-col");
            this.spanConfigurable = true;
            this.firstStackedShown = true;
            this.sortableContainerIds = new ArrayList<String>();
        }
    }
}
