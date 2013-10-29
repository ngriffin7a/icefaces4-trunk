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

package com.icesoft.faces.context;

import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextFactory;

public class CompatDOMPartialViewContextFactory extends PartialViewContextFactory {
    PartialViewContextFactory delegate;

    public CompatDOMPartialViewContextFactory(PartialViewContextFactory delegate) {
        this.delegate = delegate;
    }

    public PartialViewContextFactory getWrapped() {
        return delegate;
    }

    /**
     * <p>Create (if needed)
     * and return a {@link javax.faces.context.PartialViewContext} instance that is initialized
     * using the current {@link javax.faces.context.FacesContext} instance.</p>
     *
     * @param facesContext the {@link javax.faces.context.FacesContext} for the current request.
     */
    public PartialViewContext getPartialViewContext(FacesContext facesContext) {
        return new CompatDOMPartialViewContext(delegate.getPartialViewContext(facesContext), facesContext);
    }

}
