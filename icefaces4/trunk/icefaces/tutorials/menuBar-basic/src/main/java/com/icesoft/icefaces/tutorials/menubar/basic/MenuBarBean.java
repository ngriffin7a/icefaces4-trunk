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

package com.icesoft.icefaces.tutorials.menubar.basic;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>The MenuBarBean class determines which menu item fired the ActionEvent and
 * stores the modified id information in a String. MenuBarBean also controls the
 * orientation of the Menu Bar.</p>
 */
public class MenuBarBean implements Serializable {

    private static final long serialVersionUID = 8789957949564114786L;
    // records the param value for the menu item which fired the event
    private String param;

    // orientation of the menubar ("horizontal" or "vertical")
    private String orientation = "horizontal";

    /**
     * Get the param value for the menu item which fired the event.
     *
     * @return the param value
     */
    public String getParam() {
        return param;
    }

    /**
     * Set the param value.
     */
    public void setParam(String param) {
        this.param = param;
    }

    /**
     * Identify the ID of the element that fired the event and return it in a
     * form suitable for display.
     *
     * @param e the event that fired the listener
     */
    public void listener(ActionEvent e) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map params = facesContext.getExternalContext().getRequestParameterMap();
        String myParam = (String) params.get("myParam");
        if (myParam != null && myParam.length() > 0) {
            setParam(myParam);
        } else {
            setParam("not defined");
        }

    }

    /**
     * Get the orientation of the menu ("horizontal" or "vertical")
     *
     * @return the orientation of the menu
     */
    public String getOrientation() {
        return orientation;
    }

    /**
     * Set the orientation of the menu ("horizontal" or "vertical").
     *
     * @param orientation the new orientation of the menu
     */
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
}