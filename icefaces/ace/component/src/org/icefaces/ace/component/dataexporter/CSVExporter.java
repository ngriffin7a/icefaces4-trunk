/*
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 */
package org.icefaces.ace.component.dataexporter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import javax.el.MethodExpression;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.icefaces.ace.component.datatable.DataTable;
import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.component.columngroup.ColumnGroup;
import org.icefaces.ace.component.row.Row;
import org.icefaces.ace.model.table.RowState;
import org.icefaces.ace.model.table.RowStateMap;
import org.icefaces.ace.model.table.TreeDataModel;

public class CSVExporter extends Exporter {
	
    @Override
	public String export(FacesContext facesContext, DataExporter component, DataTable table) throws IOException {
		setUp(component, table);
		StringBuilder builder = new StringBuilder();
		List<UIColumn> columns = getColumnsToExport(table, excludeColumns);
    	
    	if (includeHeaders) {
			ColumnGroup columnGroup = getColumnGroupHeader(table);
			if (columnGroup != null) {
				List<Row> rows = getRows(columnGroup);
				for (Row row : rows) {
					List<UIColumn> rowColumns = getRowColumnsToExport(row, table, excludeColumns);
					addFacetColumns(builder, rowColumns, ColumnType.HEADER);
				}
			} else {
				addFacetColumns(builder, columns, ColumnType.HEADER);
			}
		}
		
		Object model = table.getModel();
		TreeDataModel rootModel = null;
		boolean hasRowExpansion = false;
		if (model != null && table.hasTreeDataModel()) {
			rootModel = (TreeDataModel) model;
			hasRowExpansion = true;
		}
    	
		int rowCount = table.getRowCount();
    	int first = pageOnly ? table.getFirst() : 0;
    	int size = pageOnly ? (first + table.getRows()) : rowCount;
		size = size > rowCount ? rowCount : size;

		RowStateMap rowStateMap = table.getStateMap();

		String rowIndexVar = table.getRowIndexVar();
		rowIndexVar = rowIndexVar == null ? "" : rowIndexVar;
    	for (int i = first; i < size; i++) {
    		table.setRowIndex(i);
			boolean exportRow = true;
			if (selectedRowsOnly) {
				RowState rowState = rowStateMap.get(table.getRowData());
				if (!rowState.isSelected()) exportRow = false;
			}
			if (exportRow) {
				if (!"".equals(rowIndexVar)) {
					facesContext.getExternalContext().getRequestMap().put(rowIndexVar, i);
				}
				// 'before' conditional rows
				List<Row> leadingRows = table.getConditionalRows(i, true);
				for (Row r : leadingRows) exportConditionalRow(builder, r);
				
				addColumnValues(builder, columns);
				builder.append("\n");
				if (hasRowExpansion) {
					exportChildRows(facesContext, rootModel, rowStateMap, table, columns, "" + i, builder);
				}
				
				// 'after' conditional rows
				List<Row> tailingRows = table.getConditionalRows(i, false);
				for (Row r : tailingRows) exportConditionalRow(builder, r);
			}
		}

        if (hasColumnFooter(columns) && includeFooters) {
            addFacetColumns(builder, columns, ColumnType.FOOTER);
        }
    	
    	table.setRowIndex(-1);
        
		byte[] bytes = builder.toString().getBytes();
		
		return registerResource(bytes, filename + ".csv", "text/csv");
	}
	
	protected void exportChildRows(FacesContext context, TreeDataModel rootModel, RowStateMap rowStateMap,
		DataTable table, List<UIColumn> columns, String rootIndex, StringBuilder builder) throws IOException {		
		rootModel.setRootIndex(rootIndex);
		rootModel.setRowIndex(0);

		RowState rootState = rowStateMap.get(rootModel.getRootData());
		
		String rowVar = table.getVar();
		String rowIndexVar = table.getRowIndexVar();
		if ((rootState.isExpanded() || !expandedOnly) && rootModel.getRowCount() > 0) {
			while (rootModel.getRowIndex() < rootModel.getRowCount()) {
				int rowIndex = rootModel.getRowIndex();
				Object rowData = rootModel.getRowData();
                if (rowVar != null) context.getExternalContext()
                        .getRequestMap().put(rowVar, rowData);
                if (rowIndexVar != null) context.getExternalContext()
                        .getRequestMap().put(rowIndexVar, rowData);
				
				// export
				addColumnValues(builder, columns);
				builder.append("\n");
				
				RowState rowState = rowStateMap.get(rootModel.getRowData());
				if (rowState.isExpanded() || !expandedOnly) {
					
					// recurse
					exportChildRows(context, rootModel, rowStateMap, table, columns, rootIndex + "." + rowIndex, builder);
					
					// restore
					rootModel.setRootIndex(rootIndex);
					rootModel.setRowIndex(rowIndex);
				}
                rootModel.setRowIndex(rootModel.getRowIndex() + 1);
                if (rowIndexVar != null) context.getExternalContext().getRequestMap().remove(rowIndexVar);
                if (rowVar != null) context.getExternalContext().getRequestMap().remove(rowVar);
			}
		}
		
        rootModel.setRootIndex(null);
	}
	
	protected void exportConditionalRow(StringBuilder builder, Row row) throws IOException {
		List<UIComponent> children = row.getChildren();
		List<UIColumn> rowColumns = new ArrayList<UIColumn>(children.size());
		for (UIComponent kid : children) {
			if (kid instanceof Column) {
				Column c = (Column) kid;
				int colspan = c.getColspan();
				for (int i = 0; i < colspan; i++) rowColumns.add(c);
			}
		}
		addColumnValues(builder, rowColumns);
		builder.append("\n");
	}
	
	protected void addColumnValues(StringBuilder builder, List<UIColumn> columns) throws IOException {
		for (Iterator<UIColumn> iterator = columns.iterator(); iterator.hasNext();) {
            addColumnValue(builder, iterator.next().getChildren());
            if (iterator.hasNext()) { builder.append(","); }
		}
	}

	protected void addFacetColumns(StringBuilder builder, List<UIColumn> columns, ColumnType columnType) throws IOException {
		for (Iterator<UIColumn> iterator = columns.iterator(); iterator.hasNext();) {
			UIColumn uiColumn = iterator.next();
			UIComponent facet = uiColumn.getFacet(columnType.facet());
			if (facet != null) {
				addColumnValue(builder, facet);
			} else {
				String value = "";
				if (uiColumn instanceof Column) {
					Column column = (Column) uiColumn;
					if (columnType == ColumnType.HEADER) {
						String headerText = column.getHeaderText();
						value = headerText != null ? headerText : "";
					} else if (columnType == ColumnType.FOOTER) {
						String footerText = column.getFooterText();
						value = footerText != null ? footerText : "";
					}
				}
				builder.append("\"" + value + "\"");
			}
            if (iterator.hasNext()) { builder.append(","); }
		}
		builder.append("\n");
    }
	
	protected void addColumnValue(StringBuilder builder, UIComponent component) throws IOException {
		String value = component == null ? "" : exportValue(FacesContext.getCurrentInstance(), component);
		builder.append("\"" + value + "\"");
	}
	
	protected void addColumnValue(StringBuilder builder, List<UIComponent> components) throws IOException {
		StringBuilder builder1 = new StringBuilder();
		for (UIComponent component : components)
			if (component.isRendered()) {
				String value = exportValue(FacesContext.getCurrentInstance(), component);
				builder1.append(value);
            }
		builder.append("\"" + builder1.toString() + "\"");
	}
}
