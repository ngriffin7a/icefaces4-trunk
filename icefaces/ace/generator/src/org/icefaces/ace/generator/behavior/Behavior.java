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

package org.icefaces.ace.generator.behavior;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.icefaces.ace.generator.utils.PropertyValues;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.generator.artifacts.ComponentArtifact;
import org.icefaces.ace.generator.context.ComponentContext;


public abstract class Behavior {
    private ArrayList<PropertyValues> properties;

	public Behavior(Class clazz) {
        properties = new ArrayList<PropertyValues>();
		for (Field field: clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Property.class)) {
                Property property = field.getAnnotation(Property.class);
                PropertyValues prop = new PropertyValues();
                prop.importProperty(field, property, true, null);
                properties.add(prop);
			}
		}
	}
	
	public ArrayList<PropertyValues> getProperties() {
		return properties;
	}

	public boolean hasInterface() {
		return false;
	}
	public String getInterfaceName() {
		return null;
	}
	public abstract boolean hasBehavior(Class clazz);
	
	public void addImportsToComponent(StringBuilder stringBuilder ) {
		
	}
	
	public void addCodeToComponent(StringBuilder stringBuilder ) {
		
	}
	public void addImportsToTag(StringBuilder stringBuilder ) {
		
	}
	public void addProperties(ComponentContext componentContext ) {
		
	}
	public void addPropertiesEnumToComponent(StringBuilder output) {
		
	}
	
	public void addGetterSetter(ComponentArtifact artifact, StringBuilder output) {
		for(PropertyValues prop : getProperties()) {
			artifact.addGetterSetter(prop);
		}
	}	
}
