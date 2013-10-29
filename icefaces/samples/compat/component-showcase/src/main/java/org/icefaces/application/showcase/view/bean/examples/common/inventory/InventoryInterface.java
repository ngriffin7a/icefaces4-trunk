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

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Simple interface to describe an inventory model. Not really need for this
 * example but it fun to play with generics.
 *
 * @since 1.7
 */
public interface InventoryInterface extends Serializable {

    /**
     * Gets a collection of items that makes up the inventory.
     *
     * @return collection of items contained in the inventory
     */
    public ArrayList getInventory();

    /**
     * Gets the total price of all the items that are contained in this
     * iventory.
     *
     * @return total price of all inventory items.
     */
    public double getInventoryPriceTotal();

    /**
     * Add the specified item to the inventory.
     *
     * @param inventory iventory to add to this instance
     */
    public void addInventoryItem(InventoryItem inventory);

    /**
     * Remove an item from this instance of inventory.
     *
     * @param inventory        inventory item to add
     * @param removeItemOnZero if true, item will be removed from the inventory
     *                         when quantity reach zero otherwise the item is left in the invenotry with
     *                         a quantity of zero.
     */
    public void removeInventoryItem(InventoryItem inventory, boolean removeItemOnZero);
}
