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

package com.icesoft.faces.component.outputconnectionstatus;

import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.util.pooling.ClientIdPool;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class OutputConnectionStatusRenderer extends DomBasicRenderer {

    public void encodeBegin(FacesContext context, UIComponent uiComponent)
            throws IOException {
        validateParameters(context, uiComponent, null);
        OutputConnectionStatus component =
                ((OutputConnectionStatus) uiComponent);
        DOMContext domContext =
                DOMContext.attachDOMContext(context, uiComponent);
        if (!domContext.isInitialized()) {
            String id = uiComponent.getClientId(context);
            Element root = domContext.createRootElement(HTML.DIV_ELEM);
            domContext.setRootNode(root);

            root.setAttribute(HTML.ID_ATTR, id);
            root.setAttribute(HTML.CLASS_ATTR, component.getStyleClass());
            String style = component.getStyle();
            if (style != null && style.length() > 0)
                root.setAttribute(HTML.STYLE_ATTR, style);
            else
                root.removeAttribute(HTML.STYLE_ATTR);

            String idleID = ClientIdPool.get(id + UINamingContainer.getSeparatorChar(context) + "connection-idle");
            root.appendChild(getNextNode(domContext,
                    component.getInactiveClass(),
                    component.getInactiveLabel(),
                    idleID, true));
            String workingID = ClientIdPool.get(id + UINamingContainer.getSeparatorChar(context) + "connection-working");
            root.appendChild(getNextNode(domContext, component.getActiveClass(),
                    component.getActiveLabel(),
                    workingID, false));
            String troubleID = ClientIdPool.get(id + UINamingContainer.getSeparatorChar(context) + "connection-trouble");
            root.appendChild(getNextNode(domContext,
                    component.getCautionClass(),
                    component.getCautionLabel(),
                    troubleID, false));
            String lostID = ClientIdPool.get(id + UINamingContainer.getSeparatorChar(context) + "connection-lost");
            root.appendChild(getNextNode(domContext,
                    component.getDisconnectedClass(),
                    component.getDisconnectedLabel(),
                    lostID, false));
            Element script = domContext.createElement(HTML.SCRIPT_ELEM);
            script.setAttribute(HTML.TYPE_ATTR, "text/javascript");
            script.setAttribute(HTML.ID_ATTR, ClientIdPool.get(id + "script"));
            script.appendChild(domContext.createTextNodeUnescaped(
                    "ice.ComponentIndicators('" + workingID + "', '" + idleID + "', '" + troubleID + "', '" + lostID + "', " + component.isShowPopupOnDisconnect() + ", " + component.isDisplayHourglassWhenActive() + ");"
            ));
            root.appendChild(script);
        }

        domContext.stepOver();
    }

    public Element getNextNode(DOMContext domContext, String classString,
                               String label, String id, boolean visible) {
        Element div = (Element) domContext.createElement(HTML.DIV_ELEM);
        div.setAttribute(HTML.ID_ATTR, id);
        div.setAttribute(HTML.CLASS_ATTR, classString);
        if (!visible) {
            div.setAttribute(HTML.STYLE_ATTR, "visibility: hidden;");
        }
        if ((null == label) || ("".equals(label))) {
            return div;
        }
        if (label != null) {
//            label = DOMUtils.escapeAnsi(label);
        }
        Text text = (Text) domContext.createTextNode(label);
        div.appendChild(text);
        return div;
    }
}
