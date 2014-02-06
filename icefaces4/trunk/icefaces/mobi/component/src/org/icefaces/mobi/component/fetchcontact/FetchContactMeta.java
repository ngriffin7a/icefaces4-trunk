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

package org.icefaces.mobi.component.fetchcontact;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;

@Component(
        tagName = "fetchContact",
        componentClass = "org.icefaces.mobi.component.fetchcontact.FetchContact",
        rendererClass = "org.icefaces.mobi.component.fetchcontact.FetchContactRenderer",
        generatedClass = "org.icefaces.mobi.component.fetchcontact.FetchContactBase",
        componentType = "org.icefaces.FetchContact",
        rendererType = "org.icefaces.FetchContactRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.FetchContact",
        tlddoc = "Renders a button that can access the device contact list and allow selection and retrieval " +
        		"of a contact. "
)
@ResourceDependencies({
        @ResourceDependency(library = "icefaces.mobi", name = "core/bridgeit.js"),
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class FetchContactMeta {
    
    @Property(tlddoc = "The button label. ", defaultValue="Fetch Contact")
    private String buttonLabel;
    
    @Property(defaultValue = "name, email, phone", tlddoc = "Determines which contact fields are retrieved.  Can contain one or more of 'name', 'email', and 'phone' fields submitted contained in a comma-separated string. ")
    private String fields;
    
    @Property(defaultValue = "false", 
            tlddoc = org.icefaces.mobi.util.TLDConstants.DISABLED)
    private boolean disabled;

    @Property(tlddoc = org.icefaces.mobi.util.TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = org.icefaces.mobi.util.TLDConstants.STYLECLASS)
    private String styleClass;
    
    @Property(tlddoc = "The encoded result containing all fields.")
    private String value;
    
    @Property(tlddoc = "The name of the selected contact.")
    private String name;
    
    @Property(tlddoc = "The phone number of the selected contact.")
    private String phone;
    
    @Property(tlddoc = "The email of the selected contact.")
    private String email;
}
