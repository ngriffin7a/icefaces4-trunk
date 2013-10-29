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

package org.icefaces.application.showcase.view.bean.examples.layoutPanel.panelConfirmation;

import org.icefaces.application.showcase.util.MessageBundleLoader;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

@ManagedBean(name = "confirmation")
@ViewScoped
public class ConfirmationPanelBean implements Serializable {
    private String dataIn = MessageBundleLoader.getMessage("page.panelConfirmation.dataIn");
    private String dataOut = MessageBundleLoader.getMessage("page.panelConfirmation.dataOut");
    private boolean withConfirmation = true;

    public void save(ActionEvent event) {
        dataOut = dataIn;
    }

    public void delete(ActionEvent event) {
        dataOut = null;
    }

    public String getDataOut() {
        return dataOut;
    }

    public String getDataIn() {
        return dataIn;
    }

    public void setDataIn(String dataIn) {
        this.dataIn = dataIn;
    }

    public boolean isWithConfirmation() {
        return withConfirmation;
    }

    public void setWithConfirmation(boolean withConfirmation) {
        this.withConfirmation = withConfirmation;
    }
}