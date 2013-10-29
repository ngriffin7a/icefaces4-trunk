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
import org.icefaces.ace.json.JSONArray;
import org.icefaces.ace.json.JSONException;
import org.icefaces.ace.model.SimpleEntry;
import org.icefaces.ace.model.chart.ChartSeries;
import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.impl.util.Base64;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@MandatoryResourceComponent(tagName="chart", value="org.icefaces.ace.component.chart.Chart")
public class ChartRenderer extends CoreRenderer {
    @Override
    public void	decode(FacesContext context, UIComponent component) {
        Chart chart = (Chart) component;
        String id = chart.getClientId(context);
        String select = id + "_selection";
        String drag = id + "_drag";
        String export = id + "_export";
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();

        String selectInput = params.get(select);
        String dragInput = params.get(drag);
        String exportInput = params.get(export);

        if (selectInput != null) processSelections(chart, selectInput.split(","));
        if (dragInput != null) processDraggings(chart, dragInput);
        if (exportInput != null) processExport(chart, exportInput);

        decodeBehaviors(context, component);
    }

    private void processDraggings(Chart chart, String draggingInput) {
        try {
            JSONArray array = new JSONArray(draggingInput);
            for (int i = 0; i < array.length(); i++) {
                JSONArray drag = array.getJSONArray(i);
                JSONArray newVals = drag.getJSONArray(0);
                Object newX = newVals.get(0);
                Object newY = newVals.get(1);

                Integer seriesIndex = drag.getInt(1);
                Integer pointIndex = drag.getInt(2);

                // Set to new val
                List seriesList = (List)chart.getValue();
                List seriesData = ((ChartSeries) seriesList.get(seriesIndex)).getData();
                Map.Entry point = (Map.Entry) seriesData.get(pointIndex);

                Object[] oldValArr = entryToArray(point);
                if (oldValArr[0] instanceof Date) newX = new Date(((Number)newX).longValue());
                if (oldValArr[1] instanceof Date) newY = new Date(((Number)newY).longValue());

                Map.Entry newVal = new SimpleEntry(newX, newY);

                seriesData.set(pointIndex, newVal);

                // Queue value change event
                chart.queueEvent(new PointValueChangeEvent(chart, oldValArr, entryToArray(newVal), seriesIndex, pointIndex));
            }
        } catch (JSONException e) {
            throw new FacesException(e);
        }
    }

    private Object[] entryToArray(Map.Entry point) {
        return new Object[] {point.getKey(), point.getValue()};
    }

    private void processSelections(Chart chart, String[] select) {
        int seriesIndex = Integer.parseInt(select[0]);
        int pointIndex = Integer.parseInt(select[1]);
        chart.queueEvent(new SeriesSelectionEvent(chart, seriesIndex, pointIndex));
    }

    private void processExport(Chart chart, String exportInput) {
        if (chart.getImageExportListener() != null) {
            exportInput = exportInput.substring(22); // remove "data:image/png;base64,"
            byte[] bytes = Base64.decode(exportInput);
            chart.queueEvent(new ChartImageExportEvent(chart, bytes));
        }
    }

    @Override
    public void	encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Chart chart = (Chart) component;
        String clientId = component.getClientId();

