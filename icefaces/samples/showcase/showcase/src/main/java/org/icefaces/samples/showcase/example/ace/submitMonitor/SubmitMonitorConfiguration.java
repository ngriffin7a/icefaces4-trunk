package org.icefaces.samples.showcase.example.ace.submitMonitor;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
		parent = SubmitMonitorBean.BEAN_NAME,
        title = "example.ace.submitMonitor.configuration.title",
        description = "example.ace.submitMonitor.configuration.description",
        example = "/resources/examples/ace/submitMonitor/submitMonitorConfiguration.xhtml"
)
@ExampleResources(
        resources ={
            @ExampleResource(type = ResourceType.xhtml,
                    title="submitMonitorConfiguration.xhtml",
                    resource = "/resources/examples/ace/submitMonitor/submitMonitorConfiguration.xhtml"),
            @ExampleResource(type = ResourceType.java,
                    title="SubmitMonitorConfiguration.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/submitMonitor/SubmitMonitorConfiguration.java")
        }
)
@ManagedBean(name= SubmitMonitorConfiguration.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SubmitMonitorConfiguration extends ComponentExampleImpl<SubmitMonitorConfiguration> implements Serializable {
    public static final String BEAN_NAME = "submitMonitorConfiguration";
    
	public String getBeanName() { return BEAN_NAME; }
    private String idleLabel = "Idle";
    private String activeLabel = "Submitting...";
    private String serverErrorLabel = "Server error";
    private String networkErrorLabel = "Network error";
    private String sessionExpiredLabel = "Session expired";
    private boolean autoCenter = true;
    private boolean blockPanel = false;

    public SubmitMonitorConfiguration() {
        super(SubmitMonitorConfiguration.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

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
