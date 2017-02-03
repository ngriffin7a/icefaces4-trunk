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

package org.icefaces.samples.showcase.example.ace.colorEntry;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import org.icefaces.ace.component.colorentry.ColorFormat;
import org.icefaces.ace.model.colorEntry.ColorEntryLayout;

@ManagedBean(name= ColorEntryCustomBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ColorEntryCustomBean implements Serializable
{
    public static final String BEAN_NAME = "colorEntryCustomBean";
	public String getBeanName() { return BEAN_NAME; }
    
    private String value, customLayout;
    private boolean alpha;
    private boolean showNoneButton;
    private String title1="Simple Layout with RGB Slider";
    private String title2= "Full Layout";
    private List<String> parts = new ArrayList<String>();
    private List<String> full = new ArrayList<String>(Arrays.asList("header", "map", "bar", "hex", "hsv", "rgb", "alpha", "lab", "cmyk", "preview", "swatches", "footer"));
    private List<String> parts1 = new ArrayList<String>(Arrays.asList("header", "preview", "hex", "rgbslider"));
    private List<ColorEntryLayout> layout1 = new ArrayList<ColorEntryLayout>();
    private List<ColorEntryLayout> fullLayout = new ArrayList<ColorEntryLayout>();
    private List<ColorEntryLayout> layout = new ArrayList<ColorEntryLayout>();
    private ColorFormat colorFormat;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ColorEntryCustomBean(){
        layout1.add(new ColorEntryLayout("preview", 0, 0, 1, 1));
        layout1.add(new ColorEntryLayout("hex", 1,0,1,1));
        layout1.add(new ColorEntryLayout("rgbslider", 0, 1, 2, 1));
        layout1.add(new ColorEntryLayout("memory", 0, 2, 2, 1));

        fullLayout.add(new ColorEntryLayout("map", 0,0,1,5));
        fullLayout.add(new ColorEntryLayout("bar", 1,0,1,5));
        fullLayout.add(new ColorEntryLayout("preview", 2,0,1,1));
        fullLayout.add(new ColorEntryLayout("hsv", 2,1,1,1));
        fullLayout.add(new ColorEntryLayout("rgb", 2, 2, 1, 1));
        fullLayout.add(new ColorEntryLayout("alpha", 2, 3, 1, 1));
        fullLayout.add(new ColorEntryLayout("hex", 2, 4, 1, 1));
        fullLayout.add(new ColorEntryLayout("lab", 3, 1, 1, 1));
        fullLayout.add(new ColorEntryLayout("cmyk", 3, 2, 1, 2));
        fullLayout.add(new ColorEntryLayout("swatches",4,0, 1, 5));

        this.parts = parts1;
        this.layout = layout1;
        this.title=title1;
        this.alpha = false;
        this.showNoneButton=false;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public ColorFormat[] getColorFormats(){
        return ColorFormat.values();
    }

    public boolean isAlpha() {
        return alpha;
    }

    public void setAlpha(boolean alpha) {
        this.alpha = alpha;
    }

    public boolean isShowNoneButton() {
        return showNoneButton;
    }

    public void setShowNoneButton(boolean showNoneButton) {
        this.showNoneButton = showNoneButton;
    }

    public ColorFormat getValueFormat() {
        return colorFormat;
    }

    public void setValueFormat(ColorFormat colorFormat) {
        this.colorFormat = colorFormat;
    }

    public List<String> getParts() {
        return parts;
    }

    public List<ColorEntryLayout> getLayout() {
        return layout;
    }

    public String getCustomLayout() {
        return customLayout;
    }

    public void setCustomLayout(String customLayout) {
        this.customLayout = customLayout;
    }

    public void setLayout(List<ColorEntryLayout> layout) {
        this.layout = layout;
    }

    public void setParts(List<String> parts) {
        this.parts = parts;
    }

    public void changeLayout(AjaxBehaviorEvent event) {
        if (customLayout.equals("layout2")){
            this.parts=full;
            this.layout = fullLayout;
            this.title = title2;
            this.alpha=true;
            this.showNoneButton=true;
        } else {
            this.alpha = false;
            this.showNoneButton=false;
            this.parts = parts1;
            this.layout=layout1;
            this.title=title1;
        }
    }

    public String getPartsDefined(){
        if (parts.isEmpty())return "";
        String definedParts = "| ";
        for (String part: parts){
            definedParts += part+" | ";
        }
        return definedParts;
    }

    public String getLayoutDefined(){
        if (layout.isEmpty())return "";
        String customLayout = "|";
        for (ColorEntryLayout cel: layout){
            customLayout += cel.getPart() + ":" + cel.getEntry()+" | ";
        }
        return customLayout;
    }

    protected List<ColorEntryLayout> getFullLayout() {
        return fullLayout;
    }

    protected void setFullLayout(List<ColorEntryLayout> fullLayout) {
        this.fullLayout = fullLayout;
    }

    protected List<String> getFull() {
        return full;
    }

    protected void setFull(List<String> full) {
        this.full = full;
    }
}
