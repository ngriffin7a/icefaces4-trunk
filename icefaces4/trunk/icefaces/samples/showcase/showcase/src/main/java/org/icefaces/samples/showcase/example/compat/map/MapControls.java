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

package org.icefaces.samples.showcase.example.compat.map;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = MapBean.BEAN_NAME,
        title = "example.compat.map.controls.title",
        description = "example.compat.map.controls.description",
        example = "/resources/examples/compat/map/mapControls.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="mapControls.xhtml",
                    resource = "/resources/examples/compat/"+
                               "map/mapControls.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MapControls.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/map/MapControls.java")
        }
)
@ManagedBean(name= MapControls.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MapControls extends ComponentExampleImpl<MapControls> implements Serializable {
	
	public static final String BEAN_NAME = "mapControls";
	
	private boolean smallMap = false;
	private boolean largeMap = true;
	private boolean zoom = false;
	private boolean scale = true;
	private boolean type = false;
	private boolean overview = false;
	
	public MapControls() {
		super(MapControls.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public boolean getSmallMap() { return smallMap; }
	public boolean getLargeMap() { return largeMap; }
	public boolean getZoom() { return zoom; }
	public boolean getScale() { return scale; }
	public boolean getType() { return type; }
	public boolean getOverview() { return overview; }
	
	public void setSmallMap(boolean smallMap) { this.smallMap = smallMap; }
	public void setLargeMap(boolean largeMap) { this.largeMap = largeMap; }
	public void setZoom(boolean zoom) { this.zoom = zoom; }
	public void setScale(boolean scale) { this.scale = scale; }
	public void setType(boolean type) { this.type = type; }
	public void setOverview(boolean overview) { this.overview = overview; }
}
