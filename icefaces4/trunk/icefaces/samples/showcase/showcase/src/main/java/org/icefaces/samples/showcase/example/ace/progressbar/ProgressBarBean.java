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

package org.icefaces.samples.showcase.example.ace.progressbar;

import org.icefaces.samples.showcase.dataGenerators.ImageSet.ImageInfo;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.event.ActionEvent;
import org.icefaces.samples.showcase.dataGenerators.ImageSet;

@ComponentExample(
        title = "example.ace.progressbar.title",
        description = "example.ace.progressbar.description",
        example = "/resources/examples/ace/progressbar/progressBar.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="progressBar.xhtml",
                    resource = "/resources/examples/ace/progressbar/progressBar.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ProgressBarBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/progressbar/ProgressBarBean.java")
        }
)
@Menu(
	title = "menu.ace.progressbar.subMenu.title",
	menuLinks = {
	         @MenuLink(title = "menu.ace.progressbar.subMenu.main", isDefault = true, exampleBeanName = ProgressBarBean.BEAN_NAME)
                        ,@MenuLink(title = "menu.ace.progressbar.subMenu.polling", exampleBeanName = ProgressBarPolling.BEAN_NAME)
                        ,@MenuLink(title = "menu.ace.progressbar.subMenu.push", exampleBeanName = ProgressBarPush.BEAN_NAME)
                        ,@MenuLink(title = "menu.ace.progressbar.subMenu.client", exampleBeanName = ProgressBarClient.BEAN_NAME)
                        ,@MenuLink(title = "menu.ace.progressbar.subMenu.clientAndServer", exampleBeanName = ProgressBarClientAndServer.BEAN_NAME)
    }
)
@ManagedBean(name= ProgressBarBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ProgressBarBean extends ComponentExampleImpl<ProgressBarBean> implements Serializable {
    public static final String BEAN_NAME = "progressBarBean";
    
    private ArrayList<ImageSet.ImageInfo> imagesOfCars;
    private Integer progressValue;
    private ImageSet.ImageInfo currentImage;
    private int currentIndex;
    private String message;
    private ImageSet.ImageInfo arrowForwardImage;
    private ImageSet.ImageInfo arrowBackwardImage;
    
    /////////////---- CONSTRUCTOR BEGIN
    public ProgressBarBean() {
        super(ProgressBarBean.class);
        
        this.imagesOfCars = ImageSet.getImages(ImageSet.ImagesSelect.CARS);
        this.arrowForwardImage = ImageSet.getImage(ImageSet.ImageSelect.FORWARD_ARROW);
        this.arrowBackwardImage = ImageSet.getImage(ImageSet.ImageSelect.BACKWARD_ARROW);
        currentIndex = 0;
        setBeanVariables(currentIndex);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    /////////////---- ACTION LISTENERS BEGIN
    public void returnToPreviousImage(ActionEvent event)
    {
        if(currentIndex > 0 && currentIndex<imagesOfCars.size())
        {
            currentIndex--;
            setBeanVariables(currentIndex);
        }
    }
    public void proceedToNextImage(ActionEvent event)
    {
        if(currentIndex!=imagesOfCars.size()-1)
        {
            currentIndex++;
            setBeanVariables(currentIndex);
        }
    }
	
    /////////////---- PRIVATE METHODS BEGIN
    private void setBeanVariables(int currentIndex) 
    {
        this.progressValue = findProgressValue(currentIndex);
        this.currentImage = imagesOfCars.get(currentIndex);
        this.message = "Image " +(currentIndex+1)+" out of "+imagesOfCars.size();
    }
    
    private String findImageDescription(String imagePath) 
    {
        String description;
        char searchCriteria = '/';
        int substringStartIndex = imagePath.lastIndexOf(searchCriteria);
        description = imagePath.substring(substringStartIndex+1);
        return description;
    }
    
    private Integer findProgressValue(int indexValue)
    {
        return ++indexValue*(100/imagesOfCars.size());
    }
    
    /////////////---- GETTERS & SETTERS BEGIN
    public ArrayList<ImageSet.ImageInfo> getImagesOfCars() { return imagesOfCars; }
    public Integer getProgressValue() { return progressValue; }
    public void setProgressValue(Integer progressValue) { this.progressValue = progressValue; }
    public ImageSet.ImageInfo getCurrentImage() { return currentImage; }
    public String getMessage() { return message; }
    public ImageInfo getArrowBackwardImage() { return arrowBackwardImage; }
    public void setArrowBackwardImage(ImageInfo arrowBackwardImage) { this.arrowBackwardImage = arrowBackwardImage; }
    public ImageInfo getArrowForwardImage() { return arrowForwardImage; }
    public void setArrowForwardImage(ImageInfo arrowForwardImage) { this.arrowForwardImage = arrowForwardImage; }
}