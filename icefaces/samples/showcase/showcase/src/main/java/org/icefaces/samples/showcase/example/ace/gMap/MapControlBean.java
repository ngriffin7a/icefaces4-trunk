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
import java.util.LinkedHashMap;
import java.util.Map;

@ComponentExample(
        parent = MapBean.BEAN_NAME,
        title = "example.ace.gMap.control.title",
        description = "example.ace.gMap.control.description",
        example = "/resources/examples/ace/gMap/gMapControl.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="gMapControl.xhtml",
                    resource = "/resources/examples/ace/gMap/gMapControl.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MapControlBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/gMap/MapControlBean.java")
        }
)
@ManagedBean(name= MapControlBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MapControlBean extends ComponentExampleImpl<MapControlBean> implements Serializable{
	public static final String BEAN_NAME = "controlBean";
	private String zoomStyle="DEFAULT";
    private String typeStyle="HORIZONTAL_BAR";
    private boolean type = false;
    private boolean overview = false;
    private boolean pan = false;
    private boolean rotate = false;
    private boolean scale = false;
    private boolean streetView = false;
    private boolean zoom = false;
    private String typeLocation="topRight";
    private String panLocation="topLeft";
    private String rotateLocation="topLeft";
    private String scaleLocation="bottomLeft";
    private String streetViewLocation="leftTop";
    private String zoomLocation="leftTop";

    private static Map<String,Object> positions;
    static{
        positions = new LinkedHashMap<String,Object>();
        positions.put("Top Left", "topLeft");
        positions.put("Top Center", "topCenter");
        positions.put("Top Right", "topRight");
        positions.put("Left Top", "leftTop");
        positions.put("Left Center", "leftCenter");
        positions.put("Left Bottom", "leftBottom");
        positions.put("Right Top", "rightTop");
        positions.put("Right Center", "rightCenter");
        positions.put("Right Bottom", "rightBottom");
        positions.put("Bottom Left", "bottomLeft");
        positions.put("Bottom Center", "bottomCenter");
        positions.put("Bottom Right", "bottomRight");
    }

    public Map<String,Object> getPositions() {
        return positions;
    }

    public String getZoomStyle() {
        return zoomStyle;
    }

    public void setZoomStyle(String zoomStyle) {
        this.zoomStyle = zoomStyle;
    }

    public String getTypeStyle() {
        return typeStyle;
    }

    public void setTypeStyle(String typeStyle) {
        this.typeStyle = typeStyle;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public boolean isOverview() {
        return overview;
    }

    public void setOverview(boolean overview) {
        this.overview = overview;
    }

    public boolean isPan() {
        return pan;
    }

    public void setPan(boolean pan) {
        this.pan = pan;
    }

    public boolean isRotate() {
        return rotate;
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
    }

    public boolean isScale() {
        return scale;
    }

    public void setScale(boolean scale) {
        this.scale = scale;
    }

    public boolean isStreetView() {
        return streetView;
    }

    public void setStreetView(boolean streetView) {
        this.streetView = streetView;
    }

    public boolean isZoom() {
        return zoom;
    }

    public void setZoom(boolean zoom) {
        this.zoom = zoom;
    }

    public String getTypeLocation() {
        return typeLocation;
    }

    public void setTypeLocation(String typeLocation) {
        this.typeLocation = typeLocation;
    }

    public String getPanLocation() {
        return panLocation;
    }

    public void setPanLocation(String panLocation) {
        this.panLocation = panLocation;
    }

    public String getRotateLocation() {
        return rotateLocation;
    }

    public void setRotateLocation(String rotateLocation) {
        this.rotateLocation = rotateLocation;
    }

    public String getScaleLocation() {
        return scaleLocation;
    }

    public void setScaleLocation(String scaleLocation) {
        this.scaleLocation = scaleLocation;
    }

    public String getStreetViewLocation() {
        return streetViewLocation;
    }

    public void setStreetViewLocation(String streetViewLocation) {
        this.streetViewLocation = streetViewLocation;
    }

    public String getZoomLocation() {
        return zoomLocation;
    }

    public void setZoomLocation(String zoomLocation) {
        this.zoomLocation = zoomLocation;
    }
	public MapControlBean() {
        super(MapControlBean.class);
    }
	@PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}