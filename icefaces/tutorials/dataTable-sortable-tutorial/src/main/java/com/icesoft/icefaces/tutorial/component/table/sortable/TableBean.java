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

package com.icesoft.icefaces.tutorial.component.table.sortable;

import java.util.Comparator;
import java.util.Arrays;
import java.io.Serializable;

/**
 * <p>
 * A basic backing bean for an ice:dataTable component.  This bean contains
 * an array of InventoryItem objects which is used as the dataset for a
 * dataTable component.  Each instance variable in the InventoryItem object
 * is represented as a column in the dataTable component.
 * </p>
 */
public class TableBean implements Serializable {

    //  List of sample inventory data.
    private InventoryItem[] carInventory = new InventoryItem[]{
            new InventoryItem(58285, "Dodge Grand Caravan", "Sto&Go/Keyless",  43500, 21695),
            new InventoryItem(57605, "Dodge SX 2.0", "Loaded/Keyless", 28000 ,14495),
            new InventoryItem(57805, "Chrysler Sebring Touring", "Keyless/Trac Cont", 31500, 15995),
            new InventoryItem(57965, "Chrysler PT Cruiser Convertible", "Touring/Loaded", 7000 , 22195),
            new InventoryItem(58095, "Chrysler Pacifica AWD", "Heated Lthr/19' Alloy", 43500, 31995),
            new InventoryItem(58165, "Jeep Liberty Sport", "Loaded/Keyless", 31000, 26995),
            new InventoryItem(58205, "Dodge SX 2.0", "Loaded/Keyless", 19500, 15495),
            new InventoryItem(58245, "Chrysler Pacifica AWD", "Moonroof/DVD", 15500, 35695),
            new InventoryItem(58295, "Pontiac Montana SV6 Ext", "Loaded/Quads", 40000, 22695),
            new InventoryItem(58355, "Jeep Grand Cherokee", "Laredo/Trailer", 26500, 27495),
            new InventoryItem(58365, "Jeep Grand Cherokee", "Laredo/Trailer", 27000, 28595),
            new InventoryItem(58375, "Chrysler PT Cruiser", "Cruise/KeylessD", 29500, 17795),
            new InventoryItem(58425, "Dodge Durango SLT", "Leather/3rd row", 32500, 26695),
            new InventoryItem(58475, "Dodge Grand Caravan", "Quads/Rear AC", 52000, 19895),
            new InventoryItem(58455, "Chrysler Sebring Touring", "Keyless/Trac Cont", 34000, 16695),
            new InventoryItem(58465, "Chrysler Sebring Touring", "Keyless/Trac Cont", 32500, 15995),
            new InventoryItem(58495, "Chrysler Sebring Touring", "Keyless/Trac Cont", 22500, 16695),
            new InventoryItem(58155, "GM G2500 Cargo Van", "Extended/Auto/Air", 34000, 27795),
            new InventoryItem(58275, "Dodge Dakota Q.C. SLT", "4x4/Loaded/Alloys", 22500, 27995),
            new InventoryItem(58265, "Chrysler 300 Touring", "Heated Leather", 40500, 26495)
    };

    // dataTableColumn Names
    private static final String stockColumnName = "Stock #";
    private static final String modelColumnName = "Model";
    private static final String descriptionColumnName = "Description";
    private static final String odometerColumnName = "Odometer";
    private static final String priceColumnName = "Price";

    protected String sortColumnName;
    protected boolean ascending;

    public TableBean() {
        sortColumnName = stockColumnName;
        ascending = true;
        sort();
    }

    public String getStockColumnName() {
        return stockColumnName;
    }

    public String getModelColumnName() {
        return modelColumnName;
    }

    public String getDescriptionColumnName() {
        return descriptionColumnName;
    }

    public String getOdometerColumnName() {
        return odometerColumnName;
    }

    public String getPriceColumnName() {
        return priceColumnName;
    }

    /**
     * Gets the sortColumnName column.
     *
     * @return column to sortColumnName
     */
    public String getSortColumnName() {
        return sortColumnName;
    }

    /**
     * Sets the sortColumnName column
     *
     * @param sortColumnName column to sortColumnName
     */
    public void setSortColumnName(String sortColumnName) {
        this.sortColumnName = sortColumnName;
    }

    /**
     * Is the sortColumnName ascending.
     *
     * @return true if the ascending sortColumnName otherwise false.
     */
    public boolean isAscending() {
        return ascending;
    }

    /**
     * Set sortColumnName type.
     *
     * @param ascending true for ascending sortColumnName, false for desending sortColumnName.
     */
    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    /**
     * Gets the inventoryItem array of car data.
     * @return array of car inventory data.
     */
    public InventoryItem[] getCarInventory() {
        return carInventory;
    }

    public void doSort(javax.faces.event.ActionEvent event) {
        sort();
    }

    /**
     *  Sorts the list of car data.
     */
    protected void sort() {
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                InventoryItem c1 = (InventoryItem) o1;
                InventoryItem c2 = (InventoryItem) o2;
                if (sortColumnName == null) {
                    return 0;
                }
                int ret = 0;
                if (sortColumnName.equals(stockColumnName)) {
                    ret = new Integer(c1.getStock()).compareTo(new Integer(c2.getStock()));
                } else if (sortColumnName.equals(modelColumnName)) {
                    ret =  c1.getModel().compareTo(c2.getModel());
                } else if (sortColumnName.equals(descriptionColumnName)) {
                    ret = c1.getDescription().compareTo(c2.getDescription());
                } else if (sortColumnName.equals(odometerColumnName)) {
                    ret = new Integer(c1.getOdometer()).compareTo(new Integer(c2.getOdometer()));
                } else if (sortColumnName.equals(priceColumnName)) {
                    ret = new Integer(c1.getPrice()).compareTo(new Integer(c2.getPrice()));
                }
                ret *= (ascending ? 1 : -1);
                return ret;
            }
        };
        Arrays.sort(carInventory, comparator);
    }

    /**
     * Inventory Item subclass stores data about a cars inventory data.  Properties
     * such a stock, model, description, odometer and price are stored.
     */
    public class InventoryItem {
        // slock number
        int stock;
        // model or type of inventory
        String model;
        // description of item
        String description;
        // number of miles on odometer
        int odometer;
        // price of car in Canadian dollars
        int price;

        /**
         * Creates a new instance of InventoryItem.
         * @param stock stock number.
         * @param model model or type of inventory.
         * @param description description of item.
         * @param odometer number of miles on odometer.
         * @param price price of care in Canadian dollars.
         */
        public InventoryItem(int stock, String model, String description, int odometer, int price) {
            this.stock = stock;
            this.model = model;
            this.description = description;
            this.odometer = odometer;
            this.price = price;
        }

        /**
         * Gets the stock number of this iventory item.
         * @return stock number.
         */
        public int getStock() {
            return stock;
        }

        /**
         * Gets the model number of this iventory item.
         * @return model number.
         */
        public String getModel() {
            return model;
        }

        /**
         * Gets the description of the this iventory item.
         * @return description
         */
        public String getDescription() {
            return description;
        }

        /**
         * Gets the odometer reading from this iventory item.
         * @return  odometer reading.
         */
        public int getOdometer() {
            return odometer;
        }

        /**
         * Gets the price of this item in Canadian Dollars.
         * @return price.
         */
        public int getPrice() {
            return price;
        }
    }
}
