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
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

/**
 * Created with IntelliJ IDEA.
 * User: Nils
 * Date: 5/11/12
 * Time: 2:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class SeriesSelectionEvent extends FacesEvent {
    int seriesIndex;
    int pointIndex;

    public SeriesSelectionEvent(UIComponent c, int seriesIndex, int pointIndex) {
        super(c);
        this.seriesIndex = seriesIndex;
        this.pointIndex = pointIndex;
    }

    public SeriesSelectionEvent(UIComponent component) {
        super(component);
    }

    @Override
    public boolean isAppropriateListener(FacesListener facesListener) {
        return true;
    }

    @Override
    public void processListener(FacesListener facesListener) {
        throw new UnsupportedOperationException();
    }

    public int getPointIndex() {
        return pointIndex;
    }

    public void setPointIndex(int pointIndex) {
        this.pointIndex = pointIndex;
    }

    public int getSeriesIndex() {
        return seriesIndex;
    }

    public void setSeriesIndex(int seriesIndex) {
        this.seriesIndex = seriesIndex;
    }
}
