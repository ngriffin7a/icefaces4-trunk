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

package org.icefaces.impl.event;

import org.icefaces.impl.component.DefaultAction;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.IOException;
import java.util.logging.Logger;

public class FormSubmit implements SystemEventListener {
    private static final Logger LOGGER = Logger.getLogger(FormSubmit.class.getName());
    public static final String DISABLE_CAPTURE_SUBMIT = "DISABLE_CAPTURE_SUBMIT";
    private static final String CAPTURE_SUBMIT_SUFFIX = "_captureSubmit";
    private boolean deltaSubmit;
    private boolean partialStateSaving;

    public FormSubmit() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        deltaSubmit = EnvUtils.isDeltaSubmit(facesContext);
        partialStateSaving = EnvUtils.isPartialStateSaving(facesContext);
    }

    public void processEvent(final SystemEvent event) throws AbortProcessingException {
        final UIForm form = (UIForm) event.getSource();
        String componentId = form.getId() + CAPTURE_SUBMIT_SUFFIX;

        UIOutput scriptWriter = new UIOutputWriter() {
            public void encode(ResponseWriter writer, FacesContext context) throws IOException {
                if (form.getAttributes().get(DISABLE_CAPTURE_SUBMIT) != null) {
                    LOGGER.finer("UIForm has DISABLE_CAPTURE_SUBMIT attribute set, so core not rendering submit capturing script");
                    return;
                }
                Object keyMap = form.getAttributes().get(DefaultAction.class.getName());

                String formId = form.getClientId(context);
                writer.startElement("script", this);
                writer.writeAttribute("type", "text/javascript", "type");
                writer.writeAttribute("id", getClientId(context), "id");
                writer.write("ice.captureSubmit('" +
                formId +
                "'," +
                Boolean.toString(deltaSubmit) +
                ");" +
                "ice.captureKeypress('" +
                formId +
                "'," +
                (keyMap == null ? "null" : keyMap.toString())+
                ");");
                writer.endElement("script");
            }
        };

        scriptWriter.setId(componentId);
        scriptWriter.setTransient(true);
        form.getChildren().add(0, scriptWriter);

        AjaxDisabledWriter disabledWriter = new AjaxDisabledWriter();
        disabledWriter.setTransient(true);
        //add to end of list
        form.getChildren().add(disabledWriter);

    }

    public boolean isListenerForSource(final Object source) {
        if (!(source instanceof UIForm)) {
            return false;
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!EnvUtils.isICEfacesView(facesContext)) {
            return false;
        }
        UIForm htmlForm = (UIForm) source;
        if (htmlForm.getAttributes().get(DISABLE_CAPTURE_SUBMIT) != null) {
            LOGGER.finer("UIForm has DISABLE_CAPTURE_SUBMIT attribute set, so core not capturing submit");
            return false;
        }
        String componentId = htmlForm.getId() + CAPTURE_SUBMIT_SUFFIX;
        if (!partialStateSaving) {
            for (UIComponent child : htmlForm.getChildren()) {
                String id = child.getId();
                if (null != id && id.endsWith(CAPTURE_SUBMIT_SUFFIX)) {
                    return false;
                }
            }
        }

        // Guard against duplicates within the same JSF lifecycle
        for (UIComponent comp : htmlForm.getChildren()) {
            if (componentId.equals(comp.getId())) {
                return false;
            }
        }

        return true;
    }
}

class AjaxDisabledWriter extends UIOutputWriter {
    public void encode(ResponseWriter writer, FacesContext context)
            throws IOException {
        UIForm form = AjaxDisabledList.getContainingForm(this);
        //consume with remove to reset the list each time
        String value = (String) form.getAttributes()
                .remove(AjaxDisabledList.DISABLED_LIST);
        if (null == value) {
            return;
        }
        writer.startElement("input", this);
        writer.writeAttribute("type", "hidden", "type");
        writer.writeAttribute("autocomplete", "off", null);
        writer.writeAttribute("id", getClientId(context), "id");
        writer.writeAttribute("disabled", "true", "disabled");
        writer.writeAttribute("value", value, "value");
        writer.endElement("input");
    }

    public String getClientId(FacesContext context) {
        UIForm form = AjaxDisabledList.getContainingForm(this);
        return (form.getClientId() + UINamingContainer
                .getSeparatorChar(context) + "ajaxDisabled");
    }
}
