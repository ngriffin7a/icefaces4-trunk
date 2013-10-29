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
import com.icesoft.faces.context.effects.LocalEffectEncoder;
import com.icesoft.faces.component.AttributeConstants;
import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;

public class GroupRenderer extends DomBasicRenderer {
    // Basically, everything is excluded
    private static final String[] PASSTHRU_EXCLUDE =
        new String[] { HTML.STYLE_ATTR };
    private static final String[] PASSTHRU =
        AttributeConstants.getAttributes(
            AttributeConstants.H_PANELGROUP,
            PASSTHRU_EXCLUDE);

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {

        // render a span if either of the style or style class attributes
        // are present
        String style = (String) uiComponent.getAttributes().get("style");
        String styleClass =
                (String) uiComponent.getAttributes().get("styleClass");
        boolean requiresSpan = requiresRootElement(style, styleClass, uiComponent);

        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);

        if (requiresSpan) {
            if (!domContext.isInitialized()) {
                Element rootSpan = createRootElement(domContext);
                domContext.setRootNode(rootSpan);
                setRootElementId(facesContext, rootSpan, uiComponent);
            }
            Element rootSpan = (Element) domContext.getRootNode();
            DOMContext.removeChildren(rootSpan);
            renderStyleAndStyleClass(style, styleClass, rootSpan);
        }
        domContext.stepInto(uiComponent);
    }

    public void encodeChildren(FacesContext facesContext,
                               UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, null);
        DOMContext domContext =
                DOMContext.getDOMContext(facesContext, uiComponent);
        if (uiComponent.getChildCount() > 0) {
            Iterator children = uiComponent.getChildren().iterator();
            while (children.hasNext()) {
                UIComponent nextChild = (UIComponent) children.next();
                if (nextChild.isRendered()) {
                    encodeParentAndChildren(facesContext, nextChild);
                }
            }
        }
        // set the cursor here since nothing happens in encodeEnd
        domContext.stepOver();
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, null);
    }
    
    protected Element createRootElement(DOMContext domContext) {
        return domContext.createElement(HTML.SPAN_ELEM);
    }
    
    protected void renderStyleAndStyleClass(
        String style, String styleClass, Element root)
    {
        if (styleClass != null) {
            root.setAttribute(HTML.CLASS_ATTR, styleClass);
        }
        if (style != null && style.length() > 0) {
            root.setAttribute(HTML.STYLE_ATTR, style);
        }
        else {
            root.removeAttribute(HTML.STYLE_ATTR);
        }
    }


    /**
     * @param style
     * @param styleClass
     * @param uiComponent
     * @return boolean
     */
    private boolean requiresRootElement(String style, String styleClass,
                                 UIComponent uiComponent) {
        return idNotNull(uiComponent) || style != null || styleClass != null;
    }

}