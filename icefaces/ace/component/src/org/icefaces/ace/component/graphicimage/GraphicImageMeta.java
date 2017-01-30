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

package org.icefaces.ace.component.graphicimage;


import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.component.PassthroughAttributes;

@Component(
        tagName = "graphicImage",
        componentClass = "org.icefaces.ace.component.graphicimage.GraphicImage",
        rendererClass = "org.icefaces.ace.component.graphicimage.GraphicImageRenderer",
        generatedClass = "org.icefaces.ace.component.graphicimage.GraphicImageBase",
        componentType = "org.icefaces.GraphicImage",
        rendererType = "org.icefaces.GraphicImageRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.GraphicImage",
        tlddoc = "Render markup for &lt;img&gt; with support for " +
                " byte[] for images from database."
)
@PassthroughAttributes({
        "alt",
        "dir",
        "height",
        "width",
        "lang",
        "longdesc",
        "title",
        "onclick",
        "onkeypress",
        "readonly",
        "disabled",
        "ismap",
        "usemap",
        "style"
})
public class GraphicImageMeta {

    @Property(tlddoc = "Override for the \"src\" attribute of this image.")
    private String src;

    @Property(tlddoc = "The value of the image, which can be of type String (to specify a URL), byte[], or IceOutputResource.")
    private Object value;

    @Property(tlddoc = "Equivalent to the \"src\" attribute.")
    private String url;

    @Property(tlddoc = "Override for the MIME type of the image.")
    private String mimeType;

    @Property(defaultValue = "session", tlddoc = "Scope of Resource or byte[] when image is specified with a dynamic data value.")
    private String scope;

    @Property(tlddoc = "Name of resource object stored when image is specified with a dynamic data value.")
    private String name;

    @Property(tlddoc = "Space-separated list of CSS style class(es) to be applied when this element is rendered.")
    private String styleClass;
}


