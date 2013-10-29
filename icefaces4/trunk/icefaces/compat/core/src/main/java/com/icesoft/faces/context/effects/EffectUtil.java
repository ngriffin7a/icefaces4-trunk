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

package com.icesoft.faces.context.effects;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *Utility functions for effect
 */
public class EffectUtil {

    /**
     * build up a map from a css string
     * key:value;
     * @param css
     * @return
     */
    public static Map buildCssMap(String css) {
        Map map = new HashMap();
        if (css == null || css.trim().length() == 0)
            return map;
        css = css.trim();
        StringTokenizer st = new StringTokenizer(css, ";");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            int i = token.indexOf(":");
            if (i == 0) throw new IllegalArgumentException(
                    "Can't find : in [" + token + "]");
            String name = token.substring(0, i);
            String value = token.substring(++i);
            map.put(name, value);
        }
        return map;
    }
}
