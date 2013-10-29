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

package org.icefaces.samples.showcase.example.ace.contextMenu;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        parent = ContextMenuBean.BEAN_NAME,
        title = "example.ace.contextMenu.delegate.title",
        description = "example.ace.contextMenu.delegate.description",
        example = "/resources/examples/ace/contextMenu/contextMenuDelegate.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="contextMenuDelegate.xhtml",
                    resource = "/resources/examples/ace/contextMenu/contextMenuDelegate.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ContextMenuDelegate.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/contextMenu/ContextMenuDelegate.java"),
            @ExampleResource(type = ResourceType.java,
                    title="DataTableDynamic.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/compat/dataTable/DataTableDynamic.java")
        }
)
@ManagedBean(name= ContextMenuDelegate.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ContextMenuDelegate extends ComponentExampleImpl<ContextMenuDelegate> implements Serializable {
    public static final String BEAN_NAME = "contextMenuDelegate";
    
    public ContextMenuDelegate() {
        super(ContextMenuDelegate.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
	
	private Object data;
	
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
