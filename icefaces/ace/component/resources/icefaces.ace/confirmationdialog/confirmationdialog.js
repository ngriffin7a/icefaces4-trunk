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
 *  ConfirmDialog Widget
 */
ice.ace.ConfirmDialog = function(id, cfg) {
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(id);
    this.jq = ice.ace.jq(this.jqId + " div:first");
    this.cfg.resizable = false;
    this.cfg.autoOpen = false;

	// disable unsupported effects
	if (this.cfg.hide == 'pulsate') this.cfg.hide = null;
	var browser = ice.ace.ConfirmDialog.browser();
	if (browser == 'ie7' || browser == 'ie8') {
		var hide = this.cfg.hide;
		if (hide) {
			if (hide == 'highlight' || hide == 'bounce')
				this.cfg.hide = null;
			if (browser == 'ie7')
				if (hide == 'puff')
					this.cfg.hide = null;
		}
		var show = this.cfg.show;
		if (show && browser == 'ie7') {
			if (show == 'puff' || show == 'scale')
				this.cfg.show = null;
		}
		if (show == 'explode') {
			this.cfg.show = null;
		}
	}

    //Remove scripts to prevent duplicate widget issues
    this.jq.find("script").remove();

    //Create dialog
    this.jq.dialog(this.cfg);
	
	// set style attribute
	var dialogParent = this.jq.parent();
	var style = dialogParent.attr('style');
	dialogParent.attr('style', style + ';' + this.cfg.dialogStyle);

    //Setup button pane
    var buttons = ice.ace.jq(this.jqId + '_buttons');
    buttons.addClass('ui-dialog-buttonpane ui-widget-content ui-helper-clearfix');
//    buttons.appendTo(buttons.parent().parent()).addClass('ui-dialog-buttonpane ui-widget-content ui-helper-clearfix');

    //Close icon
    if(this.cfg.closable == false) {
        ice.ace.jq(this.jqId).parent().find('.ui-dialog-titlebar-close').hide();
    }

//    if(this.cfg.appendToBody) {
//        this.jq.parent().appendTo(document.body);
//    }

};

ice.ace.ConfirmDialog.prototype.show = function() {
	var jq = this.jq;
    setTimeout(function(){jq.dialog('open');},1);
};

ice.ace.ConfirmDialog.prototype.hide = function() {
	var jq = this.jq;
    setTimeout(function(){jq.dialog('close');},1);
};

ice.ace.ConfirmDialog.browser = function() {
	if (ice.ace.jq.browser.msie) 
		if (ice.ace.jq.browser.version < 8) {
			if (navigator.userAgent.indexOf("Trident/5") < 0) // detects IE9, regardless of compatibility mode
				return 'ie7';
		} else {
			if (ice.ace.jq.browser.version < 9)
				return 'ie8';
		}
	return '';
};