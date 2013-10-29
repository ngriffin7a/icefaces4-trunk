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

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.icefaces.util.EnvUtils;

/**
 * The purpose of this class is to forward propogate FacesMessage objects
 * across partialSubmit, singleSubmit, server push lifecycles. The issue being 
 * that, in a previous lifecycle, components may have added FacesMessages to 
 * the FacesContext due to failed validation, or the application may have 
 * programmatically added FacesMessages to the FacesContext, and then these 
 * inadvertent lifecycles occur, which not only remove all previous 
 * FacesMessage objects, but which also only execute subsets of the component 
 * tree, not giving the unexecuted components, nor the already invoked 
 * application code, a chance to re-add their FacesMessage objects to the 
 * FacesContext. This class saves away all FacesMessage objects, at the end 
 * of a lifecycle, to re-add these specific ones, before rendering, of the 
 * next lifecycle.
 */
public class FacesMessagesPhaseListener implements PhaseListener {
    private static final Logger LOGGER = Logger.getLogger(FacesMessagesPhaseListener.class.getName());

    private static final String SAVED_GLOBAL_FACES_MESSAGES_KEY = "org.icefaces.event.saved_global_faces_messages";
    private static final String SAVED_COMPONENT_FACES_MESSAGES_KEY = "org.icefaces.event.saved_component_faces_messages";

    public void afterPhase(final PhaseEvent phaseEvent) {
        // Do nothing.
    }

    protected void saveFacesMessages(FacesContext facesContext) {
        List<FacesMessage> globals = facesContext.getMessageList(null);
        Map<String, List<FacesMessage>> components = new LinkedHashMap<String, List<FacesMessage>>(6);
        Iterator<String> cids = facesContext.getClientIdsWithMessages();
        while (cids.hasNext()) {
            String clientId = cids.next();
            if (clientId == null) {
                continue;
            }
            List<FacesMessage> msgs = facesContext.getMessageList(clientId);
            components.put(clientId, msgs);
        }
        if (!globals.isEmpty()) {
            facesContext.getViewRoot().getAttributes().put(SAVED_GLOBAL_FACES_MESSAGES_KEY, globals);
        }
        if (!components.isEmpty()) {
            facesContext.getViewRoot().getAttributes().put(SAVED_COMPONENT_FACES_MESSAGES_KEY, components);
        }
    }

    public void beforePhase(final PhaseEvent phaseEvent) {
        // Restore any previously saved FacesMessage objects, back into the
        // FacesContext, if they meet the following criteria:
        // A. Have a clientId, and so are associated to a component
        //    Components NOT executed (not full execute, and not in partial execute subtree)
        //    (Possibly) Components still invalid
        // B. Global messages, not associated to a component 
        //    A partial execute, not a full execute
////if(!phaseEvent.getPhaseId().equals(PhaseId.RENDER_RESPONSE))return;
        FacesContext facesContext = phaseEvent.getFacesContext();
        if (facesContext.getViewRoot() != null) {
            Map viewMap = facesContext.getViewRoot().getViewMap();
            if ((viewMap.containsKey(EnvUtils.MESSAGE_PERSISTENCE) && ((Boolean)viewMap.get(EnvUtils.MESSAGE_PERSISTENCE))) ||
                (!viewMap.containsKey(EnvUtils.MESSAGE_PERSISTENCE) && EnvUtils.isMessagePersistence(facesContext))) {

                restoreFacesMessages(facesContext);
                saveFacesMessages(facesContext);
            }
        }
    }
    
