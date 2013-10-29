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

import org.icefaces.impl.context.DOMPartialViewContext;
import org.icefaces.impl.util.CoreUtils;
import org.icefaces.apache.commons.fileupload.FileItemStream;
import org.icefaces.apache.commons.fileupload.FileItemIterator;
import org.icefaces.apache.commons.fileupload.servlet.ServletFileUpload;
import org.icefaces.apache.commons.fileupload.util.Streams;
import org.icefaces.util.EnvUtils;

import javax.faces.application.ProjectStage;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.event.FacesEvent;
import javax.faces.context.PartialViewContext;
import javax.faces.context.FacesContext;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.component.UIComponent;
import javax.faces.application.FacesMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.lang.reflect.Constructor;
import java.util.*;
import java.io.InputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileEntryPhaseListener implements PhaseListener {
    private static Logger log = Logger.getLogger(FileEntry.class.getName());

    public void afterPhase(PhaseEvent phaseEvent) {
        log.fine(
            "FileEntryPhaseListener.afterPhase()\n" +
            "  phaseId         : " + phaseEvent.getPhaseId() + "\n" +
            "  renderResponse  : " + phaseEvent.getFacesContext().getRenderResponse() + "\n" +
            "  responseComplete: " + phaseEvent.getFacesContext().getResponseComplete());
        if (phaseEvent.getPhaseId().equals(PhaseId.PROCESS_VALIDATIONS)) {
            log.finer("FileEntryPhaseListener.afterPhase()  FileEntry.removeResults()");
            FileEntry.removeResults(phaseEvent.getFacesContext());
        }
    }

    public void beforePhase(PhaseEvent phaseEvent) {
        log.fine("FileEntryPhaseListener.beforePhase()  phaseId: " + phaseEvent.getPhaseId());
        if (phaseEvent.getPhaseId().equals(PhaseId.RENDER_RESPONSE)) {
            log.finer("FileEntryPhaseListener.beforePhase()\n" +
                "  ajaxRequest   : " + phaseEvent.getFacesContext().getPartialViewContext().isAjaxRequest() + "\n" +
                "  partialRequest: " + phaseEvent.getFacesContext().getPartialViewContext().isPartialRequest());
            final Map<String, FacesEvent> clientId2FacesEvent = FileEntry.
                removeEventsForPreRender(phaseEvent.getFacesContext());
            if (clientId2FacesEvent != null) {
                Set<String> clientIds = clientId2FacesEvent.keySet();
                EnumSet<VisitHint> hints = EnumSet.of(
                    VisitHint.SKIP_UNRENDERED);
                VisitContext visitContext = VisitContext.createVisitContext(
                    phaseEvent.getFacesContext(), clientIds, hints);
                VisitCallback vcall = new VisitCallback() {
                    public VisitResult visit(VisitContext visitContext,
                                             UIComponent uiComponent) {
                        FacesContext facesContext = visitContext.getFacesContext();
                        String clientId = uiComponent.getClientId(facesContext);
                        FacesEvent event = clientId2FacesEvent.get(clientId);
                        log.finer("FileEntryPhaseListener  pre-Render  clientId: " + clientId + "  event: " + event);
                        if (event != null) {
                            uiComponent.broadcast(event);
                        }
                        return VisitResult.REJECT;
                    }
                };
                phaseEvent.getFacesContext().getViewRoot().visitTree(
                    visitContext, vcall);
            }
            return;
        }

        //if (!phaseEvent.getPhaseId().equals(PhaseId.RESTORE_VIEW))
        //    return;
        // Old place for multipart handling
    }

    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }
}
