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
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ComponentExample(
        parent = ChartBean.BEAN_NAME,
        title = "example.ace.chart.line.title",
        description = "example.ace.chart.line.description",
        example = "/resources/examples/ace/chart/chartLine.xhtml"
)
@ExampleResources(
        resources ={
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title="ChartLine.xhtml",
                        resource = "/resources/examples/ace/chart/chartLine.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title="ChartLineBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/chart/ChartLineBean.java")
        }
)
@ManagedBean(name= ChartLineBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartLineBean extends ComponentExampleImpl<ChartLineBean> implements Serializable {
    public static final String BEAN_NAME = "chartLineBean";

    public ChartLineBean() {
        super(ChartLineBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    private List<CartesianSeries> lineData = new ArrayList<CartesianSeries>() {{
        add(new CartesianSeries() {{
            add("Nickle", 28);
            add("Aluminum", 13);
            add("Xenon", 54);
            add("Silver", 47);
            add("Sulfur", 16);
            add("Silicon", 14);
            add("Vanadium", 23);
            setLabel("Resources / Demand");
        }});
    }};

    private Axis[] yAxes = new Axis[] {
            new Axis() {{
                setAutoscale(true);
                setTickInterval("5");
                setLabel("Tonnes");
            }}
    };

    private Axis xAxis = new Axis() {{
        setTicks(new String[] {"Nickle", "Aluminum", "Xenon", "Silver", "Sulfur", "Silicon", "Vanadium"});
        setType(AxisType.CATEGORY);
    }};


    public List<CartesianSeries> getLineData() {
        return lineData;
    }

    public void setLineData(List<CartesianSeries> lineData) {
        this.lineData = lineData;
    }

    public Axis[] getyAxes() {
        return yAxes;
    }

    public void setyAxes(Axis[] yAxes) {
        this.yAxes = yAxes;
    }

    public Axis getxAxis() {
        return xAxis;
    }

    public void setxAxis(Axis xAxis) {
        this.xAxis = xAxis;
    }
}
