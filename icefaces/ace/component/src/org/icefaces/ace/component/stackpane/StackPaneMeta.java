/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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
package org.icefaces.ace.component.stackpane;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Implementation;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;


@Component(
        tagName = "stackPane",
        componentClass = "org.icefaces.ace.component.stackpane.StackPane",
        rendererClass = "org.icefaces.ace.component.stackpane.StackPaneRenderer",
        generatedClass = "org.icefaces.ace.component.stackpane.StackPaneBase",
        handlerClass = "org.icefaces.ace.component.stackpane.StackPaneHandler",
        componentType = "org.icefaces.StackPane",
        rendererType = "org.icefaces.StackPaneRenderer",
        extendsClass = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.StackPane",
        tlddoc = "This component is a child of panelStack, " +
                " which implements StackPaneController.  " +
                "The facelet attribute determines whether the children of this component are constructed in the " +
                "server side component tree. A special tag handler is utilised to assist with this task.  The desired " +
                "outcome of this feature is to allow developers to maintain as small a server-side component tree as " +
                "possible for purposes of increased scalability, without having to worry about the combinatory " +
                "use of ui include and c forEach tags used with components." +
                "The facelet attribute defaults to false meaning that this panel is constructed " +
                "and present in server side component tree. If facelet is true (can only be so if client is false), " +
                "then the stackPane is only constructed if it is the selected panel in the panelStack." +
                "If the client attribute is true, this means children of this component are not only constructed but " +
                "area also always rendered on the client --best for static data. "
)

public class StackPaneMeta extends UIPanelMeta{

    @Property(tlddoc="Style to apply to the container element.")
    private String style;

    @Property(tlddoc="Style class to apply to the container element.")
    private String styleClass;

    @Property(defaultValue="false", implementation = Implementation.EXISTS_IN_SUPERCLASS,
               tlddoc = "If this attribute is \"true\" the component utilizes the StackPaneHandler to optimise " +
                       "server-side performance by reducing the size of the server-side component tree. " +
                       "Any non-selected stackPane will not have its children added to the component tree. " +
                       "If this attribute evaluates to \"false\", then normal jsf construction of the component tree occurs. " +
                       "Must have client=\"false\" which is the default value for that attribute.. " +
                       "Default value for this attribute is false.")
    private boolean facelet;

    @Property(defaultValue="false",
             tlddoc = "If this attribute evaluates to \"true\", the stackPane's children are always rendered to the client/browser. " +
                     "This is ideal for static content. " +
                     "The facelet attribute is only relevant if this attribute is false.")
    private boolean client;

}
