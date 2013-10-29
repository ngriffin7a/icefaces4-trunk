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

ice.ace.BlockUI = {};

ice.ace.BlockUI.eventSink = function() {
	return false;
};

ice.ace.BlockUI.onfocusSink = function() {
	this.blur();
	return false;
};

ice.ace.BlockUI.activate = function(id) {
	var target = ice.ace.jq(ice.ace.escapeClientId(id));
	if (target.children('.ice-ace-blockui').size() > 0) return;
	ice.ace.BlockUI.disableControls(id);
	var overlay = ice.ace.BlockUI.createOverlay(id);
	target.append(overlay);
};

ice.ace.BlockUI.disableControls = function(id) {
	var target = ice.ace.jq(ice.ace.escapeClientId(id));
	target.find('input, select, label, textarea, button, a').each(function(i,e) {
		ice.ace.jq.data(e, 'blockui-onfocus', e.onfocus);
		ice.ace.jq.data(e, 'blockui-onblur', e.onblur);
		ice.ace.jq.data(e, 'blockui-onkeypress', e.onkeypress);
		ice.ace.jq.data(e, 'blockui-onkeyup', e.onkeyup);
		ice.ace.jq.data(e, 'blockui-onkeydown', e.onkeydown);
		ice.ace.jq.data(e, 'blockui-onclick', e.onclick);
		e.onfocus = ice.ace.BlockUI.onfocusSink;
		e.onblur = ice.ace.BlockUI.eventSink;
		e.onkeypress = ice.ace.BlockUI.eventSink;
		e.onkeyup = ice.ace.BlockUI.eventSink;
		e.onkeydown = ice.ace.BlockUI.eventSink;
		e.onclick = ice.ace.BlockUI.eventSink;
	});
}

ice.ace.BlockUI.createOverlay = function(id) {
	var target = ice.ace.jq(ice.ace.escapeClientId(id));
	var offset = target.offset();
	var overlay = document.createElement('div');
	overlay.className = 'ui-widget-overlay ice-ace-blockui';
	overlay.style.cssText = 'position: absolute; z-index: 28000; zoom: 1;' +
		'top:' + offset.top + 'px;left:' + offset.left + 'px;' +
		'height:' + target.outerHeight() + 'px;width:' + target.outerWidth() + 'px;';
	return overlay;
};

ice.ace.BlockUI.deactivate = function(id) {
	var target = ice.ace.jq(ice.ace.escapeClientId(id));
	ice.ace.BlockUI.enableControls(id);
	target.children('.ice-ace-blockui').remove();
};

ice.ace.BlockUI.enableControls = function(id) {
	var target = ice.ace.jq(ice.ace.escapeClientId(id));
	target.find('input, select, label, textarea, button, a').each(function(i,e) {
		e.onfocus = ice.ace.jq.data(e, 'blockui-onfocus');
		e.onblur = ice.ace.jq.data(e, 'blockui-onblur');
		e.onkeypress = ice.ace.jq.data(e, 'blockui-onkeypress');
		e.onkeyup = ice.ace.jq.data(e, 'blockui-onkeyup');
		e.onkeydown = ice.ace.jq.data(e, 'blockui-onkeydown');
		e.onclick = ice.ace.jq.data(e, 'blockui-onclick');
		ice.ace.jq.data(e, 'blockui-onfocus', null);
		ice.ace.jq.data(e, 'blockui-onblur', null);
		ice.ace.jq.data(e, 'blockui-onkeypress', null);
		ice.ace.jq.data(e, 'blockui-onkeyup', null);
		ice.ace.jq.data(e, 'blockui-onkeydown', null);
		ice.ace.jq.data(e, 'blockui-onclick', null);
	});
}