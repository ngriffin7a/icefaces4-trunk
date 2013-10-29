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
import javax.faces.event.ActionEvent;

import org.icefaces.samples.showcase.view.navigation.NavigationController;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = MapBean.BEAN_NAME,
        title = "example.compat.map.type.title",
        description = "example.compat.map.type.description",
        example = "/resources/examples/compat/map/mapType.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="mapType.xhtml",
                    resource = "/resources/examples/compat/"+
                               "map/mapType.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MapType.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/map/MapType.java")
        }
)
@ManagedBean(name= MapType.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MapType extends ComponentExampleImpl<MapType> implements Serializable {
	
	public static final String BEAN_NAME = "mapType";
	
	private String[] availableTypes = new String[] {
	    "Map",
	    "Satellite",
	    "Hybrid"
	};
	private String type = availableTypes[1];

	public MapType() {
		super(MapType.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public String[] getAvailableTypes() { return availableTypes; }
	public String getType() { return type; }
	
	public void setType(String type) { this.type = type; }
}
