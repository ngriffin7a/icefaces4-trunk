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

package org.icefaces.ace.component.tabset;

import javax.el.MethodExpression;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import javax.faces.component.UIComponent;

import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.ace.meta.annotation.*;
import org.icefaces.resources.ICEResourceLibrary;

import java.util.List;

@Component(
    tagName = "tabSet",
    componentClass  = "org.icefaces.ace.component.tabset.TabSet",
    rendererClass   = "org.icefaces.ace.component.tabset.TabSetRenderer",
    generatedClass  = "org.icefaces.ace.component.tabset.TabSetBase",
    extendsClass    = "javax.faces.component.UIComponentBase",
    componentType   = "org.icefaces.ace.component.TabSet",
    rendererType    = "org.icefaces.ace.component.TabSetRenderer",
    componentFamily = "org.icefaces.ace.TabSet",
    tlddoc = "<p>The TabSet component is a container for its TabPane children, " +
        "each of which may contain any arbitrary components. Only one " +
        "TabPane component is currently active, and its contents shown, " +
        "at any given time. The TabSet may operate in a server-side mode, " +
        "where only the current TabPane's contents exist in the browser; or " +
        "in client-side mode, where every TabPane's contents exist in the " +
        "browser, and no server round-trip is necessary to change TabPanes. " +
        "The entire TabSet may exist within a single parent form, so that " +
        "validation will apply to all of its contents, and so that " +
        "validation may enforce remaining on the current TabPane if the " +
        "user attempts to change the selected TabPane while other input " +
        "components are in an invalid state. Also, a TabSet may exist " +
        "outside of any form, perhaps with each TabPane containing their " +
        "own child form, so that validation may be more limited in scope. " +
        "In this case, a TabSetProxy may be used, in conjunction with the " +
        "TabSet, so that the TabSetProxy may be placed in a form, for " +
        "communicating with the server, removing the need for the TabSet " +
        "itself to be in a form. When changing the selected TabPane, the " +
        "TabSet may use application configurable animations to accentuate " +
        "the transition from the previously selected TabPane to the newly " +
        "selected TabPane. The label portion of the TabPanes may be shown " +
        "on the bottom, top, left, or right of the TabSet. To support the " +
        "capability of dynamically adding and removing TabPane(s) without " +
        "updating the entire TabSet, there is a side-effect where any " +
        "iframe(s) within them can get loaded twice in rapid succession, " +
        "when first shown or subsequently updated. To eliminate the " +
        "redundant load by deferring the loading until the TabSet is ready, " +
        "do not use a src attribute on the iframe(s), but rather set the " +
        "org.icefaces.ace.component.tabset.deferred_src attribute to what " +
        "src would have been.<p>For more information, see the " +
        "<a href=\"http://wiki.icefaces.org/display/ICE/TabSet\">TabSet Wiki Documentation</a>."
)
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
    @ICEResourceDependency(name = ACEResourceNames.JQUERY_JS),
    @ICEResourceDependency(name = "util/ace-yui.js")
})
@ClientBehaviorHolder(events = {
    @ClientEvent(name="clientSideTabChange",
        javadoc="Fired when the tabSet has clientSide=true, and a tab " +
            "change occurs. Use onstart=\"return false;\" to limit " +
            "javascript execution.",
        tlddoc="Fired when the tabSet has clientSide=true, and a tab " +
            "change occurs. Use onstart=\"return false;\" to limit " +
            "javascript execution.",
        defaultExecute="@none", defaultRender="@none"),
    @ClientEvent(name="serverSideTabChange",
        javadoc="Fired when the tabSet has clientSide=false, and a tab " +
            "change occurs (default event).",
        tlddoc="Fired when the tabSet has clientSide=false, and a tab " +
            "change occurs (default event).",
        defaultExecute="@all", defaultRender="@all")
}, defaultEvent="serverSideTabChange")
public class TabSetMeta extends UIComponentBaseMeta {

