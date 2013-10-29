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
 * This specifies the tag and interface classes that exist, that a baseMeta
 * represents.
 *
 * Annotated on a jsp baseMeta class, providing default values for the
 * generateInterfaceExtendsClass and generateTagExtendsClass fields of JSP
 * annotations on Meta classes that extend the jsp baseMeta class.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JSPBaseMeta {
    final String EMPTY = "";

    /**
     * Specifies the tag class name that the annotated baseMeta corresponds to.
     *
     * When a Meta class, with a @JSP annotation, extends a baseMeta class,
     * with this @JSPBaseMeta annotation, this field acts as a default value
     * for the @JSP annotation's generatedTagExtendsClass.
     *
     * @return fully qualified name of the tag class
     */
    String tagClass();

    /**
     * Specifies the class name of the interface that the annotated baseMeta
     * corresponds to.
     *
     * When a Meta class, with a @JSP annotation, extends a baseMeta class,
     * with this @JSPBaseMeta annotation, this field acts as a default value
     * for the @JSP annotation's generatedInterfaceExtendsClass.
     *
     * @return fully qualified class name of the interface.
     */
    String interfaceClass() default EMPTY;

    /**
     * Specify which body-content value to use in the TLD file.
     *
     * When a Meta class, with a @JSP annotation, extends a baseMeta class,
     * with this @JSPBaseMeta annotation, this field acts as a default value
     * for the @JSP annotation's bodyContent.
     *
     * @return value for the tag's body-content in the generated TLD file.
     */
    BodyContent bodyContent();
}
