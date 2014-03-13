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

package org.icefaces.mobi.component.dataview;

import org.icefaces.ace.meta.annotation.Component;

@Component(
        tagName = "dataViewDetails",
        componentClass = "org.icefaces.mobi.component.dataview.DataViewDetails",
        generatedClass = "org.icefaces.mobi.component.dataview.DataViewDetailsBase",
        componentType = "org.icefaces.DataViewDetails",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.DataViewDetails",
        tlddoc = "The child components of this component define the region to be displayed when a row object of DataView " +
                "table region is activated. When DataView 'activationMode' is set to 'server' the child components of this " +
                "region may be arbitrary JSF components. When 'activationMode' is set to 'client' the child components of this " +
                "region are prerendered and have their dynamic attributes cached on the client to allow instanteous activation. " +
                "Client activation mode is restricted to particular components and only particular attributes of these components " +
                "may be dynamic, the specifics are documented on our wiki."
)
public class DataViewDetailsMeta {
}
