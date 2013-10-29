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

package com.icesoft.icefaces.tutorial.component.outputchart.basic;

import com.icesoft.faces.component.outputchart.OutputChart;

import java.io.Serializable;
import java.util.List;

import javax.faces.event.ActionEvent;

/**
 * The PieChartBean is responsible for holding all the UI information and
 * generating detailed sales data for the pie chart
 *
 * @since 1.5
 */
public class PieChartBean extends PieChart implements Serializable {

    private static final long serialVersionUID = -1420728945742006104L;
    //String displayed in the UI
    private static String clickedAreaValue = "Click on the image map below to display a chart value: ";

    //flag to determine if the graph needs rendering
    private boolean pieNeedsRendering = false;
    
    //list of the sales data from the sales class
    private List sales = SalesBean.buildSales(this);

    public PieChartBean() {
        super();
    }

    /**
     * Method to call the rendering of the chart based on the pieNeedsRendering
     * flag
     *
     * @param component chart component which will be rendered.
     * @return boolean true if OutputChart should be re-rendered; otherwise,
     *         false.
     */
    public boolean newChart(OutputChart component) {
        if (pieNeedsRendering) {
            pieNeedsRendering = false;
            return true;
        } else {
            return false;
        }
    }

    public String getClickedAreaValue() {
        return clickedAreaValue;
    }

    public void setClickedAreaValue(String clickedAreaValue) {
        PieChartBean.clickedAreaValue = clickedAreaValue;
    }

    // When a piece of the pie is clicked, retrieve the sales data for that year.
    public void imageClicked(ActionEvent event) {
        
        if (event.getSource() instanceof OutputChart) {
            OutputChart chart = (OutputChart) event.getSource();
            if (chart.getClickedImageMapArea().getLengendLabel() != null) {
                setClickedAreaValue(clickedAreaValue + chart
                        .getClickedImageMapArea().getLengendLabel()
                        + " : " +
                        chart.getClickedImageMapArea().getValue());
                SalesBean.setSalesForYear(this,
                        chart.getClickedImageMapArea().getLengendLabel());
                
                
            }
        }
    }
    
    public List getSales() {
        return sales;
    }

}
