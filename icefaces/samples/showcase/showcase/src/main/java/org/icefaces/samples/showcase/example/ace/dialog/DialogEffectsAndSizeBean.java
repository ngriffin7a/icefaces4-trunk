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

package org.icefaces.samples.showcase.example.ace.dialog;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name= DialogEffectsAndSizeBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DialogEffectsAndSizeBean implements Serializable
{
    public static final String BEAN_NAME = "dialogEffectsAndSizeBean";
	public String getBeanName() { return BEAN_NAME; }
    private String showEffect;
    private String hideEffect;
    private int minWidth;
    private int minHeight;
    

    public DialogEffectsAndSizeBean() 
    {
        initializeDefaultBeanValues();
    }

    public String getHideEffect() {
        return hideEffect;
    }

    public void setHideEffect(String hideEffect) {
        this.hideEffect = hideEffect;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public String getShowEffect() {
        return showEffect;
    }

    public void setShowEffect(String showEffect) {
        this.showEffect = showEffect;
    }

    private void initializeDefaultBeanValues() 
    {
        this.showEffect = "blind";
        this.hideEffect = "blind";
        this.minWidth = 200;
        this.minHeight = 200;
    }
}
