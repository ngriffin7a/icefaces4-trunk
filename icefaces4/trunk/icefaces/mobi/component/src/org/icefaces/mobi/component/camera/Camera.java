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

package org.icefaces.mobi.component.camera;

import org.icefaces.mobi.api.ThumbnailProvider;
import org.icefaces.mobi.util.MobiJSFUtils;
import org.icefaces.mobi.api.IDevice;
import org.icefaces.ace.util.ClientDescriptor;

import javax.el.MethodExpression;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

public class Camera extends CameraBase implements IDevice, ThumbnailProvider {

    private static Logger logger = Logger.getLogger(Camera.class.getName());

    public Camera() {
        super();
    }

    /*if the image properties have to be gotten from the image map to encode*/
    public Object getPropertyFromMap(Map<String, Object> cameraMap, String key) {
        if (cameraMap.containsKey(key)) {
            return cameraMap.get(key);
        } else return null;
    }

    private boolean containsKey(Map<String, Object> cameraMap, String key) {
        if (cameraMap.containsKey(key)) return true;
        else return false;
    }


    public void broadcast(FacesEvent event)
       throws AbortProcessingException {
         if (event instanceof ValueChangeEvent){
            if (event != null) {
                 ValueChangeEvent e = (ValueChangeEvent)event;
                 MethodExpression method = getValueChangeListener();
                 if (method != null) {
                     method.invoke(getFacesContext().getELContext(), new Object[]{event});
                 }
             }
         }
     }

     public void queueEvent(FacesEvent event) {
         if (event.getComponent() instanceof Camera) {
             if (isImmediate()) {
                 event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
}
             else {
                 event.setPhaseId(PhaseId.INVOKE_APPLICATION);
             }
         }
         super.queueEvent(event);
     }

     public String getScript( String clientId, boolean auxUpload) {
        int width = getMaxwidth() ;
        int height = getMaxheight();
        String script;
        if (auxUpload)  {
            HashMap<String,String> params = new HashMap();
            if ( (width != Integer.MIN_VALUE) || 
                    (height != Integer.MIN_VALUE) ) {
                if (width > 0)  {
                    params.put("maxwidth", String.valueOf(width));
                }
                if (height > 0)  {
                    params.put("maxheight", String.valueOf(height));
                }
            }
            script = MobiJSFUtils.getICEmobileSXScript(
                    "camera", params, this);
        } else {
            script = "ice.camera( '" + clientId + "'";
            boolean needParams = (width > 0) || (height > 0);
            if (needParams)  {
                script += ", '";
            }
            if (width > 0)  {
                script += "maxwidth=" + String.valueOf(width);
                if (height > 0)  {
                    script += "&";
                }
            }
            if (height > 0)  {
                script += "maxheight=" + String.valueOf(height);
            }
            if (needParams)  {
                script += "'";
            }
            script += ");";
        }
        return script;
    }

    public ClientDescriptor getClient() {
         return MobiJSFUtils.getClientDescriptor();
    }
    public boolean isUseCookie(){
        return false;
    }
    public String getComponentType(){
        return "camera";
    }
    /* Needed for ICEmobile-SX and JSP */
    public String getSessionId()  {
        return MobiJSFUtils.getSessionIdCookie(
                FacesContext.getCurrentInstance());
    }
    public String getParams(){
        return null;
    }

    public String getPostURL()  {
       return MobiJSFUtils.getPostURL();
    }

}

