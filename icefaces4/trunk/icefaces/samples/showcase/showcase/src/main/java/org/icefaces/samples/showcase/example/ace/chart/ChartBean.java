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
 
package org.icefaces.samples.showcase.example.ace.chart;

import java.io.Serializable;
import java.util.Random;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.ace.component.chart.Axis;
import org.icefaces.ace.component.chart.AxisType;
import org.icefaces.ace.model.chart.CartesianSeries;

@ManagedBean(name= ChartBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartBean implements Serializable {
    public static final String BEAN_NAME = "chartBean";
	public String getBeanName() { return BEAN_NAME; }
    
    private Random randomizer = new Random(System.currentTimeMillis());
    
    private CartesianSeries barData = new CartesianSeries() {{
        setType(CartesianType.BAR);
        add("HDTV Receiver", randomizer.nextInt(20));
        add("Cup Holder Pinion Bob", randomizer.nextInt(20));
        add("Generic Fog Lamp", randomizer.nextInt(20));
        add("8 Track Control Module", randomizer.nextInt(20));
        add("Sludge Pump Fourier Modulator", randomizer.nextInt(20));
        add("Transceiver Spice Rack", randomizer.nextInt(20));
        add("Hair Spray Danger Indicator", randomizer.nextInt(20));
        setLabel("Product / Sales");
    }};
    
    private Axis barDemoDefaultAxis = new Axis() {{
        setTickAngle(-30);
    }};

    private Axis barDemoXAxis = new Axis() {{
        setType(AxisType.CATEGORY);
    }};

    private Axis barDemoYAxis = new Axis() {{
            setAutoscale(true);
            setTickInterval("5");
            setLabel("USD Millions");
}};

    private Axis barDemoXTwoAxis = new Axis() {{
        setTicks(new String[] {"Nickle", "Aluminum", "Xenon", "Silver", "Sulfur", "Silicon", "Vanadium"});
        setType(AxisType.CATEGORY);
    }};

    public CartesianSeries getBarData() {
        return barData;
    }

    public void setBarData(CartesianSeries barData) {
        this.barData = barData;
    }

    public Axis getBarDemoXAxis() {
        return barDemoXAxis;
    }

    public void setBarDemoXAxis(Axis barDemoXAxis) {
        this.barDemoXAxis = barDemoXAxis;
    }

    public Axis getBarDemoDefaultAxis() {
        return barDemoDefaultAxis;
    }

    public void setBarDemoDefaultAxis(Axis barDemoDefaultAxis) {
        this.barDemoDefaultAxis = barDemoDefaultAxis;
    }

    public Axis getBarDemoXTwoAxis() {
        return barDemoXTwoAxis;
    }

    public void setBarDemoXTwoAxis(Axis barDemoXTwoAxis) {
        this.barDemoXTwoAxis = barDemoXTwoAxis;
    }

    public Axis getBarDemoYAxis() {
        return barDemoYAxis;
    }

    public void setBarDemoYAxis(Axis barDemoYAxis) {
        this.barDemoYAxis = barDemoYAxis;
    }
}
