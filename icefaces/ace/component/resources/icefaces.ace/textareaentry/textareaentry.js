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

ice.ace.TextAreaEntry = function(id, cfg) {
    var jQ = ice.ace.jq;
    var inputId = id + "_input";
    var labelName = id + "_label";
    var maxlength = cfg.maxlength;
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(inputId);
    this.jq = jQ(this.jqId);
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
    if (maxlength > 0) {
        this.jq.on("keyup change", function (e) {
            if (this.value.length > maxlength) {
                this.value = this.value.substring(0, maxlength);
            }
        });
    }
    if (this.cfg.behaviors) {
        ice.ace.attachBehaviors(this.jq, this.cfg.behaviors);
    }
};