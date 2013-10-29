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
 * Move an HTML element to a new position. Moves can be absolute or relative.
 * Relative moves an element from it current position, absolute moves it from
 * its begging position. (Here the HTML initially  rendered it)
 */
public class Move extends Effect {
    private int x;
    private int y;
    private String mode;


    public Move() {
    }

    /**
     * Move an element to a new position
     *
     * @param x    or left location
     * @param y    or top location
     * @param mode can be relative or absolute
     */
    public Move(int x, int y, String mode) {
        setX(x);
        setY(y);
        setMode(mode);
    }

    /**
     * Move an element to a new position. Mode is relative
     *
     * @param x or left location
     * @param y or top location
     */
    public Move(int x, int y) {
        setX(x);
        setY(y);
        setMode("relative");
    }

    /**
     * Get the X or left end position
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * Set the X or left end position
     *
     * @param x
     */
    public void setX(int x) {
        this.x = x;
        ea.add("x", x);
    }

    /**
     * Get the Y or top position
     *
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     * Set the Y or top position
     *
     * @param y
     */
    public void setY(int y) {
        this.y = y;
        ea.add("y", y);
    }

    /**
     * Get the mode of the move (absolute or relative)
     *
     * @return
     */
    public String getMode() {
        return mode;
    }

    /**
     * Set the mode (absolute or realitve)
     *
     * @param mode
     */
    public void setMode(String mode) {
        this.mode = mode;
        ea.add("mode", mode);
    }

    /**
     * The javascript function name
     *
     * @return
     */
    public String getFunctionName() {
        return "new Ice.Scriptaculous.Effect.Move";
    }

    public int hasCode() {
        return EffectHashCode.MOVE * (x * 1) * (y * 2) +
                ("relative".equals(mode) ? 1 : 2);
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof Move)) {
            return false;
        }
        Move effect = (Move) obj;
        if (x != effect.x) {
            return false;
        }
        if (y != effect.y) {
            return false;
        }
        if (!CoreUtils.objectsEqual(mode, effect.mode)) {
            return false;
        }
        return true;
    }
}
