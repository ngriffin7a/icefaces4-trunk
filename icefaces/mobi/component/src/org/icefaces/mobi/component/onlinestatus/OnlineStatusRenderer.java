/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.mobi.component.onlinestatus;


import org.icefaces.ace.util.ComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;

public class OnlineStatusRenderer extends Renderer {

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        final OnlineStatus status = (OnlineStatus) component;
        final ResponseWriter writer = context.getResponseWriter();
        final String id = component.getClientId();
        writer.startElement("div", component);
        writer.writeAttribute("id", id, null);
        ComponentUtils.enableOnElementUpdateNotify(writer, id);

        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeText("ice.mobi.onlineStatus({", null);
        writer.writeText("id: '", null);
        writer.writeText(id, null);
        writer.writeText("', offlineClass: '", null);
        writer.writeText(status.getOfflineStyleClass(), null);
        writer.writeText("', onlineClass: '", null);
        writer.writeText(status.getOnlineStyleClass(), null);
        writer.writeText("', offlineCallback: function(element) { ", null);
        writer.writeText(status.getOnOffline(), null);
        writer.writeText("}, onlineCallback: function(element) { ", null);
        writer.writeText(status.getOnOnline(), null);
        writer.writeText("}});", null);
        writer.endElement("script");
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        writer.endElement("div");
    }
}
