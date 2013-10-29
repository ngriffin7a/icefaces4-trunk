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
package org.icefaces.ace.component.graphicimage;


import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;


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

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class GraphicImageMeta {
    @Property(tlddoc = "Alternate textual discription as \"alt\" attribute for this image.")
    private String alt;

    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc = "Integer specifying  the width of this image.")
    private int width;

    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc = "Integer specifying the height of this image.")
    private int height;

    @Property(tlddoc = "Override for the \"src\" attribute of this image.")
    private String src;

    @Property(tlddoc = "Value of the image, which can be byte[], String filename, or Resource.")
    private Object value;

    @Property(tlddoc = "Equivalent to the \"src\" attribute.")
    private String url;

    @Property(tlddoc = "Override for the MIME type of the image.")
    private String mimeType;

    @Property(defaultValue = "session", tlddoc = "Scope of Resource or byte[] when image is specified with a dynamic data value.")
    private String scope;

    @Property(tlddoc = "Name of resource object stored when image is specified with a dynamic data value.")
    private String name;

    @Property(tlddoc = "Flag indicating that the image serves as a client-side image map via the HTML \"ismap\" attribute.")
    private String ismap;

    @Property(tlddoc = "URI to a long description of the image.")
    private String longdesc;

    @Property(tlddoc = "Value for the HTML \"usemap\" attribute, providing the hash fragment reference to a client-side image map <map> tag in the page.")
    private String usemap;

    @Property(tlddoc = "Space-separated list of CSS style class(es) to be applied when this element is rendered.")
    private String styleClass;

    @Property(tlddoc = "CSS style(s) to be applied when this component is rendered.")
    private String style;

    @Property(tlddoc = "HTML text direction for text that does not inherit directionality. Valid values are LTR " +
            "or RTL  left to right or right to left.")
    private String dir;

    @Property(tlddoc = "HTML title attribute providing extra information about the element.")
    private String title;

}


