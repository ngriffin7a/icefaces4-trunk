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

package com.icesoft.faces.component.util;

import java.util.HashMap;
import java.util.Map;
/*
 * This class is created to give a java interface to the comma separated values
 * support format: key!value, key!value, ....
 * Along with this class a javascript utility added names as "Ice.delimitedProperties" 
 * which allows to create above formated string via object based API.  
 */
public class DelimitedProperties {
    Map properties = new HashMap();
    public DelimitedProperties(String rawProps) {
        if (null == rawProps || "".equals(rawProps))return;
        String[] props = rawProps.split(",");
        for (int i=0; i < props.length; i++) {
            String prop = props[i];
            String key = prop.substring(0, prop.indexOf("!"));
            String value = prop.substring(prop.indexOf("!")+1); 
            properties.put(key, value);
        }
    }
    
    public String get(String key) {
        return (String)properties.get(key); 
    }
    
    public void set(String key, String value) {
        properties.put(key, value);
    }
    
    public Map getProperties() {
        return properties;
    }
}
