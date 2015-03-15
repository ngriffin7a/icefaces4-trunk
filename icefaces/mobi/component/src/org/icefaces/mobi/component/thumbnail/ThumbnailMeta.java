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

package org.icefaces.mobi.component.thumbnail;


import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Field;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Required;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;


@Component(
        tagName = "thumbnail",
        componentClass = "org.icefaces.mobi.component.thumbnail.Thumbnail",
        rendererClass = "org.icefaces.mobi.component.thumbnail.ThumbnailRenderer",
        generatedClass = "org.icefaces.mobi.component.thumbnail.ThumbnailBase",
        componentType = "org.icefaces.Thumbnail",
        rendererType = "org.icefaces.ThumbnailRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.Thumbnail",
        tlddoc = "thumbnail provides a thumbnail image to a camera or camcorder component.")

@ResourceDependencies({
		@ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class ThumbnailMeta extends UIComponentBaseMeta {

    @Property(tlddoc = org.icefaces.mobi.util.TLDConstants.TABINDEX)
    private int tabindex;

    @Property(tlddoc = org.icefaces.mobi.util.TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = org.icefaces.mobi.util.TLDConstants.STYLECLASS)
    private String styleClass;

    @Property( name = "for",
               required = Required.yes,
               tlddoc = "The id of camera or camcorder component the thumbnail is associated with.")
    private String For;

}
