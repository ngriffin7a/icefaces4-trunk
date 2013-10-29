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
 * script.aculo.us Shrink effect
 * Shrink an element to nothing
 */
public class Shrink extends Effect {
    private String direction = "center";

    public Shrink() {
        ea.add("direction", direction);
    }

    public String getDirection() {
        return direction;
    }

    /**
     * Grow to current size center, top-left, top-right, bottom-left,
     * bottom-right
     *
     * @param direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
        ea.add("direction", direction);
    }

    public String getFunctionName() {
        return "Ice.Scriptaculous.Effect.Shrink";
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof Shrink)) {
            return false;
        }
        Shrink effect = (Shrink) obj;
        if (!CoreUtils.objectsEqual(direction, effect.direction)) {
            return false;
        }
        return true;
    }
}
