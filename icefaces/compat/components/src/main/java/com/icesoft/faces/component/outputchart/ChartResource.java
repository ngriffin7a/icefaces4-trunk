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

package com.icesoft.faces.component.outputchart;

import com.icesoft.faces.context.ByteArrayResource;

import java.io.Serializable;

public class ChartResource extends ByteArrayResource implements Serializable {
    private static long prevDigest = 0;
    
    public ChartResource(byte[] content) {
        super(content);
    }

    public String calculateDigest() {
        long digest = System.currentTimeMillis();
        synchronized (getClass()) { // ICE-3052
            if (digest <= prevDigest) {
                digest = prevDigest + 1;
            }
            prevDigest = digest;
        }
        return String.valueOf("CHART"+digest);
    }
    
    public boolean equals(Object obj) {
        if (!(obj instanceof ChartResource)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        return true;
    }
}
