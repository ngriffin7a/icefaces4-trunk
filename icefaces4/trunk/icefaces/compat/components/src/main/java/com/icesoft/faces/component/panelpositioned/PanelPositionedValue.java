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

package com.icesoft.faces.component.panelpositioned;

import java.util.ArrayList;
import java.util.List;

/**
 * Store a value
 */
public class PanelPositionedValue implements java.io.Serializable {
    private List sourceList;
    private int valueIndex;


    public PanelPositionedValue(List sourceList, int valueIndex) {
        // Need to make a copy to keep orderingin line.
        this.sourceList = new ArrayList(sourceList);
        this.valueIndex = valueIndex;


    }

    public List getSourceList() {
        return sourceList;
    }

    public int getValueIndex() {
        return valueIndex;
    }
}
