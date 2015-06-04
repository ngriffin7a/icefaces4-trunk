package org.icefaces.samples.showcase.example.ace.submitMonitor;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
		parent = SubmitMonitorBean.BEAN_NAME,
        title = "example.ace.submitMonitor.facet.title",
        description = "example.ace.submitMonitor.facet.description",
        example = "/resources/examples/ace/submitMonitor/submitMonitorFacet.xhtml"
)
@ExampleResources(
        resources ={
            @ExampleResource(type = ResourceType.xhtml,
                    title="submitMonitorFacet.xhtml",
                    resource = "/resources/examples/ace/submitMonitor/submitMonitorFacet.xhtml"),
            @ExampleResource(type = ResourceType.java,
                    title="SubmitMonitorFacet.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/submitMonitor/SubmitMonitorFacet.java")
        }
)
@ManagedBean(name= SubmitMonitorFacet.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SubmitMonitorFacet extends ComponentExampleImpl<SubmitMonitorFacet> implements Serializable {
    public static final String BEAN_NAME = "submitMonitorFacet";
	public String getBeanName() { return BEAN_NAME; }
	
    public SubmitMonitorFacet() {
        super(SubmitMonitorFacet.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public void forceError(ActionEvent event) {
		// This is an intentional error so we can demonstrate the ace:submitMonitor serverError state
		int divideByZeroError = 5/0;
	}
}
