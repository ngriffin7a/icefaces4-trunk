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

package org.icefaces.ace.meta.baseMeta;

import org.icefaces.ace.meta.annotation.Only;
import org.icefaces.ace.meta.annotation.OnlyType;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Implementation;

import javax.faces.convert.Converter;

/**
 * These are the properties for javax.faces.component.UIOutput
 */
public class UIOutputMeta extends UIComponentBaseMeta {

    @Only(OnlyType.JSF)
    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="The current value of the simple component. The value to be " +
            "rendered.")
    private Object value;

    @Only(OnlyType.JSF)
    @Property (implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="Converter is an interface describing a Java class that can " +
            "perform Object-to-String and String-to-Object conversions " +
            "between model data objects and a String representation of " +
            "those objects that is suitable for rendering.")
    private Converter converter;
}
