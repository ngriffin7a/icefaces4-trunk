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

package org.icefaces.samples.showcase.example.ace.ajax;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name= AjaxAdvancedBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AjaxAdvancedBean implements Serializable
{
    public static final String BEAN_NAME = "ajaxAdvancedBean";
	public String getBeanName() { return BEAN_NAME; }

	private String event = "slideEnd";
    private int sliderValue = 0;
	private boolean updateCelsius = true;
	private boolean updateFahrenheit = true;
	private boolean updateKelvin = true;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getSliderValue() {
        return sliderValue;
    }

    public void setSliderValue(int sliderValue) {
        this.sliderValue = sliderValue;
    }

    public boolean getUpdateCelsius() {
        return updateCelsius;
    }

    public void setUpdateCelsius(boolean updateCelsius) {
        this.updateCelsius = updateCelsius;
    }

    public boolean getUpdateFahrenheit() {
        return updateFahrenheit;
    }

    public void setUpdateFahrenheit(boolean updateFahrenheit) {
        this.updateFahrenheit = updateFahrenheit;
    }

    public boolean getUpdateKelvin() {
        return updateKelvin;
    }

    public void setUpdateKelvin(boolean updateKelvin) {
        this.updateKelvin = updateKelvin;
    }
}