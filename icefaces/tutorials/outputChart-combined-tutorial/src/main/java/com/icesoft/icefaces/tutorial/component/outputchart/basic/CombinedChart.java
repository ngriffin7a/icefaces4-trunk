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

import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.io.Serializable;

import javax.faces.event.ActionEvent;

import org.krysalis.jcharts.axisChart.AxisChart;
import org.krysalis.jcharts.chartData.AxisChartDataSet;
import org.krysalis.jcharts.chartData.DataSeries;
import org.krysalis.jcharts.properties.AxisProperties;
import org.krysalis.jcharts.properties.BarChartProperties;
import org.krysalis.jcharts.properties.ChartProperties;
import org.krysalis.jcharts.properties.LegendProperties;
import org.krysalis.jcharts.properties.LineChartProperties;
import org.krysalis.jcharts.properties.PointChartProperties;
import org.krysalis.jcharts.test.TestDataGenerator;
import org.krysalis.jcharts.types.ChartType;

import com.icesoft.faces.component.outputchart.OutputChart;

/**
 * CombinedChartBean is the backend bean handling the Combined Chart.
 *
 * @since 1.5
 */
public class CombinedChart implements Serializable {

    private static final long serialVersionUID = 8486500013246791132L;
    //the text value returned after clicking on the chart
    private String clickedValue;

    //local variable for the axis chart component of the combined chart
    private static AxisChart axisChart;
    
    //flag to determine if initialized
    private boolean initialzed = false;
    
    /**
     * Method to change the output text to the valuse selected by the user when
     * they click on the chart
     *
     * @param event JSF action event
     */
    public void action(ActionEvent event) {
        if (event.getSource() instanceof OutputChart) {
            OutputChart chart = (OutputChart) event.getSource();
            clickedValue = "";
            if (chart.getClickedImageMapArea().getXAxisLabel() != null) {
                setClickedValue(chart.getClickedImageMapArea().getXAxisLabel() +
                                "  :  " +
                                chart.getClickedImageMapArea().getValue());
            }
        }
    }


    public String getClickedValue() {
        return clickedValue;
    }

    public void setClickedValue(String clickedValue) {
        this.clickedValue = clickedValue;
    }
    
    /**
     * Method to tell the page to render or not based on the initialized flag
     *
     * @param component chart component which will be rendered.
     * @return boolean true if OutputChart should be re-rendered; otherwise,
     *         false.
     */
    public boolean newChart(OutputChart component) {
        if (axisChart == null || component.getChart() == null)
            buildAxisChart();
        component.setChart(axisChart);

        return !initialzed && (initialzed = true);

    }
    
    private void buildAxisChart() {
        try {
            String[] xAxisLabels =
                    {"1998", "1999", "2000", "2001", "2002", "2003", "2004"};
            String xAxisTitle = "Years";
            String yAxisTitle = "Problems";
            String title = "Company Software";
            DataSeries dataSeries =
                    new DataSeries(xAxisLabels, xAxisTitle, yAxisTitle, title);

            double[][] data = TestDataGenerator.getRandomNumbers(3, 7, 0, 5000);
            String[] legendLabels = {"Bugs", "Security Holes", "Backdoors"};
            Paint[] paints = new Color[]{
                                new Color(0xCAE1EF), 
                                new Color(0xF78208),
                                new Color(0x0D4274) };

            BarChartProperties barChartProperties = new BarChartProperties();
            AxisChartDataSet axisChartDataSet =
                    new AxisChartDataSet(data,
                                         legendLabels,
                                         paints,
                                         ChartType.BAR,
                                         barChartProperties);
            dataSeries.addIAxisPlotDataSet(axisChartDataSet);


            data = TestDataGenerator.getRandomNumbers(2, 7, 1000, 5000);
            legendLabels = new String[]{"Patches", "New Patch Bugs"};
            paints = new Paint[]{ new Color(0x0D4274),
                                new Color(0xF78208)};

            Stroke[] strokes = {LineChartProperties.DEFAULT_LINE_STROKE,
                                LineChartProperties.DEFAULT_LINE_STROKE};
            Shape[] shapes = {PointChartProperties.SHAPE_CIRCLE,
                              PointChartProperties.SHAPE_TRIANGLE};
            LineChartProperties lineChartProperties =
                    new LineChartProperties(strokes, shapes);
            axisChartDataSet = new AxisChartDataSet(data, legendLabels, paints,
                                                    ChartType.LINE,
                                                    lineChartProperties);
            dataSeries.addIAxisPlotDataSet(axisChartDataSet);


            ChartProperties chartProperties = new ChartProperties();
            AxisProperties axisProperties = new AxisProperties();
            LegendProperties legendProperties = new LegendProperties();

            axisChart = new AxisChart(dataSeries, chartProperties,
                                      axisProperties,
                                      legendProperties, 500, 500);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}