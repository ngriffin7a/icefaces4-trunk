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


if (!window['mobi']) {
    window.mobi = {};
}

mobi.flipswitch = {
    lastTime: 0,

    init: function (clientId, cfg) {
        // Mobi-526 filter double clicks
        if (cfg.transHack) {
            var currentTimeMillis = new Date().getTime();
            if ((currentTimeMillis - mobi.flipswitch.lastTime) < 100) {
                console.log("Double click suppression required");
                return;
            }
            mobi.flipswitch.lastTime = currentTimeMillis;
        }

        this.id = clientId;
        this.cfg = cfg;
        this.flipperEl = cfg.elVal;
        this.event = cfg.event;

        var hasBehaviors = false;
        if (this.cfg.behaviors) {
            hasBehaviors = true;
        }
        if (this.flipperEl) {
            var oldClass = this.flipperEl.className;
            var value = "off";
            var onClass = this.flipperEl.children[0].className;
            var offClass = this.flipperEl.children[2].className;
            if (oldClass.indexOf('-off ') > 0) {
                this.flipperEl.className = 'mobi-flipswitch mobi-flipswitch-on ui-widget';
                this.flipperEl.children[0].className = 'mobi-flipswitch-txt-on ui-button ui-corner-all ui-state-default ui-state-active';
                this.flipperEl.children[2].className = 'mobi-flipswitch-txt-off ui-button ui-corner-all ui-state-default';
                value = true;
            } else {
                this.flipperEl.className = 'mobi-flipswitch mobi-flipswitch-off ui-widget';
                this.flipperEl.children[0].className = 'mobi-flipswitch-txt-on ui-button ui-corner-all ui-state-default';
                this.flipperEl.children[2].className = 'mobi-flipswitch-txt-off ui-button ui-corner-all ui-state-default ui-state-active';
                value = false;
            }
            var hidden = this.id + "_hidden";
            var thisEl = document.getElementById(hidden);
            if (thisEl) {
                thisEl.value = value.toString();
            }
            if (hasBehaviors) {
                if (this.cfg.behaviors.click) {
                    ice.ace.ab(this.cfg.behaviors.click);
                }
            }
        }
    },

    offlineDisabled: function(id) {
        var flipswitch = document.getElementById(id);
        var clickCallback = flipswitch.onclick;

        flipswitch.removeOnOfflineCallback = ice.onOffline(function() {
            clickCallback = flipswitch.onclick;
            flipswitch.onclick = null;
            var buttons = flipswitch.getElementsByTagName('span');
            for (var i = 0, l = buttons.length; i < l; i++) {
                var button = buttons[i];
                var cssClass = button.className;
                cssClass = cssClass.replace('ui-state-default', 'ui-state-disabled');
                button.className = cssClass;
            }
        });

        flipswitch.removeOnOnlineCallback = ice.onOnline(function() {
            if (clickCallback) {
                flipswitch.onclick = clickCallback;
                var buttons = flipswitch.getElementsByTagName('span');
                for (var i = 0, l = buttons.length; i < l; i++) {
                    var button = buttons[i];
                    var cssClass = button.className;
                    cssClass = cssClass.replace('ui-state-disabled', 'ui-state-default');
                    button.className = cssClass;
                }
            }
        });

        ice.onElementUpdate(id, function() {
            flipswitch.removeOnOfflineCallback();
            flipswitch.removeOnOnlineCallback();
        });
    }
};

