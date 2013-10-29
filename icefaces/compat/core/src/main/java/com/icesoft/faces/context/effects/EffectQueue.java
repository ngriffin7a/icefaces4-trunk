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

package com.icesoft.faces.context.effects;

import com.icesoft.faces.util.CoreUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Use to fire a sequence of effects at once
 */
public class EffectQueue extends Effect {

    private List effects = new ArrayList();

    public EffectQueue(String name) {
        setSequence(name);
    }

    public void add(Effect effect) {
        effects.add(effect);
    }

    public String getFunctionName() {
        return null;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        Object[] oa = effects.toArray();
        for (int i = oa.length - 1; i >= 0; i--) {
            Effect fx = (Effect) oa[i];
            int next = i - 1;
            if (next != 0) {
                fx.setQueued(true);
            } else {
                fx.setQueueEnd(true);
            }
            sb.append(fx.toString());
        }
        return sb.toString();
    }

    public List getEffects() {
        return effects;
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof EffectQueue)) {
            return false;
        }
        EffectQueue effect = (EffectQueue) obj;
        if (!CoreUtils.objectsEqual(effects, effect.effects)) {
            return false;
        }
        return true;
    }
}