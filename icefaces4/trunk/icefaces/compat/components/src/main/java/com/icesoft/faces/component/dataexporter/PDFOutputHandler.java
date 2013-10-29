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

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class PDFOutputHandler extends OutputTypeHandler {
    protected String title;
    protected ArrayList headers;    // Index is column
    protected ArrayList footers;    // Index is column
    protected ArrayList rowsOfData; // Index is row, element is ArrayList of column data

    protected String orientation;   // landscape, portrait (default)
    protected String pageSize;      // default: A4
    protected String headerFont;    // Courier, Courier-Bold, Courier-Oblique, Courier-BoldOblique,
                                    //   Helvetica, Helvetica-Bold, Helvetica-Oblique, Helvetica-BoldOblique,
                                    //   Symbol, Times, Times-Roman (default), Times-Bold, Times-Italic, Times-BoldItalic
    protected String cellFont;      // default: Times-Roman
    protected int headerFontSize;   // default: 9
    protected int cellFontSize;     // default: 8

	public PDFOutputHandler(String path, String title) {
		super(path);
        this.title = title;
        this.mimeType = "application/pdf";
        this.headers = new ArrayList();
        this.footers = new ArrayList();
        this.rowsOfData = new ArrayList();

        this.orientation = "portrait";
        this.pageSize = "A4";
        this.headerFont = "Times-Roman";
        this.cellFont = "Times-Roman";
        this.headerFontSize = 9;
        this.cellFontSize = 8;

        if (!REFLECTION_LOADED) {
            throw new IllegalStateException("iText library not found, can not export dataTable to PDF");
        }
	}

    /**
     * The row indexing is zero based, from the perspective of the row data,
     * ignoring how many rows were used for the header
     */
	public void writeCell(Object output, int col, int row) {
        rowsOfData.ensureCapacity(row);
        while (row >= rowsOfData.size()) {
            rowsOfData.add(new ArrayList());
        }
        ArrayList rowData = (ArrayList) rowsOfData.get(row);

        rowData.ensureCapacity(col);
        while (col >= rowData.size()) {
            rowData.add(null);
        }
        rowData.set(col, output);
	}

	public void writeHeaderCell(String text, int col) {
        headers.ensureCapacity(col);
        while (col >= headers.size()) {
            headers.add(null);
        }
        headers.set(col, text);
	}

    /**
     * The row indexing is zero based, from the perspective of the row data,
     * ignoring how many rows were used for the header
     */
	public void writeFooterCell(Object output, int col, int row) {
        footers.ensureCapacity(col);
        while (col >= footers.size()) {
            footers.add(null);
        }
        footers.set(col, output);
	}

    public void flushFile() {
        try{
            FileOutputStream fos = new FileOutputStream(this.getFile());
            BufferedOutputStream bos = new BufferedOutputStream(fos,64*1014);
            printPDF(bos);
            bos.flush();
            fos.flush();
            bos.close();
            fos.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    protected void printPDF(OutputStream osPDF)
            throws Exception { // DocumentException, IOException
        //Initialize document
        Object document = new_Document();

        //Apply Document Properties
        applyDocumentProperties(document);

        Object writer = PdfWriter_getInstance(document, osPDF);

        //we open the document
        Document_open(document);

        PdfWriter_setOpenAction(writer);

        //Generate Contents

        //Add Header If provided
        printHeaderTitle(document,this.title);

        //Print Table Contents
        printTableData(document);

        Document_close(document);
    }

    protected void applyDocumentProperties(Object document) throws Exception { // DocumentException
        //Find Document Size and Orientation
        Object page = PageSize_getRectangle(this.pageSize);

        // Check for Page Orientation
        if(nvl(this.orientation).equalsIgnoreCase("LANDSCAPE")){
            Document_setPageSize(document, Rectangle_rotate(page));
        }else{
            Document_setPageSize(document, page);
        }
    }

    protected void printHeaderTitle(Object document, String headerText) throws Exception { // DocumentException
        if(this.title!=null && this.title.length()>0){
            Object font = FontFactory_getFont_BOLD(com_lowagie_text_FontFactory_TIMES_ROMAN, 14.0f);
            Object ck = new_Chunk(headerText, font);
            Object phrase = new_Phrase(ck);
            Object paragraph = new_Paragraph(phrase);
            Paragraph_setAlignment_ALIGN_CENTER(paragraph);
            Document_add(document, paragraph);
        }
    }

    protected void printTableData(Object document) throws Exception { // DocumentException
        try{
            //Find the number of Columns
            int length = calculateColumnCount();

            //Set table widths
            float[] tWidth =  new float[length];
            for(int wi=0;wi<length;wi++){
                //Calculate appropriate width for column
                tWidth[wi] = calculateColumnWidth(wi);
            }

            Object table = new_Table_init(length, tWidth);

            //Check Header Font and Font Size
            String headerFontProp = nvl(this.headerFont);
            //Check for Font Size
            float fHeaderSize = this.headerFontSize;
            //Apply Properties
            Object font = FontFactory_getFont_BOLD(headerFontProp, fHeaderSize);
            java.awt.Color backGroundColor = new java.awt.Color(225, 225, 225);

            //Add Table Header
            boolean wasHeader = printTableHeadersOrFooters(table, font, backGroundColor, this.headers, 0);

            //Print Table Contents
            int numContentRows = printTableContents(table, wasHeader);

            //Add Table Footer
            int footerRow = (wasHeader ? 1 : 0) + numContentRows;
            boolean wasFooter = printTableHeadersOrFooters(table, font, backGroundColor, this.footers, footerRow);

            //Add the table to the document
            Document_add(document, table);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * @param table
     * @param font
     * @param backGroundColor
     * @param headersOrFooters
     * @param row
     * @return If there was a header/footer row printed
     */
    protected boolean printTableHeadersOrFooters(
            Object table,
            Object font,
            java.awt.Color backGroundColor,
            ArrayList headersOrFooters,
            int row)
            throws Exception { // BadElementException
        for(int i=0;i<headersOrFooters.size();i++){
            String text = nvl((String)headersOrFooters.get(i));
            Table_add_new_Cell_BORDER_TOP_BOTTOM_RIGHT_LEFT_ALIGN_CENTER(
                table, row, i, text, font, backGroundColor);
        }
        return headersOrFooters.size() > 0;
    }

    /**
     * @param table
     * @return Number of rows of data added to the table
     */
    protected int printTableContents(Object table, boolean wasHeader) throws Exception { // DocumentException
        // Check Header Font and Font Size
        String cellFontProp = nvl(this.cellFont);
        //Check for Font Size
        float fCellSize = this.cellFontSize;

        Object font = FontFactory_getFont(cellFontProp, fCellSize);
        java.awt.Color backGroundColor = java.awt.Color.white;

        int headerRowOffset = wasHeader ? 1 : 0;
        //Add Table Data
        int numRows = this.rowsOfData.size();
        for(int j=0;j<numRows;j++){
            ArrayList rowData=(ArrayList)this.rowsOfData.get(j);
            for(int i=0;i<rowData.size();i++){
                String cellText = nvl((String)rowData.get(i));
                Table_add_new_Cell_BORDER_TOP_BOTTOM_RIGHT_LEFT_ALIGN_CENTER(
                    table, j+headerRowOffset, i, cellText, font, backGroundColor);
            }
        }

        return numRows;
    }

    protected int calculateColumnCount() {
        int maxColumns = 0;
        maxColumns = Math.max(maxColumns, headers.size());
        maxColumns = Math.max(maxColumns, footers.size());
        for (int i = 0; i < rowsOfData.size(); i++) {
            ArrayList rowData = (ArrayList) rowsOfData.get(i);
            maxColumns = Math.max(maxColumns, rowData.size());
        }
        return maxColumns;
    }

    /**
     * @return
     * @throws Exception
     */
    protected float calculateColumnWidth(int index)throws Exception{
        float defaultWidth = 1.2f;
        //Calculate width of the column
        //Find the largest length associated to a cell column
        String headerText = nvl((String)this.headers.get(index));
        int iTxtLength = headerText.length();
        for(int i=0;i<this.rowsOfData.size();i++){
            ArrayList rowData = (ArrayList)rowsOfData.get(i);
            String cellValue = nvl((String)rowData.get(index));
            iTxtLength = Math.max(iTxtLength, cellValue.length());
        }
        return returnPDFWidth(iTxtLength);
    }

    /**
     * @param colLength
     * @return
     */
    protected float returnPDFWidth(int colLength){
        float retValue = 1.2f;
        if(colLength<=5){
            retValue = 0.5f;
        }else
            if(colLength<=7){
                retValue = 0.7f;
            }else
            if(colLength<=10){
                retValue = 0.8f;
            }else
                if(colLength<=15){
                    retValue = 1.0f;
                }else
                    if(colLength<=20){
                        retValue = 1.2f;
                    }else
                        if(colLength<=30){
                            retValue = 1.5f;
                        }else
                            if(colLength<=50){
                                retValue = 1.8f;
                            }else{
                                retValue = 2.2f;
                            }
        return retValue;
    }

    protected static String nvl(String stringValue){
        String returnValue = "";
        if(stringValue!=null){
            returnValue = stringValue.trim();
        }
        return returnValue;
    }


    private static final boolean REFLECTION_LOADED;

    private static Class com_lowagie_text_Cell;
    private static Class com_lowagie_text_Chunk;
    private static Class com_lowagie_text_Document;
    private static Class com_lowagie_text_Element;
    private static Class com_lowagie_text_Font;
    private static Class com_lowagie_text_FontFactory;
    private static Class com_lowagie_text_PageSize;
    private static Class com_lowagie_text_Paragraph;
    private static Class com_lowagie_text_Phrase;
    private static Class com_lowagie_text_Rectangle;
    private static Class com_lowagie_text_Table;
    private static Class com_lowagie_text_pdf_PdfAction;
    private static Class com_lowagie_text_pdf_PdfDestination;
    private static Class com_lowagie_text_pdf_PdfWriter;

    private static Constructor com_lowagie_text_Document_ctor;
    private static Method com_lowagie_text_Document_open;
    private static Method com_lowagie_text_Document_close;
    private static Method com_lowagie_text_Document_setPageSize;
    private static Method com_lowagie_text_Document_add;
    private static Method com_lowagie_text_pdf_PdfWriter_getInstance;
    private static Method com_lowagie_text_pdf_PdfWriter_setOpenAction;
    private static int com_lowagie_text_pdf_PdfDestination_XYZ;
    private static Constructor com_lowagie_text_pdf_PdfDestination_ctor;
    private static Method com_lowagie_text_pdf_PdfAction_gotoLocalPage;
    private static Method com_lowagie_text_PageSize_getRectangle;
    private static Method com_lowagie_text_Rectangle_rotate;
    private static int com_lowagie_text_Rectangle_ALIGN_CENTER;
    private static Method com_lowagie_text_FontFactory_getFont;
    private static Method com_lowagie_text_FontFactory_getFont_BOLD;
    private static String com_lowagie_text_FontFactory_TIMES_ROMAN;
    private static int com_lowagie_text_Font_BOLD;
    private static Constructor com_lowagie_text_Chunk_ctor;
    private static Constructor com_lowagie_text_Phrase_ctor_Chunk;
    private static Constructor com_lowagie_text_Phrase_ctor_SF;
    private static Constructor com_lowagie_text_Paragraph_ctor;
    private static Method com_lowagie_text_Paragraph_setAlignment_ALIGN_CENTER;
    private static Constructor com_lowagie_text_Table_ctor;
    private static Method com_lowagie_text_Table_setWidth;
    private static Method com_lowagie_text_Table_setBorderWidth;
    private static Method com_lowagie_text_Table_setPadding;
    private static Method com_lowagie_text_Table_setCellsFitPage;
    private static Method com_lowagie_text_Table_setWidths;
    private static Method com_lowagie_text_Table_addCell;
    private static int com_lowagie_text_Cell_TOP_BOTTOM_RIGHT_LEFT;
    private static int com_lowagie_text_Cell_ALIGN_CENTER;
    private static Constructor com_lowagie_text_Cell_ctor;
    private static Method com_lowagie_text_Cell_setBorder;
    private static Method com_lowagie_text_Cell_setHorizontalAlignment;
    private static Method com_lowagie_text_Cell_setBackgroundColor;

    static {
        boolean loaded = false;
        try {
            com_lowagie_text_Cell = Class.forName("com.lowagie.text.Cell");
            com_lowagie_text_Chunk = Class.forName("com.lowagie.text.Chunk");
            com_lowagie_text_Document = Class.forName("com.lowagie.text.Document");
            com_lowagie_text_Element = Class.forName("com.lowagie.text.Element");
            com_lowagie_text_Font = Class.forName("com.lowagie.text.Font");
            com_lowagie_text_FontFactory = Class.forName("com.lowagie.text.FontFactory");
            com_lowagie_text_PageSize = Class.forName("com.lowagie.text.PageSize");
            com_lowagie_text_Paragraph = Class.forName("com.lowagie.text.Paragraph");
            com_lowagie_text_Phrase = Class.forName("com.lowagie.text.Phrase");
            com_lowagie_text_Rectangle = Class.forName("com.lowagie.text.Rectangle");
            com_lowagie_text_Table = Class.forName("com.lowagie.text.Table");
            com_lowagie_text_pdf_PdfAction = Class.forName("com.lowagie.text.pdf.PdfAction");
            com_lowagie_text_pdf_PdfDestination = Class.forName("com.lowagie.text.pdf.PdfDestination");
            com_lowagie_text_pdf_PdfWriter = Class.forName("com.lowagie.text.pdf.PdfWriter");

            com_lowagie_text_Document_ctor = com_lowagie_text_Document.getConstructor(new Class[0]);
            com_lowagie_text_Document_open = com_lowagie_text_Document.getMethod("open", new Class[0]);
            com_lowagie_text_Document_close = com_lowagie_text_Document.getMethod("close", new Class[0]);
            com_lowagie_text_Document_setPageSize = com_lowagie_text_Document.getMethod(
                "setPageSize", new Class[] {com_lowagie_text_Rectangle});
            com_lowagie_text_Document_add = com_lowagie_text_Document.getMethod(
                "add", new Class[] {com_lowagie_text_Element});

            com_lowagie_text_pdf_PdfWriter_getInstance = com_lowagie_text_pdf_PdfWriter.getMethod(
                "getInstance", new Class[] {com_lowagie_text_Document, OutputStream.class});
            com_lowagie_text_pdf_PdfWriter_setOpenAction = com_lowagie_text_pdf_PdfWriter.getMethod(
                "setOpenAction", new Class[] {com_lowagie_text_pdf_PdfAction});

            com_lowagie_text_pdf_PdfDestination_XYZ = ((Integer)com_lowagie_text_pdf_PdfDestination.getField("XYZ").get(null)).intValue();
            com_lowagie_text_pdf_PdfDestination_ctor = com_lowagie_text_pdf_PdfDestination.getConstructor(
                new Class[] {Integer.TYPE, Float.TYPE, Float.TYPE, Float.TYPE});
            com_lowagie_text_pdf_PdfAction_gotoLocalPage = com_lowagie_text_pdf_PdfAction.getMethod(
                "gotoLocalPage", new Class[] {Integer.TYPE, com_lowagie_text_pdf_PdfDestination, com_lowagie_text_pdf_PdfWriter});
            com_lowagie_text_PageSize_getRectangle = com_lowagie_text_PageSize.getMethod(
                "getRectangle", new Class[] {String.class});
            com_lowagie_text_Rectangle_rotate = com_lowagie_text_Rectangle.getMethod("rotate", new Class[0]);
            com_lowagie_text_Rectangle_ALIGN_CENTER = ((Integer)com_lowagie_text_Rectangle.getField("ALIGN_CENTER").get(null)).intValue();

            com_lowagie_text_FontFactory_getFont = com_lowagie_text_FontFactory.getMethod(
                "getFont", new Class[] {String.class, Float.TYPE});
            com_lowagie_text_FontFactory_getFont_BOLD = com_lowagie_text_FontFactory.getMethod(
                "getFont", new Class[] {String.class, Float.TYPE, Integer.TYPE});
            com_lowagie_text_FontFactory_TIMES_ROMAN = com_lowagie_text_FontFactory.getField("TIMES_ROMAN").get(null).toString();
            com_lowagie_text_Font_BOLD = ((Integer)com_lowagie_text_Font.getField("BOLD").get(null)).intValue();

            com_lowagie_text_Chunk_ctor = com_lowagie_text_Chunk.getConstructor(new Class[] {String.class, com_lowagie_text_Font});
            com_lowagie_text_Phrase_ctor_Chunk = com_lowagie_text_Phrase.getConstructor(new Class[] {com_lowagie_text_Chunk});
            com_lowagie_text_Phrase_ctor_SF = com_lowagie_text_Phrase.getConstructor(new Class[] {String.class, com_lowagie_text_Font});
            com_lowagie_text_Paragraph_ctor = com_lowagie_text_Paragraph.getConstructor(new Class[] {com_lowagie_text_Phrase});
            com_lowagie_text_Paragraph_setAlignment_ALIGN_CENTER = com_lowagie_text_Paragraph.getMethod(
                "setAlignment", new Class[] {Integer.TYPE});

            com_lowagie_text_Table_ctor = com_lowagie_text_Table.getConstructor(new Class[] {Integer.TYPE});
            com_lowagie_text_Table_setWidth = com_lowagie_text_Table.getMethod("setWidth", new Class[] {Float.TYPE});
            com_lowagie_text_Table_setBorderWidth = com_lowagie_text_Table.getMethod("setBorderWidth", new Class[] {Float.TYPE});
            com_lowagie_text_Table_setPadding = com_lowagie_text_Table.getMethod("setPadding", new Class[] {Float.TYPE});
            com_lowagie_text_Table_setCellsFitPage = com_lowagie_text_Table.getMethod("setCellsFitPage", new Class[] {Boolean.TYPE});
            com_lowagie_text_Table_setWidths = com_lowagie_text_Table.getMethod("setWidths", new Class[] {Class.forName("[F")});
            com_lowagie_text_Table_addCell = com_lowagie_text_Table.getMethod("addCell", new Class[] {com_lowagie_text_Cell, Integer.TYPE, Integer.TYPE});

            Field Cell_TOP = com_lowagie_text_Cell.getField("TOP");
            Field Cell_BOTTOM = com_lowagie_text_Cell.getField("BOTTOM");
            Field Cell_RIGHT = com_lowagie_text_Cell.getField("RIGHT");
            Field Cell_LEFT = com_lowagie_text_Cell.getField("LEFT");
            com_lowagie_text_Cell_TOP_BOTTOM_RIGHT_LEFT = ((Integer)Cell_TOP.get(null)).intValue() |
                ((Integer)Cell_BOTTOM.get(null)).intValue() | ((Integer)Cell_RIGHT.get(null)).intValue() |
                ((Integer)Cell_LEFT.get(null)).intValue();
            com_lowagie_text_Cell_ALIGN_CENTER = ((Integer)com_lowagie_text_Cell.getField("ALIGN_CENTER").get(null)).intValue();

            com_lowagie_text_Cell_ctor = com_lowagie_text_Cell.getConstructor(new Class[] {com_lowagie_text_Element});
            com_lowagie_text_Cell_setBorder = com_lowagie_text_Cell.getMethod("setBorder", new Class[] {Integer.TYPE});
            com_lowagie_text_Cell_setHorizontalAlignment = com_lowagie_text_Cell.getMethod("setHorizontalAlignment", new Class[] {Integer.TYPE});
            com_lowagie_text_Cell_setBackgroundColor = com_lowagie_text_Cell.getMethod("setBackgroundColor", new Class[] {java.awt.Color.class});

            loaded = true;
        }
        catch(Exception e) {
            //TODO log
            System.out.println("Problem with PDFOutputHandler iText reflection: " + e);
        }
        REFLECTION_LOADED = loaded;
    }

    private static Object new_Document() {
        try {
            return com_lowagie_text_Document_ctor.newInstance(new Object[0]);
        } catch (InvocationTargetException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        return null;
    }

    private static void Document_open(Object document) {
        try {
            com_lowagie_text_Document_open.invoke(document, new Object[0]);
        } catch (InvocationTargetException e) {
        } catch (IllegalAccessException e) {
        }
    }

    private static void Document_close(Object document) {
        try {
            com_lowagie_text_Document_close.invoke(document, new Object[0]);
        } catch (InvocationTargetException e) {
        } catch (IllegalAccessException e) {
        }
    }

    private void Document_setPageSize(Object document, Object page) {
        try {
            com_lowagie_text_Document_setPageSize.invoke(document, new Object[] {page});
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
    }

    private void Document_add(Object document, Object element) {
        try {
            com_lowagie_text_Document_add.invoke(document, new Object[] {element});
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
    }

    private static Object PdfWriter_getInstance(Object document, OutputStream os) {
        try {
            return com_lowagie_text_pdf_PdfWriter_getInstance.invoke(null, new Object[] {document, os});
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return null;
    }

    private static void PdfWriter_setOpenAction(Object writer) {
        //writer.setOpenAction(PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, 0, 10000, 1), writer));
        try {
            Object destination = com_lowagie_text_pdf_PdfDestination_ctor.newInstance(
                new Object[] {
                    new Integer(com_lowagie_text_pdf_PdfDestination_XYZ),
                    new Float(0.0f), new Float(10000.0f), new Float(1.0f)});
            Object action = com_lowagie_text_pdf_PdfAction_gotoLocalPage.invoke(
                null, new Object[] {new Integer(1), destination, writer});
            com_lowagie_text_pdf_PdfWriter_setOpenAction.invoke(writer, new Object[] {action});
        } catch (InvocationTargetException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
    }

    private static Object PageSize_getRectangle(String pageSize) {
        try {
            return com_lowagie_text_PageSize_getRectangle.invoke(null, new Object[] {pageSize});
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return null;
    }

    private static Object Rectangle_rotate(Object rectangle) {
        try {
            return com_lowagie_text_Rectangle_rotate.invoke(rectangle, new Object[0]);
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return null;
    }

    private static Object FontFactory_getFont(String fontName, float size) {
        try {
            return com_lowagie_text_FontFactory_getFont.invoke(null, new Object[] {
                fontName,
                new Float(size)
            });
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return null;
    }

    private static Object FontFactory_getFont_BOLD(String fontName, float size) {
        try {
            return com_lowagie_text_FontFactory_getFont_BOLD.invoke(null, new Object[] {
                fontName,
                new Float(size),
                new Integer(com_lowagie_text_Font_BOLD)
            });
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return null;
    }

    private static Object new_Chunk(String text, Object font) {
        try {
            return com_lowagie_text_Chunk_ctor.newInstance(new Object[] {text, font});
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return null;
    }

    private static Object new_Phrase(Object chunk) {
        try {
            return com_lowagie_text_Phrase_ctor_Chunk.newInstance(new Object[] {chunk});
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return null;
    }

    private static Object new_Paragraph(Object phrase) {
        try {
            return com_lowagie_text_Paragraph_ctor.newInstance(new Object[] {phrase});
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return null;
    }

    private static void Paragraph_setAlignment_ALIGN_CENTER(Object paragraph) {
        try {
            com_lowagie_text_Paragraph_setAlignment_ALIGN_CENTER.invoke(paragraph, new Object[] {
                new Integer(com_lowagie_text_Rectangle_ALIGN_CENTER)
            });
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
    }

    private static Object new_Table_init(int length, float[] tWidth) throws Exception {
        //Table table = new Table(length);
        //table.setWidth(107);
        //table.setBorderWidth(0);
        //table.setPadding(2);
        //table.setCellsFitPage(true);
        //table.setWidths(tWidth);

        Object table = null;
        try {
            table = com_lowagie_text_Table_ctor.newInstance(new Object[] {new Integer(length)});
            com_lowagie_text_Table_setWidth.invoke(table, new Object[] {new Float(107.0f)});
            com_lowagie_text_Table_setBorderWidth.invoke(table, new Object[] {new Float(0.0f)});
            com_lowagie_text_Table_setPadding.invoke(table, new Object[] {new Float(2.0f)});
            com_lowagie_text_Table_setCellsFitPage.invoke(table, new Object[] {Boolean.TRUE});
            com_lowagie_text_Table_setWidths.invoke(table, new Object[] {tWidth});
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return table;
    }

    private static void Table_add_new_Cell_BORDER_TOP_BOTTOM_RIGHT_LEFT_ALIGN_CENTER(
            Object table, int row, int column,
            String text, Object font, java.awt.Color backgroundColor)
            throws Exception { // BadElementException
        if (text == null) {
            text = "";
        }
        //Cell cell = new Cell(new Phrase(text, font));
        //cell.setBorder(border);
        //cell.setHorizontalAlignment(align);
        //cell.setBackgroundColor(backGroundColor);
        //table.addCell(cell, row, column);
        try {
            Object phrase = com_lowagie_text_Phrase_ctor_SF.newInstance(new Object[] {text, font});
            Object cell = com_lowagie_text_Cell_ctor.newInstance(new Object[] {phrase});
            com_lowagie_text_Cell_setBorder.invoke(cell, new Object[] {new Integer(com_lowagie_text_Cell_TOP_BOTTOM_RIGHT_LEFT)});
            com_lowagie_text_Cell_setHorizontalAlignment.invoke(cell, new Object[] {new Integer(com_lowagie_text_Cell_ALIGN_CENTER)});
            com_lowagie_text_Cell_setBackgroundColor.invoke(cell, new Object[] {backgroundColor});
            com_lowagie_text_Table_addCell.invoke(table, new Object[] {cell, new Integer(row), new Integer(column)});
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
    }
}
