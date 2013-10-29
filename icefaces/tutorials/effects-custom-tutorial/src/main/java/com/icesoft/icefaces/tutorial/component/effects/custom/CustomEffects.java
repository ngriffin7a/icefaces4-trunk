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

/*
 * CustomEffects.java
 *
 * Created on July 17, 2007, 1:32 PM
 *
 */

package com.icesoft.icefaces.tutorial.component.effects.custom;

import java.io.Serializable;
import com.icesoft.faces.context.effects.BlindDown;
import com.icesoft.faces.context.effects.Effect;


public class CustomEffects implements Serializable {
    //effect used to expand the panelGroup
    private Effect panelEffect;
    
    /** Creates a new instance of CustomEffects */
    public CustomEffects() {
    }

    public Effect getPanelEffect() {
        return panelEffect;
    }

    /*
     *fires the blindDown effect
     *@return null
     */
    public String fireEffect(){
        panelEffect = new BlindDown();
        return null;
    }
}
