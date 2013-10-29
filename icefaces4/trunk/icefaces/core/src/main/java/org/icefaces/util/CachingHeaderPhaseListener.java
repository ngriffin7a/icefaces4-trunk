package org.icefaces.util;

import javax.annotation.PostConstruct;
import javax.faces.FactoryFinder;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import java.io.Serializable;

/**
 * User: Nils Lundquist
 * Date: 5/22/13
 * Time: 9:05 AM
 * Prevent browser from caching ICEfaces GET requests as part of browser history.
 * Returning to the old GET request result and attempt to interact with it will
 * result in a mismatch between the old DOM held by DOM diff, and the DOM being
 * show to the user.
 *
 * Besides returning to a state other than the one they left being confusing for
 * the user, this could cause JS errors if DOM diff attempts to narrow a response
 * to a region that is not in the client DOM.
 */
@ManagedBean(eager = true)
@ApplicationScoped
public class CachingHeaderPhaseListener implements Serializable {
    @PostConstruct
    private void initialize() {
        LifecycleFactory factory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Lifecycle lifecycle = factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        lifecycle.addPhaseListener(new PhaseListener() {
            public void afterPhase(PhaseEvent phaseEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void beforePhase(PhaseEvent phaseEvent) {
                ExternalContext ec = phaseEvent.getFacesContext().getExternalContext();
                Object responseObj = ec.getResponse();

                //Attempting to add these headers to a PortletResponse that is not of type ResourceResponse results
                //in the portlet bridge logging warnings.  So we avoid doing it in that particular scenario.
                boolean avoidAddingHeaders = EnvUtils.instanceofPortletResponse(responseObj) &&
                                             !EnvUtils.instanceofPortletResourceResponse(responseObj);

                if (!avoidAddingHeaders) {
                    ec.addResponseHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                    ec.addResponseHeader("Pragma", "no-cache");
                    ec.addResponseHeader("Expires", "0");
                }
            }

            public PhaseId getPhaseId() {
                return PhaseId.RENDER_RESPONSE;
            }
        });
    }
}