    @Property(tlddoc="If true (and/or cancelOnInvalid is false) then " +
        "tabChangeListener will be invoked in APPLY_REQUEST_VALUES phase, " +
        "otherwise INVOKE_APPLICATION phase.",
        defaultValue="false")
    private boolean immediate;

    @Property(tlddoc="The index of the current selected tab.",
        defaultValue="0")
    private int selectedIndex;

    @Property(tlddoc="Where the clickable TabPane labels are shown. " +
        "Valid values are bottom, top, left and right.",
        defaultValue="top")
    private String orientation;

    @Property(tlddoc="This component supports both client and server side " +
        "tab change models. When clientSide is true, then the contents of " +
        "all TabPanes get rendered on the client and clientSideTabChange " +
        "will fire when changing the selected TabPane. Otherwise, in " +
        "server side mode, typically only the selected TabPane contents " +
        "will get rendered to the client, depending on the TabPane cache " +
        "property, and serverSideTabChange will fire or the form will be " +
        "submitted, so that the tabChangeListener will be invoked, and the " +
        "newly selected TabPane contents may be sent to the browser.",
        defaultValue="false")
    private boolean clientSide;

    @Property(tlddoc="<p>Controls how input component validation affects " +
        "changing the selected TabPane. When false, then irrespective of " +
        "immediate, selectedIndex will be set and tabChangeListener will be " +
        "invoked in APPLY_REQUEST_VALUES phase. PROCESS_VALIDATIONS phase " +
        "will still execute and create any FacesMessage(s), but won't " +
        "interfere with the changing TabPane selection. " +
        "<p>Otherwise, when true, then it depends on immediate. When " +
        "immediate is true, selectedIndex will be set and tabChangeListener " +
        "will be invoked in APPLY_REQUEST_VALUES phase, but " +
        "PROCESS_VALIDATIONS phase will not execute, so no FacesMessage(s) " +
        "can be created. When immediate is false, then selectedIndex will " +
        "be set in UPDATE_MODEL and tabChangeListener will be invoked in " +
        "INVOKE_APPLICATION, and so any validation error in " +
        "PROCESS_VALIDATIONS will stop the changing of the selected TabPane.",
        defaultValue="true")
    private boolean cancelOnInvalid;

    @Property(tlddoc="Custom CSS style class(es) to use for this " +
        "component. These style classes can be defined in your page or in " +
        "a theme CSS file.")
    private String styleClass;

    @Property(tlddoc="Custom inline CSS styles to use for this component. " +
        "These styles are generally applied to the root DOM element of the " +
        "component. This is intended for per-component basic style " +
        "customizations. Note that due to browser CSS precedence rules, CSS " +
        "rendered on a DOM element will take precedence over the external " +
        "stylesheets used to provide the ThemeRoller theme on this " +
        "component. If the CSS properties applied with this attribute do " +
        "not affect the DOM element you want to style, you may need to " +
        "create a custom theme styleClass for the theme CSS class that " +
        "targets the particular DOM elements you wish to customize.")
    private String style;

    @Property(tlddoc="MethodExpression representing a method that will be " +
        "invoked when the selected TabPane has changed. The expression " +
        "must evaluate to a public method that takes a ValueChangeEvent " +
        "parameter, with a return type of void.",
        expression= Expression.METHOD_EXPRESSION,
        methodExpressionArgument="javax.faces.event.ValueChangeEvent")
    private MethodExpression tabChangeListener;

    @Property(tlddoc = "If true then all tabs except the active one will " +
        "be disabled and can not be selected.")
    private boolean disabled;

	@Property(tlddoc="The effect when showing the contents of a tab after selecting it.")
	private String showEffect;

    @Property(tlddoc="The duration of the show effect in milliseconds. The " +
        "default value varies depending on the effect.")
	private int showEffectLength;


    @Field(javadoc="Maintains the record of which tabs have been visited")
    private List visitedTabClientIds;

    @Field(javadoc="This retains the selectedIndex from the beginning of " +
        "the lifecycle, so that when in server mode, only this tabPane's " +
        "contents will execute.")
    private Integer preDecodeSelectedIndex;

}
