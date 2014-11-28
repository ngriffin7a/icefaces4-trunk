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
import org.icefaces.ace.model.chart.CartesianSeries.CartesianType;
import org.icefaces.ace.model.chart.ChartSeries;
import org.icefaces.ace.model.chart.DragConstraintAxis;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.event.ValueChangeEvent;
import javax.faces.event.AjaxBehaviorEvent;
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
        title = "example.ace.chart.stackedFill.title",
        description = "example.ace.chart.stackedFill.description",
        example = "/resources/examples/ace/chart/chartStackedBar.xhtml"
)
@ExampleResources(
        resources ={
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title="ChartStackedBar.xhtml",
                        resource = "/resources/examples/ace/chart/chartStackedBar.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title="ChartStackedBarBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/chart/ChartStackedBarBean.java")
        }
)
@ManagedBean(name= ChartStackedBarBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartStackedBarBean extends ComponentExampleImpl<ChartStackedBarBean> implements Serializable {
    public static final String BEAN_NAME = "chartStackedBarBean";
	public String getBeanName() { return BEAN_NAME; }

    /* use a model for the fill bar example to show use of model */
    CartesianSeries modelFill = new CartesianSeries();
    private List<CartesianSeries> barsData = new ArrayList<CartesianSeries>();
    private CartesianSeries fillm1, fillm2, fillm3;
    String [] ticks = {"May", "June", "July", "August"};

    public ChartStackedBarBean() {
        super(ChartStackedBarBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
        createBarFillModel();  /* creates model for default settings and data for fill data chart */
    }

    /* for stacked bar example */
    private List<CartesianSeries> stackBarData = new ArrayList<CartesianSeries>() {
        {
            add(new CartesianSeries() {
                {
                    setType(CartesianType.BAR);
                    add(4);
                    add(6);
                    add(5);
                    add(0);
                    setPointLabelStacked(false);
                    setPointLabels(true);

                }
            });
            add(new CartesianSeries() {
                {
                    setType(CartesianType.BAR);
                    add(3);
                    add(1);
                    add(2);
                    add(4);
                    setPointLabelStacked(false);
                    setPointLabels(true);
                }
            });
            add(new CartesianSeries() {
                {
                    setType(CartesianType.BAR);
                    add(1);
                    add(1);
                    add(4);
                    add(2);
                    setPointLabelStacked(false);
                    setPointLabels(true);
                }
            });
        }
    };

    private Axis stackXAxis = new Axis() {
		{
			setType(AxisType.CATEGORY);
		}
	};

	private Axis[] barDemoYAxes = new Axis[] {
            new Axis() {
		    {
			    setAutoscale(true);
			    setLabel("USD Millions");
		    }
	    }, new Axis() {
		    {
			setAutoscale(true);
			setLabel("Tonnes");
		    }
	    }
    };

    public List<CartesianSeries> getStackBarData() {
		return stackBarData;
	}

	public void setStackBarData(List<CartesianSeries> stackBarData) {
		this.stackBarData = stackBarData;
	}

    public Axis getStackXAxis() {
        return stackXAxis;
    }

    public void setStackXAxis(Axis stackXAxis) {
        this.stackXAxis = stackXAxis;
    }

    public Axis[] getBarDemoYAxes() {
		return barDemoYAxes;
	}

	public void setBarDemoYAxes(Axis[] barDemoYAxes) {
		this.barDemoYAxes = barDemoYAxes;
	}

    /* backing data for fill bar chart */
    private void createBarFillModel(){
        //next are for the fillToZero example
        modelFill = new CartesianSeries();
        modelFill.setType(CartesianType.BAR);
        modelFill.setFillToZero(true);
        fillm1 = new CartesianSeries();
        fillm2 = new CartesianSeries();
        fillm3 = new CartesianSeries();
        fillm1.add(200);
        fillm1.add(600);
        fillm1.add(700);
        fillm1.add(1000);
        fillm1.setLabel("Hotel");
        fillm2.add(460);
        fillm2.add(-210);
        fillm2.add(690);
        fillm2.add(820);
        fillm2.setLabel("Registration");
        fillm3.add(-260);
        fillm3.add(-440);
        fillm3.add(320);
        fillm3.add(200);
        fillm3.setLabel("Airfare");
        barsData.add(fillm1);
        barsData.add(fillm2);
        barsData.add(fillm3);
    }
    private Axis xAxisFill = new Axis() { {
        setType(AxisType.CATEGORY);
        setTicks(ticks);
    }};

    private Axis[] yAxesFill = new Axis[]{
        new Axis(){{
            setPad(1.05);
            setFormatString("$%d");
        }}
    };

    public Axis[] getyAxesFill() {
        return yAxesFill;
    }

    public void setyAxesFill(Axis[] yAxesFill) {
        this.yAxesFill = yAxesFill;
    }

    public CartesianSeries getModelFill() {
        return modelFill;
    }

    public void setModelFill(CartesianSeries modelFill) {
        this.modelFill = modelFill;
    }

    public Axis getxAxisFill() {
        return xAxisFill;
    }

    public void setxAxisFill(Axis xAxisFill) {
        this.xAxisFill = xAxisFill;
    }

    public List<CartesianSeries> getBarsData() {
        return barsData;
    }

    public void setBarsData(List<CartesianSeries> barsData) {
        this.barsData = barsData;
    }
}
