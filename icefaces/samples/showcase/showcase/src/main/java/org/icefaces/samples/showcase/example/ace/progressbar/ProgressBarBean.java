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

package org.icefaces.samples.showcase.example.ace.progressbar;

import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.icefaces.samples.showcase.dataGenerators.ImageSet;
import org.icefaces.samples.showcase.dataGenerators.ImageSet.ImageInfo;

@ManagedBean(name= ProgressBarBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ProgressBarBean implements Serializable {
    public static final String BEAN_NAME = "progressBarBean";
	public String getBeanName() { return BEAN_NAME; }
    
    private ArrayList<ImageSet.ImageInfo> imagesOfCars;
    private Integer progressValue;
    private ImageSet.ImageInfo currentImage;
    private int currentIndex;
    private String message;
    private ImageSet.ImageInfo arrowForwardImage;
    private ImageSet.ImageInfo arrowBackwardImage;
    
    /////////////---- CONSTRUCTOR BEGIN
    public ProgressBarBean() {
        this.imagesOfCars = ImageSet.getImages(ImageSet.ImagesSelect.CARS);
        this.arrowForwardImage = ImageSet.getImage(ImageSet.ImageSelect.FORWARD_ARROW);
        this.arrowBackwardImage = ImageSet.getImage(ImageSet.ImageSelect.BACKWARD_ARROW);
        currentIndex = 0;
        setBeanVariables(currentIndex);
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