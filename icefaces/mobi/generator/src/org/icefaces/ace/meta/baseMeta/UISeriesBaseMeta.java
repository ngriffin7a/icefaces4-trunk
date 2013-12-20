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

package org.icefaces.ace.meta.baseMeta;

import org.icefaces.ace.meta.annotation.*;

/**
 * These are the properties for org.icefaces.impl.component.UISeriesBase
 */
public class UISeriesBaseMeta extends UIDataMeta {
    @Only(OnlyType.JSF)
    @Property(implementation=Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="Name of a request-scope attribute under which the current " +
        "indexed state will be exposed. This is modeled after the Facelets " +
        "JSTL c:forEach varStatus, and has 5 sub-properties:</p>" +
        "<dl>" +
        "<dt>int <b>begin</b></dt> " +
        "<dd>Corresponds to the UIData container's <b>first</b> property, the " +
        "    index at which it begins iteration.</dd>" +
        "<dt>int <b>end</b></dt>" +
        "<dd>The ending index of iteration, corresponding to the UIData " +
        "    container's <b>first</b> plus <b>rows</b> minus 1. Note: " +
        "    If the UIData's DataModel's isRowAvailable() method returns false, " +
        "    prematurely ending iteration, then the actual ending index may not " +
        "    equal the value for this <b>end</b> property.</dd>" +
        "<dt>int <b>index</b></dt>" +
        "<dd>The current iteration index. Corresponding to the UIData " +
        "    container's getRowIndex() value, which also corresponds with the " +
        "    UIData's DataModel's getRowIndex() value.</dd>" +
        "<dt>boolean <b>first</b></dt>" +
        "<dd>Defined simply as: <b>varStatus.index == varStatus.begin</b></dd>" +
        "<dt>boolean <b>last</b></dt>" +
        "<dd>Defined simply as: <b>varStatus.index == varStatus.end</b>. Note: " +
        "    This has the same limitations as the <b>end</b> property.</dd>")
    private String varStatus;
}
