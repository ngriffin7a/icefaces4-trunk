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

package org.icefaces.samples.showcase.example.compat.series;

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
        parent = SeriesBean.BEAN_NAME,
        title = "example.compat.series.first.title",
        description = "example.compat.series.first.description",
        example = "/resources/examples/compat/series/seriesFirst.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="seriesFirst.xhtml",
                    resource = "/resources/examples/compat/"+
                               "series/seriesFirst.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="SeriesFirst.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/series/SeriesFirst.java")
        }
)
@ManagedBean(name= SeriesFirst.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SeriesFirst extends ComponentExampleImpl<SeriesFirst> implements Serializable {
	
    public static final String BEAN_NAME = "seriesFirst";

    private int first = 0;

    public SeriesFirst() {
            super(SeriesFirst.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public int getFirst() { return first; }

    public void setFirst(int first) { this.first = first; }
}
