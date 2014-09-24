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

package org.icefaces.samples.showcase.example.core.refresh;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.samples.showcase.metadata.annotation.Menu;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.CustomScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@ComponentExample(
        title = "Refresh Component",
        description = "The <b>&lt;icecore:refresh&gt;</b> component enables a view to be periodically refreshed via ajax without requiring a push connection.",
        example = "/resources/examples/core/refresh.xhtml"
)

@ExampleResources(
        resources = {
                @ExampleResource(type = ResourceType.xhtml,
                        title="refresh.xhtml",
                        resource = "/resources/examples/core/refresh.xhtml"),
                @ExampleResource(type = ResourceType.java,
                        title="RefreshBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/core/refresh/RefreshBean.java")
        }
)
@Menu(
    title = "menu.core.refreshBean.subMenu.title", 
    menuLinks = {
        @MenuLink(title = "menu.core.refreshBean.subMenu.main", isDefault = true, exampleBeanName = RefreshBean.BEAN_NAME)
    }
)
@ManagedBean(name = RefreshBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class RefreshBean extends ComponentExampleImpl<RefreshBean> implements Serializable {
    public static final String BEAN_NAME = "refreshBean";
	public String getBeanName() { return BEAN_NAME; }

    private static SimpleDateFormat FORMATTER = new SimpleDateFormat("hh:mm:ss");
    private int interval = 2;
    private int duration = 1;

    public RefreshBean() {
        super(RefreshBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getCurrentTime() {
        return FORMATTER.format(new Date());
    }
}
