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

package org.icefaces.ace.component.animation;

import javax.el.ValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.context.FacesContext;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BehaviorBase extends ClientBehaviorBase {

    protected Map<String, ValueExpression> bindings;


    public Object saveBindingsState(FacesContext context) {

        if (bindings == null) {
            return (null);
        }

        Object values[] = new Object[2];
        values[0] = bindings.keySet().toArray(new String[bindings.size()]);

        Object[] bindingValues = bindings.values().toArray();
        for (int i = 0; i < bindingValues.length; i++) {
            bindingValues[i] = saveAttachedState(context,
                                                 bindingValues[i]);
        }
        values[1] = bindingValues;
        return (values);
    }

    public static Map<String, ValueExpression> restoreBindingsState(FacesContext context, Object state) {

        if (state == null) {
            return (null);
        }
        Object values[] = (Object[]) state;
        String names[] = (String[]) values[0];
        Object states[] = (Object[]) values[1];
        Map<String, ValueExpression> bindings = new HashMap<String, ValueExpression>(names.length);
        for (int i = 0; i < names.length; i++) {
            bindings.put(names[i],
                         (ValueExpression) restoreAttachedState(context, states[i]));
        }
        return (bindings);

    }

    public static Object restoreAttachedState(FacesContext context,
                                              Object stateObj)
            throws IllegalStateException {
        if (null == context) {
            throw new NullPointerException();
        }
        if (null == stateObj) {
            return null;
        }
        Object result;

        if (stateObj instanceof List) {
            List<BehaviorStateHolderSaver> stateList = (List<BehaviorStateHolderSaver>) stateObj;
            BehaviorStateHolderSaver collectionSaver = stateList.get(0);
            Class mapOrCollection = (Class) collectionSaver.restore(context);
            if (Collection.class.isAssignableFrom(mapOrCollection)) {
                Collection<Object> retCollection = null;
                try {
                    retCollection = (Collection<Object>) mapOrCollection.newInstance();
                } catch (Exception e) {
                    throw new IllegalStateException("Unknown object type: " + e);
                }
                for (int i = 1, len = stateList.size(); i < len; i++) {
                    try {
                        retCollection.add(stateList.get(i).restore(context));
                    } catch (ClassCastException cce) {
                        throw new IllegalStateException("Unknown object type: " + cce);
                    }
                }
                result = retCollection;
            } else {
                // If we were doing assertions: assert(mapOrList.isAssignableFrom(Map.class));
                Map<Object, Object> retMap = null;
                try {
                    retMap = (Map<Object,Object>) mapOrCollection.newInstance();
                } catch (Exception e) {
                    throw new IllegalStateException("Unknown object type");
                }
                for (int i = 1, len = stateList.size(); i < len; i+=2) {
                    try {
                        retMap.put(stateList.get(i).restore(context),
                                   stateList.get(i+1).restore(context));
                    } catch (ClassCastException cce) {
                        throw new IllegalStateException("Unknown object type: " + cce);
                    }
                }
                result = retMap;

            }
        } else if (stateObj instanceof BehaviorStateHolderSaver) {
            BehaviorStateHolderSaver saver = (BehaviorStateHolderSaver) stateObj;
            result = saver.restore(context);
        } else {
            throw new IllegalStateException("Unknown object type: " + stateObj.getClass().getName());
        }
        return result;
    }

    public static Object saveAttachedState(FacesContext context,
                                           Object attachedObject) {
        if (null == context) {
            throw new NullPointerException();
        }
        if (null == attachedObject) {
            return null;
        }
        Object result;
        Class mapOrCollectionClass = attachedObject.getClass();
        boolean newWillSucceed = true;
        // first, test for newability of the class.
        try {
            int modifiers = mapOrCollectionClass.getModifiers();
            newWillSucceed = Modifier.isPublic(modifiers);
            if (newWillSucceed) {
                newWillSucceed = null != mapOrCollectionClass.getConstructor();
            }
        } catch (Exception e) {
            newWillSucceed = false;
        }


        if (newWillSucceed && attachedObject instanceof Collection) {
            Collection attachedCollection = (Collection) attachedObject;
            List<BehaviorStateHolderSaver> resultList = null;
            for (Object item : attachedCollection) {
                if (item != null) {
                    if (item instanceof StateHolder && ((StateHolder) item).isTransient()) {
                        continue;
                    }
                    if (resultList == null) {
                        resultList = new ArrayList<BehaviorStateHolderSaver>(attachedCollection.size() + 1);
                        resultList.add(new BehaviorStateHolderSaver(context, mapOrCollectionClass));
                    }
                    resultList.add(new BehaviorStateHolderSaver(context, item));
                }
            }
            result = resultList;
        } else if (newWillSucceed && attachedObject instanceof Map) {
            Map<Object, Object> attachedMap = (Map<Object, Object>) attachedObject;
            List<BehaviorStateHolderSaver> resultList = null;
            Object key, value;
            for (Map.Entry<Object, Object> entry : attachedMap.entrySet()) {
                key = entry.getKey();
                if (key instanceof StateHolder && ((StateHolder)key).isTransient()) {
                    continue;
                }
                value = entry.getValue();
                if (value instanceof StateHolder && ((StateHolder)value).isTransient()) {
                    continue;
                }
                if (resultList == null) {
                    resultList = new ArrayList<BehaviorStateHolderSaver>(attachedMap.size()*2 + 1);
                    resultList.add(new BehaviorStateHolderSaver(context, mapOrCollectionClass));
                }
                resultList.add(new BehaviorStateHolderSaver(context, key));
                resultList.add(new BehaviorStateHolderSaver(context, value));
            }
            result = resultList;
        } else {
            result = new BehaviorStateHolderSaver(context, attachedObject);
        }

        return result;
    }
}
