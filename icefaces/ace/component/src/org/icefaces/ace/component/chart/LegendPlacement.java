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

package org.icefaces.ace.component.chart;

import java.util.Arrays;
import java.util.List;

public enum LegendPlacement {
    INSIDE_GRID, OUTSIDE_GRID, OUTSIDE;

    @Override
    public String toString() {
        List<String> parts = Arrays.asList(super.toString().split("_"));
        String name = parts.get(0).toLowerCase();

        for (String part : parts.subList(1, parts.size()))
            name += toProperCase(part);

        return name;
    }

    private String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }
}
