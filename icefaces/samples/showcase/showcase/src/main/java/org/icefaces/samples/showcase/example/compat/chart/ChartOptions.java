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
import javax.faces.model.SelectItem;


import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = ChartBean.BEAN_NAME,
        title = "example.compat.chart.options.title",
        description = "example.compat.chart.options.description",
        example = "/resources/examples/compat/chart/chartOptions.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="chartOptions.xhtml",
                    resource = "/resources/examples/compat/"+
                               "chart/chartOptions.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ChartOptions.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/chart/ChartOptions.java")
        }
)
@ManagedBean(name= ChartOptions.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartOptions extends ComponentExampleImpl<ChartOptions> implements Serializable {
	
	public static final String BEAN_NAME = "chartOptions";
	
	private SelectItem[] availablePlacements = new SelectItem[] {
	    new SelectItem("bottom", "Bottom"),
	    new SelectItem("top", "Top"),
	    new SelectItem("left", "Left"),
	    new SelectItem("right", "Right"),
	    new SelectItem("none", "None")
	};
	
	private String chartTitle = "Custom Chart Title";
	private String xaxisTitle = "Custom X-Axis for Years";
	private String yaxisTitle = "Custom Y-Axis for Occurences";
	private String legendPlacement = availablePlacements[0].getValue().toString();
	private int width = 450;
	private int height = 300;
	
	public ChartOptions() {
		super(ChartOptions.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public SelectItem[] getAvailablePlacements() { return availablePlacements; }
	public String getChartTitle() { return chartTitle; }
	public String getXaxisTitle() { return xaxisTitle; }
	public String getYaxisTitle() { return yaxisTitle; }
	public String getLegendPlacement() { return legendPlacement; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public void setChartTitle(String chartTitle) { this.chartTitle = chartTitle; }
	public void setXaxisTitle(String xaxisTitle) { this.xaxisTitle = xaxisTitle; }
	public void setYaxisTitle(String yaxisTitle) { this.yaxisTitle = yaxisTitle; }
	public void setLegendPlacement(String legendPlacement) { this.legendPlacement = legendPlacement; }
	public void setWidth(int width) { this.width = width; }
	public void setHeight(int height) { this.height = height; }
}
