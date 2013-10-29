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

package org.icefaces.application.showcase.view.bean.examples.component.effects;

import org.icefaces.application.showcase.util.FacesUtils;
import org.icefaces.application.showcase.view.bean.BeanNames;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

/**
 * <p>The effects controller is responsible for firing finding and firing
 * an effect associated with an commandLink component.  This bean is
 * in application scope to try and reduce the amount of object creation for
 * the application demos in general</p>
 *
 * @sinse 1.7
 */

@ManagedBean(eager=true)
@ApplicationScoped
public class EffectsController implements Serializable {

    /**
     * <p>Gets an instance of the BeanNames.EFFECTS_MODEL that lives in request
     * scope and sets the current effect based on a request param.  The
     * request param is named "effectKey".  The request param is a valid key
     * to an effect that exists in Map. The respective effect is then set
     * as the current effect in the session model bean. </p>
     *
     * @param event JSF action event.
     */
    public void changeEffectAction(ActionEvent event) {

        // get id of effect action, via param
        String effectKey = FacesUtils.getRequestParameter("effectKey");

        // get the callers session effects data model
        EffectsModel effectsModel =
                (EffectsModel) FacesUtils.getManagedBean(
                        BeanNames.EFFECTS_MODEL);

        // do a look up for the effect
        EffectsModel.EffectWrapper effectWrapper = (EffectsModel.EffectWrapper)
                effectsModel.getEffects().get(effectKey);

        // if found we reset the effect to fire on the soon to occure
        // response. 
        if (effectWrapper != null) {
            effectWrapper.getEffect().setFired(false);
            effectsModel.setCurrentEffecWrapper(effectWrapper);
        }

    }
}
