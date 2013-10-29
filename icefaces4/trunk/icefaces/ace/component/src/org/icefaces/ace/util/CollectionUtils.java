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

import org.icefaces.ace.util.collections.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class CollectionUtils {
    public static Collection select(Collection inputCollection, Predicate predicate) {
        ArrayList answer = new ArrayList(inputCollection.size());
        select(inputCollection, predicate, answer);
        return answer;
    }

    public static void select(Collection inputCollection, Predicate predicate, Collection outputCollection) {
        if (inputCollection != null && predicate != null) {
            for (Iterator iter = inputCollection.iterator(); iter.hasNext();) {
                Object item = iter.next();
                if (predicate.evaluate(item)) {
                    outputCollection.add(item);
                }
            }
        }
    }

    public static void filter(Collection collection, Predicate predicate) {
        if (collection != null && predicate != null) {
            for (Iterator it = collection.iterator(); it.hasNext();) {
                if (predicate.evaluate(it.next()) == false) {
                    it.remove();
                }
            }
        }
    }
}
