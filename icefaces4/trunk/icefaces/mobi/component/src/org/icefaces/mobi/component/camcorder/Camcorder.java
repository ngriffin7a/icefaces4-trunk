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
package org.icefaces.mobi.component.camcorder;

import org.icefaces.mobi.api.ThumbnailProvider;
import org.icefaces.mobi.util.MobiJSFUtils;
import org.icefaces.mobi.api.IDevice;
import org.icefaces.mobi.util.ClientDescriptor;

import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;
import java.util.Map;


public class Camcorder extends CamcorderBase implements IDevice, ThumbnailProvider{

    public Camcorder() {
        super();
    }

    public Object getPropertyFromMap(Map<String, Object> videoMap, String key) {
        if (videoMap.containsKey(key)) {
            return videoMap.get(key);
        } else return null;
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
         if (event.getComponent() instanceof Camcorder) {
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
         int maxtime = getMaxtime();
          String script;
        if (auxUpload)  {
            script = MobiJSFUtils.getICEmobileSXScript("camcorder", this);
        } else {
            //default value of unset in params is Integer.MIN_VALUE
            String params = "'" + clientId + "'";
            //only commonality between iPhone and android is duration or maxTime
            //simplify this scripting when devices have this implemented and is final api
            int unset = Integer.MIN_VALUE;
            int numParams = 0;
            String attributeSeparator = "&";
            if (maxtime != unset || width != unset || height != unset) {
                params += ",'";
            }
            if (maxtime != unset) {
                if (numParams > 0) {
                    params += attributeSeparator;
                }
                params += "maxtime=" + maxtime;
                numParams++;
            }
            if (width != Integer.MIN_VALUE) {
                if (numParams > 0) {
                    params += attributeSeparator;
                }
                params += "maxwidth=" + width;
                numParams++;
            }
            if (height != Integer.MIN_VALUE) {
                if (numParams > 0) {
                    params += attributeSeparator;
                }
                params += "maxheight=" + height;
                numParams++;
            }
            if (numParams > 0) {
                params += "'";
            }
            script = "ice.camcorder(" + params + ");";
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
         return "camcorder";
     }
     /* don't need this for JSF but the interface for the core renderer require it from JSP */
     public String getSessionId(){
         HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
         return session.getId();
     }
     public String getParams(){
         return null;
     }

    public String getPostURL()  {
       return MobiJSFUtils.getPostURL();
    }

}
