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

package org.icefaces.impl.component;

import java.io.Serializable;

/**
 * @author mcollette
 * @since 1.8
 */
public class VarStatus implements Serializable {
    private int begin;
    private int end;
    private int index;

    public VarStatus(int begin, int end, int index) {
        this.begin = begin;
        this.end = end;
        this.index = index;
    }

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public int getIndex() {
        return index;
    }

    public boolean isFirst() {
        return (begin == index);
    }

    public boolean isLast() {
        return (end == index);
    }
}
