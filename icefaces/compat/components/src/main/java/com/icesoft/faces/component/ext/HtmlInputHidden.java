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

package com.icesoft.faces.component.ext;


import org.icefaces.resources.BrowserType;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

/**
 * This is an extension of javax.faces.component.html.HtmlInputHidden, which
 * provides design-time support for SunStudioCreator 2
 */
@ICEResourceDependencies({
	@ICEResourceDependency(name="icefaces-compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={}),
	@ICEResourceDependency(name="compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={})
})

public class HtmlInputHidden
        extends javax.faces.component.html.HtmlInputHidden {

    public static final String COMPONENT_TYPE =
            "com.icesoft.faces.HtmlInputHidden";
    public static final String RENDERER_TYPE = "com.icesoft.faces.Hidden";

    public HtmlInputHidden() {
        super();
        setRendererType(RENDERER_TYPE);
    }

    public String getRendererType() {
        return RENDERER_TYPE;
    }

}
