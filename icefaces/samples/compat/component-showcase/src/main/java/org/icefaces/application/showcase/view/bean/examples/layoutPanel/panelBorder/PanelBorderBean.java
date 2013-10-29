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

package org.icefaces.application.showcase.view.bean.examples.layoutPanel.panelBorder;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;

/**
 * <p>The PanelBorderBean class store boolean visibility values for the five
 * visible areas of the BorderLayout component.  Users can change the visibilty
 * of the different visable areas and see the component update for the
 * respective visibility change. </p>
 * <p>Sizing of the BorderLayout areas is controlled by CSS style sheet rules</p>
 *
 * @since 0.3.0
 */
@ManagedBean(name = "borderLayout")
@ViewScoped
public class PanelBorderBean implements Serializable {

    // five configurable areas that make up a BorderLayout
    private boolean renderNorth = true;
    private boolean renderSouth = true;
    private boolean renderCenter = true;
    private boolean renderEast = true;
    private boolean renderWest = true;

    /**
     * event handler for the north border.
     *
     * @param event the value change event.
     */
    public void north(ValueChangeEvent event) {
        setRenderNorth(((Boolean) event.getNewValue()).booleanValue());
    }


    /**
     * event handler for the south border.
     *
     * @param event the value change event.
     */
    public void south(ValueChangeEvent event) {
        setRenderSouth(((Boolean) event.getNewValue()).booleanValue());
    }

    /**
     * event handler for the center border.
     *
     * @param event the value change event.
     */
    public void center(ValueChangeEvent event) {
        setRenderCenter(((Boolean) event.getNewValue()).booleanValue());
    }

    /**
     * event handler for the east border.
     *
     * @param event the value change event.
     */
    public void east(ValueChangeEvent event) {
        setRenderEast(((Boolean) event.getNewValue()).booleanValue());
    }

    /**
     * event handler for the west border.
     *
     * @param event the value change event.
     */
    public void west(ValueChangeEvent event) {
        setRenderWest(((Boolean) event.getNewValue()).booleanValue());
    }

    public boolean isRenderCenter() {
        return renderCenter;
    }

    public void setRenderCenter(boolean renderCenter) {
        this.renderCenter = renderCenter;
    }

    public boolean isRenderEast() {
        return renderEast;
    }

    public void setRenderEast(boolean renderEast) {
        this.renderEast = renderEast;
    }

    public boolean isRenderNorth() {
        return renderNorth;
    }

    public void setRenderNorth(boolean renderNorth) {
        this.renderNorth = renderNorth;
    }

    public boolean isRenderSouth() {
        return renderSouth;
    }

    public void setRenderSouth(boolean renderSouth) {
        this.renderSouth = renderSouth;
    }

    public boolean isRenderWest() {
        return renderWest;
    }

    public void setRenderWest(boolean renderWest) {
        this.renderWest = renderWest;
    }
}
