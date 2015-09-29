package org.icefaces.samples.showcase.example.ace.submitMonitor;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

@ManagedBean(name= SubmitMonitorFacet.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SubmitMonitorFacet implements Serializable {
    public static final String BEAN_NAME = "submitMonitorFacet";
	public String getBeanName() { return BEAN_NAME; }
	
	public void forceError(ActionEvent event) {
		// This is an intentional error so we can demonstrate the ace:submitMonitor serverError state
		int divideByZeroError = 5/0;
	}
}
