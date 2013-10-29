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

package com.icesoft.faces.component.menubar;

import javax.swing.tree.DefaultMutableTreeNode;

public class IceMenuObject {

    protected DefaultMutableTreeNode wrapper;
    protected String text;
    protected boolean expanded;
    protected String tooltip;
    protected String action;

    // icon fields
    protected String leafIcon;
    protected String branchExpandedIcon;
    protected String branchContractedIcon;
    protected String icon;

    //constructors
    public IceMenuObject(DefaultMutableTreeNode wrapper) {
        this.wrapper = wrapper;
    }

    public String action() {
        return action;
    }


    // getters/setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFamily() {
        return null;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean isExpanded) {
        this.expanded = isExpanded;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltipString) {
        this.tooltip = tooltipString;
    }

    public String toString() {
        return text;
    }

    public String getLeafIcon() {
        return leafIcon;
    }

    public void setLeafIcon(String leafIcon) {
        this.leafIcon = leafIcon;
    }

    public String getBranchContractedIcon() {
        return branchContractedIcon;
    }

    public void setBranchContractedIcon(String branchContractedIcon) {
        this.branchContractedIcon = branchContractedIcon;
    }

    public String getBranchExpandedIcon() {
        return branchExpandedIcon;
    }

    public void setBranchExpandedIcon(String branchExpandedIcon) {
        this.branchExpandedIcon = branchExpandedIcon;
    }

    /**
     * return the appropriate icon based on this node's expanded/collapsed state
     * and the presence of children
     *
     * @return String application-relative path to the image file
     */
    public String getIcon() {
        if (wrapper.getChildCount() <= 0) {
            if (leafIcon != null) {
                return leafIcon;
            }
        } else if (isExpanded()) {
            if (branchExpandedIcon != null) {
                return branchExpandedIcon;
            }
        } else {
            if (branchContractedIcon != null) {
                return branchContractedIcon;
            }
        }
        return icon;
    }

    public DefaultMutableTreeNode getWrapper() {
        return wrapper;
    }

    /**
     * Set the DefaultMutableTreeNode instance that wraps this instance
     *
     * @param wrapper
     */
    public void setWrapper(DefaultMutableTreeNode wrapper) {
        this.wrapper = wrapper;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}