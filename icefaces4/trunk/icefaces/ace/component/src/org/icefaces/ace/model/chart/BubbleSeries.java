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
import java.util.Date;

public class BubbleSeries extends ChartSeries {
    private Boolean escapeHtml;
    private Boolean showLabels;
    private Boolean bubbleGradients;
    private Boolean varyBubbleColors;
    private Integer bubbleAlpha;

    private Boolean autoscaleBubbles;
    private Double autoscaleMultiplier;
    private Double autoscalePointsFactor;

    private Boolean highlightMouseOver;
    private Boolean highlightMouseDown;
    private String[] highlightColors;
    private Integer highlightAlpha;




    public static enum BubbleType implements ChartType {
        BUBBLE
    }

    public BubbleSeries() {}

    /**
     * Add a new bubble to this set of bubbles.
     * @param x x tick of the bubble
     * @param y y tick of the bubble
     * @param magnitude the area of the bubble
     */
    public void add(Object x, Object y, int magnitude) {
        getData().add(new Object[] {x, y, magnitude, null});
    }

    /**
     * Add a new bubble to this set of bubbles.
     * @param x x tick of the bubble
     * @param y y tick of the bubble
     * @param magnitude the area of the bubble
     * @param label the label of this bubble
     */
    public void add(Object x, Object y, int magnitude, String label) {
        getData().add(new Object[] {x, y, magnitude, label});
    }

    /**
     * Used by the ChartRenderer to produce a JSON representation of the configuration of this series.
     * @return the serialized JSON object
     * @param chart
     */
    @Override
    public JSONBuilder getDataJSON(UIComponent chart) {
        JSONBuilder cfg = super.getDataJSON(chart);

        Class xType = null;
        Class yType = null;

        for (Object o : getData()) {
            Object[] arr = (Object[]) o;
            cfg.beginArray();

            if (xType == null)
                xType = getObjType(arr[0]);

            if (yType == null)
                yType = getObjType(arr[1]);

            if (xType == Number.class) cfg.item(((Number)arr[0]).doubleValue());
            else if (xType == String.class) cfg.item((String)arr[0]);
            else if (xType == Date.class) cfg.item(((Date)arr[0]).getTime());

            if (yType == Number.class) cfg.item(((Number)arr[1]).doubleValue());
            else if (yType == String.class) cfg.item((String)arr[1]);
            else if (yType == Date.class) cfg.item(((Date)arr[1]).getTime());

            cfg.item((Integer)arr[2]);

            if (arr[3] != null)
                cfg.item((String)arr[3]);

            cfg.endArray();
        }

        cfg.endArray();
        return cfg;
    }

    private Class getObjType(Object o) {
        if (o instanceof Number) return Number.class;
        else if (o instanceof Date) return Date.class;
        else if (o instanceof String) return String.class;
        else throw new IllegalArgumentException("ace:chart - Bubble series coordinates can only be supplied as Date, Number or String.");
    }

    /**
     * Used by the ChartRenderer to produce a JSON representation of the data of this series.
     * @return the JSON object
     * @param component
     */
    @Override
    public JSONBuilder getConfigJSON(UIComponent component) {
        JSONBuilder cfg = super.getConfigJSON(component);

        if (type != null) {
            if (type.equals(BubbleType.BUBBLE))
                cfg.entry("renderer", "ice.ace.jq.jqplot.BubbleRenderer", true);
        }

        if (hasRenderOptionsSet()) {
            cfg.beginMap("rendererOptions");
            if (bubbleGradients != null)
                cfg.entry("bubbleGradients", bubbleGradients);

            if (bubbleAlpha != null)
                cfg.entry("bubbleAlpha",
                        bubbleAlpha.doubleValue() / 100d);

            if (highlightAlpha != null)
                cfg.entry("highlightAlpha",
                        highlightAlpha.doubleValue() / 100d);

            if (showLabels != null)
                cfg.entry("showLabels", showLabels);

            if (varyBubbleColors != null)
                cfg.entry("varyBubbleColors", varyBubbleColors);

            if (escapeHtml != null)
                cfg.entry("escapeHtml", escapeHtml);

            if (autoscaleBubbles != null)
                cfg.entry("autoscaleBubbles", autoscaleBubbles);

            if (autoscaleMultiplier != null)
                cfg.entry("autoscaleMultiplier", autoscaleMultiplier);

            if (autoscalePointsFactor != null)
                cfg.entry("autoscalePointsFactor", autoscalePointsFactor);

            if (highlightMouseDown != null)
                cfg.entry("highlightMouseDown", highlightMouseDown);

            if (highlightMouseOver != null)
                cfg.entry("highlightMouseOver", highlightMouseOver);

            if (highlightColors != null) {
                cfg.beginArray("highlightColors");
                for (String c : highlightColors) cfg.item(c);
                cfg.endArray();
            }

            cfg.endMap();
        }

        cfg.endMap();
        return cfg;
    }

