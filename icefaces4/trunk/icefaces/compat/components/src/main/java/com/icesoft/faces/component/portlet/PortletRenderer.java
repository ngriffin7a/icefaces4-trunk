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

package com.icesoft.faces.component.portlet;

import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeRenderer;
import com.icesoft.faces.component.ExtendedAttributeConstants;
import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class PortletRenderer extends DomBasicRenderer {
    private static final String[] passThruAttributes =
            ExtendedAttributeConstants.getAttributes(ExtendedAttributeConstants.ICE_PORTLET);

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {

        validateParameters(facesContext, uiComponent, UINamingContainer.class);
        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);

        if (!domContext.isInitialized()) {
            String clientID = uiComponent.getClientId(facesContext);
            Element root = domContext.createElement(HTML.DIV_ELEM);

            domContext.setRootNode(root);
            root.setAttribute(HTML.ID_ATTR, clientID);
        }

        Element root = (Element) domContext.getRootNode();

        String styleClass =
                (String) uiComponent.getAttributes().get("styleClass");
        if (styleClass != null) {
            root.setAttribute("class", styleClass);
        }

        PassThruAttributeRenderer.renderHtmlAttributes(facesContext, uiComponent, passThruAttributes);
        facesContext.getApplication().getViewHandler().writeState(facesContext);
        domContext.stepInto(uiComponent);
    }

}
