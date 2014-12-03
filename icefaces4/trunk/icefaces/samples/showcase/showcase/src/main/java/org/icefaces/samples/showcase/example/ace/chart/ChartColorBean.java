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
import org.icefaces.ace.model.chart.SectorSeries;
import org.icefaces.ace.model.chart.SectorSeries.SectorType;
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
        title = "example.ace.chart.color.title",
        description = "example.ace.chart.color.description",
        example = "/resources/examples/ace/chart/chartColor.xhtml"
)
@ExampleResources(
        resources ={
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title="ChartColor.xhtml",
                        resource = "/resources/examples/ace/chart/chartColor.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title="ChartColorBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/chart/ChartColorBean.java")
        }
)
@ManagedBean(name= ChartColorBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartColorBean extends ComponentExampleImpl<ChartColorBean> implements Serializable {
    public static final String BEAN_NAME = "chartColorBean";
	public String getBeanName() { return BEAN_NAME; }

    String [] ticks = {"Jan", "Feb", "March", "April", "May", "June", "July", "August", "Sept", "Oct", "Nov", "Dec"};
    private List<SectorSeries> pieData = new ArrayList<SectorSeries>();
    private List<CartesianSeries> fillData = new ArrayList<CartesianSeries>();
    private CartesianSeries fillModel = new CartesianSeries();
    private final String[] colorSet = new String[]{"#a40000", "#FF8C00", "#FFE135"};

    int [] apples = {11,9,5,12,14,8,7,9,6,11,9,3};
    int [] oranges = {4,8,5,3,6,5,3,2,6,7,4,3} ;
    int [] bananas = {12,6,13,11,2,3,4,2,1,5,7,4};

    private boolean legend = true;
    private String[] customDefaultColor = colorSet;

    public ChartColorBean() {
        super(ChartColorBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
        calculatePieData();
        createModels();
    }
    private void calculatePieData(){
        int totalApples = 0;
        int totalOranges=0;
        int totalBananas = 0;
        SectorSeries pie1 = new SectorSeries();
        for (int i=0; i<12; i++){
            totalApples+= apples[i];
            totalOranges+= oranges[i];
            totalBananas+= bananas[i];
        }
        pie1.add("Apples", totalApples);
        pie1.add("Oranges", totalOranges);
        pie1.add("Bananas", totalBananas);
        pie1.setType(SectorType.PIE);
        pie1.setShowDataLabels(true);
        pie1.setSliceMargin(4);
        pieData.add(pie1);
    }

    private void createModels(){
        /* model for all series. CartesianSeries defaults to ChartType.LINE */
        fillModel.setFill(true);
        CartesianSeries fill1 = new CartesianSeries();
        CartesianSeries fill2 = new CartesianSeries();
        CartesianSeries fill3 = new CartesianSeries();
        fill1.setLabel("Apples");
        fill2.setLabel("Oranges");
        fill3.setLabel("Bananas") ;
        for (int i=0; i<12; i++){
            fill1.add(apples[i]);
            fill2.add(oranges[i]);
            fill3.add(bananas[i]);
        }
        fillData.add(fill1);
        fillData.add(fill2);
        fillData.add(fill3);
    }

    private Axis xAxis = new Axis() {{
        setType(AxisType.CATEGORY);
        setTicks(ticks);
        setDrawMajorGridlines(false);
        setTickAngle(-90);
    }};

    private Axis[] yAxes = new Axis[] {
        new Axis() {{

        }}
    };

    public List<CartesianSeries> getFillData() {
        return fillData;
    }

    public void setFillData(List<CartesianSeries> fillData) {
        this.fillData = fillData;
    }

    public CartesianSeries getFillModel() {
        return fillModel;
    }

    public void setFillModel(CartesianSeries fillModel) {
        this.fillModel = fillModel;
    }

    public List<SectorSeries> getPieData() {
        return pieData;
    }

    public void setPieData(List<SectorSeries> pieData) {
        this.pieData = pieData;
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

    public boolean isLegend() {
        return legend;
    }

    public void setLegend(boolean legend) {
        this.legend = legend;
    }

}
