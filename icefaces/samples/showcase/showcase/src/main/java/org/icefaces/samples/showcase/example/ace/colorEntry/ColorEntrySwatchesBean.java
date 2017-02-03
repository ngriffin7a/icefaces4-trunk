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
import org.icefaces.ace.model.colorEntry.SwatchEntry;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ManagedBean(name= ColorEntrySwatchesBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ColorEntrySwatchesBean implements Serializable
{
     public static final String BEAN_NAME = "colorEntrySwatchesBean";
	 public String getBeanName() { return BEAN_NAME; }
     private String color, fromPalette, fromPredefined, dynamic;
     private boolean haveValues = false;
     private String swatchName="html";

     public String getColor() {
         return color;
     }

     public void setColor(String color) {
         this.color = color;
     }

     public List<String> getColorList() {
         return colorList;
     }

     public void setColorList(List<String> colorList) {
         this.colorList = colorList;
     }

     private List<String> parts = new ArrayList<String>(Arrays.asList("header", "preview", "hex", "rgbslider", "footer"));
     private List<String> swatchParts = new ArrayList<String>(Arrays.asList("header", "preview", "hex", "swatches", "footer"));
     private List<SwatchEntry> swatchEntries = new ArrayList<SwatchEntry>();
     private List<SwatchEntry> predefSwatchEntries = new ArrayList<SwatchEntry> ();
     private List<ColorEntryLayout> layout = new ArrayList<ColorEntryLayout>();
     private List<ColorEntryLayout> swatchLayout = new ArrayList<ColorEntryLayout>();
     private List<ColorEntryLayout> swatchLayoutLong = new ArrayList<ColorEntryLayout>();
     private ColorFormat colorFormat = ColorFormat.RGB;
     private String title;
     private List<String> colorList ;

     public String getTitle() {
         return title;
     }

     public void setTitle(String title) {
         this.title = title;
     }

     public ColorEntrySwatchesBean(){
         colorList = new ArrayList<String>();
         for (int i=0; i < 4; i++){
             colorList.add("");
         }
         layout.add(new ColorEntryLayout("preview", 0, 0, 1, 1));
         layout.add(new ColorEntryLayout("hex", 1,0,1,1));
         layout.add(new ColorEntryLayout("rgbslider", 0, 1, 2, 1));
         swatchLayout.add(new ColorEntryLayout("preview", 0, 0, 1, 1));
         swatchLayout.add(new ColorEntryLayout("hex", 1,0,1,1));
         swatchLayout.add(new ColorEntryLayout("swatches", 2,0, 1, 1));
         swatchLayoutLong.add(new ColorEntryLayout("preview", 0, 0, 1, 1));
         swatchLayoutLong.add(new ColorEntryLayout("hex", 1,0,1,1));
         swatchLayoutLong.add(new ColorEntryLayout("swatches", 0, 1, 2, 1));
         predefSwatchEntries.add(new SwatchEntry("Dark Grey", 0, 0, 0)) ;
         predefSwatchEntries.add(new SwatchEntry("Med-dark Grey", 0.266666667, 0.266666667, 0.26666667));
         predefSwatchEntries.add(new SwatchEntry("Med-lt Grey", 0.4, 0.4, 0.4));
         predefSwatchEntries.add(new SwatchEntry("Lt Grey", 0.6, 0.6, 0.6));

     }

     public boolean isHaveValues() {
         return (colorList.get(1).length()>0 && colorList.get(2).length()>0 && colorList.get(3).length()>0  && colorList.get(0).length()>0 );
     }

     public void updateSwatch(ActionEvent event){
          //update the swatches list of colors--only 4 so do this each time.
         swatchEntries = new ArrayList<SwatchEntry>();
         for (int i=0; i< 4; i++){
             if (colorList.get(i).length()>0){
                 Pattern pattern = Pattern.compile("rgb *\\( *([0-9]+), *([0-9]+), *([0-9]+) *\\)");
                 Matcher matcher = pattern.matcher(colorList.get(i));
                 if (matcher.matches()) {
                     Double r =  (Double.valueOf(matcher.group(1)));
                     Double g =  (Double.valueOf(matcher.group(2)));
                     Double b =  (Double.valueOf(matcher.group(3)));
                     swatchEntries.add(new SwatchEntry("color"+i, (r/256), (g/256), b/256));
                 }
             }
         }
     }

     public void setHaveValues(boolean haveValues) {
         this.haveValues = haveValues;
     }

     public ColorFormat[] getColorFormats(){
         return ColorFormat.values();
     }

     public ColorFormat getColorFormat() {
         return colorFormat;
     }

     public void setColorFormat(ColorFormat colorFormat) {
         this.colorFormat = colorFormat;
     }

     public List<String> getParts() {
         return parts;
     }

     public List<ColorEntryLayout> getLayout() {
         return layout;
     }

     public void setLayout(List<ColorEntryLayout> layout) {
         this.layout = layout;
     }

     public void setParts(List<String> parts) {
         this.parts = parts;
     }

     public String getFromPalette() {
         return fromPalette;
     }

     public void setFromPalette(String fromPalette) {
         this.fromPalette = fromPalette;
     }

     public List<SwatchEntry> getSwatchEntries() {
         return swatchEntries;
     }

     public void setSwatchEntries(List<SwatchEntry> swatchEntries) {
         this.swatchEntries = swatchEntries;
     }

     public List<String> getSwatchParts() {
         return swatchParts;
     }

     public List<ColorEntryLayout> getSwatchLayout() {
         return swatchLayout;
     }

     public void setSwatchLayout(List<ColorEntryLayout> swatchLayout) {
         this.swatchLayout = swatchLayout;
     }

     public void setSwatchParts(List<String> swatchParts) {
         this.swatchParts = swatchParts;
     }

     public String getSwatchName() {
         return swatchName;
     }

     public void setSwatchName(String swatchName) {
         this.swatchName = swatchName;
     }

     public String getFromPredefined() {
         return fromPredefined;
     }

     public void setFromPredefined(String fromPredefined) {
         this.fromPredefined = fromPredefined;
     }

     public List<SwatchEntry> getPredefSwatchEntries() {
         return predefSwatchEntries;
     }

     public void setPredefSwatchEntries(List<SwatchEntry> predefSwatchEntries) {
         this.predefSwatchEntries = predefSwatchEntries;
     }

    public String getDynamic() {
        return dynamic;
    }

    public void setDynamic(String dynamic) {
        this.dynamic = dynamic;
    }

    public List<ColorEntryLayout> getSwatchLayoutLong() {
        return swatchLayoutLong;
    }

    public void setSwatchLayoutLong(List<ColorEntryLayout> swatchLayoutLong) {
        this.swatchLayoutLong = swatchLayoutLong;
    }
}
