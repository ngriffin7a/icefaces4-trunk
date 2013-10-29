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

package org.icefaces.samples.showcase.util;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@ApplicationScoped
public class PositionBean {
    public static final String POS_INFIELD = "inField";
    
    public static final SelectItem[] LABEL_POSITIONS = new SelectItem[] {
        new SelectItem("left", "Left"),
        new SelectItem("right", "Right"),
        new SelectItem("top", "Top"),
        new SelectItem("bottom", "Bottom"),
        new SelectItem(POS_INFIELD, "In Field"),
        new SelectItem("none", "None")
    };
    public static final SelectItem[] INDICATOR_POSITIONS = new SelectItem[] {
        new SelectItem("left", "Left"),
        new SelectItem("right", "Right"),
        new SelectItem("top", "Top"),
        new SelectItem("bottom", "Bottom"),
        new SelectItem("labelLeft", "Left of Label"),
        new SelectItem("labelRight", "Right of Label"),
        new SelectItem("none", "None")
    };
    
    public SelectItem[] getLabelPositions() { return LABEL_POSITIONS; }
    
    public SelectItem[] getIndicatorPositions() { return INDICATOR_POSITIONS; }
}
