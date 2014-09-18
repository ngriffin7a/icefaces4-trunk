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
    private final String[] colorSet = new String[]{"#85802b", "#00749F", "#73C774", "#C7754C"};
    private final String[] firstColorSet = new String[]{"#f00", "#4b0", "#b40", "#ff0", "#fb0"} ;

    private CartesianSeries barWidthModel, bwm2, bwm3;
    private List<CartesianSeries> barData = new ArrayList<CartesianSeries>();
    private String chooseColorOption="default";
    private boolean stackSeries = false;

    CartesianSeries model = new CartesianSeries() ;
    String [] ticks = {"May", "June", "July", "August"};
    String [] customColors = null;

    public ChartBarBean() {
        super(ChartBarBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
        initModel();
    }

    public void initModel(){
        /* set values in model so that they are applied to all non-set CartesianSeries.
           Create a Series object without any data and set all options that are for all or most
           series.  If you need to override the default, then set it in the series that you want
           an override.  Note that barMargin, barPadding, shadowAngle are a few of the other attributes
           that can be set on the bar chart series or model.
         */
        model.setType(CartesianType.BAR);
        model.setHorizontalBar(true);
        model.setBarWidth(10);
        model.setBarMargin(5);
        model.setShadowAngle(135);
        /* create series and add to the List object */
        barWidthModel = new CartesianSeries();
        barWidthModel.add(2, 1);
        barWidthModel.add(4, 2);
        barWidthModel.add(6, 3);
        barWidthModel.add(3, 4);
        barWidthModel.setLabel("General");
        barData.add(barWidthModel);
        bwm2 = new CartesianSeries();
        bwm2.add(5, 1);
        bwm2.add(1, 2);
        bwm2.add(3, 3);
        bwm2.add(4, 4);
        bwm2.setLabel("Sales");
        barData.add(bwm2);
        bwm3 = new CartesianSeries();
        bwm3.add(4, 1);
        bwm3.add(7, 2);
        bwm3.add(1, 3);
        bwm3.add(2, 4);
        bwm3.setLabel("Marketing") ;
        barData.add(bwm3);
    }
    private Axis xAxisH = new Axis() {{
       setType(AxisType.CATEGORY);
    } };

    private Axis[] yAxesH = new Axis[] {
          new Axis(){{
               setType(AxisType.CATEGORY);
           }}
    };

    public List<CartesianSeries> getBarData() {
        return barData;
    }

    public void setBarData(List<CartesianSeries> barData) {
        this.barData = barData;
    }

    public ChartSeries getModel() {
        return (ChartSeries)model;
    }
 	private boolean highlight, legend=true;
    private int barWidth=10;
    private int barPadding=0;
    private int barMargin=0;
    private int shadowAngle=135;

    public String[] getCustomColors() {
        return customColors;
    }

    public int getBarPadding() {
        return barPadding;
    }

    public void setBarPadding(int barPadding) {
        this.barPadding = barPadding;
    }

    public int getBarWidth() {
        return barWidth;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }

    public int getBarMargin() {
        return barMargin;
    }

    public void setBarMargin(int barMargin) {
        this.barMargin = barMargin;
    }
    public Axis getxAxisH() {
        return xAxisH;
    }

    public void setxAxisH(Axis xAxisH) {
        this.xAxisH = xAxisH;
    }

    public Axis[] getyAxesH() {
        return yAxesH;
    }

    public void setyAxesH(Axis[] yAxesH) {
        this.yAxesH = yAxesH;
    }

    public void updateChart(AjaxBehaviorEvent abe){
        CartesianSeries s1 = barData.get(0);
        CartesianSeries s2 = barData.get(1);
        CartesianSeries s3 = barData.get(2);
        if (chooseColorOption.equals("custom")){
            this.customColors = colorSet;
            model.setVaryBarColor(false);
            this.legend=true;
        } else if (chooseColorOption.equals("vary")) {
            this.customColors= null;
            this.legend = false;
            model.setVaryBarColor(true);
            s1.setSeriesColors(firstColorSet);
            s2.setSeriesColors(firstColorSet);
            s3.setSeriesColors(firstColorSet) ;
        } else {
            this.customColors=null;
            this.legend=true;
            model.setVaryBarColor(false);
        }
        if (barWidth>0) {
            model.setBarWidth(barWidth);
        }
        else {
            model.setBarWidth(0);
        }
     }

    public int getShadowAngle() {
        return shadowAngle;
    }

    public void setShadowAngle(int shadowAngle) {
        this.shadowAngle = shadowAngle;
    }

    public boolean isLegend() {
        return legend;
    }

    public void setLegend(boolean legend) {
        this.legend = legend;
    }

    public String getChooseColorOption() {
        return chooseColorOption;
    }

    public void setChooseColorOption(String chooseColorOption) {
        this.chooseColorOption = chooseColorOption;
    }

    public boolean isStackSeries() {
        return stackSeries;
    }

    public void setStackSeries(boolean stackSeries) {
        this.stackSeries = stackSeries;
    }
}
