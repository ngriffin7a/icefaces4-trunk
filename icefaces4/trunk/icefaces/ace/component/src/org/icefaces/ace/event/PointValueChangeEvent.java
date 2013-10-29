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

package org.icefaces.ace.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesListener;
import javax.faces.event.ValueChangeEvent;

public class PointValueChangeEvent extends ValueChangeEvent {
    Integer seriesIndex;
    Integer pointIndex;

    public PointValueChangeEvent(UIComponent component, Object[] oldValue, Object[] newValue, Integer seriesIndex, Integer pointIndex) {
        super(component, oldValue, newValue);
        this.seriesIndex = seriesIndex;
        this.pointIndex = pointIndex;
    }

    @Override
    public boolean isAppropriateListener(FacesListener listener) {
        return true;
    }

    public Integer getSeriesIndex() {
        return seriesIndex;
    }

    public void setSeriesIndex(Integer seriesIndex) {
        this.seriesIndex = seriesIndex;
    }

    public Integer getPointIndex() {
        return pointIndex;
    }

    public void setPointIndex(Integer pointIndex) {
        this.pointIndex = pointIndex;
    }
}
