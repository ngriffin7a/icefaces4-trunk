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

import javax.faces.FacesException;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DefaultAction extends UICommand {
    private final static HashMap<String, Integer> keyCodeMapping = new HashMap();

    static {
        keyCodeMapping.put("Enter", 13);
        keyCodeMapping.put("Esc", 27);
        keyCodeMapping.put("Space", 32);
        keyCodeMapping.put("PageUp", 33);
        keyCodeMapping.put("PageDown", 34);
        keyCodeMapping.put("PageUp", 32);
        keyCodeMapping.put("LeftArrow", 37);
        keyCodeMapping.put("UpArrow", 38);
        keyCodeMapping.put("RightArrow", 39);
        keyCodeMapping.put("DownArrow", 40);
        keyCodeMapping.put("F1", 112);
        keyCodeMapping.put("F2", 113);
        keyCodeMapping.put("F3", 114);
        keyCodeMapping.put("F4", 115);
        keyCodeMapping.put("F5", 116);
        keyCodeMapping.put("F6", 117);
        keyCodeMapping.put("F7", 118);
        keyCodeMapping.put("F8", 119);
        keyCodeMapping.put("F9", 120);
        keyCodeMapping.put("F10", 121);
        keyCodeMapping.put("F11", 122);
        keyCodeMapping.put("F12", 123);
        //..extend with more keys
    }

    private enum PropertyKeys {
        key
    }

    public DefaultAction() {
    }

    public String getId() {
        return "defaultActionOn" + getKey().replace(' ', '_');
    }

    public String getKey() {
        String key = (String) getStateHelper().eval(PropertyKeys.key);
        return key == null ? "Enter" : key;
    }

    public void setKey(String key) {
        getStateHelper().put(PropertyKeys.key, key);
    }

    public void decode(FacesContext context) {
        if (getClientId().equals(context.getExternalContext().getRequestParameterMap().get("javax.faces.source"))) {
            super.decode(context);
        }
    }

    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = getClientId(context);
        writer.startElement("input", this);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("autocomplete", "off", null);
        writer.writeAttribute("value", "", null);
        writer.endElement("input");
    }

    public void encodeEnd(FacesContext context) throws IOException {
    }

    public static class AttachToForm implements SystemEventListener {

        public boolean isListenerForSource(Object o) {
            return o instanceof DefaultAction;
        }

        public void processEvent(SystemEvent systemEvent) {
            DefaultAction source = (DefaultAction) systemEvent.getSource();
            UIComponent c = source;
            do {
                c = c.getParent();
            } while (c != null && !(c instanceof UIForm));
            if (c == null) {
                throw new FacesException("defaultAction component needs to be enclosed in a form");
            }
            String clientId = source.getClientId();
            Map attributes = c.getAttributes();
            KeyMap keyMap = (KeyMap) attributes.get(DefaultAction.class.getName());
            if (keyMap == null) {
                keyMap = new KeyMap();
                attributes.put(DefaultAction.class.getName(), keyMap);
            }
            keyMap.addMapping(clientId, keyCodeMapping.get(source.getKey()));
        }
    }

    private static class KeyMap {
        private HashMap<Integer, String> mapping = new HashMap();

        public void addMapping(String elementID, Integer keyCode) {
            mapping.put(keyCode, elementID);
        }

        public String toString() {
            StringBuffer buffer = new StringBuffer();
            Iterator i = mapping.entrySet().iterator();
            buffer.append('{');
            while (i.hasNext()) {
                Map.Entry<Integer, String> entry = (Map.Entry<Integer, String>) i.next();
                buffer.append('\'');
                buffer.append(entry.getKey());
                buffer.append("': '");
                buffer.append(entry.getValue());
                buffer.append('\'');
                if (i.hasNext()) {
                    buffer.append(',');
                }
            }
            buffer.append('}');
            return buffer.toString();    //To change body of overridden methods use File | Settings | File Templates.
        }
    }
}
