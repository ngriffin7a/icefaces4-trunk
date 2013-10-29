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

/**
 * The highlight effect will change the background color of an HTML element and
 * then transition the color back its original state
 */
public class Highlight extends Effect {
    private String startColor;

    /**
     * Default is #ffff99
     */
    public Highlight() {
        setStartColor("#ffff99");
    }

    /**
     * Set the highlight color
     *
     * @param s The RGB color to highlight to. Example: #ff00ff
     */
    public Highlight(String s) {
        setStartColor(s);
    }

    /**
     * Get the starting (highlight) color
     *
     * @return the highlight color
     */
    public String getStartColor() {
        return startColor;
    }

    /**
     * Set the starting (highlight) color
     *
     * @param startColor
     */
    public void setStartColor(String startColor) {
        this.startColor = startColor;
        ea.add("startcolor", startColor);
    }

    /**
     * The Javascript function name
     *
     * @return
     */
    public String getFunctionName() {

        return "new Ice.Scriptaculous.Effect.Highlight";
    }

    public int hashCode() {
        int value = 0;
        char[] ca = startColor.toCharArray();
        for (int i = 1; i < ca.length; i++) {
            value += (int) ca[i] + i;
        }
        return EffectHashCode.HIGHLIGHT * value;
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof Highlight)) {
            return false;
        }
        Highlight effect = (Highlight) obj;
        if (!CoreUtils.objectsEqual(startColor, effect.startColor)) {
            return false;
        }
        return true;
    }
}
