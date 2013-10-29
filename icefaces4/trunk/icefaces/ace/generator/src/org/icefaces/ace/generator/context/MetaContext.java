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

package org.icefaces.ace.generator.context;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;


import org.icefaces.ace.generator.artifacts.Artifact;
import org.icefaces.ace.generator.utils.PropertyValues;
import org.icefaces.ace.meta.annotation.*;

public abstract class MetaContext {
    private static final Logger logger = Logger.getLogger(MetaContext.class.getName());
    protected Map<String, Artifact> artifacts = new HashMap<String, Artifact>();
    protected Class activeClass;
	protected Map<Field, PropertyValues> propertyValuesMap = new HashMap<Field, PropertyValues>();
    protected Map<String, Field> internalFieldsForComponentClass = new HashMap<String, Field>();

	protected boolean generateHandler;
	
    public Map<Field, PropertyValues> getPropertyValuesMap() {
		return propertyValuesMap;
	}

    public Map<String, Field> getInternalFieldsForComponentClass() {
        return internalFieldsForComponentClass;
    }

    /**
     * List of all PropertyValues, alphabetically sorted by resolved property name
     */
    public ArrayList<PropertyValues> getPropertyValuesSorted() {
        ArrayList<PropertyValues> list = Collections.list(Collections.enumeration(propertyValuesMap.values()));
        sortByResolvedPropertyName(list);
        return list;
    }

    /**
     * List only of the PropertyValues that we're generating, alphabetically sorted by resolved property name
     */
    public ArrayList<PropertyValues> getGeneratingPropertyValuesSorted() {
        ArrayList<PropertyValues> list = new ArrayList<PropertyValues>(propertyValuesMap.size()+1);
        for(PropertyValues prop : propertyValuesMap.values()) {
            if (prop.isGeneratingProperty()) {
             /*   logger.info("adding prop to list ="+prop.name);*/
                list.add(prop);
            }
        }
        sortByResolvedPropertyName(list);
        return list;
    }

    /**
     * List only of the PropertyValues that we're generating, which apply to both JSP AND JSF,
     * alphabetically sorted by resolved property name
     */
    public ArrayList<PropertyValues> getIntersectionOfOnlyTypesGeneratingPropertyValuesSorted() {
        ArrayList<PropertyValues> list = new ArrayList<PropertyValues>(propertyValuesMap.size()+1);
        for(PropertyValues prop : propertyValuesMap.values()) {
         //   logger.info("getIntersectionOfOnlyTypesGeneratingProperty name of  " + prop.resolvePropertyName() + "  intersection: " + prop.isIntersectionOfOnlyTypes() + "  generating: " + prop.isGeneratingProperty());
            if (prop.isIntersectionOfOnlyTypes() && prop.isGeneratingProperty()) {
                if (logger.isLoggable(Level.FINE)){
                   logger.info("check intersection case prop ="+prop.name+" added to list");
                }
                list.add(prop);
            }
        }
        sortByResolvedPropertyName(list);
        return list;
    }

    private static void sortByResolvedPropertyName(List<PropertyValues> list) {
        Collections.sort(list, new Comparator<PropertyValues>() {
            public int compare(PropertyValues propertyValues, PropertyValues propertyValues1) {
                return propertyValues.resolvePropertyName().compareTo(propertyValues1.resolvePropertyName());
            }
        });
    }

    /**
     * List of all internal fields, alphabetically sorted by name
     */
    public ArrayList<Field> getInternalFieldsSorted() {
        ArrayList<Field> list = Collections.list(Collections.enumeration(internalFieldsForComponentClass.values()));
        Collections.sort(list, new Comparator<Field>() {
            public int compare(Field field1, Field field2) {
                return field1.getName().compareTo(field2.getName());
            }
        });
        return list;
    }

    public boolean isGeneratingPropertyByName(String name) {
        for(PropertyValues prop : getPropertyValuesMap().values()) {
            if (prop.isGeneratingProperty() &&
                prop.resolvePropertyName().equals(name)) {
                return true;
            }
        }
        return false;
    }

	public boolean isGenerateHandler() {
		return generateHandler;
	}

	public Artifact getArtifact(Class<? extends Artifact> artifact ) {
		return artifacts.get(artifact.getSimpleName());
	}
	
	public Class getActiveClass() {
		return activeClass;
	}

	public void setActiveClass(Class activeClass) {
		this.activeClass = activeClass;
	}

	public Iterator<Artifact> getArtifacts() {
		return artifacts.values().iterator();
	}

	public MetaContext(Class clazz) {
		setActiveClass(clazz);
	}

    public void process() {
        if (isRelevantClass(getActiveClass())) {
            GeneratorContext.getInstance().setActiveMetaContext(this);
            processAnnotation(getActiveClass());
            setupArtifacts();
            build();
        }
    }

