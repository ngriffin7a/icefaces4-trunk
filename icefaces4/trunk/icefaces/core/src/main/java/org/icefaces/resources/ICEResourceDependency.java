package org.icefaces.resources;

import java.lang.annotation.*;

/**
 * Copyright 2010-2013 ICEsoft Technologies Canada Corp.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * <p/>
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * User: Nils Lundquist
 * Date: 2013-01-24
 * Time: 11:56 AM
 */

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Inherited
public @interface ICEResourceDependency {
    /**
     * <p>A list of <code>ICEBrowserDependency</code> annotations defining
     * browser specific alternative scripts, to be served in place
     * of the script defined in this annotation.</p>
     *
     * <p>This property is mutually exclusive with 'browser'. If defined,
     * the resource defined in this annotation is assumed to be served
     * to all browsers not overridden.</p>
     */
    public ICEBrowserDependency[] browserOverride() default {};

    /**
     * <p>Define the browser that must be used for this dependency to be served.
     * Defaults to serve to all browsers.</p>
     *
     * <p>Note this property has no effect if browserOverride is defined.
     * In that cased this annotation is assumed to apply to all browsers
     * not overridden.</p>
     */
    public BrowserType browser() default BrowserType.ALL;

    /**
     * <p class="changed_added_2_0">The <em>resourceName</em> of the
     * resource pointed to by this <code>ResourceDependency</code>.  It
     * is valid to have EL Expressions in the value of this attribute,
     * as long as the expression resolves to an instance of the expected
     * type.</p>
     */
    public String name() default "";

    /**
     * <p class="changed_added_2_0">The <em>libraryName</em> in which
     * the resource pointed to by this <code>ResourceDependency</code>
     * resides.  If not specified, defaults to the empty string.  It is
     * valid to have EL Expressions in the value of this attribute, as
     * long as the expression resolves to an instance of the expected
     * type.</p>
     */
    public String library() default "";

    /**
     * <p class="changed_added_2_0">The value given for this attribute
     * will be passed as the "target" argument to {@link
     * javax.faces.component.UIViewRoot#addComponentResource(javax.faces.context.FacesContext,
     * javax.faces.component.UIComponent, java.lang.String)}.  If this
     * attribute is specified, {@link
     * javax.faces.component.UIViewRoot#addComponentResource(javax.faces.context.FacesContext,javax.faces.component.UIComponent)}
     * must be called instead, as described above.  It is valid to have
     * EL Expressions in the value of this attribute, as long as the
     * expression resolves to an instance of the expected type.</p>
     */
    public String target() default "";
}
