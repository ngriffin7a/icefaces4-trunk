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

package org.icefaces.samples.showcase.example.ace.contextMenu;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.util.FacesUtils;

@ManagedBean(name = ContextMenuComponent.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ContextMenuComponent implements Serializable {
    public static final String BEAN_NAME = "contextMenuComponent";
	public String getBeanName() { return BEAN_NAME; }

    private String actionDescription;

    public ContextMenuComponent() {
        actionDescription = "";
    }

    public String performAction() {
        String actionType = FacesUtils.getRequestParameter("actionType");
        actionDescription = "Action performed: " + actionType;
        return null;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

}
