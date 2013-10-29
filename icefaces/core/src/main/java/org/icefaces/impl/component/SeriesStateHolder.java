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

package org.icefaces.impl.component;

import javax.faces.context.FacesContext;

/**
 * StateHolder is used to hold the complete state of a UIComponent, and so
 *  is used sparingly. UIData, which is used for iterative container
 *  components, only saves the EditableValueHolder fields. So, there's a
 *  huge gap inbetween, of being able to store fields when in an iterative
 *  container. This interface is used by our UISeries to manage non-UIData
 *  state that any component might require, without having to save everything.
 * 
 * @author Mark Collette
 */
public interface SeriesStateHolder {
    public Object saveSeriesState(FacesContext facesContext);
    
    public void restoreSeriesState(FacesContext facesContext, Object state);
}
