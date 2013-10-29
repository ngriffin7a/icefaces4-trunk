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
        title = "example.ace.gMap.overlay.title",
        description = "example.ace.gMap.overlay.description",
        example = "/resources/examples/ace/gMap/gMapOverlay.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="gMapOverlay.xhtml",
                    resource = "/resources/examples/ace/gMap/gMapOverlay.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MapOverlayBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/gMap/MapOverlayBean.java")
        }
)
@ManagedBean(name= MapOverlayBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MapOverlayBean extends ComponentExampleImpl<MapOverlayBean> implements Serializable{
	public static final String BEAN_NAME = "overlayBean";
    private String shape="polygon";
    private String points="(0,0):(30,20):(0,40)";
    private String options="editable:true, strokeColor:'navy'";

	public MapOverlayBean() {
        super(MapOverlayBean.class);
    }
	
    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
	@PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
