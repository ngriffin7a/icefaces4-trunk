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
* Code Modification 2: [ADD BRIEF DESCRIPTION HERE] 
* Contributors: ______________________ 
* Contributors: ______________________ 
* 
*/

/**
 *  Panel Widget
 */
ice.ace.Panel = function(id, cfg) {
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(id);

    if(this.cfg.toggleable) {
        this.toggler = ice.ace.jq(this.jqId + '_toggler');
        this.toggleStateHolder = ice.ace.jq(this.jqId + '_collapsed');
        this.content = ice.ace.jq(this.jqId + '_content');

        this.setupToggleTrigger();
    }

    if(this.cfg.closable) {
        this.visibleStateHolder = ice.ace.jq(this.jqId + "_visible");

        this.setupCloseTrigger();
    }

    if(this.cfg.hasMenu) {
        this.visibleStateHolder = ice.ace.jq(this.jqId + "_visible");

        this.setupMenuTrigger();
    }
	
    if(!this.cfg.visible) {
        ice.ace.jq(this.jqId).css('display','none');
    }
}

ice.ace.Panel.prototype.toggle = function() {
    if(this.cfg.collapsed) {
        this.toggler.removeClass('ui-icon-plusthick').addClass('ui-icon-minusthick');
        this.cfg.collapsed = false;
        this.toggleStateHolder.val(false);
    }
    else {
        this.toggler.removeClass('ui-icon-minusthick').addClass('ui-icon-plusthick');
        this.cfg.collapsed = true;
        this.toggleStateHolder.val(true);
    }
	
    var _self = this;

    this.content.slideToggle(this.cfg.toggleSpeed,
        function(e) {
            if(_self.cfg.behaviors) {
                var toggleBehavior = _self.cfg.behaviors['toggle'];
                if (toggleBehavior) {
                    ice.ace.ab(toggleBehavior);
                }
            }
			// handle disableInputs overlay
			var panel = ice.ace.jq(ice.ace.escapeClientId(_self.id + '_content'));
			var overlay = panel.find('.ui-disableinputs');
			var overlayNode = overlay.get(0);
			if (overlayNode) {
				if (_self.cfg.collapsed == false) {
					ice.ace.Panel.positionOverlay(_self.id, overlayNode);
					overlay.show();
				} else {
					overlay.hide();
				}
			}
        });
}

ice.ace.Panel.prototype.close = function() {
    this.visibleStateHolder.val(false);

    var _self = this;

    ice.ace.jq(this.jqId).fadeOut(this.cfg.closeSpeed, '',
        function(e) {
            if(_self.cfg.behaviors) {
                var closeBehavior = _self.cfg.behaviors['close'];
                if (closeBehavior) {
                    ice.ace.ab(closeBehavior)
                }
            }
        }
    );
}

ice.ace.Panel.prototype.show = function() {
    ice.ace.jq(this.jqId).fadeIn(this.cfg.closeSpeed);
	
    this.visibleStateHolder.val(true);
}

ice.ace.Panel.prototype.setupToggleTrigger = function() {
    var _self = this,
    trigger = this.toggler.parent();

    this.setupTriggerVisuals(trigger);
    
    trigger.click(function() {_self.toggle();});
}

ice.ace.Panel.prototype.setupCloseTrigger = function() {
    var _self = this,
    trigger = ice.ace.jq(this.jqId + '_closer').parent();

    this.setupTriggerVisuals(trigger);
    
    trigger.click(function() {_self.close();});
}

ice.ace.Panel.prototype.setupMenuTrigger = function() {
    var trigger = ice.ace.jq(this.jqId + '_menu').parent();

    this.setupTriggerVisuals(trigger);
}

ice.ace.Panel.prototype.setupTriggerVisuals = function(trigger) {
    trigger.mouseover(function() {ice.ace.jq(this).addClass('ui-state-hover');})
            .mouseout(function() {ice.ace.jq(this).removeClass('ui-state-hover');});
}
