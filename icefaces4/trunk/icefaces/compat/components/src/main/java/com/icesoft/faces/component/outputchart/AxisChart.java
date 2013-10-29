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

import org.krysalis.jcharts.axisChart.ScatterPlotAxisChart;
import org.krysalis.jcharts.chartData.AxisChartDataSet;
import org.krysalis.jcharts.chartData.DataSeries;
import org.krysalis.jcharts.chartData.ScatterPlotDataSeries;
import org.krysalis.jcharts.chartData.ScatterPlotDataSet;
import org.krysalis.jcharts.properties.AreaChartProperties;
import org.krysalis.jcharts.properties.AxisProperties;
import org.krysalis.jcharts.properties.BarChartProperties;
import org.krysalis.jcharts.properties.ChartProperties;
import org.krysalis.jcharts.properties.ChartTypeProperties;
import org.krysalis.jcharts.properties.ClusteredBarChartProperties;
import org.krysalis.jcharts.properties.DataAxisProperties;
import org.krysalis.jcharts.properties.LineChartProperties;
import org.krysalis.jcharts.properties.PointChartProperties;
import org.krysalis.jcharts.properties.ScatterPlotProperties;
import org.krysalis.jcharts.properties.StackedAreaChartProperties;
import org.krysalis.jcharts.properties.StackedBarChartProperties;
import org.krysalis.jcharts.test.TestDataGenerator;
import org.krysalis.jcharts.types.ChartType;

import javax.faces.component.UIComponent;
import java.awt.*;
import java.awt.geom.Point2D;

public class AxisChart extends AbstractChart {

    public AxisChart()  {
    }

    public AxisChart(UIComponent uiComponent) throws Throwable {
        super(uiComponent);
    }

    protected void buildChart(OutputChart outputChart) throws Throwable {
        getData(outputChart.getData());
        if (outputChart.getType().equalsIgnoreCase(OutputChart.AREA_CHART_TYPE)) {
            buildAreaChart(outputChart);
        } else if (outputChart.getType().equalsIgnoreCase(OutputChart.AREA_STACKED_CHART_TYPE)) {
            buildAreaStackedChart(outputChart);
        } else if (outputChart.getType().equalsIgnoreCase(OutputChart.BAR_CHART_TYPE)) {
            buildBarChart(outputChart);
        } else if (outputChart.getType().equalsIgnoreCase(OutputChart.BAR_STACKED_CHART_TYPE)) {
            buildBarStackedChart(outputChart);
        } else
        if (outputChart.getType().equalsIgnoreCase(OutputChart.BAR_CLUSTERED_CHART_TYPE)) {
            buildBarClusteredChart(outputChart);
        } else if (outputChart.getType().equalsIgnoreCase(OutputChart.LINE_CHART_TYPE)) {
            buildLineChart(outputChart);
        } else if (outputChart.getType().equalsIgnoreCase(OutputChart.POINT_CHART_TYPE)) {
            buildPointChart(outputChart);
        } else if (outputChart.getType().equalsIgnoreCase(OutputChart.SCATTER_PLOT_CHART_TYPE)) {
            buildScatterPlotChart(outputChart);
        }
    }



	private void buildAreaChart(OutputChart outputChart) throws Throwable {
        AreaChartProperties areaChartProperties = new AreaChartProperties();
        buildAxisChart(ChartType.AREA, areaChartProperties, outputChart);
    }

    private void buildAreaStackedChart(OutputChart outputChart) throws Throwable {
        StackedAreaChartProperties areaChartProperties =
                new StackedAreaChartProperties();
        buildAxisChart(ChartType.AREA_STACKED, areaChartProperties, outputChart);
    }

    private void buildBarChart(OutputChart outputChart) throws Throwable {
        BarChartProperties barChartProperties = new BarChartProperties();
        buildAxisChart(ChartType.BAR, barChartProperties, outputChart);
    }

    private void buildBarStackedChart(OutputChart outputChart) throws Throwable {
        StackedBarChartProperties barChartProperties =
                new StackedBarChartProperties();
        buildAxisChart(ChartType.BAR_STACKED, barChartProperties, outputChart);
    }

