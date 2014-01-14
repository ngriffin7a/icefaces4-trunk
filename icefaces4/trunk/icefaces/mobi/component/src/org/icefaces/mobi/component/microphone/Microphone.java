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

package org.icefaces.mobi.component.microphone;

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
import java.util.HashMap;


public class Microphone extends MicrophoneBase implements IDevice{

    public Microphone() {
        super();
    }


    /*if the image properties have to be gotten from the image map to encode*/
    public Object getPropertyFromMap(Map<String, Object> microphoneMap, String key) {
        if (microphoneMap.containsKey(key)) {
            return microphoneMap.get(key);
        } else return null;
    }

    private boolean containsKey(Map<String, Object> microphoneMap, String key) {
        return microphoneMap.containsKey(key);
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
         if (event.getComponent() instanceof Microphone) {
             if (isImmediate()) {
                 event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
}
             else {
                 event.setPhaseId(PhaseId.INVOKE_APPLICATION);
             }
         }
         super.queueEvent(event);
     }

    public boolean isUseNative() {
        return false;
    }

    public int getMaxwidth() {
        return Integer.MIN_VALUE;
    }

    public void setMaxwidth(int i) {

    }

    public int getMaxheight() {
          return Integer.MIN_VALUE;
    }

    public void setMaxheight(int i) {

    }

    public String getScript(String clientId, boolean auxUpload) {
        final StringBuilder script = new StringBuilder();
        if (auxUpload)  {
            HashMap<String,String> params = new HashMap();
            if (getMaxtime() > 0)  {
                params.put("maxtime", String.valueOf(getMaxtime()));
            }
            script.append(MobiJSFUtils.getICEmobileSXScript(
                    "microphone", null, this));
        } else {
            if (getMaxtime() != Integer.MIN_VALUE) {
                script.append("ice.microphone( '")
                .append(clientId)
                .append(",'maxtime=")
                .append(getMaxtime()).append("');");
            } else {
                script.append("ice.microphone( '")
                .append(clientId)
                .append("');");
            }
        }
        return script.toString();
    }


    public boolean isUseCookie() {
        return false;
    }

    public String getComponentType() {
        return "microphone";
    }

  /* don't need this for JSF but the interface for the core renderer require it from JSP */
    public String getSessionId(){
        return MobiJSFUtils.getSessionIdCookie(
                FacesContext.getCurrentInstance());
    }
    public String getParams(){
        return null;
    }

    public ClientDescriptor getClient() {
         return MobiJSFUtils.getClientDescriptor();
    }

    public String getPostURL()  {
       return MobiJSFUtils.getPostURL();
    }

}
