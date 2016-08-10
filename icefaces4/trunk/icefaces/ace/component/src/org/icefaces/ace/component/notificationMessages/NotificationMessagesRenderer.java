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

package org.icefaces.ace.component.notificationMessages;

import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.render.MandatoryResourceComponent;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@MandatoryResourceComponent(tagName = "notificationMessages", value = "org.icefaces.ace.component.notificationMessages.NotificationMessages")
public class NotificationMessagesRenderer extends Renderer {
    private static final Logger logger = Logger.getLogger(NotificationMessagesRenderer.class.getName());

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        NotificationMessages notificationMessages = (NotificationMessages) component;
        String clientId = notificationMessages.getClientId();
        boolean showSummary = notificationMessages.isShowSummary();
        boolean showDetail = notificationMessages.isShowDetail();

        ComponentUtils.enableOnElementUpdateNotify(writer, clientId);

        String forId = (forId = notificationMessages.getFor()) == null ? "@all" : forId.trim();
        String sourceMethod = "encodeEnd";
        final List<String> idsInView;
        final Iterator iterator;
        if (forId.equals("@all")) {
            iterator = context.getMessages();
        } else if (forId.equals("@inView")){
            idsInView = ComponentUtils.findIdsInView(context);
            if (idsInView.isEmpty()){
                logger.logp(Level.WARNING, logger.getName(), sourceMethod, " no components in view for use of @inView value");
                iterator = Collections.emptyList().iterator();
            } else  {
                iterator = ComponentUtils.getMessagesInView(context, idsInView);
            }
        } else {
            UIComponent forComponent = forId.equals("") ? null : notificationMessages.findComponent(forId);
            if (forComponent == null) {
                logger.logp(Level.WARNING, logger.getName(), sourceMethod, "'for' attribute value cannot be empty or non-existent id.");
                iterator = Collections.emptyList().iterator();
            } else {
                iterator = context.getMessages(forComponent.getClientId(context));
            }
        }

        writer.startElement("span", notificationMessages);
        writer.writeAttribute("id", clientId, "id");

        while (iterator.hasNext()) {
            FacesMessage facesMessage = (FacesMessage) iterator.next();
            if (!facesMessage.isRendered() || notificationMessages.isRedisplay()) {
                writer.startElement("script", null);
                writer.writeAttribute("type", "text/javascript", null);
                String summary = (null != (summary = facesMessage.getSummary())) ? summary : "";
                String detail = (null != (detail = facesMessage.getDetail())) ? detail : "";
                String text = ((showSummary ? summary : "") + " " + (showDetail ? detail : "")).trim();
                //write out JS notification init code
                writer.writeText("new Notification('", null);
                writer.writeText(notificationMessages.getHeader(), null);

                JSONBuilder jb = JSONBuilder.create();
                jb.beginMap();
                jb.entry("body", text);
                jb.entry("sticky", !notificationMessages.isAutoHide());
                jb.entry("dir", notificationMessages.getDir());
                final String iconURL = notificationMessages.getIconURL();
                if (iconURL != null) {
                    jb.entry("icon", iconURL);
                }
                final String lang = notificationMessages.getLang();
                if (lang != null) {
                    jb.entry("lang", lang);
                }
                jb.endMap();
                writer.writeText("', ", null);
                writer.writeText(jb.toString(), null);
                writer.writeText(");//", null);
                writer.writeText(UUID.randomUUID(), null);
                writer.endElement("script");
                facesMessage.rendered();
            }
        }

        writer.endElement("span");
    }
}
