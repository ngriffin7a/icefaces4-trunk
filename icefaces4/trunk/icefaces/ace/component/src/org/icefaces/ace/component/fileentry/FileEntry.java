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

import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.ace.util.Utils;
import org.icefaces.component.Focusable;
import org.icefaces.impl.event.BridgeSetup;
import org.icefaces.util.JavaScriptRunner;

import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.AbortProcessingException;
import javax.faces.component.UIForm;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.MethodExpression;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileEntry extends FileEntryBase implements Focusable {
    private static Logger log = Logger.getLogger(FileEntry.class.getName());
    private static final String RESULTS_KEY = "org.icefaces.ace.component.fileEntry.results";
    private static final String EVENT_KEY = "org.icefaces.ace.component.fileEntry.events";

    public FileEntry() {
        super();
    }

    public void setResults(FileEntryResults results) {
        try {
            super.setResults(results);
        }
        catch(RuntimeException e) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            if (facesContext.isProjectStage(ProjectStage.Development) ||
                facesContext.isProjectStage(ProjectStage.UnitTest)) {
                log.log(Level.WARNING, "Problem setting results property on " +
                    "FileEntry component", e);
            }
            throw e;
        }
    }

    public void reset() {
        setResults(null);
    }

    /**
     * Apparently, the view id changes, if you try to get it early in
     * the lifecycle. So, you can only call this method later on, while 
     * rendering. As such, it's only really applicable from storeConfig(-)
     * 
     * By definition, this method must encode both the clientId and the
     * view id, so that both parts can be extracted out by the
     * FileEntryPhaseListener. It's used as a key into the session map, to 
     * store the FileEntryConfig. As well, it has to be a valid HTML form 
     * field id and name. From the HTML 4 and XHTML 1 specs: ID and NAME 
     * tokens must begin with a letter ([A-Za-z]) and may be followed by any 
     * number of letters, digits ([0-9]), hyphens ("-"), underscores ("_"), 
     * colons (":"), and periods (".").
     * 
     * TODO
     * An alternative implementation, that would not rely on the view
     * id, would involve using a sequence number in the session, so that new
     * fileEntry components would take a sequence number from the session, 
     * and then hold onto that, using state saving. There might even be a 
     * way to use view or page scope, to hold the identifier, using the 
     * clientId as a key, so that if the fileEntry component is removed and 
     * re-added to the view, it would retain the original identifier.
     * 
     * TODO
     * One way to remove some of the necessity for always consistent 
     * identifiers is for a phase listener to remove the config at the 
     * beginning of the lifecycle, so that if it's stored under a new 
     * identifier at the end of the lifecycle, then the old one is not leaked. 
     */
    static String getGloballyUniqueComponentIdentifier(
            FacesContext facesContext, String clientId) {
        String viewId = BridgeSetup.getViewID(facesContext.getExternalContext());
        // I couldn't find a character that's valid in an HTML id/name, but 
        // not in a clientId, do just going with a double colon delimiter.
        String id = clientId + "::" + viewId;
        return id;
    }

    /**
     * Used by FileEntryRenderer to save config for FileEntryPhaseListener
     * to use on the next postback
     */
    FileEntryConfig storeConfigForNextLifecycle(FacesContext facesContext,
            String clientId) {
        String resName = null;
        String groupName = null;
        if (PushUtils.isPushPresent()) {
            UIForm form = Utils.findParentForm(this);
            resName = PushUtils.getProgressResourceName(facesContext, form);
            groupName = PushUtils.getPushGroupName(facesContext, form);
        }

        String identifier = getGloballyUniqueComponentIdentifier(
                facesContext, clientId);
        javax.el.ValueExpression callbackExpression =
                getValueExpression("callback");
        String callbackEL = callbackExpression == null ? null :
                callbackExpression.getExpressionString();
        FileEntryConfig config = new FileEntryConfig(
            identifier,
            clientId,
            getAbsolutePath(),
            getRelativePath(),
            isUseSessionSubdir(),
            isUseOriginalFilename(),
            callbackEL,
            getMaxTotalSize(),
            getMaxFileSize(),
            getMaxFileCount(),
            isRequired(),
            resName,
            groupName);
        log.finer("FileEntry.storeConfigForNextLifecycle()  config: " + config);
        Object sessionObj = facesContext.getExternalContext().getSession(false);
        if (sessionObj != null) {
            synchronized(sessionObj) {
                Map<String,Object> map =
                    facesContext.getExternalContext().getSessionMap();
                map.put(identifier, config);
            }
        }
        return config;
    }
    
    /**
     * Used by FileEntryPhaseListener to retrieve config saved away by 
     * FileEntryRenderer in previous lifecycle
     */
    static FileEntryConfig retrieveConfigFromPreviousLifecycle(
            FacesContext facesContext, String identifier) {
        FileEntryConfig config = null;
        Object sessionObj = facesContext.getExternalContext().getSession(false);
        if (sessionObj != null) {
            synchronized(sessionObj) {
                Map<String,Object> map =
                    facesContext.getExternalContext().getSessionMap();
                config = (FileEntryConfig) map.get(identifier);
            }
        }
        return config;
    }

    /**
     * Used by FileEntryPhaseListener, before the component treee exists, to 
     * save the results of the file uploads, to be retrieved later in the same 
     * lifecycle, once the component tree is in place. 
     */
    static void storeResultsForLaterInLifecycle(
            FacesContext facesContext,
            Map<String, FileEntryResults> clientId2Results) {
        facesContext.getAttributes().put(RESULTS_KEY, clientId2Results);
    }

    /**
     * Used by FileEntryRenderer.decode(-) to retrieve each fileEntry
     * component's results for file uploads.
     */
    static FileEntryResults retrieveResultsFromEarlierInLifecycle(
            FacesContext facesContext, String clientId) {
        FileEntryResults results = null;
        Map<String, FileEntryResults> clientId2Result =
            (Map<String, FileEntryResults>) facesContext.getAttributes().get(
                RESULTS_KEY);
        if (clientId2Result != null) {
            results = clientId2Result.get(clientId);
        }
        return results;
    }

    /**
     * After the ApplyRequestValues phase, when the fileEntry components 
     * have all retrieved their results for uploaded files, clear the 
     * results away, so we don't leak memory. 
     */
    static void removeResults(FacesContext facesContext) {
        facesContext.getAttributes().remove(RESULTS_KEY);
    }

    /**
     * Invoked by processDecodes(FacesContext) or processValidators(FacesContext)
     */
    protected void validateResults(FacesContext facesContext) {
        log.finer("FileEntry.validateResults()  clientId: " + getClientId(facesContext));
        // The current lifecycle results. If no files were uploaded
        // this lifecycle, then this is null. Different from getResults(),
        // which may be from a previous lifecycle.
        FileEntryResults results = retrieveResultsFromEarlierInLifecycle(
            facesContext, getClientId(facesContext));

        boolean failed = false;
        if (results != null) {
            for(FileEntryResults.FileInfo fi : results.getFiles()) {
                if (!fi.isSaved()) {
                    log.finer("FileEntry.validateResults()    FAILED  file: " + fi);
                    failed = true;
                    break;
                }
            }
        }
        else {
            // No files uploaded this lifecycle
            // If required then failed unless was partial submit
            String partialSubmitValue = facesContext.getExternalContext().
                    getRequestParameterMap().get("ice.submit.partial");
            boolean partialSubmit = "true".equals(partialSubmitValue);
            log.finer("FileEntry.validateResults()    partialSubmit: " + partialSubmit + "  required: " + isRequired());
            if (!partialSubmit && isRequired()) {
                if (isMessagePersistence()) {
                    addMessageFromRequired(facesContext);
                }
                failed = true;
                log.finer("FileEntry.validateResults()    FAILED  required");
            }
        }
        if (failed) {
            facesContext.validationFailed();
            facesContext.renderResponse();
        }
    }

    protected void addMessagesFromResults(FacesContext facesContext) {
        String clientId = getClientId(facesContext);
        FileEntryResults results = getResults();
        log.finer("FileEntry.addMessagesFromResults  clientId: " + clientId + "  results: " + results);
        if (results != null) {
            ArrayList<FileEntryResults.FileInfo> files = results.getFiles();
            for (FileEntryResults.FileInfo fi : files) {
                FileEntryStatus status = fi.getStatus();
                FacesMessage fm = status.getFacesMessage(facesContext, this, fi);
                log.finer(
                    "FileEntry.addMessagesFromResults\n" +
                    "  FileInfo: " + fi + "\n" +
                    "  FacesMessage: " + fm);
                facesContext.addMessage(clientId, fm);
            }
        }
    }

    protected void addMessageFromRequired(FacesContext facesContext) {
        String clientId = getClientId(facesContext);
        FacesMessage fm = FileEntryStatuses.REQUIRED.getFacesMessage(
                facesContext, this, null);
        log.finer("FileEntry.addMessageFromRequired  clientId: " + clientId + "  FacesMessage: " + fm);
        facesContext.addMessage(clientId, fm);

    }

    /**
     * @return The label property, if specified, else the clientId
     */
    public String getFacesMessageLabel() {
        String label = getLabel();
        if (label != null && label.length() > 0) {
            return label;
        }
        return getClientId();
    }

    @Override
    /**
     * Override to add the constraint that when immediate is true, then
     * immediateValidation must be true as well, since validation must happen
     * before the fileEntryListener is invoked.
     * 
     * @see FileEntryBase#isImmediateValidation
     */
    public boolean isImmediateValidation() {
        return isImmediate() ? true : super.isImmediateValidation();
    }

    @Override
    public void processDecodes(FacesContext facesContext) {
        super.processDecodes(facesContext);

        if (isImmediateValidation()) {
            validateResults(facesContext);
        }
    }

    @Override
    public void processValidators(FacesContext facesContext) {
        super.processValidators(facesContext);

        if (!isImmediateValidation()) {
            validateResults(facesContext);
        }
    }

    @Override
    public void queueEvent(FacesEvent event) {
        log.finer("FileEntry.queueEvent  clientId: " + getClientId());
        if (isImmediate()) {
            event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            log.finer("FileEntry.queueEvent    immediate == true  queuing event: " + event);
            super.queueEvent(event);
        }
        else {
            event.setPhaseId(PhaseId.RENDER_RESPONSE);
            log.finer("FileEntry.queueEvent    immediate == false  storing event: " + event);
            storeEventForPreRender(event);
        }
    }
    
    @Override
    public void broadcast(FacesEvent event) {
        log.finer("FileEntry.broadcast  clientId: " + getClientId() + "  event: " + event + "  phaseId: " + event.getPhaseId());
        if (event instanceof FileEntryEvent) {
            FileEntryEvent fee = (FileEntryEvent) event;
            FacesContext context = FacesContext.getCurrentInstance();
            try {
                log.finer("FileEntry.broadcast    invoke: " + fee.isInvoke());
                if (fee.isInvoke()) {
                    MethodExpression listener = getFileEntryListener();
                    if (listener != null) {
                        ELContext elContext = context.getELContext();
                        try {
                            Object result = listener.invoke(elContext, new Object[] {event});
                            log.finer("FileEntry.broadcast    result: " + result);
                            if (result != null) {
                                String outcome = result.toString();
                                context.getApplication().getNavigationHandler().handleNavigation(
                                    context,
                                    (null != listener) ? listener.getExpressionString() : null,
                                    outcome);
                                context.renderResponse();
                                return;
                            }
                        } catch (ELException ee) {
                            throw new AbortProcessingException(ee.getMessage(),
                                    ee.getCause());
                        }
                    }

                    // If every file succeeded uploading, and the lifecycle is
                    // valid, then clear the file selection in the browser
                    if (getResults().isLifecycleAndUploadsSuccessful(context)){
                        String script = "ice.ace.fileentry.clearFileSelection(\"" +
                                JSONBuilder.escapeString(getClientId(context))+
                                "\")";
                        JavaScriptRunner.runScript(context, script);
                    }
                }
            } finally {
                // ICE-5750 deals with re-adding faces messages for components
                // that have not re-executed. Components that are executing
                // should re-add their faces messages themselves.
                // FileEntry will only re-add faces messages past the upload
                // lifecycle if its messagePersistence property is true.
                if (isMessagePersistence() || fee.isInvoke()) {
                    addMessagesFromResults(context);
                }
            }
        }
        else {
            super.broadcast(event);
        }
    }
    
    /**
     * Used by FileEntry.queueEvent(-), to save non-immediate FileEntryEvent
     * objects to be invoked by FileEntryPhaseListener in pre-Render phase. 
     */
    private void storeEventForPreRender(FacesEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String,FacesEvent> clientId2FacesEvent = (Map<String,FacesEvent>)
            facesContext.getAttributes().get(EVENT_KEY);
        if (clientId2FacesEvent == null) {
            clientId2FacesEvent = new HashMap<String,FacesEvent>(6);
            facesContext.getAttributes().put(EVENT_KEY, clientId2FacesEvent);
        }
        clientId2FacesEvent.put(getClientId(facesContext), event);
    }

    /**
     * Used by FileEntryPhaseListener(-) to retrieve each the FileEntryEvent
     * objects, so they can be invoked pre-Render phase.
     */
    static Map<String,FacesEvent> removeEventsForPreRender(
            FacesContext facesContext) {
        Map<String,FacesEvent> clientId2FacesEvent = (Map<String,FacesEvent>)
            facesContext.getAttributes().remove(EVENT_KEY);
        return clientId2FacesEvent;
    }

    public String getFocusedElementId() {
        FacesContext context = FacesContext.getCurrentInstance();
        String viewID = BridgeSetup.getViewID(context.getExternalContext());
        return getClientId() + "::" + viewID;
    }
}
