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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * AxisChart is the backend bean that supplies all the data for the axis
 * chart.
 *
 * @since 1.5
 */
public class AxisChart {
    
    //list of the Labels for the x axis of the chart
    private static final List labels = new ArrayList(Arrays.asList(
            new String[]{"2000", "2001", "2002", "2003", "2004", "2005",
                         "2006"}));

    //The list of the legend label for the chart
    private static final List legendLabels = new ArrayList(Arrays.asList(
            new String[]{"Bugs", "Enhancements", "Fixed"}));

    //The list of the data used by the chart
    private static final List data = new ArrayList(
            Arrays.asList(new double[][]{new double[]{350, 50, 400},
                                         new double[]{45, 145, 50},
                                         new double[]{-36, 6, 98},
                                         new double[]{66, 166, 74},
                                         new double[]{145, 105, 55},
                                         new double[]{80, 110, 4},
                                         new double[]{10, 90, 70}}));

    //The list of the colors used by the chart
    private static final List paints =
            new ArrayList(Arrays.asList(new Color[]{new Color(153, 0, 255, 100),
                                                    new Color(204, 0, 255, 150),
                                                    new Color(204, 0, 1,
                                                              150)}));


    public List getLabels() {

        return labels;
    }


    public List getLegendLabels() {

        return legendLabels;
    }


    public List getData() {

        return data;
    }


    public List getPaints() {

        return paints;
    }

}
