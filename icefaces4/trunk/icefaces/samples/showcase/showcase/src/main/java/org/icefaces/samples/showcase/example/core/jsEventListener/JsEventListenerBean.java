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

package org.icefaces.samples.showcase.example.core.jsEventListener;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.icefaces.impl.component.JSEventListener;

@ManagedBean(name = JsEventListenerBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class JsEventListenerBean implements Serializable {
    public static final String BEAN_NAME = "jsEventListenerBean";
	public String getBeanName() { return BEAN_NAME; }

    private JSEventListener.JSEvent actionEvent;

    public JSEventListener.JSEvent getActionEvent() {
        return actionEvent;
    }

    public void captureEvent(ActionEvent e) {
        actionEvent = (JSEventListener.JSEvent) e;
    }

    public boolean getShowActionDescription() {
        return actionEvent != null;
    }

}

