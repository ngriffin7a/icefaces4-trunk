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

package com.icesoft.faces.component;

import com.icesoft.faces.context.effects.JavascriptContext;
import org.icefaces.impl.event.UIOutputWriter;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.IOException;
import java.util.Map;

public class JavaScriptContextSetup implements SystemEventListener {

    public JavaScriptContextSetup() {
    }

    public boolean isListenerForSource(Object source) {
        return true;
    }

    public void processEvent(SystemEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!EnvUtils.isICEfacesView(facesContext)) {
            return;
        }

        UIOutput jsContextOutput = new UIOutputWriter() {
            public void encode(ResponseWriter writer, FacesContext context) throws IOException {
                writer.startElement("span", this);
                writer.writeAttribute("id", "dynamic-code-compat", null);
                if (!context.getPartialViewContext().isPartialRequest()) {
                    writer.startElement("script", this);
                    writer.writeAttribute("type", "text/javascript", null);
                    writer.write(JavascriptContext.getJavascriptCalls(context));
                    writer.endElement("script");
                }
                writer.endElement("span");
            }
        };
        Map<String,Object> attributes = jsContextOutput.getAttributes();
        attributes.put("name", "JavaScriptContextSetup.js");
        attributes.put("library", "ice.compat");

        UIViewRoot root = facesContext.getViewRoot();
        jsContextOutput.setTransient(true);
        jsContextOutput.setId("javascript_context_setup");
        root.addComponentResource(facesContext, jsContextOutput, "body");
    }
}
