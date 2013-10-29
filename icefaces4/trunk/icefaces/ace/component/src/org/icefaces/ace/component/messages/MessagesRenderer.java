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
package org.icefaces.ace.component.messages;

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

@MandatoryResourceComponent(tagName = "messages", value = "org.icefaces.ace.component.messages.Messages")
public class MessagesRenderer extends Renderer {

    private static final String[] icons = new String[]{"info", "notice", "alert", "alert"};
    private static final String[] states = new String[]{"highlight", "highlight", "error", "error"};
    private static final Set<String> effectSet = new HashSet<String>(Arrays.asList("blind", "bounce", "clip", "drop", "explode", "fade", "fold", "highlight", "puff", "pulsate", "scale", "shake", "size", "slide"));
    private static final Set<String> durationSet = new HashSet<String>(Arrays.asList("slow", "_default", "fast"));
    private static final Logger logger = Logger.getLogger(MessagesRenderer.class.getName());

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        Messages messages = (Messages) component;
        String style = messages.getStyle();
        String styleClass = (styleClass = messages.getStyleClass()) == null ? "" : " " + styleClass;
        boolean ariaEnabled = EnvUtils.isAriaEnabled(context);

        writer.startElement("div", messages);
        String clientId = messages.getClientId();
        writer.writeAttribute("id", clientId, "id");
        ComponentUtils.enableOnElementUpdateNotify(writer, clientId);
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }
        writer.writeAttribute("class", "ui-faces-messages ui-widget" + styleClass, null);
        if (ariaEnabled) {
            writer.writeAttribute("role", "alert", null);
            writer.writeAttribute("aria-atomic", "true", null);
            writer.writeAttribute("aria-live", "polite", null);
            writer.writeAttribute("aria-relevant", "all", null);
        }

        int count = 1;
        Map<String, ArrayList<String>> prevMsgs = messages.getPrevMsgs(), currMsgs = new HashMap<String, ArrayList<String>>();
        ArrayList<String> prevMsgsForId, currMsgsForId;
        Map<String, ArrayList<FacesMessage>> msgs = getMsgs(messages, context);
        String msgClientId, msgText;
        ArrayList<FacesMessage> msgList;
        ArrayList<Integer> removeList = new ArrayList<Integer>();
        FacesMessage facesMessage;
        // Logic to distinguish between "init" and "change" done according to Mark's suggestion.
        for (Map.Entry<String, ArrayList<FacesMessage>> entry : msgs.entrySet()) {
            msgClientId = entry.getKey();
            msgList = entry.getValue();
            removeList.clear();
            prevMsgsForId = (prevMsgsForId = prevMsgs.get(msgClientId)) == null ? new ArrayList<String>() : prevMsgsForId;
            currMsgsForId = new ArrayList<String>();
            for (int i = 0; i < msgList.size(); i++) {
                facesMessage = msgList.get(i);
                if (!facesMessage.isRendered() || messages.isRedisplay()) {
                    msgText = getMsgText(messages, facesMessage);
                    if (prevMsgsForId.contains(msgText)) {
                        encodeMessage(writer, messages, facesMessage, msgClientId, count++, msgText, "");
                        prevMsgsForId.remove(msgText);
                        removeList.add(i);
                        currMsgsForId.add(msgText);
                    }
                }
            }
            for (int i = removeList.size()-1; i >= 0; i--) {
                msgList.remove(removeList.get(i).intValue());
            }
            for (int i = 0; i < msgList.size(); i++) {
                facesMessage = msgList.get(i);
                if (!facesMessage.isRendered() || messages.isRedisplay()) {
                    msgText = getMsgText(messages, facesMessage);
                    encodeMessage(writer, messages, facesMessage, msgClientId, count++, msgText, i >= prevMsgsForId.size() ? "init" : "change");
                    currMsgsForId.add(msgText);
                }
            }
            if (!currMsgsForId.isEmpty()) {
                currMsgs.put(msgClientId, currMsgsForId);
            }
        }
        writer.endElement("div");
        messages.setPrevMsgs(currMsgs);
    }

    private void encodeMessage(ResponseWriter writer, Messages messages, FacesMessage facesMessage, String clientId, int count, String text, String event) throws IOException {

        String sourceMethod = "encodeMessage";
        int ordinal = (ordinal = FacesMessage.VALUES.indexOf(facesMessage.getSeverity())) > -1 && ordinal < states.length ? ordinal : 0;

        writer.startElement("div", messages);
        writer.writeAttribute("id", clientId + "_msg" + count, "id");
        writer.writeAttribute("class", "ui-corner-all ui-state-" + states[ordinal] + (text.equals("") ? " ui-empty-message" : ""), null);
        writeAttributes(writer, messages, "lang", "title");

        writer.startElement("span", messages);
        writer.writeAttribute("class", "ui-icon ui-icon-" + icons[ordinal], null);
        writer.endElement("span");

        if (!text.equals("")) {
            if (messages.isEscape()) {
                writer.writeText(text, messages, null);
            } else {
                writer.write(text);
            }
        }
        writer.endElement("div");

        String effect = "", duration = "";
        if (event.equals("init")) {
            effect = (effect = messages.getInitEffect()) != null ? effect.trim() : "";
            logInvalid(effectSet, "effect", effect, sourceMethod);
            effect = effectSet.contains(effect) ? effect : "";
            duration = (duration = messages.getInitEffectDuration()) != null ? duration.trim() : "";
        } else if (event.equals("change")) {
            effect = (effect = messages.getChangeEffect()) != null ? effect.trim() : "";
            logInvalid(effectSet, "effect", effect, sourceMethod);
            effect = effectSet.contains(effect) ? effect : "";
            duration = (duration = messages.getChangeEffectDuration()) != null ? duration.trim() : "";
        }
        if (!(event.equals("") || effect.equals(""))) {
            JSONBuilder jb = JSONBuilder.create();
            jb.beginMap()
                    .entry("id", clientId)
                    .entry("count", count)
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

    private Map<String, ArrayList<FacesMessage>> getMsgs(Messages component, FacesContext context) {
        Map<String, ArrayList<FacesMessage>> msgs = new HashMap<String, ArrayList<FacesMessage>>();
        String forId = (forId = component.getFor()) == null ? "@all" : forId.trim();
        if (forId.equals("@all")) {
            if (component.isGlobalOnly()) {
                addMsgs(context, null, msgs);
            } else {
                Iterator<String> iterator = context.getClientIdsWithMessages();
                while (iterator.hasNext()) {
                    addMsgs(context, iterator.next(), msgs);
                }
            }
        } else {
            UIComponent forComponent = forId.equals("") ? null : component.findComponent(forId);
            if (forComponent == null) {
                logger.logp(Level.WARNING, logger.getName(), "getMsgs", "'for' attribute value cannot be empty or non-existent id.");
            } else {
                addMsgs(context, forComponent.getClientId(context), msgs);
            }
        }
        return msgs;
    }

    private void addMsgs(FacesContext context, String clientId, Map<String, ArrayList<FacesMessage>> msgs) {
        Iterator<FacesMessage> iterator = context.getMessages(clientId);
        if (clientId == null) {
            clientId = "*";
        }
        while (iterator.hasNext()) {
            if (!msgs.containsKey(clientId)) {
                msgs.put(clientId, new ArrayList<FacesMessage>());
            }
            msgs.get(clientId).add(iterator.next());
        }
    }

    private String getMsgText(Messages messages, FacesMessage facesMessage) throws IOException {
        boolean showSummary = messages.isShowSummary();
        boolean showDetail = messages.isShowDetail();
        String summary = (null != (summary = facesMessage.getSummary())) ? summary : "";
        String detail = (null != (detail = facesMessage.getDetail())) ? detail : ""; // Mojarra defaults to summary. Not good.
        return ((showSummary ? summary : "") + " " + (showDetail ? detail : "")).trim();
    }
}
