/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.ace.component.animation;

import org.icefaces.ace.meta.annotation.TagHandler;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.TagHandlerType;
import org.icefaces.ace.meta.annotation.Required;
import org.icefaces.ace.meta.annotation.Implementation;

@TagHandler(
    tagName = "animation",
    tagHandlerType = TagHandlerType.BEHAVIOR_HANDLER,
    tagHandlerClass = "org.icefaces.ace.component.animation.AnimationBehaviorHandler",
    generatedClass = "org.icefaces.ace.component.animation.AnimationBehaviorHandlerBase",
    extendsClass = "javax.faces.view.facelets.BehaviorHandler",
	behaviorId = "org.icefaces.ace.animation.Animation",
	behaviorClass = "org.icefaces.ace.component.animation.AnimationBehavior",
    tlddoc="The ace:animation tag provides a way to animate standard JSF components. Animations are based on the jQuery effects utilities." +
                 "<p>For more information, see the " +
                 "<a href=\"http://wiki.icefaces.org/display/ICE/Animation\">Animation Wiki Documentation</a>."
)
public class AnimationBehaviorHandlerMeta {

    @Property(required=Required.no, tlddoc="(Deprecated) Boolean value that specifies whether to run the application or not.")
    private boolean run;

    @Property(required=Required.no, tlddoc="The name of the effect, when using pre-defined effects like 'fade' and 'highlight'. Use 'anim' for free-form animations or simply omit this attribute.")
    private String name;

    @Property(required=Required.no, tlddoc="CSS properties to transition from (e.g. {height:20, color:'#222'}).")
    private String from;

    @Property(required=Required.no, tlddoc="CSS properties to transition to (e.g. {height:100, color:'#CCC'}).")
    private String to;

    @Property(required=Required.no, tlddoc="Specifies the variation of the rate at which an animation progresses. Possible values are swing, easeInQuad, easeOutQuad, easeInOutQuad, easeInCubic, easeOutCubic, easeInQuart, easeOutQuart, easeInOutQuart, easeInQuint, easeOutQuint, easeInOutQuint, easeInSine, easeOutSine, easeInOutSine, easeInExpo, easeOutExpo, easeInOutExpo, easeInCirc, easeOutCirc, easeInOutCirc, easeInElastic, easeOutElastic, easeInOutElastic, easeInBack, easeOutBack, easeInOutBack, easeInBounce, easeOutBounce, easeInOutBounce.")
    private String easing;

    @Property(required=Required.no, tlddoc="Number of times the animation is to be repeated.")
    private Integer iterations;

    @Property(required=Required.no, tlddoc="The Effect object that contains the effect specifications to apply.")
    private Effect effectObject;

    @Property(required=Required.no, tlddoc="Length of effect in milliseconds. The default value is 500 for free-form animations and varies for pre-defined effects.")
    private Double duration;
	
    @Property(required=Required.no, tlddoc="Name of the event that will trigger the animation (e.g. 'click'). The event must be supported by the component. The TLD documentation of each standard JSF component lists which events are supported, which usually map to standard DOM events. The event name must be entered without the 'on' prefix (e.g. for 'onmousemove' the value of this attribute must be 'mousemove').", implementation=Implementation.EXISTS_IN_SUPERCLASS)
    private String event;
}