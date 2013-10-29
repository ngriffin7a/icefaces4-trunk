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

package org.icefaces.ace.component.fileentry;

import org.icefaces.ace.util.HTML;
import org.icefaces.util.EnvUtils;
import org.icefaces.impl.event.FormSubmit;
import org.icefaces.impl.context.ICEFacesContextFactory;

import javax.faces.event.SystemEventListener;
import javax.faces.event.SystemEvent;
import javax.faces.event.AbortProcessingException;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIForm;
import java.util.Iterator;
import java.util.Map;
import java.io.IOException;
import java.util.logging.Logger;

public class FileEntryFormSubmit implements SystemEventListener {
    private static Logger log = Logger.getLogger(FileEntry.class.getName()+".script");

    static final String IFRAME_ID = "hiddenIframe";
    private static final String ID_SUFFIX = "_captureFileOnsubmit";
    private static final String ENCODED_URL_ID = "ice_fileEntry_encodedURL";
    private static final String ENCODED_URL_NAME = "ice.fileEntry.encodedURL";
    static final String FILE_ENTRY_MULTIPART_MARKER = "ice.fileEntry.multipart";
    static final String FILE_ENTRY_AJAX_RESPONSE_MARKER = "ice.fileEntry.ajaxResponse";
    private static final String AJAX_FORCED_VIEWS =
            ICEFacesContextFactory.AJAX_FORCED_VIEWS;
    private boolean partialStateSaving;

    public FileEntryFormSubmit()  {
        partialStateSaving = EnvUtils.isPartialStateSaving(
            FacesContext.getCurrentInstance() );
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        UIForm form = (UIForm) event.getSource();
        log.finer(
            "FileEntryFormSubmit.processEvent()\n" +
            "  event: " + event + "\n" +
            "  phase: " + context.getCurrentPhaseId() + "\n" +
            "  form.clientId: " + form.getClientId(context));

        if (!partialStateSaving)  {
            for (UIComponent child : form.getChildren())  {
                String id = child.getId();
                if ((null != id) && id.endsWith(ID_SUFFIX))  {
                    return;
                }
            }
        }

        // See if there is at least one FileEntry component in the form,
        // which should alter the form submission method.
        if (!foundFileEntry(form)) {
            log.finer("FileEntryFormSubmit  !foundFileEntry");
            return;
        }
        log.finer("FileEntryFormSubmit  foundFileEntry!");

        forceAjaxOnView(context);
        form.getAttributes().put(FormSubmit.DISABLE_CAPTURE_SUBMIT, "true");

        UIOutput urlOutput = new UIOutput() {
            public void encodeBegin(FacesContext context) throws IOException {
                // Similarly to how h:form renders the 'javax.faces.encodedURL'
                // hidden input field for partial submits, when
                // ExternalContext's encodeActionURL and encodePartialActionURL
                // values differ, except that we're passing in a parameter to
                // mark the multipart request as handled by FileEntry, and
                // will always use encodePartialActionURL, as that's correct
                // for both portlets and non-portlets.

                String clientId = getClientId(context);
                String viewId = context.getViewRoot().getViewId();
                String actionURL = context.getApplication().getViewHandler().
                    getActionURL(context, viewId);
                String prefix = actionURL.contains("?") ? "&" : "?";
                actionURL = actionURL + prefix + FILE_ENTRY_MULTIPART_MARKER + "=true";
                ExternalContext externalContext = context.getExternalContext();
                String encodedPartialActionURL = externalContext.encodePartialActionURL(actionURL);
                log.finer("RENDER ENCODED_URL  clientId: " + clientId + "  encodedPartialActionURL: " + encodedPartialActionURL);
                if (encodedPartialActionURL != null) {
                    ResponseWriter writer = context.getResponseWriter();
                    writer.startElement(HTML.INPUT_ELEM, this);
                    writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
                    writer.writeAttribute(HTML.ID_ATTR, clientId, "clientId");
                    writer.writeAttribute(HTML.NAME_ATTR, ENCODED_URL_NAME, null);
                    writer.writeAttribute(HTML.VALUE_ATTR, encodedPartialActionURL, "clientId");
                    writer.endElement(HTML.INPUT_ELEM);
                }
            }
            public void encodeEnd(FacesContext context) throws IOException {
            }
        };
        urlOutput.setId(ENCODED_URL_ID);
        urlOutput.setTransient(true);
        form.getChildren().add(0, urlOutput);

        FormScriptWriter scriptWriter = new FormScriptWriter(
            null, "_captureFileOnsubmit");
        form.getChildren().add(1, scriptWriter);

        UIOutput output = new UIOutput() {
            public void encodeBegin(FacesContext context) throws IOException {
                String clientId = getClientId(context);
                log.finer("RENDER IFRAME  clientId: " + clientId);
                ResponseWriter writer = context.getResponseWriter();
                writer.startElement("iframe", this);
                writer.writeAttribute("id", clientId, "clientId");
                writer.writeAttribute("name", clientId, "clientId");
                writer.writeAttribute("style", "display:none;", "style");
                writer.writeAttribute("src", "about:blank", "src");
                writer.endElement("iframe");
            }
            public void encodeEnd(FacesContext context) throws IOException {
            }
        };
        output.setId(IFRAME_ID);
        output.setTransient(true);
        form.getChildren().add(2, output);
    }
    
    private static boolean foundFileEntry(UIComponent parent) {
        Iterator<UIComponent> kids = parent.getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = kids.next();
            if (kid instanceof FileEntry) {
                return true;
            }
            if (foundFileEntry(kid)) {
                return true;
            }
        }
        return false;
    }

    public boolean isListenerForSource(Object source) {
        return source instanceof UIForm;
    }
    
    private void forceAjaxOnView(FacesContext facesContext)  {
        //ideally we would force this only for certain views
        //unfortunately the JSF view determinateion logic is not exposed
        //so we can only enable for a given session
        //Once the FileEntry component is used, all subsequent multipart
        //posts will have "Faces-Request: partial/ajax" set 
        ExternalContext externalContext = facesContext.getExternalContext();
        Map sessionMap = externalContext.getSessionMap();
        sessionMap.put(AJAX_FORCED_VIEWS, AJAX_FORCED_VIEWS);
    }
}
