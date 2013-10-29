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
                    title="ace:cellEditor",
                    resource = ResourceRootPath.FOR_WIKI +"CellEditor"),
                @ExampleResource(type = ResourceType.wiki,
                    title="ace:column",
                    resource = ResourceRootPath.FOR_WIKI +"Column"),
                @ExampleResource(type = ResourceType.wiki,
                    title="ace:columnGroup",
                    resource = ResourceRootPath.FOR_WIKI +"ColumnGroup"),
                @ExampleResource(type = ResourceType.wiki,
                    title="ace:dataTable",
                    resource = ResourceRootPath.FOR_WIKI +"DataTable"),
                @ExampleResource(type = ResourceType.wiki,
                    title="ace:expansionToggler",
                    resource = ResourceRootPath.FOR_WIKI +"ExpansionToggler"),
                @ExampleResource(type = ResourceType.wiki,
                    title="ace:row",
                    resource = ResourceRootPath.FOR_WIKI +"Row"),
                @ExampleResource(type = ResourceType.wiki,
                    title="ace:rowEditor",
                    resource = ResourceRootPath.FOR_WIKI +"RowEditor"),
                @ExampleResource(type = ResourceType.wiki,
                    title="ace:rowExpansion",
                    resource = ResourceRootPath.FOR_WIKI +"RowExpansion"),
                @ExampleResource(type = ResourceType.wiki,
                    title="ace:tableConfigPanel",
                    resource = ResourceRootPath.FOR_WIKI +"TableConfigPanel"),
                
                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:cellEditor",
                    resource = ResourceRootPath.FOR_ACE_TLD + "cellEditor.html"),
                @ExampleResource(type = ResourceType.tld,
                    title="ace:column",
                    resource = ResourceRootPath.FOR_ACE_TLD + "column.html"),
                @ExampleResource(type = ResourceType.tld,
                    title="ace:columnGroup",
                    resource = ResourceRootPath.FOR_ACE_TLD + "columnGroup.html"),
                @ExampleResource(type = ResourceType.tld,
                    title="ace:dataTable",
                    resource = ResourceRootPath.FOR_ACE_TLD + "dataTable.html"),
                @ExampleResource(type = ResourceType.tld,
                    title="ace:expansionToggler",
                    resource = ResourceRootPath.FOR_ACE_TLD + "expansionToggler.html"),
               @ExampleResource(type = ResourceType.tld,
                    title="ace:row",
                    resource = ResourceRootPath.FOR_ACE_TLD + "row.html"),
               @ExampleResource(type = ResourceType.tld,
                    title="ace:rowEditor",
                    resource = ResourceRootPath.FOR_ACE_TLD + "rowEditor.html"),
               @ExampleResource(type = ResourceType.tld,
                    title="ace:rowExpansion",
                    resource = ResourceRootPath.FOR_ACE_TLD + "rowExpansion.html"),
               @ExampleResource(type = ResourceType.tld,
                    title="ace:tableConfigPanel",
                    resource = ResourceRootPath.FOR_ACE_TLD + "tableConfigPanel.html")
        }
)
@ManagedBean(name= DataTableResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableResources extends ComponentExampleImpl< DataTableResources > implements Serializable {
    public static final String BEAN_NAME = "dataTableResources";
    public DataTableResources()
    {
        super(DataTableResources.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}