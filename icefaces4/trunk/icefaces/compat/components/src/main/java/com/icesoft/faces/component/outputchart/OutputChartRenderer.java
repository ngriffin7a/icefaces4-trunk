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

package com.icesoft.faces.component.outputchart;

import com.icesoft.faces.component.ExtendedAttributeConstants;
import com.icesoft.faces.component.ext.renderkit.FormRenderer;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeRenderer;
import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import java.beans.Beans;
import java.io.IOException;

public class OutputChartRenderer extends DomBasicRenderer {
    private static final String[] passThruAttributes =
            ExtendedAttributeConstants.getAttributes(ExtendedAttributeConstants.ICE_OUTPUTCHART);

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {

        OutputChart outputChart = (OutputChart) uiComponent;        
        if(!Beans.isDesignTime()){
            try {
                if (outputChart.getAbstractChart() == null) {
                    outputChart.createAbstractChart();                  
                    if (outputChart.getType().equalsIgnoreCase(OutputChart.CUSTOM_CHART_TYPE)) {
                        outputChart.evaluateRenderOnSubmit(facesContext);
                    }
                    outputChart.getAbstractChart().encode(facesContext, outputChart);
                } else if (outputChart.evaluateRenderOnSubmit(facesContext).booleanValue()) {
                    outputChart.getAbstractChart().encode(facesContext, outputChart);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }  
        String clientId = outputChart.getClientId(facesContext);
        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);
        if (!domContext.isInitialized()) {
            Element table = domContext.createElement(HTML.TABLE_ELEM);
            domContext.setRootNode(table);
            setRootElementId(facesContext, table, uiComponent);
            Element tbody = (Element) domContext.createElement(HTML.TBODY_ELEM);
            Element tr = (Element) domContext.createElement(HTML.TR_ELEM);
            Element td = (Element) domContext.createElement(HTML.TD_ELEM);
            table.setAttribute(HTML.CLASS_ATTR, outputChart.getStyleClass());
            String style = outputChart.getStyle();
            if(style != null && style.length() > 0)
                table.setAttribute(HTML.STYLE_ATTR, style);
            else
                table.removeAttribute(HTML.STYLE_ATTR);
            table.appendChild(tbody);
            tbody.appendChild(tr);
            tr.appendChild(td);
        }
        Element table = (Element)domContext.getRootNode();
        FormRenderer.addHiddenField(facesContext, OutputChart.ICE_CHART_COMPONENT);
        
        Element td = (Element) domContext.getRootNode(). //table
                getFirstChild().//tbody Art: 
                getFirstChild().//tr
                getFirstChild();//td
        DOMContext.removeChildren(td);
        PassThruAttributeRenderer.renderHtmlAttributes(facesContext, uiComponent, td, table, passThruAttributes);
        Element image = (Element) domContext.createElement(HTML.IMG_ELEM);
        image.setAttribute(HTML.SRC_ATTR, outputChart.getChartURI().getPath());
       
        td.appendChild(image);
        if (outputChart.isClientSideImageMap()) {
            Element map = (Element) domContext.createElement(HTML.MAP_ELEM);
            map.setAttribute(HTML.NAME_ATTR, "map" + clientId);
            image.setAttribute(HTML.USEMAP_ATTR, "#map" + clientId);
            image.setAttribute(HTML.BORDER_ATTR, "0");
            //render the clientSideImageMap if the component has an actionListener registered 
            outputChart.generateClientSideImageMap(domContext, map);
            td.appendChild(map);
        }
        domContext.stepOver();
    }
}