    private void buildBarClusteredChart(OutputChart outputChart) throws Throwable {
        ClusteredBarChartProperties barChartProperties =
                new ClusteredBarChartProperties();
        buildAxisChart(ChartType.BAR_CLUSTERED, barChartProperties, outputChart);
    }

    private void buildLineChart(OutputChart outputChart) throws Throwable {
        Stroke[] strokes = new Stroke[data.length];
        for (int i = 0; i < data.length; i++) {
            strokes[i] = LineChartProperties.DEFAULT_LINE_STROKE;
        }
        LineChartProperties lineChartProperties = new LineChartProperties(
                strokes, getShapes(outputChart.getShapes()));
        buildAxisChart(ChartType.LINE, lineChartProperties, outputChart);
    }

    private void buildPointChart(OutputChart outputChart) throws Throwable {
        Paint[] outlinePaints = TestDataGenerator.getRandomPaints(data.length);
        boolean[] fillPointFlags = new boolean[data.length];
        for (int i = 0; i < data.length; i++) {
            fillPointFlags[i] = true;
        }
        PointChartProperties pointChartProperties = new PointChartProperties(
                getShapes(outputChart.getShapes()), fillPointFlags,
                outlinePaints);
        buildAxisChart(ChartType.POINT, pointChartProperties, outputChart);
    }
    
    private void buildScatterPlotChart(OutputChart outputChart) throws Throwable {
    	Stroke[] strokes = new Stroke[data.length];
        for (int i = 0; i < data.length; i++) {
            strokes[i] = new BasicStroke( 0 );
        }
        ScatterPlotProperties pointChartProperties = new ScatterPlotProperties(
        		strokes,
                getShapes(outputChart.getShapes()));
        buildScatterPlotChart(ChartType.SCATTER_PLOT, pointChartProperties, outputChart);
		
	}
    
	
	private ScatterPlotDataSet createScatterPlotDataSet( OutputChart outputChart, ScatterPlotProperties chartTypeProperties)
	{
		ScatterPlotDataSet scatterPlotDataSet = new ScatterPlotDataSet( chartTypeProperties );
		int maxLength = -1;
		for (int index = 0; index < data.length; index++) {
			if (data[index].length > maxLength) {
				maxLength = data[index].length;
			}
		}
		if (maxLength == 0) {
			// for some reason you have to have 2 points at least in a dataset otherwise
			// this statement will get an ArrayIndexOutOfBoundsException in ScatterPlotChart.java
			// Line2D.Float line = new Line2D.Float( xAxisCoordinates[ 0 ][ 0 ],
			//		  yAxisCoordinates[ 0 ][ 0 ],
			//		  xAxisCoordinates[ 0 ][ 1 ],
			//		  yAxisCoordinates[ 0 ][ 1 ] );
			maxLength = 2;
		}
		for (int index = 0; index < data.length; index++) {
			Point2D.Double[] points = new Point2D.Double[ maxLength ];
			int length = 0;
			if (data[index].length != 0) {
				length = (data[index].length)/2;

				for( int x = 0; x < data[index].length; x+=2 )
				{
					
					points[ x/2] = ScatterPlotDataSet.createPoint2DDouble();
					points[ x/2 ].setLocation( data[index][x], data[index][x+1] );
				}
			}
			for (int i = length; i < maxLength; i++) {
				points[ i ] = ScatterPlotDataSet.createPoint2DDouble();
				points[ i ].setLocation( Double.NaN, Double.NaN );	
			}
	
			scatterPlotDataSet.addDataPoints( points, getPaints(outputChart.getColors())[index], getAsLabelsArray(outputChart.getLabels())[index] );

		}
		return scatterPlotDataSet;
	}
    
