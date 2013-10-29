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

package org.icefaces.application.showcase.view.bean.examples.component.menuPopup;

import com.icesoft.faces.component.ContextActionEvent;
import com.icesoft.faces.component.ext.HtmlPanelGroup;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;
import com.icesoft.faces.context.effects.Pulsate;
import com.icesoft.faces.context.effects.Shake;
import org.icefaces.application.showcase.util.FacesUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

/**
 * <p>The MenuPopup class purpose is to execute an action event fired from
 * a MenuPopup components menuItem.  This class takes advantage of the
 * ContextActionEvent class that is fired on a menuItem event. The
 * ContextActionEvent allows the developer to find out the target component
 * that made the reference to a MenuPop.</p>
 * <p/>
 * <p>In this example the ContextActionEvent is used to apply an effect to target
 * panelGroup that the MenuPopup was called from.</p>
 *
 * @since 1.7
 */
@ManagedBean(name = "menuPopupBean")
@ViewScoped
public class MenuPopupBean implements Serializable {
    /**
     * Shake event type
     */
    public static final String SHAKE_EFFECT = "shake";
    /**
     * Pulsate event type
     */
    public static final String PULSATE_EFFECT = "pulsate";
    /**
     * Highlight effect type
     */
    public static final String HIGHLIGHT_EFFECT = "highlight";

    /**
     * Executes a menu effect specified by the request param "effectType".
     *
     * @param event jsf action event.
     */
    public void executeMenuEffect(ActionEvent event) {
        // get effect type from the request param effectType
        String effectType = FacesUtils.getRequestParameter("effectType");

        if (event instanceof ContextActionEvent) {
            // ContextActionEvent getTarget allows us to find out which
            // component initiated the menupopup action.  
            ContextActionEvent se = (ContextActionEvent) event;
            HtmlPanelGroup targetGroup = (HtmlPanelGroup) se.getTarget();
            targetGroup.setEffect(effectFactory(effectType));
        }

    }

    /**
     * Utility method for getting one of three types of effects based on the key
     * words,  SHAKE_EFFECT, PULSATE_EFFECT, HIGHLIGHT_EFFECT.
     *
     * @param effects name of effect to create. Should be one of the following:
     *                SHAKE_EFFECT, PULSATE_EFFECT, HIGHLIGHT_EFFECT
     * @return valid effect as described by the paramater, if no match is found
     *         the Highlight Effect is returned.
     */
    private static Effect effectFactory(String effects) {
        if (SHAKE_EFFECT.equals(effects)) {
            return new Shake();
        } else if (PULSATE_EFFECT.equals(effects)) {
            Pulsate pulsate = new Pulsate();
            pulsate.setDuration(0.75f);
            return pulsate;
        } else {
            return new Highlight("#fda505");
        }
    }
}
