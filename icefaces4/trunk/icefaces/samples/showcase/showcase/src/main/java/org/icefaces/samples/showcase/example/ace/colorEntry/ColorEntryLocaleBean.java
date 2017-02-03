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

import org.icefaces.ace.component.colorentry.ColorFormat;
import org.icefaces.ace.model.colorEntry.ColorEntryLayout;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.*;

@ManagedBean(name= ColorEntryLocaleBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ColorEntryLocaleBean implements Serializable
{
    public static final String BEAN_NAME = "colorEntryLocaleBean";
	public String getBeanName() { return BEAN_NAME; }

    private static final long serialVersionUID = 1L;
    private List<String> full = new ArrayList<String>(Arrays.asList("header", "map", "bar", "hex", "hsv", "rgb", "alpha", "lab", "cmyk", "preview", "swatches", "footer"));
    private List<ColorEntryLayout> fullLayout = new ArrayList<ColorEntryLayout>();
    private String color;
    private String localeString="en";

    public ColorEntryLocaleBean(){
        /* show all the available parts for localisation example */
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
    }

    public String getLocaleString() {
         return localeString;
     }

    public void setLocaleString(String localeString) {
        this.localeString = localeString;
    }

    public String getColor() {
         return color;
     }

    public void setColor(String color) {
         this.color = color;
     }

    public List<ColorEntryLayout> getFullLayout() {
        return fullLayout;
    }

    public List<String> getFull() {
        return full;
    }

    public void setFull(List<String> full) {
        this.full = full;
    }

    public void setFullLayout(List<ColorEntryLayout> fullLayout) {
        this.fullLayout = fullLayout;
    }
}
