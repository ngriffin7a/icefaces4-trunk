/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.ace.event.AccordionPaneChangeEvent;
import org.icefaces.samples.showcase.dataGenerators.ImageSet;

@ManagedBean(name= AccordionPanelDynamicBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AccordionPanelDynamicBean implements Serializable 
{
    public static final String BEAN_NAME = "accordionPanelDynamicBean";
	public String getBeanName() { return BEAN_NAME; }
    
    private ArrayList<ImageSet.ImageInfo> imagesOfCars;
    private ArrayList<ImageSet.ImageInfo> imagesOfElectronicDevices;
    private ArrayList<ImageSet.ImageInfo> imagesOfFood;
    private String tabChangeDescriptor;
    
    public AccordionPanelDynamicBean() 
    {
        initializeInstanceVariables();
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
