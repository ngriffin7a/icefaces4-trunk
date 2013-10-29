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

package com.icesoft.faces.el;

import javax.faces.el.MethodBinding;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodNotFoundException;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * The EL that Facelets uses allows for a MethodBinding with
 *  "true" or "false" that will resolve to Boolean.TRUE or
 *  Boolean.FALSE.  The EL with JSF1.1-JSP doesn't seem so
 *  forgiving.  So we need this helper class to act as a
 *  MethodBinding, but just return a constant Boolean value.
 * 
 * @author Mark Collette
 * @since 1.6
 */
public class LiteralBooleanMethodBinding
    extends MethodBinding
    implements Serializable
{
    private String svalue;
    private Boolean value;
    
    public LiteralBooleanMethodBinding(String svalue) {
        this.svalue = svalue;
        this.value = resolve(svalue);
    }
    
    public Object invoke(FacesContext facesContext, Object[] objects)
        throws EvaluationException, MethodNotFoundException
    {
        return value;
    }
    
    public Class getType(FacesContext facesContext)
        throws MethodNotFoundException
    {
        return Boolean.class;
    }
    
    public String getExpressionString() {
        return svalue;
    }
    
    private static Boolean resolve(String value) {
        Boolean ret = Boolean.FALSE;
        if( value != null ) {
            try {
                ret = Boolean.valueOf(value);
            }
            catch(Exception e) {} // Leave it as Boolean.FALSE
        }
        return ret;
    }
}
