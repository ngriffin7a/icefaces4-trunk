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
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

@ComponentExample(
        title = "Redirect Component",
        description = "The <b>&lt;icecore:redirect&gt;</b> component triggers a redirect immediately on page load, upon any user interaction causing a render of the tag, or at the server's discretion using push.",
        example = "/resources/examples/core/redirect.xhtml"
)

@ExampleResources(
        resources = {
                @ExampleResource(type = ResourceType.xhtml,
                        title="redirect.xhtml",
                        resource = "/resources/examples/core/redirect.xhtml"),
                @ExampleResource(type = ResourceType.java,
                        title="RedirectBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/core/RedirectBean.java")
        }
)
@Menu(
    title = "menu.core.redirectBean.subMenu.title", 
    menuLinks = {
        @MenuLink(title = "menu.core.redirectBean.subMenu.main", isDefault = true, exampleBeanName = RedirectBean.BEAN_NAME)
    }
)
@ManagedBean
@ViewScoped
public class RedirectBean extends ComponentExampleImpl<RedirectBean> implements Serializable {
    public static final String BEAN_NAME = "redirectBean";
    private boolean renderRedirect;

    public RedirectBean() {
        super(RedirectBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public boolean getRenderRedirect() {
        return renderRedirect;
    }

    public void setRenderRedirect(boolean renderRedirect) {
        this.renderRedirect = renderRedirect;
    }
}
