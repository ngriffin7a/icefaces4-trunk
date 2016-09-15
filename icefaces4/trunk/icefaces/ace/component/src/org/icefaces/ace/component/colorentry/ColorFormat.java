/*
 * Copyright 2004-2016 ICEsoft Technologies Canada Corp.
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

package org.icefaces.ace.component.colorentry;

public enum ColorFormat {

    /**
     * HEX is hexadecimal display in input field
     * HEX3 is hexadecimal display using 3 characters only if possible
     * HSL is (hue, saturation, lightness) designation of color
     * RGB is  (red, green, blue) of color
     * NAME specifies the color by name when selecting color and will fall back to hex
     * NONE will depend on the input
     */
    HEX("hex"), HEX3("hex3"), HSL("hsl"), RGB("rgb"), NAME("name"), NONE("none");
    private String value;
    private ColorFormat(String value){
        this.value=value;
    }
    public String getValue() {
        return this.value;
    }
}
