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
package org.icefaces.mobi.component.largeview;

import org.icefaces.ace.meta.annotation.Component;

@Component(
        tagName = "largeView",
        componentClass = "org.icefaces.mobi.component.largeview.LargeView",
        rendererClass = "org.icefaces.mobi.component.largeview.LargeViewRenderer",
        generatedClass = "org.icefaces.mobi.component.largeview.LargeViewBase",
        handlerClass = "org.icefaces.mobi.component.largeview.LargeViewHandler",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentType = "org.icefaces.LargeView",
        rendererType = "org.icefaces.ViewRenderer",
        componentFamily = "org.icefaces.LargeView",
        tlddoc = "LargeView will include and render the child components when " +
                "the client is a desktop or tablet view."
)

public class LargeViewMeta {

}