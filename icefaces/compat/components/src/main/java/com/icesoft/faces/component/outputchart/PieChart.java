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

package com.icesoft.faces.component.outputchart;

import org.krysalis.jcharts.Chart;
import org.krysalis.jcharts.chartData.PieChartDataSet;
import org.krysalis.jcharts.nonAxisChart.PieChart2D;
import org.krysalis.jcharts.nonAxisChart.PieChart3D;
import org.krysalis.jcharts.properties.ChartProperties;
import org.krysalis.jcharts.properties.LegendProperties;
import org.krysalis.jcharts.properties.PieChart2DProperties;
import org.krysalis.jcharts.properties.PieChart3DProperties;
import org.krysalis.jcharts.properties.util.ChartStroke;
import org.krysalis.jcharts.types.PieLabelType;

import javax.faces.component.UIComponent;
import java.awt.*;

public class PieChart extends AbstractChart {

    public PieChart()  {
    }

    public PieChart(UIComponent outputChart) throws Throwable {
        super(outputChart);
    }

    protected void buildChart(OutputChart outputChart) {
        try {
            if (outputChart.getType().equalsIgnoreCase("pie2d")) {
                chart = getPie2dChart(outputChart);
            } else if (outputChart.getType().equalsIgnoreCase("pie3d")) {
                chart = getPie3dChart(outputChart);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    double data[] = null;

    public double[] getData(Object obj) {
        if (data != null && obj instanceof String) {
            return data;
        }
        return data = super.getAsDoubleArray(obj);
    }

    String[] labels = null;

    public String[] getLabels(Object obj) {
        if (obj == null && labels == null) {
            return labels = getGeneratedLabels("label", data.length);
        } else if ((obj == null || obj instanceof String) && labels != null) {
            return labels;
        } else {
            return labels = getAsStringArray(obj);
        }
    }

    public Paint[] getPaints(Object obj) {
        return getPaints(obj, data.length);
    }

    private Chart getPie2dChart(OutputChart outputChart) throws Throwable {
        PieChart2DProperties pieChart2DProperties = new PieChart2DProperties();
        PieChartDataSet pieChartDataSet;
        pieChartDataSet = new PieChartDataSet(outputChart.getChartTitle(),
                                              getData(outputChart.getData()),
                                              getLabels(
                                                      outputChart.getLabels()),
                                              getPaints(
                                                      outputChart.getColors()),
                                              pieChart2DProperties);

        return new PieChart2D(pieChartDataSet,
        					  getLegendProperties(outputChart),
                              new ChartProperties(),
                              new Integer(outputChart.getWidth()).intValue(),
                              new Integer(outputChart.getHeight()).intValue());

    }

    private Chart getPie3dChart(OutputChart outputChart) throws Throwable {
        PieChart3DProperties pieChart3DProperties = new PieChart3DProperties();
        pieChart3DProperties.setDepth(30);
        pieChart3DProperties.setBorderChartStroke(
                new ChartStroke(new BasicStroke(1f), Color.black));
        pieChart3DProperties.setPieLabelType(PieLabelType.LEGEND_LABELS);
        PieChartDataSet pieChartDataSet =
                new PieChartDataSet(outputChart.getChartTitle(),
                                    getData(outputChart.getData()),
                                    getLabels(outputChart.getLabels()),
                                    getPaints(outputChart.getColors()),
                                    pieChart3DProperties);
        return new PieChart3D(pieChartDataSet, null, new ChartProperties(),
                              new Integer(outputChart.getWidth()).intValue(),
                              new Integer(outputChart.getHeight()).intValue());
    }
}
