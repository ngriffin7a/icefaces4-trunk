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
 * The component annotation takes information, which is used by the tld, faces-config, 
 * faces-taglib as well as the component itself. It has some mandatory fields and some 
 * that are optional. Which allows to specify following information: 
 * <ul>
 * <li> the name and location of the class to be generated</li>
 * <li> component type</li>
 * <li> renderer type</li>
 * <li> renderer class </li>
 * <li> tag name </li>
 * <li> Javadoc</li>
 * <li> TLD doc</li>  
 * </ul> 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
  final String EMPTY = "";
  /**
   * Name of tag. It's a mandatory field.
   * @return defined tag name. 
   */
  String tagName();
  
  /**
   * Class that is to be extended by the generated component. It's a mandatory field.
   * @return fully qualified name of the class has to be extended.
   */
  String extendsClass();
  
  /**
   * fully qualified name of the class of the Renderer, use by the target component. 
   * (This class has to be created by developer)
   * If not specified, will default to the componentClass+"Renderer"
   * @return fully qualified name of the Renderer class.
   */
  String rendererClass()default EMPTY;
  
  /**
   * fully qualified name for the component class. It's a mandatory field.
   * @return fully qualified name of the component class.
   */
  String componentClass();
  
  /**
   * By default, generated classes are leaf classes, so you can't override any
   * behaviour. If you want to hand code the component class, and have it
   * extend the generated one then you can use this attribute in conjunction
   * with componentClass attribute. So, if generatedClass is specified:
   * (manual) componentClass extends generatedClass extends extendsClass.
   * Otherwise: (generated) componentClass extends extendsClass.
   * @return fully qualified name of the generated class.
   */
  String generatedClass()default EMPTY;
  
  /**
   * renderer type
   * @return type of the renderer
   */
  String rendererType()default EMPTY;
  
  /**
   * type of the component.
   * @return component type.
   */
  String componentType();
  
  /**
   * name of the component family.
   * @return component family.
   */
  String componentFamily() default EMPTY;

  /**
   * JSP tag class. Default is to automatically generate it, naming it componentClass+"Tag".
   * Alternatively, this can be set if developer wants to use its own tag
   * class, instead of generating one.
   * @return JSP tag class, when not generating one.
   */
  String tagClass() default EMPTY;

  /**
   * defines a base tag class that can be extended by the generated tag class. default is 
   * "javax.faces.webapp.UIComponentELTag".
   * @return fully qualified name of base tag class.
   */
  String baseTagClass() default "javax.faces.webapp.UIComponentELTag";
  
  /**
   * Facelets handler class. Default is to automatically determine if one is
   * needed, and if so generate it, naming it componentClass+"Handler".
   * Alternatively, this can be set if developer wants to use its own handler
   * class, instead of generating one.
   * @return facelets handler class, when not generating one.
   */
  String handlerClass() default EMPTY;
  
  /**
   * javadoc for the component class. Goes into the generated component class.
   * If not specified, defaults to being the same as tlddoc.
   * @return javadoc for the generated component class.
   */
  String javadoc() default EMPTY;
  
  /**
   * tld doc for the component class. Goes into the Tld documentation.
   * @return component documentation for tld.
   */
  String tlddoc() default EMPTY; 
  
  /**
   * Name of the properties, that needs to be included from the parent class.
   * @return property names.
   */
 // String[] includeProperties() default {};

  /** Name of the properties that are not to be inherited from the superclass
   *
   */
  String[] disinheritProperties() default {};

}
