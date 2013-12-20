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

package org.icefaces.ace.generator.utils;

import org.icefaces.ace.meta.annotation.*;

import java.util.EnumSet;

public class PropertyValues {

	public PropertyValues() {
	
	}
	
	public Expression expression = Expression.UNSET;
	public String methodExpressionArgument = Property.Null;
	public String defaultValue = Property.Null;
	public DefaultValueType defaultValueType = DefaultValueType.UNSET;
	public String tlddoc = Property.Null;
	public String javadocGet = Property.Null;
	public String javadocSet = Property.Null;
	public Required required = Required.UNSET;
    public String name = Property.Null; 

	public Implementation implementation = Implementation.UNSET;

    /**
     * The field of the bottom most subclass, likely from the Meta class itself
     */
    public java.lang.reflect.Field field;

    public EnumSet<OnlyType> onlyTypes;

	/**
     * Property was first defined in a superclass
     */
	public boolean definedInSuperClass = false;

    /**
     * Property was defined (only or also) in the ending Meta class
     */
    public boolean definedInEndClass = false;

    public boolean modifiesDefaultValueOrMethodExpression = false;

    public boolean modifiesJavadoc = false;

    /**
     * If property doesn't exist in ancestor classes or if any of the key
     * fields were modified, then add to component class
     */
    public boolean isGeneratingProperty() {
        return (implementation == Implementation.GENERATE) ||
               ( (implementation != Implementation.EXISTS_IN_SUPERCLASS) &&
                 (definedInEndClass || modifiesDefaultValueOrMethodExpression || modifiesJavadoc) );
    }

    /**
     * If only javadocGet or javadocSet were specified, then simply create
     * delegating getter/setter and do not generate state saving code
     */
	public boolean isDelegatingProperty() {
        return (modifiesDefaultValueOrMethodExpression == false && modifiesJavadoc == true);
    }

    public String resolvePropertyName() {
        if (name != null && !name.equals(Property.Null) && !name.equals("null")) {
            return name;
        }
        return field.getName();
    }

    public String getJavaVariableName() {
        return field.getName();
    }

    public String getArrayAwareType() {
        return Utility.getArrayAwareType(field);
    }

    public boolean isIntersectionOfOnlyTypes() {
        return onlyTypes == null || onlyTypes.equals(EnumSet.allOf(OnlyType.class));
    }

    /**
     * Called in sequence from the most super class until the end sub class,
     * this imports the values from a Property annotation, so that later
     * values take precedence.
     */
    public void importProperty(java.lang.reflect.Field field, Property property, boolean isEndClass, OnlyType onlyType) {
        if (property.expression() != Expression.UNSET) {
            expression = property.expression();
        }
        if (!property.methodExpressionArgument().equals(Property.Null)) {
            methodExpressionArgument = property.methodExpressionArgument();
        }
        if (!property.defaultValue().equals(Property.Null)) {
            defaultValue = property.defaultValue();
        }
        if (property.defaultValueType() != DefaultValueType.UNSET) {
            defaultValueType = property.defaultValueType();
        }
        if (!property.tlddoc().equals(Property.Null)) {
            tlddoc = property.tlddoc();
        }
        if (!property.javadocGet().equals(Property.Null)) {
            javadocGet = property.javadocGet();
        }
        if (!property.javadocSet().equals(Property.Null)) {
            javadocSet = property.javadocSet();
        }
        if (property.required() != Required.UNSET) {
            required = property.required();
        }
        if (property.implementation() != Implementation.UNSET) {
            implementation = property.implementation();
        }
        if (!property.name().equals(Property.Null)) {
            name = property.name();
        }

        this.field = field;

        if (onlyType != null) {
            if (onlyTypes == null) {
                onlyTypes = EnumSet.of(onlyType);
            } else {
                onlyTypes.add(onlyType);
            }
        } else {
            if (onlyTypes == null) {
                onlyTypes = EnumSet.allOf(OnlyType.class);
            } else {
                onlyTypes.addAll(EnumSet.allOf(OnlyType.class));
            }
        }

        if (isEndClass) {
            definedInEndClass = true;
        } else {
            definedInSuperClass = true;
        }

        // If we've defined something that we want to generate
        // Not just when isEndClass, since we might want our inherited properties
        // to still generate ACE style getters and setters, instead of using the given ones
        if (implementation != Implementation.EXISTS_IN_SUPERCLASS) {
            if (property.expression() != Expression.UNSET ||
                !property.methodExpressionArgument().equals(Property.Null) ||
                !property.defaultValue().equals(Property.Null) ||
                property.defaultValueType() != DefaultValueType.UNSET) {
                modifiesDefaultValueOrMethodExpression = true;
            }
            if (!property.javadocGet().equals(Property.Null) ||
                !property.javadocSet().equals(Property.Null)) {
                modifiesJavadoc = true;
            }
        }
    }

    public void setDefaultValues() {
        if (expression == Expression.UNSET) {
            expression = Expression.DEFAULT;
        }
        if (methodExpressionArgument.equals(Property.Null)) {
            methodExpressionArgument = "";
        }
        if (defaultValue.equals(Property.Null)) {
            defaultValue = "null";
        }
        if (defaultValueType == DefaultValueType.UNSET) {
            defaultValueType = DefaultValueType.DEFAULT;
        }
        if (tlddoc.equals(Property.Null)) {
            tlddoc = "";
        }
        if (javadocGet.equals(Property.Null)) {
            javadocGet = tlddoc;
        }
        if (javadocSet.equals(Property.Null)) {
            javadocSet = tlddoc;
        }
        if (required == Required.UNSET) {
            required = Required.DEFAULT;
        }
        if (implementation == Implementation.UNSET) {
            implementation = Implementation.DEFAULT;
        }

        // ICE-6209 Append the default value to the description, if present
        if (! "null".equals(defaultValue) && ! "".equals(defaultValue)) {
            String defaultValDesc = "Default = '" + defaultValue + "'.";
            tlddoc = tlddoc + (tlddoc.length() > 0 ? " " : "") + defaultValDesc;
            javadocGet = javadocGet + (javadocGet.length() > 0 ? " " : "") + defaultValDesc;
        }
    }
}
