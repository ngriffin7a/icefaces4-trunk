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
package org.icefaces.mobi.component.smallview;

import org.icefaces.ace.meta.annotation.Component;

@Component(
        tagName = "smallView",
        componentClass = "org.icefaces.mobi.component.smallview.SmallView",
        rendererClass = "org.icefaces.mobi.component.smallview.SmallViewRenderer",
        generatedClass = "org.icefaces.mobi.component.smallview.SmallViewBase",
        handlerClass = "org.icefaces.mobi.component.smallview.SmallViewHandler",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentType = "org.icefaces.SmallView",
        rendererType = "org.icefaces.ViewRenderer",
        componentFamily = "org.icefaces.SmallView",
        tlddoc = "SmallView will include and render the child components when the client is a handheld."
)

public class SmallViewMeta {

}
