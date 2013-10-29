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
package org.icefaces.ace.component.themeselect;

import org.icefaces.ace.renderkit.InputRenderer;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.Constants;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.impl.event.UIOutputWriter;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.util.EnvUtils;

import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.*;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@MandatoryResourceComponent(tagName = "themeSelect", value = "org.icefaces.ace.component.themeselect.ThemeSelect")
public class ThemeSelectRenderer extends InputRenderer {

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        //keep the theme updated in case the value binding changes its value
        String selectedTheme = ((ThemeSelect) component).getSelectedTheme(context);
        context.getExternalContext().getSessionMap().put(Constants.THEME_PARAM, selectedTheme);
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ThemeSelect themeSelectComponent = (ThemeSelect) component;
        super.encodeEnd(context, component);
        if (!component.isRendered()) {
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);
        String selectId = "select_" + clientId;
        String styleClass = (styleClass = themeSelectComponent.getStyleClass()) == null ? "" : styleClass.trim();

        writer.startElement("span", component);
        writer.writeAttribute("id", clientId, "id");
        ComponentUtils.enableOnElementUpdateNotify(writer, clientId);

        writer.writeAttribute("class", "ui-select-theme" + (styleClass.equals("") ? "" : " " + styleClass), null);

        writer.startElement("select", component);
        writer.writeAttribute("id", selectId, "id");
        writer.writeAttribute("name", selectId, "id");
        ComponentUtils.enableOnElementUpdateNotify(writer, selectId);
        String stateClass = "ui-state-default";
        if (themeSelectComponent.isDisabled()) {
            writer.writeAttribute("disabled", "disabled", "disabled");
            stateClass = "ui-state-disabled";
        }
        writer.writeAttribute("class", "ui-widget " + stateClass + getStateStyleClasses(themeSelectComponent), null);
        writeAttributes(context, themeSelectComponent, "accesskey", "dir", "label", "lang", "style", "tabindex", "title", "alt");
        writerSelAriaAttrs(context, themeSelectComponent);
        renderOptions(context, themeSelectComponent);
        writer.endElement("select");

        renderScript(context, themeSelectComponent);

        writer.startElement("span", null);
        writer.writeAttribute("class", "ui-helper-hidden", null);
        writer.write(Integer.toString(themeSelectComponent.getSelectedTheme(context).hashCode()) + Integer.toString(themeSelectComponent.getThemeList(context).hashCode()));
        writer.endElement("span");

        writer.endElement("span");
    }

    private static void writerSelAriaAttrs(FacesContext context, ThemeSelect component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        boolean ariaEnabled = EnvUtils.isAriaEnabled(context);
        if (!ariaEnabled) return;

        writer.writeAttribute("role", "listbox", null);
        if (component.isRequired()) {
            writer.writeAttribute("aria-required", "true", "required");
        }
        if (component.isDisabled()) {
            writer.writeAttribute("aria-disabled", "true", "disabled");
        }
        if (!component.isValid()) {
            writer.writeAttribute("aria-invalid", "true", null);
        }
        String label = component.getLabel();
        if (label != null) {
            writer.writeAttribute("aria-label", label, "label");
        }
    }

    private static void writeAttributes(FacesContext context, ThemeSelect component, String... keys) throws IOException {
        Object value;
        for (String key : keys) {
            value = component.getAttributes().get(key);
            if (value != null) {
                ResponseWriter writer = context.getResponseWriter();
                writer.writeAttribute(key, value, key);
            }
        }
    }

    private static void renderOptions(FacesContext context, ThemeSelect themeSelectComponent) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Collection<String> themeList = themeSelectComponent.getThemeList(context);
        String selectedTheme = themeSelectComponent.getSelectedTheme(context);

        for (String theme : themeList) {
            writer.startElement("option", null);
            writer.writeAttribute("value", theme, null);
            if (theme.equals(selectedTheme)) {
                writer.writeAttribute("selected", "selected", null);
            }
            writer.write(theme);
            writer.endElement("option");
        }
    }

    private void renderScript(FacesContext context, ThemeSelect component) throws IOException {
        JSONBuilder jb = JSONBuilder.create();
        encodeClientBehaviors(context, component, jb);

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("ice.ace.ThemeSelect.singleEntry(\"" + component.getClientId(context) + "\",{" + jb + "});");
        writer.endElement("script");
    }

    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String submittedValue = requestParameterMap.get("select_" + component.getClientId(context));
        if (submittedValue != null) {
            ((ThemeSelect) component).setSubmittedValue(submittedValue);
            //AddTheme needs the newly selected theme
            context.getExternalContext().getSessionMap().put(Constants.THEME_PARAM, submittedValue);
        }
        decodeBehaviors(context, component);
    }

//    public static class AddTheme implements SystemEventListener {
//        public void processEvent(SystemEvent event) throws AbortProcessingException {
//            FacesContext context = FacesContext.getCurrentInstance();
//            context.getViewRoot().addComponentResource(context, createThemeResource());
//        }
//
//        public boolean isListenerForSource(Object source) {
//            return source instanceof UIViewRoot;
//        }
//
//        private static UIComponent createThemeResource() {
//            UIComponent resourceComponent = new UIOutputWriter() {
//                public void encode(ResponseWriter writer, FacesContext context) throws IOException {
//                    ExternalContext externalContext = context.getExternalContext();
//                    String theme = (String) externalContext.getSessionMap().get(Constants.THEME_PARAM);
//                    if (theme == null) {
//                        String defaultTheme = externalContext.getInitParameter(Constants.THEME_PARAM);
//                        theme = defaultTheme == null ? "sam" : defaultTheme;
//                    } else {
//                        theme = theme.trim();
//                    }
//                    //acquire the selected theme
//                    String name;
//                    String library;
//                    if (theme.equalsIgnoreCase("sam")) {
//                        library = "icefaces.ace";
//                        name = "themes/sam/theme.css";
//                    } else if (theme.equalsIgnoreCase("rime")) {
//                        library = "icefaces.ace";
//                        name = "themes/rime/theme.css";
//                    } else {
//                        library = "ace-" + theme;
//                        name = "theme.css";
//                    }
//
//                    Resource resource = context.getApplication().getResourceHandler().createResource(name, library);
//                    writer.startElement("stylesheet", this);
//                    writer.writeAttribute("type", "text/css", null);
//                    writer.writeAttribute("src", resource.getRequestPath(), null);
//                    writer.endElement("stylesheet");
//                }
//            };
//            resourceComponent.setTransient(true);
//            Map attributes = resourceComponent.getAttributes();
//            attributes.put("name", "theme.css");
//            attributes.put("library", "icefaces.ace");
//            return resourceComponent;
//        }
//    }
}
