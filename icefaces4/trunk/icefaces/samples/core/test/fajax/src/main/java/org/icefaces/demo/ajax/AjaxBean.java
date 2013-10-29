
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

package org.icefaces.demo.ajax;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "ajaxBean")
@ViewScoped
public class AjaxBean implements Serializable {
    Boolean visible = false;
    int toggleCount = 0;

    public Boolean getVisible() {
        return visible;
    }

    public void toggle(ActionEvent ae) {
        toggleCount++;
        visible = !visible;
    }

    public void toggleListener(ActionEvent ae) {
        toggle(ae);
    }

    public String toggleAction(){
        toggle(null);
        return null;
    }

    public String getResponseWriter(){
        FacesContext fc = FacesContext.getCurrentInstance();
        ResponseWriter rw = fc.getResponseWriter();
        return rw.getClass().getName();
    }

    public int getToggleCount(){
        return toggleCount;
    }

}