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

package org.icefaces.samples.showcase.example.compat.chart;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import com.icesoft.faces.component.outputchart.OutputChart;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = ChartBean.BEAN_NAME,
        title = "example.compat.chart.pie2d.title",
        description = "example.compat.chart.pie2d.description",
        example = "/resources/examples/compat/chart/chartPie2d.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="chartPie2d.xhtml",
                    resource = "/resources/examples/compat/"+
                               "chart/chartPie2d.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ChartPie2d.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/chart/ChartPie2d.java")
        }
)
@ManagedBean(name= ChartPie2d.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartPie2d extends ComponentExampleImpl<ChartPie2d> implements Serializable {
	
	public static final String BEAN_NAME = "chartPie2d";
	
	private ChartModelRadial model = new ChartModelRadial("Pie 2D", false, false, false, false);
	
	public ChartPie2d() {
		super(ChartPie2d.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public String getType() { return OutputChart.PIE2D_CHART_TYPE; }
	public ChartModelRadial getModel() { return model; }
	
	public void setModel(ChartModelRadial model) { this.model = model; }
}
