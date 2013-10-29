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
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;


@Component(
        tagName = "outputList",
        componentClass = "org.icefaces.ace.component.outputlist.OutputList",
        rendererClass = "org.icefaces.ace.component.outputlist.OutputListRenderer",
        generatedClass = "org.icefaces.ace.component.outputlist.OutputListBase",
        componentType = "org.icefaces.OutputList",
        rendererType = "org.icefaces.OutputListRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.OutputList",
        tlddoc = "outputList defines a grouping of unordered list items.  The child component "+
                "of this component must be outputListItem components. "
)


@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js"),
		@ResourceDependency(library = "org.icefaces.component.outputlist", name = "outputlist.css")
})
public class OutputListMeta extends UIComponentBaseMeta {
	
    @Property(defaultValue="false",
    		  tlddoc = "Determines if inset padding is applied around list group.")
    private boolean inset;

     @Property(tlddoc = "Sets the CSS style definition to be applied to this component.")
     private String style;

     @Property(tlddoc = "Sets the CSS class to apply to this component.")
     private String styleClass;

}
