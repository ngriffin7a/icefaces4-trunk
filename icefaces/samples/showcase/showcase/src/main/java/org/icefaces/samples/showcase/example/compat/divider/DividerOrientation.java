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

package org.icefaces.samples.showcase.example.compat.divider;

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
        parent = DividerBean.BEAN_NAME,
        title = "example.compat.divider.orientation.title",
        description = "example.compat.divider.orientation.description",
        example = "/resources/examples/compat/divider/dividerOrientation.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dividerOrientation.xhtml",
                    resource = "/resources/examples/compat/"+
                               "divider/dividerOrientation.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DividerOrientation.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/divider/DividerOrientation.java")
        }
)
@ManagedBean(name= DividerOrientation.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DividerOrientation extends ComponentExampleImpl<DividerOrientation> implements Serializable {
	
	public static final String BEAN_NAME = "dividerOrientation";
	
	public static final String ORIENTATION_HOR = "horizontal";
	public static final String ORIENTATION_VER = "vertical";
	
	private String orientation = ORIENTATION_HOR;
	
	public DividerOrientation() {
		super(DividerOrientation.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public String getOrientation() { return orientation; }
	public String getOrientationHor() { return ORIENTATION_HOR; }
	public String getOrientationVer() { return ORIENTATION_VER; }
	
	public void setOrientation(String orientation) { this.orientation = orientation; }
}
