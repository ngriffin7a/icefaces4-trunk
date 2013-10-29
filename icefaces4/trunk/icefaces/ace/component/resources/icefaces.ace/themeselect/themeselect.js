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
(function ($, undefined) {

var ThemeSelect = ice.ace.ThemeSelect = function (clientId, cfg) {
    this.clientId = clientId;
    var selectId = "select_" + clientId;
    this.$sel = $(ice.ace.escapeClientId(selectId));

    if (cfg.behaviors) {
        ice.ace.attachBehaviors(this.$sel, cfg.behaviors);
    }
    var currentSelection = document.getElementById(selectId).value;
    ice.onElementUpdate(selectId, function() {
        if (currentSelection != document.getElementById(selectId).value) {
            window.location.reload();
        } else {
            currentSelection = document.getElementById(selectId).value;
        }
    });
};

ThemeSelect.prototype.destroy = function () {
    var instances = this.constructor.instances;
    this.$sel.off("change");
    instances[this.clientId] = null;
    delete instances[this.clientId];
};

ThemeSelect.instances = {};

ThemeSelect.singleEntry = function (clientId, cfg) {
    $(function () {
        var instance = ThemeSelect.instances[clientId] = new ThemeSelect(clientId, cfg);
        ice.onElementUpdate(clientId, function () {
            instance.destroy();
        });
    });
};

})(ice.ace.jq);
