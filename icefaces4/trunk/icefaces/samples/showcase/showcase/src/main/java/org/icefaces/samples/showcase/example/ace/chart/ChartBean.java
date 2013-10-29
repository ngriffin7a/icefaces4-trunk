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
 
package org.icefaces.samples.showcase.example.ace.chart;

import org.icefaces.ace.component.chart.Axis;
import org.icefaces.ace.component.chart.AxisType;
import org.icefaces.ace.model.chart.CartesianSeries;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ComponentExample(
    title = "example.ace.chart.title",
    description = "example.ace.chart.description",
    example = "/resources/examples/ace/chart/chart.xhtml"
)
@ExampleResources(
    resources ={
        // xhtml
        @ExampleResource(type = ResourceType.xhtml,
                title="Chart.xhtml",
                resource = "/resources/examples/ace/chart/chart.xhtml"),
        // Java Source
        @ExampleResource(type = ResourceType.java,
                title="ChartBean.java",
                resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/chart/ChartBean.java")
    }
)
@Menu(
    title = "menu.ace.chart.subMenu.title",
    menuLinks = {
        @MenuLink(title = "menu.ace.chart.subMenu.main",
                isDefault = true, exampleBeanName = ChartBean.BEAN_NAME),
        @MenuLink(title = "menu.ace.chart.subMenu.bar",
                exampleBeanName = ChartBarBean.BEAN_NAME),
        @MenuLink(title = "menu.ace.chart.subMenu.bubble",
                exampleBeanName = ChartBubbleBean.BEAN_NAME),
        @MenuLink(title = "menu.ace.chart.subMenu.candlestick",
                exampleBeanName = ChartCandlestickBean.BEAN_NAME),
        @MenuLink(title = "menu.ace.chart.subMenu.donut",
                exampleBeanName = ChartDonutBean.BEAN_NAME),
        @MenuLink(title = "menu.ace.chart.subMenu.gauge",
                exampleBeanName = ChartGaugeBean.BEAN_NAME),
        @MenuLink(title = "menu.ace.chart.subMenu.line",
                exampleBeanName = ChartLineBean.BEAN_NAME),
        @MenuLink(title = "menu.ace.chart.subMenu.pie",
                exampleBeanName = ChartPieBean.BEAN_NAME),
        @MenuLink(title = "menu.ace.chart.subMenu.combined",
                exampleBeanName = ChartCombinedBean.BEAN_NAME),
        @MenuLink(title = "menu.ace.chart.subMenu.dynamic",
                exampleBeanName = ChartDynamicBean.BEAN_NAME),
        @MenuLink(title = "menu.ace.chart.subMenu.export",
                exampleBeanName = ChartExportBean.BEAN_NAME)
    }
)

@ManagedBean(name= ChartBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartBean extends ComponentExampleImpl<ChartBean> implements Serializable {
    public static final String BEAN_NAME = "chartBean";
    
    private Random randomizer = new Random(System.currentTimeMillis());
    
    private CartesianSeries barData = new CartesianSeries() {{
        setType(CartesianType.BAR);
        add("HDTV Receiver", randomizer.nextInt(20));
        add("Cup Holder Pinion Bob", randomizer.nextInt(20));
        add("Generic Fog Lamp", randomizer.nextInt(20));
        add("8 Track Control Module", randomizer.nextInt(20));
        add("Sludge Pump Fourier Modulator", randomizer.nextInt(20));
        add("Transceiver Spice Rack", randomizer.nextInt(20));
        add("Hair Spray Danger Indicator", randomizer.nextInt(20));
        setLabel("Product / Sales");
    }};
    
    private Axis barDemoDefaultAxis = new Axis() {{
        setTickAngle(-30);
    }};

    private Axis barDemoXAxis = new Axis() {{
        setType(AxisType.CATEGORY);
    }};

    private Axis barDemoYAxis = new Axis() {{
            setAutoscale(true);
            setTickInterval("5");
            setLabel("USD Millions");
}};

    private Axis barDemoXTwoAxis = new Axis() {{
        setTicks(new String[] {"Nickle", "Aluminum", "Xenon", "Silver", "Sulfur", "Silicon", "Vanadium"});
        setType(AxisType.CATEGORY);
    }};

    public CartesianSeries getBarData() {
        return barData;
    }

    public void setBarData(CartesianSeries barData) {
        this.barData = barData;
    }

    public Axis getBarDemoXAxis() {
        return barDemoXAxis;
    }

    public void setBarDemoXAxis(Axis barDemoXAxis) {
        this.barDemoXAxis = barDemoXAxis;
    }

    public Axis getBarDemoDefaultAxis() {
        return barDemoDefaultAxis;
    }

    public void setBarDemoDefaultAxis(Axis barDemoDefaultAxis) {
        this.barDemoDefaultAxis = barDemoDefaultAxis;
    }

    public Axis getBarDemoXTwoAxis() {
        return barDemoXTwoAxis;
    }

    public void setBarDemoXTwoAxis(Axis barDemoXTwoAxis) {
        this.barDemoXTwoAxis = barDemoXTwoAxis;
    }

    public Axis getBarDemoYAxis() {
        return barDemoYAxis;
    }

    public void setBarDemoYAxis(Axis barDemoYAxis) {
        this.barDemoYAxis = barDemoYAxis;
    }
    
    public ChartBean() {
        super(ChartBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }    
}
