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

package com.icesoft.icefaces.tutorial.component.tree.selection;

/**
 * <p>
 * The PanelStackBean is responsible for storing the name of the panel in the
 * panelStack which should be displayed when rendered. The default panel stack
 * panel name is "splash".
 * </p>
 * <p>
 * If a selectedPanel name is not found in the panel stack the panelStack
 * comonent will not change the current selected PanelStack
 * </p>
 */
public class PanelStackBean implements java.io.Serializable {

    private static final long serialVersionUID = 3873419766151672310L;
    // default panel name
    private String selectedPanel = "splash";

    /**
     * Gets the name of the selected panel stack.
     * @return selected panel stack name
     */
    public String getSelectedPanel() {
        return selectedPanel;
    }

    /**
     * 
     * @param selectedPanel
     */
    public void setSelectedPanel(String selectedPanel) {
        this.selectedPanel = selectedPanel;
    }
}
