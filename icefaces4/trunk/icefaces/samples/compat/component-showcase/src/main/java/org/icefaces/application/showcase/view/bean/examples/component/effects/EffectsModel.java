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

package org.icefaces.application.showcase.view.bean.examples.component.effects;

import com.icesoft.faces.context.effects.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.icefaces.application.showcase.view.bean.BaseBean;
import org.icefaces.application.showcase.util.MessageBundleLoader;

/**
 * <p>The EffectsModel bean is stores a Map of Effect objects.  This Map
 * is used to display the list of available effects to the user as well as
 * stores effect currently selected by the user.</p>
 *
 * @since 1.7
 */
@ManagedBean
@ViewScoped
public class EffectsModel extends BaseBean {

    // current effect that consists of a label and the actual Effect.
    private EffectWrapper currentEffectWrapper;

    // map of possible effects.
    private HashMap effects;
    private ArrayList effectKeys;

    /**
     * Creates a new instance of the effects model.
     */
    public EffectsModel() {
        init();
    }

    /**
     * Initialized all the effects object.  Effects have to be in either
     * Session or Request scope so that only the indended users sees them.
     */
    protected void init() {
        // build a list of our know effects
        effects = new HashMap(11);

        // fade appear effect combo
        EffectQueue fadeAppear = new EffectQueue("effectFadeAppear");
        fadeAppear.add(new Fade());
        fadeAppear.add(new Appear());
        effects.put("effectFadeAppear",
                new EffectWrapper(
                        "page.effect.appearFade.title",
                        fadeAppear));

        effects.put("effectHighlight",
                new EffectWrapper(
                        "page.effect.highlight.title",
                        new Highlight("#fda505")));
        // pulsate
        Pulsate pulsate = new Pulsate();
        pulsate.setDuration(1.0f);
        effects.put("effectPulsate",
                new EffectWrapper(
                        "page.effect.pulsate.title",
                        pulsate));

        // effect move and move back.
//        EffectQueue move = new EffectQueue("effectMove");
//        move.add(new Move(25, 25, "relative"));
//        move.add(new Move(-25, -25, "relative"));
//        effects.put("effectMove",
//                new EffectWrapper(
//                        "page.effect.move.title",
//                        move));

        // scale effect
//        EffectQueue scale = new EffectQueue("effectScale");
//        scale.add(new Scale(50));
//        scale.add(new Scale(200));
//        effects.put("effectScale",
//                new EffectWrapper(
//                        "page.effect.scale.title",
//                        scale));

        // puff effect
        EffectQueue puff = new EffectQueue("effectPuff");
        puff.add(new Puff());
        puff.add(new Appear());
        effects.put("effectPuff",
                new EffectWrapper(
                        "page.effect.puff.title",
                        puff));

        // Blind effect
        EffectQueue blind = new EffectQueue("effectBlind");
        blind.add(new BlindUp());
        blind.add(new BlindDown());
        effects.put("effectBlind",
                new EffectWrapper(
                        "page.effect.blind.title",
                        blind));

        // drop out effect
        EffectQueue dropOut = new EffectQueue("effectDropOut");
        dropOut.add(new DropOut());
        dropOut.add(new Appear());
        effects.put("effectDropOut",
                new EffectWrapper(
                        "page.effect.dropout.title",
                        dropOut));

        // shake effect
        effects.put("effectShake",
                new EffectWrapper(
                        "page.effect.shake.title",
                        new Shake()));

        // Slide effect
        EffectQueue slide = new EffectQueue("effectSlide");
        slide.add(new SlideUp());
        slide.add(new SlideDown());
        effects.put("effectSlide",
                new EffectWrapper(
                        "page.effect.slide.title",
                        slide));

        // Shrink effect
        EffectQueue shrink = new EffectQueue("effectShrink");
        shrink.add(new Shrink());
        shrink.add(new Appear());
        effects.put("effectShrink",
                new EffectWrapper(
                        "page.effect.shrink.title",
                        shrink));
        effectKeys = new ArrayList(effects.keySet());
    }

    public EffectWrapper getCurrentEffectWrapper() {
        return currentEffectWrapper;
    }

    public void setCurrentEffecWrapper(EffectWrapper currentEffectWrapper) {
        this.currentEffectWrapper = currentEffectWrapper;
    }

    public HashMap getEffects() {
        return effects;
    }

    /**
     * Gets a list of EffectWrapper objects.
     * @return collection of EffectWrapper
     */
    public Collection getEffectKeys() {
        return effectKeys;
    }

    /**
     * Wrapper class to make it easy to display the different Effects with a
     * discriptive label. 
     */
    public class EffectWrapper implements Serializable{
        private Effect effect;
        private String title;

        public EffectWrapper(String title, Effect effect) {
            this.effect = effect;
            this.effect.setFired(true);
            this.title = title;
        }

        public Effect getEffect() {
            return effect;
        }

        public void setEffect(Effect effect) {
            this.effect = effect;
        }

        public String getTitle() {
            return MessageBundleLoader.getMessage(title);           
        }
    }
}
