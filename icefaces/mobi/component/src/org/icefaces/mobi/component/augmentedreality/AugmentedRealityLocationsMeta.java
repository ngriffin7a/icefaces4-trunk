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
        tagName = "augmentedRealityLocations",
        componentClass = "org.icefaces.mobi.component.augmentedreality.AugmentedRealityLocations",
        rendererClass = "org.icefaces.mobi.component.augmentedreality.AugmentedRealityLocationsRenderer",
        generatedClass = "org.icefaces.mobi.component.augmentedreality.AugmentedRealityLocationsBase",
        componentType = "org.icefaces.AugmentedRealityLocations",
        rendererType = "org.icefaces.AugmentedRealityLocationsRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.AugmentedRealityLocations",
        tlddoc = "Renders a list of locations to be displayed according to their " +
            "latitude, longitude, icon and text label on an Augmented Reality " +
            "video overlay view."
)

public class AugmentedRealityLocationsMeta extends UIComponentBaseMeta  {

    @Property(tlddoc="Name of request scope attribute for list iterator variable.",
            required = Required.yes)
    private String var;

    @Property(tlddoc="List containing location items.", required = Required.yes)
    private Object value;

    @Property(tlddoc="String containing location label.", required = Required.yes)
    private String locationLabel;

    @Property(tlddoc="String containing location latitude.", required = Required.no)
    private String locationLat;

    @Property(tlddoc="String containing location longitude.", required = Required.no)
    private String locationLon;

    @Property(tlddoc="String containing location altitude.", required = Required.no)
    private String locationAlt;

    @Property(tlddoc="String containing location direction 0-359 from North.", required = Required.no)
    private String locationDir;

    @Property(tlddoc="String containing location Icon URL.", required = Required.no)
    private String locationIcon;

}
