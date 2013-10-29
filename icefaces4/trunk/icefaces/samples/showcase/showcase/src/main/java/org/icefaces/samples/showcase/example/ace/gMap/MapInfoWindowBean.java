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
import javax.faces.application.*;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.util.ArrayList;
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
	private boolean oneRendered = true;
    private boolean twoRendered = false;
    private boolean threeRendered = false;
    private boolean fourRendered = false;
    private boolean fiveRendered = false;
    private boolean sixRendered = false;
    private boolean sevenRendered = false;
    private String oneContent = "This infoWindow stands on its own, and uses the content attribute.";
    private String twoContent = "This infoWindow stands on its own, and nests its content.";
    private String threeContent = "This infoWindow is bound to a marker, will re-appear when the marker is clicked, and starts open.";
    private String fourContent = "This infoWindow is bound to a marker, will re-appear when the marker is clicked, and starts closed.";
    private String fiveContent = "This infoWindow is bound to a marker and starts open, but will not reappear on a click.";
    private String sixContent = "Images can be loaded into infoWindows, but it must be done prior to the page loading. Changing this text will have no effect upon the infoWindow.";
    private String sevenContent = "Other Icefaces components can be nested within infoWindows, but it must be done prior to page loading. Changing this text will have no effect upon the infoWindow.";
    private String selectedRender = "one";
    private String displayedValue = "This infoWindow stands on its own, and uses the content attribute.";

    public boolean isOneRendered() {
        return oneRendered;
    }

    public void setOneRendered(boolean oneRendered) {
        this.oneRendered = oneRendered;
    }

    public boolean isTwoRendered() {
        return twoRendered;
    }

    public void setTwoRendered(boolean twoRendered) {
        this.twoRendered = twoRendered;
    }

    public boolean isThreeRendered() {
        return threeRendered;
    }

    public void setThreeRendered(boolean threeRendered) {
        this.threeRendered = threeRendered;
    }

    public boolean isFourRendered() {
        return fourRendered;
    }

    public void setFourRendered(boolean fourRendered) {
        this.fourRendered = fourRendered;
    }

    public boolean isFiveRendered() {
        return fiveRendered;
    }

    public void setFiveRendered(boolean fiveRendered) {
        this.fiveRendered = fiveRendered;
    }

    public boolean isSixRendered() {
        return sixRendered;
    }

    public void setSixRendered(boolean sixRendered) {
        this.sixRendered = sixRendered;
    }

    public boolean isSevenRendered() {
        return sevenRendered;
    }

    public void setSevenRendered(boolean sevenRendered) {
        this.sevenRendered = sevenRendered;
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
        oneRendered = false;
        twoRendered = false;
        threeRendered = false;
        fourRendered = false;
        fiveRendered = false;
        sixRendered = false;
        sevenRendered = false;
        if (selectedRender.equals("one")) {
            oneRendered = true;
            displayedValue = oneContent;
        } else if (selectedRender.equals("two")) {
            twoRendered = true;
            displayedValue = twoContent;
        } else if (selectedRender.equals("three")) {
            threeRendered = true;
            displayedValue = threeContent;
        } else if (selectedRender.equals("four")) {
            fourRendered = true;
            displayedValue = fourContent;
        } else if (selectedRender.equals("five")) {
            fiveRendered = true;
            displayedValue = fiveContent;
        } else if (selectedRender.equals("six")) {
            sixRendered = true;
            displayedValue = sixContent;
        } else if (selectedRender.equals("seven")) {
            sevenRendered = true;
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