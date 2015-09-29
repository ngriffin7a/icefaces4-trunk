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

package org.icefaces.samples.showcase.example.ace.combobox;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.samples.showcase.util.PositionBean;

@ManagedBean(name= ComboBoxLabelBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ComboBoxLabelBean implements Serializable
{
    public static final String BEAN_NAME = "comboBoxLabelBean";
	public String getBeanName() { return BEAN_NAME; }

    private String selectedText;    
    private String labelText = "Select province:";
    private String labelPosition = "left";

    public String getSelectedText() {
        return selectedText;
    }
    
    public String getLabelText() {
        return labelText;
    }
    
    public String getLabelPosition() {
        return labelPosition;
    }
    
    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }
    
    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }
    
    public void setLabelPosition(String labelPosition) {
        this.labelPosition = labelPosition;
    }
    
    public void positionChanged(ValueChangeEvent event) {
        // Reset our date if the user selected inField, so that we can see the label properly
        if (PositionBean.POS_INFIELD.equals(event.getNewValue().toString())) {
            setSelectedText(null);
        }
    }
}
