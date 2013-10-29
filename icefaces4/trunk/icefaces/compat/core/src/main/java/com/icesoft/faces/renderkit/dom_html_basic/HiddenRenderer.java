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

package com.icesoft.faces.renderkit.dom_html_basic;

import com.icesoft.faces.context.DOMContext;
import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class HiddenRenderer extends DomBasicInputRenderer {

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, UIInput.class);
    }

    public void encodeChildren(FacesContext facesContext,
                               UIComponent uiComponent) {
        validateParameters(facesContext, uiComponent, UIInput.class);
    }

    protected void renderEnd(FacesContext facesContext,
                             UIComponent uiComponent, String currentValue)
            throws IOException {

        validateParameters(facesContext, uiComponent, UIInput.class);
        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);
        if (!domContext.isInitialized()) {
            Element root = domContext.createElement("input");
            domContext.setRootNode(root);
            setRootElementId(facesContext, root, uiComponent);
            String clientId = uiComponent.getClientId(facesContext);
            root.setAttribute("name", clientId);
            root.setAttribute("type", "hidden");
            root.setAttribute(HTML.AUTOCOMPLETE_ATTR, "off");
        }
        Element root = (Element) domContext.getRootNode();
        if (currentValue != null) {
            root.setAttribute("value", currentValue);
        }
    }
}

