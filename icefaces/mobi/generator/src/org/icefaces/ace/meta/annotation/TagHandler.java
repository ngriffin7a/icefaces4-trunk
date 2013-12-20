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
 * The TagHandler annotation takes information, which is used by the tld, and
 * faces-taglib as well as the TagHandler class itself. It has some mandatory fields and some 
 * that are optional. Which allows to specify following information: 
 * <ul>
 * <li> the name and location of the class to be generated</li>
 * <li> tag handler type</li>
 * <li> tag name </li>
 * <li> Javadoc</li>
 * <li> TLD doc</li>  
 * </ul> 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TagHandler {

  public final String EMPTY = "";
  
  /**
   * Name of tag. Its a mandatory field.
   * @return defined tag name. 
   */
  String tagName();
  
  /**
   * Name of tag. Its a mandatory field.
   * @return defined tag name. 
   */
  String tagHandlerClass() default EMPTY;
  
  /**
   * by default generated classes are leaf classes, so you can't override any behaviour. 
   * If you want to hand code the leaf class and extend the generated one then you can 
   * use this attribute in conjunction with componentClass attribute. For example:
   * HandCodedClass --> GeneratedClass --> UIComponent
   * @return fully qualified name of the generated class.
   */
  String generatedClass() default EMPTY;
  
  /**
   * Class that has to be extended by the generated component. Its a mandatory field.
   * @return fully qualified name of the class has to be extended.
   */
  String extendsClass();
  
  /**
   * Type of the tag handler.
   * @return component type.
   */
  TagHandlerType tagHandlerType();
  
  /*
   * The identifier of this behavior within the framework if this is a BehaviorHandler type.
   * @return behavior id.
   */
  String behaviorId() default EMPTY;
  
  /**
   * javadocs for the tag handler class. Goes into the Base class.
   * @return javadocs for the component class.
   */
  String javadoc() default EMPTY;
  
  /**
   * tld docs for the tag handler class. Goes into the Tld documents.
   * @return component doc for tld.
   */
  String tlddoc() default EMPTY; 
  
  /**
    * Class of the behavior itself (if this is a behavior handler).
    * @return behavior class.
    */
  String behaviorClass() default EMPTY;
}
