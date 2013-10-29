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

import org.icefaces.ace.util.JSONBuilder;

import java.io.Serializable;
import java.util.Date;

public class Axis implements Serializable {
    private Boolean forceTickAt0;
    private Boolean forceTickAt100;
    private Boolean show;
    private String label;
    // Needs to be Object to support date ranges
    private Object min;
    private Object max;
    private Boolean autoscale;
    private Boolean sortMergedLabels;
    private Boolean drawMajorGridlines;
    private String[] ticks;
    private Integer tickAngle;
    private String tickFontSize;
    private AxisType type;
    private Double pad;
    private Double padMin;
    private Double padMax;
    private String formatString;
    private String tickInterval;
    private String tickPrefix;

    /**
     * Used by the ChartRenderer to produce a JSON representation of the configuration of this axis.
     * @return the JSON configuration object
     */
    @Override
    public String toString() {
        JSONBuilder json = JSONBuilder.create();
        Object max = this.getMax();
        Object min = this.getMin();
        Double pad = this.getPad();
        Double padMin = this.getPadMin();
        Double padMax = this.getPadMax();
        Boolean show = this.getShow();
        Integer tickAngle = this.getTickAngle();
        Boolean autoscale = this.getAutoscale();
        String tickInterval = this.getTickInterval();
        String[] ticks = this.getTicks();
        Boolean drawMajorGridlines = this.getDrawMajorGridlines();

        json.beginMap();

        if (label != null)
            json.entry("label", label);

        if (drawMajorGridlines != null)
            json.entry("drawMajorGridLines", drawMajorGridlines);

        if (max != null)
            if (max instanceof Number)
                json.entry("max", ((Number)max).doubleValue());
            else if (max instanceof Date)
                json.entry("max", ((Date)max).getTime());
            else
                json.entry("max", max.toString());

        if (min != null)
            if (min instanceof Number)
                json.entry("min", ((Number)min).doubleValue());
            else if (min instanceof Date)
                json.entry("min", ((Date)min).getTime());
            else
                json.entry("min", min.toString());

        if (show != null)
            json.entry("show", show);

        if (autoscale != null)
            json.entry("autoscale", autoscale);

        if (pad != null)
           json.entry("pad", pad);
        if (padMin != null)
            json.entry("padMin", padMin);
        if (padMax != null)
            json.entry("padMax", padMax);

        if (type != null)
            json.entry("renderer", type.toString(), true);

        if (hasRendererOptionsSet()) {
            json.beginMap("rendererOptions");
            if (type == AxisType.CATEGORY && isSortMergedLabels() != null)
                json.entry("sortMergedLabels", isSortMergedLabels());
            if (isForceTickAt0() != null)
                json.entry("forceTickAt0", isForceTickAt0());
            if (isForceTickAt100() != null)
                json.entry("forceTickAt100", isForceTickAt100());
            json.endMap();
        }

        if (tickInterval != null)
            json.entry("tickInterval", tickInterval);

        if (ticks != null)
            encodeTicks(json);

        if (hasTickOptionsSet()) {
            json.entry("tickRenderer", "ice.ace.jq.jqplot.CanvasAxisTickRenderer", true);

            encodeTickOptions(json);
        }

        json.endMap();

        return json.toString();
    }

    private void encodeTickOptions(JSONBuilder json) {
        json.beginMap("tickOptions");
        String fontSize = getTickFontSize();
        Integer angle = getTickAngle();
        String formatString = getFormatString();
        String prefix = getTickPrefix();

        if (prefix != null) json.entry("prefix", prefix);
        if (formatString != null) json.entry("formatString", getFormatString());
        if (angle != null) json.entry("angle", angle);
        if (fontSize!= null) json.entry("fontSize", fontSize);

        json.endMap();
    }

    private boolean hasTickOptionsSet() {
        return (getTickAngle() != null || getTickFontSize() != null || getTickPrefix() != null || getFormatString() != null);
    }

    private boolean hasRendererOptionsSet() {
        if ((type == AxisType.CATEGORY && sortMergedLabels != null) || forceTickAt0 != null || forceTickAt100 != null || formatString != null)
            return true;
        return false;
    }

