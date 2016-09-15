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

package org.icefaces.samples.showcase.example.ace.colorEntry;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name= ColorEntryBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ColorEntryBean implements Serializable
{
    public static final String BEAN_NAME = "colorEntryBean";
	public String getBeanName() { return BEAN_NAME; }

    private String basicValue, colorValue, allowEmptyValue, showButtonsValue, showInputValue, showPaletteOnly;

    public String getBasicValue() {
        return basicValue;
    }

    public void setBasicValue(String basicValue) {
        this.basicValue = basicValue;
    }

    public String getAllowEmptyValue() {
        return allowEmptyValue;
    }

    public void setAllowEmptyValue(String allowEmptyValue) {
        this.allowEmptyValue = allowEmptyValue;
    }

    public String getShowInputValue() {
        return showInputValue;
    }

    public void setShowInputValue(String showInputValue) {
        this.showInputValue = showInputValue;
    }

    public String getShowPaletteOnly() {
        return showPaletteOnly;
    }

    public void setShowPaletteOnly(String showPaletteOnly) {
        this.showPaletteOnly = showPaletteOnly;
    }
}
