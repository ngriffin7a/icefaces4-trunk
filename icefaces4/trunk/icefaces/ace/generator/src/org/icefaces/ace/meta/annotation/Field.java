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
 * This annotation allows to create a protected member for component internal use and 
 * by default adds it to the saveState/restoreState
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {
	/**
	 * Transient property will not be saved and restored.
	 * @return boolean value.
	 */
    boolean isTransient() default false;
    
    /**
     * Default value of the property.
     * @return default value.
     */
    String defaultValue() default "null";
    
    /**
     * javadoc for the property.
     * @return javadoc.
     */
    String javadoc() default ""; 
    
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
     * But what if you want to define some other type then string or a constant, expression etc. You don't want
     * value to be quoted in that case. So you would set this attribute to false. (e.g)
     * 
     * @Property (value="10", defaultValueIsStringLiteral=false)
     * Integer count;      
     * </pre>
     * @return 
     */
    boolean defaultValueIsStringLiteral() default true;
}
