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

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import java.io.IOException;

import java.util.Map;

public class JSEventListener extends UICommand {
    public JSEventListener() {
    }

    public void setEvents(String events) {
        getStateHelper().put("events", events);
    }

    public String getEvents() {
        return (String) getStateHelper().get("events");
    }

    public void setHandler(String handler) {
        getStateHelper().put("handler", handler);
    }

    public String getHandler() {
        return (String) getStateHelper().get("handler");
    }

    public void decode(FacesContext context) {
            Map parameters = context.getExternalContext().getRequestParameterMap();
            if (parameters.containsKey(getClientId(context))) {
                String result = String.valueOf(parameters.get(getClientId(context)));
                if ("submitted".equals(result)) {
                    queueEvent(new JSEvent(this, parameters));
                }
            }
        }

    public void encodeBegin(FacesContext facesContext)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = getClientId(facesContext);
        writer.startElement("div", this);
        writer.writeAttribute("id", clientId, "id");
        String events = this.getEvents();
        String handler = this.getHandler();
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        if (events != null) {
            StringBuffer normalizedEvents = new StringBuffer("[");
            String[] evs = events.split(",");
            for (int i = 0; i < evs.length; i++) {
                String ev = evs[i];
                if (!"".equals(ev)) {
                    normalizedEvents.append("'");
                    normalizedEvents.append(ev.trim());
                    normalizedEvents.append("'");
                    normalizedEvents.append(',');
                }
            }
            normalizedEvents.setCharAt(normalizedEvents.length() - 1, ']');

            String handlerCode = handler == null ? "function() {return true;}" : handler;
            writer.writeText("var handler = " + handlerCode + ";", null);
            writer.writeText("var el = document.getElementById('" + clientId +"');", null);
            writer.writeText("el.submitOnEnter = 'disabled';", null);
            writer.writeText("var events = " + normalizedEvents + ";", null);
            writer.writeText("var callback = function(ev) { if (handler(ev)) ice.sef(ev, el, function(p) { p(el.id, 'submitted') }); };" , null);
            writer.writeText("for (var i = 0, l = events.length; i < l; i++) el[events[i]] = callback;", null);
        }
        writer.endElement("script");
    }

    public void encodeEnd(FacesContext facesContext) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement("div");
    }

    public static class JSEvent extends ActionEvent {
        private String captured;
        private String target;
        private String type;
        private String submitType;
        private String sourceElement;
        private long x, y;
        private boolean alt;
        private boolean ctrl;
        private boolean meta;
        private boolean shift;
        private int keycode;

        public JSEvent(UIComponent component, Map parameters) {
            super(component);
            if (parameters.containsKey("ice.event.captured")) {
                captured = (String) parameters.get("ice.event.captured");
            }
            if (parameters.containsKey("ice.event.target")) {
                target = (String) parameters.get("ice.event.target");
            }
            if (parameters.containsKey("ice.event.type")) {
                type = (String) parameters.get("ice.event.type");
            }
            if (parameters.containsKey("ice.submit.type")) {
                submitType = (String) parameters.get("ice.submit.type");
            }
            if (parameters.containsKey("javax.faces.source")) {
                sourceElement = (String) parameters.get("javax.faces.source");
            }
            if (parameters.containsKey("ice.event.x") && parameters.containsKey("ice.event.x")) {
                x = Long.valueOf((String) parameters.get("ice.event.x"));
                y = Long.valueOf((String) parameters.get("ice.event.y"));
            }
            if (parameters.containsKey("ice.event.alt")) {
                alt = Boolean.valueOf((String) parameters.get("ice.event.alt"));
            }
            if (parameters.containsKey("ice.event.ctrl")) {
                ctrl = Boolean.valueOf((String) parameters.get("ice.event.ctrl"));
            }
            if (parameters.containsKey("ice.event.meta")) {
                meta = Boolean.valueOf((String) parameters.get("ice.event.meta"));
            }
            if (parameters.containsKey("ice.event.shift")) {
                shift = Boolean.valueOf((String) parameters.get("ice.event.shift"));
            }
            if (parameters.containsKey("ice.event.keycode")) {
                keycode = Integer.valueOf((String) parameters.get("ice.event.keycode"));
            }
        }

        public String getCaptured() {
            return captured;
        }

        public String getTarget() {
            return target;
        }

        public String getType() {
            return type;
        }

        public String getSubmitType() {
            return submitType;
        }

        public String getSourceElement() {
            return sourceElement;
        }

        public long getX() {
            return x;
        }

        public long getY() {
            return y;
        }

        public boolean isAlt() {
            return alt;
        }

        public boolean isCtrl() {
            return ctrl;
        }

        public boolean isMeta() {
            return meta;
        }

        public boolean isShift() {
            return shift;
        }

        public int getKeycode() {
            return keycode;
        }

        public String toString() {
            return "JSEvent{" +
                    "captured='" + captured + '\'' +
                    ", target='" + target + '\'' +
                    ", type='" + type + '\'' +
                    ", submitType='" + submitType + '\'' +
                    ", source='" + sourceElement + '\'' +
                    ", x=" + x +
                    ", y=" + y +
                    ", alt=" + alt +
                    ", ctrl=" + ctrl +
                    ", meta=" + meta +
                    ", shift=" + shift +
                    ", keycode=" + keycode +
                    '}';
        }
    }
}
