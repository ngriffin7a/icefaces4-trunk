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
        title = "example.ace.gMap.event.title",
        description = "example.ace.gMap.event.description",
        example = "/resources/examples/ace/gMap/gMapEvent.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="gMapEvent.xhtml",
                    resource = "/resources/examples/ace/gMap/gMapEvent.xhtml")
        }
)
@ManagedBean(name= MapEventBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MapEventBean extends ComponentExampleImpl<MapEventBean> implements Serializable{
	public static final String BEAN_NAME = "eventBean";

	public MapEventBean() {
        super(MapEventBean.class);
    }
	@PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}