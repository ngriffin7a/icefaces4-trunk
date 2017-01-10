package org.icefaces.ace.component.resizable;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

public class ResizableListener implements SystemEventListener {
    public void processEvent(SystemEvent systemEvent) throws AbortProcessingException {
        Resizable resizable = (Resizable) systemEvent.getSource();
        resizable.attachRenderer();
    }

    public boolean isListenerForSource(Object o) {
        return o instanceof Resizable;
    }
}
