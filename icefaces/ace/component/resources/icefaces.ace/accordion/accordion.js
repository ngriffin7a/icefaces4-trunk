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
 *  Accordion Widget
 */
ice.ace.AccordionPanel = function(id, cfg) {
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(id);
    this.jq = ice.ace.jq(this.jqId).children().eq(0);
	this.activeId = this.jqId + '_active';
    var _self = this;
	
	try {
		this.cfg.active = parseInt(ice.ace.jq(this.activeId).val());
		if (this.cfg.active < 0)
			this.cfg.active = false;
	} catch (e) {
		this.cfg.active = 0;
	}

    //Create accordion
    this.jq.accordion(this.cfg);
    
    this.jq.bind('accordionchangestart', function(event, ui) {
        _self.onTabChange(event, ui);
    });
    
    setTimeout(function() { _self.jq.accordion('resize'); }, 100); // for calculating correct heights when inside tabSet
}

/**
 * TabChange handler
 */
ice.ace.AccordionPanel.prototype.onTabChange = function(event, ui) {
    var panel = ui.newContent.get(0);

    //Write state
    ice.ace.jq(this.activeId).val(ui.options.active);

	this.fireAjaxTabChangeEvent(panel);
}

/**
 * Fires an ajax tabChangeEvent if a tabChangeListener is defined on server side
 */
ice.ace.AccordionPanel.prototype.fireAjaxTabChangeEvent = function(panel) {
    var formId = this.jq.closest('form').attr('id');
    var options = {
        source: this.id,
        execute: this.id,
		render: this.id
    },
    behaviourArgs = this.cfg && this.cfg.behaviors && this.cfg.behaviors.panechange;
	if (this.cfg.ajaxTabChange) {
		options.execute = formId;
		options.render = '@all';
	}

    var params = {};
    params[this.id + '_tabChange'] = true;
    if (panel) params[this.id + '_newTab'] = panel.id;

    options.params = params;

    if (behaviourArgs) {
        ice.ace.ab(ice.ace.extendAjaxArgs(
                behaviourArgs,
                ice.ace.clearExecRender(options)
        ));
    } else ice.ace.AjaxRequest(options);
};

ice.ace.AccordionPanel.prototype.select = function(index) {
    this.jq.accordion('activate', index);
};

ice.ace.AccordionPanel.prototype.collapseAll = function() {
    this.jq.accordion('activate', false);
};