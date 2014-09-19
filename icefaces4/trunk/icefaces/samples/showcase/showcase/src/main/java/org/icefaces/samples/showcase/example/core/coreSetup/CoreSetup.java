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

package org.icefaces.samples.showcase.example.core.coreSetup;

import org.icefaces.samples.showcase.view.navigation.NavigationModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

@ManagedBean
@ViewScoped
public class CoreSetup implements Serializable {
    @ManagedProperty(value = "#{navigationModel}")
    private NavigationModel model;
    private boolean invoked = false;

    public CoreSetup() {
    }

    public NavigationModel getModel() {
        return model;
    }

    public void setModel(NavigationModel model) {
        this.model = model;
        //need to set "renderAsExample" property to have the demo's content shown
        model.setRenderAsExample(true);
    }

    public boolean isInvoked() {
        return invoked;
    }
}
