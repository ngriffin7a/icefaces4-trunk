package org.icefaces.ace.component.dynamicresource;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;

import javax.faces.application.Resource;
import java.util.Date;

@Component(
        tagName         = "dynamicResource",
        componentClass  = "org.icefaces.ace.component.dynamicresource.DynamicResource",
        generatedClass  = "org.icefaces.ace.component.dynamicresource.DynamicResourceBase",
        rendererClass   = "org.icefaces.ace.component.dynamicresource.DynamicResourceRenderer",
        extendsClass    = "javax.faces.component.UIComponentBase",
        componentType   = "org.icefaces.ace.component.DynamicResource",
        rendererType    = "org.icefaces.ace.component.DynamicResourceRenderer",
        componentFamily = "org.icefaces.ace.component",
        tlddoc = "<p></p>")
public class DynamicResourceMeta {
    @Property(tlddoc = "Custom inline CSS styles to use for this component. These styles are generally applied to the root DOM element of the component. This is intended for per-component basic style customizations. Note that due to browser CSS precedence rules, CSS rendered on a DOM element will take precedence over the external stylesheets used to provide the ThemeRoller theme on this component. If the CSS properties applied with this attribute do not affect the DOM element you want to style, you may need to create a custom theme styleClass for the theme CSS class that targets the particular DOM elements you wish to customize.")
    private String style;

    @Property(tlddoc = "Custom CSS style class(es) to use for this component. These style classes can be defined in your page or in a theme CSS file.")
    private String styleClass;

    @Property(tlddoc="The file name to be used for the attachment header. If the label is not specified the file name will be used. See also the resource attribute.")
    private String fileName;

    @Property(tlddoc="The image path that will be used to display an image for link instead of text.")
    private String image;

    @Property(tlddoc="The type of link to render: link (default) image: renders a link with an image (must be used with the image attribute) button: renders a button with the text from value")
    private String type;

    @Property(tlddoc="The label to be displayed for the resource. The label will be used for the text of the link or button. If an image is used, the label will be used as the alt attribute value of the image element.")
    private String label;

    @Property(tlddoc="If true, specifies that the resource should be downloaded as an attachment. See also the resource attribute.")
    private boolean attachment;

    @Property(tlddoc="Value of the target attribute when resource is rendered as a link. Default is \"_blank\".")
    private String target;

    @Property(tlddoc="The mime-type for the resource. If not specified, and available from the backing Resource, the Resource's mime-type will be used. See also the resource attribute.")
    private String mimeType;

    @Property(tlddoc="The java.util.Data object specifying the last modified header that will be sent to the browser for this resource. See also the resource attribute.")
    private Date lastModified;

    @Property(tlddoc="Flag indicating that this element must never receive focus or be included in a subsequent submit.")
    private boolean disabled;

    @Property(tlddoc="The object of type com.icesoft.faces.context.Resource that will be used. It can be use to override the attachment, fileName, lastModified and mimeType attributes via the withOptions() method.")
    private Resource resource;

    @Property(tlddoc="The scope in which the resource will be stored. The values can be 'flash', 'view', window', 'session', and 'application'. When not specified the 'session' scope is used.")
    private String scope;

}
