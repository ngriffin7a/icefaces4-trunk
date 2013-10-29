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

package org.icefaces.samples.showcase.example.compat.exporter;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = ExporterBean.BEAN_NAME,
        title = "example.compat.exporter.type.title",
        description = "example.compat.exporter.type.description",
        example = "/resources/examples/compat/exporter/exporterType.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="exporterType.xhtml",
                    resource = "/resources/examples/compat/"+
                               "exporter/exporterType.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ExporterType.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/exporter/ExporterType.java")
        }
)
@ManagedBean(name= ExporterType.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ExporterType extends ComponentExampleImpl<ExporterType> implements Serializable {
	
	public static final String BEAN_NAME = "exporterType";
	
	private static final String TYPE_EXCEL = "excel";
	private static final String TYPE_CSV = "csv";
    private static final String TYPE_PDF = "pdf";

	private SelectItem[] availableTypes = new SelectItem[] {
	    new SelectItem(TYPE_EXCEL, "Excel"),
	    new SelectItem(TYPE_CSV, "CSV"),
        new SelectItem(TYPE_PDF, "PDF")
	};
	private String type = availableTypes[0].getValue().toString();
	
	public ExporterType() {
		super(ExporterType.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public SelectItem[] getAvailableTypes() { return availableTypes; }
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }
    public String getTypeLabel() {
        for (SelectItem si : availableTypes) {
            if (si.getValue().toString().equals(type)) {
                return si.getLabel();
            }
        }
        return type;
    }
}
