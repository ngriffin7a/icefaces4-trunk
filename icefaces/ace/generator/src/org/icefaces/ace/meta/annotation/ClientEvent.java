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
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClientEvent {
	final String EMPTY = "";
	
	/**
	* Name of the client event supported by this component.
	* @return event names.
	*/ 
	String name();
	
	/**
	* javadocs for the component class. Goes into the component class.
	* @return javadocs for the component class.
	*/
	String javadoc() default EMPTY;

	/**
	* tld docs for the component class. Goes into the Tld documents.
	* @return component doc for tld.
	*/
	String tlddoc() default EMPTY; 

	/**
	* Default render attribute value.
	* @return javadocs for the component class.
	*/
	String defaultRender() default EMPTY;

	/**
	* Default execute attribute value.
	* @return component doc for tld.
	*/
	String defaultExecute() default EMPTY; 
	
	/**
	* Fully qualified class name of the argument supported by the listener method
	* @return component doc for tld.
	*/
	String argumentClass() default EMPTY; 
}
