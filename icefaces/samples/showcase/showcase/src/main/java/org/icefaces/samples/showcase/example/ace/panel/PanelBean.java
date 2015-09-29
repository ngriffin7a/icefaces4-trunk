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

package org.icefaces.samples.showcase.example.ace.panel;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

@ManagedBean(name= PanelBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PanelBean implements Serializable {
    public static final String BEAN_NAME = "panelBean";
	public String getBeanName() { return BEAN_NAME; }
    private boolean reOpenButton = false;
    
    private boolean collapsed = false;

    public boolean getCollapsed() { return collapsed; }
    public void setCollapsed(boolean collapsed) { this.collapsed = collapsed; }

    /**
     * use the close event of panels ace:ajax tag to make a show button visible
     * @param event
     */
    public void setButtonVisible(javax.faces.event.AjaxBehaviorEvent event){
       this.reOpenButton=true;
    }

    /**
     * use ace:ajax on the pushButton to reset it to not be rendered
     * @param event
     */
    public void buttonNotVisible(ActionEvent event){
        this.reOpenButton = false;
    }

    public boolean isReOpenButton() {
        return reOpenButton;
    }

    public void setReOpenButton(boolean reOpenButton) {
        this.reOpenButton = reOpenButton;
    }


}