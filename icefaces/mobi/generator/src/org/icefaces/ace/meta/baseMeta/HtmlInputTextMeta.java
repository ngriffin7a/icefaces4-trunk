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

package org.icefaces.ace.meta.baseMeta;

import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.DefaultValueType;
import org.icefaces.ace.meta.annotation.Implementation;

/**
 * These are the properties for javax.faces.component.html.HtmlInputText
 */
public class HtmlInputTextMeta extends UIInputMeta {

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Access key that, when pressed, transfers focus to this element.")
    private String accesskey;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Alternate textual description of the element rendered by this component.")
    private String alt;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "If the value of this attribute is \"off\", render \"off\" as the value of the attribute. This indicates that the browser should disable its autocomplete feature for this component. This is useful for components that perform autocompletion and do not want the browser interfering. If this attribute is not set or the value is \"on\", render nothing.")
    private String autocomplete;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Direction indication for text that does not inherit directionality. Valid values are \"LTR\" (left-to-right) and \"RTL\" (right-to-left).")
    private String dir;

    @Property(defaultValue="false",
        defaultValueType= DefaultValueType.EXPRESSION,
        implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Flag indicating that this element must never receive focus or be included in a subsequent submit. A value of false causes no attribute to be rendered, while a value of true causes the attribute to be rendered as disabled=\"disabled\".")
    private boolean disabled;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "A localized user presentable name for this component.")
    private String label;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Code describing the language used in the generated markup for this component.")
    private String lang;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "The maximum number of characters that may be entered in this field.")
    private int maxlength;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when this element loses focus.")
    private String onblur;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when this element loses focus and its value has been modified since gaining focus.")
    private String onchange;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when a pointer button is clicked over this element.")
    private String onclick;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when a pointer button is double clicked over this element.")
    private String ondblclick;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when this element receives focus.")
    private String onfocus;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when a key is pressed down over this element.")
    private String onkeydown;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when a key is pressed and released over this element.")
    private String onkeypress;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when a key is released over this element.")
    private String onkeyup;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when a pointer button is pressed down over this element.")
    private String onmousedown;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when a pointer button is moved within this element.")
    private String onmousemove;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when a pointer button is moved away from this element.")
    private String onmouseout;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when a pointer button is moved onto this element.")
    private String onmouseover;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when a pointer button is released over this element.")
    private String onmouseup;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when text within this element is selected by the user.")
    private String onselect;

    @Property(defaultValue="false",
        defaultValueType= DefaultValueType.EXPRESSION,
        implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Flag indicating that this component will prohibit changes by the user. The element may receive focus unless it has also been disabled. A value of false causes no attribute to be rendered, while a value of true causes the attribute to be rendered as readonly=\"readonly\".")
    private boolean readonly;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "The number of characters used to determine the width of this field.")
    private int size;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "CSS style(s) to be applied when this component is rendered.")
    private String style;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Space-separated list of CSS style class(es) to be applied when this element is rendered. This value must be passed through as the \"class\" attribute on generated markup.")
    private String styleClass;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Position of this element in the tabbing order for the current document. This value must be an integer between 0 and 32767.")
    private String tabindex;

    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Advisory title information about markup elements generated for this component.")
    private String title;
}
