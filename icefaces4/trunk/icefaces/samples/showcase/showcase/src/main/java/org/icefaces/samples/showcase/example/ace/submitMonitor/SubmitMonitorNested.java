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
        title = "example.ace.submitMonitor.nested.title",
        description = "example.ace.submitMonitor.nested.description",
        example = "/resources/examples/ace/submitMonitor/submitMonitorNested.xhtml"
)
@ExampleResources(
        resources ={
            @ExampleResource(type = ResourceType.xhtml,
                    title="submitMonitorNested.xhtml",
                    resource = "/resources/examples/ace/submitMonitor/submitMonitorNested.xhtml"),
            @ExampleResource(type = ResourceType.java,
                    title="SubmitMonitorNested.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/submitMonitor/SubmitMonitorNested.java")
        }
)
@ManagedBean(name= SubmitMonitorNested.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SubmitMonitorNested extends ComponentExampleImpl<SubmitMonitorNested> implements Serializable {
    public static final String BEAN_NAME = "submitMonitorNested";
	public String getBeanName() { return BEAN_NAME; }
	
	private boolean renderPanel = true;
	private boolean renderList = true;
	
    public SubmitMonitorNested() {
        super(SubmitMonitorNested.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

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