    protected void processAnnotation(Class clazz) {
        for (Field field : getDeclaredFields(clazz)) {
            processPotentiallyIrrelevantField(clazz, field);
        }
    }

    /**
     * if it's used and not disinherited return true, if not return false;
     * @param clazz
     * @param field
     * @return
     */
    protected boolean processPotentiallyIrrelevantField(Class clazz, Field field) {
        if(field.isAnnotationPresent(Only.class)){
            Only only = field.getAnnotation(Only.class);
            if (!isAllowedPropertyOnlyType(only.value())) {
                return false;
            }
        }
        if(field.isAnnotationPresent(Property.class)){
            // collect @Property values from top to bottom
            PropertyValues propertyValues = collectPropertyValues(field.getName(), clazz);
            /* first check to see if this is a disinherited property */
            if (isPropertyValueDisinherited(clazz, field.getName())){
                if (logger.isLoggable(Level.FINE)){
                   logger.info("YAYAYAYAYAYAYA property="+ field.getName() +" is disinherited!!!!!");
                }
                return false;
            }
            /*logger.info("==="+field.getName() +" not disinherited");*/
            /* if values end up being UNSET, then set them to default*/
            propertyValues.setDefaultValues();
            propertyValuesMap.put(field, propertyValues);
            furtherProcessProperty(clazz, propertyValues);
            return true;
        } else if (field.isAnnotationPresent(org.icefaces.ace.meta.annotation.Field.class)) {
            internalFieldsForComponentClass.put(field.getName(), field);
            return true;
        }
        return false;
    }

    abstract protected boolean isRelevantClass(Class clazz);
    abstract protected boolean isAllowedPropertyOnlyType(OnlyType onlyType);
    abstract protected void setupArtifacts();
    abstract protected boolean isPropertyValueDisinherited(Class clazz, String name);


    protected void furtherProcessProperty(Class clazz, PropertyValues propertyValues) {
    }
	
	protected PropertyValues collectPropertyValues(String fieldName, Class clazz) {
        if (logger.isLoggable(Level.FINE)){
            logger.info(getClass().getSimpleName() + "  COLLECT  " + clazz.getSimpleName()+"."+fieldName);
        }
		return collectPropertyValues(fieldName, clazz, new PropertyValues(), true);
	}

    /**
     * collect the list of eligible property values. GEnerating tries to add stuff...
     * is it in one of the superclasses?  Can we override?  allow means ability to
     * override.
     * @param fieldName
     * @param clazz
     * @param propertyValues
     * @param isEndClass
     * @return
     */
	protected PropertyValues collectPropertyValues(String fieldName,
            Class clazz, PropertyValues propertyValues, boolean isEndClass) {
        Field field = null;
        Property property = null;
        OnlyType onlyType = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            if (field.isAnnotationPresent(Property.class)) {
                property = field.getAnnotation(Property.class);
            }
            if (field.isAnnotationPresent(Only.class)) {
                onlyType = field.getAnnotation(Only.class).value();
            }
        } catch (NoSuchFieldException e) {}

        Class superClass = clazz.getSuperclass();
        if (superClass != null) {
            collectPropertyValues(fieldName, superClass, propertyValues, false);
        }

        if (field != null && property != null) {
            boolean allowed = onlyType == null || isAllowedPropertyOnlyType(onlyType);
            //overriding is not allowed.  allowed does not remove the property.
      //      logger.info("  " + clazz.getSimpleName()+"."+fieldName + "  allowed: " + allowed + "\n    field: " + field + "\n    property: " + property + "\n    onlyType: " + onlyType);
            if (allowed) {
                propertyValues.importProperty(field, property, isEndClass, onlyType);
            }
        }
        
		return propertyValues;
	}
	
	protected Field[] getDeclaredFields(Class clazz) {
        HashMap<String, Field> fields = new HashMap<String, Field>();
		getDeclaredFields(clazz, fields);
        return fields.values().toArray(new Field[fields.size()]);
	}
	
	// collect all declared fields of a class and all its ancestor classes
	private void getDeclaredFields(Class clazz, Map<String, Field> fields) {
        if (clazz == null) return;
		// add fields to map
		for (Field localField : clazz.getDeclaredFields()) {
            if(localField.isAnnotationPresent(Only.class)){
                Only only = localField.getAnnotation(Only.class);
                if (!isAllowedPropertyOnlyType(only.value())) {
                    continue;
                }
            }
			if (!fields.containsKey(localField.getName())) {
				fields.put(localField.getName(), localField);
			}
		}
        getDeclaredFields(clazz.getSuperclass(), fields);
	}

    protected void build() {
        Iterator<Artifact> artifacts = getArtifacts();
        while (artifacts.hasNext()) {
            artifacts.next().build();
        }
    }
}
