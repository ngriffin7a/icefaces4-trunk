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

package org.icefaces.ace.component.submitmonitor;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Facet;
import org.icefaces.ace.meta.annotation.Facets;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.DefaultValueType;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;

import javax.faces.component.UIComponent;
import java.lang.Boolean;
import java.lang.String;

@Component(
    tagName = "submitMonitor",
    componentClass  = "org.icefaces.ace.component.submitmonitor.SubmitMonitor",
    rendererClass   = "org.icefaces.ace.component.submitmonitor.SubmitMonitorRenderer",
    generatedClass  = "org.icefaces.ace.component.submitmonitor.SubmitMonitorBase",
    extendsClass    = "javax.faces.component.UIComponentBase",
    componentType   = "org.icefaces.ace.component.SubmitMonitor",
    rendererType    = "org.icefaces.ace.component.SubmitMonitorRenderer",
    componentFamily = "org.icefaces.ace.SubmitMonitor",
    tlddoc = "Monitors submits to the server, and indicates the status of " +
        "the submits, server and network connection, and session validity. " +
        "Examples of supported submit methods are: components doing full " +
        "form submits, singleSubmit(s) such as via icecore:singleSubmit, " +
        "partialSubmit(s) by ice: components, f:ajax submits by h: " +
        "components, ace:ajax submits by ace: components, and direct use " +
        "of ICEfaces javascript submit APIs. " +
        "Supports configurable text labels and image state indicators, or " +
        "facets for a fully configurable UI. Optionally uses an overlay for " +
        "UI blocking during submits. Can be set to monitor a portion of " +
        "the page, in tandem with other submitMonitors each monitoring " +
        "their own distinct portions.<p>For more information, see the " +
        "<a href=\"http://wiki.icefaces.org/display/ICE/SubmitMonitor\">" +
        "SubmitMonitor Wiki Documentation</a>."
)
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
     @ICEResourceDependency(name = ACEResourceNames.COMPONENTS_JS)
})
public class SubmitMonitorMeta extends UIComponentBaseMeta {
    @Property(tlddoc = "Label to be displayed on the submitMonitor when no " +
        "submit is in progress. This is only shown if blockUI=\"@none\", " +
        "since with the popup cases, the submitMonitor is not shown when idle.")
    String idleLabel;

    @Property(tlddoc = "Label to be displayed on the submitMonitor while a submit is in progress.")
    String activeLabel;

    @Property(tlddoc = "Label to be displayed on the submitMonitor when there is a server error.")
    String serverErrorLabel;

    @Property(tlddoc = "Label to be displayed on the submitMonitor when there is a network error.")
    String networkErrorLabel;

    @Property(tlddoc = "Label to be displayed on the submitMonitor when the session is expired.")
    String sessionExpiredLabel;

    @Property(name="for", tlddoc = "Specify space separated list of " +
        "components, by their for property format search strings, so that those " +
        "components and, recursively, all of their children, will be " +
        "monitored by this component when they act as the source for " +
        "submits. When this property is empty or unspecified, this " +
        "component will monitor all submits from all sources. This property " +
        "allows for different submitMonitor components to co-exist on a " +
        "page, and each monitor different, non-overlapping, regions of the " +
        "page, and be shown with their own unique labels etc.")
    String For;

    @Property(tlddoc = "When enabled, display a translucent overlay on a " +
        "portion of the window, and only show the submitMonitor UI when the " +
        "connection is not idle and a submit is underway. This property " +
        "specifies on what portion of the window to show the overlay: " +
        "\"@all\" means the whole document body, \"@source\" means only over " +
        "the component that originated the request, or a for style " +
        "component search string may be given to specify a component. " +
        "Finally, \"@none\" means to disable the overlay and have the " +
        "submitMonitor UI always present where it has been placed in the " +
        "page.",
        defaultValue = "@all",
        defaultValueType = DefaultValueType.STRING_LITERAL)
    String blockUI;

    @Property(tlddoc = "When blockUI is enabled, and this property is true, " +
        "the submitMonitor will display centered over the translucent overlay.",
        defaultValue = "true", defaultValueType = DefaultValueType.EXPRESSION)
    Boolean autoCenter;

    @Property(tlddoc="Style class of the container element.")
    String styleClass;

    @Property(tlddoc="When using images to indicate a server or network " +
        "error, they may need to be preloaded while the server is still " +
        "accessible. This preloads theme images when facets are not used.",
        defaultValue="true", defaultValueType = DefaultValueType.EXPRESSION)
    Boolean preload;

    @Facets
    class FacetsMeta {
        @Facet(tlddoc = "Allows rendering of nested components as label for idle state.")
        UIComponent idle;

        @Facet(tlddoc = "Allows rendering of nested components as label for active state.")
        UIComponent active;

        @Facet(tlddoc = "Allows rendering of nested components as label for serverError state.")
        UIComponent serverError;

        @Facet(tlddoc = "Allows rendering of nested components as label for networkError state.")
        UIComponent networkError;

        @Facet(tlddoc = "Allows rendering of nested components as label for sessionExpired state.")
        UIComponent sessionExpired;
    }
}

