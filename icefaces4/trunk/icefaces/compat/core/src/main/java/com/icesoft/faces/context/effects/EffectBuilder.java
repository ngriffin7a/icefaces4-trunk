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
 * Used to translate simple strings into effects
 */
public class EffectBuilder {

    public static Effect build(String name) {
        if ("appear".equalsIgnoreCase(name)) {
            return new Appear();
        }
        if("move".equalsIgnoreCase(name)){
            return new Move();
        }

        if ("fade".equalsIgnoreCase(name)) {
            System.err.println("Returning [" + Fade.class.getName() + "]");
            return new Fade();
        }
        if ("highlight".equalsIgnoreCase(name)) {
            return new Highlight();
        }
        if ("pulsate".equalsIgnoreCase(name)) {
            return new Pulsate();
        }
        if("scale".equalsIgnoreCase(name)){
            return new Scale(.5f);
        }
        if("puff".equalsIgnoreCase(name)){
            return new Puff();
        }
        if("blindup".equalsIgnoreCase(name)){
            return new BlindUp();
        }
        if("blinddown".equalsIgnoreCase(name)){
            return new BlindDown();
        }
        if("swtichoff".equalsIgnoreCase(name)){
            return new SwitchOff();
        }
        if("dropout".equalsIgnoreCase(name)){
            return new DropOut();
        }
        if("shake".equalsIgnoreCase(name)) {
            return new Shake();
        }
        if("slidedown".equalsIgnoreCase(name)){
            return new SlideDown();
        }
        if("slideup".equalsIgnoreCase(name)){
            return new SlideUp();
        }
        if("squish".equalsIgnoreCase(name)){
            return new Squish();
        }
        if("grow".equalsIgnoreCase(name)){
            return new Grow();
        }
        if("shrink".equalsIgnoreCase(name)){
            return new Shrink();
        }
        if("fold".equalsIgnoreCase(name)){
            return new Fold();
        }
        if("opacity".equalsIgnoreCase(name)){
            return new Opacity();
        }
        return null;
    }
}