    private void encodeTicks(JSONBuilder json) {
        json.beginArray("ticks");
        Class tickType = null;
        for (Object tick : getTicks()) {
            if (tickType == null) {
                if (tick instanceof Number) tickType = Number.class;
                else if (tick instanceof String) tickType = String.class;
                else if (tick instanceof Date) tickType = Date.class;
            }

            if (tickType == Number.class)
                json.item(((Number)tick).doubleValue());
            else if (tickType == String.class) {
                json.item(tick.toString());
            } else if (tickType == Date.class) {
                json.item(((Date)tick).getTime());
            }
        }
        json.endArray();
    }

    /**
     * Get whether or not to force 0 to be shown on this axis.
     * @return whether or not to force 0 on this axis
     */
    public Boolean isForceTickAt0() {
        return forceTickAt0;
    }

    /**
     * Set whether or not to force 0 to be shown on this axis.
     * @param forceTickAt0 whether or not to force 0 on this axis
     */
    public void setForceTickAt0(Boolean forceTickAt0) {
        this.forceTickAt0 = forceTickAt0;
    }

    /**
     * Get whether or not to force 100 to be shown on this axis.
     * @return whether or not to force 100 on this axis
     */
    public Boolean isForceTickAt100() {
        return forceTickAt100;
    }

    /**
     * Set whether or not to force 100 to be shown on this axis.
     * @param forceTickAt100 whether or not to force 100 on this axis
     */
    public void setForceTickAt100(Boolean forceTickAt100) {
        this.forceTickAt100 = forceTickAt100;
    }

    /**
     * Get the value of the axis rendering toggle.
     * @return whether or not to render this axis
     */
    public Boolean getShow() {
        return show;
    }

    /**
     * Set the value of the axis rendering toggle.
     * @param show whether or not to render this axis
     */
    public void setShow(Boolean show) {
        this.show = show;
    }

    /**
     * Get the minimum value of this axis. Either a Date or Numeral.
     * If null, interpreted on the client from the x/y values of the series or from the explicit ticks given.
     * @return minimum axis value
     */
    public Object getMin() {
        return min;
    }

    /**
     * Set the minimum value of this axis. Either a Date or Numeral.
     * If null, interpreted on the client from the x/y values of the series or from the explicit ticks given.
     * @param min either a Date or Number
     */
    public void setMin(Object min) {
        this.min = min;
    }

    /**
     * Get the maximum value of this axis. Either a Date or Numeral.
     * If null, interpreted on the client from the x/y values of the series or from the explicit ticks given.
     * @return maximum axis value
     */
    public Object getMax() {
        return max;
    }

    /**
     * Set the maximum value of this axis. Either a Date or Numeral.
     * If null, interpreted on the client from the x/y values of the series or from the explicit ticks given.
     * @param max either a Date or Number
     */
    public void setMax(Object max) {
        this.max = max;
    }

    /**
     * Get the type of this Axis
     * If null, defaults on the client to Linear.
     * @return AxisType
     */
    public AxisType getType() {
        return type;
    }

    /**
     * Sets the type of this Axis, altering default tick behaviour and point alignment.
     * Potential modes are:
     * AxisType.LINEAR - The default. Renders tick every even integer.
     * AxisType.CATEGORY - Oft used for Bar Charts. Renders ticks between grid lines. Accepts string tick values.
     * AxisType.DATE - Renders dates in a variety of formats, including date ranges.
     * AxisType.LOGARITHMIC - Similar to linear, however tick placement is on a logarithmic scale.
     * @param type
     */
    public void setType(AxisType type) {
        this.type = type;
    }

    /**
     * Get the list of String used as explicit ticks on this axis.
     * @return array of tick values
     */
    public String[] getTicks() {
        return ticks;
    }

    /**
     * Set a list of String values to be used as explicit ticks on this axis.
     * @param ticks
     */
    public void setTicks(String[] ticks) {
        this.ticks = ticks;
    }

    /**
     * Is this axis sorting the ticks it is given from series definitions into an ordered axis?
     * @return merged ticks truth value
     */
    public Boolean isSortMergedLabels() {
        return sortMergedLabels;
    }

    /**
     * Set this axis to sort together the lists of values it interprets as its ticks.
     * @param sortMergedLabels
     */
    public void setSortMergedLabels(Boolean sortMergedLabels) {
        this.sortMergedLabels = sortMergedLabels;
    }

    /**
     * Get the multiplier that determines how far to extend the range above and below the data bounds. A value of 0 interpreted as no padding (pad=1.0).
     * @return the double value that is multiplied by the data range to determine padding
     */
    public Double getPad() {
        return pad;
    }

