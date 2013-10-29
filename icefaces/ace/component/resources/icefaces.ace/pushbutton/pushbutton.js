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

ice.ace.pushbutton = function(clientId, cfg){
    this.cfg = cfg;
    this.id = clientId;
    this.jqId = ice.ace.escapeClientId(this.id);
    this.element = ice.ace.jq(this.jqId);
    this.button = ice.ace.jq(this.jqId).find(this.buttonSelector);
    this.styleState = 'default';
    var self = this;

    this.button
            .on("click", function() { self.onClick(); })
            .on("mousedown", function() { self.changeStyleState('active'); })
            .on("mouseup", function() { self.changeStyleState('hover'); })
            .on("mouseenter",function() { self.changeStyleState('hover'); })
            .on("focus",function() { self.changeStyleState('hover'); })
            .on("blur",function() { self.changeStyleState('default'); })
            .on("mouseleave",function() { self.changeStyleState('default'); })

    // lazy init occuring via kb focus, set focus style since
    // our focus event won't be set up yet
    if (document.activeElement == this.button[0])
        self.changeStyleState('hover');

    ice.onElementUpdate(this.id, function() {self.unload()});
};

// Selectors
ice.ace.pushbutton.prototype.buttonSelector = " > span > span > button";

ice.ace.pushbutton.prototype.unload = function() {
    this.button.off("click mousedown mouseup mouseenter focus blur mouseleave");
}

ice.ace.pushbutton.prototype.onClick = function () {
    var options = {
        source:this.id,
        render:"@all",
        params:this.cfg.uiParams
    },
    singleOptions = {
        execute:"@this"
    },
    fullOptions = {
        execute:"@all"
    };

    if (this.cfg.fullSubmit)
        ice.ace.jq(options).extend(fullOptions);
    else
        ice.ace.jq(options).extend(singleOptions);

    if (this.cfg.behaviors && this.cfg.behaviors.activate) {
        ice.ace.ab(ice.ace.extendAjaxArgs(
                this.cfg.behaviors.activate,
                ice.ace.clearExecRender(options))
        );
    } else
        ice.ace.ab(options);
};

ice.ace.pushbutton.prototype.changeStyleState = function(state) {
    this.removeStyleState(this.styleState);
    this.addStyleState(state);
    this.styleState = state;
}

ice.ace.pushbutton.prototype.addStyleState = function(state) {
    if (state == 'hover')
        this.button.addClass('ui-state-hover');
    else if (state == 'active')
        this.button.addClass('ui-state-active');
    else if (state == 'default') {};
};

ice.ace.pushbutton.prototype.removeStyleState = function(state) {
    if (state == 'hover')
        this.button.removeClass('ui-state-hover');
    else if (state == 'active')
        this.button.removeClass('ui-state-active');
    else if (state == 'default') {};
};