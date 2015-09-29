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

package org.icefaces.samples.showcase.example.ace.gMap;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name= MapOptionsBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MapOptionsBean implements Serializable {
	public static final String BEAN_NAME = "gMapOptionsBean";
	public String getBeanName() { return BEAN_NAME; }
    private boolean typeControl=true;
    private boolean overviewControl=true;
    private boolean panControl=true;
    private boolean rotateControl=true;
    private boolean scaleControl=true;
    private boolean streetViewControl=true;
    private boolean zoomControl=true;
    private boolean controlsPanelCollapsed=true;
    private boolean mapTypeControlPanel=true;
    private String typeLocation="TOP_RIGHT";
    private String typeStyle="HORIZONTAL_BAR";
    private boolean draggable = true;
    private boolean scrollWheel = true;
    private int maxZoom = 18;
    private int minZoom = 1;
    private boolean optionsPanelCollapsed = true;

    public boolean isTypeControl() {
        return typeControl;
    }

    public void setTypeControl(boolean typeControl) {
        this.typeControl = typeControl;
    }

    public boolean isOverviewControl() {
        return overviewControl;
    }

    public void setOverviewControl(boolean overviewControl) {
        this.overviewControl = overviewControl;
    }

    public boolean isPanControl() {
        return panControl;
    }

    public void setPanControl(boolean panControl) {
        this.panControl = panControl;
    }

    public boolean isRotateControl() {
        return rotateControl;
    }

    public void setRotateControl(boolean rotateControl) {
        this.rotateControl = rotateControl;
    }

    public boolean isScaleControl() {
        return scaleControl;
    }

    public void setScaleControl(boolean scaleControl) {
        this.scaleControl = scaleControl;
    }

    public boolean isStreetViewControl() {
        return streetViewControl;
    }

    public void setStreetViewControl(boolean streetViewControl) {
        this.streetViewControl = streetViewControl;
    }

    public boolean isZoomControl() {
        return zoomControl;
    }

    public void setZoomControl(boolean zoomControl) {
        this.zoomControl = zoomControl;
    }


    public boolean isControlsPanelCollapsed() {
        return controlsPanelCollapsed;
    }

    public void setControlsPanelCollapsed(boolean controlsPanelCollapsed) {
        this.controlsPanelCollapsed = controlsPanelCollapsed;
    }

    public boolean isMapTypeControlPanel() {
        return mapTypeControlPanel;
    }

    public void setMapTypeControlPanel(boolean mapTypeControlPanel) {
        this.mapTypeControlPanel = mapTypeControlPanel;
    }

    public String getTypeLocation() {
        return typeLocation;
    }

    public void setTypeLocation(String typeLocation) {
        this.typeLocation = typeLocation;
    }

    public String getTypeStyle() {
        return typeStyle;
    }

    public void setTypeStyle(String typeStyle) {
        this.typeStyle = typeStyle;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public boolean isScrollWheel() {
        return scrollWheel;
    }

    public void setScrollWheel(boolean scrollWheel) {
        this.scrollWheel = scrollWheel;
    }

    public int getMaxZoom() {
        return maxZoom;
    }

    public void setMaxZoom(int maxZoom) {
        this.maxZoom = maxZoom;
    }

    public int getMinZoom() {
        return minZoom;
    }

    public void setMinZoom(int minZoom) {
        this.minZoom = minZoom;
    }

    public boolean isOptionsPanelCollapsed() {
        return optionsPanelCollapsed;
    }

    public void setOptionsPanelCollapsed(boolean optionsPanelCollapsed) {
        this.optionsPanelCollapsed = optionsPanelCollapsed;
    }
}
