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
ice.ace.checkboxbutton = function(clientId, options) {
    var groups = ice.ace.checkboxbutton.groups,
        groupId = options.groupId;
    if (groupId) {
        groups[groupId] = groups[groupId] || {};
        groups[groupId][clientId] = clientId;
    }
    this.options = options;

    // Selectors
    this.id = clientId;
    this.jqId = ice.ace.escapeClientId(clientId);
    this.spanSelector = this.jqId + " > span"
    this.innerSpanSelector = this.jqId + " > span > span"
    this.fieldSelector = this.jqId + " > input"
    this.buttonSelector = this.jqId + " > span > span > button"
    this.iconSelector = this.buttonSelector + " > span.ui-icon"

    // References
    this.button = ice.ace.jq(this.buttonSelector);
    this.icon = ice.ace.jq(this.iconSelector);
    var self = this,
        event = ice.ace.util.getEvent();

    // Event Binding
    ice.ace.jq(this.jqId)
            .on("mouseenter", function() { self.addStateCSSClasses('hover'); })
            .on("mouseleave", function() { self.removeStateCSSClasses('hover') ; });

    if (!options.disabled)
        ice.ace.jq(this.jqId).on("click", function () {
            self.toggleCheckbox();
            if (self.isChecked()) {
                ice.ace.checkboxbutton.toggleOthers(self.options, self.id)
            }
        });

    if (options.ariaEnabled)
        ice.ace.jq(this.jqId).on("keypress", function() { self.onAriaKeypress(); });

    // lazy init occuring via kb focus, set focus style since
    // our focus event won't be set up yet
    if (document.activeElement == this.button[0])
        this.addStateCSSClasses('hover');
    else if (event.type == "mouseover")
        this.addStateCSSClasses('hover');

    var unload = function() {
        // Unload WidgetVar
        // Unload events
        ice.ace.jq(self.jqId).off("click mouseenter mouseleave keypress");
    }

    ice.onElementUpdate(this.id, unload);
};

ice.ace.checkboxbutton.prototype.isChecked = function() {
    return ice.ace.jq(this.fieldSelector).val() == 'true' ? true : false;
};

ice.ace.checkboxbutton.prototype.setChecked = function(bool) {
    ice.ace.jq(this.fieldSelector).val(bool == true ? 'true' : 'false');
};

ice.ace.checkboxbutton.prototype.addStateCSSClasses = function(state) {
    if (state == 'hover') {
        this.button.addClass('ui-state-hover');
    }
    else if (state == 'checked') {
        this.button.addClass('ui-state-active');
        this.icon.removeClass('ui-icon-unchecked')
                 .addClass('ui-icon-check');
    }
};

ice.ace.checkboxbutton.prototype.removeStateCSSClasses = function(state) {
    if (state == 'hover') {
        this.button.removeClass('ui-state-hover');
    }
    else if (state == 'checked') {
        this.button.removeClass('ui-state-active');
        this.icon.removeClass('ui-icon-check')
                 .addClass('ui-icon-unchecked');
    }
};

ice.ace.checkboxbutton.prototype.onAriaKeypress = function (e) {
    var isSpace = e.keyCode == 32;
    var submittedValue = this.isChecked();

    if (isSpace) {
        var innerSpan = ice.ace.jq(this.innerSpanSelector);

        if (submittedValue) {
            innerSpan.attr("aria-checked", true);
        } else {
            innerSpan.attr("aria-checked", false);
        }
    }
}

ice.ace.checkboxbutton.prototype.toggleCheckbox = function (e) {
    var newValue = !this.isChecked();

    this.setChecked(newValue);
    if (newValue == true) this.addStateCSSClasses('checked');
    else this.removeStateCSSClasses('checked');

    if (this.options.ariaEnabled) {
        ice.ace.jq(this.innerSpanSelector).attr("aria-checked", newValue);
    }

    if (this.options.behaviors && this.options.behaviors.activate) {
        ice.ace.ab(ice.ace.extendAjaxArgs(
            this.options.behaviors.activate,
            {params: this.options.uiParams}
        ));
    }
};

ice.ace.checkboxbutton.groups = {};

ice.ace.checkboxbutton.toggleOthers = function (options, clientId) {
    var groups = ice.ace.checkboxbutton.groups,
        groupId = options.groupId,
        id, widget;
    if (groupId) {
        groups[groupId] = groups[groupId] || {};
        for (id in groups[groupId]) {
            if (groups[groupId].hasOwnProperty(id) && id != clientId) {
                widget = document.getElementById(id).widget;
                if (widget && widget.isChecked()) {
                    widget.toggleCheckbox();
                }
            }
        }
    }
};
