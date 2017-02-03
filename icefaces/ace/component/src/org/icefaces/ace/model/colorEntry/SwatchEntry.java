/*
 * Copyright 2004-2016 ICEsoft Technologies Canada Corp.
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

package org.icefaces.ace.model.colorEntry;

import java.io.Serializable;

public class SwatchEntry implements Serializable{

    /**
     * to use swatches/palette support, a list is made of these objects which contain a color name as well
     * as an int value for RGB for example 'red': {r:1, g:0, b:0} could be one such SwatchEntry entry
     * for a swatch.
     */
    private String colorName;

    private double rpercent;
    private double bpercent;
    private double gpercent;

    public SwatchEntry(String colorName, double red, double green, double blue){
        if (colorName==null || colorName.length() < 1){
              throw new IllegalArgumentException(" ColorName must not be null or empty");
        }
        this.colorName = colorName;
        if (red >=0 && red <= 255 && green >=0 && green <=255 && blue >=0 && blue <=255){
            this.rpercent= red;
            this.bpercent = blue;
            this.gpercent = green;
        }   else throw new IllegalArgumentException("Bad value in rgb input.  Values must be int between 0 and 255") ;
    }
    public String getWrittenEntry(){
        return  "{ name: '"+this.colorName+"', r: "+this.rpercent+", g: "+this.gpercent+", b: "+this.bpercent+"}";
    }



    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }


    public double getRpercent() {
        return rpercent;
    }

    public void setRpercent(double rpercent) {
        this.rpercent = rpercent;
    }

    public double getBpercent() {
        return bpercent;
    }

    public void setBpercent(double bpercent) {
        this.bpercent = bpercent;
    }

    public double getGpercent() {
        return gpercent;
    }

    public void setGpercent(double gpercent) {
        this.gpercent = gpercent;
    }
}
