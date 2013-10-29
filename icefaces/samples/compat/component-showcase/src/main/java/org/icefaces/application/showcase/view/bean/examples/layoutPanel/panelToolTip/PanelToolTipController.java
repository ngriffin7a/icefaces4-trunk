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

package org.icefaces.application.showcase.view.bean.examples.layoutPanel.panelToolTip;

import com.icesoft.faces.component.DisplayEvent;
import org.icefaces.application.showcase.util.MessageBundleLoader;
import org.icefaces.application.showcase.util.RandomNumberGenerator;
import org.icefaces.application.showcase.view.bean.examples.common.inventory.Inventory;
import org.icefaces.application.showcase.view.bean.examples.common.inventory.InventoryItem;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.ValueHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.Serializable;

/**
 * @since 1.7
 */
@ManagedBean(name = "tooltipController")
@ViewScoped
public class PanelToolTipController implements Serializable {


    /**
     * Main data models for this example.
     */
    // Store Iventory maintains the number of stock available for a given
    // stock item.
    private Inventory storeInventory;

    private PanelToolTipModel panelToolTipModel;

    private ArrayList cityList = new ArrayList();


    /**
     * Creates a new instace and initializes the store and chopping cart
     * Iventory models.
     */
    public PanelToolTipController() {
        panelToolTipModel = new PanelToolTipModel();
        init();
    }

    /**
     * Initializes storeInventory with four default items that have radomly
     * generated price and quantity values.
     */
    private void init() {
        RandomNumberGenerator randomNumberGenerator =
                RandomNumberGenerator.getInstance();

        // we need to create four inventory items which has random values
        // for price and inventory count.
        storeInventory = new Inventory();
        ArrayList store = storeInventory.getInventory();
        store.add(new InventoryItem(1, "Laptop", "laptop",
                randomNumberGenerator.getRandomDouble(699, 3200),
                (int) randomNumberGenerator.getRandomDouble(15, 20)));
        store.add(new InventoryItem(2, "Monitor", "monitor",
                randomNumberGenerator.getRandomDouble(299, 799),
                (int) randomNumberGenerator.getRandomDouble(5, 10)));
        store.add(new InventoryItem(4, "Desktop", "desktop",
                randomNumberGenerator.getRandomDouble(299, 499),
                (int) randomNumberGenerator.getRandomDouble(25, 50)));
        store.add(new InventoryItem(3, "PDA", "pda",
                randomNumberGenerator.getRandomDouble(60, 300),
                (int) randomNumberGenerator.getRandomDouble(5, 20)));
    }

    /**
     * @param event
     */
    public void displayListener(DisplayEvent event) {
        // updated the city list for the city that activated the tooltip
        if (event.isVisible()) {
            String province = event.getContextValue().toString();
            final int len = 5;
            ArrayList cities = new ArrayList(len);
            for(int i = 1; i <= len; i++) {
                cities.add(MessageBundleLoader.getMessage(province + ".city"+i+".label"));
            }
            cityList = cities;
        }
    }

    public Inventory getStoreInventory() {
        return storeInventory;
    }

    public PanelToolTipModel getPanelToolTipModel() {
        return panelToolTipModel;
    }

    public ArrayList getCityList() {
        return cityList;
    }
}
