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

package com.icesoft.icefaces.tutorial.component.effects.basic;

import java.io.Serializable;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;

/**
 * <p>
 * A basic backing bean for effect component.  </p>
 */
public class BasicEffects implements Serializable {
    //Effect used to highlight the text
    private Highlight effectOutputText = new Highlight("#ffff99");
    //displayed input text
    private String text;

    /* Returns the text effect
    *@return Effect EffectOutputText
    */
    public Effect getEffectOutputText() {
        return effectOutputText;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        
        effectOutputText = new Highlight("#FFCC0B");
        effectOutputText.setFired(false);
    }
}
