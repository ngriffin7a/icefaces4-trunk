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

/**
 * Set the opacity of an HTML element
 */
public class Opacity extends Effect {
    private float from = 1.0f;
    private float to = 0.0f;
    private float value;

    /**
     * Set the opacity  to a givin value. 0.0 to 1.0
     *
     * @param f
     */
    public Opacity() {
        ea.add("from", from);
        ea.add("to", to);
    }

    /**
     * @param from Starting opacity
     * @param to   end opacity
     */
    public Opacity(float from, float to) {
        setFrom(from);
        setTo(to);
    }

    /**
     * @param from     Starting opacity
     * @param to       end opacity
     * @param duration
     */
    public Opacity(float from, float to, float duration) {
        this(from, to);
        setDelay(duration);
    }

    /**
     * Get the opacity
     *
     * @return
     */
    public float getValue() {
        return value;
    }

    /**
     * Get the Javascript function name
     *
     * @return
     */
    public String getFunctionName() {
        return "new Ice.Scriptaculous.Effect.Opacity";
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof Opacity)) {
            return false;
        }
        Opacity effect = (Opacity) obj;
        if (from != effect.from) {
            return false;
        }
        if (to != effect.to) {
            return false;
        }
        if (value != effect.value) {
            return false;
        }
        return true;
    }
}
