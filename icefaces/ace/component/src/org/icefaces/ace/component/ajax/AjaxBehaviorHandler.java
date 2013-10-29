/*
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: (ICE-8258) Completely re-architected to bring more in line with f:ajax capability
 * of working with composite components and ui:parameters.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 */
package org.icefaces.ace.component.ajax;

import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorListener;
import javax.faces.view.AttachedObjectHandler;
import javax.faces.view.AttachedObjectTarget;
import javax.faces.view.BehaviorHolderAttachedObjectHandler;
import javax.faces.view.BehaviorHolderAttachedObjectTarget;
import javax.faces.view.facelets.*;

import org.icefaces.ace.api.IceClientBehaviorHolder;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.io.IOException;
import java.util.*;

public class AjaxBehaviorHandler extends AjaxBehaviorHandlerBase implements BehaviorHolderAttachedObjectHandler {
    protected final boolean wrapping;

    public AjaxBehaviorHandler(TagConfig config) {
        super(config);
        wrapping = isWrapping();
    }

    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        //System.out.println("AjaxBehaviorHandler.apply()  parent: " + parent + "  id: " + parent.getId());
        //System.out.println("AjaxBehaviorHandler.apply()  wrapping: " + wrapping);
        //System.out.println("AjaxBehaviorHandler.apply()  isCompositeComponent(parent): " + UIComponent.isCompositeComponent(parent));

