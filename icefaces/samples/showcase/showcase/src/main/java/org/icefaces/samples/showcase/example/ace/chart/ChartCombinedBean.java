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
import org.icefaces.ace.model.chart.DragConstraintAxis;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

@ComponentExample(
        parent = ChartBean.BEAN_NAME,
        title = "example.ace.chart.combined.title",
        description = "example.ace.chart.combined.description",
        example = "/resources/examples/ace/chart/chartCombined.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="ChartCombined.xhtml",
                    resource = "/resources/examples/ace/chart/chartCombined.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ChartCombinedBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/chart/ChartCombinedBean.java")
        }
)
@ManagedBean(name= ChartCombinedBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartCombinedBean extends ComponentExampleImpl<ChartCombinedBean> implements Serializable
{
    public static final String BEAN_NAME = "chartCombinedBean";
    
    private List<CartesianSeries> barData = new ArrayList<CartesianSeries>() {{
        add(new CartesianSeries() {{
            add("Nickle", 28);
            add("Aluminum", 13);
            add("Xenon", 54);
            add("Silver", 47);
            add("Sulfur", 16);
            add("Silicon", 14);
            add("Vanadium", 23);
            setDragable(true);
            setDragConstraintAxis(DragConstraintAxis.Y);
            setLabel("Resources / Demand");
            setYAxis(2);
            setXAxis(2);
        }});

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
    
    private Axis barDemoDefaultAxis = new Axis() {{
        setTickAngle(-30);
    }};

    private Axis barDemoXOneAxis = new Axis() {{
        setType(AxisType.CATEGORY);
    }};

    private Axis[] barDemoYAxes = new Axis[] {
        new Axis() {{
            setAutoscale(true);
            setTickInterval("5");
            setLabel("USD Millions");
        }},
        new Axis() {{
            setAutoscale(true);
            setTickInterval("5");
            setLabel("Tonnes");
        }}
    };

    private Axis barDemoXTwoAxis = new Axis() {{
        setTicks(new String[] {"Nickle", "Aluminum", "Xenon", "Silver", "Sulfur", "Silicon", "Vanadium"});
        setType(AxisType.CATEGORY);
    }};
    
    public List<CartesianSeries> getBarData() {
        return barData;
    }

    public void setBarData(List<CartesianSeries> barData) {
        this.barData = barData;
    }
    
    public Axis getBarDemoXOneAxis() {
        return barDemoXOneAxis;
    }

    public void setBarDemoXOneAxis(Axis barDemoXOneAxis) {
        this.barDemoXOneAxis = barDemoXOneAxis;
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

    public Axis[] getBarDemoYAxes() {
        return barDemoYAxes;
    }

    public void setBarDemoYAxes(Axis[] barDemoYAxes) {
        this.barDemoYAxes = barDemoYAxes;
    }
    
    public ChartCombinedBean() {
        super(ChartCombinedBean.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
