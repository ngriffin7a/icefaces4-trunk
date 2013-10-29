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

package org.icefaces.ace.component.listcontrol;

import javax.faces.context.FacesContext;

public class ListControl extends ListControlBase {
    @Override
    public boolean getRendersChildren() {
        return true;
    }

    public String getSelector(String clientId, boolean dualListMode) {
        if (dualListMode) {
            return "#"+clientId + " > div.if-list-dl > span." + ListControlRenderer.firstStyleClass + " > div > div.if-list, " +
                   "#"+clientId + " > div.if-list-dl > span." + ListControlRenderer.secondStyleClass + " > div > div.if-list";
        }
        return super.getSelector();
    }
}