        writer.startElement(HTML.DIV_ELEM, chart);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        encodeChartContainer(context, writer, clientId,
                chart.getHeight(),
                chart.getWidth(),
                chart.getStyle());
        encodeScript(context, writer, clientId, chart);
        writer.endElement(HTML.DIV_ELEM);
    }

    private void encodeScript(FacesContext context, ResponseWriter writer, String clientId, Chart component) throws IOException {
        String widgetVar = resolveWidgetVar(component);
        List<ChartSeries> data = (List<ChartSeries>)component.getValue();
        ChartSeries seriesDefaults = component.getDefaultSeriesConfig();
        Boolean stacking = component.isStackSeries();
        Boolean animated = component.isAnimated();
        Boolean hiddenInit = component.isHiddenInitPolling();
        Boolean zoom = component.isZoom();
        Boolean cursor = component.isCursor();
        Boolean showTooltip = component.isShowTooltip();

        String title = component.getTitle();

        JSONBuilder json = JSONBuilder.create().initialiseVar(widgetVar)
                .beginFunction("ice.ace.create").item("Chart")
                .beginArray().item(clientId);

        writer.startElement(HTML.SPAN_ELEM, null);
        writer.startElement(HTML.SCRIPT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);

        // Build data arrays
        json.beginArray();
        if (data != null)
            for (ChartSeries series : data)
                json.item(series.getDataJSON(component).toString(), false);
        json.endArray();


        // Build configuration object
        json.beginMap();
        encodeAxesConfig(json, component);
        encodeSeriesConfig(json, component, seriesDefaults, data);
        encodeLegendConfig(json, component);
        encodeHighlighterConfig(json, component);
        if (title != null) json.entry("title", title);
        if (stacking) json.entry("stackSeries", true);
        if (animated == null) json.entry("animate", "!ice.ace.jq.jqplot.use_excanvas", true);
        else if (animated) json.entry("animate", true);
        if (isAjaxClick(component))
            json.entry("handlePointClick", true);
        if (!hiddenInit) json.entry("disableHiddenInit", true);
        encodeClientBehaviors(context, component, json);

        if (cursor != null) {
            json.beginMap("cursor");
            json.entry("show", cursor);
            if (zoom != null) json.entry("zoom", zoom);
            if (showTooltip != null) json.entry("showTooltip", showTooltip);
            json.endMap();
        }

        if (cursor != null) {
            json.beginMap("cursor");
            json.entry("show", cursor);
            if (zoom != null) json.entry("zoom", zoom);
            if (showTooltip != null) json.entry("showTooltip", showTooltip);
            json.endMap();
        }

        json.endMap().endArray().endFunction();;

        writer.write(json.toString());

        writer.endElement(HTML.SCRIPT_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
    }

    private void encodeHighlighterConfig(JSONBuilder cfgBuilder, Chart component) {
        if (component.isHighlighter() != null && component.isHighlighter()) {
            cfgBuilder.beginMap("highlighter");
            cfgBuilder.entry("show", true);

            Boolean showMarker = component.isHighlighterShowMarker();
            Location location = component.getHighlighterLocation();
            HighlighterTooltipAxes axes = component.getHighlighterAxes();
            String formatString = component.getHighlighterFormatString();
            Integer yvals = component.getHighlighterYValueCount();
            Boolean btf = component.isHighlighterBringSeriesToFront();

            if (showMarker != null) cfgBuilder.entry("showMarker", showMarker);
            if (location != null) cfgBuilder.entry("tooltipLocation", location.toString());
            if (axes != null) cfgBuilder.entry("tooltipAxes", axes.toString());
            if (formatString != null) cfgBuilder.entry("formatString", formatString);
            if (yvals != null) cfgBuilder.entry("yvalues", yvals);
            if (btf != null) cfgBuilder.entry("bringSeriesToFront", btf);

            cfgBuilder.endMap();
        }
    }
    
    private void encodeSeriesConfig(JSONBuilder cfg, Chart chart, ChartSeries defaults, List<ChartSeries> series) {
        // If defined, add default series config
        if (defaults != null)
            cfg.entry("seriesDefaults", defaults.getConfigJSON(chart).toString(), true);
        else if (series != null && series.size() > 0) {
            try {
                // Configure the default type of this plot as the type of the first series.
                ChartSeries firstSeries = series.get(0);

                // Get reference to class of Series- if init shorthand is used, the superclass must be accessed.
                Class seriesClass = firstSeries.getClass().getSuperclass() != ChartSeries.class
                        ? firstSeries.getClass().getSuperclass()
                        : firstSeries.getClass();

                ChartSeries dummySeries = ((ChartSeries)seriesClass.newInstance());

                ChartSeries.ChartType firstSeriesType = firstSeries.getType();

                // If the first series doesn't have a configured type, render the default type
                dummySeries.setType(firstSeriesType != null ? firstSeriesType : dummySeries.getDefaultType());
                cfg.entry("seriesDefaults", dummySeries.getConfigJSON(chart).toString(), true);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // If defined, add per-series config
        cfg.beginArray("series");
        if (series != null)
            for (ChartSeries s : series)
                cfg.item(s.getConfigJSON(chart).toString(), false);
        cfg.endArray();
    }

    private void encodeAxesConfig(JSONBuilder cfgBuilder, Chart component) {
        Axis axesDefaults = component.getDefaultAxesConfig();

        if (axesDefaults != null)
            cfgBuilder.entry("axesDefaults", axesDefaults.toString(), true);

        if (component.hasAxisConfig()) {
            Axis xAxis = component.getXAxis();
            Axis x2Axis = component.getX2Axis();
            Axis[] yAxes = component.getYAxes();
            cfgBuilder.beginMap("axes");
            if (xAxis != null)
                cfgBuilder.entry("xaxis", xAxis.toString(), true);
            if (x2Axis != null)
                cfgBuilder.entry("x2axis", x2Axis.toString(), true);
            if (yAxes != null)
                for (int i = 0; i < yAxes.length; i++)
                    cfgBuilder.entry(i == 0 ? "yaxis" : "y"+(i+1)+"axis", yAxes[i].toString(), true);
            cfgBuilder.endMap();
        }
    }

    private void encodeLegendConfig(JSONBuilder cfgBuilder, Chart component) {
        Boolean legend = component.isLegend();
        if (legend != null && legend) {
            cfgBuilder.beginMap("legend");
            cfgBuilder.entry("show", true);

            Location pos = component.getLegendLocation();
            LegendPlacement place = component.getLegendPlacement();

            if (place != null) cfgBuilder.entry("placement", place.toString());
            if (pos != null) cfgBuilder.entry("location", pos.toString());

            cfgBuilder.endMap();
        }
    }

    private void encodeChartContainer(FacesContext context, ResponseWriter writer, String clientId, Integer height, Integer width, String userStyle) throws IOException {
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_chart", null);

        String style = (userStyle != null) ? userStyle : "";
        if (height != null) style += "height:"+height+"px; ";
        if (width != null) style += "width:"+width+"px; ";

        writer.writeAttribute(HTML.STYLE_ATTR, style, null);
        writer.endElement(HTML.DIV_ELEM);
    }

    public boolean isAjaxClick(Chart component) {
        if (component.getClientBehaviors().get("click") != null) return true;
        return component.getSelectListener() != null;
    }
}