    /**
     * Set the multiplier that determines how far to extend the range above and below the data bounds. A value of 0 interpreted as no padding and is interpreted on the client as 1.0.
     * @param pad the double value that is multiplied by the data range to determine padding
     */
    public void setPad(Double pad) {
        this.pad = pad;
    }

    /**
     * Get the multiplier that determines how far to extend the range above the data bounds. A value of 0 interpreted as no padding and is interpreted on the client as 1.0.
     * @return the double value that is multiplied by the data range to determine padding
     */
    public Double getPadMax() {
        return padMax;
    }

    /**
     * Set the multiplier that determines how far to extend the range above the data bounds. A value of 0 interpreted as no padding and is interpreted on the client as 1.0.
     * @param padMax the double value that is multiplied by the data range to determine padding
     */
    public void setPadMax(Double padMax) {
        this.padMax = padMax;
    }

    /**
     * Get the multiplier that determines how far to extend the range below the data bounds. A value of 0 interpreted as no padding and is interpreted on the client as 1.0.
     * @return the double value that is multiplied by the data range to determine padding
     */
    public Double getPadMin() {
        return padMin;
    }

    /**
     * Set the multiplier that determines how far to extend the range below the data bounds. A value of 0 interpreted as no padding and is interpreted on the client as 1.0.
     * @param padMin the double value that is multiplied by the data range to determine padding
     */
    public void setPadMin(Double padMin) {
        this.padMin = padMin;
    }

    /**
     * Get the String used to label this axis when rendered.
     * @return the name string
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the String used to label this axis when rendered.
     * @param label the name of this axis
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Get the angle of the ticks in degrees.
     * @return degrees of rotation (+ or -) of the ticks from 0 degrees
     */
    public Integer getTickAngle() {
        return tickAngle;
    }

    /**
     * Set the angle of the ticks in degrees
     * @param tickAngle degrees of rotation (+ or -) of the ticks from 0 degrees
     */
    public void setTickAngle(Integer tickAngle) {
        this.tickAngle = tickAngle;
    }

    /**
     * Get the font size for the ticks.
     * @return css-style font size definition
     */
    public String getTickFontSize() {
        return tickFontSize;
    }

    /**
     * Set the font size for the ticks.
     * @param tickFontSize css-style font size definition
     */
    public void setTickFontSize(String tickFontSize) {
        this.tickFontSize = tickFontSize;
    }

    /**
     * Get if this axis will draw its scale so that it shares grid lines with other autoscaled axes.
     * @return whether or not this axis is autoscaling
     */
    public Boolean getAutoscale() {
        return autoscale;
    }

    /**
     * Set if this axis will draw its scale so that it shares grid lines with other autoscaled axes.
     * @param autoscale whether or not this axis is autoscaling
     */
    public void setAutoscale(Boolean autoscale) {
        this.autoscale = autoscale;
    }

    /**
     * Get the coded String that determines the output format of axis Date values.
     * @return coded format String
     */
    public String getFormatString() {
        return formatString;
    }

    /**
     * Set the coded String that determines the output format of axis Date values.
     * @param formatString coded string
     */
    public void setFormatString(String formatString) {
        this.formatString = formatString;
    }

    /**
     * Get the number of units between ticks.
     * @return a numeral string or a representation of date units '1 (month / week / day)' or 'x (months / weeks / days)'
     */
    public String getTickInterval() {
        return tickInterval;
    }

    /**
     * Set the number of units between ticks.
     * @param tickInterval a numeral string or a representation of date units '1 (month / week / day)' or 'x (months / weeks / days)'
     */
    public void setTickInterval(String tickInterval) {
        this.tickInterval = tickInterval;
    }

    /**
     * Get the prefix to the beginning of each tick.
     * @return prefix text
     */
    public String getTickPrefix() {
        return tickPrefix;
    }

    /**
     * Set the prefix to the beginning of each tick.
     * @param tickPrefix prefix text
     */
    public void setTickPrefix(String tickPrefix) {
        this.tickPrefix = tickPrefix;
    }

    /**
     * Get whether or not this axis draws grid lines for each tick.
     * @return major gridline visibility
     */
    public Boolean getDrawMajorGridlines() {
        return drawMajorGridlines;
    }

    /**
     * Set whether or not this axis draw grid lines for each tick.
     * @param drawMajorGridlines major gridline visible
     */
    public void setDrawMajorGridlines(Boolean drawMajorGridlines) {
        this.drawMajorGridlines = drawMajorGridlines;
    }
}
