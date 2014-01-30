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

package org.icefaces.mobi.component.scan;


import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "scan",
        componentClass = "org.icefaces.mobi.component.scan.Scan",
        rendererClass = "org.icefaces.mobi.component.scan.ScanRenderer",
        generatedClass = "org.icefaces.mobi.component.scan.ScanBase",
        componentType = "org.icefaces.Scan",
        rendererType = "org.icefaces.ScanRenderer",
        extendsClass = "javax.faces.component.UIInput",
        componentFamily = "org.icefaces.Scan",
        tlddoc = "The Scan component captures a QR code value."
)

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class ScanMeta extends UIComponentBaseMeta {

    @Property(defaultValue = "false",
            tlddoc = org.icefaces.mobi.util.TLDConstants.DISABLED)
    private boolean disabled;

    @Property(tlddoc = org.icefaces.mobi.util.TLDConstants.TABINDEX)
    private int tabindex;

     @Property(tlddoc = org.icefaces.mobi.util.TLDConstants.STYLE)
     private String style;

     @Property(tlddoc = org.icefaces.mobi.util.TLDConstants.STYLECLASS)
     private String styleClass;

    @Property(defaultValue="Scan Captured", tlddoc="The message displays upon successful capture from device.")
    private String captureMessageLabel;

    @Property(defaultValue="Scan Code", tlddoc="The button label for this component.")
    private String buttonLabel;
}
