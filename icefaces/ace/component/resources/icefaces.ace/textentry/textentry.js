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

ice.ace.TextEntry = function(id, cfg) {
    var inputId = id + "_input";
    var labelName = id + "_label";
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(id) + " input.ui-textentry";
    this.jq = ice.ace.jq(this.jqId);
    var maxlength = cfg.maxlength;

    if (cfg.embeddedLabel) { // execute this when component is lazy loaded
		if (this.jq.attr("name") == labelName) {
			this.jq.attr({name: inputId});
			this.jq.val("");
			this.jq.removeClass("ui-input-label-infield");
			if (this.cfg.secret) {
				this.changeType('password');
				this.jq.focus();
			}
		}
	}

	if (!this.jq.hasClass("ui-input-label-infield"))
		ice.ace.setResetValue(this.id, this.jq.val());

    if (cfg.autoTab) {
		this.attachAutoTabEvents();
    }
    if (cfg.embeddedLabel) {
		this.attachEmbeddedLabelEvents();
    } else {
		if (this.cfg.secret) {
			this.changeType('password');
			if (cfg.autoTab) this.attachAutoTabEvents();
			var self = this;
			setTimeout(function() {
				var element = self.jq.get(0);
				element.focus();
				if (element.value.length > 0) {
					if (element.createTextRange) { // IE
						var fieldRange = element.createTextRange();  
						fieldRange.moveStart('character', element.value.length);  
						fieldRange.collapse(false);  
						fieldRange.select();
					} else { // other browsers
						var length = element.value.length;  
						element.setSelectionRange(length, length);  
					}
				}
			}, 0);
		}
	}
    this.jq.blur(function() {
        ice.setFocus();
    });
    this.jq.change(function() {
        ice.setFocus();
    });
    if (cfg.behaviors) this.attachBehaviors();
    ice.onElementUpdate(inputId, function() {
        ice.ace.destroy(id);
    });
};

ice.ace.TextEntry.prototype.attachAutoTabEvents = function() {
	var self = this;
	this.jq.keypress(
		function(e) {
			var curLength = this.value.length + 1, maxLength = this.maxLength;
			var nextTabElement = ice.ace.TextEntry.nextTabElement(this);

			if (curLength < maxLength || !nextTabElement) {
				return;
			}
			if (e.which < 32 || e.charCode == 0 || e.ctrlKey || e.altKey) {
				return;
			}
			e.preventDefault();
			if (curLength == maxLength) {
				this.value += String.fromCharCode(e.which);
			}

			ice.setFocus();
			nextTabElement.focus();

			if (ice.ace.jq.browser.msie || ice.ace.jq.browser.webkit || !!navigator.userAgent.match(/Trident.*rv\:11\./)) {
				if (self.cfg.behaviors) {
					if (self.cfg.behaviors.blur) {
						ice.ace.ab(self.cfg.behaviors.blur);
					}
					if (self.cfg.behaviors.change) {
						ice.ace.ab(self.cfg.behaviors.change);
					}
				}
			}
		}
	);
};

ice.ace.TextEntry.prototype.attachEmbeddedLabelEvents = function() {
	var self = this;
    var inputId = this.id + "_input";
    var labelName = this.id + "_label";
	this.jq.focus(
		function() {
			var input = ice.ace.jq(this);
			if (input.attr("name") == labelName) {
				input.attr({name: inputId});
				input.val("");
				input.removeClass("ui-input-label-infield");
				if (self.cfg.secret) {
					self.changeType('password');
					if (self.cfg.autoTab) self.attachAutoTabEvents();
					if (self.cfg.embeddedLabel) self.attachEmbeddedLabelEvents();
					if (self.cfg.behaviors) self.attachBehaviors();
					self.jq.focus();
				}
			}
		}).blur(
		function() {
			ice.setFocus('');
			var input = ice.ace.jq(this);
			if (ice.ace.jq.trim(input.val()) == ""
				&& !(self.cfg.behaviors && self.cfg.behaviors.blur)) {
				input.attr({name: labelName});
				input.val(self.cfg.embeddedLabel);
				input.addClass("ui-input-label-infield");
				if (self.cfg.secret) {
					self.changeType(self.originalType);
					if (self.cfg.autoTab) self.attachAutoTabEvents();
					if (self.cfg.embeddedLabel) self.attachEmbeddedLabelEvents();
					if (self.cfg.behaviors) self.attachBehaviors();
				}
			}
		});
};

