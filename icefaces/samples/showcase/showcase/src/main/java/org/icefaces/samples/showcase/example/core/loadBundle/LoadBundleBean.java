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

package org.icefaces.samples.showcase.example.core.loadBundle;

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
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.Locale;

@ComponentExample(
        title = "Load Bundle Component",
        description = "The <b>&lt;icecore:loadBundle&gt;</b> component allows the loading of a resource bundle to be used for locale specific messages. The defined bundle is completely dynamic, meaning that if any of its attribute change at any time there will be no need to reload the page, session or application.",
        example = "/resources/examples/core/load-bundle.xhtml"
)

@ExampleResources(
        resources = {
                @ExampleResource(type = ResourceType.xhtml,
                        title="loadBundle.xhtml",
                        resource = "/resources/examples/core/load-bundle.xhtml"),
                @ExampleResource(type = ResourceType.java,
                        title="LoadBundleBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/core/loadBundle/LoadBundleBean.java")
        }
)
@Menu(
    title = "menu.core.loadBundleBean.subMenu.title", 
    menuLinks = {
        @MenuLink(title = "menu.core.loadBundleBean.subMenu.main", isDefault = true, exampleBeanName = LoadBundleBean.BEAN_NAME)
    }
)
@ManagedBean(name = LoadBundleBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class LoadBundleBean extends ComponentExampleImpl<LoadBundleBean> implements Serializable {
    public static final String BEAN_NAME = "loadBundleBean";
    private String language = "en";

    public LoadBundleBean() {
        super(LoadBundleBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;

        UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
        if ("en".equals(language)) {
            viewRoot.setLocale(Locale.ENGLISH);
        } else if ("fr".equals(language)) {
            viewRoot.setLocale(Locale.FRENCH);
        } else if ("it".equals(language)) {
            viewRoot.setLocale(Locale.ITALIAN);
        }
    }
}
