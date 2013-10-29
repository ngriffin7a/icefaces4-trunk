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

package org.icefaces.application.showcase.view.bean.examples.layoutPanel.panelToolTip;
import java.io.Serializable;

/**
 * @since 1.7
 */
public class PanelToolTipModel implements Serializable{

    private String hideOn = "mouseout";

    private String hoverDelay ="1000";

    private boolean draggable;

    private String displayOn = "hover";

    private boolean moveWithMouse = false;

    public String getHideOn() {
        return hideOn;
    }

    public void setHideOn(String hideOn) {
        this.hideOn = hideOn;
    }

    public String getHoverDelay() {
        return hoverDelay;
    }

    public int getHoverDelayTime(){
        try {
            return Integer.parseInt(hoverDelay);
        } catch (NumberFormatException e) { // ICE-4753
            return 500;
        }
    }

    public void setHoverDelay(String hoverDelay) {
        this.hoverDelay = hoverDelay;
    }

    public boolean getDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public String getDisplayOn() {
        return displayOn;
    }

    public void setDisplayOn(String displayOn) {
        this.displayOn = displayOn;
    }

    public boolean isMoveWithMouse() {
        return moveWithMouse;
    }

    public void setMoveWithMouse(boolean moveWithMouse) {
        this.moveWithMouse = moveWithMouse;
    }
}