    void buildScatterPlotChart(ChartType chartType,
    		ScatterPlotProperties chartTypeProperties, OutputChart outputChart)
			throws Throwable {
    	
		ScatterPlotDataSet scatterPlotDataSet = this.createScatterPlotDataSet(outputChart,chartTypeProperties);

		ScatterPlotDataSeries scatterPlotDataSeries = new ScatterPlotDataSeries( scatterPlotDataSet,
				outputChart.getXaxisTitle(),
				outputChart.getYaxisTitle(),
				outputChart.getChartTitle() );

		double[] ranges = getAsDoubleArray(outputChart.getXaxisLabels());

		DataAxisProperties xAxisProperties = new DataAxisProperties();
		
		xAxisProperties.setUserDefinedScale( ranges[0], ranges[1] );
		xAxisProperties.setNumItems( (int) ranges[2] );
		xAxisProperties.setRoundToNearest( (int) ranges[3] );

		DataAxisProperties yAxisProperties = new DataAxisProperties();
		yAxisProperties.setUserDefinedScale( ranges[4], ranges[5] );
		yAxisProperties.setNumItems( (int) ranges[6] );
		yAxisProperties.setRoundToNearest( (int) ranges[7] );

		AxisProperties axisProperties = new AxisProperties( xAxisProperties, yAxisProperties );

		chart = new ScatterPlotAxisChart( scatterPlotDataSeries,
				 new ChartProperties(),
				 axisProperties,
				 getLegendProperties(outputChart),
				 new Integer(outputChart
							.getWidth()).intValue(),
				 new Integer(outputChart
						.getHeight()).intValue() );

	}


    void buildAxisChart(ChartType chartType,
                        ChartTypeProperties chartTypeProperties,
                        OutputChart outputChart
                        )
            throws Throwable {
        DataSeries dataSeries = new DataSeries(
                getAsXaxisLabelsArray(outputChart.getXaxisLabels()),
                outputChart.getXaxisTitle(),
                outputChart.getYaxisTitle(),
                outputChart.getChartTitle());

        AxisChartDataSet axisChartDataSet = new AxisChartDataSet(
                getAs2dDoubleArray(outputChart.getData()),
                getAsLabelsArray(outputChart.getLabels()),
                getPaints(outputChart.getColors()),
                chartType,
                chartTypeProperties);

        AxisProperties axisProperties = ((chartType.equals(ChartType.BAR) || 
                chartType.equals(ChartType.BAR_CLUSTERED) ||
                chartType.equals(ChartType.BAR_STACKED)) && 
                outputChart.isHorizontal())?
                new AxisProperties(true): new AxisProperties();

        dataSeries.addIAxisPlotDataSet(axisChartDataSet);
        chart = new org.krysalis.jcharts.axisChart.AxisChart(dataSeries,
                                                             new ChartProperties(),
                                                             axisProperties,
                                                             getLegendProperties(outputChart),
                                                             new Integer(
                                                                     outputChart.getWidth()).intValue(),
                                                             new Integer(
                                                                     outputChart.getHeight()).intValue());
    }

    private Shape[] shapes;

    private Shape[] getShapes(Object obj) {
    	// must recalulate generated shapes if data length changed
        if (obj == null && (shapes == null || shapes.length != data.length)) {
            return shapes = getGeneratedShapes(data.length);
        } else if (obj == null && shapes != null) {
            return shapes;
        } else {
            return shapes = getAsShapeArray(obj);
        }
    }

    String[] xaxisLabels = null;

    public String[] getAsXaxisLabelsArray(Object obj) {
        if (obj == null && xaxisLabels == null) {
            return xaxisLabels = getGeneratedLabels("Xlabel", data[0].length);
        } else if (obj == null && xaxisLabels != null) {
            return xaxisLabels;
        } else {
            return getAsStringArray(obj);
        }
    }

    String[] labels = null;

    public String[] getAsLabelsArray(Object obj) {
        if (obj == null && labels == null) {
            return labels = getGeneratedLabels("Label", data.length);
        } else if (obj == null && labels != null) {
            return labels;
        } else {
            return getAsStringArray(obj);
        }
    }

    private double[][] data = null;

    public double[][] getData(Object obj) {
        if (obj instanceof String && data != null && data.equals(obj)) {
            return data;
        } else {
            return data = getAs2dDoubleArray(obj);
        }
    }

    public Paint[] getPaints(Object obj) {
        return getPaints(obj, data.length);
    }
}
