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

package org.icefaces.samples.showcase.example.ace.tooltip;

import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.dataGenerators.ImageSet;
import org.icefaces.samples.showcase.dataGenerators.ImageSet.ImageInfo;

@ManagedBean(name= TooltipOverviewBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TooltipOverviewBean implements Serializable {
    
    public static final String BEAN_NAME = "tooltipOverviewBean";
	public String getBeanName() { return BEAN_NAME; }
    private String tooltipEffect = "slide";
    private Integer tooltipShowDelay = 500;
    private Integer tooltipHideDelay = 500;
    private ArrayList<ImageInfo> carSet;
    private String tooltipTargetPosition = "bottomLeft";
    private String tooltipPosition = "topRight";
    private Integer showEffectLength = 500;
    private Integer hideEffectLength = 500;
    private boolean renderSpeechBuble;
    
    /////////////---- CONSTRUCTOR BEGIN
    public TooltipOverviewBean() 
    {
        carSet = ImageSet.getImages(ImageSet.ImagesSelect.CARS);
    }

    /////////////---- GETTERS & SETTERS BEGIN
    public String getTooltipEffect() { return tooltipEffect; }
    public void setTooltipEffect(String tooltipEffect) { this.tooltipEffect = tooltipEffect; }
    public Integer getTooltipHideDelay() { return tooltipHideDelay; }
    public void setTooltipHideDelay(Integer tooltipHideDelay) { this.tooltipHideDelay = tooltipHideDelay; }
    public Integer getTooltipShowDelay() { return tooltipShowDelay; }
    public void setTooltipShowDelay(Integer tooltipShowDelay) { this.tooltipShowDelay = tooltipShowDelay; }
    public String getTooltipTargetPosition() { return tooltipTargetPosition; }
    public void setTooltipTargetPosition(String tooltipTargetPosition) { this.tooltipTargetPosition = tooltipTargetPosition; }
    public String getTooltipPosition() { return tooltipPosition; }
    public void setTooltipPosition(String tooltipPosition) { this.tooltipPosition = tooltipPosition; }
    public Integer getHideEffectLength() { return hideEffectLength; }
    public void setHideEffectLength(Integer hideEffectLength) { this.hideEffectLength = hideEffectLength; }
    public Integer getShowEffectLength() { return showEffectLength; }
    public void setShowEffectLength(Integer showEffectLength) { this.showEffectLength = showEffectLength; }
    public ArrayList<ImageInfo> getCarSet() { return carSet; }
    public boolean isRenderSpeechBuble() { return renderSpeechBuble; }
    public void setRenderSpeechBuble(boolean renderSpeechBuble) { this.renderSpeechBuble = renderSpeechBuble; }
}