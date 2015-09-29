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

package org.icefaces.samples.showcase.example.ace.tab;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.util.FacesUtils;

@ManagedBean(name = TabServerSideBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TabServerSideBean implements Serializable {
    public static final String BEAN_NAME = "tabServerSide";
	public String getBeanName() { return BEAN_NAME; }

    private boolean fastTabs = true; // Add delay (large image, backend wait etc.) to tab loading make tabset difference clear

    public String getSlowDownTab() {
        try { Thread.sleep(2000); }
        catch (Exception e) {
            FacesUtils.addErrorMessage("Server-side tab waiting could not finish.");
        }
        return "";
    }

    public void setSlowDownTab(String slowDownTab) {}

    public boolean isFastTabs() {
        return fastTabs;
    }

    public void setFastTabs(boolean fastTabs) {
        this.fastTabs = fastTabs;
    }
}
