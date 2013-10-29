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
 * The appear effect will transition an HTML element from one opacity  to
 * another. By default if will transition from invisible to 100% opacity.
 */
public class Appear extends Effect {

    private float from = 0.0f;
    private float to = 1.0f;

    /**
     * Default. Transition from invisible to 100%
     */
    public Appear() {
        ea.add("from", from);
        ea.add("to", to);
    }

    /**
     * Transition between two opacities. Max value i 1.0 min value 0.0. 1.0 =
     * 100% 0.0 = 0%
     *
     * @param from
     * @param to
     */
    public Appear(float from, float to) {
        setFrom(from);
        setTo(to);
    }

    /**
     * Get Start opacity
     *
     * @return
     */
    public float getFrom() {
        return from;
    }

    /**
     * Set start opacity
     *
     * @param from
     */
    public void setFrom(float from) {
        this.from = from;
        ea.add("from", from);
    }

    /**
     * Get end opacity
     *
     * @return
     */
    public float getTo() {
        return to;
    }

    /**
     * Set end opacity
     *
     * @param to
     */
    public void setTo(float to) {
        this.to = to;
        ea.add("to", to);
    }

    /**
     * The javascript function call.
     *
     * @return
     */
    public String getFunctionName() {
        return "Ice.Scriptaculous.Effect.Appear";
    }

    public int hashCode() {
        int from = (int) (this.from * 100);
        int to = (int) (this.to * 100);
        return EffectHashCode.APPEAR * from * to;
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof Appear)) {
            return false;
        }
        Appear effect = (Appear) obj;
        if (from != effect.from) {
            return false;
        }
        if (to != effect.to) {
            return false;
        }
        return true;
    }
}
