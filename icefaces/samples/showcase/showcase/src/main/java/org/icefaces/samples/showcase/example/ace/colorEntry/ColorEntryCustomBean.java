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
    private List<String> parts = new ArrayList<String>();
    private List<String> parts1 = new ArrayList<String>(Arrays.asList("header", "preview", "hex", "rgbslider"));
    private List<String> parts2 = new ArrayList<String>(Arrays.asList("header", "map", "bar", "hex", "hsv", "rgb", "alpha", "preview", "swatches", "footer"));
    private List<ColorEntryLayout> layout1 = new ArrayList<ColorEntryLayout>();
    private List<ColorEntryLayout> layout2 = new ArrayList<ColorEntryLayout>();
    private List<ColorEntryLayout> layout = new ArrayList<ColorEntryLayout>();
    private ColorFormat valueFormat;

    public ColorEntryCustomBean(){
        layout1.add(new ColorEntryLayout("preview", 0, 0, 1, 1));
        layout1.add(new ColorEntryLayout("hex", 1,0,1,1));
        layout1.add(new ColorEntryLayout("rgbslider", 0, 1, 2, 1));
        layout1.add(new ColorEntryLayout("memory", 0, 2, 2, 1));

        layout2.add(new ColorEntryLayout("hex", 0, 0, 2, 1));
        layout2.add(new ColorEntryLayout("preview", 2, 0, 1, 1));
        layout2.add(new ColorEntryLayout("map", 0, 1, 3, 1));
        layout2.add(new ColorEntryLayout("bar", 0, 2, 1, 4));
        layout2.add(new ColorEntryLayout("swatches", 2, 2, 1, 4));
        layout2.add(new ColorEntryLayout("rbg", 1, 2, 1, 1));
        layout2.add(new ColorEntryLayout("hsv", 1, 3, 1, 1));
        layout2.add(new ColorEntryLayout("alpha", 1, 4, 1, 1));
        layout2.add(new ColorEntryLayout("lab", 0, 5, 1, 1));
        layout2. add(new ColorEntryLayout("cmyk", 1, 5, 1, 2));
        this.parts = parts1;
        this.layout = layout1;
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

    public ColorFormat getValueFormat() {
        return valueFormat;
    }

    public void setValueFormat(ColorFormat valueFormat) {
        this.valueFormat = valueFormat;
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
            this.parts=parts2;
            this.layout = layout2;
        } else {
            this.parts = parts1;
            this.layout=layout1;
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
}
