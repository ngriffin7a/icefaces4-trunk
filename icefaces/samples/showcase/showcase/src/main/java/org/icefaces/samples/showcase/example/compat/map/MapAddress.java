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

import org.icefaces.samples.showcase.util.FacesUtils;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = MapBean.BEAN_NAME,
        title = "example.compat.map.address.title",
        description = "example.compat.map.address.description",
        example = "/resources/examples/compat/map/mapAddress.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="mapAddress.xhtml",
                    resource = "/resources/examples/compat/"+
                               "map/mapAddress.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MapAddress.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/map/MapAddress.java")
        }
)
@ManagedBean(name= MapAddress.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MapAddress extends ComponentExampleImpl<MapAddress> implements Serializable 
{
    public static final String BEAN_NAME = "mapAddress";
    private String from;
    private String to;
    private boolean locateAddress = false;
    private boolean showDirections = false;

    public MapAddress() {
            super(MapAddress.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public void lookup(ActionEvent event) {
                if (FacesUtils.isBlank(from)) 
                {
                    if (!FacesUtils.isBlank(to)) {
                        from = new String(to);
                        to = null;
                    }
                    else 
                    {
                        from = MapBean.DEFAULT_ADDRESS;
                        to = null;
                    }
        }
        locateAddress = true;
        showDirections = !FacesUtils.isBlank(to);
    }
    
    public String getFrom() { return from; }
    public String getTo() { return to; }
    public boolean getLocateAddress() {
        if (locateAddress) {
            locateAddress = false;

            return true;
        }

        return locateAddress;
    }
    public boolean getShowDirections() { return showDirections; }
    public void setFrom(String from) { this.from = from; }
    public void setTo(String to) { this.to = to; }
    public void setLocateAddress(boolean locateAddress) { this.locateAddress = locateAddress; }
    public void setShowDirections(boolean showDirections) { this.showDirections = showDirections; }
}
