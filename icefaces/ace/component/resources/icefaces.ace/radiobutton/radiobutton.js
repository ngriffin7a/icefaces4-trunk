/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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
	ice.ace.radiobutton.register(clientId, options.groupId);
    this.options = options;

    // Selectors
    this.id = clientId;
    this.jqId = ice.ace.escapeClientId(clientId);
    this.spanSelector = this.jqId + " > span"
    this.innerSpanSelector = this.jqId + " > span > span"
    this.fieldSelector = this.jqId + " > input"
    this.buttonSelector = this.jqId + " > span > span > button"
    this.iconSelector = this.buttonSelector + " > span.fa"

    // References
    this.button = ice.ace.jq(this.buttonSelector);
    this.icon = ice.ace.jq(this.iconSelector);
    var self = this;

	ice.ace.radiobutton.setResetValue(clientId, options.radioButtons);

    // Event Binding
    ice.ace.jq(this.jqId)
            .on("mouseenter", function() { self.addStateCSSClasses('hover'); })
            .on("mouseleave", function() { self.removeStateCSSClasses('hover') ; });

    if (!options.disabled)
        ice.ace.jq(this.jqId).on("click", function () {
			if (self.options.radioButtons) {
				ice.ace.radiobutton.toggleOthers(self.options, self.id);
				self.toggleCheckbox(true);
			} else {
				ice.ace.radiobutton.toggleOthers(self.options, self.id);
				self.toggleCheckbox(true);
			}
        });

    if (options.ariaEnabled)
        ice.ace.jq(this.jqId).on("keypress", function(e) { self.onAriaKeypress(e); });
		
    var unload = function() {
        // Unload events
        ice.ace.jq(self.jqId).off("click mouseenter mouseleave keypress");
    }

    ice.onElementUpdate(this.id, unload);
};

ice.ace.radiobutton.register = function(clientId, groupId) {
    var groups = ice.ace.radiobutton.groups,
        groupId = groupId;
    if (groupId) {
        groups[groupId] = groups[groupId] || {};
        groups[groupId][clientId] = clientId;
    }
};

ice.ace.radiobutton.prototype.isChecked = function() {
    if (this.options.radioButtons) {
        return (!!ice.ace.jq(this.fieldSelector).attr('name'));
    } else {
		return ice.ace.jq(this.fieldSelector).val() == 'true' ? true : false;
	}
};

ice.ace.radiobutton.prototype.setChecked = function(bool) {
    if (this.options.radioButtons) {
		if (!ice.ace.jq(this.fieldSelector).attr('name'))
			ice.ace.jq(this.fieldSelector).attr('name', this.options.radioButtons);
		else if (!bool)
			ice.ace.jq(this.fieldSelector).attr('name', '');
    } else {
		ice.ace.jq(this.fieldSelector).val(bool == true ? 'true' : 'false');
	}
};

ice.ace.radiobutton.prototype.addStateCSSClasses = function(state) {
    if (state == 'hover') {
        this.button.addClass('ui-state-hover');
    }
    else if (state == 'checked') {
		this.button.removeClass('ice-ace-radiobutton-unselected').addClass('ice-ace-radiobutton-selected');
        this.icon.removeClass('fa-circle-o')
                 .addClass('fa-dot-circle-o');
    }
};

ice.ace.radiobutton.prototype.removeStateCSSClasses = function(state) {
    if (state == 'hover') {
        this.button.removeClass('ui-state-hover');
    }
    else if (state == 'checked') {
		this.button.removeClass('ice-ace-radiobutton-selected').addClass('ice-ace-radiobutton-unselected');
        this.icon.removeClass('fa-dot-circle-o')
                 .addClass('fa-circle-o');
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
		if (newValue) {
			if (this.options.behaviors.action) {
				if (activeButton) ice.setFocus(this.id + '_button');
				ice.ace.ab(ice.ace.extendAjaxArgs(
					this.options.behaviors.action,
					{params: this.options.uiParams}
				));
			}
			if (this.options.behaviors.change) {
				if (activeButton) {
					ice.setFocus(this.id + '_button');
					ice.ace.ab(ice.ace.extendAjaxArgs(
						this.options.behaviors.change,
						{params: this.options.uiParams}
					));
				}
			}
		} else {
			if (this.options.behaviors.deactivate) {
				if (activeButton) ice.setFocus(this.id + '_button');
				ice.ace.ab(ice.ace.extendAjaxArgs(
					this.options.behaviors.deactivate,
					{params: this.options.uiParams}
				));
			}
			if (this.options.behaviors.change) {
				if (activeButton) {
					ice.setFocus(this.id + '_button');
					ice.ace.ab(ice.ace.extendAjaxArgs(
						this.options.behaviors.change,
						{params: this.options.uiParams}
					));
				}
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
                widget = ice.ace.instance(id);
                if (widget && widget.isChecked()) {
                    widget.toggleCheckbox();
                }
            }
        }
    }
};

