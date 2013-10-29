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

package com.icesoft.faces.component.inputfile;

import javax.faces.event.PhaseListener;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.icesoft.util.CoreComponentUtils;

/**
 * @author mcollette
 * @since 1.8
 */
public class FileUploadPhaseListener implements PhaseListener {
    private static final Log log = LogFactory.getLog(FileUploadPhaseListener.class);
    
    public void beforePhase(PhaseEvent phaseEvent)  {
        if (log.isDebugEnabled())
            log.debug("FileUploadPhaseListener.beforePhase()  " + phaseEvent.getPhaseId().toString());
        
        if (PhaseId.APPLY_REQUEST_VALUES == phaseEvent.getPhaseId())  {
            UploadStateHolder stateHolder = UploadStateHolder.take();
            if (log.isDebugEnabled())
                log.debug("FileUploadPhaseListener.beforePhase()  stateHolder: " + stateHolder);
            if (stateHolder != null) {
                UploadConfig uploadConfig = stateHolder.getUploadConfig();
                FileInfo fileInfo = stateHolder.getFileInfo();
                String clientId = uploadConfig.getClientId();
                String formClientId = uploadConfig.getFormClientId();
                if (log.isDebugEnabled()) {
                    log.debug("FileUploadPhaseListener.beforePhase()  clientId: " + clientId);
                    log.debug("FileUploadPhaseListener.beforePhase()  formClientId: " + formClientId);
                    log.debug("FileUploadPhaseListener.beforePhase()  uploadConfig: " + uploadConfig);
                    log.debug("FileUploadPhaseListener.beforePhase()  fileInfo: " + fileInfo);
                }
                if (clientId != null && formClientId != null) {
                    FacesContext context = phaseEvent.getFacesContext();
                    Map parameterMap = context.getExternalContext().
                        getRequestParameterMap();
                    //parameterMap.put(formClientId, formClientId);
                    parameterMap.put(clientId, stateHolder);
                    
                    UIComponent inputFile = CoreComponentUtils.findComponent(
                        clientId, phaseEvent.getFacesContext().getViewRoot());
                    if (inputFile != null) {
                        inputFile.getAttributes().put("fileInfo", fileInfo);
                        inputFile.queueEvent( new InputFileProgressEvent(inputFile) );
                        if (fileInfo.isSaved()) {
                            inputFile.queueEvent( new InputFileSetFileEvent(inputFile) );
                        }
                        if (fileInfo.isFinished()) {
                            ActionEvent event = new ActionEvent(inputFile);
                            event.setPhaseId(PhaseId.INVOKE_APPLICATION);
                            inputFile.queueEvent(event);
                        }
                    }
                }
            }
        }
    }
    
    public void afterPhase(PhaseEvent phaseEvent)  {
        if (log.isDebugEnabled())
            log.debug("FileUploadPhaseListener.afterPhase()  " + phaseEvent.getPhaseId().toString());
    }
    
    public PhaseId getPhaseId()  {
        return PhaseId.APPLY_REQUEST_VALUES;
    }
}
