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

package org.icefaces.samples.showcase.example.ace.menu;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

@ManagedBean(name= MenuEvents.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MenuEvents implements Serializable {
    public static final String BEAN_NAME = "menuEvents";
	public String getBeanName() { return BEAN_NAME; }
    
    private String message;
    private String currentColor = "black";
    
    public String getMessage() { return message; }
    public String getCurrentColor() { return currentColor; }
    
    public void setMessage(String message) { this.message = message; }
    public void setCurrentColor(String currentColor) { this.currentColor = currentColor; }
    
    public String ourAction() {
        message = "Fired Action.";
        return null;
    }
    
    public void ourActionListener(ActionEvent event) {
        message = "Fired Action Listener.";
    }
    
    public void applyColor(ActionEvent event) {
        currentColor = event.getComponent() != null ? event.getComponent().getId() : "black";
    }
}
