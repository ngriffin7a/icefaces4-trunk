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
import org.icefaces.ace.model.table.ColumnGroupModel;
import org.icefaces.ace.model.table.ColumnModel;
import org.icefaces.ace.model.table.DepthFirstHeadTraversal;
import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.JSONBuilder;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.*;

public class DataTableHeadRenderer {
    protected static void encodeTableHead(FacesContext context, DataTableRenderingContext tableContext) throws IOException {
        DataTable table = tableContext.getTable();
        if (!table.hasHeaders()) return;

        String clientId = table.getClientId(context);
        ColumnModel columnModel = tableContext.getColumnModel();
        Map<String, AutoAdjustRenderedColspan.AdjustedRenderedColspan> map =
            AutoAdjustRenderedColspan.adjustIfAllowed(
                table.findTableConfigPanel(context), columnModel);
        ColumnGroupModel.TreeIterator iterator = columnModel.getHeaderModel().iterate();
        if (iterator.empty()) return;

        ResponseWriter writer = context.getResponseWriter();

        if (tableContext.isStaticHeaders() && !table.isInDuplicateSegment()) {
            writer.startElement(HTML.DIV_ELEM, null);
            writer.writeAttribute(HTML.CLASS_ATTR, DataTableConstants.SCROLLABLE_HEADER_CLASS, null);
            writer.startElement(HTML.TABLE_ELEM, null);
        }

        writer.startElement(HTML.THEAD_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_header", null);

        if (table.isInDuplicateSegment())
            writer.writeAttribute(HTML.STYLE_ATTR, "display:none;", null);

        boolean renderingFirstCol = true;
        if (iterator.columnGroup() == null || iterator.columnGroup().isRendered()) {
            do {
                if (iterator.row() != null && !iterator.row().isRendered()) {
                    continue;
                }
                writer.startElement(HTML.TR_ELEM, null);
                tableContext.setInHeaderSubrows(iterator.row() != null);
                do {
                    List<Column> columnsInCell = iterator.columns();
                    for (int i = 0; i < columnsInCell.size(); i++) {
                        Column column = columnsInCell.get(i);
                        if (column.isRendered()) {
                            tableContext.setFirstColumn(renderingFirstCol);
                            tableContext.setLastColumn(!iterator.nextRendered(false));
                            boolean isCurrStacked = DataTableRendererUtil.
                                isCurrColumnStacked(columnsInCell, column);
                            boolean isNextStacked = DataTableRendererUtil.
                                isNextStacked(columnsInCell, column);
                            if (isNextStacked) { // Used to only check if tableContext.isInHeaderSubrows()
                                if (!DataTableRendererUtil.areBothSingleColumnSpan(column, columnsInCell.get(i+1)))
                                    throw new FacesException("DataTable : \"" + clientId + "\" must not have stacked header columns, with colspan values greater than 1.");
                                if (!DataTableRendererUtil.isNextColumnRowSpanEqual(column, columnsInCell.get(i+1)))
                                    throw new FacesException("DataTable : \"" + clientId + "\" must not have stacked header columns, with unequal rowspan values.");
                            }
                            encodeColumn(context, tableContext, column,
                                isCurrStacked, isNextStacked, map);
                            renderingFirstCol = false;
                        }
                    }
                } while (iterator.nextPeer(false, true));

                writer.endElement(HTML.TR_ELEM);
            } while (iterator.nextRow(true, true));
        }

        // None of the header rows or columns are rendered, so render the
        // TableConfigPanel launcher button on its own
        TableConfigPanel panel = table.findTableConfigPanel(context);
        if (renderingFirstCol && panel != null && !panel.getType().equals(
            "paginator-button")) {
            writer.startElement(HTML.TR_ELEM, null);
            writer.startElement(HTML.TD_ELEM, null);
            writer.writeAttribute(HTML.COLSPAN_ATTR,
                iterator.getColumnGroupModel().getColumns(), null);
            encodeConfigPanelLaunchButton(writer, table, true);
            writer.endElement(HTML.TD_ELEM);
            writer.endElement(HTML.TR_ELEM);
        }

        writer.endElement(HTML.THEAD_ELEM);

        if (tableContext.isStaticHeaders() && !table.isInDuplicateSegment()) {
            writer.endElement(HTML.TABLE_ELEM);
            writer.endElement(HTML.DIV_ELEM);
        }
    }

   private static void encodeColumn(FacesContext context,
            DataTableRenderingContext tableContext, Column column,
            boolean isCurrStacked, boolean isNextStacked,
            Map<String, AutoAdjustRenderedColspan.AdjustedRenderedColspan>
            adjColspans) throws IOException {
        DataTable table = tableContext.getTable();
        ResponseWriter writer = context.getResponseWriter();
        String clientId = column.getClientId(context);

        tableContext.setColumnSortable(column.getValueExpression("sortBy") != null);
        tableContext.setColumnFilterable(column.getValueExpression("filterBy") != null);

        if (!isCurrStacked) {
            String style = column.getStyle();
            String styleClass = column.getStyleClass();
            String columnClass = DataTableConstants.COLUMN_HEADER_CLASS;

            columnClass = (tableContext.isReorderableColumns() && column.isReorderable())
                    ? columnClass + " " + DataTableConstants.REORDERABLE_COL_CLASS
                    : columnClass;
            columnClass = styleClass != null
                    ? columnClass + " " + styleClass
                    : columnClass;
            columnClass = (column.hasSortPriority() && !isNextStacked)
                    ? columnClass + " ui-state-active"
                    : columnClass;

            writer.startElement(HTML.TH_ELEM, null);
            writer.writeAttribute(HTML.CLASS_ATTR, columnClass, null);

            if (style != null)
                writer.writeAttribute(HTML.STYLE_ELEM, style, null);
            int rowspan = column.getRowspan();
            if (rowspan != 1)
                writer.writeAttribute(HTML.ROWSPAN_ATTR, rowspan, null);
            AutoAdjustRenderedColspan.AdjustedRenderedColspan adj =
                (adjColspans != null) ? adjColspans.get(clientId) : null;
            int colspan = adj != null ? adj.getAdjustedColspan() : column.getColspan();
            if (colspan != 1)
                writer.writeAttribute(HTML.COLSPAN_ATTR, colspan, null);
        }

        else {
            writer.startElement("hr", null);
            writer.endElement("hr");
        }

        //Container
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);

        if (tableContext.isResizableColumns())
            writer.writeAttribute(HTML.STYLE_ATTR, "position:relative;", null);

        String columnClass = DataTableConstants.COLUMN_HEADER_CONTAINER_CLASS;
        columnClass = tableContext.isColumnSortable() ? columnClass + " " + DataTableConstants.SORTABLE_COLUMN_CLASS : columnClass;
        columnClass = table.isClickableHeaderSorting() ? columnClass + " clickable" : columnClass;
        // Add style class to div in stacking case, else style th
        columnClass = (column.hasSortPriority() && (isCurrStacked || isNextStacked)) ? columnClass + " ui-state-active" : columnClass;

        writer.writeAttribute(HTML.CLASS_ATTR, columnClass, null);

        TableConfigPanel panel = table.findTableConfigPanel(context);

        if (!isCurrStacked && panelTargetsColumn(panel, column,
                tableContext.isFirstColumn(),
                tableContext.isLastColumn(), true))
            encodeLeftSideControls(writer, table,
                    tableContext.isFirstColumn());

        writer.startElement(HTML.SPAN_ELEM, null);

        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_text", null);
        writer.writeAttribute(HTML.CLASS_ATTR, DataTableConstants.HEAD_TEXT_CLASS, null);

        //Header content
        UIComponent header = column.getFacet("header");
        String headerText = column.getHeaderText();

        if (header != null) header.encodeAll(context);
        else if (headerText != null) writer.write(headerText);
        else if (tableContext.isInHeaderSubrows())
            for (UIComponent c : column.getChildren())
                c.encodeAll(context);


        writer.endElement(HTML.SPAN_ELEM);
        writer.endElement(HTML.SPAN_ELEM);

        boolean configButton = !isCurrStacked && panelTargetsColumn(panel, column, tableContext.isFirstColumn(),
                tableContext.isLastColumn(), false);

        if (tableContext.isColumnSortable() || tableContext.isColumnPinningEnabled() || configButton)
            encodeRightSideControls(writer, context, tableContext, column, configButton);

        //Filter
        if (tableContext.isColumnFilterable())
            encodeFilter(context, tableContext, column);

		if (tableContext.isColumnSortable() && table.isClickableHeaderSorting()) {
			writer.startElement(HTML.SCRIPT_ELEM, null);
			writer.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);
			writer.writeText("(function(){var table = ice.ace.instance('"+table.getClientId(context)+"');if(table) table.setupClickableHeaderEventsForColumn('"+clientId+"');})();", null);
			writer.endElement(HTML.SCRIPT_ELEM);
		}

