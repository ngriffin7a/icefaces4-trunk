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

package org.icefaces.application.showcase.view.bean.examples.common.inventory;

import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;

import java.io.Serializable;

/**
 * <p>The InventoryItem is a descriptive class that respresent a sudo inventory
 * item. It is made up of a basic description as well the number of avialable
 * units (quantity).</p>
 * <p/>
 * <p>InventoryItem are collected in the Iventory class.</p>
 *
 * @see Inventory
 * @since 1.7
 */
public class InventoryItem implements Serializable {
    // unique inventory id
    private int id;
    // name of inventory item
    private String name;
    // picture name used to define inventory item
    private String pictureName;
    // price of one unit of this inventory
    private double price;
    // stock or quantity of iventory items available
    private int quantity;
    // effect to highlight when an inventory quantity has changed.
    private Effect changeQuantityEffect;

    /**
     * <p>Constructs a new Inventory item by copying the id, name, pictureName
     * and price from the item attribute.  The quantity is set to zero for
     * the newly create InventoryItem object.</p>
     *
     * @param item iventory object who's values will be used to create the new
     *             instance.
     */
    public InventoryItem(InventoryItem item) {
        this(item.getId(), item.getName(), item.getPictureName(),
                item.getPrice(), 0);
    }

    /**
     * <p>Constructs a new Inventory item by copying the id, name, pictureName
     * and price from the item attribute.  The quantity is set to zero for
     * the newly create InventoryItem object.</p>
     *
     * @param id          unique for this instance
     * @param name        name of the inventory item for display purposes
     * @param pictureName picture name of the inventory item
     * @param price       price of each individual item.
     * @param quantity    number of units for this inventory item.
     */
    public InventoryItem(int id, String name, String pictureName,
                         double price, int quantity) {
        this.id = id;
        this.name = name;
        this.pictureName = pictureName;
        this.price = price;
        this.quantity = quantity;
        changeQuantityEffect = new Highlight("#fda505");
        changeQuantityEffect.setFired(true);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPictureName() {
        return pictureName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Increments the quqntity count and resets the Effect to fire on the
     * next render pass.
     */
    public void incrementQuantity() {
        quantity++;
        // show an effect indicating a change. 
        changeQuantityEffect.setFired(false);
    }

    /**
     * Decrements the quqntity count and resets the Effect to fire on the
     * next render pass.
     */
    public void decrementQuantity() {
        quantity--;
        changeQuantityEffect.setFired(false);
    }

    public Effect getChangeQuantityEffect() {
        return changeQuantityEffect;
    }
}
