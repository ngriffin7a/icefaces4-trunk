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

package com.icesoft.faces.component.panelseries;

import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;


public class PanelSeriesRenderer extends DomBasicRenderer {
    public boolean getRendersChildren() {
        return true;
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);

        if (!domContext.isInitialized()) {
            Element rootSpan = domContext.createElement(HTML.DIV_ELEM);
            domContext.setRootNode(rootSpan);
            setRootElementId(facesContext, rootSpan, uiComponent);
        }
        Element root = (Element) domContext.getRootNode();
        String style = ((PanelSeries) uiComponent).getStyle();
        if(style != null && style.length() > 0)
            root.setAttribute(HTML.STYLE_ATTR, style);
        else
            root.removeAttribute(HTML.STYLE_ATTR);
        root.setAttribute(HTML.CLASS_ATTR,
                          ((PanelSeries) uiComponent).getStyleClass());
        domContext.stepInto(uiComponent);
    }

    public void encodeChildren(FacesContext facesContext,
                               UIComponent uiComponent) throws IOException {
        validateParameters(facesContext, uiComponent, null);
        DOMContext domContext =
                DOMContext.getDOMContext(facesContext, uiComponent);
        Element root = (Element) domContext.getRootNode();
        DOMContext.removeChildren(root);
        PanelSeries list = (PanelSeries) uiComponent;
        UISeries uiList = (UISeries) uiComponent;
        int rowIndex = uiList.getFirst();
        int numberOfRowsToDisplay = uiList.getRows();
        int countOfRowsDisplayed = 0;
        while (  ( numberOfRowsToDisplay == 0 ) ||
                 ( (numberOfRowsToDisplay > 0) &&
                   (countOfRowsDisplayed < numberOfRowsToDisplay) )  )
        {
             uiList.setRowIndex(rowIndex);
             if(!uiList.isRowAvailable()){
                break;
            }
            Iterator childs;
            if (list.getChildCount() > 0) {
                childs = list.getChildren().iterator();
                while (childs.hasNext()) {
                    UIComponent nextChild = (UIComponent) childs.next();
                    if (nextChild.isRendered()) {
                        encodeParentAndChildren(facesContext, nextChild);
                    }
                }
            }
            rowIndex++;
            countOfRowsDisplayed++;
        }
        uiList.setRowIndex(-1);

    }
}
