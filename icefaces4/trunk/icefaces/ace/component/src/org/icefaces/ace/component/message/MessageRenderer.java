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
package org.icefaces.ace.component.message;

import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.util.EnvUtils;

import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@MandatoryResourceComponent(tagName = "message", value = "org.icefaces.ace.component.message.Message")
public class MessageRenderer extends Renderer {

    private static final String[] icons = new String[]{"info", "notice", "alert", "alert"};
    private static final String[] states = new String[]{"highlight", "highlight", "error", "error"};
    private static final Set<String> effectSet = new HashSet<String>(Arrays.asList("blind", "bounce", "clip", "drop", "explode", "fade", "fold", "highlight", "puff", "pulsate", "scale", "shake", "size", "slide"));
    private static final Set<String> durationSet = new HashSet<String>(Arrays.asList("slow", "_default", "fast"));
    private static final Logger logger = Logger.getLogger(MessageRenderer.class.getName());

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        Message message = (Message) component;
        String forId = (forId = message.getFor()) == null ? "" : forId.trim();
        String styleClass = "ui-faces-message" + ((styleClass = message.getStyleClass()) == null ? "" : " " + styleClass);
        boolean ariaEnabled = EnvUtils.isAriaEnabled(context);
        String sourceMethod = "encodeEnd";

        UIComponent forComponent = forId.equals("") ? null : message.findComponent(forId);
        if (forComponent == null) {
            log(Level.WARNING, sourceMethod, "'for' attribute value cannot be null or empty or non-existent id.");
            return;
        }
        Iterator messageIter = context.getMessages(forComponent.getClientId(context));

        writer.startElement("span", message);
        String clientId = message.getClientId();
        writer.writeAttribute("id", clientId, "id");
        ComponentUtils.enableOnElementUpdateNotify(writer, clientId);
        writer.writeAttribute("class", styleClass, null);
        writeAttributes(writer, component, "lang", "style", "title");
        if (ariaEnabled) {
            writer.writeAttribute("role", "alert", null);
            writer.writeAttribute("aria-atomic", "true", null);
            writer.writeAttribute("aria-live", "polite", null);
            writer.writeAttribute("aria-relevant", "all", null);
        }

        writer.startElement("span", message);
        writer.writeAttribute("id", clientId + "_msg", "id");
        boolean rendered = false;
        FacesMessage facesMessage;
        while (messageIter.hasNext()) {
            facesMessage = (FacesMessage) messageIter.next();
            if (!facesMessage.isRendered() || message.isRedisplay()) {
                encodeMessage(writer, message, facesMessage, "");
                /*encodeMessage(writer, message, facesMessage, styleClass);*/
                rendered = true;
                break;
            }
        }
        if (!rendered) {
            message.setCurrText("");
            /*writer.writeAttribute("class", styleClass, null);*/
        }
        writer.endElement("span");

        String event = "", effect = "", duration = "";
        String prevText = (prevText = message.getPrevText()) != null ? prevText : "";
        String currText = (currText = message.getCurrText()) != null ? currText : "";
        if (prevText.equals("") && !currText.equals("")) {
            event = "init";
            effect = (effect = message.getInitEffect()) != null ? effect.trim() : "";
            logInvalid(effectSet, "effect", effect, sourceMethod);
            effect = effectSet.contains(effect) ? effect : "";
            duration = (duration = message.getInitEffectDuration()) != null ? duration.trim() : "";
        } else if (!prevText.equals("") && !currText.equals("") && !prevText.equals(currText)) {
            event = "change";
            effect = (effect = message.getChangeEffect()) != null ? effect.trim() : "";
            logInvalid(effectSet, "effect", effect, sourceMethod);
            effect = effectSet.contains(effect) ? effect : "";
            duration = (duration = message.getChangeEffectDuration()) != null ? duration.trim() : "";
        }
        if (!(event.equals("") || effect.equals(""))) {
            JSONBuilder jb = JSONBuilder.create();
            jb.beginMap()
                    .entry("id", clientId)
                    .entry("event", event)
                    .entry("effect", effect);
            try {
                jb.entry("duration", Integer.parseInt(duration));
            } catch (NumberFormatException e) {
                logInvalid(durationSet, "duration", duration, sourceMethod);
                duration = durationSet.contains(duration) ? duration : "_default";
                jb.entry("duration", duration);
            }
            jb.endMap();
            writer.startElement("script", null);
            writer.write("ice.ace.Message.factory(" + jb + ");//" + UUID.randomUUID());
            writer.endElement("script");
        }

        writer.endElement("span");
        message.setPrevText(message.getCurrText());
    }

    private void encodeMessage(ResponseWriter writer, Message message, FacesMessage facesMessage, String styleClass) throws IOException {

        boolean showSummary = message.isShowSummary();
        boolean showDetail = message.isShowDetail();
        String summary = (null != (summary = facesMessage.getSummary())) ? summary : "";
        String detail = (null != (detail = facesMessage.getDetail())) ? detail : ""; // Mojarra defaults to summary. Not good.
        String text = ((showSummary ? summary : "") + " " + (showDetail ? detail : "")).trim();
        message.setCurrText(text);
        int ordinal = (ordinal = FacesMessage.VALUES.indexOf(facesMessage.getSeverity())) > -1 && ordinal < states.length ? ordinal : 0;

        if (text.equals("")) {
            styleClass += " ui-empty-message";
        }
        writer.writeAttribute("class", styleClass + " ui-widget ui-corner-all ui-state-" + states[ordinal], null);

        writer.startElement("span", message);
        writer.writeAttribute("class", "ui-faces-message-icon", null);
        writer.startElement("span", message);
        writer.writeAttribute("class", "ui-icon ui-icon-" + icons[ordinal], null);
        writer.endElement("span");
        writer.endElement("span");

        writer.startElement("span", message);
        writer.writeAttribute("class", "ui-faces-message-text", null);
        if (!text.equals("")) {
            if (message.isEscape()) {
                writer.writeText(text, message, null);
            } else {
                writer.write(text);
            }
        }
        writer.endElement("span");

        facesMessage.rendered();
    }

    private void writeAttributes(ResponseWriter writer, UIComponent component, String... keys) throws IOException {
        Object value;
        for (String key : keys) {
            value = component.getAttributes().get(key);
            if (value != null) {
                writer.writeAttribute(key, value, key);
            }
        }
    }

    private void log(Level level, String sourceMethod, String message) {
        if (!FacesContext.getCurrentInstance().isProjectStage(ProjectStage.Development)) return;
        logger.logp(level, logger.getName(), sourceMethod, message);
    }

    private void logInvalid(Set<String> validSet, String name, String value, String sourceMethod) {
        if (!value.equals("") && !validSet.contains(value)) {
            log(Level.WARNING, sourceMethod, "Invalid " + name + " \"" + value + "\" reset to default. Read TLD doc.");
        }
    }
}
