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

// Constructor
ice.ace.linkButton = function(clientId, cfg) {
    var self = this,
        event = ice.ace.util.getEvent();
    this.id = clientId;
    this.jqId = ice.ace.escapeClientId(clientId);
    this.element = ice.ace.jq(this.jqId);
    this.anchor = this.element.find(this.anchorSelector);
    this.cfg = cfg;
    this.styleState = 'default';

    this.anchor.on('click', function() { self.onClick() })
               .on('keypress', function(e) { self.onKeypress(e) })
               .on("mousedown", function() { self.changeStyleState('active'); })
               .on("mouseup", function() { self.changeStyleState('hover'); })
               .on("mouseenter",function() { self.changeStyleState('hover'); })
               .on("focus",function() { self.changeStyleState('hover'); })
               .on("blur",function() { self.changeStyleState('default'); })
               .on("mouseleave",function() { self.changeStyleState('default'); });

    // lazy init occuring- our focus/hover event won't be set up yet
    if (document.activeElement == this.anchor[0])
        self.changeStyleState('hover');
    else if (event.type == "mouseover")
        self.changeStyleState('hover');

    var unload = function() {
        self.anchor.off('click keypress mousedown mouseup mouseenter focus blur mouseleave');
    }

    ice.onElementUpdate(clientId, unload);
};

ice.ace.linkButton.prototype.anchorSelector = ' > span > span > a';

ice.ace.linkButton.prototype.onClick = function () {
    var href = this.anchor.attr('href');
    var hasHref = !!href;
    var options = {
        source: this.id,
        render:"@all",
        params:this.cfg.uiParams
    },
    singleOptions = {
        execute:"@this"
    },
    fullOptions = {
        execute:"@all"
    };

    if (this.cfg.behaviors && this.cfg.behaviors.activate) {
        ice.ace.ab(ice.ace.extendAjaxArgs(
                this.cfg.behaviors.activate,
                {params:this.cfg.uiParams}
        ));
    } else if (this.cfg.hasAction) {
        ice.ace.jq(options).extend(fullOptions);
        ice.ace.ab(options);
    } else if (!hasHref) {
        ice.ace.jq(options).extend(singleOptions);
        ice.ace.ab(options);
    }

    // Skip default anchor behavior if missing an href or if
    // a listener/behavior is attached to the component
    if (!hasHref || this.cfg.hasAction || this.cfg.behaviors)
        return false;
};

ice.ace.linkButton.prototype.onKeypress = function (e) {
    if (e.keyCode != 13)
        return true;

    this.onClick();
};

ice.ace.linkButton.prototype.changeStyleState = function(state) {
    this.removeStyleState(this.styleState);
    this.addStyleState(state);
    this.styleState = state;
}

ice.ace.linkButton.prototype.addStyleState = function(state) {
    if (state == 'hover') this.element.addClass('ui-state-hover');
    else if (state == 'active') this.element.addClass('ui-state-active');
    else if (state == 'default') {}
};

ice.ace.linkButton.prototype.removeStyleState = function(state) {
    if (state == 'hover') this.element.removeClass('ui-state-hover');
    else if (state == 'active') this.element.removeClass('ui-state-active');
    else if (state == 'default') {}
};
