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
import java.util.List;
import java.util.ArrayList;

import javax.el.MethodExpression;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import org.icefaces.ace.component.datatable.DataTable;
import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.component.columngroup.ColumnGroup;
import org.icefaces.ace.model.table.RowState;
import org.icefaces.ace.model.table.RowStateMap;
import org.icefaces.ace.model.table.TreeDataModel;

import java.io.ByteArrayOutputStream;

public class ExcelExporter extends Exporter {

    @Override
	public String export(FacesContext facesContext, DataExporter component, DataTable table) throws IOException {
		setUp(component, table);
    	Workbook wb = new HSSFWorkbook();
    	Sheet sheet = wb.createSheet();
    	List<UIColumn> columns = getColumnsToExport(table, excludeColumns);
    	int numberOfColumns = columns.size();
    	if (preProcessor != null) {
    		preProcessor.invoke(facesContext.getELContext(), new Object[]{wb});
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
    	int rowsToExport = pageOnly ? (first + table.getRows()) : rowCount;
		rowsToExport = rowsToExport > rowCount ? rowCount : rowsToExport;
    	int sheetRowIndex = 0;

        if (includeHeaders) {
			ColumnGroup columnGroup = getColumnGroupHeader(table);
			if (columnGroup != null) {
				List<org.icefaces.ace.component.row.Row> rows = getRows(columnGroup);
				for (org.icefaces.ace.component.row.Row row : rows) {
					List<UIColumn> rowColumns = getRowColumnsToExport(row, table, excludeColumns);
					addFacetColumns(sheet, rowColumns, ColumnType.HEADER, sheetRowIndex++);
				}
			} else {
				sheetRowIndex = 1;
				addFacetColumns(sheet, columns, ColumnType.HEADER, 0);
			}
		}

		RowStateMap rowStateMap = table.getStateMap();

		String rowIndexVar = table.getRowIndexVar();
		rowIndexVar = rowIndexVar == null ? "" : rowIndexVar;
    	for (int i = first; i < rowsToExport; i++) {
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
				List<org.icefaces.ace.component.row.Row> leadingRows = table.getConditionalRows(i, true);
				for (org.icefaces.ace.component.row.Row r : leadingRows) {
					Row row = sheet.createRow(sheetRowIndex++);
					exportConditionalRow(r, row);
				}
				
				Row row = sheet.createRow(sheetRowIndex++);
				for (int j = 0; j < numberOfColumns; j++) {
					addColumnValue(row, columns.get(j).getChildren(), j);
				}
				if (hasRowExpansion) {
					sheetRowIndex = exportChildRows(facesContext, rootModel, rowStateMap, table, columns, "" + i, sheet, sheetRowIndex, numberOfColumns);
				}
				
				// 'after' conditional rows
				List<org.icefaces.ace.component.row.Row> tailingRows = table.getConditionalRows(i, false);
				for (org.icefaces.ace.component.row.Row r : tailingRows) {
					row = sheet.createRow(sheetRowIndex++);
					exportConditionalRow(r, row);
				}
			}
		}

        if (hasColumnFooter(columns) && includeFooters) {
            addFacetColumns(sheet, columns, ColumnType.FOOTER, sheetRowIndex++);
        }
    	
    	table.setRowIndex(-1);
    	
    	if (postProcessor != null) {
    		postProcessor.invoke(facesContext.getELContext(), new Object[]{wb});
    	}
    	
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
		wb.write(baos);
		
		byte[] bytes = baos.toByteArray();
		
		return registerResource(bytes, filename + ".xls", "application/vnd.ms-excel");
	}
	
	protected int exportChildRows(FacesContext context, TreeDataModel rootModel, RowStateMap rowStateMap,
		DataTable table, List<UIColumn> columns, String rootIndex, Sheet sheet, int sheetRowIndex, int numberOfColumns) throws IOException {		
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
				Row row = sheet.createRow(sheetRowIndex++);
				for (int j = 0; j < numberOfColumns; j++) {
					addColumnValue(row, columns.get(j).getChildren(), j);
				}
				
				RowState rowState = rowStateMap.get(rootModel.getRowData());
				if (rowState.isExpanded() || !expandedOnly) {
					
					// recurse
					sheetRowIndex = exportChildRows(context, rootModel, rowStateMap, table, columns, rootIndex + "." + rowIndex, sheet, sheetRowIndex, numberOfColumns);
					
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
		return sheetRowIndex;
	}
	
	protected void exportConditionalRow(org.icefaces.ace.component.row.Row r, Row row) throws IOException {
		List<UIComponent> children = r.getChildren();
		List<UIColumn> rowColumns = new ArrayList<UIColumn>(children.size());
		for (UIComponent kid : children) {
			if (kid instanceof Column) {
				Column c = (Column) kid;
				int colspan = c.getColspan();
				for (int i = 0; i < colspan; i++) rowColumns.add(c);
			}
		}
		int numberOfColumns = rowColumns.size();
		for (int i = 0; i < numberOfColumns; i++) {
			addColumnValue(row, rowColumns.get(i).getChildren(), i);
		}
	}
	
	protected void addFacetColumns(Sheet sheet, List<UIColumn> columns, ColumnType columnType, int rowIndex) {
        Row rowHeader = sheet.createRow(rowIndex);

        for (int i = 0; i < columns.size(); i++) {
            UIColumn uiColumn = (UIColumn) columns.get(i);
			UIComponent facet = uiColumn.getFacet(columnType.facet());
			
            if (facet != null) {
				addColumnValue(rowHeader, facet, i);
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
				Cell cell = rowHeader.createCell(i);
				cell.setCellValue(new HSSFRichTextString(value));
			}
        }
    }
	
    protected void addColumnValue(Row rowHeader, UIComponent component, int index) {
        Cell cell = rowHeader.createCell(index);
        String value = component == null ? "" : exportValue(FacesContext.getCurrentInstance(), component);

        cell.setCellValue(new HSSFRichTextString(value));
    }
    
    protected void addColumnValue(Row rowHeader, List<UIComponent> components, int index) {
        Cell cell = rowHeader.createCell(index);
        StringBuilder builder = new StringBuilder();
        
        for (UIComponent component : components) {
        	if (component.isRendered()) {
                String value = exportValue(FacesContext.getCurrentInstance(), component);
                
                if (value != null)
                	builder.append(value);
            }
		}  
        
        cell.setCellValue(new HSSFRichTextString(builder.toString()));
    }
}
