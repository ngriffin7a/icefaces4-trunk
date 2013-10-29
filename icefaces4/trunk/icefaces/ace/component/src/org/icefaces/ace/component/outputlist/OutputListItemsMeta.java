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

package org.icefaces.ace.component.outputlist;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UISeriesBaseMeta;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "outputListItems",
        componentClass = "org.icefaces.ace.component.outputlist.OutputListItems",
        rendererClass = "org.icefaces.ace.component.outputlist.OutputListItemsRenderer",
        generatedClass = "org.icefaces.ace.component.outputlist.OutputListItemsBase",
        componentType = "org.icefaces.OutputListItems",
        rendererType = "org.icefaces.OutputListItemsRenderer",
        extendsClass = "org.icefaces.impl.component.UISeriesBase",
        componentFamily = "org.icefaces.OutputListItems",
        tlddoc = "outputListItems is used within an outputList tag to group lists of items."
)

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js"),
		@ResourceDependency(library = "org.icefaces.component.outputlist", name = "outputlist.css")
})
public class OutputListItemsMeta extends UISeriesBaseMeta {

    @Property(defaultValue="false",
	      tlddoc = "Determines if inset padding is included around list group.")
    private boolean inset;

     @Property(tlddoc = "Sets the CSS style definition to be applied to this component.")
     private String style;

     @Property(tlddoc = "Sets the CSS class to apply to this component.")
     private String styleClass;
}

