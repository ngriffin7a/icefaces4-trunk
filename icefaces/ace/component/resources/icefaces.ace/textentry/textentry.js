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
ice.ace.TextEntry = function(id, cfg) {
    var jQ = ice.ace.jq;
    var inputId = id + "_input";
    var labelName = id + "_label";
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(id) + " input.ui-textentry";
    this.jq = jQ(this.jqId);
	
	if (cfg.embeddedLabel) { // execute this when component is lazy loaded
		if (this.jq.attr("name") == labelName) {
			this.jq.attr({name: inputId});
			this.jq.val("");
			this.jq.removeClass("ui-input-label-infield");
		}
	}

    if (cfg.autoTab) {
        this.jq.keypress(
            function(e) {
                var curLength = this.value.length + 1, maxLength = this.maxLength;
                var nextTabElement = ice.ace.TextEntry.nextTabElement(this);
                /*
                 console.log("id: ", this.id);
                 console.log("value: ", this.value);
                 console.log("value.length: ", this.value.length);
                 console.log("maxLength: ", maxLength);
                 console.log("charCode: ", e.charCode);
                 console.log("keyCode: ", e.keyCode);
                 console.log("which: ", e.which);
                 //            console.dir(e);
                 */
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
                /*
                 console.log("value: ", this.value);
                 console.log("value.length: ", this.value.length);
                 */
                nextTabElement.focus();
            }
        );
    }
    if (cfg.embeddedLabel) {
        this.jq.focus(
            function() {
                var input = jQ(this);
                if (input.attr("name") == labelName) {
                    input.attr({name: inputId});
                    input.val("");
                    input.removeClass("ui-input-label-infield");
                }
            }).blur(
            function() {
                var input = jQ(this);
                if (jQ.trim(input.val()) == "") {
                    input.attr({name: labelName});
                    input.val(cfg.embeddedLabel);
                    input.addClass("ui-input-label-infield");
                }
            });
    }
    this.jq.blur(function() {
        ice.setFocus();
    });
    if (this.cfg.behaviors) {
        ice.ace.attachBehaviors(this.jq, this.cfg.behaviors);
    }
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