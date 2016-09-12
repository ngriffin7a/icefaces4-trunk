package org.icefaces.samples.showcase.example.ace.submitMonitor;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;


@ManagedBean(name= SubmitMonitorNested.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SubmitMonitorNested implements Serializable {
    public static final String BEAN_NAME = "submitMonitorNested";
	public String getBeanName() { return BEAN_NAME; }
	
	private boolean renderPanel = true;
	private boolean renderList = true;
	
	public boolean isRenderPanel() {
		return renderPanel;
	}

	public void setRenderPanel(boolean renderPanel) {
		this.renderPanel = renderPanel;
	}

	public boolean isRenderList() {
		return renderList;
	}

	public void setRenderList(boolean renderList) {
		this.renderList = renderList;
	}
	
	public void forceError(ActionEvent event) {
		// This is an intentional error so we can demonstrate the ace:submitMonitor serverError state
		int divideByZeroError = 5/0;
	}	

	
	
}