ice.ace.TextEntry.prototype.attachBehaviors = function() {
	var self = this;
    var inputId = this.id + "_input";
	ice.ace.jq.each(this.cfg.behaviors, function(name, behavior) {
		if (name == 'charCount') {
			var target = document.getElementById(inputId);
			behavior.currLength = function() {
				return target.value.length;
			};
			behavior.charsRemaining = function() {
				return target.maxLength - target.value.length;
			};
			if (document.attachEvent) {
				//IE 7,8,9 handling -- backspace and delete keypresses do not trigger 'input' events
				self.jq.on('input', function (e) {
					if (e.target.value.length <= e.target.maxLength) {
						e.cancelBubble = true;
						ice.ace.ab(behavior);
					}
				});
				self.jq.on('keyup', function (e) {
					if (e.target.value.length <= e.target.maxLength) {
						ice.ace.ab(behavior);
					}
				});
			} else {
				self.jq.on('input', function (e) {
					ice.ace.ab(behavior);
				});
			}
		} else {
			self.jq.on(name, function () {
				ice.ace.ab(behavior);
			});
		}
	});
};

ice.ace.TextEntry.prototype.changeType = function(type) {
	var newInput = '<input';
	ice.ace.jq.each(this.jq.get(0).attributes, function(i, attr) {
		if (attr.name == 'type' || attr.name == 'value') return true;
		newInput += ' ' + attr.name + '="' + attr.value + '"';
	});
	newInput += ' type="'+type+'" value="'+this.jq.val()+'" />';
	this.jq.replaceWith(newInput);
    this.jq = ice.ace.jq(this.jqId);
};

// Original code copied from http://stackoverflow.com/a/7329696
// See comments at http://jira.icesoft.org/browse/ICE-7824?focusedCommentId=39755&page=com.atlassian.jira.plugin.system.issuetabpanels%3Acomment-tabpanel#action_39755
ice.ace.TextEntry.nextTabElement = function(currElement) {
    // if we haven't stored the tabbing order
    if (!currElement.form.tabOrder) {

        var els = currElement.form.elements,
                ti = [],
                rest = [];

        // store all focusable form elements with tabIndex > 0
        for (var i = 0, il = els.length; i < il; i++) {
            if (els[i].tabIndex > 0 &&
                    !els[i].disabled &&
                    !els[i].hidden &&
                    !els[i].readOnly &&
                    els[i].type !== 'hidden') {
                ti.push(els[i]);
            }
        }

        // sort them by tabIndex order
        ti.sort(function(a,b){ return a.tabIndex - b.tabIndex; });

        // store the rest of the elements in order
        for (i = 0, il = els.length; i < il; i++) {
            if (els[i].tabIndex == 0 &&
                    !els[i].disabled &&
                    !els[i].hidden &&
                    !els[i].readOnly &&
                    els[i].type !== 'hidden') {
                rest.push(els[i]);
            }
        }

        // store the full tabbing order
        currElement.form.tabOrder = ti.concat(rest);
    }

    // find the next element in the tabbing order and focus it
    // if the last element of the form then blur
    // (this can be changed to focus the next <form> if any)
    for (var j = 0, jl = currElement.form.tabOrder.length; j < jl; j++) {
        if (currElement === currElement.form.tabOrder[j]) {
            if (j+1 < jl) {
//                        $(this.form.tabOrder[j+1]).focus();
                return currElement.form.tabOrder[j+1];
            } else {
//                        $(this).blur();
            }
        }
    }
};

ice.ace.TextEntry.clear = function(id, secret, originalType, embeddedLabel) {
	var input = ice.ace.jq(ice.ace.escapeClientId(id) + " input.ui-textentry");
	if (!input.hasClass("ui-input-label-infield"))
		ice.ace.setResetValue(id, input.val());
	try { if (secret) input.attr({type: originalType}); } catch (e) {}
	if (embeddedLabel) {
		input.attr({name: id + "_label"});
		input.val(embeddedLabel);
		input.addClass("ui-input-label-infield");
	} else {
		input.val('');
	}
};

ice.ace.TextEntry.reset = function(id, secret, originalType, embeddedLabel) {
	var value = ice.ace.resetValues[id];
	if (ice.ace.isSet(value)) {
		var input = ice.ace.jq(ice.ace.escapeClientId(id) + " input.ui-textentry");
		if (value) {
			try { if (self.cfg.secret) input.attr({type: 'password'}); } catch (e) {}
			if (embeddedLabel) {
				input.attr({name: input.attr('id')});
				input.removeClass("ui-input-label-infield");
			}
			input.val(value);
		} else ice.ace.TextEntry.clear(id, secret, originalType, embeddedLabel);
	}
};