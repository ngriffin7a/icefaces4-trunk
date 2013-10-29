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

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.ValueHolder;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanelToolTipBean {
    //source component for which the tooltip will be rendered/unrendered
    UIComponent tooltipSrc;

    //current state of the tooltip
    String state = "hide";

    Map provinces = new HashMap();

    List cityList = new ArrayList();

    public PanelToolTipBean() {
        List alberta = new ArrayList();
        alberta.add("Calgary");
        alberta.add("Edmonton");
        alberta.add("Red Deer");
        alberta.add("Lethbridge");
        alberta.add("Medicine Hat");
        alberta.add("Airdrie");
        provinces.put("Alberta", alberta);

        List ontario = new ArrayList();
        ontario.add("Toronto");
        ontario.add("Mississauga");
        ontario.add("Hamilton");
        ontario.add("Brampton");
        ontario.add("London");
        ontario.add("Windsor");
        provinces.put("Ontario", ontario);

        List novascotia = new ArrayList();
        novascotia.add("Halifax");
        novascotia.add("Kings County");
        novascotia.add("Colchester County");
        novascotia.add("Lunenburg County");
        provinces.put("Nova Scotia", novascotia);

        List saskatchewan = new ArrayList();
        saskatchewan.add("Saskatoon");
        saskatchewan.add("Regina");
        saskatchewan.add("Prince Albert");
        saskatchewan.add("Moose Jaw");
        provinces.put("Saskatchewan", saskatchewan);

    }

    public UIComponent getTooltipSrc() {
        return tooltipSrc;
    }

    public void setTooltipSrc(UIComponent tooltipSrc) {
        this.tooltipSrc = tooltipSrc;
    }

    public void stateListener(ValueChangeEvent event) {
        if (tooltipSrc != null) {
            UIComponent component = (UIComponent) tooltipSrc.getChildren().get(0);
            if (component instanceof UIOutput) {
                cityList = (List) provinces.get(((ValueHolder) component).getValue());
            }
        }
    }

    public Map getProvinces() {
        return provinces;
    }

    public void setProvinces(Map provinces) {
        this.provinces = provinces;
    }

    public List getCityList() {
        return cityList;
    }

    public void setCityList(List cityList) {
        this.cityList = cityList;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void hideTooltip(ActionEvent event) {
        this.state = "hide";
    }

}