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

package org.icefaces.samples.showcase.example.ace.chart;

import org.icefaces.ace.component.chart.Axis;
import org.icefaces.ace.component.chart.AxisType;
import org.icefaces.ace.model.chart.CartesianSeries;
import org.icefaces.ace.model.chart.DragConstraintAxis;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.event.ValueChangeEvent;
import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ComponentExample(
        parent = ChartBean.BEAN_NAME,
        title = "example.ace.chart.bar.title",
        description = "example.ace.chart.bar.description",
        example = "/resources/examples/ace/chart/chartBar.xhtml"
)
@ExampleResources(
        resources ={
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title="ChartBar.xhtml",
                        resource = "/resources/examples/ace/chart/chartBar.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title="ChartBarBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/chart/ChartBarBean.java")
        }
)
@ManagedBean(name= ChartBarBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartBarBean extends ComponentExampleImpl<ChartBarBean> implements Serializable {
    public static final String BEAN_NAME = "chartBarBean";

    public boolean varyBarColor = false;
    public boolean simple = true;
    private boolean legend = true;
    private String[] customDefaultColor = null;
    BarChartExample[] examples;
    private BarChartExample example;
    private String example2 = "SIMPLE";

    public ChartBarBean() {
        super(ChartBarBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getExample2() {
        return example2;
    }

    public void setExample2(String example2) {
        this.example2 = example2;
    }

    public void setExample(BarChartExample bce){
        this.example = bce;
    }


    public BarChartExample[] getExamples(){
        return BarChartExample.values();
    }

    private List<CartesianSeries> barData = new ArrayList<CartesianSeries>() {{
     add(new CartesianSeries() {{
            setType(CartesianType.BAR);
            add("HDTV Receiver", 15);
            add("Cup Holder Pinion Bob", 7);
            add("Generic Fog Lamp", 9);
            add("8 Track Control Module", 12);
            add("Sludge Pump Fourier Modulator", 3);
            add("Transceiver Spice Rack", 6);
            add("Hair Spray Danger Indicator", 18);
            setLabel("Product / Sales");
        }});
    }};

    private Axis xAxis = new Axis() {{
        setType(AxisType.CATEGORY);
        setTickAngle(-30);
    }};

    private Axis[] yAxes = new Axis[] {
            new Axis() {{
                setAutoscale(true);
                setTickInterval("5");
                setLabel("USD Millions");
            }}
    };

    public List<CartesianSeries> getBarData() {
        return barData;
    }

    public void setBarData(List<CartesianSeries> barData) {
        this.barData = barData;
    }

    public Axis getxAxis() {
        return xAxis;
    }

    public void setxAxis(Axis xAxis) {
        this.xAxis = xAxis;
    }

    public Axis[] getyAxes() {
        return yAxes;
    }

    public void setyAxes(Axis[] yAxes) {
        this.yAxes = yAxes;
    }

    public String[] getCustomDefaultColor() {
        return customDefaultColor;
    }

    public void setCustomDefaultColor(String[] customDefaultColor) {
        this.customDefaultColor = customDefaultColor;
    }

    public void updateChart(ValueChangeEvent event){
        CartesianSeries cs = (CartesianSeries)barData.get(0);
        String newval = event.getNewValue().toString();
        switch (BarChartExample.valueOf(newval)){
            case SIMPLE :
                cs.setVaryBarColor(Boolean.FALSE);
                cs.setSeriesColors(null);
                setLegend(true);
                setCustomDefaultColor(null);
                break;
            case VARY_SIMPLE :
                cs.setVaryBarColor(Boolean.TRUE);
                cs.setSeriesColors(null);
                setLegend(false);
                setCustomDefaultColor(null);
                break;
            case VARY_CUSTOM :
                cs.setVaryBarColor(Boolean.TRUE);
                cs.setSeriesColors(new String[]{"#85802b", "#00749F", "#73C774", "#C7754C", "#17BDB8", "#f00"}) ;
                setLegend(false);
                setCustomDefaultColor(null);
                break;
            case SIMPLE_CUSTOM:
                cs.setVaryBarColor(Boolean.FALSE);
                setLegend(true);
                setCustomDefaultColor(new String[]{"#85802b", "#00749F"});
                break;
        }
    }

    public boolean isLegend() {
        return legend;
    }

    public void setLegend(boolean legend) {
        this.legend = legend;
    }

    public boolean isSimple() {
        return simple;
    }

    public void setSimple(boolean simple) {
        this.simple = simple;
    }

    public enum BarChartExample {
        SIMPLE_CUSTOM("example.ace.chart.bar.SIMPLE_CUSTOM"),
        SIMPLE("example.ace.chart.bar.SIMPLE"),
        VARY_SIMPLE("example.ace.chart.bar.VARY_SIMPLE"),
        VARY_CUSTOM("example.ace.chart.bar.VARY_CUSTOM");

        private String propsLookup;

        private BarChartExample(String lookup){
            this.propsLookup = lookup;
        }

        public String getPropsLookup(){
            return this.propsLookup;
        }
    }

}
