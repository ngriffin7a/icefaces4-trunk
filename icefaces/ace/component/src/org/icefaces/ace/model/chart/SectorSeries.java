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

package org.icefaces.ace.model.chart;

import org.icefaces.ace.model.SimpleEntry;
import org.icefaces.ace.util.JSONBuilder;

import javax.faces.component.UIComponent;

public class SectorSeries extends ChartSeries {
    public static enum SectorType implements ChartType {
        PIE, DONUT
    }

    public ChartType getDefaultType() {
        if (getData() == null || getData().size() < 2)
            return SectorType.PIE;
        else
            return SectorType.DONUT;
    }

    Integer diameter; // pixels
    Integer padding;
    Integer sliceMargin; // degrees

    // Fill is also a property of standard series, which isn't used in place of this fill property
    // check our super class for fill to determine if we add it to our renderer properties.
    //Boolean fill;

    Boolean shadow;
    Integer shadowAngle; // degrees
    Integer shadowOffset; // pixels
    Integer shadowDepth; // number of shadow strokes, each 1 shadow offset from the last
    Integer shadowAlpha; // 0 - 100 alpha
    Boolean highlightMouseOver;
    Boolean highlightMouseDown;
    String[] highlightColors;
    String dataLabels; // 'label','value','percent' or a comma separated list of labels
    Boolean showDataLabels;
    String dataLabelFormatString;
    Integer dataLabelPositionFactor; // 0 - 100 - multiplier of label position radius
    Integer dataLabelThreshold;
    Integer dataLabelNudge; // + or - pixels away from the center of the pie
    Boolean dataLabelCenter;
    Integer startAngle;

    public SectorSeries() {}

    public void add(String key, Object val) {
        getData().add(new SimpleEntry<String, Object>(key, val));
    }

    @Override
    public JSONBuilder getDataJSON(UIComponent chart) {
        JSONBuilder builder = super.getDataJSON(chart);
        Class valueType = null;

        for (Object x : getData()) {
            SimpleEntry<String, Object> s = (SimpleEntry<String, Object>) x;

            Object value = s.getValue();

            if (valueType == null) {
                if (value instanceof Number) valueType = Number.class;
                else if (value instanceof String) valueType = String.class;
            }

            if (valueType == Number.class)
                builder.beginArray().item(s.getKey()).item(((Number)value).doubleValue()).endArray();
            else if (valueType == String.class)
                builder.beginArray().item(s.getKey()).item((String)value).endArray();
        }

        builder.endArray();
        return builder;
    }

    @Override
    public JSONBuilder getConfigJSON(UIComponent component) {
        JSONBuilder cfg = super.getConfigJSON(component);

        if (type != null) {
            if (type.equals(SectorType.PIE))
                cfg.entry("renderer", "ice.ace.jq.jqplot.PieRenderer", true) ;
            else if (type.equals(SectorType.DONUT))
                cfg.entry("renderer", "ice.ace.jq.jqplot.DonutRenderer", true) ;
        }

        if (hasRenderOptionsSet()) {
            cfg.beginMap("rendererOptions");

            String labels = getDataLabels();
            Integer sliceMargin = getSliceMargin();
            Boolean showLabels = getShowDataLabels();
            Boolean fill = getFill();

            if (labels != null)
                cfg.entry("dataLabels", labels);
            if (showLabels != null)
                cfg.entry("showDataLabels", showLabels);
            if (fill != null)
                cfg.entry("fill", fill);
            if (sliceMargin != null)
                cfg.entry("sliceMargin", sliceMargin);

            cfg.endMap();
        }

        cfg.endMap();

        return cfg;
    }

    private boolean hasRenderOptionsSet() {
        return (getShowDataLabels() != null || getShowDataLabels() != null || getFill() != null || getSliceMargin() != null);
    }

    /**
     * Get the slice labels of this series.
     * @return "percent", "value" or a comma separated array of labels.
     */
    public String getDataLabels() {
        return dataLabels;
    }

    /**
     * Set the slice labels of this series
     * @param dataLabels "percent", "value" or a comma separated array of labels
     */
    public void setDataLabels(String dataLabels) {
        this.dataLabels = dataLabels;
    }

    /**
     * Get whether or not labels are rendered over each slice of the pie.
     * @return slice label visibility
     */
    public Boolean getShowDataLabels() {
        return showDataLabels;
    }

    /**
     * Set whether or not labels are rendered over each slice of the pie.
     * @param showDataLabels slice label visibility
     */
    public void setShowDataLabels(Boolean showDataLabels) {
        this.showDataLabels = showDataLabels;
    }

    /**
     * Set gap size between each slice.
     * @return size in degrees
     */
    public Integer getSliceMargin() {
        return sliceMargin;
    }

    /**
     * Get gap size between each slice.
     * @param sliceMargin size in degress
     */
    public void setSliceMargin(Integer sliceMargin) {
        this.sliceMargin = sliceMargin;
    }
}
