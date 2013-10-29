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

package org.icefaces.ace.component.simpleselectonemenu;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Field;
import org.icefaces.ace.meta.baseMeta.UISelectOneMeta;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;

import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import javax.el.ValueExpression;
import javax.el.MethodExpression;

import java.util.List;

@Component(
        tagName = "simpleSelectOneMenu",
        componentClass = "org.icefaces.ace.component.simpleselectonemenu.SimpleSelectOneMenu",
        generatedClass = "org.icefaces.ace.component.simpleselectonemenu.SimpleSelectOneMenuBase",
        extendsClass = "javax.faces.component.UISelectOne",
		rendererClass   = "org.icefaces.ace.component.simpleselectonemenu.SimpleSelectOneMenuRenderer",
        componentFamily = "org.icefaces.ace.SimpleSelectOneMenu",
        componentType = "org.icefaces.ace.component.SimpleSelectOneMenu",
		rendererType    = "org.icefaces.ace.component.SimpleSelectOneMenuRenderer",
        tlddoc = "A simple selection component that uses the browser's native \"select\" element and integrates with the ACE environment, supporting Themeroller, ARIA, label positioning, and ace:ajax." +
                 "<p>For more information, see the " +
                 "<a href=\"http://wiki.icefaces.org/display/ICE/SimpleSelectOneMenu\">SimpleSelectOneMenu Wiki Documentation</a>."
)
@ICEResourceDependencies({
	@ICEResourceDependency(library = "icefaces.ace", name = "util/ace-jquery.js")
})
@ClientBehaviorHolder(events = {
	@ClientEvent( name="valueChange",
		javadoc="Fired whenever the value of the component changes.",
		tlddoc="Fired whenever the value of the component changes.",
		defaultRender="@all", defaultExecute="@this" ),
	@ClientEvent( name="blur",
		javadoc="Fired any time the component loses focus.",
		tlddoc="Fired any time the component loses focus.",
		defaultRender="@all", defaultExecute="@this" )},
	defaultEvent="valueChange" )
public class SimpleSelectOneMenuMeta extends UISelectOneMeta {

    @Property(tlddoc = "Style class name of the container element.", defaultValue="")
    private String style;
	
    @Property(tlddoc = "Style class name of the container element.", defaultValue="")
    private String styleClass;
	
    @Property(tlddoc = "Indicator indicating that the user is required to provide a submitted value for this input component.")
    private String requiredIndicator;

    @Property(tlddoc = "Indicator indicating that the user is NOT required to provide a submitted value for this input component.")
    private String optionalIndicator;

    @Property(tlddoc = "Position of label relative to input field. Supported values are \"left/right/top/bottom/none\". Default is \"none\".")
    private String labelPosition;
	
    @Property(tlddoc = "A localized user presentable name for this component.")
    private String label;

    @Property(tlddoc = "Position of input-required or input-optional indicator relative to input field or label. " +
            "Supported values are \"left/right/top/bottom/labelLeft/labelRight/none\". " +
            "Default is \"right\".")
    private String indicatorPosition;
	
	@Property(tlddoc = "Flag indicating that this element must never receive focus or be included in a subsequent submit. A value of false causes no attribute to be rendered, while a value of true causes the attribute to be rendered as disabled=\"disabled\".", defaultValue="false")
	private boolean disabled;
	
	@Property(tlddoc = "Flag indicating that this component will prohibit changes by the user. The element may receive focus unless it has also been disabled. A value of false causes no attribute to be rendered, while a value of true causes the attribute to be rendered as readonly=\"readonly\".", defaultValue="false")
	private boolean readonly;
	
	@Property(tlddoc = "Access key that, when pressed, transfers focus to this element.")
	private String accesskey;
	
	@Property(tlddoc = "Direction indication for text that does not inherit directionality. Valid values are \"LTR\" (left-to-right) and \"RTL\" (right-to-left).")
	private String dir;
	
	@Property(tlddoc = "Code describing the language used in the generated markup for this component.")
	private String lang;
	
	@Property(tlddoc = "Position of this element in the tabbing order for the current document. This value must be an integer between 0 and 32767.")
	private String tabindex;
	
	@Property(tlddoc = "Advisory title information about markup elements generated for this component.")
	private String title;
	
    @Field()
    private List itemList;
}
