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


package org.icefaces.ace.component.fileentry;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseListener;
import javax.faces.event.PreRenderComponentEvent;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.FactoryFinder;
import java.util.Iterator;

@ManagedBean(name="fileEntryLoader", eager=true)
@ApplicationScoped
public class FileEntryLoader {
    public FileEntryLoader() {
        Application application =
            FacesContext.getCurrentInstance().getApplication();
        application.subscribeToEvent(PreRenderComponentEvent.class, null,
            new FileEntryFormSubmit());

        PhaseListener phaseListener = new FileEntryPhaseListener();
        LifecycleFactory lifecycleFactory = (LifecycleFactory)
            FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        for (Iterator ids = lifecycleFactory.getLifecycleIds(); ids.hasNext();) {
            Lifecycle lifecycle = lifecycleFactory.getLifecycle(
                (String) ids.next());
            // Remove all other PhaseListeners, and re-add them after
            // FileEntryPhaseListener, since they'll likely rely on it having 
            // setup a valid environment for JSF. Eg: WindowScopeManager.
            // This also allows FileEntryPhaseListener to add FacesMessages
            // for FileEntry components with immediate=false in PreRender
            // before the core FacesMessagesPhaseListener works in PreRender.
            PhaseListener[] phaseListeners = lifecycle.getPhaseListeners();
            for (PhaseListener otherPhaseListener : phaseListeners) {
                lifecycle.removePhaseListener(otherPhaseListener);
            }
            lifecycle.addPhaseListener(phaseListener);
            for (PhaseListener otherPhaseListener : phaseListeners) {
                lifecycle.addPhaseListener(otherPhaseListener);
            }
        }
    }
    
    @ManagedProperty(value="name")
    private String name;

    public void setName(String n) {
        name = n;
    }
    
    public String getName() {
        return name;
    }
}
