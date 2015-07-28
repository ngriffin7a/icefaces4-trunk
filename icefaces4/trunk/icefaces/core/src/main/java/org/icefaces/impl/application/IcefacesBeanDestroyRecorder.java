package org.icefaces.impl.application;

import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.SystemEvent;
import java.io.Serializable;

@ManagedBean (eager = true)
@ViewScoped
public class IcefacesBeanDestroyRecorder implements Serializable {
    private boolean disposed;

    public IcefacesBeanDestroyRecorder() {
    }

    @PreDestroy
    public void dispose() {
        disposed = true;
    }

    public boolean isDisposed() {
        return disposed;
    }
}
