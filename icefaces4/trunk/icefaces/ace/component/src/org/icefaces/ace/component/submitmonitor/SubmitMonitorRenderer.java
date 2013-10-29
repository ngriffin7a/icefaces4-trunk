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

package org.icefaces.ace.component.submitmonitor;

import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.ace.util.Utils;
import org.icefaces.render.MandatoryResourceComponent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

@MandatoryResourceComponent(tagName="submitMonitor", value="org.icefaces.ace.component.submitmonitor.SubmitMonitor")
public class SubmitMonitorRenderer extends CoreRenderer {
    @Override
    public void	encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId();
        SubmitMonitor monitor = (SubmitMonitor)component;

        writer.startElement(HTML.DIV_ELEM, monitor);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);

        // Encode Component
        writeComponent(context, writer, monitor, clientId);

        //Encode Script
        writeScript(context, writer, monitor, clientId);

        writer.endElement(HTML.DIV_ELEM);
    }

    private void writeScript(FacesContext context, ResponseWriter writer,
            SubmitMonitor monitor, String clientId) throws IOException {
        writer.startElement(HTML.DIV_ELEM, monitor);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_script", null);
        ComponentUtils.enableOnElementUpdateNotify(writer, clientId);

        writer.startElement(HTML.SCRIPT_ELEM, null);
        JSONBuilder json = JSONBuilder.create();
        json.initialiseVar(resolveWidgetVar(monitor));
        json.beginFunction("ice.ace.create");
        json.item("SubmitMonitor");
        json.beginArray();
        writeConfig(monitor, json);
        json.endArray();
        json.endFunction();
        writer.write(json.toString());
        writer.endElement(HTML.SCRIPT_ELEM);

        writer.endElement(HTML.DIV_ELEM);
    }

    private void writeComponent(FacesContext context, ResponseWriter writer,
            SubmitMonitor monitor, String clientId) throws IOException {
        writer.startElement(HTML.DIV_ELEM, monitor);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_display", null);
        Utils.writeConcatenatedStyleClasses(
            writer, "ice-sub-mon ui-widget", monitor.getStyleClass());
        if (Boolean.TRUE.equals(monitor.isHidingIdleSubmitMonitor())) {
            writer.writeAttribute(HTML.STYLE_ATTR, "display:none;", null);
        }

        // Don't give anything under here an id, since this section is cloned,
        // and we don't want duplicate id(s) in the DOM. It may be problematic
        // with the facet components having id(s).
        for (State state : State.values()) {
            writer.startElement(HTML.DIV_ELEM, monitor);
            // Start us off in the idle state. When first rendering, this
            // puts us in the right state, since we don't have any listeners
            // setup to transition us from active to idle. But on subsequent
            // renders, if the submitMonitor itself is updated, then things
            // could get dicey. But it's probably best to be back in idle
            // anyway.
            if (!state.equals(State.idle)) {
                writer.writeAttribute(HTML.STYLE_ATTR, "display:none;", null);
            }
            writer.writeAttribute(HTML.CLASS_ATTR, state.getMidClassName(), null);

            UIComponent facet = monitor.getFacet(state.name());
            if (facet != null) {
                facet.encodeAll(context);
            } else {
                writer.startElement(HTML.SPAN_ELEM, null);
                writer.writeAttribute(HTML.CLASS_ATTR, state.getImageClassName(), null);
                writer.endElement(HTML.SPAN_ELEM);

                String label = state.getLabel(monitor);
                if (label != null && label.length() > 0) {
                    writer.startElement(HTML.SPAN_ELEM, null);
                    writer.writeAttribute(HTML.CLASS_ATTR, state.getTextClassName(), null);
                    writer.writeText(label, null);
                    writer.endElement(HTML.SPAN_ELEM);
                }
            }
            writer.endElement(HTML.DIV_ELEM);
        }

        writer.endElement(HTML.DIV_ELEM);

        if (monitor.isPreload()) {
            writer.startElement(HTML.DIV_ELEM, monitor);
            writer.writeAttribute(HTML.ID_ATTR, clientId+"_preload", null);
            writer.writeAttribute(HTML.CLASS_ATTR, "ice-sub-mon ui-widget", null);
            writer.writeAttribute(HTML.STYLE_ATTR, "position:absolute;top:-99999px;left:-99999px;display:inline;width:0px;height:0px;padding:0px;margin:0px;", null);

            for (State state : State.values()) {
                writer.startElement(HTML.DIV_ELEM, monitor);
                writer.writeAttribute(HTML.CLASS_ATTR, state.getMidClassName(), null);

                UIComponent facet = monitor.getFacet(state.name());
                if (facet == null) {
                    writer.startElement(HTML.SPAN_ELEM, null);
                    writer.writeAttribute(HTML.CLASS_ATTR, state.getImageClassName(), null);
                    writer.endElement(HTML.SPAN_ELEM);

                    writer.startElement(HTML.SPAN_ELEM, null);
                    writer.writeAttribute(HTML.CLASS_ATTR, state.getTextClassName(), null);
                    writer.writeText("X", null);
                    writer.endElement(HTML.SPAN_ELEM);
                }
                writer.endElement(HTML.DIV_ELEM);
            }

            writer.endElement(HTML.DIV_ELEM);
        }
    }

    public JSONBuilder writeConfig(SubmitMonitor monitor, JSONBuilder config) {
        config.item(monitor.getClientId());
        config.beginMap();
        config.entry("id", monitor.getClientId());
        config.entryNonNullValue("blockUI", monitor.resolveBlockUI());
        config.entryNonNullValue("autoCenter", monitor.isAutoCenter());
        config.entryNonNullValue("monitorFor", monitor.resolveFor());
        config.endMap();
        return config;
    }


    private static enum State {
        idle {
            String getLabel(SubmitMonitor monitor) {
                return monitor.getIdleLabel();
            }
        },
        active {
            String getLabel(SubmitMonitor monitor) {
                return monitor.getActiveLabel();
            }
        },
        serverError {
            String getLabel(SubmitMonitor monitor) {
                return monitor.getServerErrorLabel();
            }
        },
        networkError {
            String getLabel(SubmitMonitor monitor) {
                return monitor.getNetworkErrorLabel();
            }
        },
        sessionExpired {
            String getLabel(SubmitMonitor monitor) {
                return monitor.getSessionExpiredLabel();
            }
        };

        abstract String getLabel(SubmitMonitor monitor);
        String getMidClassName() {
            return MID_CLASS_NAMES[ordinal()];
        }
        String getImageClassName() {
            return IMG_CLASS_NAMES[ordinal()];
        }
        String getTextClassName() {
            return TXT_CLASS_NAMES[ordinal()];
        }

        // Implement as interned String constants instead of concatenated
        private static final String[] MID_CLASS_NAMES = new String[] {
            "ice-sub-mon-mid idle",
            "ice-sub-mon-mid active",
            "ice-sub-mon-mid serverError ui-state-error",
            "ice-sub-mon-mid networkError ui-state-error",
            "ice-sub-mon-mid sessionExpired ui-state-error"
        };
        private static final String[] IMG_CLASS_NAMES = new String[] {
            "ice-sub-mon-img",
            "ice-sub-mon-img",
            "ice-sub-mon-img ui-icon ui-icon-alert",
            "ice-sub-mon-img ui-icon ui-icon-alert",
            "ice-sub-mon-img ui-icon ui-icon-clock"
        };
        private static final String[] TXT_CLASS_NAMES = new String[] {
            "ice-sub-mon-txt",
            "ice-sub-mon-txt",
            "ice-sub-mon-txt ui-state-error-text",
            "ice-sub-mon-txt ui-state-error-text",
            "ice-sub-mon-txt ui-state-error-text"
        };
    }
}
