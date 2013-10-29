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

package org.icefaces.impl.component;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class IdleMonitor extends UIComponentBase {
    private enum PropertyKeys {
        interval
    }

    public String getFamily() {
        return "javax.faces.Output";
    }

    public long getInterval() {
        Long interval = (Long) getStateHelper().eval(PropertyKeys.interval);
        return interval == null ? 20 : interval;
    }

    public void setInterval(long interval) {
        getStateHelper().put(PropertyKeys.interval, interval);
    }

    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String id = getClientId(context);
        String setupID = id + "_setup";
        String containerID = id + "_container";

        writer.startElement("span", null);
        writer.writeAttribute("id", setupID, null);

        writer.startElement("script", null);
        writer.writeText("ice.onUserInactivity(", null);
        writer.writeText(getInterval(), null);
        writer.writeText(", function() { document.getElementById('", null);
        writer.writeText(containerID, null);
        writer.writeText("').style.visibility = 'visible'; }", null);
        writer.writeText(", function() { document.getElementById('", null);
        writer.writeText(containerID, null);
        writer.writeText("').style.visibility = 'hidden'; });", null);
        writer.endElement("script");

        writer.endElement("span");

        writer.startElement("div", this);
        writer.writeAttribute("id", containerID, null);
        writer.writeAttribute("style", "visibility: hidden", null);
    }

    public void encodeEnd(FacesContext context) throws IOException {
        context.getResponseWriter().endElement("div");
    }
}
