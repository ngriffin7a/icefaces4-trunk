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

package org.icefaces.ace.component.splitpane;

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.icefaces.ace.util.HTML;
import javax.faces.context.ResponseWriter;

public class SplitPaneCoreRenderer {
    public static final String SPLITPANE_BASE_CSS = "mobi-splitpane" ;
    public static final String SPLITPANE_NONSCROLL_CSS = "mobi-splitpane-nonScrollable";
    public static final String SPLITPANE_SCROLLABLE_CSS = "mobi-splitpane-scrollable";
    public static final String SPLITPANE_DIVIDER_CSS = "mobi-splitpane-divider";

    private static final Logger logger =
            Logger.getLogger(SplitPaneCoreRenderer.class.toString());
    private static final int DEFAULT_COLUMN_WIDTH = 25;
    private String leftwidth;
    private String rightwidth;
    private StringBuilder paneClass = new StringBuilder(SPLITPANE_SCROLLABLE_CSS); //default
    private StringBuilder spltClass = new StringBuilder(SPLITPANE_DIVIDER_CSS);

    public void encodeBegin(SplitPane component, ResponseWriter writer)
            throws IOException {;
        if (!component.isScrollable()) {
            this.paneClass = new StringBuilder(SPLITPANE_NONSCROLL_CSS) ;
        }
        int leftWidth = component.getColumnDivider();
        if (leftWidth < 1 || leftWidth>99){
            leftWidth = DEFAULT_COLUMN_WIDTH;
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(" input of ColumnDivider is invalid, setting it to default value");
            }
        }
        int rightWidth = 100 - leftWidth;
        this.setLeftwidth(String.valueOf(leftWidth)+ "%");
        this.setRightwidth(String.valueOf(rightWidth) + "%");
        String userClass = component.getStyleClass();
       if (userClass!=null){
            this.paneClass.append(" ").append(userClass) ;
            this.spltClass.append(" ").append(userClass).toString();
        }
        writer.startElement(HTML.DIV_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, component.getClientId(), null);
        writeStandardLayoutAttributes(writer, component, SPLITPANE_BASE_CSS );
    }

    public void encodePaneEnd(ResponseWriter writer)
        throws IOException{
        writer.endElement(HTML.DIV_ELEM);
    }

    /**
     *   used by jsf renderer  for facets
     * @param component
     * @param writer
     * @param side
     * @throws IOException
     */
    public void encodePane(SplitPane component, ResponseWriter writer, String side)
        throws IOException {
        writer.startElement(HTML.DIV_ELEM, component);
        String width = this.getLeftwidth();
        if (side.equals("right")){
           width = this.getRightwidth();
        }
        writer.writeAttribute(HTML.STYLE_ATTR, width, null);
        writer.writeAttribute(HTML.CLASS_ATTR, this.getPaneClass(), null);
        writer.writeAttribute(HTML.ID_ATTR, component.getClientId()+"_"+side, null);
    }

    public void encodeColumnDivider(SplitPane component, ResponseWriter writer)
        throws IOException {
            /* column Divider for next iteration of component  if resizable is true then render */
  /*      writer.startElement(DIV_ELEM, component);
        writer.writeAttribute(ID_ATTR, component.getClientId()+"_splt");
        writer.writeAttribute(CLASS_ATTR, this.getSpltClass());
        writer.endElement(DIV_ELEM);   */
    }

    public void encodeEnd(SplitPane pane, ResponseWriter writer)
            throws IOException{
        writer.startElement(HTML.SPAN_ELEM, pane);
        writer.writeAttribute(HTML.CLASS_ATTR, "mobi-hidden", null);
        writer.startElement(HTML.SCRIPT_ELEM, pane);
        writer.writeAttribute("type", "text/javascript", null);
        StringBuilder sb = new StringBuilder("ice.mobi.splitpane.initClient('").append(pane.getClientId()).append("'");
        sb.append(",{ scrollable: '").append(pane.isScrollable()).append("'");
     //   sb.append(", resize: ").append(pane.isResizable());
        int width = pane.getColumnDivider();
        if (width < 1 || width > 99){
            width = DEFAULT_COLUMN_WIDTH;
        }
        sb.append(",width: '").append(width).append("'");
        sb.append("});");
        writer.write(sb.toString());
        writer.endElement(HTML.SCRIPT_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
        writer.endElement(HTML.DIV_ELEM);
    }

    public String getLeftwidth() {
        if (this.leftwidth != null){
            return "width:" +leftwidth+";";}
        else {
            return "width: 30%;"; //default value
        }
    }

    public void setLeftwidth(String leftwidth) {
        this.leftwidth = leftwidth;
    }

    public String getRightwidth() {
        if (this.rightwidth !=null){
            return "width:" + rightwidth+";";
        } else {
            return "width: 70%;";
        }
    }

    public void setRightwidth(String rightwidth) {
        this.rightwidth = rightwidth;
    }

    public String getPaneClass() {
        return paneClass.toString();
    }

    public void setPaneClass(StringBuilder paneClass) {
        this.paneClass = paneClass;
    }

    public String getSpltClass() {
        return spltClass.toString();
    }

    public void setSpltClass(StringBuilder spltClass) {
        this.spltClass = spltClass;
    }
	
    public void writeStandardLayoutAttributes(ResponseWriter writer,
                    SplitPane component, String baseClass) throws IOException  {
        StringBuilder inputStyle = new StringBuilder(baseClass);
        if (null != component.getStyleClass())  {
            inputStyle.append(" ").append(component.getStyleClass());
        }
        if( inputStyle.length() > 0 ){
            writer.writeAttribute(HTML.CLASS_ATTR, inputStyle, null);
        }
        if (null != component.getStyle())  {
            writer.writeAttribute(HTML.STYLE_ATTR, component.getStyle(), null);
        }
    }
}
