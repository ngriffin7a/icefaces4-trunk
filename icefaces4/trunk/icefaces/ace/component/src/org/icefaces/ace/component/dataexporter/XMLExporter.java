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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import javax.el.MethodExpression;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.icefaces.ace.component.datatable.DataTable;
import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.component.columngroup.ColumnGroup;
import org.icefaces.ace.component.row.Row;
import org.icefaces.ace.model.table.RowState;
import org.icefaces.ace.model.table.RowStateMap;
import org.icefaces.ace.model.table.TreeDataModel;

import org.icefaces.ace.util.XMLChar;

public class XMLExporter extends Exporter {

    @Override
	public String export(FacesContext facesContext, DataExporter component, DataTable table) throws IOException {
		setUp(component, table);
		StringBuilder builder = new StringBuilder();
		
		List<UIColumn> columns = getColumnsToExport(table, excludeColumns);
    	
		List<String> headers;
		ColumnGroup columnGroup = getColumnGroupHeader(table);
		if (columnGroup != null) {
			headers = getHeadersFromColumnGroup(columnGroup, columns, table, excludeColumns);
		} else {
			headers = getFacetTexts(columns, ColumnType.HEADER);
		}
    	List<String> footers = getFacetTexts(columns, ColumnType.FOOTER);
    	String var = table.getVar().toLowerCase();
    	
    	builder.append("<?xml version=\"1.0\"?>\n");
    	builder.append("<" + table.getId() + ">\n");
    	
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
				for (Row r : leadingRows) exportConditionalRow(builder, r, var, true);
				
				builder.append("\t<" + var + ">\n");
				addColumnValues(builder, columns, headers);
				builder.append("\t</" + var + ">\n");
				if (hasRowExpansion) {
					exportChildRows(facesContext, rootModel, rowStateMap, table, columns, "" + i, builder, headers, var);
				}
				
				// 'after' conditional rows
				List<Row> tailingRows = table.getConditionalRows(i, false);
				for (Row r : tailingRows) exportConditionalRow(builder, r, var, false);
			}
		}

        if (hasColumnFooter(columns) && includeFooters) {
            builder.append("\t<footers>\n");
            addFooterValues(builder, footers, headers);
            builder.append("\t</footers>\n");
        }
    	
    	builder.append("</" + table.getId() + ">");
    	
    	table.setRowIndex(-1);
    	
		byte[] bytes = builder.toString().getBytes();
		
		return registerResource(bytes, filename + ".xml", "text/xml");
	}
	
	protected void exportChildRows(FacesContext context, TreeDataModel rootModel, RowStateMap rowStateMap,
		DataTable table, List<UIColumn> columns, String rootIndex, StringBuilder builder, List<String> headers, String var) throws IOException {		
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
				builder.append("\t<" + var + ">\n");
				addColumnValues(builder, columns, headers);
				builder.append("\t</" + var + ">\n");
				
				RowState rowState = rowStateMap.get(rootModel.getRowData());
				if (rowState.isExpanded() || !expandedOnly) {
					
					// recurse
					exportChildRows(context, rootModel, rowStateMap, table, columns, rootIndex + "." + rowIndex, builder, headers, var);
					
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
	
	protected void exportConditionalRow(StringBuilder builder, Row row, String var, boolean isBefore) throws IOException {
		List<UIComponent> children = row.getChildren();
		List<UIColumn> rowColumns = new ArrayList<UIColumn>(children.size());
		for (UIComponent kid : children) {
			if (kid instanceof Column) {
				rowColumns.add((Column) kid);
			}
		}
		String suffix = isBefore ? "-before" : "-after";
		builder.append("\t<" + var + suffix + ">\n");
		addColumnValues(builder, rowColumns, getFacetTexts(rowColumns, ColumnType.HEADER));
		builder.append("\t</" + var + suffix + ">\n");
	}
	
	protected void addColumnValues(StringBuilder builder, List<UIColumn> columns, List<String> headers) throws IOException {
		for (int i = 0; i < columns.size(); i++) {
            addColumnValue(builder, columns.get(i).getChildren(), headers.get(i));
		}
	}
	
	protected void addFooterValues(StringBuilder builder, List<String> footers, List<String> headers) throws IOException {
		for (int i = 0; i < footers.size(); i++) {
			String footer = footers.get(i);
			
			if (footer.length() > 0)
				addColumnValue(builder, footer, headers.get(i));
		}
	}	
	
	protected List<String> getFacetTexts(List<UIColumn> columns, ColumnType columnType) {
		List<String> facets = new ArrayList<String>();
		 
		for (Iterator<UIColumn> iterator = columns.iterator(); iterator.hasNext();) {
			UIColumn uiColumn = iterator.next();
			UIComponent facet = uiColumn.getFacet(columnType.facet());
			if (facet != null) {
				String value = exportValue(FacesContext.getCurrentInstance(), facet);
				facets.add(sanitizeXMLTagName(value));
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
				facets.add(sanitizeXMLTagName(value));
			}
		}
        return facets;
	}
	
	protected String extractValueToDisplay(UIColumn column, ColumnType columnType) {
		UIComponent facet = column.getFacet(columnType.facet());
		
		if (facet != null && facet.isRendered()) {
			String value = exportValue(FacesContext.getCurrentInstance(), facet);
			
			return value;
		} else {
			String value = "";
			if (column instanceof Column) {
				Column _column = (Column) column;
				if (columnType == ColumnType.HEADER) {
					String headerText = _column.getHeaderText();
					value = headerText != null ? headerText : "";
				} else if (columnType == ColumnType.FOOTER) {
					String footerText = _column.getFooterText();
					value = footerText != null ? footerText : "";
				}
			}
			return value;
		}
	}
	
	protected List<String> getHeadersFromColumnGroup(ColumnGroup columnGroup, List<UIColumn> columns, UIData data, int[] excludeColumns) {
	
		ArrayList<Row> rows = (ArrayList<Row>) getRows(columnGroup);
		int size = rows.size();
		if (size > 0) {
			List<UIColumn> rowColumns = getRowColumnsToExport(rows.get(size-1), data, excludeColumns); // only use last row in column group
			List<String> values = new ArrayList<String>();
			for (UIColumn column : rowColumns) {
				String value = extractValueToDisplay(column, ColumnType.HEADER);
				values.add(sanitizeXMLTagName(value));
			}
			return values;
		} else {
			return getFacetTexts(columns, ColumnType.HEADER);
		}
	}

	protected void addColumnValue(StringBuilder builder, List<UIComponent> components, String header) throws IOException {
		StringBuilder builder1 = new StringBuilder();
		String tag = header.toLowerCase();
		builder.append("\t\t<" + tag + ">");

		for (UIComponent component : components) {
			if (component.isRendered()) {
				String value = exportValue(FacesContext.getCurrentInstance(), component);

				builder1.append(value);
			}
		}

		builder.append(builder1.toString());
		
		builder.append("</" + tag + ">\n");
	}
	
	protected void addColumnValue(StringBuilder builder, String footer, String header) throws IOException {
		String tag = header.toLowerCase();
		builder.append("\t\t<" + tag + ">");

		builder.append(footer.toLowerCase());
		
		builder.append("</" + tag + ">\n");
	}
	
	protected String sanitizeXMLTagName(String tag) {
		StringBuilder sb = new StringBuilder();
		
		int length = tag.length();
		for (int i = 0; i < length; i++) {
			if (XMLChar.isName(tag.codePointAt(i)))
				sb.appendCodePoint(tag.codePointAt(i));
		}
		
		String sanitized = sb.toString();
		if ("".equals(sanitized)) // case where all characters are invalid
			return "_";
		else if (!XMLChar.isNameStart(sanitized.codePointAt(0))) // case where tag has invalid start character
			return ("_" + sanitized);
		else return sanitized;
	}
}
