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

package org.icefaces.application.showcase.view.bean.examples.layoutPanel.panelStack;

import org.icefaces.application.showcase.util.MessageBundleLoader;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;

/**
 * <p>The PanelStackBean class is a backing bean for the PanelStack showcase
 * demonstration and is used to store the selected panel state of the
 * ice:panelStack component. </p>
 */
@ManagedBean(name = "panelStack")
@ViewScoped
public class PanelStackBean implements Serializable {

    //currently selected panel
    private String selectedPanel =
            MessageBundleLoader.getMessage("page.panelStack.example.1.value");

    public String getSelectedPanel() {
        return selectedPanel;
    }

    public void setSelectedPanel(String selectedPanel) {
        this.selectedPanel = selectedPanel;
    }
}
