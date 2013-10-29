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

package org.icefaces.ace.meta.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Property annotation allows to add JSF managed properties to the generated component. All
 * fields are optional on this annotation.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {
    final String Null = "null";
    
    /**
     * If generated property is a method expression, then this field should be set to Expresion.METHOD_EXPRESSION. (e.g.)
	 * <pre>
	 * @Property(expression=Expression.METHOD_EXPRESSION, methodExpressionArgument="javax.faces.event.ValueChangeEvent")
	 * public MethodExpression valueChangeListener;
	 * </pre>
     * @return boolean value.
     */
    Expression expression() default Expression.UNSET;
    
    /**
     * Allows to define method expression argument if any.
     * @return fully qualified name of the argument class.
     */
    String methodExpressionArgument() default Null;
    
    /**
     * Default value of the property.
     * @return default value.
     */
    String defaultValue() default Null;
    
    /**
     * By default the value being assigned to the property as string  literal (e.g.)
     * <pre>
     * @Property (value="Car")
     * String type;
     * 
     * The generated property would look something like this:
     * 
     * String type = "Car";
     * 
     * But what if you want to define some other type than string or a constant, expression etc. You don't want
     * value to be quoted in that case. So you would set this attribute to DefaultValueType.EXPRESSION. (e.g)
     * 
     * @Property (value="10", defaultValueType=DefaultValueType.EXPRESSION)
     * Integer count;      
     * </pre>
     * @return 
     */    
    DefaultValueType defaultValueType() default DefaultValueType.UNSET;

    /**
     * Allow the user to specify a 'name' for the property that is different
     * from the field name. This allows a property 'for' or 'while' which are
     * Java keywords.
     * @return optional name of the field
     */
    String name() default Null; 
    
    /**
     * TLDDoc for this property
     * @return property tlddoc
     */
    String tlddoc() default Null;
    
    /**
     * javadoc for the getter.
     * @return getter javadoc.
     */    
    String javadocGet() default Null;  
    
    /**
     * javadoc for the setter.
     * @return setter javadoc.
     */     
    String javadocSet() default Null;
    
    /**
     * Attribute that goes inside the TLD for each attribute. It also helps IDEs
     * 
     */
    Required required() default Required.UNSET;
    
    /**
	 * If this property exists in a superclass, and one desires to use such implementation and/or to inherit the settings
	 * specified in the @Property annotation found in ancestor classes for this property, then one should set this field
	 * to Implementation.EXISTS_IN_SUPERCLASS. This property will still appear in the TLD and javadocs for this component and in the tag class.
	 * The default value is Implementation.GENERATE, which will ignore all settings in @Property annotations in ancestor classes
	 * for this property and will generate all necessary code for this property in this component class.
     * @return
     */
	Implementation implementation() default Implementation.UNSET;
}
