package org.icefaces.samples.showcase.example.ace.submitMonitor;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

@ManagedBean(name= SubmitMonitorStyle.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SubmitMonitorStyle implements Serializable {
    public static final String BEAN_NAME = "submitMonitorStyle";
	public String getBeanName() { return BEAN_NAME; }
	
	private static final String DEFAULT_ICON = "bar";
	private static final SelectItem[] AVAILABLE_ICONS = { 
		new SelectItem(DEFAULT_ICON, "Animated Bar"),
		new SelectItem("bounce", "Bouncing Ball"),
		new SelectItem("spin", "Twirling Spinner")
	};
	
	private String activeIcon = "bar";
	
    public SelectItem[] getAvailableIcons() {
    	return AVAILABLE_ICONS;
    }

	public String getActiveIcon() {
		return activeIcon;
	}

	public void setActiveIcon(String activeIcon) {
		this.activeIcon = activeIcon;
	}
}
