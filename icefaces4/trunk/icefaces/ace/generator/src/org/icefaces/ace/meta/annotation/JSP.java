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
 * Used to generate pure JSP (non-JSF) Tag file, interface, TLD, TLDDOC,
 * Javadoc. Specify this annotation on Meta classes, optionally along-side any
 * Component annotation. Use the Property annotation on the Meta class'
 * fields, and discern between properties that are only for JSP or only for
 * JSF via the Only annotation. Have a JSP annotated Meta class extend a
 * JSPBaseMeta annotated baseMeta class to use it's defaults for the JSP's
 * generateInterfaceExtendsClass and generateTagExtendsClass fields.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JSP {
    final String EMPTY = "";
    
    /**
     * Name of tag. If not specified, then it defaults to the Meta class'
     * simple name, without the "Meta" suffix, using camel case.
     * Eg: MyCompMeta -> myComp
     * 
     * @return defined tag name.
     */
    String tagName() default EMPTY;

    /**
     * Fully qualified name for the tag class. This is the manually coded Tag
     * class that will be referenced in the TLD, that would extend the
     * generated tag class, and add any necessary manual code, or simply remain
     * empty.
     *
     * If not specified, then it defaults to the Meta class' full name,
     * without the "Meta" suffix, but with a "Tag" suffix added.
     * Eg: org.mypackage.MyCompMeta -> org.mypackage.MyCompTag
     *
     * @return fully qualified name of the tag class.
     */
    String tagClass() default EMPTY;

    /**
     * Fully qualified name for the generated tag class. This will contain the
     * generated fields, getter and setter methods, and release method. The
     * tag class will extend it, and it will extend generateTagExtendsClass.
     *
     * If not specified, then it defaults to the Meta class' full name,
     * without the "Meta" suffix, but with a "BaseTag" suffix added.
     * Eg: org.mypackage.MyCompMeta -> org.mypackage.MyCompBaseTag
     *
     * @return fully qualified name of the generated base tag class.
     */
    String generatedTagClass() default EMPTY;

    /**
     * Fully qualified name of the class that the generated tag class will extend.
     *
     * If not specified, but the Meta class extends a @JSPBaseMeta annotated
     * baseMeta class, then the @JSPBaseMeta tagClass will be used. Otherwise,
     * "javax.servlet.jsp.tagext.TagSupport" will be used.
     *
     * @return fully qualified name of the class that the generated base tag
     * will extended.
     */
    String generatedTagExtendsClass() default EMPTY;

    /**
     * Fully qualified class name for the generated interface. This will be
     * an interface for the getter and setter methods corresponding to the
     * generated properties. While the generated base tag class will only
     * contain common and @Only(JSP) properties, the interface will contain
     * getter and setter methods for the union of all of the properties,
     * including @Only(JSF) ones.
     *
     * If not specified, then it defaults to the Meta class' full name,
     * without the "Meta" suffix, but with an "I" prefix added.
     * Eg: org.mypackage.MyCompMeta -> org.mypackage.IMyComp
     *
     * @return fully qualified class name of the generated interface.
     */
    String generatedInterfaceClass() default EMPTY;

    /**
     * Fully qualified class name of the interface that the generated interface
     * will extend.
     *
     * If not specified, but the Meta class extends a @JSPBaseMeta annotated
     * baseMeta class, then the @JSPBaseMeta interfaceClass will be used.
     * Otherwise, the generated interface will not extend anything.
     *
     * @return fully qualified class name of the interface that the generated
     * interface will extended.
     */
    String generatedInterfaceExtendsClass() default EMPTY;

    /**
     * tld doc for the tag class. Goes into the Tld documentation.
     * @return tag documentation for tld.
     */
    String tlddoc() default EMPTY;

    /**
     * javadoc for the tag class. Goes into the generated tag class.
     * If not specified, defaults to being the same as tlddoc.
     * @return javadoc for the generated base tag class.
     */
    String javadoc() default EMPTY;

    /**
     * Specify which body-content value to use in the TLD file.
     *
     * If not specified, but the Meta class extends a @JSPBaseMeta annotated
     * baseMeta class, then the @JSPBaseMeta bodyContent will be used,
     * otherwise "JSP".
     *
     * @return value for the tag's body-content in the generated TLD file.
     */
    BodyContent bodyContent() default BodyContent.UNSET;
}
