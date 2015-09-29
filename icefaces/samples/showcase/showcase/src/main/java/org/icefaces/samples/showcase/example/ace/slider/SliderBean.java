/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.samples.showcase.example.ace.slider;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name= SliderBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SliderBean implements Serializable {

    public static final String BEAN_NAME = "slider";
	public String getBeanName() { return BEAN_NAME; }
    private String axis;
    private boolean clickableRail;
    private String length;
    private int minValue;
    private int maxValue;
    private int sliderValue;
    private int stepPercent;

    public SliderBean() {
        initialaziInstanceVariables();
        this.sliderValue = 50;
    }
    
    private void initialaziInstanceVariables() {
           this.axis = "x";
           this.clickableRail =false;
           this.length = "100";
           this.minValue = 0;
           this.maxValue = 100;
           this.sliderValue = 50;
           this.stepPercent = 10;
    }

    public String getAxis() {
        return axis;
    }

    public void setAxis(String axis) {
        this.axis = axis;
    }

    public boolean isClickableRail() {
        return clickableRail;
    }

    public void setClickableRail(boolean clickableRail) {
        this.clickableRail = clickableRail;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getSliderValue() {
        return sliderValue;
    }

    public void setSliderValue(int sliderValue) {
        this.sliderValue = sliderValue;
    }

    public int getStepPercent() {
        return stepPercent;
    }

    public void setStepPercent(int stepPercent) {
        this.stepPercent = stepPercent;
    }
}
