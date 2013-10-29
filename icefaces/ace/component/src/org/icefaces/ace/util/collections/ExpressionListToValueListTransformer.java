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

package org.icefaces.ace.util.collections;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ExpressionListToValueListTransformer {
    FacesContext context;
    ELContext elContext;
    ELResolver resolver;
    Object expressionBase;
    
    public ExpressionListToValueListTransformer(FacesContext context, ELContext elContext, ELResolver resolver, Object expressionBase) {
        this.context = context;
        this.elContext = elContext;
        this.resolver = resolver;
        this.expressionBase = expressionBase;
    }

    public List transform(Collection o) {
        List<Object> valueSet = new ArrayList<Object>();
        for (String s : (Collection<String>) o) {
            int firstPeriodPos = s.indexOf('.');
            if (firstPeriodPos >= 0) 
                s = s.substring(firstPeriodPos+1, s.lastIndexOf('}'));
            else
                s = s.substring(s.indexOf('{')+1, s.lastIndexOf('}'));

            valueSet.add(resolver.getValue(elContext, expressionBase, s));
        }
        return valueSet;
    }
}