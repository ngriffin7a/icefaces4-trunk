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

package org.icefaces.samples.showcase.ace.documentationResources;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import org.icefaces.samples.showcase.metadata.context.ResourceRootPath;

@ExampleResources(
        resources ={
                
                // WIKI Resources
                @ExampleResource(type = ResourceType.wiki,
                    title="ace:dateTimeEntry",
                    resource = ResourceRootPath.FOR_WIKI + "DateTimeEntry"),

                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:dateTimeEntry",
                    resource = ResourceRootPath.FOR_ACE_TLD + "dateTimeEntry.html")
                
        }
)
@ManagedBean(name= DateTimeEntryResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DateTimeEntryResources extends ComponentExampleImpl<DateTimeEntryResources> implements Serializable {
    public static final String BEAN_NAME = "dateTimeEntryResources";
    public DateTimeEntryResources()
    {
        super(DateTimeEntryResources.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}