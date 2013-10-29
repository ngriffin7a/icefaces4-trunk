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

import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.util.Map;

@ICEResourceDependencies({
        @ICEResourceDependency(name = "navigation-notifier/blank.html"),
        @ICEResourceDependency(name = "navigation-notifier/json2007.js"),
        @ICEResourceDependency(name = "navigation-notifier/rsh.js")
})
public class NavigationNotifier extends UICommand {
    public NavigationNotifier() {
    }

    public String getFamily() {
        return getClass().getName();
    }

    public void decode(FacesContext context) {
        Map requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String source = String.valueOf(requestParameterMap.get("javax.faces.source"));
        String clientId = getClientId();
        if (clientId.equals(source)) {
            queueEvent(new ActionEvent(this));
        }
    }

    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String id = getClientId();
        writer.startElement("span", this);
        writer.writeAttribute("id", id + "_notifier", null);

        writer.startElement("input", this);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("name", id, null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("autocomplete", "off", null);
        writer.writeAttribute("value", "", null);
        writer.endElement("input");

        writer.startElement("form", this);
        writer.writeAttribute("id", "rshStorageForm", null);
        writer.writeAttribute("style", "left:-1000px;top:-1000px;width:1px;height:1px;border:0;position:absolute;", null);
        writer.startElement("textarea", this);
        writer.writeAttribute("id", "rshStorageField", null);
        writer.writeAttribute("style", "left:-1000px;top:-1000px;width:1px;height:1px;border:0;position:absolute;", null);
        writer.endElement("textarea");
        writer.endElement("form");

        writer.startElement("script", this);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("window.dhtmlHistory.create();\n");
        writer.write("var init = function(newLocation, historyData) {\n");
        writer.write("dhtmlHistory.initialize();\n");
        writer.write("dhtmlHistory.addListener(function(newLocation, historyData) {\n");
        writer.write("ice.se(null, '" + id + "');\n");
        writer.write("});\n");
        writer.write("};\n");
        writer.write("if (window.addEventListener) { window.addEventListener('load', init, false) } else { window.attachEvent('onload', init); }");
        writer.endElement("script");

        writer.endElement("span");
    }
}
