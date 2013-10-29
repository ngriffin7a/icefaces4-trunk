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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * SalesBean retrieves sales data to be displayed in the UI.
 */
public class SalesBean extends Sales implements Serializable {

    private static final long serialVersionUID = 7046508728833936043L;

    /**
     * Method to build the sales list and create the chart using the data from
     * the Sales class
     *
     * @return list of sales items for charting.
     */
    public static List buildSales(PieChartBean pieChartBean) {
        ArrayList salesTemp = new ArrayList();

        Iterator it = masterSales.values().iterator();
        double price;
        String label;
        while (it.hasNext()) {

            Sales[] yearSale = (Sales[]) it.next();
            price = 0;
            label = "";
            for (int i = 0; i < yearSale.length; i++) {
                price += (yearSale[i]).getPrice();
                label = (yearSale[i]).getYear();
                salesTemp.add(yearSale[i]);
            }
            pieChartBean.getLabels().add(label);
            pieChartBean.getData().add(new Double(price));

        }
        return salesTemp;
    }
    
    /**
     * Method to set the displayed table data to correspond with the
     * year clicked
     *
     * @param year clicked
     */
    public static void setSalesForYear(PieChartBean pieChartBean, String year) {
        pieChartBean.getSales().clear();
        Sales[] yearSales = (Sales[]) masterSales.get(year);
        for (int i = 0; i < yearSales.length; i++) {
            pieChartBean.getSales().add(yearSales[i]);
        }
    }

}
