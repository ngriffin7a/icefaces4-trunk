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

package org.icefaces.samples.showcase.example.mobi.fieldset;

//import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
//import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

/**
 *
 *//*
@Destination(
        title = "example.layout.fieldset.destination.title.short",
        titleExt = "example.layout.fieldset.destination.title.long",
        titleBack = "example.layout.fieldset.destination.title.back"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/layout/fieldset-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/layout/fieldset-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "fieldset-example.xhtml",
                        resource = "/WEB-INF/includes/examples/layout/fieldset-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "FieldsetBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/layout/fieldset/FieldsetBean.java")
        }
)*/
@ManagedBean(name = FieldsetBean.BEAN_NAME)
@SessionScoped
public class FieldsetBean /*extends ExampleImpl<FieldsetBean>*/ implements
        Serializable {

    public static final String BEAN_NAME = "fieldsetBean";

    public FieldsetBean() {
        //super(FieldsetBean.class);
    }

}