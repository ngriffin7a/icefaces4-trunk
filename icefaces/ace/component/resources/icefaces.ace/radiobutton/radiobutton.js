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
ice.ace.radiobutton = function(clientId, options) {
    var groups = ice.ace.radiobutton.groups,
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
    var self = this;

    // Event Binding
    ice.ace.jq(this.jqId)
            .on("mouseenter", function() { self.addStateCSSClasses('hover'); })
            .on("mouseleave", function() { self.removeStateCSSClasses('hover') ; });

    if (!options.disabled)
        ice.ace.jq(this.jqId).on("click", function () {
			ice.ace.radiobutton.toggleOthers(self.options, self.id);
			self.toggleCheckbox(true);
        });

    if (options.ariaEnabled)
        ice.ace.jq(this.jqId).on("keypress", function() { self.onAriaKeypress(); });
		
	if (this.isChecked()) {
		this.addStateCSSClasses('checked');
		ice.ace.radiobutton.toggleOthers(this.options, this.id)
	} else {
		this.removeStateCSSClasses('checked');
	}

    var unload = function() {
        // Unload WidgetVar
        // Unload events
        ice.ace.jq(self.jqId).off("click mouseenter mouseleave keypress");
    }

    ice.onElementUpdate(this.id, unload);
};

ice.ace.radiobutton.prototype.isChecked = function() {
    return ice.ace.jq(this.fieldSelector).val() == 'true' ? true : false;
};

ice.ace.radiobutton.prototype.setChecked = function(bool) {
    ice.ace.jq(this.fieldSelector).val(bool == true ? 'true' : 'false');
};

ice.ace.radiobutton.prototype.addStateCSSClasses = function(state) {
    if (state == 'hover') {
        this.button.addClass('ui-state-hover');
    }
    else if (state == 'checked') {
		this.button.removeClass('ice-ace-radiobutton-unselected').addClass('ice-ace-radiobutton-selected');
        this.icon.removeClass('ui-icon-radio-off')
                 .addClass('ui-icon-radio-on');
    }
};

ice.ace.radiobutton.prototype.removeStateCSSClasses = function(state) {
    if (state == 'hover') {
        this.button.removeClass('ui-state-hover');
    }
    else if (state == 'checked') {
		this.button.removeClass('ice-ace-radiobutton-selected').addClass('ice-ace-radiobutton-unselected');
        this.icon.removeClass('ui-icon-radio-on')
                 .addClass('ui-icon-radio-off');
    }
};

ice.ace.radiobutton.prototype.onAriaKeypress = function (e) {
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

ice.ace.radiobutton.prototype.toggleCheckbox = function (activeButton) {
    var newValue = !this.isChecked();
	if (activeButton) newValue = true;

    this.setChecked(newValue);
    if (newValue == true) this.addStateCSSClasses('checked');
    else this.removeStateCSSClasses('checked');

    if (this.options.ariaEnabled) {
        ice.ace.jq(this.innerSpanSelector).attr("aria-checked", newValue);
    }

	if (this.options.behaviors) {
		if (newValue == true) {
			if (this.options.behaviors.activate) {
				ice.ace.ab(ice.ace.extendAjaxArgs(
					this.options.behaviors.activate,
					{params: this.options.uiParams}
				));
			}
		} else {
			if (this.options.behaviors.deactivate) {
				ice.ace.ab(ice.ace.extendAjaxArgs(
					this.options.behaviors.deactivate,
					{params: this.options.uiParams}
				));
			}
		}
	}
};

if (!ice.ace.radiobutton.groups) ice.ace.radiobutton.groups = {};

ice.ace.radiobutton.toggleOthers = function (options, clientId) {
    var groups = ice.ace.radiobutton.groups,
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
