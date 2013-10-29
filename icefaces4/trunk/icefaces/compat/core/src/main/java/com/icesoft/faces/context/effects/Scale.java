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
 * script.aculo.us scale effect
 * <p/>
 * Grow or shrink an element by a specifed percent. (Default 50%)
 */
public class Scale extends Effect {

    private boolean scaleX = true;
    private boolean scaleY = true;
    private boolean scaleContent = true;
    private boolean scaleFromCenter = false;
    private String scaleMode = "box";
    private float scaleFrom = 100.0f;
    private float scaleTo = 50.0f;

    public Scale(float to) {
        this.scaleTo = to;
        ea.add("scaleX", scaleX);
        ea.add("scaleY", scaleY);
        ea.add("scaleContent", scaleContent);
        ea.add("scaleFromCenter", scaleFromCenter);
        ea.add("scaleMode", scaleMode);
        ea.add("scaleFrom", scaleFrom);
        ea.add("scaleTo", scaleTo);
    }

    public boolean isScaleX() {
        return scaleX;
    }

    public void setScaleX(boolean scaleX) {
        this.scaleX = scaleX;
        ea.add("scaleX", scaleX);
    }

    public boolean isScaleY() {
        return scaleY;
    }

    public void setScaleY(boolean scaleY) {
        this.scaleY = scaleY;
        ea.add("scaleY", scaleY);
    }

    public boolean isScaleContent() {
        return scaleContent;
    }

    public void setScaleContent(boolean scaleContent) {
        this.scaleContent = scaleContent;
        ea.add("scaleContent", scaleContent);
    }

    public boolean isScaleFromCenter() {
        return scaleFromCenter;
    }

    public void setScaleFromCenter(boolean scaleFromCenter) {
        this.scaleFromCenter = scaleFromCenter;
        ea.add("scaleFromCenter", scaleFromCenter);
    }

    public String getScaleMode() {
        return scaleMode;
    }

    public void setScaleMode(String scaleMode) {
        this.scaleMode = scaleMode;
        ea.add("scaleMode", scaleMode);
    }

    public float getScaleFrom() {
        return scaleFrom;
    }

    public void setScaleFrom(float scaleFrom) {
        this.scaleFrom = scaleFrom;
        ea.add("scaleFrom", scaleFrom);
    }

    public float getScaleTo() {
        return scaleTo;
    }

    public void setScaleTo(float scaleTo) {
        this.scaleTo = scaleTo;
        ea.add("scaleTo", scaleTo);
    }

    public String getFunctionName() {
        return "new Ice.Scriptaculous.Effect.Scale";
    }

    public String toString(String var, String lastCall) {
        if (isQueued()) {
            ea.add("queue", "front");
        }
        if (isQueueEnd()) {
            ea.add("queue", "end");
        }
        if (!isTransitory()) {
            ea.add("uploadCSS", "true");
        }
        if (lastCall != null) {
            ea.addFunction("iceFinish", "function(){" + lastCall + "}");
        }
        return "new Effect.Scale(" + var + ", " + scaleTo + ea.toString();
    }

    public String toString() {
        return toString("id", null);
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof Scale)) {
            return false;
        }
        Scale effect = (Scale) obj;
        if (scaleX != effect.scaleX) {
            return false;
        }
        if (scaleY != effect.scaleY) {
            return false;
        }
        if (scaleContent != effect.scaleContent) {
            return false;
        }
        if (scaleFromCenter != effect.scaleFromCenter) {
            return false;
        }
        if (!CoreUtils.objectsEqual(scaleMode, effect.scaleMode)) {
            return false;
        }
        if (scaleFrom != effect.scaleFrom) {
            return false;
        }
        if (scaleTo != effect.scaleTo) {
            return false;
        }
        return true;
    }
}
