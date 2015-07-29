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

package org.icefaces.ace.component.fileentry;

import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.ScriptWriter;
import org.icefaces.impl.application.WindowScopeManager;
import org.icefaces.impl.context.ICEFacesContextFactory;
import org.icefaces.impl.event.BridgeSetup;
import org.icefaces.impl.event.FormSubmit;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIOutput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.render.ResponseStateManager;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

public class FileEntryFormSubmit implements SystemEventListener {
    private static Logger log = Logger.getLogger(FileEntry.class.getName() + ".script");

    static final String IFRAME_ID = "hiddenIframe";
    private static final String ID_SUFFIX = "_captureFileOnsubmit";
    private static final String ENCODED_URL_ID = "ice_fileEntry_encodedURL";
    private static final String ENCODED_URL_NAME = "ice.fileEntry.encodedURL";
    static final String FILE_ENTRY_MULTIPART_MARKER = "ice.fileEntry.multipart";
    static final String FILE_ENTRY_AJAX_RESPONSE_MARKER = "ice.fileEntry.ajaxResponse";
    private static final String AJAX_FORCED_VIEWS =
            ICEFacesContextFactory.AJAX_FORCED_VIEWS;
    private boolean partialStateSaving;

    public FileEntryFormSubmit() {
        partialStateSaving = EnvUtils.isPartialStateSaving(
                FacesContext.getCurrentInstance());
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        final FacesContext context = FacesContext.getCurrentInstance();
        final UIForm form = (UIForm) event.getSource();
        log.finer(
                "FileEntryFormSubmit.processEvent()\n" +
                        "  event: " + event + "\n" +
                        "  phase: " + context.getCurrentPhaseId() + "\n" +
                        "  form.clientId: " + form.getClientId(context));

        if (!partialStateSaving) {
            for (UIComponent child : form.getChildren()) {
                String id = child.getId();
                if ((null != id) && id.endsWith(ID_SUFFIX)) {
                    return;
                }
            }
        }

        final FileEntry fileEntry = findFileEntry(form);
        log.finer("FileEntryFormSubmit  findFileEntry!");

        forceAjaxOnView(context);
        form.getAttributes().put(FormSubmit.DISABLE_CAPTURE_SUBMIT, "true");
        form.setInView(false);
        context.getApplication().subscribeToEvent(PreRenderViewEvent.class, new ReEnableCaptureSubmit(form.getId()));

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
                String actionURL = context.getApplication().getViewHandler().getActionURL(context, viewId);
                ExternalContext externalContext = context.getExternalContext();
                String viewState = context.getApplication().getStateManager().getViewState(context);

                StringBuffer url = new StringBuffer(actionURL);
                url.append(actionURL.contains("?") ? "&" : "?");
                url.append(FILE_ENTRY_MULTIPART_MARKER);
                url.append("=true");
                url.append("&");
                url.append(ResponseStateManager.VIEW_STATE_PARAM);
                url.append("=");
                url.append(EnvUtils.isMyFaces() ? URLEncoder.encode(viewState, "UTF-8") : viewState);
                url.append("&ice.window=");
                url.append(externalContext.getClientWindow().getId());
                url.append("&ice.view=");
                url.append(BridgeSetup.getViewID(externalContext));

                String encodedPartialActionURL = externalContext.encodePartialActionURL(url.toString());
                log.finer("RENDER ENCODED_URL  clientId: " + clientId + "  encodedPartialActionURL: " + encodedPartialActionURL);
                ResponseWriter writer = context.getResponseWriter();
                if (encodedPartialActionURL != null) {
                    writer.startElement(HTML.INPUT_ELEM, this);
                    writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
                    writer.writeAttribute(HTML.ID_ATTR, clientId, "clientId");
                    writer.writeAttribute(HTML.NAME_ATTR, ENCODED_URL_NAME, null);
                    writer.writeAttribute(HTML.VALUE_ATTR, encodedPartialActionURL, "clientId");
                    writer.endElement(HTML.INPUT_ELEM);
                }
                writer.startElement(HTML.INPUT_ELEM, this);
                writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
                writer.writeAttribute(HTML.NAME_ATTR, "file-entry-id", null);
                writer.writeAttribute(HTML.VALUE_ATTR, fileEntry.getId(), null);
                writer.endElement(HTML.INPUT_ELEM);
            }

            public void encodeEnd(FacesContext context) throws IOException {
            }
        };
        urlOutput.setId(fileEntry.getId() + ENCODED_URL_ID);
        urlOutput.setTransient(true);
        form.getChildren().add(0, urlOutput);

        UIOutput output = new UIOutput() {
            public void encodeBegin(FacesContext context) throws IOException {
                //TODO render into javascript. Probably have to scrape out notion of MessageFormat.
                String clientId = getClientId(context);
                String progressResourcePath = "";
                String progressPushId = "";
                if (PushUtils.isPushPresent()) {
                    progressResourcePath = PushUtils.getProgressResourcePath(context, form);
                    final Map<String, Object> windowMap = WindowScopeManager.lookupWindowScope(context);
                    progressPushId = (String) windowMap.get(FileEntryFormSubmit.class.getName());
                    if (progressPushId == null) {
                        progressPushId = PushUtils.getPushId(context, form);
                        windowMap.put(FileEntryFormSubmit.class.getName(), progressPushId);
                    }
                }

                String script = "ice.ace.fileentry.captureFormOnsubmit('" + form.getClientId() + "', '" + clientId + "', '" +
                        progressPushId + "', '" + progressResourcePath + "');";
                ScriptWriter.insertScript(context, this, script);

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
        output.setId(fileEntry.getId() + IFRAME_ID);
        output.setTransient(true);
        form.getChildren().add(1, output);
        form.setInView(true);
    }

    private static FileEntry findFileEntry(UIComponent parent) {
        Iterator<UIComponent> kids = parent.getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = kids.next();
            if (kid instanceof FileEntry) {
                return (FileEntry) kid;
            }
            final FileEntry grandKid = findFileEntry(kid);
            if (grandKid != null) {
                return grandKid;
            }
        }
        return null;
    }

    public boolean isListenerForSource(Object source) {
        return source instanceof UIForm && findFileEntry((UIForm) source) != null;
    }

    private void forceAjaxOnView(FacesContext facesContext) {
        //ideally we would force this only for certain views
        //unfortunately the JSF view determinateion logic is not exposed
        //so we can only enable for a given session
        //Once the FileEntry component is used, all subsequent multipart
        //posts will have "Faces-Request: partial/ajax" set 
        ExternalContext externalContext = facesContext.getExternalContext();
        Map sessionMap = externalContext.getSessionMap();
        sessionMap.put(AJAX_FORCED_VIEWS, AJAX_FORCED_VIEWS);
    }

    private static class ReEnableCaptureSubmit implements SystemEventListener {
        private final String id;

        public ReEnableCaptureSubmit(String id) {
            this.id = id;
        }

        public void processEvent(SystemEvent event) throws AbortProcessingException {
            final FacesContext ctx = FacesContext.getCurrentInstance();
            UIComponent f = ctx.getViewRoot().findComponent(id);
            f.getAttributes().remove(FormSubmit.DISABLE_CAPTURE_SUBMIT);
            ctx.getApplication().unsubscribeFromEvent(PreRenderViewEvent.class, this);
        }

        public boolean isListenerForSource(Object source) {
            return true;
        }
    }
}
