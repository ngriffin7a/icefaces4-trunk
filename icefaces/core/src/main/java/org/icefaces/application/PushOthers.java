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

package org.icefaces.application;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PushOthers
extends PushOptions {
    private static final Logger LOGGER = Logger.getLogger(PushOthers.class.getName());

    public static final String PUSH_OTHERS = "pushOthers";

    public PushOthers() {
        putAttribute(PUSH_OTHERS, true);
    }

    public PushOthers(final boolean pushOthers) {
        putAttribute(PUSH_OTHERS, pushOthers);
    }

    public boolean getPushOthers() {
        return (Boolean)getAttribute(PUSH_OTHERS);
    }

    public void setPushOthers(final boolean pushOthers) {
        putAttribute(PUSH_OTHERS, pushOthers);
    }

    @Override
    public String toString() {
        return
            new StringBuilder().
                append("PushOthers[").
                    append(classMembersToString()).
                append("]").
                    toString();
    }
}
