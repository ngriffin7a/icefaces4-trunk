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

package org.icefaces.samples.showcase.example.ace.accordionpanel;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@ComponentExample(
        parent = AccordionPanelBean.BEAN_NAME,
        title = "example.ace.accordionpanel.effect.title",
        description = "example.ace.accordionpanel.effect.description",
        example = "/resources/examples/ace/accordionpanel/accordionPanelEffect.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="accordionPanelEffect.xhtml",
                    resource = "/resources/examples/ace/accordionpanel/accordionPanelEffect.xhtml")
        }
)
@ManagedBean(name= AccordionPanelEffectBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AccordionPanelEffectBean extends ComponentExampleImpl<AccordionPanelEffectBean> implements Serializable {
    public static final String BEAN_NAME = "accordionPanelEffectBean";

    public AccordionPanelEffectBean() {
        super(AccordionPanelEffectBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}