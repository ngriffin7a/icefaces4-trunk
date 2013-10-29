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

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.icesoft.faces.component.outputchart.OutputChart;

import java.io.Serializable;

/**
 * AxisChartBean is the backend bean handling all UI interaction with
 * the AxisChart.
 *
 * @since 1.5
 */
public class AxisChartBean extends AxisChart implements Serializable {

    private static final long serialVersionUID = -5566104048052983899L;
    //sets the chart type to bar for default
    private String type = OutputChart.BAR_CHART_TYPE;
    
    //List of charts that the user can seleect from the drop down menu
    private static SelectItem[] chartList = new SelectItem[]{
        new SelectItem(OutputChart.AREA_CHART_TYPE),
        new SelectItem(OutputChart.AREA_STACKED_CHART_TYPE),
        new SelectItem(OutputChart.BAR_CHART_TYPE),
        new SelectItem(OutputChart.BAR_CLUSTERED_CHART_TYPE),
        new SelectItem(OutputChart.BAR_STACKED_CHART_TYPE),
        new SelectItem(OutputChart.LINE_CHART_TYPE),
        new SelectItem(OutputChart.POINT_CHART_TYPE),};

    //Flag to determine whether the type of chart was changed
    private boolean chartChangedFlag = true;

    private static final String DEFAULT_STRING =
        "Click on the image map below to display a chart value: ";
    
    //sets the string returned when the chart is clicked to the default value
    private String clickedValue = DEFAULT_STRING;
    
    // Used to show the orientation radio buttons
    private boolean orientationChange = true;
    
    //Title for the x-axis
    private String titleX = "Years";
    
    //Title for the y-axis
    private String titleY = "Problems";
    
    // Used for bar and barclustered horizontal attribute
    private boolean horizontal = false;

    public AxisChartBean(){
        super();
    }

    /*
     * Returns the list of charts to choose from
     * @return list of possible charts.
     */
    public SelectItem[] getChartList() {
        return chartList;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }
    
    /*
     * Determines whether the chart was changed
     * Sets the default_string if the area type charts are selected
     *@param ValueChangeEvent event
     */
    public void chartChanged(ValueChangeEvent event) {
        chartChangedFlag = true;
        
        if (event.getNewValue().equals(OutputChart.AREA_CHART_TYPE) ||
                event.getNewValue().equals(OutputChart.AREA_STACKED_CHART_TYPE)) {
            setClickedValue(
                    "A client side image map is not supported for Area charts " +
                    "(clicking on the chart will not display any values)");
        } else {
            setClickedValue(DEFAULT_STRING);
        }
        //if it is a bar chart then show the orientation radio buttons
        if(event.getNewValue().equals(OutputChart.BAR_CHART_TYPE) || 
              event.getNewValue().equals(OutputChart.BAR_CLUSTERED_CHART_TYPE)){
            orientationChange = true;
            if(horizontal == true){
                //align labels to the proper axis
                titleX = "Problems";
                titleY = "Years";
            }
        }
        else {
            orientationChange = false;
            //align labels to the proper axis
            titleX = "Years";
            titleY = "Problems";
        }
            
    }

    /*
     * Returns the clickedValue
     *@return String clickedValue
     */
    public String getClickedValue() {
        
        return clickedValue;
    }
    
    /*
     * Sets the clicked value
     *@param String clickedValue
     */
    public void setClickedValue(String clickedValue) {
        
        this.clickedValue = clickedValue;
    }

    /**
     * Gets the orientationChange value.  True is for when it is a bar or
     * barclustered chart.  False is all other charts.  Used to display the
     * radio button group for changing chart orientation.
     *
     * @return orientationChange true for visible, false is not visible.
     */
    public boolean getOrientationChange() {
        return orientationChange;
    }
    
    /**
     * Sets the orientationChange value, true, radio button group is visivble,
     * false, the radio button group is not visible.
     *
     * @param orientationChange true for visible, false is not.  
     */
    public void setOrientationChange(boolean orientationChange) {
        this.orientationChange = orientationChange;
    }
    
    /**
     * Method called when the orientation is change by the user.
     *
     * @param event contains new orientation value.
     */
    public void selectOrientation(ValueChangeEvent event){
        String temp = titleX;
        if(event.getNewValue().equals("true")){
            //set the orientation and reverse the axis labels
            horizontal = true;
            titleX = titleY;
            titleY = temp;
        }
        else{
            //set the orientation and reverse the axis labels
            horizontal = false;
            titleX = titleY;
            titleY = temp;
        }
        //render the chart
        chartChangedFlag = true;
    }
    
    /**
     * Gets the title of the x-axis.
     *
     * @return titleX name for the x-axis.
     */
    public String getTitleX(){
        return titleX;
    }
    
    /**
     * Sets the title of the x-axis.
     *
     * @param titleX new title of the x-axis.
     */
    public void setTitleX(String titleX){
        this.titleX = titleX;
    }
    
    /**
     * Gets the title of the y-axis.
     *
     * @return titleY name for the y-axis.
     */
    public String getTitleY(){
        return titleY;
    }
    
    /**
     * Sets the title of the y-axis.
     *
     * @param titleY new title of the y-axis.
     */
    public void setTitleY(String titleY){
        this.titleY = titleY;
    }
    
    /**
     * Sets the horizontal, true the chart will be displayed in a 
     * horizontal orientation, false the chart will be in a vertical 
     * orientation.
     *
     * @param horizontal true for horizontal, false for vertical.
     */
    public void setHorizontal(boolean horizontal){
        this.horizontal = horizontal;
    }
    
    /**
     * Gets the horizontal boolean value for the charts orientation.
     *
     * @return horizontal boolean value for chart orientation.
     */
    public boolean getHorizontal(){
        return horizontal;
    }

    /**
     * When the image map has been clicked this method returns the axis label plus the value
     *@param ActionEvent event
     */
    public void imageClicked(ActionEvent event) {
        
        if (event.getSource() instanceof OutputChart) {
            OutputChart chart = (OutputChart) event.getSource();
            if (chart.getClickedImageMapArea().getXAxisLabel() != null) {
                
                setClickedValue(DEFAULT_STRING +
                        chart.getClickedImageMapArea()
                        .getXAxisLabel() +
                        "  :  " +  chart.getClickedImageMapArea()
                        .getValue());

            }
            
        }
        
    }
    
    /**
     * Determines whether or not the application should render based on status change
     *@return boolean
     */
    public boolean newChart(OutputChart component) {
        
        if (chartChangedFlag) {
            chartChangedFlag = false;
            return true;
        } else {
            return chartChangedFlag;
        }
    }
}
