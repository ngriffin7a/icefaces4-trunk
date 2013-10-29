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

package org.icefaces.ace.component.menuitem;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UICommandMeta;

import org.icefaces.resources.ICEResourceDependencies;

import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;

@Component(
        tagName = "menuItem",
        componentClass = "org.icefaces.ace.component.menuitem.MenuItem",
        generatedClass = "org.icefaces.ace.component.menuitem.MenuItemBase",
        extendsClass = "javax.faces.component.UICommand",
        componentFamily = "org.icefaces.ace.component.Menu",
        componentType = "org.icefaces.ace.component.MenuItem",
        tlddoc = "MenuItem is used by various menu components" +
                 "<p>For more information, see the " +
                 "<a href=\"http://wiki.icefaces.org/display/ICE/MenuItem\">MenuItem Wiki Documentation</a>."
)
@ClientBehaviorHolder(events = {
	@ClientEvent(name="activate", javadoc="", tlddoc="Triggers when the menu item is clicked or selected by any other means.", defaultRender="@all", defaultExecute="@all")
}, defaultEvent="activate")
public class MenuItemMeta extends UICommandMeta {
    @Property(tlddoc = "Url to be navigated when menuitem is clicked.")
    private String url;
	
    @Property(tlddoc = "Specifies the encoding type for value of the 'url' attribute. Possible values are \"resource\", \"action\", \"partialaction\", \"bookmarkable\", \"redirect\", and \"none\". For all encodings other than \"resource\" and \"none\" the URL must be relative to the context path and must start with '/'. For external links and to render the value of the 'url' attribute unmodified use \"none\".", defaultValue="resource")
    private String urlEncoding;
	
    @Property(tlddoc = "Specifies an optional map of parameters to use when encoding URLs of the \"bookmarkable\" and \"redirect\" types. The value of this attribute must evaluate to an object of type Map<String,List<String>>.")
    private java.util.Map urlParameters;

    @Property(tlddoc = "Target type of url navigation.")
    private String target;

    @Property(tlddoc = "Text to display additional information.")
    private String helpText;

    @Property(tlddoc = "Style of the menuitem label.")
    private String style;

    @Property(tlddoc = "StyleClass of the menuitem label.")
    private String styleClass;

    @Property(tlddoc = "Javascript event handler for click event. If this function explicitly returns 'false', then the request to the server will be cancelled.")
    private String onclick;

    @Property(tlddoc = "Path of the menuitem image.")
    private String icon;

    @Property(tlddoc = "Boolean value to disable/enable the menu item. The menu item will still be shown but with different styling, and clicking on it will not trigger any action.")
    private boolean disabled;	
	
}
