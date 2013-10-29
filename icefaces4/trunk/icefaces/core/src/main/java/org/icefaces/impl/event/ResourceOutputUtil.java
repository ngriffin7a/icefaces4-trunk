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

package org.icefaces.impl.event;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import java.util.Map;

public class ResourceOutputUtil {

    public static final String SCRIPT_RENDERER = "javax.faces.resource.Script";
    public static final String STYLE_RENDERER = "javax.faces.resource.Stylesheet";

    public static UIComponent createResourceComponent(String name, String lib, String rendererType, boolean isTransient){
        UIOutput res = new UIOutput();
        res.setRendererType(rendererType);
        res.setTransient(isTransient);
        Map attrs = res.getAttributes();
        attrs.put("name", name);
        if (lib != null && lib.trim().length() > 0) {
            attrs.put("library", lib);
        }
        attrs.put("version", "fubar");
        return res;
    }

    public static UIComponent createScriptResourceComponent(String name, String lib, boolean isTransient){
        return createResourceComponent(name,lib,SCRIPT_RENDERER, isTransient);
    }

    public static UIComponent createStyleResourceComponent(String name, String lib, boolean isTransient){
        return createResourceComponent(name,lib,STYLE_RENDERER, isTransient);
    }

    public static UIComponent createTransientScriptResourceComponent(String name, String lib){
        return createResourceComponent(name,lib,SCRIPT_RENDERER, true);
    }

    public static UIComponent createTransientStyleResourceComponent(String name, String lib){
        return createResourceComponent(name,lib,STYLE_RENDERER, true);
    }

}
