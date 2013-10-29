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

package org.icefaces.samples.showcase.metadata.context;

import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.util.FacesUtils;

import com.icesoft.faces.context.effects.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 */
public class ComponentExampleImpl<T> implements ComponentExample, ExampleResources, ContextBase, Serializable {
    private Class<T> parentClass;

    private String id;
    private String parent;
    private String title;
    private String description;
    private String example;

    private ArrayList<ExampleResource> exampleResource;
    private ArrayList<ExampleResource> javaResources;
    private ArrayList<ExampleResource> xhtmlResources;
    private ArrayList<ExampleResource> tldResources;
    private ArrayList<ExampleResource> externalResources;
    private ArrayList<ExampleResource> wikiResources;

    private String subMenuTitle;
    private ArrayList<MenuLink> subMenuLinks;
    
    private Effect effect;

    public ComponentExampleImpl(Class<T> parentClass) {
        this.parentClass = parentClass;
        exampleResource = new ArrayList<ExampleResource>();
        javaResources = new ArrayList<ExampleResource>();
        xhtmlResources = new ArrayList<ExampleResource>();
        tldResources = new ArrayList<ExampleResource>();
        externalResources = new ArrayList<ExampleResource>();
        wikiResources = new ArrayList<ExampleResource>();
        subMenuLinks = new ArrayList<MenuLink>();
    }

    public void initMetaData() {
        //Start scan for Class<T> parentClass annotations and use data from them to initialize ComponentExampleImpl object variables
        if (parentClass.isAnnotationPresent(org.icefaces.samples.showcase.metadata.annotation.ComponentExample.class)) 
        {
            processComponentExampleAnnotation();
        }
        // build up the separate lists of ExampleResources assigned to this class.
        if (parentClass.isAnnotationPresent(org.icefaces.samples.showcase.metadata.annotation.ComponentExample.class)) {
            processExampleResourcesAnnotation();
        }
        if (parentClass.isAnnotationPresent(org.icefaces.samples.showcase.metadata.annotation.ExampleResources.class)) {
            processExampleResourcesAnnotation();
        }
        processMenuAnnotation(parentClass.isAnnotationPresent( org.icefaces.samples.showcase.metadata.annotation.Menu.class));
    }
    
    private void processComponentExampleAnnotation()
    {
        org.icefaces.samples.showcase.metadata.annotation.ComponentExample componentExample = parentClass.getAnnotation(org.icefaces.samples.showcase.metadata.annotation.ComponentExample.class);
            parent = componentExample.parent();
            title = componentExample.title();
            description = componentExample.description();
            example = componentExample.example();
    }
    
    private void processExampleResourcesAnnotation()
    {
        if(exampleResource.isEmpty())
        {
            org.icefaces.samples.showcase.metadata.annotation.ExampleResources exampleResources = parentClass.getAnnotation(org.icefaces.samples.showcase.metadata.annotation.ExampleResources.class);
            org.icefaces.samples.showcase.metadata.annotation.ExampleResource[] resources = exampleResources.resources();
            ExampleResource tmpResource;

            for (org.icefaces.samples.showcase.metadata.annotation.ExampleResource resource : resources)
            {
                tmpResource = new ExampleResource(resource.title(), resource.resource(), resource.type());
                exampleResource.add(tmpResource);
                if (resource.type().equals(ResourceType.href)){
                    externalResources.add(tmpResource);
                }else if (resource.type().equals(ResourceType.java)){
                    javaResources.add(tmpResource);
                }else if (resource.type().equals(ResourceType.tld)){
                    tldResources.add(tmpResource);
                }else if (resource.type().equals(ResourceType.xhtml)){
                    xhtmlResources.add(tmpResource);
                }
                else if (resource.type().equals(ResourceType.wiki)){
                    wikiResources.add(tmpResource);
                }
            }
        }
    }
    
    private void processMenuAnnotation(boolean annotationExist)
    {
        if(annotationExist)
        {
            org.icefaces.samples.showcase.metadata.annotation.Menu menu = parentClass.getAnnotation(org.icefaces.samples.showcase.metadata.annotation.Menu.class);
            subMenuTitle = menu.title();
            org.icefaces.samples.showcase.metadata.annotation. MenuLink[] menuLinks =  menu.menuLinks();
            MenuLink menuLink;
            for (org.icefaces.samples.showcase.metadata.annotation.MenuLink link : menuLinks )
            {
                menuLink = new MenuLink(link.title(), link.isDefault(), link.isNew(), link.isDisabled(), link.exampleBeanName());
                subMenuLinks.add(menuLink);
            }
        }
        else
        {
            // If we don't have a submenu link annotation then check if we have a parent
            // If we have a parent then try to use their submenu links
            // This would be for something like BorderLayout associating itself with the parent of BorderBean
            if ((parent != null) && (!"".equals(parent))) {
                subMenuLinks = ((ComponentExampleImpl)FacesUtils.getManagedBean(parent)).getSubMenuLinks();
            }
        }
    }

    public String getId() {
        return id;
    }
    
    public String getParent() {
        return parent;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getExample() {
        return example;
    }

    public ArrayList<ExampleResource> getJavaResources() {
        return javaResources;
    }

    public ArrayList<ExampleResource> getXhtmlResources() {
        return xhtmlResources;
    }

    public ArrayList<ExampleResource> getTldResources() {
        return tldResources;
    }
    
    public ArrayList<ExampleResource> getWikiResources() {
        return wikiResources;
    }

    public ArrayList<ExampleResource> getExternalResources() {
        return externalResources;
    }

    public ArrayList<ExampleResource> getExampleResource() {
        return exampleResource;
    }

    public ArrayList<MenuLink> getSubMenuLinks() {
        return subMenuLinks;
    }

    public String getSubMenuTitle() {
        return subMenuTitle;
    }
    
    public Effect getEffect() {
        return effect;
    }
    
    public void setEffect(Effect effect) {
        this.effect = effect;
    }
    
    public void prepareEffect() {
        if (effect != null) {
            effect.setFired(false);
        }
    }
}
