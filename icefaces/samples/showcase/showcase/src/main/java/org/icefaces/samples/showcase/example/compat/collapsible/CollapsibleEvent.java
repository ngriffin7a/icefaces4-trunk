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

package org.icefaces.samples.showcase.example.compat.collapsible;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = CollapsibleBean.BEAN_NAME,
        title = "example.compat.collapsible.event.title",
        description = "example.compat.collapsible.event.description",
        example = "/resources/examples/compat/collapsible/collapsibleEvent.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="collapsibleEvent.xhtml",
                    resource = "/resources/examples/compat/"+
                               "collapsible/collapsibleEvent.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="CollapsibleEvent.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/collapsible/CollapsibleEvent.java")
        }
)
@ManagedBean(name= CollapsibleEvent.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CollapsibleEvent extends ComponentExampleImpl<CollapsibleEvent> implements Serializable {
	
	public static final String BEAN_NAME = "collapsibleEvent";
	private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	private Random randomizer = new Random(System.nanoTime());
	private String lastFired;
	private int lastNumber;
	
	public CollapsibleEvent() {
		super(CollapsibleEvent.class);
	}

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

                public String getLastFired() {
                    return lastFired;
                }

                public void setLastFired(String lastFired) {
                    this.lastFired = lastFired;
                }
	
	public void setLastNumber(int lastNumber) { this.lastNumber = lastNumber; }
        	public int getLastNumber() { return lastNumber; }
	
	public void actionListener(ActionEvent event) {
	    lastFired = formatter.format(new Date());
	    lastNumber = 1+randomizer.nextInt(99);
	}
}
