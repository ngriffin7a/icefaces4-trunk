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

package org.icefaces.mobi.component.augmentedreality;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Required;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import org.icefaces.ace.meta.baseMeta.UISeriesBaseMeta;
import javax.faces.model.SelectItem;
import javax.el.MethodExpression;
import java.util.List;

@Component(
        tagName = "augmentedRealityMarkers",
        componentClass = "org.icefaces.mobi.component.augmentedreality.AugmentedRealityMarkers",
        rendererClass = "org.icefaces.mobi.component.augmentedreality.AugmentedRealityMarkersRenderer",
        generatedClass = "org.icefaces.mobi.component.augmentedreality.AugmentedRealityMarkersBase",
        componentType = "org.icefaces.AugmentedRealityMarkers",
        rendererType = "org.icefaces.AugmentedRealityMarkersRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.AugmentedRealityMarkers",
        tlddoc = "Renders a list of three dimensional models attached to markers " +
                "displayed so that the marker ID corresponds to the list index " +
                "of the model."
)

public class AugmentedRealityMarkersMeta extends UIComponentBaseMeta  {

    @Property(tlddoc="Name of request scope attribute for list iterator variable.",
            required = Required.yes)
    private String var;

    @Property(tlddoc="List containing marker items.", required = Required.yes)
    private Object value;

    @Property(tlddoc="String containing Marker label.", required = Required.yes)
    private String markerLabel;

    @Property(tlddoc="URL pointing to marker model object.", required = Required.yes)
    private String markerModel;

}
