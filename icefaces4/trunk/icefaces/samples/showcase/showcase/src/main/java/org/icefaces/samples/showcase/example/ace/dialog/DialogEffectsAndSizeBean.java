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

package org.icefaces.samples.showcase.example.ace.dialog;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        parent = DialogBean.BEAN_NAME,
        title = "example.ace.dialog.dialogEffectAndSize.title",
        description = "example.ace.dialog.dialogEffectAndSize.description",
        example = "/resources/examples/ace/dialog/dialogEffectsAndSize.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dialogEffectsAndSize.xhtml",
                    resource = "/resources/examples/ace/dialog/dialogEffectsAndSize.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DialogEffectsAndSize.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dialog/DialogEffectsAndSizeBean.java")
        }
)
@ManagedBean(name= DialogEffectsAndSizeBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DialogEffectsAndSizeBean extends ComponentExampleImpl<DialogEffectsAndSizeBean> implements Serializable
{
    public static final String BEAN_NAME = "dialogEffectsAndSizeBean";
    private String showEffect;
    private String hideEffect;
    private int minWidth;
    private int minHeight;
    

    public DialogEffectsAndSizeBean() 
    {
        super(DialogEffectsAndSizeBean.class);
        initializeDefaultBeanValues();
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getHideEffect() {
        return hideEffect;
    }

    public void setHideEffect(String hideEffect) {
        this.hideEffect = hideEffect;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public String getShowEffect() {
        return showEffect;
    }

    public void setShowEffect(String showEffect) {
        this.showEffect = showEffect;
    }

    private void initializeDefaultBeanValues() 
    {
        this.showEffect = "blind";
        this.hideEffect = "blind";
        this.minWidth = 200;
        this.minHeight = 200;
    }
}
