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

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.commandsortheader.CommandSortHeader;
import com.icesoft.faces.component.ext.RowSelector;
import com.icesoft.faces.component.ext.UIColumn;
import com.icesoft.faces.component.ext.UIColumns;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.component.outputresource.OutputResource;
import com.icesoft.faces.context.FileResource;
import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.faces.util.CoreUtils;
import com.icesoft.util.CoreComponentUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icefaces.resources.BrowserType;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@ICEResourceDependencies({
	@ICEResourceDependency(name="icefaces-compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={}),
	@ICEResourceDependency(name="compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={})
})
public class DataExporter extends OutputResource {
    public static final String COMPONENT_FAMILY = "com.icesoft.faces.DataExporter";
	public static final String COMPONENT_TYPE = "com.icesoft.faces.DataExporter";
	public static final String DEFAULT_RENDERER_TYPE = "com.icesoft.faces.DataExporterRenderer";
	private final Log log = LogFactory.getLog(DataExporter.class);
	
    private static final OutputTypeHandler NoopOutputHandler = new OutputTypeHandler("no-data") {
        public void writeHeaderCell(String text, int col) {
        }

        public void writeCell(Object output, int col, int row) {
        }

        public void flushFile() {
        }
    };

	private boolean readyToExport = false;

	private String _for;
	private String type;
	private String clickToCreateFileText;
	private String _origType;
	private transient OutputTypeHandler outputTypeHandler;
	private String _origFor;
	private transient OutputTypeHandler _origOutputTypeHandler;
	private transient int _origDataModelHash = 0;
	public final static String EXCEL_TYPE = "excel";
	public final static String CSV_TYPE = "csv";
    public final static String PDF_TYPE = "pdf";
	private Boolean ignorePagination;
    private Boolean renderLabelAsButton;
    private String styleClass;
    private String includeColumns;
    private int rows = Integer.MIN_VALUE;
    private int first = Integer.MIN_VALUE;
    private String popupBlockerLabel;
	public DataExporter() {
	}
	
	   /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.UIComponent#getFamily()
     */
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    public String getComponentType() {
        return COMPONENT_TYPE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.UIComponent#getRendererType()
     */
    public String getRendererType() {
        return DEFAULT_RENDERER_TYPE;
    }
    
	public UIData getUIData() {
		String forStr = getFor();
		UIData forComp = (UIData) CoreComponentUtils.findComponent(forStr, this);

		if (forComp == null) {
			throw new IllegalArgumentException(
					"could not find UIData referenced by attribute @for = '"
							+ forStr + "'");
		} else if (!(forComp instanceof UIData)) {
			throw new IllegalArgumentException(
					"uiComponent referenced by attribute @for = '" + forStr
							+ "' must be of type " + UIData.class.getName()
							+ ", not type " + forComp.getClass().getName());
		}
		//compare with cached DataModel to check for updates
		if( _origDataModelHash != 0 && _origDataModelHash != forComp.getValue().hashCode()){
			reset();
		}
		if (!isIgnorePagination() && ((first != Integer.MIN_VALUE && first != forComp.getFirst()) ||
		        (rows != Integer.MIN_VALUE && rows != forComp.getRows()))  ) {
		    reset(); 
		}
		

		Object value = forComp.getValue();
		if (null != value) {
		    _origDataModelHash = forComp.getValue().hashCode();
		}
		return forComp;
	}
	
	private void reset(){
		this.readyToExport = false;
		this.resource = null;
	}
	
	public String getFor() {
		if (_for != null) {
			if( !_for.equals(this._origFor))
				reset();
			this._origFor = _for;
			return _for;
		}
		ValueBinding vb = getValueBinding("for");
		String newFor = null;
		if( vb != null ){
			newFor = (String) vb.getValue(getFacesContext());
			if( newFor != null && !newFor.equals(this._origFor))
				reset();
			this._origFor = newFor;
		}
		
		return newFor;
	}

	public void setFor(String forValue) {
		if( forValue != null && !forValue.equals(_for))
			this.resource = null;
		_for = forValue;
	}
	
	public String getType(){
		if (type != null) {
			if( !type.equals(this._origType))
				reset();
			this._origType = type;
			return type;
		}
		ValueBinding vb = getValueBinding("type");
		String newType = null;
		if( vb != null ){
			newType = (String) vb.getValue(getFacesContext());
			if( newType != null && !newType.equals(this._origType))
				reset();
			this._origType = newType;
		}
		return newType;
	}
	
	public void setType(String type){
		if( type != null && !type.equals(this.type))
			reset();
		this.type = type;
	}

	public boolean isReadyToExport() {
		return readyToExport;
	}

	public void setReadyToExport(boolean readyToExport) {
		this.readyToExport = readyToExport;
	}
	
	/**
	 * @deprecated
	 */
	public String getClickToCreateFileText() {
		if (this.clickToCreateFileText != null) {
			return clickToCreateFileText;
		}
		ValueBinding vb = getValueBinding("clickToCreateFileText");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

     /**
     * @deprecated
     */
	public void setClickToCreateFileText(String clickToCreateFileText) {
		this.clickToCreateFileText = clickToCreateFileText;
	}

	public OutputTypeHandler getOutputTypeHandler() {
		ValueBinding vb = getValueBinding("outputTypeHandler");
		OutputTypeHandler newOutputHandler = null;
		if( vb != null ){
			newOutputHandler = (OutputTypeHandler) vb.getValue(getFacesContext());
			if( newOutputHandler != null && newOutputHandler != this._origOutputTypeHandler)
				reset();
			this._origOutputTypeHandler = newOutputHandler;
		}
		return newOutputHandler;
	}

	public void setOutputTypeHandler(OutputTypeHandler outputTypeHandler) {
		if( outputTypeHandler != null && outputTypeHandler != this.outputTypeHandler)
			reset();
		this.outputTypeHandler = outputTypeHandler;
	}
	
    private transient Object values[];
    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.StateHolder#saveState(javax.faces.context.FacesContext)
     */
    public Object saveState(FacesContext context) {

        if(values == null){
            values = new Object[14];
        }
        values[0] = super.saveState(context);
        values[1] = _for;
        values[2] = type;
        values[3] = clickToCreateFileText;
        values[4] = readyToExport? Boolean.TRUE : Boolean.FALSE;
        values[5] = _origType;
        values[6] = _origFor;
        values[7] = ignorePagination; 
        values[8] = renderLabelAsButton;     
        values[9] = styleClass;
        values[10] = includeColumns;         
        values[11] = new Integer(rows); 
        values[12] = new Integer(first); 
        values[13] = popupBlockerLabel;
        return ((Object) (values));
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.StateHolder#restoreState(javax.faces.context.FacesContext,
     *      java.lang.Object)
     */
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _for = (String) values[1];
        type = (String) values[2];
        clickToCreateFileText = (String) values[3];
        readyToExport = ((Boolean) values[4]).booleanValue();        
        _origType = (String) values[5];
        _origFor = (String)values[6];
        ignorePagination = (Boolean)values[7]; 
        renderLabelAsButton = (Boolean)values[8];  
        styleClass = (String)values[9];  
        includeColumns = (String)values[10];    
        rows = ((Integer)values[11]).intValue(); 
        first = ((Integer)values[12]).intValue();  
        popupBlockerLabel = (String)values[13];         
    }
    
    public String getLabel() {
        String label = super.getLabel();
        if (label == null) {
            if (resource instanceof FileResource)
            label = ((FileResource)resource).getFile().getName();
        }
        return label;
    }
	
    public void broadcast(FacesEvent event)
    throws AbortProcessingException {
        super.broadcast(event);
        if (event != null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String type = getType();
            UIData uiData = getUIData();
			File output = createFile(facesContext, type, uiData);
			setResource(new FileResource(output));
			getResource();
			JavascriptContext.addJavascriptCall(facesContext, "Ice.DataExporters['" + getClientId(facesContext) + "'].url('" + getPath() + "');");
        }
    }    

    private File createFile(FacesContext fc, String type, UIData uiData) {
        OutputTypeHandler outputHandler = null;
        String path = CoreUtils.getRealPath(fc, "/export"); 
        File exportDir = new File(path);
        if (!exportDir.exists())
            exportDir.mkdirs();
        String pathWithoutExt = path + "/export_"
                + new Date().getTime();

        if (getOutputTypeHandler() != null)
            outputHandler = getOutputTypeHandler();
        else if (DataExporter.EXCEL_TYPE.equals(getType())) {
            outputHandler = new ExcelOutputHandler(pathWithoutExt + ".xls",
                    fc, uiData.getId());
        } else if (DataExporter.CSV_TYPE.equals(getType())) {
            outputHandler = new CSVOutputHandler(pathWithoutExt + ".csv");
        } else if (DataExporter.PDF_TYPE.equals(getType())) {
            outputHandler = new PDFOutputHandler(pathWithoutExt + ".pdf",
                    uiData.getId());
        } else {
            outputHandler = NoopOutputHandler;
        }
        renderToHandler(outputHandler, uiData, fc);
        setMimeType(outputHandler.getMimeType());

        return outputHandler.getFile();
    }

    private String encodeParentAndChildrenAsString(FacesContext fc,
            UIComponent uic) {
        StringBuffer str = new StringBuffer();
        if (uic instanceof CommandSortHeader) {
            if (uic.getChildCount() > 0) {
                Iterator iter = uic.getChildren().iterator();
                while (iter.hasNext()) {
                    UIComponent child = (UIComponent) iter.next();
                    str.append(encodeParentAndChildrenAsString(fc, child));
                }
            }
        }
        Object value = uic.getAttributes().get("value");
        if (value == null) {
            ValueBinding vb = uic.getValueBinding("value");
            if (vb != null) {
                value = vb.getValue(fc);
            }
        }
        if (value == null) {
        	return str.toString();
        }
        Converter converter = null;
        if(uic instanceof ValueHolder) {
        	converter = ((ValueHolder)uic).getConverter();
        }
        if(converter == null) {
        	converter = FacesContext.getCurrentInstance().getApplication().createConverter(value.getClass());
        }
        if(converter != null) {
        	str.append(converter.getAsString(FacesContext.getCurrentInstance(), uic, value));
        } else {
        	str.append(value);
        }

        //don't process selectItems or f:param for uiCommand)
        if (uic instanceof UISelectBoolean || 
        		uic instanceof UISelectMany || 
        		uic instanceof UISelectOne||
        		uic instanceof UICommand){
        	return str.toString();
        }         
        
        if (uic.getChildCount() > 0) {
            Iterator iter = uic.getChildren().iterator();
            while (iter.hasNext()) {
                UIComponent child = (UIComponent) iter.next();
                str.append(encodeParentAndChildrenAsString(fc, child));
            }
        }
        return str.toString();
    }

    protected List getRenderedChildColumnsList(UIComponent component) {
        List results = new ArrayList();
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if ((kid instanceof UIColumn) && kid.isRendered()) {
                results.add(kid);
            }
            else if (kid instanceof UIColumns) {
                results.add(kid);
            }
        }
        return results;
    }

    private void renderToHandler(OutputTypeHandler outputHandler,
            UIData uiData, FacesContext fc) {

        try {
            int rowIndex = 0;
            int numberOfRowsToDisplay = 0;  
            if (!isIgnorePagination()) {
                rowIndex = uiData.getFirst();
                numberOfRowsToDisplay = uiData.getRows();
                first = rowIndex;
                rows = numberOfRowsToDisplay;
            }
 
            int countOfRowsDisplayed = 0;
            uiData.setRowIndex(rowIndex);
            String[] includeColumnsArray = null;
            String includeColumns = getIncludeColumns();
            if (includeColumns != null)
                includeColumnsArray = includeColumns.split(",");
            List columns = getRenderedChildColumnsList(uiData);
//System.out.println("DataExporter.renderToHandler()  columns.size: " + columns.size());
                
            // write header
//System.out.println("DataExporter.renderToHandler()  HEADERS");
            processAllColumns(
                fc, outputHandler, columns, includeColumnsArray, -1, false);

//System.out.println("DataExporter.renderToHandler()  ROWS");
            while (uiData.isRowAvailable()) {
                if (numberOfRowsToDisplay > 0
                        && countOfRowsDisplayed >= numberOfRowsToDisplay) {
                    break;
                }

                // render the child columns; each one in a td
                processAllColumns(fc, outputHandler, columns,
                    includeColumnsArray, countOfRowsDisplayed, false);
                
                // keep track of rows displayed
                countOfRowsDisplayed++;
                // maintain the row index property on the underlying UIData
                // component
                rowIndex++;
                uiData.setRowIndex(rowIndex);

            }
            // reset the underlying UIData component
            uiData.setRowIndex(-1);

            // write footer
//System.out.println("DataExporter.renderToHandler()  FOOTERS");
            processAllColumns(fc, outputHandler, columns,
                includeColumnsArray, countOfRowsDisplayed, true);
            
            outputHandler.flushFile();
        } catch (Exception e) {
            log.error("renderToHandler()", e);
        }
    }
    
    public void addInfo() {}
    
    /**
     * <p>Set the value of the <code>ignorePagination</code> property.</p>
     */
    public void setIgnorePagination(boolean ignorePagination) {
        this.ignorePagination = new Boolean(ignorePagination);
    }

    /**
     * <p>Return the value of the <code>ignorePagination</code> property.</p>
     */
    public boolean isIgnorePagination() {
        if (ignorePagination != null) {
            return ignorePagination.booleanValue();
        }
        ValueBinding vb = getValueBinding("ignorePagination");
        return vb != null ? ((Boolean) vb.getValue(getFacesContext()))
                .booleanValue() : false;
    }   
    
    /**
     * <p>Set the value of the <code>renderLabelAsButton</code> property.</p>
     */
    public void setRenderLabelAsButton(boolean renderLabelAsButton) {
        this.renderLabelAsButton = new Boolean(renderLabelAsButton);
    }

    /**
     * <p>Return the value of the <code>renderLabelAsButton</code> property.</p>
     */
    public boolean isRenderLabelAsButton() {
        if (renderLabelAsButton != null) {
            return renderLabelAsButton.booleanValue();
        }
        ValueBinding vb = getValueBinding("renderLabelAsButton");
        return vb != null ? ((Boolean) vb.getValue(getFacesContext()))
                .booleanValue() : false;
    }
    
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }
    
    public String getStyleClass() {
        return Util.getQualifiedStyleClass(this, 
                styleClass,
                CSS_DEFAULT.DATAEXPORTER_DEFAULT_STYLE_CLASS,
                "styleClass");
    } 
    
    protected void processAllColumns(FacesContext fc,
                                     OutputTypeHandler outputHandler,
                                     List columns,
                                     String[] includeColumnsArray,
                                     int countOfRowsDisplayed,
                                     boolean footer) {
        if (includeColumnsArray != null) {
//System.out.println("DataExporter.processAllColumns()  COLUMNS ARRAY");
            renderInUserDefinedOrder(fc, outputHandler, columns,
                includeColumnsArray, 0, countOfRowsDisplayed, footer);
        } else {
//System.out.println("DataExporter.processAllColumns()  COLUMNS ITERATING");
            int colIndex = 0;
            Iterator childColumns = columns.iterator();
            while (childColumns.hasNext()) {
                UIComponent nextColumn = (UIComponent) childColumns.next();
                if (nextColumn instanceof UIColumn) {
//System.out.println("DataExporter.processAllColumns()  UIColumn  colIndex: " + colIndex);
                    if (countOfRowsDisplayed == -1) {
//System.out.println("DataExporter.processAllColumns()    HEADER CELL");
                        processColumnHeader(
                            fc, outputHandler, nextColumn, colIndex);
                    }
                    else if (footer) {
//System.out.println("DataExporter.processAllColumns()    FOOTER CELL");
                        processColumnFooter(fc, outputHandler, nextColumn,
                            colIndex, countOfRowsDisplayed);
                    }
                    else {
//System.out.println("DataExporter.processAllColumns()    CELL");
                        processColumn(fc, outputHandler, nextColumn, colIndex,
                            countOfRowsDisplayed);                        
                    }
                    colIndex++;
                }
                else if (nextColumn instanceof UIColumns) {
                    UIColumns nextColumns = (UIColumns) nextColumn;
                    int colsSpecificIndex = nextColumns.getFirst();
                    int count = nextColumns.getRows();
                    int colsSpecificLast = (count == 0) ?
                        (Integer.MAX_VALUE - 1) :
                        (colsSpecificIndex + count - 1);
//System.out.println("DataExporter.processAllColumns()  UIColumns  first: " + colsSpecificIndex + "  rows: " + count);
                    while (colsSpecificIndex <= colsSpecificLast) {
                        nextColumns.setRowIndex(colsSpecificIndex);
                        if (!nextColumns.isRowAvailable()) {
                            break;
                        }
                        if (countOfRowsDisplayed == -1) {
//System.out.println("DataExporter.processAllColumns()    HEADER CELL  colIndex: " + colIndex);
                            processColumnHeader(
                                fc, outputHandler, nextColumn, colIndex);
                        }
                        else if (footer) {
//System.out.println("DataExporter.processAllColumns()    FOOTER CELL  colIndex: " + colIndex);
                            processColumnFooter(fc, outputHandler, nextColumn,
                                colIndex, countOfRowsDisplayed);
                        }
                        else {
//System.out.println("DataExporter.processAllColumns()    CELL  colIndex: " + colIndex);
                            processColumn(fc, outputHandler, nextColumn,
                                colIndex, countOfRowsDisplayed);
                        }
                        colsSpecificIndex++;
                        colIndex++;
                    }
                    nextColumns.setRowIndex(-1);
                }
            }
        }
    }
    
    protected void processColumnHeader(FacesContext fc, 
                                    OutputTypeHandler outputHandler,
                                    UIComponent uiColumn, int colIndex) {
        UIComponent headerComp = uiColumn.getFacet("header");
        if (headerComp != null) {
            String headerText = encodeParentAndChildrenAsString(fc, headerComp);
            if (headerText != null) {
                outputHandler.writeHeaderCell(headerText, colIndex);
            }
        }        
    }
    
    protected void processColumnFooter(FacesContext fc, 
                                    OutputTypeHandler outputHandler,
                                    UIComponent uiColumn, int colIndex,
                                    int countOfRowsDisplayed) {
        UIComponent footerComp = uiColumn.getFacet("footer");
        if (footerComp != null) {
            Object output = encodeParentAndChildrenAsString(fc, footerComp);
            if (output != null) {
                outputHandler.writeFooterCell(output, colIndex, countOfRowsDisplayed);
            }
        }        
    }
    
    protected void processColumn(FacesContext fc, 
            OutputTypeHandler outputHandler,
            UIComponent uiColumn, int colIndex,
            int countOfRowsDisplayed) {
        StringBuffer stringOutput = new StringBuffer();

        Iterator childrenOfThisColumn = uiColumn.getChildren()
                .iterator();
        while (childrenOfThisColumn.hasNext()) {

            UIComponent nextChild = (UIComponent) childrenOfThisColumn
                    .next();
            if (nextChild.isRendered() && !(nextChild instanceof RowSelector)) {
                stringOutput.append(encodeParentAndChildrenAsString(fc,
                        nextChild));
                //a blank to separate 
                if (childrenOfThisColumn.hasNext()) {
                    stringOutput.append(' '); 
                }
            }

        }
        outputHandler.writeCell(stringOutput.toString(), colIndex, countOfRowsDisplayed);
        
    }
    
    
    /**
      */
    public String getIncludeColumns() {
        if (this.includeColumns != null) {
            return includeColumns;
        }
        ValueBinding vb = getValueBinding("includeColumns");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

     /**
     */
    public void setIncludeColumns(String includeColumns) {
        this.includeColumns = includeColumns;
    }    
    
    protected void renderInUserDefinedOrder(FacesContext fc,
            OutputTypeHandler outputHandler, 
            List columns,
            String[] includeColumnsArray,
            int colIndex,
            int countOfRowsDisplayed,
            boolean footer) {
        UIComponent previousColumn = null;
        for (int i=0; i<includeColumnsArray.length; i++) {
//System.out.println("DataExporter.renderInUserDefinedOrder()  includeColumnsArray["+i+"]: " + includeColumnsArray[i]);
            int userIndex = 0;
            try {
                userIndex = Integer.parseInt(includeColumnsArray[i].trim());
            } catch (Exception e) {
                log.error("renderInUserDefinedOrder() invalid column index ", e);
                continue;
            }
//System.out.println("DataExporter.renderInUserDefinedOrder()  userIndex: " + userIndex);
            UIComponent nextColumn = null;
            int runningIndex = 0;
            for (int listIndex = 0; listIndex < columns.size(); listIndex++) {
                UIComponent runningComp = (UIComponent) columns.get(listIndex);
                if (runningComp instanceof UIColumn) {
//System.out.println("DataExporter.renderInUserDefinedOrder()    Check UIColumn  runningIndex: " + runningIndex);
                    if (runningIndex == userIndex) {
//System.out.println("DataExporter.renderInUserDefinedOrder()      MATCH");
                        nextColumn = runningComp;
                        break;
                    }
                    runningIndex++;
                }
                else if (runningComp instanceof UIColumns) {
//System.out.println("DataExporter.renderInUserDefinedOrder()    Check UIColumns");
                    UIColumns runningCols = (UIColumns) runningComp;
                    // If rows is specified, so it's possible to know if 
                    // userIndex is contained in this UIColumns or not.
                    int runningColsSize = runningCols.getRows();
                    if (runningColsSize != 0) {
//System.out.println("DataExporter.renderInUserDefinedOrder()      rows specified");
                        if (userIndex >= runningIndex &&
                            userIndex < runningIndex + runningColsSize) {
                            int colsSpecificIndex = userIndex - runningIndex +
                                runningCols.getFirst();
                            runningIndex = userIndex;
                            runningCols.setRowIndex(colsSpecificIndex);
//System.out.println("DataExporter.renderInUserDefinedOrder()      MATCH  colsSpecificIndex: " + colsSpecificIndex);
                            nextColumn = runningCols;
                            break;
                        }
                        else {
//System.out.println("DataExporter.renderInUserDefinedOrder()      beyond rows");
                            runningIndex += runningColsSize;
                        }
                    }
                    // Have to just iterate over the UIColumns to determine
                    // its size, to know if userIndex falls within it.
                    else {
//System.out.println("DataExporter.renderInUserDefinedOrder()      rows not specified");
                        int colsSpecificIndex = runningCols.getFirst();
                        while (true) {
                            runningCols.setRowIndex(colsSpecificIndex);
                            if (!runningCols.isRowAvailable()) {
                                break;
                            }
                            if (runningIndex == userIndex) {
//System.out.println("DataExporter.renderInUserDefinedOrder()      MATCH  colsSpecificIndex: " + colsSpecificIndex);
                                nextColumn = runningCols;
                                break;
                            }
                            runningIndex++;
                            colsSpecificIndex++;
                        }
                        if (nextColumn != null) {
                            break;
                        }
                        
                        // If we just scanned through runningCols, and aren't 
                        // using it, clean it up, unless we're going to already
                        if (nextColumn == null &&
                            runningCols != previousColumn) {
                            runningCols.setRowIndex(-1);
                        }
                    }
                }
            }
            
            if (nextColumn == null) {
                log.info("["+userIndex +"] is invalid column index. Column index is 0 based and should be less then from "+ runningIndex);
                continue;
            }
            
            // If we're switching away from a UIColumns, clean it up
            if (previousColumn != null &&
                nextColumn != previousColumn &&
                previousColumn instanceof UIColumns) {
                ((UIColumns)previousColumn).setRowIndex(-1);
            }
            previousColumn = nextColumn;
            
            if (countOfRowsDisplayed == -1) {
                processColumnHeader(fc, outputHandler, nextColumn, colIndex);
            } else if (footer) {
                processColumnFooter(fc, outputHandler, nextColumn, colIndex,
                    countOfRowsDisplayed);
            } else {
                processColumn(fc, outputHandler, nextColumn, colIndex, countOfRowsDisplayed);
            }
            colIndex++; 
        }
        
        
        // Cleanup any trailing UIColumns
        if (previousColumn != null &&
            previousColumn instanceof UIColumns) {
            ((UIColumns)previousColumn).setRowIndex(-1);
        }
    }

    public String getPopupBlockerLabel() {
        if (this.popupBlockerLabel != null) {
            return popupBlockerLabel;
        }
        ValueBinding vb = getValueBinding("popupBlockerLabel");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public void setPopupBlockerLabel(String popupBlockerLabel) {
        this.popupBlockerLabel = popupBlockerLabel;
    }    
}
