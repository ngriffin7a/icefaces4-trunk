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

package org.icefaces.mobi.component.camera;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ValueChangeEvent;
import javax.faces.render.Renderer;

import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icefaces.mobi.util.HTML;
import org.icefaces.mobi.util.MobiJSFUtils;
import org.icefaces.mobi.util.Utils;
import org.icefaces.util.EnvUtils;
import org.icefaces.mobi.util.ClientDescriptor;
import org.icefaces.mobi.renderkit.DeviceCoreRenderer;


public class CameraRenderer extends Renderer {
    private static final Logger logger = Logger.getLogger(CameraRenderer.class.getName());


    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Camera camera = (Camera) uiComponent;
        String clientId = camera.getClientId();
        try {
            if (!camera.isDisabled()) {
                Map<String, Object> map = new HashMap<String, Object>();
                boolean valid =  extractImages(facesContext, map, clientId);
                /* only set map to value if boolean returned from extractImages is true */
                if (valid){
                    if (map !=null){
                       camera.setValue(map);

             //   trigger valueChange and add map as newEvent value old event is NA
                       uiComponent.queueEvent(new ValueChangeEvent(uiComponent,
    		    		    null, map));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param facesContext
     * @param map
     * @param clientId
     * @return   boolean true means validation of the upload passes, false mean it does not.
     * @throws IOException
     *
     * that uploaded this component.
     */
    public boolean extractImages(FacesContext facesContext, Map map, String clientId) throws IOException {
        return MobiJSFUtils.decodeComponentFile(facesContext, clientId, map);
    }

    /*
      rendering markup moved to core renderer for use with JSP and JSF
     */
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException {
        Camera camera = (Camera) uiComponent;
        String oldLabel = camera.getButtonLabel();
        if (MobiJSFUtils.uploadInProgress(camera))  {
            camera.setButtonLabel(camera.getCaptureMessageLabel()) ;
        } 
        DeviceCoreRenderer renderer = new DeviceCoreRenderer();
        ResponseWriterWrapper writer = new ResponseWriterWrapper(
                facesContext.getResponseWriter());
        renderer.encode(camera, writer, false);
        camera.setButtonLabel(oldLabel);
    }

}
