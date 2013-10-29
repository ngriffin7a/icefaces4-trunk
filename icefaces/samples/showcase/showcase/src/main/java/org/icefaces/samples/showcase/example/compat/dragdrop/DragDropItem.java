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

package org.icefaces.samples.showcase.example.compat.dragdrop;

import java.io.Serializable;
import org.icefaces.samples.showcase.dataGenerators.ImageSet;

public class DragDropItem implements Serializable {
    private int id;
    private String name;
    private String image;
    private String type;
    private double price;
    private int quantity;
    
    
    public DragDropItem(ImageSet.ImageInfo image) 
    {
        this.name = image.getDescription();
        this.image = image.getPath();
    }
    
    public DragDropItem(int id, String name, String image, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
    }

    public DragDropItem(int id, String name, String image, double price, String type) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.type = type;
    }
    
    public DragDropItem(DragDropItem clone) {
        this(clone.getId(),
             clone.getName(), clone.getImage(),
             clone.getPrice(), 1);
    }
    
    public DragDropItem(DragDropItem clone, int quantity) {
        this(clone.getId(),
             clone.getName(), clone.getImage(),
             clone.getPrice(), quantity);
    }
    
    public int getId() { return id; }
    public String getName() { return name; }
    public String getImage() { return image; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setImage(String image) { this.image = image; }
    public void setType(String type) { this.type = type; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int reduceQuantity() {
        return reduceQuantity(1);
    }
    
    public int reduceQuantity(int by) {
        quantity -= by;
        
        return quantity;
    }
    
    public int increaseQuantity() {
        return increaseQuantity(1);
    }
    
    public int increaseQuantity(int by) {
        quantity += by;
        
        return quantity;
    }    
    
    public boolean isOutOfStock() {
        return quantity <= 0;
    }
    
    public double getTotalPrice() {
        return price * (double)quantity;
    }
}
