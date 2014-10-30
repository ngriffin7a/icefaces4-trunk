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

package org.icefaces.samples.showcase.example.ace.gMapAutoComplete;

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
        title = "example.ace.gMapAutoComplete.title",
        description = "example.ace.gMapAutoComplete.description",
        example = "/resources/examples/ace/gMapAutoComplete/gMapAutoComplete.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="gMapAutoComplete.xhtml",
                    resource = "/resources/examples/ace/gMapAutoComplete/gMapAutoComplete.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="GMapAutoCompleteBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/gMapAutoComplete/GMapAutoCompleteBean.java")
        }
)
@Menu(
    title = "menu.ace.gMapAutoComplete.subMenu.title", 
    menuLinks = {
        @MenuLink(title = "menu.ace.gMapAutoComplete.subMenu.main", isDefault = true, exampleBeanName = GMapAutoCompleteBean.BEAN_NAME)
    }
)
@ManagedBean(name= GMapAutoCompleteBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class GMapAutoCompleteBean extends ComponentExampleImpl<GMapAutoCompleteBean> implements Serializable{
	public static final String BEAN_NAME = "gMapAutoCompleteBean";
	public String getBeanName() { return BEAN_NAME; }
	private boolean showWindow = false;
    private String address,types;
	private String url = "https://maps.google.com/maps/place";

    public boolean isShowWindow() {
        return showWindow;
    }

    public void setShowWindow(boolean showWindow) {
        this.showWindow = showWindow;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
	
	public GMapAutoCompleteBean() {
        super(GMapAutoCompleteBean.class);
    }
	@PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
