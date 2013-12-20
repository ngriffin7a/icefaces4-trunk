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


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClientBehaviorHolder {
    
    ClientEvent[] events();
	
    String defaultEvent() default "";

    /**
     * ACE components only work with ace:ajax, not f:ajax. By default, code is
     * generated to block f:ajax from being added to ACE components and
     * interfering with them. This is necessary when f:ajax wraps components,
     * ostensibly for addition to h: components, but if ace: components are
     * also in that component sub-tree, then they would be affected as well.
     *
     * When ACEnvironment is used to generate third party components, they may
     * wish their components to work with f:ajax, in which case this field can
     * be set to true for those components.
     */
    boolean allowFAjax() default false;
}
