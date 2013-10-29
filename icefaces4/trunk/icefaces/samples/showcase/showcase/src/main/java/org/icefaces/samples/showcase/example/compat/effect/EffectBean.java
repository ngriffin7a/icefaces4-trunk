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

package org.icefaces.samples.showcase.example.compat.effect;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.icesoft.faces.context.effects.*;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        title = "example.compat.effect.title",
        description = "example.compat.effect.description",
        example = "/resources/examples/compat/effect/effect.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="effect.xhtml",
                    resource = "/resources/examples/compat/"+
                               "effect/effect.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="EffectBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/effect/EffectBean.java")
        }
)
@Menu(
	title = "menu.compat.effect.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.effect.subMenu.main",
                    isDefault = true,
                    exampleBeanName = EffectBean.BEAN_NAME)
})
@ManagedBean(name= EffectBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class EffectBean extends ComponentExampleImpl<EffectBean> implements Serializable {
	
	public static final String BEAN_NAME = "effectBean";
	
	// Constants to track each available effect
	// Integers are used so we can use a "switch" statement later
	private static final int E_BLIND = 1;
	private static final int E_DROPOUT = 5;
	private static final int E_FADE = 10;
	private static final int E_FOLD = 15;
	private static final int E_GROW = 20;
	private static final int E_HIGHLIGHT = 25;
	private static final int E_MOVE = 30;
	private static final int E_OPACITY = 35;
	private static final int E_PUFF = 40;
	private static final int E_PULSATE = 45;
	private static final int E_SHAKE = 50;
	private static final int E_SHRINK = 55;
	private static final int E_SLIDE = 60;
	private static final int E_SWITCHOFF = 65;
	
	// Wrap the unique identifiers in a human readable string to display on the page
	private SelectItem[] availableEffects = new SelectItem[] {
	    new SelectItem(E_BLIND, "Blind"),
	    new SelectItem(E_DROPOUT, "Drop Out"),
	    new SelectItem(E_FADE, "Fade"),
	    new SelectItem(E_FOLD, "Fold"),
	    new SelectItem(E_GROW, "Grow"),
	    new SelectItem(E_HIGHLIGHT, "Highlight"),
	    new SelectItem(E_MOVE, "Move"),
	    new SelectItem(E_OPACITY, "Opacity"),
	    new SelectItem(E_PUFF, "Puff"),
	    new SelectItem(E_PULSATE, "Pulsate"),
	    new SelectItem(E_SHAKE, "Shake"),
	    new SelectItem(E_SHRINK, "Shrink"),
	    new SelectItem(E_SLIDE, "Slide"),
	    new SelectItem(E_SWITCHOFF, "Switch Off")
	};
	private Integer selectedEffect = E_HIGHLIGHT;
	private Effect currentEffect = buildEffect(selectedEffect);
	
	public EffectBean() {
		super(EffectBean.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public Integer getSelectedEffect() { return selectedEffect; }
	public Effect getCurrentEffect() { return currentEffect; }
	public SelectItem[] getAvailableEffects() { return availableEffects; }
	
	public void setSelectedEffect(Integer selectedEffect) { this.selectedEffect = selectedEffect; }
	public void setCurrentEffect(Effect currentEffect) { this.currentEffect = currentEffect; }
	
	/**
	 * Method called when the effect has been changed
	 * This will cause the current Effect object to be rebuilt to match
	 *  what the user specified
	 */
	public void changeEffect(ValueChangeEvent event) {
	    currentEffect = buildEffect(Integer.parseInt(event.getNewValue().toString()));
	}
	
	/**
	 * Method called when the currently selected effect should be fired
	 * Basically called setFired(false)
	 */
	public void fireEffect(ActionEvent event) {
	    if (currentEffect != null) {
	        currentEffect.setFired(false);
	    }
	}
	
	/**
	 * Method to build an Effect (or EffectQueue) object based on the currently
	 *  selected Integer backing for the dropdown list on the page
	 */
	private Effect buildEffect(Integer id) {
	    Effect toReturn = null;
	    
	    // Switch on the new effect identifier
	    // We'll build a corresponding Effect object
	    switch (id) {
	        case E_BLIND :
	            toReturn = buildQueue(new BlindUp(), new BlindDown()); 
            break;
	        case E_DROPOUT :
	            toReturn = buildQueue(new DropOut());
            break;
	        case E_FADE :
	            toReturn = buildQueue(new Fade());
            break;
	        case E_FOLD :
	            toReturn = buildQueue(new Fold());
            break;
	        case E_GROW :
	            toReturn = new Grow();
            break;
	        case E_HIGHLIGHT :
	            toReturn = new Highlight();
            break;
	        case E_MOVE :
	            toReturn = buildQueue(new Move(-50, -50, "relative"), new Move(50, 50, "relative"));
            break;
	        case E_OPACITY :
	            toReturn = buildQueue(new Opacity(1.0f, 0.2f), new Opacity(0.2f, 1.0f));
            break;
	        case E_PUFF :
	            toReturn = buildQueue(new Puff());
            break;
	        case E_PULSATE :
	            toReturn = new Pulsate(1.1f);
            break;
	        case E_SHAKE :
	            toReturn = new Shake();
            break;
	        case E_SHRINK :
	            toReturn = buildQueue(new Shrink());
            break;
	        case E_SLIDE :
	            toReturn = buildQueue(new SlideUp(), new SlideDown());
            break;
	        case E_SWITCHOFF :
	            toReturn = buildQueue(new SwitchOff());
            break;
	    }
	    
	    if (toReturn != null) {
	        toReturn.setFired(true);
	    }
	    
	    return toReturn;
	}
	
	/**
	 * Wrapper method to build an EffectQueue that automatically Appears afterwards
	 * This use a useful method for Effects that remove the content from the page
	 *  (such as Fade or Fold)
	 */
	private EffectQueue buildQueue(Effect one) {
	    return buildQueue(one, new Appear());
	}
	
	/**
	 * Method to build an EffectQueue from the two passed Effects
	 * We will use a preset delay between the effects, and a unique name based
	 */
	private EffectQueue buildQueue(Effect one, Effect two) {
	    two.setDelay(0.35f);
	    
	    EffectQueue toReturn = new EffectQueue("q" + System.currentTimeMillis());
        toReturn.add(one);
        toReturn.add(two);
        return toReturn;
	}
}
