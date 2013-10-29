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
 * Pulsate or flash an HTML element
 */
public class Pulsate extends Effect {
    private float duration;

    /**
     * Pulsate for 5 seconds
     */
    public Pulsate() {
        setDuration(5.0f);
    }

    /**
     * Pulsate for a givin duration (In seconds)
     *
     * @param duration
     */
    public Pulsate(float duration) {
        setDuration(duration);
    }

    /**
     * Get the duration (Seconds)
     *
     * @return
     */
    public float getDuration() {
        return duration;
    }

    /**
     * Set the duration (Seconds)
     *
     * @param duration
     */
    public void setDuration(float duration) {
        this.duration = duration;
        ea.add("duration", duration);
    }


    /**
     * Get the Javascript function name
     *
     * @return
     */
    public String getFunctionName() {
        return "Ice.Scriptaculous.Effect.Pulsate";
    }

    public int hashCode() {
        return EffectHashCode.PULSTATE * ((int) duration * 100);
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof Pulsate)) {
            return false;
        }
        Pulsate effect = (Pulsate) obj;
        if (duration != effect.duration) {
            return false;
        }
        return true;
    }
}
