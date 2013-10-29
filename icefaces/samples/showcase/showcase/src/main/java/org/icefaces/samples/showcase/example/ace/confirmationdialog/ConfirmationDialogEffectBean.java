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

package org.icefaces.samples.showcase.example.ace.confirmationdialog;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.LinkedHashMap;

@ComponentExample(
        parent = ConfirmationDialogBean.BEAN_NAME,
        title = "example.ace.confirmationdialog.effect.title",
        description = "example.ace.confirmationdialog.effect.description",
        example = "/resources/examples/ace/confirmationdialog/confirmationDialogEffect.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="confirmationDialogEffect.xhtml",
                    resource = "/resources/examples/ace/confirmationdialog/confirmationDialogEffect.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ConfirmationDialogEffect.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/confirmationdialog/ConfirmationDialogEffectBean.java")
        }
)
@ManagedBean(name= ConfirmationDialogEffectBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ConfirmationDialogEffectBean extends ComponentExampleImpl<ConfirmationDialogEffectBean> implements Serializable {
    public static final String BEAN_NAME = "confirmationDialogEffectBean";
    private String showEffect;
    private String hideEffect;
    private String outcome;
    private LinkedHashMap<String, String> showEffects;
	private LinkedHashMap<String, String> hideEffects;

    public ConfirmationDialogEffectBean() {
        super(ConfirmationDialogEffectBean.class);
        outcome = null;
        
        showEffects = initializeDialogShowEffects();
		hideEffects = initializeDialogHideEffects();
        showEffect = showEffects.get("Fade");
        hideEffect = hideEffects.get("Fade");
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

        private LinkedHashMap<String, String> initializeDialogShowEffects()
        {
            LinkedHashMap<String, String> list = new LinkedHashMap<String, String>();
            list.put("Fade", "fade");
            list.put("Highlight", "highlight");
            list.put("Blind", "blind");
            list.put("Bounce", "bounce");
            list.put("Clip", "clip");
            list.put("Explode", "explode");
            list.put("Puff", "puff");
            list.put("Pulsate", "pulsate");
            list.put("Scale", "scale");
            list.put("Slide", "slide"); 
            list.put("Drop", "drop");
            list.put("Fold", "fold");
            list.put("Shake", "shake");
            return list;
    }
	
        private LinkedHashMap<String, String> initializeDialogHideEffects()
        {
            LinkedHashMap<String, String> list = new LinkedHashMap<String, String>();
            list.put("Fade", "fade");
            list.put("Highlight", "highlight");
            list.put("Blind", "blind");
            list.put("Bounce", "bounce");
            list.put("Clip", "clip");
            list.put("Explode", "explode");
            list.put("Puff", "puff");
            list.put("Scale", "scale");
            list.put("Slide", "slide");
            list.put("Drop", "drop");
            list.put("Fold", "fold");
            return list;
    }

    public void yes(ActionEvent actionEvent) { 
            outcome = "You clicked 'yes'";
    }

    public void no(ActionEvent actionEvent) { 
            outcome = "You clicked 'no'";
    }

    public String getOutcome() {
            return outcome;
    }

    public String getHideEffect() {
        return hideEffect;
    }

    public void setHideEffect(String hideEffect) {
        this.hideEffect = hideEffect;
    }

    public String getShowEffect() {
        return showEffect;
    }

    public void setShowEffect(String showEffect) {
        this.showEffect = showEffect;
    }

    public LinkedHashMap<String, String> getShowEffects() {
        return showEffects;
    }

    public void setShowEffects(LinkedHashMap<String, String> effects) {
        this.showEffects = effects;
    }
	
    public LinkedHashMap<String, String> getHideEffects() {
        return hideEffects;
    }

    public void setHideEffects(LinkedHashMap<String, String> effects) {
        this.hideEffects = effects;
    }
}