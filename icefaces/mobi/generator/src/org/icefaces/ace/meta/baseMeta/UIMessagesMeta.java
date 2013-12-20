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

import org.icefaces.ace.meta.annotation.*;

/**
 * These are the properties for javax.faces.component.UIMessage TODO 1561
 */
public class UIMessagesMeta extends UIComponentBaseMeta {
    @Only(OnlyType.JSF)
    @Property(defaultValue="false",
        defaultValueType= DefaultValueType.EXPRESSION,
        implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="Flag indicating whether only global messages (that is, " +
            "messages with no associated client identifier) should be " +
            "rendered. Mutually exclusive with the 'for' property which " +
            "takes precedence." )
    private boolean globalOnly;

    @Only(OnlyType.JSF)
    @Property(
        name = "for",
        implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="Identifier of the component for which to render error " +
            "messages. If this component is within the same NamingContainer " +
            "as the target component, this must be the component " +
            "identifier. Otherwise, it must be an absolute component " +
            "identifier (starting with \":\").")
    private String forValue;

    
    
    @Only(OnlyType.JSF)
    @Property(defaultValue="false",
        defaultValueType= DefaultValueType.EXPRESSION,
        implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="Flag indicating whether the \"detail\" property of messages " +
            "for the specified component should be rendered.")
    private boolean showDetail;

    @Only(OnlyType.JSF)
    @Property(defaultValue="true",
        defaultValueType= DefaultValueType.EXPRESSION,
        implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="Flag indicating whether the \"summary\" property of " +
            "messages for the specified component should be rendered.")
    private boolean showSummary;
    
    //TODO Check Mojarra 2.0.3+ code to see if exists there
    @Only(OnlyType.JSF)
    @Property(defaultValue="true",
        defaultValueType= DefaultValueType.EXPRESSION,
        implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="A flag indicating this UIMessages instance should redisplay " +
            "FacesMessages that have already been handled.")
    private boolean redisplay;
}
