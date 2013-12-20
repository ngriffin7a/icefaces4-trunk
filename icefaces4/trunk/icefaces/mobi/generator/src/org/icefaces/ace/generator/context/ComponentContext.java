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


import org.icefaces.ace.generator.artifacts.ComponentArtifact;
import org.icefaces.ace.generator.artifacts.ComponentHandlerArtifact;
import org.icefaces.ace.generator.artifacts.TagArtifact;
import org.icefaces.ace.generator.behavior.Behavior;
import org.icefaces.ace.generator.utils.PropertyValues;
import org.icefaces.ace.generator.utils.Utility;
import org.icefaces.ace.meta.annotation.*;

public class ComponentContext extends MetaContext {
    private static final Logger logger = Logger.getLogger(ComponentContext.class.getName());

    private Map<String, Field> fieldsForFacet = new HashMap<String, Field>();
    private List<Behavior> behaviors = new ArrayList<Behavior>();
    private List<String> disinheritProperties = new ArrayList<String>();
    
    public List<Behavior> getBehaviors() {
		return behaviors;
	}

	public Map<String, Field> getFieldsForFacet() {
		return fieldsForFacet;
	}

    public List<String> getDisinheritProperties() {
        return disinheritProperties;
    }
    private void setDisinheritProperties(List props){
        this.disinheritProperties = props;
    }

	public ComponentContext(Class clazz) {
		super(clazz);
    }

    @Override
    protected void setupArtifacts() {
		artifacts.put(ComponentArtifact.class.getSimpleName(), new ComponentArtifact(this));
		artifacts.put(TagArtifact.class.getSimpleName(), new TagArtifact(this));
		artifacts.put(ComponentHandlerArtifact.class.getName(), new ComponentHandlerArtifact(this));
	}

    @Override
    protected boolean isPropertyValueDisinherited(Class clazz, String name) {
        if (!getDisinheritProperties().isEmpty()){
            if (logger.isLoggable(Level.FINE)){
                logger.info("this component has disinherited properties");
            }
            if (getDisinheritProperties().contains(name)){
                if (logger.isLoggable(Level.FINE)){
                    logger.info("property name has been disinherited");
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean isRelevantClass(Class clazz) {
        return clazz.isAnnotationPresent(Component.class);
    }

    @Override
    protected boolean isAllowedPropertyOnlyType(OnlyType onlyType) {
        return OnlyType.JSF.equals(onlyType);
    }
    
    @Override
    protected void processAnnotation(Class clazz) {
        //first have to get disinheritedprops to check the annotations to see if disinherited
        Component component = (Component) this.getActiveClass().getAnnotation(Component.class);
        String[] propsArr = component.disinheritProperties();
        if (propsArr.length>0 ){
            /*logger.info(" number of disinherited props = "+propsArr.length);*/
            this.setDisinheritProperties(Arrays.asList(propsArr));
        }
        super.processAnnotation(clazz);
        processFacets(clazz);
        processBehaviors(clazz);
    }

    @Override
    protected void furtherProcessProperty(Class clazz, PropertyValues propertyValues) {
        if (propertyValues.isGeneratingProperty()) {
            if (propertyValues.expression == Expression.METHOD_EXPRESSION) {
                generateHandler = true;
            }
        }
    }


    private void processFacets(Class clazz){
        for (Class declaredClass : clazz.getDeclaredClasses()) {
            if (declaredClass.isAnnotationPresent(Facets.class)) {
                for (Field field : declaredClass.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Facet.class)) {
                        fieldsForFacet.put(field.getName(), field);
                    }
                }
            }
        }
    }

    private void processBehaviors(Class clazz) {
        for (Behavior behavior: GeneratorContext.getInstance().getBehaviors()) {
            if (behavior.hasBehavior(clazz)) {
                //attach behavior to the component context
                getBehaviors().add(behavior); // ComponentArtifact uses this List
                behavior.addProperties(this); // This does nothing
            }
        }
    }
}
