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

package org.icefaces.ace.component.richtextentry;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIInputMeta;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;
import org.icefaces.resources.ICEResourceLibrary;

@Component(
        tagName = "richTextEntry",
        componentClass = "org.icefaces.ace.component.richtextentry.RichTextEntry",
        generatedClass = "org.icefaces.ace.component.richtextentry.RichTextEntryBase",
        extendsClass = "javax.faces.component.UIInput",
        componentFamily = "org.icefaces.ace.component.Menu",
        componentType = "org.icefaces.ace.component.RichTextEntry",
        rendererClass = "org.icefaces.ace.component.richtextentry.RichTextEntryRenderer",
        rendererType = "org.icefaces.ace.component.RichTextEntryRenderer",
        tlddoc = "RichTextEntry uses the CKEditor API to provide JSF based rich text editor component." +
                 "<p>For more information, see the " +
                 "<a href=\"http://wiki.icefaces.org/display/ICE/RichTextEntry\">RichTextEntry Wiki Documentation</a>."
)
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
    @ICEResourceDependency(name = "richtextentry/richtextentry.js")
})
@ClientBehaviorHolder(events = {
	@ClientEvent( name="save",
		javadoc="Fired when the 'Save' button is clicked and the contents of the editor are saved.",
		tlddoc="Fired when the 'Save' button is clicked and the contents of the editor are saved.",
		defaultRender="@all", defaultExecute="@this" ),
	@ClientEvent( name="blur",
		javadoc="Fired any time the editor loses focus.",
		tlddoc="Fired any time the editor loses focus.",
		defaultRender="@all", defaultExecute="@this" )},
	defaultEvent="save" )
public class RichTextEntryMeta extends UIInputMeta {

    @Property(tlddoc = "Specifies the language to be used for the user interface.", defaultValue="en")
    private String language;
	
    @Property(tlddoc = "Inline CSS styling for the editor.")
    private String style;
	
    @Property(tlddoc = "The CSS style class of the editor.")
    private String styleClass;
	
    @Property(tlddoc = "The width of the editor. It can be expressed in the following units: px, em, %. If no unit is specified, the value is assumed to be in pixels.", defaultValue="100%")
    private String width;
	
    @Property(tlddoc = "The height of the editor. It can be expressed in the following units: px, em, %. If no unit is specified, the value is assumed to be in pixels.", defaultValue="100%")
    private String height;
	
    @Property(tlddoc = "Specifies the type of toolbar. There are two valid values for this field (case sensitive): 'Default' and 'Basic'.", defaultValue="Default")
    private String toolbar;

    @Property(tlddoc = "This attribute defines the path of the custom config file, the path is relative to the web app.")
    private String customConfigPath;

    @Property(tlddoc = "Specify the pre-defined skin for the editor. Possible values are 'v2', 'office2003', and 'kama' ('default' is synonymous of 'v2').", defaultValue="default")
    private String skin;
	
    @Property(tlddoc = "Boolean value to disable and re-enable the component.", defaultValue="false")
    private boolean disabled;
	
    @Property(tlddoc = "By default the data of the editor will only be saved when its 'Save' button is clicked. Any other submit request will not save its data. In order to save its data on any submit request like inputText does, this attribute can be set to true.", defaultValue="false")
    private boolean saveOnSubmit;
}
