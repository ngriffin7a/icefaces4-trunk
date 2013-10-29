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

package org.icefaces.ace.component.progressbar;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import javax.el.MethodExpression;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;
import org.icefaces.resources.ICEResourceLibrary;

@Component(
        tagName = "progressBar",
        componentClass = "org.icefaces.ace.component.progressbar.ProgressBar",
        generatedClass = "org.icefaces.ace.component.progressbar.ProgressBarBase",
        extendsClass = "javax.faces.component.UIComponentBase",
        rendererClass = "org.icefaces.ace.component.progressbar.ProgressBarRenderer",
        componentFamily = "org.icefaces.ace.component.ProgressBar",
        componentType = "org.icefaces.ace.component.ProgressBar",
        rendererType = "org.icefaces.ace.component.ProgressBarRenderer",
        tlddoc = "The Progress Bar is a process status indicator that can either work purely on client side or " +
                 "interact with server side using ajax." +
                 "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/ProgressBar\">ProgressBar Wiki Documentation</a>.")
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
    @ICEResourceDependency(name = ACEResourceNames.COMPONENTS_JS)
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="complete", javadoc="Fired when the value of the progressbar reaches the maximum value (default event).", tlddoc="Fired when the value of the progressbar reaches the maximum value (default event).", defaultRender="@all", defaultExecute="@this"),
	@ClientEvent(name="cancel", javadoc="Fired when the progress process is cancelled by calling cancel().", tlddoc="Fired when the progress process is cancelled by calling cancel().", defaultRender="@all", defaultExecute="@this"),
	@ClientEvent(name="change", javadoc="Fired when the value of the progressbar changes.", tlddoc="Fired when the value of the progressbar changes.", defaultRender="@all", defaultExecute="@this")
}, defaultEvent="complete")
public class ProgressBarMeta extends UIComponentBaseMeta {
    @Property(tlddoc = "Name of the client side widget.")
    private String widgetVar;

    @Property(defaultValue = "0", tlddoc = "Value of the progress bar.")
    private int value;

    @Property(tlddoc = "Disables or enables the progressbar.")
    private boolean disabled;

    @Property(tlddoc = "If true, you can use start() to start polling sever for progress value.")
    private boolean usePolling;

    @Property(defaultValue = "3000", tlddoc = "Interval in milliseconds to do polling.")
    private int pollingInterval;

    @Property(tlddoc = "Inline style of the main container element.")
    private String style;

    @Property(tlddoc = "Style class of the main container element.")
    private String styleClass;

    @Property(expression = Expression.METHOD_EXPRESSION,
              tlddoc = "A server side listener to be invoked when a progress is completed.")
    private MethodExpression completeListener;

    @Property(expression = Expression.METHOD_EXPRESSION, methodExpressionArgument = "org.icefaces.ace.event.ProgressBarChangeEvent",
            tlddoc = "A server side listener to be invoked when the value of the progress bar changes.")
    private MethodExpression changeListener;

    @Property(expression = Expression.METHOD_EXPRESSION,
              tlddoc = "A server side listener to be invoked when a progress is cancelled.")
    private MethodExpression cancelListener;
}
