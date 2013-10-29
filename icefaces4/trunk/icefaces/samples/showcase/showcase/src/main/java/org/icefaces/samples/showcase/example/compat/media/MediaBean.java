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

package org.icefaces.samples.showcase.example.compat.media;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        title = "example.compat.media.title",
        description = "example.compat.media.description",
        example = "/resources/examples/compat/media/media.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="media.xhtml",
                    resource = "/resources/examples/compat/"+
                               "media/media.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MediaBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/media/MediaBean.java")
        }
)
@Menu(
	title = "menu.compat.media.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.media.subMenu.main",
                    isDefault = true,
                    exampleBeanName = MediaBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.media.subMenu.flash",
                    exampleBeanName = MediaFlash.BEAN_NAME),
            @MenuLink(title = "menu.compat.media.subMenu.quicktime",
                    exampleBeanName = MediaQuicktime.BEAN_NAME),
            @MenuLink(title = "menu.compat.media.subMenu.windows",
                    exampleBeanName = MediaWindows.BEAN_NAME),
            @MenuLink(title = "menu.compat.media.subMenu.real",
                    exampleBeanName = MediaReal.BEAN_NAME)
})
@ManagedBean(name= MediaBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MediaBean extends ComponentExampleImpl<MediaBean> implements Serializable {
	
	public static final String BEAN_NAME = "media";
	
	private static final String SOURCE_DIR = "resources/examples/compat/media/files";
	
	public MediaBean() {
		super(MediaBean.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public String getSourceDir() { return SOURCE_DIR; }
}