        writer.endElement(HTML.DIV_ELEM);

        if (!isNextStacked) {
            writer.endElement("th");
        }
    }

    private static boolean panelTargetsColumn(TableConfigPanel panel, Column column, boolean firstColumn, boolean lastColumn, boolean left) {
        if (panel == null) return false;
        String type = panel.getType();
        if (type.equals("first-col") && firstColumn && left) {
            return true;
        } else if (type.equals("last-col") && lastColumn && !left)  {
            return true;
        } else if (type.equals("in-col-left") && left && panel.getInColumnId().equals(column.getId())) {
            return true;
        } else if (type.equals("in-col-right") && !left && panel.getInColumnId().equals(column.getId())) {
            return true;
        }
        return false;
    }

    private static void encodeLeftSideControls(ResponseWriter writer, DataTable table, boolean first) throws IOException {
        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, DataTableConstants.HEADER_LEFT_CLASS, null);

        encodeConfigPanelLaunchButton(writer, table, first);

        writer.endElement(HTML.SPAN_ELEM);
    }

    private static void encodeRightSideControls(ResponseWriter writer, FacesContext context, DataTableRenderingContext tableContext, Column column, boolean renderConfButton) throws IOException {
        writer.write("&nbsp;");

        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, DataTableConstants.HEADER_RIGHT_CLASS, null);

        //Sort icon
        if (tableContext.isColumnSortable())
            encodeSortControl(writer, context, tableContext, column);

        if (tableContext.isColumnPinningEnabled() && tableContext.showPinningControls())
            encodePinningControl(writer, context, tableContext, column);

        //Configurable last-col controls
        if (renderConfButton)
            encodeConfigPanelLaunchButton(writer, tableContext.getTable(), false);

        writer.endElement(HTML.SPAN_ELEM);
    }

    private static void encodePinningControl(ResponseWriter writer, FacesContext context, DataTableRenderingContext tableContext, Column column) throws IOException {
        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, DataTableConstants.PIN_COLUMN_CONTROL_CLASS, null);
        writer.startElement(HTML.ANCHOR_ELEM, null);

        writer.writeAttribute(HTML.CLASS_ATTR, "ui-state-default ui-corner-all", null);
        writer.writeAttribute(HTML.HREF_ATTR, "#", null);
        writer.writeAttribute(HTML.ONCLICK_ATTR,
                CoreRenderer.resolveWidgetVar(tableContext.getTable())+".pinThisColumn(event);return false;", null);
        writer.startElement(HTML.SPAN_ELEM, null);

        writer.writeAttribute(HTML.CLASS_ATTR, "ui-icon", null);

        writer.endElement(HTML.SPAN_ELEM);
        writer.endElement(HTML.ANCHOR_ELEM);
        writer.endElement(HTML.SPAN_ELEM);

    }

    private static void encodeSortControl(ResponseWriter writer, FacesContext context, DataTableRenderingContext tableContext, Column column) throws IOException {
        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, column.getClientId() + "_sortControl", null);
        writer.writeAttribute(HTML.CLASS_ATTR, DataTableConstants.SORTABLE_COLUMN_CONTROL_CLASS, null);

        // Write carats
		if (!column.isHideSortControls()) {
			writer.startElement(HTML.SPAN_ELEM, null);
			writer.writeAttribute(HTML.CLASS_ATTR, DataTableConstants.SORTABLE_COLUMN_ICON_CONTAINER, null);

            final String iconUpID = column.getClientId() + "_sortControl_up";
            final String iconDownID = column.getClientId() + "_sortControl_down";

			writer.startElement(HTML.ANCHOR_ELEM, null);
            writer.writeAttribute(HTML.ID_ATTR, iconUpID, null);
            writer.writeAttribute(HTML.TABINDEX_ATTR, tableContext.getTabIndex(), null);
            writer.writeAttribute(HTML.ONCLICK_ATTR, "ice.setFocus('" + iconUpID + "');", null);
			if (column.hasSortPriority() && column.isSortAscending())
				writer.writeAttribute(HTML.CLASS_ATTR, DataTableConstants.SORTABLE_COLUMN_ICON_UP_CLASS + " ui-toggled", null);
			else writer.writeAttribute(HTML.CLASS_ATTR, DataTableConstants.SORTABLE_COLUMN_ICON_UP_CLASS, null);
			writer.endElement(HTML.ANCHOR_ELEM);

			writer.startElement(HTML.ANCHOR_ELEM, null);
            writer.writeAttribute(HTML.ID_ATTR, iconDownID, null);
			writer.writeAttribute(HTML.TABINDEX_ATTR, tableContext.getTabIndex(), null);
            writer.writeAttribute(HTML.ONCLICK_ATTR, "ice.setFocus('" + iconDownID + "');", null);
			if (column.hasSortPriority() && !column.isSortAscending())
				writer.writeAttribute(HTML.CLASS_ATTR, DataTableConstants.SORTABLE_COLUMN_ICON_DOWN_CLASS + " ui-toggled", null);
			else writer.writeAttribute(HTML.CLASS_ATTR, DataTableConstants.SORTABLE_COLUMN_ICON_DOWN_CLASS, null);
			writer.endElement(HTML.ANCHOR_ELEM);

			DataTable table = tableContext.getTable();
			writer.startElement(HTML.SCRIPT_ELEM, null);
			writer.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);
			writer.writeText("(function(){var table = ice.ace.instance('"+table.getClientId(context)+"');if(table) table.setupSortEventsForColumn('"+column.getClientId()+"_sortControl');})();", null);
			writer.endElement(HTML.SCRIPT_ELEM);

			writer.endElement(HTML.SPAN_ELEM);
		}

        // Write Sort Order Integer
        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, DataTableConstants.SORTABLE_COLUMN_ORDER_CLASS, null);

        if (tableContext.getTable().isSingleSort())
            writer.writeAttribute(HTML.STYLE_ATTR, "display:none;", null);
        else if (column.hasSortPriority()) writer.writeText(column.getSortPriority(), null);

        writer.endElement(HTML.SPAN_ELEM);

        writer.endElement(HTML.SPAN_ELEM);
    }

    private static void encodeFilter(FacesContext context, DataTableRenderingContext tableContext, Column column) throws IOException {
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        ResponseWriter writer = context.getResponseWriter();
        DataTable table = tableContext.getTable();

        String widgetVar = CoreRenderer.resolveWidgetVar(table);
        String filterId = column.getClientId(context) + "_filter";
        String filterFunction = widgetVar + ".filter(event)";
        String filterStyleClass = column.getFilterStyleClass();
        String filterEvent = table.getFilterEvent();
        filterStyleClass = filterStyleClass == null
                ? DataTableConstants.COLUMN_FILTER_CLASS
                : DataTableConstants.COLUMN_FILTER_CLASS + " " + filterStyleClass;

        if (column.getValueExpression("filterOptions") == null) {
            String filterValue = column.getFilterValue() != null ? column.getFilterValue() : "";

            writer.startElement(HTML.INPUT_ELEM, null);
            writer.writeAttribute(HTML.ID_ATTR, filterId, null);
            writer.writeAttribute(HTML.NAME_ATTR, filterId, null);
            writer.writeAttribute(HTML.TABINDEX_ATTR, tableContext.getTabIndex(), null);
            writer.writeAttribute(HTML.CLASS_ATTR, filterStyleClass, null);
            writer.writeAttribute("size", "1", null); // Webkit requires none zero/null size value to use CSS width correctly.
            writer.writeAttribute("value", filterValue , null);

            if (filterEvent.equals("keyup") || filterEvent.equals("blur"))
                writer.writeAttribute("on"+filterEvent, filterFunction , null);

            if (column.getFilterStyle() != null)
                writer.writeAttribute(HTML.STYLE_ELEM, column.getFilterStyle(), null);

            writer.endElement(HTML.INPUT_ELEM);
        }
        else {
            writer.startElement("select", null);
            writer.writeAttribute(HTML.ID_ATTR, filterId, null);
            writer.writeAttribute(HTML.NAME_ATTR, filterId, null);
            writer.writeAttribute(HTML.TABINDEX_ATTR, tableContext.getTabIndex(), null);
            writer.writeAttribute(HTML.CLASS_ATTR, filterStyleClass, null);
            writer.writeAttribute("onchange", filterFunction, null);

            SelectItem[] itemsArray = (SelectItem[]) getFilterOptions(column);
            Object filterVal = column.getFilterValue();

            for (SelectItem item : itemsArray) {
                writer.startElement("option", null);
                writer.writeAttribute("value", item.getValue(), null);

                Object itemVal = item.getValue();

                if ((filterVal == null && itemVal == null)
                        || itemVal.toString().equals(filterVal)) {
                    writer.writeAttribute("selected", "selected", null);
                }

                writer.write(item.getLabel());
                writer.endElement("option");
            }

            writer.endElement("select");
        }

    }

    protected static void encodeConfigPanelLaunchButton(ResponseWriter writer, DataTable component, boolean first) throws IOException {
        String jsId = CoreRenderer.resolveWidgetVar(component);

        String panelJsId = CoreRenderer
                .resolveWidgetVar(component.findTableConfigPanel(FacesContext.getCurrentInstance()));

        TableConfigPanel configPanel = component.findTableConfigPanel(FacesContext.getCurrentInstance());

        if (configPanel.isRendered()) {
            String clientId = configPanel.getClientId();

            JSONBuilder json = JSONBuilder.create().beginFunction("ice.ace.lazy")
                    .item("TableConfLauncher")
                    .beginArray()
                    .item(clientId + "_tableconf_launch")
                    .item(panelJsId, false)
                    .endArray()
                    .endFunction();

            String script = json.toString();

            writer.startElement(HTML.SPAN_ELEM, null);
            writer.writeAttribute(HTML.CLASS_ATTR, "ui-tableconf-button", null);
            writer.writeAttribute(HTML.STYLE_ELEM, (first) ? "left:0;" : "right:0;", null);

            writer.startElement(HTML.ANCHOR_ELEM, null);
            writer.writeAttribute(HTML.CLASS_ATTR, "ui-state-default ui-corner-all", null);
            writer.writeAttribute(HTML.ONMOUSEOVER_ATTR, script, null);
            writer.writeAttribute(HTML.ONFOCUS_ATTR, script, null);
            writer.writeAttribute(HTML.TABINDEX_ATTR, 0, null);
            writer.writeAttribute(HTML.ID_ATTR, clientId +"_tableconf_launch", null);

            writer.startElement(HTML.SPAN_ELEM, null);
            writer.writeAttribute(HTML.CLASS_ATTR, "ui-icon ui-icon-gear", null);

            writer.endElement(HTML.SPAN_ELEM);
            writer.endElement(HTML.ANCHOR_ELEM);
            writer.endElement(HTML.SPAN_ELEM);
        }
    }

    /* Util Methods */

    private static SelectItem[] getFilterOptions(Column column) {
        Object options = column.getFilterOptions();
        if (options instanceof SelectItem[]) return (SelectItem[]) options;
        else if (options instanceof Collection<?>) return ((Collection<SelectItem>) column.getFilterOptions()).toArray(new SelectItem[] {});
        else throw new FacesException("Filter options for column " + column.getClientId() + " should be a SelectItem array or collection");
    }
}
