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

import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ExampleResources(
        resources = {
                @ExampleResource(type = ResourceType.tld,
                        title = "icecore:navigationNotifier",
                        resource = "http://res.icesoft.org/docs/v4_latest/core/comps/tld/icecore/navigationNotifier.html"),
                @ExampleResource(type = ResourceType.wiki,
                        title = "icecore:navigationNotifier",
                        resource = "http://www.icesoft.org/wiki/display/ICE/navigationNotifier")
        }
)
@ManagedBean
@ApplicationScoped
public class NavigationNotifierBeanResources extends ComponentExampleImpl<NavigationNotifierBeanResources> implements Serializable {
    public NavigationNotifierBeanResources() {
        super(NavigationNotifierBeanResources.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