    protected void restoreFacesMessages(FacesContext facesContext) {
        List<FacesMessage> globals =
            (List<FacesMessage>)
                facesContext.getViewRoot().getAttributes().remove(SAVED_GLOBAL_FACES_MESSAGES_KEY);
        Map<String, List<FacesMessage>> components = 
            (Map<String, List<FacesMessage>>)
                facesContext.getViewRoot().getAttributes().remove(SAVED_COMPONENT_FACES_MESSAGES_KEY);
        
//System.out.println("restoreFacesMessages    isExecuteAll     : " + facesContext.getPartialViewContext().isExecuteAll());
//System.out.println("restoreFacesMessages    isRenderAll      : " + facesContext.getPartialViewContext().isRenderAll());
//System.out.println("restoreFacesMessages    isAjaxRequest    : " + facesContext.getPartialViewContext().isAjaxRequest());
//System.out.println("restoreFacesMessages    isPartialRequest : " + facesContext.getPartialViewContext().isPartialRequest());
        if (globals != null && globals.size() > 0) {
            if (!fullExecute(facesContext)) {
                List<FacesMessage> newGlobals = facesContext.getMessageList(null);
                for (FacesMessage fm : globals) {
                    //TODO Check that this actually eliminated redundant additions
                    if (newGlobals.contains(fm)) {
                        continue;
                    }
                    boolean matchedSummaryAndDetail = false;
                    for (FacesMessage newGlobal : newGlobals) {
                        if (stringEquals(newGlobal.getSummary(), fm.getSummary()) &&
                            stringEquals(newGlobal.getDetail(), fm.getDetail())) {
                            matchedSummaryAndDetail = true;
                            break;
                        }
                    }
                    if (!matchedSummaryAndDetail) {
                        facesContext.addMessage(null, fm);
                    }
                }
            }
        }
        
        if (components != null && components.size() > 0) {
            //TODO Handle the case where it is a full execute, but another form was submitted
            if (fullExecute(facesContext)) {
            } else {
                /*
                char sep = UINamingContainer.getSeparatorChar(facesContext);
                */
                Collection<String> executeIds = facesContext.getPartialViewContext().getExecuteIds();
                // Determine which components were executed
                //TODO Possibly check if component still invalid. Hopefully don't have to check this
                Map<String, Boolean> clientId2Executed = new HashMap<String, Boolean>(components.size());
                Set<String> clientIds = components.keySet();
                EnumSet<VisitHint> hints = EnumSet.of(VisitHint.SKIP_UNRENDERED);
                VisitContext visitContext = VisitContext.createVisitContext(facesContext, clientIds, hints);
                VisitCallback vcall = new ComponentsExecutedByExecuteId(executeIds, clientId2Executed);
                facesContext.getViewRoot().visitTree(visitContext, vcall);
                // Re-add the FacesMessage(s) for the unexecuted components
                for (String clientId : clientIds) {
                    Boolean executedResult = clientId2Executed.get(clientId);
                    boolean executed = (executedResult != null && executedResult.booleanValue());
                    /*
                    for (String execId : executeIds) {
                        if (execId.length() > 0 &&
                            (clientId.equals(execId) || clientId.startsWith(execId + sep))) {
                            executed = true;
                            break;
                        }
                    }
                    */
                    if (!executed) {
                        List<FacesMessage> msgs = components.get(clientId);
                        List<FacesMessage> existingMsgs = facesContext.getMessageList(clientId);
                        for (FacesMessage fm : msgs) {
                            boolean matchedSummaryAndDetail = false;
                            if (existingMsgs != null) {
                                for (FacesMessage existing : existingMsgs) {
                                    if (stringEquals(existing.getSummary(), fm.getSummary()) &&
                                        stringEquals(existing.getDetail(), fm.getDetail())) {

                                        matchedSummaryAndDetail = true;
                                        break;
                                    }
                                }
                            }
                            if (!matchedSummaryAndDetail) {
                                facesContext.addMessage(clientId, fm);
                            }
                        }
                    }
                }
            }
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

    private static boolean fullExecute(FacesContext facesContext) {
        return !facesContext.getPartialViewContext().isPartialRequest() ||
                facesContext.getPartialViewContext().isExecuteAll();
    }

    private static boolean stringEquals(String s1, String s2) {
        if (s1 == s2) {
            return true;
        }
        else if (s1 == null && s2 != null) {
            return false;
        }
        else if (s1 != null && s2 == null) {
            return false;
        }
        else {
            return s1.equals(s2);
        }
    }
    
    private static class ComponentsExecutedByExecuteId implements VisitCallback {
        private Collection<String> executeIds;
        private Map<String, Boolean> clientId2Executed;
        
        ComponentsExecutedByExecuteId(
            Collection<String> executeIds,
            Map<String, Boolean> clientId2Executed) {
            this.executeIds = executeIds;
            this.clientId2Executed = clientId2Executed;
        }
        
        public VisitResult visit(
            VisitContext visitContext, UIComponent uiComponent) {
            FacesContext facesContext = visitContext.getFacesContext();
            String clientId = uiComponent.getClientId(facesContext);
            boolean executed = false;
            UIComponent currComp = uiComponent;
            String currClientId = clientId;
            while (true) {
                if (executeIds.contains(currClientId)) {
                    executed = true;
                    break;
                }
                currComp = currComp.getParent();
                if (currComp == null || currComp instanceof UIViewRoot) {
                    break;
                }
                currClientId = currComp.getClientId(facesContext);
            }
            clientId2Executed.put(clientId, executed);
            return VisitResult.ACCEPT;
        }
    }
}
