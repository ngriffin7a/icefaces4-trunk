package org.icefaces.samples.showcase.example.ace.submitMonitor;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name= SubmitMonitorConfiguration.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SubmitMonitorConfiguration implements Serializable {
    public static final String BEAN_NAME = "submitMonitorConfiguration";
    
	public String getBeanName() { return BEAN_NAME; }
    private String idleLabel = "Idle";
    private String activeLabel = "Submitting...";
    private String serverErrorLabel = "Server error";
    private String networkErrorLabel = "Network error";
    private String sessionExpiredLabel = "Session expired";
    private boolean autoCenter = true;
    private boolean blockPanel = false;

    public String getIdleLabel() {
        return idleLabel;
    }
    public void setIdleLabel(String idleLabel) {
        this.idleLabel = idleLabel;
    }

    public String getActiveLabel() {
        return activeLabel;
    }
    public void setActiveLabel(String activeLabel) {
        this.activeLabel = activeLabel;
    }

    public String getServerErrorLabel() {
        return serverErrorLabel;
    }
    public void setServerErrorLabel(String serverErrorLabel) {
        this.serverErrorLabel = serverErrorLabel;
    }

    public String getNetworkErrorLabel() {
        return networkErrorLabel;
    }
    public void setNetworkErrorLabel(String networkErrorLabel) {
        this.networkErrorLabel = networkErrorLabel;
    }

    public String getSessionExpiredLabel() {
        return sessionExpiredLabel;
    }
    public void setSessionExpiredLabel(String sessionExpiredLabel) {
        this.sessionExpiredLabel = sessionExpiredLabel;
    }

    public boolean isAutoCenter() {
		return autoCenter;
	}
	public void setAutoCenter(boolean autoCenter) {
		this.autoCenter = autoCenter;
	}

	public boolean isBlockPanel() {
		return blockPanel;
	}
	public void setBlockPanel(boolean blockPanel) {
		this.blockPanel = blockPanel;
	}
}
