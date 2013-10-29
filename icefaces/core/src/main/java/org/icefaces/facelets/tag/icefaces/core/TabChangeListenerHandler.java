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

package org.icefaces.facelets.tag.icefaces.core;

import java.lang.reflect.Method;
import java.io.IOException;

import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributeException;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandler;
//import com.sun.facelets.tag.jsf.ComponentSupport;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;


/**
 *  !! NOTE: This is a duplicate of the  original class in compat
 * Any changes in that file or this one MUST be replicated in both copies.
 */

/**
 * @author Mark Collette
 * @since 1.1
 */
public class TabChangeListenerHandler extends TagHandler {
    private static Class panelTabSetClass;
    private static Class tabChangeListenerClass;
    private static Method addTabChangeListenerMethod;
    private static boolean triedGettingICEfacesComponentClasses;

    private static synchronized boolean isICEfacesComponentClassesPresent() {
        if( !triedGettingICEfacesComponentClasses ) {
            try {
                panelTabSetClass = Class.forName(
                        "com.icesoft.faces.component.paneltabset.PanelTabSet");
                tabChangeListenerClass = Class.forName(
                        "com.icesoft.faces.component.paneltabset.TabChangeListener");
                addTabChangeListenerMethod = panelTabSetClass.getMethod(
                        "addTabChangeListener", new Class[]{tabChangeListenerClass});
            }
            catch (Exception e) {
            }
            triedGettingICEfacesComponentClasses = true;
        }
        return (panelTabSetClass           != null &&
                tabChangeListenerClass     != null &&
                addTabChangeListenerMethod != null);
    }


    private final TagAttribute typeTagAttribute;

    /**
     * @param config
     */
    public TabChangeListenerHandler(TagConfig config) {
        super(config);

        typeTagAttribute = getRequiredAttribute("type");
    }

    /**
     * Threadsafe Method for controlling evaluation of its child tags,
     * represented by "nextHandler"
     */
    public void apply(FaceletContext ctx, UIComponent parent)
            throws IOException, FacesException, ELException {
        if( !isICEfacesComponentClassesPresent() ) {
            throw new TagException(
                    tag,
                    "ICEfaces components classes not found, can not use this tag");
        }
        if( parent == null ) {
            throw new TagException(tag, "Parent UIComponent was null");
        }
        if( !panelTabSetClass.isAssignableFrom(parent.getClass()) ) {
            throw new TagException(
                    tag,
                    "Parent UIComponent must be a "+
                    "com.icesoft.faces.component.paneltabset.PanelTabSet");
        }
        if( !typeTagAttribute.isLiteral() ) {
            throw new TagAttributeException(
                    tag, typeTagAttribute,
                    "The class, as given by tabChangeListener tag's type " +
                    "attribute, must be literal, and not a value expression: " +
                    typeTagAttribute.getValue());
        }
        String listenerClassName = typeTagAttribute.getValue(ctx);
        try {
            Class listenerClass = Class.forName( listenerClassName );
            if( !tabChangeListenerClass.isAssignableFrom(listenerClass) ) {
                throw new TagAttributeException(
                        tag, typeTagAttribute,
                        "The class, as given by tabChangeListener tag's type " +
                        "attribute, must implement TabChangeListener: " +
                        listenerClassName);
            }
throw new UnsupportedOperationException("com.sun.facelets.tag.jsf.ComponentSupport");
//            if( ComponentSupport.isNew(parent) ) {
//                // TabChangeListener instance
//                Object listenerObject = listenerClass.newInstance();
//                addTabChangeListenerMethod.invoke(
//                        parent, new Object[]{listenerObject} );
//            }
        }
        catch(Exception e) {
            throw new TagAttributeException(
                    tag, typeTagAttribute,
                    "Could not either find, or instantiate, or add as a " +
                    "listener, the class described by tabChangeListener " +
                    "tag's type attribute: " + listenerClassName, e);
        }

        //TODO Use nextHandler? ice:tabChangeListener can't have children
    }
}
