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

package org.icefaces.ace.util;

/**
 * Utility API that builds the parameter strings, performs param escaping.
 * Output is a JSON string as specified at <a href="http://www.json.org/">json.org</a>.
 * Based on <a href="http://jira.icefaces.org/browse/ICE-5831">spec.</a> from Mark Collette
 * and <a href="http://yui.yahooapis.com/2.8.1/build/json/json.js"">code from YUI</a>.
 * List of escaped characters can be found at <a href="http://www.json.org/">json.org</a>.
 */
public class JSONBuilder {
    private StringBuilder params = new StringBuilder();

    /**
     * Makes a new instance of JSONBuilder.
     * @return a reference to this object.
     */
    public static JSONBuilder create() {
        return new JSONBuilder();
    }
    
    public JSONBuilder initialiseVar(String varName) {
        params.append("var ");
        params.append(varName);
        params.append(" = ");
        return this;
    }

    public JSONBuilder initialiseWindowVar(String varName) {
        params.append(varName);
        params.append(" = ");
        return this;
    }

    /**
     * Begins an anonymous object.
     * @return a reference to this object.
     */
    public JSONBuilder beginMap() {
        conditionallyAppendComma();
        params.append("{");
        return this;
    }

    /**
     * Begins a named object.
     * @param key name of the object.
     * @return a reference to this object.
     */
    public JSONBuilder beginMap(String key) {
        appendCommaAndKey(key);
        params.append("{");
        return this;
    }

    /**
     * Ends an object.
     * @return a reference to this object.
     */
    public JSONBuilder endMap() {
        params.append("}");
        return this;
    }

    /**
     * Begins an anonymous array.
     * @return a reference to this object.
     */
    public JSONBuilder beginArray() {
        conditionallyAppendComma();
        params.append("[");
        return this;
    }

    public JSONBuilder beginArray(String key) {
        appendCommaAndKey(key);
        params.append("[");
        return this;
    }

    public JSONBuilder endArray() {
        params.append("]");
        return this;
    }

    public JSONBuilder beginFunction(String name) {
        params.append(name);
        params.append('(');
        return this;
    }

    public JSONBuilder endFunction() {
        params.append(");");
        return this;
    }

    /**
     * Adds an int property to a map.
     * @param key name of the property.
     * @param value value of the property.
     * @return a reference to this object.
     */
    public JSONBuilder entry(String key, int value) {
        appendCommaAndKey(key);
        params.append(value);
        return this;
    }

    /**
     * Adds a long property to a map.
     * @param key name of the property.
     * @param value value of the property.
     * @return a reference to this object.
     */
    public JSONBuilder entry(String key, long value) {
        appendCommaAndKey(key);
        params.append(value);
        return this;
    }

    /**
     * Adds a float property to a map.
     * @param key name of the property.
     * @param value value of the property.
     * @return a reference to this object.
     */
    public JSONBuilder entry(String key, float value) {
        appendCommaAndKey(key);
        params.append(value);
        return this;
    }

    /**
     * Adds a double property to a map.
     * @param key name of the property.
     * @param value value of the property.
     * @return a reference to this object.
     */
    public JSONBuilder entry(String key, double value) {
        appendCommaAndKey(key);
        params.append(value);
        return this;
    }

    /**
     * Adds a boolean property to a map.
     * @param key name of the property.
     * @param value value of the property.
     * @return a reference to this object.
     */
    public JSONBuilder entry(String key, boolean value) {
        appendCommaAndKey(key);
        params.append(value);
        return this;
    }

    /**
     * Append a key bound String array that is itself a set of key-value pairs to a map.
     * Even array indexes = key, odd array index  = values
     * @param key overall key to put entry under
     * @param keyValuePairs Array of key value pair string entries 
     * @return The builder object 
     */
    public JSONBuilder entry (String key, String[] keyValuePairs) {
        beginArray(key);
        int len = keyValuePairs.length;
        for (int idx = 0; idx < len; idx ++) {
            String curr = keyValuePairs[idx];
            if (curr == null) {
                params.append("null");
            }
            else {
                params.append('"').append(escapeString(curr)).append('"');
            }
            if (idx < (len-1)) {
                params.append(',');
            }
        }
        endArray();
        return this; 
    }

    /**
     * Adds an Integer property to a map only if the value is non-null,
     * otherwise the key/value pair will not be added at all.
     * @param key name of the property.
     * @param value value of the property.
     * @return a reference to this object.
     */
    public JSONBuilder entryNonNullValue(String key, Integer value) {
        if (value == null) {
            return this;
        }
        return entry(key, value.intValue());
    }

    /**
     * Adds a Long property to a map only if the value is non-null,
     * otherwise the key/value pair will not be added at all.
     * @param key name of the property.
     * @param value value of the property.
     * @return a reference to this object.
     */
    public JSONBuilder entryNonNullValue(String key, Long value) {
        if (value == null) {
            return this;
        }
        return entry(key, value.longValue());
    }

