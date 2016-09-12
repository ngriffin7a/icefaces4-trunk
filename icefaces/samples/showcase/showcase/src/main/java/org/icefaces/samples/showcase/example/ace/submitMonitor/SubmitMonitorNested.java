package org.icefaces.samples.showcase.example.ace.submitMonitor;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

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
}
