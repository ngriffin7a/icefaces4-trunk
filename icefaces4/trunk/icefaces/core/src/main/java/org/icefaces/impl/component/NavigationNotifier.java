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

package org.icefaces.impl.component;

import org.icefaces.impl.event.BridgeSetup;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

import javax.faces.component.UICommand;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.ValueHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.IOException;
import java.util.Map;

@ICEResourceDependencies({
        @ICEResourceDependency(name = "navigation-notifier/navigation-notifier.js")
})
public class NavigationNotifier extends UICommand {
    public NavigationNotifier() {
    }

    public String getFamily() {
        return getClass().getName();
    }

    public void decode(FacesContext context) {
        final ExternalContext externalContext = context.getExternalContext();
        Map requestParameterMap = externalContext.getRequestParameterMap();
        String source = String.valueOf(requestParameterMap.get("javax.faces.source"));
        String clientId = getClientId();
        if (clientId.equals(source)) {
            queueEvent(new ActionEvent(this));
        }
    }

    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String id = getClientId();

        writer.startElement("input", this);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("name", id, null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("autocomplete", "off", null);
        writer.writeAttribute("value", "", null);
        writer.endElement("input");
    }

    public static class Setup implements SystemEventListener {
        public void processEvent(SystemEvent event) throws AbortProcessingException {
            final FacesContext context = FacesContext.getCurrentInstance();
            final UIViewRoot root = context.getViewRoot();
            final NavigationNotifier navigationNotifier = (NavigationNotifier) event.getSource();
            UIOutput setupComponent = new UIOutput() {
                public void encodeBegin(FacesContext context) throws IOException {
                    ResponseWriter writer = context.getResponseWriter();
                    String id = navigationNotifier.getClientId();
                    writer.startElement("span", this);

                    writer.writeAttribute("id", id + "_notifier", null);
                    writer.startElement("script", this);
                    writer.writeAttribute("type", "text/javascript", null);
                    writer.write("ice.setupNavigationNotifier('" + id + "');");
                    writer.endElement("script");

                    writer.endElement("span");
                }
            };
            setupComponent.setTransient(true);
            root.addComponentResource(context, setupComponent, "body");
        }

        public boolean isListenerForSource(Object source) {
            return source instanceof NavigationNotifier;
        }
    }
}
