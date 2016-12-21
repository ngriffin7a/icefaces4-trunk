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
import java.util.List;
import java.util.ArrayList;

import org.icefaces.ace.component.colorentry.ColorFormat;

@ManagedBean(name= ColorEntryOptionsBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ColorEntryOptionsBean implements Serializable
{
    public static final String BEAN_NAME = "colorEntryOptionsBean";
	public String getBeanName() { return BEAN_NAME; }
    private List<ColorFormat> colorFormats = new ArrayList<ColorFormat>();
    private ColorFormat colorFormat = ColorFormat.RGB;
    private String value;

    private String effect, duration;
    private boolean showAlpha,inline,showNoneButton;

    @PostConstruct
    public void init(){
        this.colorFormats.add(ColorFormat.HEX);
        this.colorFormats.add(ColorFormat.HEX3);
        this.colorFormats.add(ColorFormat.RGB);
        this.colorFormats.add(ColorFormat.RGBPERCENT);
        this.colorFormats.add(ColorFormat.HSLA);
        this.colorFormats.add(ColorFormat.HSLAPERCENT) ;
        this.colorFormats.add(ColorFormat.NAME);
    }
    public boolean isInline() {
        return inline;
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }

    public boolean isShowNoneButton() {
        return showNoneButton;
    }

    public void setShowNoneButton(boolean showNoneButton) {
        this.showNoneButton = showNoneButton;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isShowAlpha() {
        return showAlpha;
    }

    public void setShowAlpha(boolean showAlpha) {
        this.showAlpha = showAlpha;
    }

    public List<ColorFormat> getColorFormats(){
        return this.colorFormats;
    }
    public ColorFormat getColorFormat() {
        return colorFormat;
    }
    public void setColorFormat(ColorFormat colorFormat) {
          this.colorFormat = colorFormat;
      }
}
