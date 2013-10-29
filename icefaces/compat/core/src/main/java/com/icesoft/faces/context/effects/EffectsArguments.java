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

package com.icesoft.faces.context.effects;

import com.icesoft.faces.util.CoreUtils;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.*;

/**
 * Utility class to set arguments for effects
 */
public class EffectsArguments implements Serializable{

    private Map map = new HashMap();
    private List parameter = new ArrayList();
    private NumberFormat floatFormat = NumberFormat.getNumberInstance(Locale.US);
    private String options = null;

    public EffectsArguments(){
        floatFormat.setGroupingUsed(false);
    }


    public void setParameter(int i, float f) {
        addParameter(i, floatFormat.format(f));
    }

    public void addParameter(int i, String value) {
        if (i == parameter.size()) {
            parameter.add(i, value);
        } else {
            parameter.set(i, value);
        }
    }

    public void add(String argument, String value) {
        if (value == null) {
            return;
        }
        map.put(argument, "'" + value + "'");
    }

    public void setOptions(String value){
        options = value;
    }

    public void add(String argument, float value) {
        map.put(argument, floatFormat.format(value));
    }

    public void addFunction(String argument, String function) {
        map.put(argument, function);
    }

    public void add(String arg, boolean value) {

        map.put(arg, value + "");
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }


    public String toString() {
        if(options != null){
            // To prevent javascript injection
            options = options.replace(')', ' ');
            options = options.replace('}', ' ');
        }
        StringBuffer sb = new StringBuffer(",{");
        Iterator iter = parameter.iterator();
        if (iter.hasNext()) {
            sb.append(", ");
        }
        while (iter.hasNext()) {
            String value = (String) iter.next();
            sb.append("'").append(value).append("'");
            if (iter.hasNext()) {
                sb.append(",");
            }
        }
        if (map.isEmpty()) {
            if(options != null){
                return ",{ " +options + "})";
            }
            return ");";
        }

        iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String arg = (String) iter.next();
            String value = (String) map.get(arg);
            sb.append(arg).append(":").append(value);
            if (iter.hasNext()) {
                sb.append(",");
            }
        }
        if(options != null){
            sb.append(",").append(options);
        }
        sb.append("});");
        
        return sb.toString();
    }
    
    public boolean equals(Object obj) {
        if (!(obj instanceof EffectsArguments)) {
            return false;
        }
        EffectsArguments ea = (EffectsArguments) obj;
        if (!CoreUtils.objectsEqual(map, ea.map)) {
            return false;
        }
        if (!CoreUtils.objectsEqual(parameter, ea.parameter)) {
            return false;
        }
        if (!CoreUtils.objectsEqual(options, ea.options)) {
            return false;
        }
        return true;
    }
 }