    /**
     * Adds a Float property to a map only if the value is non-null,
     * otherwise the key/value pair will not be added at all.
     * @param key name of the property.
     * @param value value of the property.
     * @return a reference to this object.
     */
    public JSONBuilder entryNonNullValue(String key, Float value) {
        if (value == null) {
            return this;
        }
        return entry(key, value.floatValue());
    }

    /**
     * Adds a Double property to a map only if the value is non-null,
     * otherwise the key/value pair will not be added at all.
     * @param key name of the property.
     * @param value value of the property.
     * @return a reference to this object.
     */
    public JSONBuilder entryNonNullValue(String key, Double value) {
        if (value == null) {
            return this;
        }
        return entry(key, value.doubleValue());
    }

    /**
     * Adds a Boolean property to a map only if the value is non-null,
     * otherwise the key/value pair will not be added at all.
     * @param key name of the property.
     * @param value value of the property.
     * @return a reference to this object.
     */
    public JSONBuilder entryNonNullValue(String key, Boolean value) {
        if (value == null) {
            return this;
        }
        return entry(key, value.booleanValue());
    }

    /**
     * Adds a String property to a map only if the value is non-null,
     * otherwise the key/value pair will not be added at all.
     * Adds quotes and does JSON string escaping, as described at
     * <a href="http://www.json.org/">json.org</a>.
     * @param key name of the property.
     * @param value value of the property.
     * @return a reference to this object.
     */
    public JSONBuilder entryNonNullValue(String key, String value) {
        if (value == null) {
            return this;
        }
        return entry(key, value, false);
    }

    /**
     * Adds a String property to a map.
     * Adds quotes and does JSON string escaping, as described at
     * <a href="http://www.json.org/">json.org</a>.
     * @param key name of the property.
     * @param value value of the property.
     * @return a reference to this object.
     */
    public JSONBuilder entry(String key, String value) {
        return entry(key, value, false);
    }

    /**
     * Adds a String property as String literal optionally to a map.
     * Adds quotes and does JSON string escaping, as described at <a href="http://www.json.org/">json.org</a>. 
     * @param key name of the property.
     * @param value value of the property.
     * @return a reference to this object.
     */
    public JSONBuilder entry(String key, String value, boolean isStringLiteral) {
        appendCommaAndKey(key);
        if (isStringLiteral) {
        	params.append(value);   
        } else {
            value = escapeString(value);
	    	params.append('"').append(value).append('"');     
        }
        return this;
    }


    /**
     * Adds an int to an array or function call.
     * @param value value of the item.
     * @return a reference to this object.
     */
    public JSONBuilder item(int value) {
        conditionallyAppendComma();
        params.append(value);
        return this;
    }

    /**
     * Adds a long to an array or function call.
     * @param value value of the item.
     * @return a reference to this object.
     */
    public JSONBuilder item(long value) {
        conditionallyAppendComma();
        params.append(value);
        return this;
    }

    /**
     * Adds a float to an array or function call.
     * @param value value of the item.
     * @return a reference to this object.
     */
    public JSONBuilder item(float value) {
        conditionallyAppendComma();
        params.append(value);
        return this;
    }

    /**
     * Adds a double to an array or function call.
     * @param value value of the item.
     * @return a reference to this object.
     */
    public JSONBuilder item(double value) {
        conditionallyAppendComma();
        params.append(value);
        return this;
    }

    /**
     * Adds a boolean to an array or function call.
     * @param value value of the item.
     * @return a reference to this object.
     */
    public JSONBuilder item(boolean value) {
        conditionallyAppendComma();
        params.append(value);
        return this;
    }

    /**
     * Adds an escaped String to an array or function call.
     * @param value value of the item.
     * @return a reference to this object.
     */
    public JSONBuilder item(String value) {
        return item(value, true);
    }

    /**
     * Adds a String to an array or function call.
     * @param value value of the item.
     * @param escaped whether to escape the value and surround it with double quotes.
     * @return a reference to this object.
     */
    public JSONBuilder item(String value, boolean escaped) {
        conditionallyAppendComma();
        if (escaped) params.append('"').append(escapeString(value)).append('"');
        else params.append(value);
        return this;
    }



    public static String escapeString(String value) {
        StringBuilder sb = new StringBuilder();
        char c;
        for (int idx = 0; idx < value.length(); idx++) {
            c = value.charAt(idx);
            switch (c) {
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\"':
                    sb.append("\\\"");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Outputs the JSON string.
     * @return the JSON string.
     */
    public String toString() {
        return params.toString();
    }

    private void appendCommaAndKey(String key) {
        conditionallyAppendComma();
        params.append('"').append(key).append('"').append(":");
    }

    private void conditionallyAppendComma() {
        int len = params.length();
        if (len > 0) {
            char lastChar = params.charAt(len - 1);
            if (lastChar != '{' && lastChar != '[' && lastChar != '(') {
                params.append(",");
            }
        }
    }
}
