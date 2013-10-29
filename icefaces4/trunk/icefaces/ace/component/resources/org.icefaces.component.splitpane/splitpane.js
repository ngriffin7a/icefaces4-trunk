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

if (!window['mobi']) {
    window.mobi = {};
}
if( !window['ice']){
    window.ice = {};
}
if (!window.ice['mobi']) {
    window.ice.mobi = {};
}
ice.mobi.splitpane = {
    panels: {},
    initClient: function(clientId, cfgIn) {
        if (!this.panels[clientId]) {
            this.panels[clientId] = ice.mobi.splitpane.Scrollable(clientId, cfgIn);
            this.panels[clientId].resize(clientId);
        } else {
            this.panels[clientId].resize(clientId);
        }
    },
    resizeHt: function(clientId) {
        if (this.panels[clientId])
            this.panels[clientId].resize(clientId);
        else {
            this.panels[clientId].unload(clientId);
        }
    },
	addListener: function(obj, event, fnc){
		if (obj.addEventListener){
			obj.addEventListener(event, fnc, false);
		} else if (obj.attachEvent) {
			obj.attachEvent("on"+event, fnc);
		} else {
			// log
		}
	},
	removeListener: function(obj, event, fnc){
		if (obj.addEventListener){
			obj.removeEventListener(event, fnc, false);
		} else if (obj.attachEvent){
			obj.detachEvent("on"+event, fnc);
		} else {
			// log
		}
	},
    Scrollable: function Scrollable(clientId, cfgIn) {
        var wrapPanel = clientId + "_wrp";
        var leftNode = document.getElementById(clientId + "_left");
        var rightNode = document.getElementById(clientId + "_right");
        var resizeCall = function() {
            ice.mobi.splitpane.resizeHt(clientId);
        };
        //
        if (cfgIn.width) {
            var width = cfgIn.width || -1;
            if (width > 0 && width < 99) {
                leftNode.style.width = width + "%";
                rightNode.style.width = (100 - width) + "%";
            }
        }
        ice.mobi.splitpane.addListener(window, 'resize', resizeCall);

        return {
            resize: function(elId) {
                var height = 0;
                var leftNode = document.getElementById(elId + "_left");
                var rtNode = document.getElementById(elId + "_right");
                var splt = document.getElementById(elId + "_splt");
                var body = window.document.body || null;
                if (body == null) return;
                if (leftNode && rtNode) {
                    if (window.innerHeight) {
                        height = window.innerHeight;
                    } else if (body.parentElement.clientHeight) {
                        height = body.parentElement.clientHeight;
                    } else if (body) {
                        if (body.clientHeight) {
                            height = body.clientHeight;
                        }
                    }
                    if (height > 0) {
                        var leftHeight = height - leftNode.offsetTop;
                        var rightHeight = height - rtNode.offsetTop;
                        if( leftHeight > 0 ){
                            leftNode.style.height = "" + leftHeight + "px";
                        }
                        if( rightHeight > 0 ){
                            rtNode.style.height = "" + rightHeight + "px";
                        }
                    }
                }
            },
            unload: function() {
                ice.mobi.splitpane.removeListener(window, "resize", resizeCall);
            }
        }
    }
};