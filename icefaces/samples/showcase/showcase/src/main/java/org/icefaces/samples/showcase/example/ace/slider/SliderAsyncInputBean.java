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

package org.icefaces.samples.showcase.example.ace.slider;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import javax.faces.event.ValueChangeEvent;

@ComponentExample(
        parent = SliderBean.BEAN_NAME,
        title = "example.ace.slider.asyncinput.title",
        description = "example.ace.slider.asyncinput.description",
        example = "/resources/examples/ace/slider/slider-async-input.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "slider-async-input.xhtml",
                        resource = "/resources/examples/ace/slider/slider-async-input.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "SliderAsyncInputBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/slider/SliderAsyncInputBean.java")
        }
)
@ManagedBean(name = SliderAsyncInputBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SliderAsyncInputBean extends ComponentExampleImpl<SliderAsyncInputBean> implements Serializable {
    public static final String BEAN_NAME = "sliderAsyncInput";
    private int sliderValue = 0;
    private boolean render = false;

    public SliderAsyncInputBean() {
        super(SliderAsyncInputBean.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public void sliderValueChanged(ValueChangeEvent e)
    {
        if((Integer)e.getNewValue() == 9)
        {
            render = true;
        }
        else
        {
            render = false;
        }
    }

    public int getSliderValue() {
        return sliderValue;
    }

    public void setSliderValue(int sliderValue) {
        this.sliderValue = sliderValue;
    }

    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }
}