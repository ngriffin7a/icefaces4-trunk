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

package org.icefaces.facelets.tag.icefaces.core;

import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.view.facelets.*;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodRule extends MetaRule {
    private final String methodName;
    private final Class returnTypeClass;
    private final Class[] params;

    public MethodRule(String methodName, Class returnTypeClass, Class[] params) {
        this.methodName = methodName;
        this.returnTypeClass = returnTypeClass;
        this.params = params;
    }

    public Metadata applyRule(String name, TagAttribute attribute,
            MetadataTarget meta) {
        if (!name.equals(this.methodName))  {
            return null;
        }

        if (MethodBinding.class.equals(meta.getPropertyType(name))) {
            Method method = meta.getWriteMethod(name);
            if (null != method) {
                return new MethodBindingMetadata(method, attribute,
                        this.returnTypeClass, this.params);
            }
        } else if (MethodExpression.class.equals(meta.getPropertyType(name))) {
            Method method = meta.getWriteMethod(name);
            if (null != method) {
                return new MethodExpressionMetadata(method, attribute,
                        this.returnTypeClass, this.params);
            }
        }

        return null;
    }


    private static class MethodBindingMetadata extends Metadata  {
        private final Method method;
        private final TagAttribute attribute;
        private Class[] params;
        private Class returnTypeClass;

        public MethodBindingMetadata(Method method, TagAttribute attribute,
                Class returnTypeClass, Class[] params)  {
            this.method = method;
            this.attribute = attribute;
            this.returnTypeClass = returnTypeClass;
            this.params = params;
        }

        public void applyMetadata(FaceletContext faceletContext, Object instance) {
            MethodExpression expr = attribute.getMethodExpression(faceletContext,
                    returnTypeClass, params);
            try {
                method.invoke(instance, new MethodExpressionMethodBinding(expr) );
            } catch (InvocationTargetException e)  {
                throw new TagAttributeException(attribute, e.getCause());
            } catch (Exception e)  {
                throw new TagAttributeException(attribute, e);
            }
        }

    }

    private static class MethodExpressionMetadata extends Metadata  {
        private final Method method;
        private final TagAttribute attribute;
        private Class[] params;
        private Class returnTypeClass;

        public MethodExpressionMetadata(Method method, TagAttribute attribute,
                Class returnTypeClass, Class[] params)  {
            this.method = method;
            this.attribute = attribute;
            this.returnTypeClass = returnTypeClass;
            this.params = params;
        }

        public void applyMetadata(FaceletContext faceletContext, Object instance) {
            MethodExpression expr = attribute.getMethodExpression(faceletContext,
                    returnTypeClass, params);
            try {
                method.invoke(instance, expr );
            } catch (InvocationTargetException e)  {
                throw new TagAttributeException(attribute, e.getCause());
            } catch (Exception e)  {
                throw new TagAttributeException(attribute, e);
            }
        }

    }

    private static class MethodExpressionMethodBinding extends MethodBinding
            implements Serializable {
        private final MethodExpression expression;

        public MethodExpressionMethodBinding(MethodExpression expression) {
            this.expression = expression;
        }

        public Class getType(FacesContext facesContext)  {
            return expression.getMethodInfo(facesContext.getELContext()).getReturnType();
        }

        public Object invoke(FacesContext facesContext, Object[] params)  {
            return expression.invoke(facesContext.getELContext(), params);
        }

        public String getExpressionString() {
            return expression.getExpressionString();
        }
    }
}
