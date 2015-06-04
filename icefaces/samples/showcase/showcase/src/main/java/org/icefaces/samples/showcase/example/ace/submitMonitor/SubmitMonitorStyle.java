package org.icefaces.samples.showcase.example.ace.submitMonitor;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
		parent = SubmitMonitorBean.BEAN_NAME,
        title = "example.ace.submitMonitor.style.title",
        description = "example.ace.submitMonitor.style.description",
        example = "/resources/examples/ace/submitMonitor/submitMonitorStyle.xhtml"
)
@ExampleResources(
        resources ={
            @ExampleResource(type = ResourceType.xhtml,
                    title="submitMonitorStyle.xhtml",
                    resource = "/resources/examples/ace/submitMonitor/submitMonitorStyle.xhtml"),
            @ExampleResource(type = ResourceType.java,
                    title="SubmitMonitorStyle.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/submitMonitor/SubmitMonitorStyle.java")
        }
)
@ManagedBean(name= SubmitMonitorStyle.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SubmitMonitorStyle extends ComponentExampleImpl<SubmitMonitorStyle> implements Serializable {
    public static final String BEAN_NAME = "submitMonitorStyle";
	public String getBeanName() { return BEAN_NAME; }
	
	private static final String DEFAULT_ICON = "bar";
	private static final SelectItem[] AVAILABLE_ICONS = { 
		new SelectItem(DEFAULT_ICON, "Animated Bar"),
		new SelectItem("bounce", "Bouncing Ball"),
		new SelectItem("spin", "Twirling Spinner")
	};
	
	private String activeIcon = "bar";
	
    public SubmitMonitorStyle() {
        super(SubmitMonitorStyle.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
    
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
