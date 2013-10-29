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

package org.icefaces.impl.renderkit.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;

public class SubmitOnEnterRenderer extends Renderer {

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        UIComponent parent = component.getParent();
        String parentId = parent.getClientId();
        boolean enabled = true;
        if ("false".equalsIgnoreCase((String) component.getAttributes().get("enabled")))  {
            enabled = false;
        }
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", component);
        String clientId = component.getClientId();
        writer.writeAttribute("id", clientId, "id");
        writer.startElement("script", component);
        writer.writeAttribute("type", "text/javascript", "type");
        String state = enabled ? "'enabled';" : "'disabled';";
        writer.writeText("document.getElementById('" + clientId + "').submitOnEnter=" + state, component, null);
        writer.endElement("script");
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("div");
    }
}
