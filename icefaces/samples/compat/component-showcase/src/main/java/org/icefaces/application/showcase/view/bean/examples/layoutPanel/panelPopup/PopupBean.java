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

package org.icefaces.application.showcase.view.bean.examples.layoutPanel.panelPopup;

import org.icefaces.application.showcase.util.MessageBundleLoader;


import javax.faces.bean.ManagedBean;

import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

/**
 * <p>The PopupBean class is the backing bean that manages the Popup Panel
 * state.</p>
 * <p>This includes the modal and draggable user configurable message, as well
 * as the rendered and visibility state.</p>
 */
@ManagedBean(name = "popup")
@ViewScoped
public class PopupBean implements Serializable {
    // user entered messages for both dialogs
    private String draggableMessage = MessageBundleLoader.getMessage("page.panelPopup.defaultDraggableMessage");
    private String modalMessage = MessageBundleLoader.getMessage("page.panelPopup.defaultModalMessage");
    // render flags for both dialogs
    private boolean draggableRendered = false;
    private boolean modalRendered = false;
    // if we should use the auto centre attribute on the draggable dialog
    private boolean autoCentre = false;

    
    
    public String getDraggableMessage() {
        return draggableMessage;
    }

    public void setDraggableMessage(String draggableMessage) {
        this.draggableMessage = draggableMessage;
    }

    public String getModalMessage() {
        return modalMessage;
    }

    public void setModalMessage(String modalMessage) {
        this.modalMessage = modalMessage;
    }

    public boolean isDraggableRendered() {
        return draggableRendered;
    }

    public void setDraggableRendered(boolean draggableRendered) {
        this.draggableRendered = draggableRendered;
    }

    public boolean getModalRendered() {
        return modalRendered;
    }

    public void setModalRendered(boolean modalRendered) {
        this.modalRendered = modalRendered;
    }
    
    public boolean getAutoCentre() {
        return autoCentre;
    }

    public void setAutoCentre(boolean autoCentre) {
        this.autoCentre = autoCentre;
    }

    public String getDetermineDraggableButtonText() {
        return MessageBundleLoader.getMessage("page.panelPopup.show."
                + draggableRendered);
    }

    public String getDetermineModalButtonText() {
        return MessageBundleLoader.getMessage("page.panelPopup.show."
                + modalRendered);
    }

    public void toggleDraggable(ActionEvent event) {
        draggableRendered = !draggableRendered;
    }

    public void toggleTest(){
    	this.modalRendered= !this.modalRendered;
    }
    
    public void toggleModal(ActionEvent event) {
        modalRendered = !modalRendered;
    }
}
