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

package org.icefaces.samples.showcase.example.compat.paginator;

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
        parent = PaginatorBean.BEAN_NAME,
        title = "example.compat.paginator.max.title",
        description = "example.compat.paginator.max.description",
        example = "/resources/examples/compat/paginator/paginatorMax.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="paginatorMax.xhtml",
                    resource = "/resources/examples/compat/"+
                               "paginator/paginatorMax.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PaginatorMax.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/paginator/PaginatorMax.java")
        }
)
@ManagedBean(name= PaginatorMax.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PaginatorMax extends ComponentExampleImpl<PaginatorMax> implements Serializable {
	
	public static final String BEAN_NAME = "paginatorMax";
	
	private int rows = 3;
	private int pages = 2;
	
	public PaginatorMax() {
		super(PaginatorMax.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public int getRows() { return rows; }
	public int getPages() { return pages; }
	
	public void setRows(int rows) { this.rows = rows; }
	public void setPages(int pages) { this.pages = pages; }
}
