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

package org.icefaces.mobi.component.scan;


import org.icefaces.mobi.util.MobiJSFUtils;
import org.icefaces.mobi.api.IDevice;
import org.icefaces.util.ClientDescriptor;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class Scan extends ScanBase implements IDevice{

    public Scan() {
        super();
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
        if (auxUpload)  {
            return MobiJSFUtils.getICEmobileSXScript(
                    "scan", null, this);
        }
        return "ice.scan('" + clientId + "');";
    }


    public boolean isUseCookie() {
        return false;
    }

    public String getComponentType() {
        return "scan";
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
