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

import org.icefaces.samples.showcase.dataGenerators.ImageSet.ImageInfo;
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
import org.icefaces.samples.showcase.dataGenerators.ImageSet;

@ComponentExample(
        parent = SliderBean.BEAN_NAME,
        title = "example.ace.slider.listener.title",
        description = "example.ace.slider.listener.description",
        example = "/resources/examples/ace/slider/sliderListener.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "sliderListener.xhtml",
                        resource = "/resources/examples/ace/slider/sliderListener.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "SliderListener.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/slider/SliderListener.java"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "ImageSet.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/dataGenerators/ImageSet.java")
        }
)
@ManagedBean(name = SliderListener.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SliderListener extends ComponentExampleImpl<SliderListener> implements Serializable {
    public static final String BEAN_NAME = "sliderListener";
    private int sliderValue;
    private ImageSet.ImageInfo image;

    public SliderListener() {
        super(SliderListener.class);
        initializeInstanceVariables();
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    private void initializeInstanceVariables()
    {
        this.sliderValue = 0;
        this.image = ImageSet.getImage(ImageSet.ImageSelect.LIGHTBULB_OFF);
    }
    
    public void handleSwitch(ValueChangeEvent e)
    {
        if((Integer)e.getNewValue() == 0)
            this.image = ImageSet.getImage(ImageSet.ImageSelect.LIGHTBULB_OFF);
        else
            this.image = ImageSet.getImage(ImageSet.ImageSelect.LIGHTBULB_ON);
    }

    public int getSliderValue() {
        return sliderValue;
    }

    public void setSliderValue(int sliderValue) {
        this.sliderValue = sliderValue;
    }

    public ImageInfo getImage() {
        return image;
    }

    public void setImage(ImageInfo image) {
        this.image = image;
    }
}