    private boolean hasRenderOptionsSet() {
        return bubbleGradients != null || bubbleAlpha != null
                || highlightAlpha != null || showLabels != null
                || varyBubbleColors != null || escapeHtml != null
                || autoscaleBubbles != null || autoscaleMultiplier != null
                || autoscalePointsFactor != null || highlightMouseDown != null
                || highlightMouseOver != null || highlightColors != null;
    }

    @Override
    public ChartType getDefaultType() {
        return BubbleType.BUBBLE;
    }

    /**
     * Get if this series colors the bubbles with gradients instead of normal colors.
     * @return enable gradient boolean
     */
    public Boolean isBubbleGradients() {
        return bubbleGradients;
    }

    /**
     * Get if this series colors the bubbles with gradients instead of normal colors.
     * @param bubbleGradients enable gradient boolean
     */
    public void setBubbleGradients(Boolean bubbleGradients) {
        this.bubbleGradients = bubbleGradients;
    }

    /**
     * Get the percentage of opacity for the bubble points.
     * @return integer value 0 to 100
     */
    public Integer getBubbleAlpha() {
        return bubbleAlpha;
    }

    /**
     * Set the percentage of opacity for the bubble points.
     * @param bubbleAlpha integer value 0 to 100
     */
    public void setBubbleAlpha(Integer bubbleAlpha) {
        this.bubbleAlpha = bubbleAlpha;
    }

    /**
     * Get if the labels are shown on each bubble.
     * @return show bubble label boolean
     */
    public Boolean getShowLabels() {
        return showLabels;
    }

    /**
     * Set if the labels are shown on each bubble.
     * @param showLabels show bubble label boolean
     */
    public void setShowLabels(Boolean showLabels) {
        this.showLabels = showLabels;
    }

    /**
     * Get the percentage of opacity for a bubble point when hovered over.
     * @return integer value 0 to 100
     */
    public Integer getHighlightAlpha() {
        return highlightAlpha;
    }

    /**
     * Set the percentage of opacity for a bubble point when hovered over.
     * @param highlightAlpha integer value 0 to 100
     */
    public void setHighlightAlpha(Integer highlightAlpha) {
        this.highlightAlpha = highlightAlpha;
    }

    /**
     * Get if this series will vary rendered bubble colors. If true, bubble colors are
     * supplied by the seriesColors property or the default JQPlot vector of colors.
     * If there are more bubbles than colors, the colors will loop.
     * If false, the bubble color is supplied by the color property.
     * @return enable varied bubble colors
     */
    public Boolean getVaryBubbleColors() {
        return varyBubbleColors;
    }

    /**
     * Set if this series will vary rendered bubble colors. If true, bubble colors are
     * supplied by the seriesColors property or the default JQPlot vector of colors.
     * If there are more bubbles than colors, the colors will loop.
     * If false, the bubble color is supplied by the color property.
     * @param varyBubbleColors
     */
    public void setVaryBubbleColors(Boolean varyBubbleColors) {
        this.varyBubbleColors = varyBubbleColors;
    }

