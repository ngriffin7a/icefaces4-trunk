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

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name= ColorEntryPaletteBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ColorEntryPaletteBean implements Serializable
{
    public static final String BEAN_NAME = "colorEntryPaletteBean";
	public String getBeanName() { return BEAN_NAME; }
    
    private String value, togglePaletteMoreText, togglePaletteLessText;
    private boolean showPalette, showPaletteOnly,
                                   togglePaletteOnly,
                                   showSelectionPalette;
    private int maxSelectionSize;
    private String[] togglePalette1 ={
             "#000","#444","#666","#999","#ccc","#eee","#f3f3f3","#fff"
    } ;
    private String[] togglePalette2={
         "#f00","#f90","#ff0","#0f0","#0ff","#00f","#90f","#f0f"
    };
    private String[] togglePalette3={
        "#f4cccc","#fce5cd","#fff2cc","#d9ead3","#d0e0e3","#cfe2f3","#d9d2e9","#ead1dc"
    };
    private String[] togglePalette4={
             "#ea9999","#f9cb9c","#ffe599","#b6d7a8","#a2c4c9","#9fc5e8","#b4a7d6","#d5a6bd"
    };
    private String[] togglePalette5={
             "#e06666","#f6b26b","#ffd966","#93c47d","#76a5af","#6fa8dc","#8e7cc3","#c27ba0"
    };
    private String[] togglePalette6={
             "#c00","#e69138","#f1c232","#6aa84f","#45818e","#3d85c6","#674ea7","#a64d79"
    };
    private String[] togglePalette7={
             "#900","#b45f06","#bf9000","#38761d","#134f5c","#0b5394","#351c75","#741b47"
    };
    private String[] togglePalette8={
             "#600","#783f04","#7f6000","#274e13","#0c343d","#073763","#20124d","#4c1130"
    };
    private List<String[]> palleteList = new ArrayList<String[]>();
    private List<String[]> togglePaletteList = new ArrayList<String[]>();

    @PostConstruct
    public void init() {
        this.togglePaletteList.add(this.togglePalette1);
        this.togglePaletteList.add(this.togglePalette2);
        this.togglePaletteList.add(this.togglePalette3);
        this.togglePaletteList.add(this.togglePalette4);
        this.togglePaletteList.add(this.togglePalette5);
        this.togglePaletteList.add(this.togglePalette6);
        this.togglePaletteList.add(this.togglePalette7);
        this.togglePaletteList.add(this.togglePalette8);
        this.togglePaletteMoreText = "More";
        this.togglePaletteLessText="less";
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTogglePaletteMoreText() {
        return togglePaletteMoreText;
    }

    public void setTogglePaletteMoreText(String togglePaletteMoreText) {
        this.togglePaletteMoreText = togglePaletteMoreText;
    }

    public String getTogglePaletteLessText() {
        return togglePaletteLessText;
    }

    public void setTogglePaletteLessText(String togglePaletteLessText) {
        this.togglePaletteLessText = togglePaletteLessText;
    }

    public boolean isShowPalette() {
        return showPalette;
    }

    public void setShowPalette(boolean showPalette) {
        this.showPalette = showPalette;
    }

    public boolean isShowPaletteOnly() {
        return showPaletteOnly;
    }

    public void setShowPaletteOnly(boolean showPaletteOnly) {
        this.showPaletteOnly = showPaletteOnly;
    }

    public boolean isTogglePaletteOnly() {
        return togglePaletteOnly;
    }

    public void setTogglePaletteOnly(boolean togglePaletteOnly) {
        this.togglePaletteOnly = togglePaletteOnly;
    }

    public boolean isShowSelectionPalette() {
        return showSelectionPalette;
    }

    public void setShowSelectionPalette(boolean showSelectionPalette) {
        this.showSelectionPalette = showSelectionPalette;
    }

    public int getMaxSelectionSize() {
        return maxSelectionSize;
    }

    public void setMaxSelectionSize(int maxSelectionSize) {
        this.maxSelectionSize = maxSelectionSize;
    }

    public List<String[]> getTogglePaletteList() {
        return togglePaletteList;
    }

    public void setTogglePaletteList(List<String[]> togglePaletteList) {
        this.togglePaletteList = togglePaletteList;
    }
}