        String eventName = getEventName();
        if (this.wrapping) {
            applyWrapping(ctx, parent, eventName);
        }  else {
            applyNested(ctx, parent, eventName);
        }
    }

    protected boolean isWrapping() {
        // No easy way to determine if a TagHandler is a leaf, since even
        // leafs have a CompilationUnit.LEAF nextHandler, which is an impl
        // class' private field. MyFaces checks instanceof on a bunch of
        // other impl classes, while Mojarra checks against public api classes
        return ((this.nextHandler instanceof TagHandler) ||
                (this.nextHandler instanceof CompositeFaceletHandler));
    }

    protected void applyWrapping(FaceletContext ctx, UIComponent parent,
            String eventName) throws IOException {
        // MyFaces and Mojarra, in their AjaxHandler code that covers the
        // wrapping scenario, basically put something from the AjaxHandler
        // into a stack in proprietary classes and mechanisms, so that when
        // we call nextHandler.apply(-), any composite component and
        // ClientBehaviorHolder tags within us will be able to access that
        // stack, and have an AjaxBehavior from us be added to them, if it's
        // applicable to them. Next they pop that from the stack.
        // It seems to me to be a hack to get around not being able to pass
        // info to child tags, nor access ancestor tags, so that a global
        // stack is needed to maintain scope.
        // Worst of all, no hook into the mechanism is standardised, and
        // they're implemented completely differently with proprietary and
        // closed mechanisms by each implementation.
        // MyFaces stores the AttachedObjectHandler/AjaxHandler references so
        // that CompositeComponentResourceTagHandler.applyNextHandler(-) can
        // access them and pass them to [Facelet]ViewDeclarationLanguage's
        // retargetAttachedObjects(-) method, which calls our
        // AttachedObjectHandler.applyAttachedObject(-) method.
        // Mojarra is even worse, because it hard-codes the idea of only using
        // an f:ajax AjaxHandler, and so creates one and stores that on its
        // stack. ComponentTagHandlerDelegateImpl.privateOnComponentPopulated(-)
        // then essentially inlines AjaxHandler.applyAttachedObject(-). It
        // seems to apply to both composite components and ClientBehaviorHolder(s)

        nextHandler.apply(ctx, parent);
    }

    // Applies a nested AjaxHandler by adding the AjaxBehavior to the
    // parent component.
    private void applyNested(FaceletContext ctx, UIComponent parent, String eventName) {
        if (!ComponentHandler.isNew(parent)) {
            return;
        }

        // Composite component can be both a ClientBehaviorHolder and
        // have AttachedObjectTarget(s). Error if neither.
        boolean isClientBehaviorHolder = (parent instanceof ClientBehaviorHolder);
        boolean isCompositeComponent = UIComponent.isCompositeComponent(parent);
        verifyNestClientBehaviorHolderAndOrCompositeComp(
            isClientBehaviorHolder, isCompositeComponent);
        if (isClientBehaviorHolder) {
            // This checks the event name
            applyAttachedObject(ctx, parent, eventName, true);
        }
        if (isCompositeComponent) {
            BeanInfo beanInfo = (BeanInfo) parent.getAttributes().get(
                UIComponent.BEANINFO_KEY);
            verifyNestCompositeCompBeanInfo(beanInfo);
            BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();
            verifyNestCompositeCompBeanDescriptor(beanDescriptor);
            List<AttachedObjectTarget> attachedObjectTargetList =
                (List<AttachedObjectTarget>) beanDescriptor.getValue(
                      AttachedObjectTarget.ATTACHED_OBJECT_TARGETS_KEY);
            // Verify that is ClientBehaviorHolder, or there exists an
            // AttachedObjectTarget corresponding to our event.
            boolean isCorrespondingAttachedObjectTarget =
                verifyNestCompositeCompTakesBehaviorEvent(
                    isClientBehaviorHolder, attachedObjectTargetList, eventName);
            if (isCorrespondingAttachedObjectTarget) {
                // The Mojarra way
                // From CompositeComponentTagHandler.getAttachedObjectHandlers(-)
                Map<String, Object> attrs = parent.getAttributes();
                final String key = "javax.faces.RetargetableHandlers";
                List<AttachedObjectHandler> handlers =
                    (List<AttachedObjectHandler>) attrs.get(key);
                if (null == handlers) {
                    handlers = new ArrayList<AttachedObjectHandler>(6);
                    attrs.put(key, handlers);
                }
                handlers.add(this);

                // For MyFaces, might be able to do something like this, since
                // their CompositeComponentResourceTagHandler.applyNextHandler
                // invokes us, then their AjaxHandler just adds itself to an
                // impl class list so that later in CCRTH.applyNextHndler, it
                // can do essentially the following:
                /*
                List<AttachedObjectHandler> handlers = new ArrayList<AttachedObjectHandler>(1);
                handlers.add(this);
                ViewDeclarationLanguage vdl = facesContext.getApplication().getViewHandler().
                    getViewDeclarationLanguage(facesContext, facesContext.getViewRoot().getViewId());
                vdl.retargetAttachedObjects(facesContext, c, handlers);
                */
            }
        }
    }

    /**
     * @see javax.faces.view.BehaviorHolderAttachedObjectHandler#getEventName()
     */
    public String getEventName() {
        return (this.event != null) ? this.event.getValue() : null;
    }

    /**
     * @see javax.faces.view.AttachedObjectHandler#getFor()
     */
    public String getFor() {
        return null;
    }

    /**
     * @see javax.faces.view.AttachedObjectHandler#applyAttachedObject(FacesContext, UIComponent)
     */
    public void applyAttachedObject(FacesContext context, UIComponent parent) {
        FaceletContext ctx = (FaceletContext) context.getAttributes().get(
            FaceletContext.FACELET_CONTEXT_KEY);
        applyAttachedObject(ctx, parent, getEventName(), false);
    }

    protected void applyAttachedObject(FaceletContext ctx, UIComponent parent,
            String eventName, boolean calledOurselves) {
        ClientBehaviorHolder parentClientBehaviorHolder = (ClientBehaviorHolder) parent;
        boolean applicable = verifyAttachClientBehaviorHolder(eventName,
            parentClientBehaviorHolder, calledOurselves);
        if (applicable) {
            if (null == eventName) {
                eventName = parentClientBehaviorHolder.getDefaultEventName();
            }
            ClientBehavior ajaxBehavior = createAjaxBehavior(ctx, parent, eventName);
            parentClientBehaviorHolder.addClientBehavior(eventName, ajaxBehavior);
        }
    }

    // Construct our AjaxBehavior from tag parameters.
    protected ClientBehavior createAjaxBehavior(FaceletContext ctx,
            UIComponent parent, String eventName) {
        Application application = ctx.getFacesContext().getApplication();
        AjaxBehavior behavior = (AjaxBehavior) application.createBehavior(
            BEHAVIOR_ID);

        setBehaviorAttribute(ctx, behavior, this.onStart, AjaxBehavior.Property.onStart);
        setBehaviorAttribute(ctx, behavior, this.onComplete, AjaxBehavior.Property.onComplete);
        setBehaviorAttribute(ctx, behavior, this.onSuccess, AjaxBehavior.Property.onSuccess);
        setBehaviorAttribute(ctx, behavior, this.onError, AjaxBehavior.Property.onError);
        setBehaviorAttribute(ctx, behavior, this.disabled, AjaxBehavior.Property.disabled);
        setBehaviorAttribute(ctx, behavior, this.immediate, AjaxBehavior.Property.immediate);
        setBehaviorAttribute(ctx, behavior, this.execute, AjaxBehavior.Property.execute);
        setBehaviorAttribute(ctx, behavior, this.render, AjaxBehavior.Property.render);

        addListenerToAjaxBehavior(ctx, parent, behavior, eventName);
        return behavior;
    }

    protected void setBehaviorAttribute(FaceletContext ctx, AjaxBehavior behavior,
            TagAttribute attr, AjaxBehavior.Property property) {
        if (null != attr) {
            if (attr.isLiteral()) {
                behavior.setLiteral(property, attr.getObject(ctx, property.expectedType));
            } else {
                behavior.setValueExpression(property,
                    attr.getValueExpression(ctx, property.expectedType));
            }
        }
    }

    protected void addListenerToAjaxBehavior(FaceletContext ctx,
            UIComponent parent, AjaxBehavior ajaxBehavior, String eventName) {
        //System.out.println("AjaxBehaviorHandler.addListenerToAjaxBehavior()  parent: " + parent);
        //System.out.println("AjaxBehaviorHandler.addListenerToAjaxBehavior()  ajaxBehavior: " + ajaxBehavior);
        //System.out.println("AjaxBehaviorHandler.addListenerToAjaxBehavior()  listener: " + listener);
        if (null == listener) {
            return;
        }
        Class superArgEventClass = javax.faces.event.AjaxBehaviorEvent.class;
        Class oneArgEventClass = deriveEventClass(parent, eventName, superArgEventClass);
        final Class returnType = null;
        MethodExpression noArg = listener.getMethodExpression(ctx, returnType,
            new Class[0]);
        MethodExpression oneArg = (oneArgEventClass == null) ? null :
            listener.getMethodExpression(ctx, returnType,
                new Class[] {oneArgEventClass});
        MethodExpression superArg = listener.getMethodExpression(
            ctx, returnType, new Class[] {superArgEventClass});
        AjaxBehaviorListener behaviorListener = new AjaxBehaviorListenerImpl(
            noArg, oneArg, superArg);
        ajaxBehavior.addBehaviorListener(behaviorListener);
    }

    protected Class deriveEventClass(UIComponent parent, String eventName, Class superArgEventClass) {
        Class oneArgEventClass = null;
        if (eventName != null && parent instanceof IceClientBehaviorHolder) {
            IceClientBehaviorHolder aceParent = (IceClientBehaviorHolder) parent;
            String oneArgEventClassName = aceParent.getListenerArgument(eventName);
            if (oneArgEventClassName != null && !superArgEventClass.getName().
                    equals(oneArgEventClassName)) {
                try {
                    Class clazz = Class.forName(oneArgEventClassName.toString());
                    if (superArgEventClass.isAssignableFrom(clazz)) {
                        oneArgEventClass = clazz;
                    }
                } catch(Exception e) {
                    throw new RuntimeException("For component '" +
                        parent.getClass().getName() +"', for the '" + eventName
                        + "' ClientEvent, the listener argument class '" +
                        oneArgEventClassName + "' can not load. Either its " +
                        "Meta class' @ClientEvent annotation's " +
                        "argumentClass field was specific incorrectly, or " +
                        "the component's getListenerArgument(String event) " +
                        "method was implemented incorrectly.", e);
                }
            }
            // oneArgEventClassName being superArgEventClass indicates it may 
            // not have been specified
            else {
                // Derive the event class name from the event name
                StringBuilder className = new StringBuilder("org.icefaces.ace.event.");
                int toUpperIndex = className.length();
                className.append(eventName).append("Event");
                className.setCharAt(toUpperIndex, Character.toUpperCase(className.charAt(toUpperIndex)));
                try {
                    Class clazz = Class.forName(className.toString());
                    if (superArgEventClass.isAssignableFrom(clazz)) {
                        oneArgEventClass = clazz;
                    }
                } catch(Exception e) {
                    // Silently eat this. It's typical to not have this event
                }
            }
        }
        return oneArgEventClass;
    }

    protected void verifyNestClientBehaviorHolderAndOrCompositeComp(
            boolean isClientBehaviorHolder, boolean isCompositeComponent) {
        if (!isClientBehaviorHolder && !isCompositeComponent) {
            throw new TagException(this.tag,
                "Error: ace:ajax is nested in a parent that does not support behaviors");
        }
    }
    
    protected void verifyNestCompositeCompBeanInfo(BeanInfo bi) {
        if (null == bi) {
            throw new TagException(tag,
                "Error: ace:ajax is nested in a composite component that does not have a BeanInfo attribute");
        }
    }

    protected void verifyNestCompositeCompBeanDescriptor(BeanDescriptor beanDescriptor) {
        if (null == beanDescriptor) {
            throw new TagException(tag,
                "Error: ace:ajax is nested in a composite component that does not have a BeanDescriptor");
        }
    }

    protected boolean verifyNestCompositeCompTakesBehaviorEvent(
            boolean isClientBehaviorHolder,
            List<AttachedObjectTarget> attachedObjectTargetList,
            String eventName) {
        if (!isClientBehaviorHolder && null == attachedObjectTargetList) {
            throw new TagException(tag,
                "Error: ace:ajax is nested in a composite component that does not support behaviors");
        }

        boolean isCorrespondingAttachedObjectTarget = false;
        if (null != attachedObjectTargetList) {
            for (AttachedObjectTarget target : attachedObjectTargetList) {
                if (target instanceof BehaviorHolderAttachedObjectTarget) {
                    BehaviorHolderAttachedObjectTarget behaviorTarget =
                        (BehaviorHolderAttachedObjectTarget) target;
                    if ((null != eventName &&
                         eventName.equals(behaviorTarget.getName()))
                        ||
                        (null == eventName &&
                         behaviorTarget.isDefaultEvent()))
                    {
                        isCorrespondingAttachedObjectTarget = true;
                        break;
                    }
                }
            }
        }
        if (!isClientBehaviorHolder && !isCorrespondingAttachedObjectTarget) {
            throw new TagException(tag,
                "Error: ace:ajax is nested in a composite component that does not support its event: "
                + eventName);
        }
        return isCorrespondingAttachedObjectTarget;
    }

    protected boolean verifyAttachClientBehaviorHolder(String eventName,
            ClientBehaviorHolder parentClientBehaviorHolder, boolean doThrow) {
        if (null == eventName) {
            eventName = parentClientBehaviorHolder.getDefaultEventName();
            if (null == eventName) {
                if (!doThrow) {
                    return false;
                }
                throw new TagException(this.tag,
                    "Error: ace:ajax has unspecified event name, and is " +
                        "nested in a ClientBehaviorHolder that does not " +
                        "specify a default event name");
            }
        } else {
            Collection<String> eventNames =
                parentClientBehaviorHolder.getEventNames();
            if (!eventNames.contains(eventName)) {
                if (!doThrow) {
                    return false;
                }
                throw new TagException(this.tag,
                    "Error: ace:ajax specifies an event name that is " +
                        "unsupported by its parent ClientBehaviorHolder. " +
                        "Event name: " + eventName + ", Parent: " +
                        parentClientBehaviorHolder + ", Valid values: " +
                        eventNames);
            }
        }
        return true;
    }
}