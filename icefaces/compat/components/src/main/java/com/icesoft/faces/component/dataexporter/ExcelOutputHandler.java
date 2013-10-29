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

package com.icesoft.faces.component.dataexporter;

import java.io.IOException;
import java.util.Date;

import javax.faces.context.FacesContext;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Blank;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelOutputHandler extends OutputTypeHandler{
	
	WritableSheet sheet = null;
	WritableWorkbook workbook = null;
	

	public ExcelOutputHandler(String path, FacesContext fc, String title) {
		super(path);
		try{
			WorkbookSettings settings = new WorkbookSettings();
			settings.setLocale(fc.getViewRoot().getLocale());
			workbook = Workbook.createWorkbook(super.getFile());
			sheet = workbook.createSheet(title, 0);
			
			this.mimeType = "application/vnd.ms-excel";
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
		
	}

	
	public void flushFile() {
		try{
			workbook.write();
			workbook.close();
		}
		catch( WriteException ioe){
			ioe.printStackTrace();
		}
		catch( IOException ioe){
			ioe.printStackTrace();
		}
	}

    /**
     * The row indexing is zero based, from the perspective of the row data,
     * ignoring how many rows were used for the header 
     */
	public void writeCell(Object output, int col, int row) {
        WritableCellFormat format = getCellFormat();
        WritableCell cell = deriveCellFromObject(output, col, row + 1, format);
        addCell(cell);
	}

	public void writeHeaderCell(String text, int col) {
        WritableCellFormat format = getHeaderCellFormat();
        WritableCell cell = deriveCellFromObject(text, col, 0, format);
        addCell(cell);
	}
    
    /**
     * The row indexing is zero based, from the perspective of the row data,
     * ignoring how many rows were used for the header 
     */
	public void writeFooterCell(Object output, int col, int row) {
        WritableCellFormat format = getFooterCellFormat();
        WritableCell cell = deriveCellFromObject(output, col, row + 1, format);
        addCell(cell);
	}

    /**
     * Unlike the other methods, row is specific to the whole worksheet,
     * not just the data rows
     */
    protected WritableCell deriveCellFromObject(Object output, int col, int row, WritableCellFormat format) {
		WritableCell cell = null;
        if (output == null) {
            cell = new Blank(col, row);
        }
		else if (output instanceof String) {
            cell = new Label(col, row, (String)output);
		}
		else if (output instanceof Double) {
            cell = new Number(col, row, ((Double)output).doubleValue());
		}
		else if (output instanceof Date) {
            cell = new DateTime(col, row, (Date) output);
		}
        else if (output instanceof Boolean) {
            cell = new jxl.write.Boolean(col, row, ((Boolean)output).booleanValue());
        }
        else {
            cell = new Label(col, row + 1, (String)output);
        }
        
        if (cell != null && format != null) {
            cell.setCellFormat(format);
        }
        
        return cell;
    }
    
    protected void addCell(WritableCell cell) {
        try {
			sheet.addCell(cell);
		}
		catch(WriteException we){
            System.out.println("Could not write excel cell");
			we.printStackTrace();
		}
    }
    
    protected WritableCellFormat getHeaderCellFormat() {
        WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        WritableCellFormat format = new WritableCellFormat(font);
        return format;
    }
    
    protected WritableCellFormat getCellFormat() {
        return null;
    }
    
    protected WritableCellFormat getFooterCellFormat() {
        WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        WritableCellFormat format = new WritableCellFormat(font);
        return format;
    }
}
