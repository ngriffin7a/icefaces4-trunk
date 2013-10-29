<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE faces-config PUBLIC
  "-//Sun Microsystems, Inc.//DTD JavaServer Faces Config 1.1//EN"
  "http://java.sun.com/dtd/web-facesconfig_1_1.dtd">
<faces-config>
<#list componentBeans as componentBean>
    <component>
        <component-type>${componentBean.componentType}</component-type>
        <component-class>${componentBean.componentClass}</component-class>
    </component>
</#list>
    <render-kit>
        <#list rendererBeans as rendererBean>
            <renderer>
                <component-family>${rendererBean.componentFamily}</component-family>
                <renderer-type>${rendererBean.rendererType}</renderer-type>
                <renderer-class>${rendererBean.rendererClass}</renderer-class>
            </renderer>
        </#list>
    </render-kit>


    <!-- configuration taken from compat/components -->
    <ordering>
        <after>
            <name>ICEfacesCore</name>
        </after>
    </ordering>
    <lifecycle>
        <phase-listener>com.icesoft.faces.application.PartialSubmitPhaseListener</phase-listener>
    </lifecycle>
    <application>
        <resource-handler>com.icesoft.faces.application.ExtrasSymbolicResourceHandler</resource-handler>
        <resource-handler>com.icesoft.faces.component.inputrichtext.InputRichTextResourceHandler</resource-handler>
        <resource-handler>com.icesoft.faces.component.gmap.GMapResourceHandler</resource-handler>
        <system-event-listener>
            <system-event-listener-class>com.icesoft.faces.application.ExtrasSetup</system-event-listener-class>
            <system-event-class>javax.faces.event.PreRenderComponentEvent</system-event-class>
        </system-event-listener>
        <system-event-listener>
            <system-event-listener-class>com.icesoft.faces.component.JavaScriptContextSetup</system-event-listener-class>
            <system-event-class>javax.faces.event.PreRenderViewEvent</system-event-class>
        </system-event-listener>
        <system-event-listener>
            <system-event-listener-class>com.icesoft.faces.async.render.SessionRenderer$StartupListener
            </system-event-listener-class>
            <system-event-class>javax.faces.event.PostConstructApplicationEvent</system-event-class>
        </system-event-listener>
    </application>
    <factory>
        <partial-view-context-factory>com.icesoft.faces.context.CompatDOMPartialViewContextFactory
        </partial-view-context-factory>
    </factory>
</faces-config>
