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

package com.icesoft.faces.component.repeat;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;

import com.icesoft.faces.component.panelseries.UISeries;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;
import org.icefaces.resources.BrowserType;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

@ICEResourceDependencies({
        @ICEResourceDependency(name="icefaces-compat.js", library="ice.compat",target="head", browser= BrowserType.ALL, browserOverride={}),
        @ICEResourceDependency(name="compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={})
})
public class Repeat extends UISeries{
    public static final String COMPONENT_TYPE = "com.icesoft.faces.Repeat";
    public static final String COMPONENT_FAMILY = "com.icesoft.faces.Repeat";
    public Repeat() {
        setRendererType(null);        
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    public boolean getRendersChildren() {
        return true;
    }
    
    public void encodeChildren(FacesContext context) throws IOException {
        int rowIndex = getFirst();
       
        int numberOfRowsToDisplay = getRows();
        int countOfRowsDisplayed = 0;
        while (  ( numberOfRowsToDisplay == 0 ) ||
                 ( (numberOfRowsToDisplay > 0) &&
                   (countOfRowsDisplayed < numberOfRowsToDisplay) )  )
        {
             setRowIndex(rowIndex);
             if(!isRowAvailable()){
                break;
            }
            Iterator childs;
            if (getChildCount() > 0) {
                childs = getChildren().iterator();
                while (childs.hasNext()) {
                    UIComponent nextChild = (UIComponent) childs.next();
                    if (nextChild.isRendered()) {
                        DomBasicRenderer.encodeParentAndChildren(context, nextChild);
                    }
                }
            }
            rowIndex++;
            countOfRowsDisplayed++;
        }
        setRowIndex(-1);        
    }
    
    /*
     *  (non-Javadoc)
     * @see com.icesoft.faces.component.panelseries.UISeries#restoreChild(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    protected void restoreChild(FacesContext facesContext,
                                UIComponent uiComponent) {
        super.restoreChild(facesContext, uiComponent);
        if (uiComponent instanceof UIData) {
            String clientId = uiComponent.getClientId(facesContext);
            Object value = savedChildren.get(clientId);
            ((UIData) uiComponent).setValue(value);
        }
    }
    
    /*
     *  (non-Javadoc)
     * @see com.icesoft.faces.component.panelseries.UISeries#saveChild(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    protected void saveChild(FacesContext facesContext,
                             UIComponent uiComponent) {
        super.saveChild(facesContext, uiComponent);
        if (uiComponent instanceof UIData) {
            String clientId = uiComponent.getClientId(facesContext);
            savedChildren.put(clientId, ((UIData) uiComponent).getValue());
        }
    }

}
