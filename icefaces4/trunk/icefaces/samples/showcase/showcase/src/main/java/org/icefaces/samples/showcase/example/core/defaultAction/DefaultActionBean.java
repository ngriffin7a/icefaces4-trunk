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

package org.icefaces.samples.showcase.example.core.defaultAction;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

@ManagedBean(name = DefaultActionBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DefaultActionBean implements Serializable {
    public static final String BEAN_NAME = "defaultActionBean";
	public String getBeanName() { return BEAN_NAME; }

    private String actionDescription;
    private String a, b, c = "";

    public String getActionDescription() {
        return actionDescription;
    }

    public void pressedEsc(ActionEvent e) {
        actionDescription = "Esc key press";
    }

    public void pressedEnter(ActionEvent e) {
        actionDescription = "Enter key press.";
    }

    public void pressedLeftArrow(ActionEvent e) {
        actionDescription = "Left Arrow key press.";
    }

    public void pressedF3(ActionEvent e) {
        actionDescription = "F3 key press.";
    }

    public boolean getShowActionDescription() {
        return actionDescription != null;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }
}
