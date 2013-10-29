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

import org.icefaces.samples.showcase.dataGenerators.ImageSet;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import org.icefaces.ace.event.AccordionPaneChangeEvent;

@ComponentExample(
        parent = AccordionPanelBean.BEAN_NAME,
        title = "example.ace.accordionpanel.dynamic.title",
        description = "example.ace.accordionpanel.dynamic.description",
        example = "/resources/examples/ace/accordionpanel/accordionPanelDynamic.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="accordionPanelDynamic.xhtml",
                    resource = "/resources/examples/ace/accordionpanel/accordionPanelDynamic.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="AccordionPanelDynamic.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/accordionpanel/AccordionPanelDynamicBean.java")
        }
)
@ManagedBean(name= AccordionPanelDynamicBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AccordionPanelDynamicBean extends ComponentExampleImpl<AccordionPanelDynamicBean> implements Serializable 
{
    public static final String BEAN_NAME = "accordionPanelDynamicBean";
    
    private ArrayList<ImageSet.ImageInfo> imagesOfCars;
    private ArrayList<ImageSet.ImageInfo> imagesOfElectronicDevices;
    private ArrayList<ImageSet.ImageInfo> imagesOfFood;
    private String tabChangeDescriptor;
    
    public AccordionPanelDynamicBean() 
    {
        super(AccordionPanelDynamicBean.class);
        initializeInstanceVariables();
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    ////////////////////////////////////////////ON TAB CHANGE EVENT BEGIN/////////////////////////////////////////////////
    public void onPaneChange(AccordionPaneChangeEvent event)
    {  
        tabChangeDescriptor = event.getTab().getTitle();
    }
    
    /////////////////////////////////////////////////PRIVATE METHODS BEGIN//////////////////////////////////////////////////
    private void initializeInstanceVariables() 
    {
        this.imagesOfElectronicDevices = ImageSet.getImages(ImageSet.ImagesSelect.GADGETS);
        this.imagesOfCars = ImageSet.getImages(ImageSet.ImagesSelect.CARS);
        this.imagesOfFood = ImageSet.getImages(ImageSet.ImagesSelect.FOOD);
    }
    
    //////////////////////////////////////////////////GETTERS&SETTERS BEGIN////////////////////////////////////////////////
    public ArrayList<ImageSet.ImageInfo> getImagesOfCars()
    {
        return imagesOfCars;
    }

    public int getImagesOfCarsSize(){
        return imagesOfCars != null ? imagesOfCars.size():0;
    }

    public ArrayList<ImageSet.ImageInfo> getImagesOfElectronicDevices()
    {
        return imagesOfElectronicDevices;
    }

    public int getImagesOfElectronicDevicesSize(){
        return imagesOfElectronicDevices != null? imagesOfElectronicDevices.size():0;
    }

    public ArrayList<ImageSet.ImageInfo> getImagesOfFood() {
        return imagesOfFood;
    }

    public int getImagesOfFoodSize(){
        return imagesOfFood != null ? imagesOfFood.size(): 0;
    }

    public String getTabChangeDescriptor() {
        if(tabChangeDescriptor == null)
            return "Click on any pane to fire an event";
        else
            return tabChangeDescriptor;
    }

    public void setTabChangeDescriptor(String tabChangeDescriptor) {
        this.tabChangeDescriptor = tabChangeDescriptor;
    }
}