    /**
     * Get if the series escapes HTML in the bubble labels.
     * @return enable label html escaping boolean
     */
    public Boolean getEscapeHtml() {
        return escapeHtml;
    }

    /**
     * Set if the series escapes HTML in the bubble labels.
     * @param escapeHtml enable label html escaping boolean
     */
    public void setEscapeHtml(Boolean escapeHtml) {
        this.escapeHtml = escapeHtml;
    }

    /**
     * Get if the series scales the bubbles based on plot scale.
     * If false, the radius value given is interpreted as raw pixel radius.
     * @return scale bubble by plot size boolean
     */
    public Boolean getAutoscaleBubbles() {
        return autoscaleBubbles;
    }

    /**
     * Set if the series scales the bubbles based on plot scale.
     * If false, the radius value given is interpreted as raw pixel radius.
     * @param autoscaleBubbles scale bubble by plot size boolean
     */
    public void setAutoscaleBubbles(Boolean autoscaleBubbles) {
        this.autoscaleBubbles = autoscaleBubbles;
    }

    /**
     * Get a multiplier to modify the size of autoscaled bubble radius
     * @return positive double value
     */
    public Double getAutoscaleMultiplier() {
        return autoscaleMultiplier;
    }

    /**
     * Set a multiplier to modify the size of autoscaled bubble radius
     * @param autoscaleMultiplier positive double value
     */
    public void setAutoscaleMultiplier(Double autoscaleMultiplier) {
        this.autoscaleMultiplier = autoscaleMultiplier;
    }

    /**
     * Get factor which decreases bubble size based on how many bubbles are
     * on on the chart. 0 means no adjustment for number of bubbles.
     * Negative values will decrease size of bubbles as more bubbles
     * are added. Values between 0 and -0.2 should work well.
     * @return point factor
     */
    public Double getAutoscalePointsFactor() {
        return autoscalePointsFactor;
    }

    /**
     * Set factor which decreases bubble size based on how many bubbles are
     * on on the chart. 0 means no adjustment for number of bubbles.
     * Negative values will decrease size of bubbles as more bubbles
     * are added. Values between 0 and -0.2 should work well.
     * @param autoscalePointsFactor double value
     */
    public void setAutoscalePointsFactor(Double autoscalePointsFactor) {
        this.autoscalePointsFactor = autoscalePointsFactor;
    }

    /**
     * Get if the highlighter is triggered when mousing over a bubble.
     * @return mouse over highlight enabled
     */
    public Boolean getHighlightMouseOver() {
        return highlightMouseOver;
    }

    /**
     * Set if the highlighter is triggered when mousing over a bubble.
     * @param highlightMouseOver mouse over highlight enabled
     */
    public void setHighlightMouseOver(Boolean highlightMouseOver) {
        this.highlightMouseOver = highlightMouseOver;
    }

    /**
     * Get if the highlighter is triggered when using a mouse button over a bubble.
     * HighlightMouseOver must be false if this property is enabled.
     * @return mouse button highlight enabled
     */
    public Boolean getHighlightMouseDown() {
        return highlightMouseDown;
    }

    /**
     * Set if the highlighter is triggered when using a mouse button over a bubble.
     * HighlightMouseOver must be false if this property is enabled.
     * @param highlightMouseDown mouse button highlight enabled
     */
    public void setHighlightMouseDown(Boolean highlightMouseDown) {
        this.highlightMouseDown = highlightMouseDown;
    }

    /**
     * Get the list of CSS color definitions to be applied to the bubbles when highlighted.
     * @return array of String CSS color definitions
     */
    public String[] getHighlightColors() {
        return highlightColors;
    }

    /**
     * Set the list of CSS color definitions to be applied to the bubbles when highlighted.
     * @param highlightColors array of String CSS color definitions
     */
    public void setHighlightColors(String[] highlightColors) {
        this.highlightColors = highlightColors;
    }
}
