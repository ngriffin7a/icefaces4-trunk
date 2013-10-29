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

package org.icefaces.application.showcase.facelets.navigation;

import javax.faces.event.ActionEvent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>The NavigationBean class is responsible for storing the state of the
 * included dynamic content for display.  </p>
 *
 * @since 0.3.0
 */
@ManagedBean(name = "navigation")
@SessionScoped
public class NavigationBean implements Serializable{

    private boolean extendedRender = false;
    private boolean customRender = true;
    private boolean tableRender = false;
    private boolean layoutRender = false;
    
    // selected include contents.
    private String selectedIncludePath = "/WEB-INF/includes/content/splash.xhtml";

    /**
     * Gets the currently selected content include path.
     *
     * @return currently selected content include path.
     */
    public String getSelectedIncludePath() {
        //check for a currently selected path to be ready for ui:include
        FacesContext context = FacesContext.getCurrentInstance();
        Map map = context.getExternalContext().getRequestParameterMap();
        String requestedPath = (String) map.get("includePath");
        if ((null != requestedPath) && (requestedPath.length() > 0))  {
            selectedIncludePath = requestedPath;
        }
        return selectedIncludePath;
    }

    /**
     * Sets the selected content include path to the specified path.
     *
     * @param selectedIncludePath the specified content include path.
     */
    public void setSelectedIncludePath(String selectedIncludePath) {
        this.selectedIncludePath = selectedIncludePath;
    }

    /**
     * Action Listener for the changes the selected content path in the
     * facelets version of component showcase.
     *
     * @param event JSF Action Event.
     */
    public void navigationPathChange(ActionEvent event){

        // get from the context content include path to show as well
        // as the title associated with the link.
        FacesContext context = FacesContext.getCurrentInstance();
        Map map = context.getExternalContext().getRequestParameterMap();
        selectedIncludePath = (String) map.get("includePath");
    }
    
    public boolean getExtendedRender() {
        return extendedRender;
    }
    
    public boolean getCustomRender() {
        return customRender;
    }
    
    public boolean getTableRender() {
        return tableRender;
    }
    
    public boolean getLayoutRender() {
        return layoutRender;
    }
    
    public void setExtendedRender(boolean extendedRender) {
        this.extendedRender = extendedRender;
    }
    
    public void setCustomRender(boolean customRender) {
        this.customRender = customRender;
    }
    
    public void setTableRender(boolean tableRender) {
        this.tableRender = tableRender;
    }
    
    public void setLayoutRender(boolean layoutRender) {
        this.layoutRender = layoutRender;
    }
}