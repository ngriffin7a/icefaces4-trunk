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

package org.icefaces.samples.showcase.example.core;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

@ComponentExample(
        title = "Focus Manager Component",
        description = "The <b>&lt;icecore:focusManager&gt;</b> component manages where focus will be applied on page load. The focus will move to the first invalid component, in this case when the required field is not filled in.",
        example = "/resources/examples/core/focus-manager.xhtml"
)

@ExampleResources(
        resources = {
                @ExampleResource(type = ResourceType.xhtml,
                        title="focus-manager.xhtml",
                        resource = "/resources/examples/core/focus-manager.xhtml"),
                @ExampleResource(type = ResourceType.java,
                        title="FocusManagerBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/core/FocusManagerBean.java")
        }
)
@Menu(
    title = "menu.core.focusManagerBean.subMenu.title", 
    menuLinks = {
        @MenuLink(title = "menu.core.focusManagerBean.subMenu.main", isDefault = true, exampleBeanName = FocusManagerBean.BEAN_NAME)
    }
)
@ManagedBean
@CustomScoped(value = "#{window}")
public class FocusManagerBean extends ComponentExampleImpl<FocusManagerBean> implements Serializable {
    public static final String BEAN_NAME = "focusManagerBean";
    private String focusedComponent = "";
    private String a, b, c, d = "";

    public FocusManagerBean() {
        super(FocusManagerBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getFocusedComponent() {
        return focusedComponent;
    }

    public void setFocusedComponent(String focusedComponent) {
        this.focusedComponent = focusedComponent;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }
}