ice.ace.radiobutton.clear = function(id, ariaEnabled, multiple) {
	if (typeof ice.ace.resetValues[id] == 'undefined') ice.ace.radiobutton.setResetValue(id, multiple);

    var jqId = ice.ace.escapeClientId(id);
    var innerSpanSelector = jqId + " > span > span";
    var buttonSelector = jqId + " > span > span > button";
    var iconSelector = buttonSelector + " > span.fa";
	var fieldSelector = jqId + " > input";

	if (multiple) {
		var field = ice.ace.jq(fieldSelector);
		field.attr('name', '');
		field.attr('value', field.attr('data-value')); // value is fixed in ace:radioButtons
	} else {
		ice.ace.jq(fieldSelector).val('false');
	}

	ice.ace.jq(buttonSelector).removeClass('ice-ace-radiobutton-selected')
			 .addClass('ice-ace-radiobutton-unselected');
	ice.ace.jq(iconSelector).removeClass('fa-dot-circle-o')
			 .addClass('fa-circle-o');

    if (ariaEnabled) {
        ice.ace.jq(innerSpanSelector).attr("aria-checked", false);
    }
};

ice.ace.radiobutton.reset = function(id, ariaEnabled, multiple) {
	var value = ice.ace.resetValues[id];
	if (!ice.ace.isEmpty(value)) {
		var jqId = ice.ace.escapeClientId(id);
		var innerSpanSelector = jqId + " > span > span";
		var buttonSelector = jqId + " > span > span > button";
		var iconSelector = buttonSelector + " > span.fa";
		var fieldSelector = jqId + " > input";

		if (multiple) {
			var field = ice.ace.jq(fieldSelector);
			field.attr('value', field.attr('data-value')); // value is fixed in ace:radioButtons
			if (value === true) field.attr('name', multiple);
			else field.attr('name', '');
		} else {
			if (value === true) ice.ace.jq(fieldSelector).val('true');
			else ice.ace.jq(fieldSelector).val('false');
		}

		if (value === true) {
			ice.ace.jq(buttonSelector).removeClass('ice-ace-radiobutton-unselected')
					 .addClass('ice-ace-radiobutton-selected');
			ice.ace.jq(iconSelector).removeClass('fa-circle-o')
					 .addClass('fa-dot-circle-o');
		} else {
			ice.ace.jq(buttonSelector).removeClass('ice-ace-radiobutton-selected')
					 .addClass('ice-ace-radiobutton-unselected');
			ice.ace.jq(iconSelector).removeClass('fa-dot-circle-o')
					 .addClass('fa-circle-o');
		}

		if (ariaEnabled) {
			if (value === true) ice.ace.jq(innerSpanSelector).attr("aria-checked", true);
			else ice.ace.jq(innerSpanSelector).attr("aria-checked", false);
		}
	} else ice.ace.checkboxbutton.setResetValue(id, multiple);
};

ice.ace.radiobutton.setResetValue = function(id, multiple) {
	if (typeof ice.ace.resetValues[id] == 'undefined') {
		var jqId = ice.ace.escapeClientId(id);
		var field = ice.ace.jq(jqId + " > input");
		if (multiple) {
			ice.ace.setResetValue(id, !!field.attr('name'));
		} else {
			ice.ace.setResetValue(id, field.val() == 'true' ? true : false);
		}
	}
};