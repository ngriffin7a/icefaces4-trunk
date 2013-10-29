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

import javax.el.MethodExpression;

/**
 * These are the properties for javax.faces.component.UICommand
 */
public class UICommandMeta extends UIComponentBaseMeta {
    @Only(OnlyType.JSF)
    @Property(expression= Expression.METHOD_EXPRESSION,
        implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="MethodExpression representing the application action to " +
            "invoke when this component is activated by the user. The " +
            "expression must evaluate to a public method that takes no " +
            "parameters, and returns an Object (the toString() of which is " +
            "called to derive the logical outcome) which is passed to the " +
            "NavigationHandler for this application.")
    private MethodExpression action;

    @Only(OnlyType.JSF)
    @Property (expression=Expression.METHOD_EXPRESSION,
        methodExpressionArgument="javax.faces.event.ActionEvent",
        implementation=Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="MethodExpression representing an action listener method that " +
            "will be notified when this component is activated by the user. " +
            "The expression must evaluate to a public method that takes an " +
            "ActionEvent parameter, with a return type of void, or to a " +
            "public method that takes no arguments with a return type of " +
            "void. In the latter case, the method has no way of easily " +
            "knowing where the event came from, but this can be useful in " +
            "cases where a notification is needed that \"some action happened\".")
    private MethodExpression actionListener;
    
    @Only(OnlyType.JSF)
    @Property (defaultValue="false",
        defaultValueType= DefaultValueType.EXPRESSION,
        implementation=Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="Flag indicating that, if this component is activated by " +
            "the user, notifications should be delivered to interested " +
            "listeners and actions immediately (that is, during Apply " +
            "Request Values phase) rather than waiting until Invoke " +
            "Application phase.")
    private boolean immediate;
    
    @Only(OnlyType.JSF)
    @Property (implementation=Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="The current value of the simple component.")
    private Object value;
}
