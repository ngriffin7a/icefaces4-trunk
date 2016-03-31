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

import static org.icesoft.util.ObjectUtilities.*;
import static org.icesoft.util.PreCondition.checkArgument;
import static org.icesoft.util.StringUtilities.isNotNullAndIsNotEmpty;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PushOptions {
    private static final Logger LOGGER = Logger.getLogger(PushOptions.class.getName());

    private final Map<String, Object> attributeMap = new HashMap<String, Object>();

    public PushOptions()  {
        // Do nothing.
    }

    public PushOptions(final Map<String, Object> attributeMap)
    throws IllegalArgumentException {
        putAllAttributes(attributeMap);
    }

    public PushOptions and(final PushOptions pushOptions) {
        putAllAttributes(pushOptions.getAttributeMap());
        return this;
    }

    public boolean containsAttributeKey(final String key) {
        return getAttributeMap().containsKey(key);
    }

    public Object getAttribute(final String key) {
        return getAttributeMap().get(key);
    }

    public Map<String, Object> getAttributeMap() {
        return Collections.unmodifiableMap(getModifiableAttributeMap());
    }

    public void putAllAttributes(final Map<String, Object> attributeMap)
    throws IllegalArgumentException {
        for (final Map.Entry<String, Object> _attributeEntry : attributeMap.entrySet()) {
            // throws IllegalArgumentException
            putAttribute(_attributeEntry.getKey(), _attributeEntry.getValue());
        }
    }

    public Object putAttribute(final String key, final Object value)
    throws IllegalArgumentException {
        checkArgument(
            isNotNullAndIsNotEmpty(key), "Illegal argument key: '" + key + "'.  Argument cannot be null or empty."
        );
        checkArgument(
            isNotNull(value), "Illegal argument value: '" + value + "'.  Argument cannot be null."
        );
        return getModifiableAttributeMap().put(key, value);
    }

    public Object removeAttribute(final String key) {
        return getModifiableAttributeMap().remove(key);
    }

    @Override
    public String toString() {
        return
            new StringBuilder().
                append("PushOptions[").
                    append(classMembersToString()).
                append("]").
                    toString();
    }

    protected String classMembersToString() {
        return
            new StringBuilder().
                append("attributeMap: '").append(getAttributeMap()).append("'").
                    toString();
    }

    protected Map<String, Object> getModifiableAttributeMap() {
        return attributeMap;
    }
}