/* 
* Original Code Copyright Prime Technology.
* Subsequent Code Modifications Copyright 2011-2014 ICEsoft Technologies Canada Corp. (c)
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
if (!window['ice']) window.ice = {};
if (!window.ice['ace']) window.ice.ace = {};
if (!ice.ace.NotificationsPanels) ice.ace.NotificationPanels = {};
ice.ace.NotificationBar = function(id, cfg) {
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(id);
    this.jq = ice.ace.jq(this.jqId);
    var init = false;
    if (init==false && ice.ace.NotificationPanels[this.id]){
        init = true;
    }

	var portlet = ice.ace.NotificationBar.getPortletContainer(this.jq.get(0));
	if (!portlet) {
		this.jq.css(this.cfg.position, '0');
		this.jq.css("left", '0');
	} else {
		// obtain position of portlet
		var $portlet = ice.ace.jq(portlet);
		var portletPosition = $portlet.position();
		var portletWidth = $portlet.outerWidth();
		var leftBorder = parseInt(this.jq.css('border-left-width'));
		var rightBorder = parseInt(this.jq.css('border-right-width'));
		var leftPadding = parseInt(this.jq.css('padding-left'));
		var rightPadding = parseInt(this.jq.css('padding-right'));
		var computedWidth = portletWidth - leftBorder - rightBorder - leftPadding - rightPadding;
		//this.cfg.position
		// obtain width of portlet
		// set all those values to the panel
		// can't be 'fixed', need to use different style classes
		this.jq.css({'left':portletPosition.left, 'top':portletPosition.top, 'width': computedWidth});
	}

    if (this.cfg.visible){
         if (init==true && ice.ace.NotificationPanels[this.id].cfg.visible != this.cfg.visible){
            this.jq.hide();
            this.show();
        } else {
            this.jq.show();
        }
    } else {
        if (init==true && ice.ace.NotificationPanels[this.id].cfg.visible != this.cfg.visible){
            this.jq.show();
            this.hide();
        } else {
            this.jq.hide();
        }
    }
    ice.ace.NotificationPanels[this.id] = this;
};


ice.ace.NotificationBar.prototype.show = function() {
    if (this.cfg.effect === "slide")
        ice.ace.jq(this.jq).slideDown(this.cfg.effectSpeed);
    else if (this.cfg.effect === "fade")
        ice.ace.jq(this.jq).fadeIn(this.cfg.effectSpeed);
    else if (this.cfg.effect === "none")
        ice.ace.jq(this.jq).show();
    if (!this.cfg.visible) {
        this.cfg.visible = true;
    }
    if (this.cfg.ariaEnabled) {
        ice.ace.jq(this.jq).attr("aria-hidden", !this.cfg.visible);
    }
    var behaviour = this.cfg && this.cfg.behaviors && this.cfg.behaviors.display;
    if (behaviour) {
        ice.ace.ab(behaviour);
    }
};

ice.ace.NotificationBar.prototype.hide = function() {
    if (this.cfg.effect === "slide")
        ice.ace.jq(this.jq).slideUp(this.cfg.effectSpeed);
    else if (this.cfg.effect === "fade")
        ice.ace.jq(this.jq).fadeOut(this.cfg.effectSpeed);
    else if (this.cfg.effect === "none")
        ice.ace.jq(this.jq).hide();
    if (this.cfg.visible){
        this.cfg.visible = false;
    }
    if (this.cfg.ariaEnabled) {
        ice.ace.jq(this.jq).attr("aria-hidden", !this.cfg.visible);
    }
    var behaviour = this.cfg && this.cfg.behaviors && this.cfg.behaviors.close;
    if (behaviour) {
        ice.ace.ab(behaviour);
    }
};

ice.ace.NotificationBar.getPortletContainer = function(node) {
	if (!node) return null;
	if (ice.ace.jq(node).hasClass('portlet-content')) return node;
	return ice.ace.NotificationBar.getPortletContainer(node.parentNode);
};