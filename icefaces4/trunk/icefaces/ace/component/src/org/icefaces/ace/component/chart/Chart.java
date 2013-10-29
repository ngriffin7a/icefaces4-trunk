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

package org.icefaces.ace.component.chart;

import org.icefaces.ace.event.PointValueChangeEvent;
import org.icefaces.ace.event.SeriesSelectionEvent;
import org.icefaces.ace.event.ChartImageExportEvent;
import org.icefaces.ace.event.TableFilterEvent;
import org.icefaces.ace.event.UnselectEvent;
import org.icefaces.ace.model.chart.ChartSeries;

import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import java.util.ArrayList;
import java.util.List;

public class Chart extends ChartBase {
   public boolean hasAxisConfig() {
        return (getXAxis() != null) ||
               (getX2Axis() != null) ||
               (getYAxes() != null);
   }

    @Override
    public Object getValue() {
        final Object o = super.getValue();

        if (o instanceof List)
            return o;
        else if (o instanceof ChartSeries)
            return new ArrayList() {{
                add(o);
            }};
        else
            throw new RuntimeException("Chart component value is not an instance of, or a List of ChartSeries subclasses.");
    }

    @Override
    public void broadcast(javax.faces.event.FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);

        FacesContext context = FacesContext.getCurrentInstance();
        MethodExpression me = null;

        if (event instanceof SeriesSelectionEvent) me = getSelectListener();
        else if (event instanceof PointValueChangeEvent) me = getPointChangeListener();
        else if (event instanceof ChartImageExportEvent) me = getImageExportListener();

        if (me != null)
            me.invoke(context.getELContext(), new Object[] {event});
    }

}