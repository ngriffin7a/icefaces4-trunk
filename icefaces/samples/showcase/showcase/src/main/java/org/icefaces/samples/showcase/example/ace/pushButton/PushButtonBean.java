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

package org.icefaces.samples.showcase.example.ace.pushButton;

import org.icefaces.samples.showcase.dataGenerators.ImageSet.ImageInfo;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;                                                                       
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import org.icefaces.samples.showcase.dataGenerators.ImageSet;


@ComponentExample(
        title = "example.ace.pushButton.title",
        description = "example.ace.pushButton.description",
        example = "/resources/examples/ace/pushButton/pushbutton.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="pushbutton.xhtml",
                    resource = "/resources/examples/ace/pushButton/pushbutton.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PushButtonBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/pushButton/PushButtonBean.java")
        }
)
@Menu(
	title = "menu.ace.pushButton.subMenu.title",
	menuLinks = 
                {
                    @MenuLink(title = "menu.ace.pushButton.subMenu.main", isDefault = true,
                                     exampleBeanName = PushButtonBean.BEAN_NAME)
                }
)

@ManagedBean(name= PushButtonBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PushButtonBean extends ComponentExampleImpl<PushButtonBean> implements Serializable {

    public static final String BEAN_NAME = "pushButton";
    ImageSet.ImageInfo currentImage;
    
    public PushButtonBean() {
        super(PushButtonBean.class);
        currentImage = ImageSet.getNextImage(currentImage);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String executeAction() {
        //application logic can be added here
        return null;
    }
    
    public void executeListener(ActionEvent event) 
    {
        currentImage = ImageSet.getNextImage(currentImage);
    }
    
    public ImageInfo getCurrentImage() {
        return currentImage;
    }

    public void setCurrentImage(ImageInfo currentImage) {
        this.currentImage = currentImage;
    }
}
