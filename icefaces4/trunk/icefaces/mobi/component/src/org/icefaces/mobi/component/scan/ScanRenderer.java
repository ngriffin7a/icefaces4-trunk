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

package org.icefaces.mobi.component.scan;


import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.icefaces.impl.application.AuxUploadResourceHandler;
import org.icefaces.mobi.renderkit.BaseInputRenderer;
import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icefaces.mobi.util.MobiJSFUtils;

import org.icefaces.mobi.renderkit.DeviceCoreRenderer;


public class ScanRenderer extends BaseInputRenderer {
    private static final Logger logger = Logger.getLogger(ScanRenderer.class.getName());

    @Override
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Scan scan = (Scan) uiComponent;
        String clientId = scan.getClientId();
        if (scan.isDisabled()) {
            return;
        }
        Map requestParameterMap = facesContext.getExternalContext()
                .getRequestParameterMap();
        String valueId = clientId;
        Object submitted = requestParameterMap.get(valueId);
        if (null == submitted)  {
            Map auxMap = AuxUploadResourceHandler.getAuxRequestMap();
            submitted = auxMap.get(valueId);
        }
        if (null != submitted) {
            String submittedString = String.valueOf(submitted);
            if (submittedString != null){
                Object convertedValue = this.getConvertedValue(facesContext, uiComponent, submittedString);
                this.setSubmittedValue(scan, convertedValue);
            }

        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        Scan scan = (Scan) uiComponent;
        String oldLabel = scan.getButtonLabel();
        if (MobiJSFUtils.uploadInProgress(scan))  {
           scan.setButtonLabel(scan.getCaptureMessageLabel()) ;
        } 
        DeviceCoreRenderer renderer = new DeviceCoreRenderer();
        ResponseWriterWrapper writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
        renderer.encode(scan, writer, false);
        scan.setButtonLabel(oldLabel);
    }


}
