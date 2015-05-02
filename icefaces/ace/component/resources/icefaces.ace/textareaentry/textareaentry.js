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

ice.ace.TextAreaEntry = function(id, cfg) {
    var jQ = ice.ace.jq;
    var inputId = id + "_input";
    var labelName = id + "_label";
    var maxlength = cfg.maxlength;
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(inputId);
    this.jq = jQ(this.jqId);
    var self = this;
	if (cfg.placeholder && !('placeholder' in document.createElement('input'))) { // if 'placeholder' isn't supported, use label inField
		this.cfg.inFieldLabel = this.cfg.placeholder;
	}

    if (cfg.inFieldLabel) {
		if (this.jq.hasClass(cfg.inFieldLabelStyleClass)) {
			this.jq.attr({name: inputId});
			this.jq.val("");
			this.jq.removeClass(cfg.inFieldLabelStyleClass);
		}
        this.jq.focus(
            function() {
                var input = jQ(this);
                if (input.hasClass(cfg.inFieldLabelStyleClass)) {
                    input.attr({name: inputId});
                    input.val("");
                    input.removeClass(cfg.inFieldLabelStyleClass);
                }
            }).blur(
            function() {
                var input = jQ(this);
                if (jQ.trim(input.val()) == "") {
                    input.attr({name: labelName});
                    input.val(cfg.inFieldLabel);
                    input.addClass(cfg.inFieldLabelStyleClass);
                }
            });
    }
    this.jq.blur(function() {
        ice.setFocus();
    });
    this.jq.change(function() {
        ice.setFocus();
    });
    if (maxlength > 0) {
        this.jq.on("keyup change", function (e) {
            var target = e.target;
            if (target.value.length > maxlength) {
                target.value = target.value.substring(0, maxlength);
            }
        });
    }
    if (cfg.behaviors) {
        ice.ace.jq.each(cfg.behaviors, function(name, behavior) {
            if (name == 'charCount') {
                if (document.attachEvent) {
                    //IE 7,8,9 handling -- backspace and delete keypresses do not trigger 'input' events
                    self.jq.on('input', function (e) {
                        if (e.target.value.length <= maxlength) {
                            e.cancelBubble = true;
                            ice.ace.ab(behavior);
                        }
                    });
                    self.jq.on('keyup', function (e) {
                        if (e.target.value.length <= maxlength) {
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
    }

    ice.onElementUpdate(inputId, function() {
        ice.ace.destroy(id);
    });
};

ice.ace.jq(document).on("keydown keypress", function(e){
    if ((e.which || e.keyCode) == 8) {
        var tag = e.target.nodeName;
        if (!('TEXTAREA' == tag || 'INPUT' == tag)) {
            e.preventDefault();
        }
    }
});
