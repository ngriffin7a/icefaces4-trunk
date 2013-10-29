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

package org.icefaces.impl.application;

import org.icefaces.util.EnvUtils;

import javax.faces.application.Application;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean(name="auxUpload", eager=true)
@ApplicationScoped
public class AuxUploadSetup {
    private AuxUploadResourceHandler auxHandler;
    private static String AUX_UPLOAD = "auxUpload";

    public AuxUploadSetup()  {
        Application application = FacesContext.getCurrentInstance()
                .getApplication();
        ResourceHandler currentHandler = application.getResourceHandler();
        auxHandler = new AuxUploadResourceHandler(currentHandler);
        application.setResourceHandler(auxHandler);
    }

    public static AuxUploadSetup getInstance()  {
        ExternalContext externalContext = FacesContext.getCurrentInstance()
                .getExternalContext();
        AuxUploadSetup auxUpload = (AuxUploadSetup) externalContext
            .getApplicationMap().get(AUX_UPLOAD);
        return auxUpload;
    }

    public String getUploadPath()  {
        return auxHandler.getTokenResourcePath();
    }
    
    public String getUploadURL()  {
        ExternalContext externalContext = FacesContext.getCurrentInstance()
                .getExternalContext();
        String serverName = externalContext.getRequestHeaderMap().
                get("x-forwarded-host");
        if (null == serverName)  {
            serverName = externalContext.getRequestServerName() + ":" +
                    externalContext.getRequestServerPort();
        }
        return "http://" + serverName + getUploadPath();
    }
    
    public boolean getEnabled()  {
        return EnvUtils.isAuxUploadBrowser(FacesContext.getCurrentInstance());
    }
    
    public String getCloudPushId()  {
        return auxHandler.getCloudPushId();
    }
}
