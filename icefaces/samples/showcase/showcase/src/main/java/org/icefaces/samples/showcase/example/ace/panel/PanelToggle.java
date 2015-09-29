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

@ManagedBean(name= PanelToggle.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PanelToggle implements Serializable {

    public static final String BEAN_NAME = "panelToggle";
	public String getBeanName() { return BEAN_NAME; }

    private boolean toggleable = true;
    private int speed = 700;
    
    public boolean getToggleable() { return toggleable; }
    public int getSpeed() { return speed; }
    
    public void setToggleable(boolean toggleable) { this.toggleable = toggleable; }
    public void setSpeed(int speed) { this.speed = speed; }
}
