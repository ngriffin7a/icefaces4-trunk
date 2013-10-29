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
 * These are the properties for javax.faces.component.UIInput
 */
public class UIInputMeta extends UIOutputMeta {
    @Only(OnlyType.JSF)
    @Property(defaultValue="false",
        defaultValueType= DefaultValueType.EXPRESSION,
        implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="A flag indicating that conversion and validation of this " +
            "component's value should occur during Apply Request Values " +
            "phase instead of Process Validations phase.")
    private boolean immediate;
    
    @Only(OnlyType.JSF)
    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="If present, will be used as the text of the converter " +
            "message, replacing any message that comes from the converter.")
    private String converterMessage;
    
    @Only(OnlyType.JSF)
    @Property(defaultValue="false",
        defaultValueType= DefaultValueType.EXPRESSION,
        implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="A flag indicating whether the user required to provide a " +
            "non-empty submitted value for this component.")
    private boolean required;
    
    @Only(OnlyType.JSF)
    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="If present, will be used as the text of the validation " +
            "message for the \"required\" facility, if the \"required\" " +
            "facility is used.")
    private String requiredMessage;
    
    @Only(OnlyType.JSF)
    @Property (expression= Expression.METHOD_EXPRESSION,
        methodExpressionArgument="", //TODO ICE-6114
        // void validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
        implementation=Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="MethodExpression representing a validator method that will " +
            "be called during Process Validations to perform correctness " +
            "checks on the value of this component. The expression must " +
            "evaluate to a public method that takes FacesContext, " +
            "UIComponent, and Object parameters, with a return type of void.")
    private MethodExpression validator;
    
    @Only(OnlyType.JSF)
    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="If present, will be used as the text of the validator " +
            "message, replacing any message that comes from the validator.")
    private String validatorMessage;
    
    @Only(OnlyType.JSF)
    @Property (expression= Expression.METHOD_EXPRESSION,
        methodExpressionArgument="javax.faces.event.ValueChangeEvent",
        implementation=Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="MethodExpression representing a value change listener " +
            "method that will be notified when a new value has been set for " +
            "this input component. The expression must evaluate to a public " +
            "method that takes a <code>ValueChangeEvent</code> parameter, " +
            "with a return type of void, or to a public method that takes " +
            "no arguments with a return type of void. In the latter case, " +
            "the method has no way of easily knowing what the new value is, " +
            "but this can be useful in cases where a notification is needed " +
            "that \"this value changed\".")
    private MethodExpression valueChangeListener;
}
