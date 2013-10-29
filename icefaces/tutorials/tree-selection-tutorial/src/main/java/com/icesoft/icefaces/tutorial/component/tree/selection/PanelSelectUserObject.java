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

import com.icesoft.faces.component.tree.IceUserObject;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * <p>The PanelSelectUserObject is responsible for selcting a known panel in a
 * panelStack when #selectPanelStackPanel is called.  When the PanelSelectUserObject
 * is constructed a reference is set via the FacesContext to the backing
 * bean which is responsible for setting the selected panel in the panelStack
 * component</p>
 */
public class PanelSelectUserObject extends IceUserObject implements Serializable {

    private static final long serialVersionUID = 4708525521970673798L;
    // displayPanel to show when a node is clicked
    private String displayPanel;

    // panel stack which will be manipulated when a command links action is fired.
    private PanelStackBean panelStack;

    /**
     * Default contsructor for a PanelSelectUserObject object.  A reference
     * is made to a backing bean with the name "panelStack", if possible.
     * @param wrapper
     */
    public PanelSelectUserObject(DefaultMutableTreeNode wrapper) {
        super(wrapper);

        // get a reference to the PanelStackBean from the faces context
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Object panelStackObject =
                facesContext.getApplication()
                        .createValueBinding("#{panelStack}")
                        .getValue(facesContext);
        if (panelStackObject instanceof PanelStackBean){
            panelStack = (PanelStackBean)panelStackObject;
        }
    }

    /**
     * Gets the name of a panel in the panel stack which is associated with
     * this object.
     *
     * @return name of a panel in the panel stack
     */
    public String getDisplayPanel() {
        return displayPanel;
    }

    /**
     * Sets the name of a panelStack panel, assumed to be valid.
     *
     * @param displayPanel panelStack panel name.
     */
    public void setDisplayPanel(String displayPanel) {
        this.displayPanel = displayPanel;
    }

    /**
     * ActionListener method called when a node in the tree is clicked.  Sets
     * the selected panel of the reference panelStack to the value of the instance
     * variable #displayPanel.   
     *
     * @param action JSF action event.
     */
    public void selectPanelStackPanel(ActionEvent action){
        if (panelStack != null){
            panelStack.setSelectedPanel(displayPanel);
        }
    }

}
