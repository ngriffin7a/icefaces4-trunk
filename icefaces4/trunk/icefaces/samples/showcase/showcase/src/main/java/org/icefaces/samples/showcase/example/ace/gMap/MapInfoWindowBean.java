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

package org.icefaces.samples.showcase.example.ace.gMap;

import javax.el.MethodExpression;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.CustomScoped;
import javax.annotation.PostConstruct;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import java.io.Serializable;

@ComponentExample(
        parent = MapBean.BEAN_NAME,
        title = "example.ace.gMap.infowindow.title",
        description = "example.ace.gMap.infowindow.description",
        example = "/resources/examples/ace/gMap/gMapInfoWindow.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="gMapInfoWindow.xhtml",
                    resource = "/resources/examples/ace/gMap/gMapInfoWindow.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MapInfoWindowBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/gMap/MapInfoWindowBean.java")
        }
)
@ManagedBean(name= MapInfoWindowBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MapInfoWindowBean extends ComponentExampleImpl<MapInfoWindowBean> implements Serializable{
	public static final String BEAN_NAME = "infoWindowBean";
	private boolean oneDisabled = false;
    private boolean twoDisabled = true;
    private boolean threeDisabled = true;
    private boolean fourDisabled = true;
    private boolean fiveDisabled = true;
    private boolean sixDisabled = true;
    private boolean sevenDisabled = true;
    private String oneContent = "This infoWindow stands on its own, and uses the content attribute.";
    private String twoContent = "This infoWindow stands on its own, and nests its content.";
    private String threeContent = "This infoWindow is bound to a marker, will re-appear when the marker is clicked, and starts open.";
    private String fourContent = "This infoWindow is bound to a marker, will re-appear when the marker is clicked, and starts closed.";
    private String fiveContent = "This infoWindow is bound to a marker and starts open, but will not reappear on a click.";
    private String sixContent = "Images can be loaded into infoWindows, but it must be done prior to the page loading. Changing this text will have no effect upon the infoWindow.";
    private String sevenContent = "Other Icefaces components can be nested within infoWindows, but it must be done prior to page loading. Changing this text will have no effect upon the infoWindow.";
    private String selectedRender = "one";
    private String displayedValue = "This infoWindow stands on its own, and uses the content attribute.";

    public boolean isOneDisabled() {
        return oneDisabled;
    }

    public void setOneDisabled(boolean oneDisabled) {
        this.oneDisabled = oneDisabled;
    }

    public boolean isTwoDisabled() {
        return twoDisabled;
    }

    public void setTwoDisabled(boolean twoDisabled) {
        this.twoDisabled = twoDisabled;
    }

    public boolean isThreeDisabled() {
        return threeDisabled;
    }

    public void setThreeDisabled(boolean threeDisabled) {
        this.threeDisabled = threeDisabled;
    }

    public boolean isFourDisabled() {
        return fourDisabled;
    }

    public void setFourDisabled(boolean fourDisabled) {
        this.fourDisabled = fourDisabled;
    }

    public boolean isFiveDisabled() {
        return fiveDisabled;
    }

    public void setFiveDisabled(boolean fiveDisabled) {
        this.fiveDisabled = fiveDisabled;
    }

    public boolean isSixDisabled() {
        return sixDisabled;
    }

    public void setSixDisabled(boolean sixDisabled) {
        this.sixDisabled = sixDisabled;
    }

    public boolean isSevenDisabled() {
        return sevenDisabled;
    }

    public void setSevenDisabled(boolean sevenDisabled) {
        this.sevenDisabled = sevenDisabled;
    }

    public String getOneContent() {
        return oneContent;
    }

    public void setOneContent(String oneContent) {
        this.oneContent = oneContent;
    }

    public String getTwoContent() {
        return twoContent;
    }

    public void setTwoContent(String twoContent) {
        this.twoContent = twoContent;
    }

    public String getThreeContent() {
        return threeContent;
    }

    public void setThreeContent(String threeContent) {
        this.threeContent = threeContent;
    }

    public String getFourContent() {
        return fourContent;
    }

    public void setFourContent(String fourContent) {
        this.fourContent = fourContent;
    }

    public String getFiveContent() {
        return fiveContent;
    }

    public void setFiveContent(String fiveContent) {
        this.fiveContent = fiveContent;
    }

    public String getSixContent() {
        return sixContent;
    }

    public void setSixContent(String sixContent) {
        this.sixContent = sixContent;
    }

    public String getSevenContent() {
        return sevenContent;
    }

    public void setSevenContent(String sevenContent) {
        this.sevenContent = sevenContent;
    }

    public String getSelectedRender() {
        return selectedRender;
    }

    public void setSelectedRender(String selectedRender) {
        this.selectedRender = selectedRender;
        oneDisabled = true;
        twoDisabled = true;
        threeDisabled = true;
        fourDisabled = true;
        fiveDisabled = true;
        sixDisabled = true;
        sevenDisabled = true;
        if (selectedRender.equals("one")) {
            oneDisabled = false;
            displayedValue = oneContent;
        } else if (selectedRender.equals("two")) {
            twoDisabled = false;
            displayedValue = twoContent;
        } else if (selectedRender.equals("three")) {
            threeDisabled = false;
            displayedValue = threeContent;
        } else if (selectedRender.equals("four")) {
            fourDisabled = false;
            displayedValue = fourContent;
        } else if (selectedRender.equals("five")) {
            fiveDisabled = false;
            displayedValue = fiveContent;
        } else if (selectedRender.equals("six")) {
            sixDisabled = false;
            displayedValue = sixContent;
        } else if (selectedRender.equals("seven")) {
            sevenDisabled = false;
            displayedValue = sevenContent;
        }
    }

    public String getDisplayedValue() {
        return displayedValue;
    }

    public void setDisplayedValue(String displayedValue) {
        this.displayedValue = displayedValue;
        if (selectedRender.equals("one")) {
            oneContent = displayedValue;
        } else if (selectedRender.equals("two")) {
            twoContent = displayedValue;
        } else if (selectedRender.equals("three")) {
            threeContent = displayedValue;
        } else if (selectedRender.equals("four")) {
            fourContent = displayedValue;
        } else if (selectedRender.equals("five")) {
           fiveContent = displayedValue;
        } else if (selectedRender.equals("six")) {
            sixContent = displayedValue;
        } else if (selectedRender.equals("seven")) {
           sevenContent = displayedValue;
        }
    }
	public MapInfoWindowBean() {
        super(MapInfoWindowBean.class);
    }
	@PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}