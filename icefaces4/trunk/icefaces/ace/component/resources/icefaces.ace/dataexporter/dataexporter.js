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

if (!window['ice']) window.ice = {};
if (!window.ice['ace']) window.ice.ace = {};
if (!ice.ace.DataExporters) ice.ace.DataExporters = {};

ice.ace.DataExporter = function(id, behaviors) {
	this.id = id;
	if (!ice.ace.DataExporters[this.id]) {
		ice.ace.DataExporters[this.id] = this;
		if (ice.ace.DataExporter.shouldOpenPopUp()) {
			if (!this.window) {
				this.openWindow();
			}
		}
	} else {
		if (ice.ace.DataExporter.shouldOpenPopUp()) {
			var instance = ice.ace.DataExporters[this.id];
			if (instance.window.closed) {
				instance.openWindow();
			}
			instance.body.innerHTML = '<p>Please wait while your file is generated...</p>';
			instance.window.focus();
		}
	}
	behaviors();
};
ice.ace.DataExporter.prototype.openWindow = function() {
	this.window = window.open('','','width=400,height=150');
	this.window.document.write('<html><head><title>Data export file loader</title></head><body id="body"></body></html>');
	this.body = this.window.document.getElementById('body');
	this.body.innerHTML = '<p>Please wait while your file is generated...</p>';
};
ice.ace.DataExporter.prototype.url = function(url) {
	if (ice.ace.DataExporter.shouldOpenPopUp()) {
		if (url == 'unsupported format') {
			this.body.innerHTML = '<p>The format you selected is not supported at this time. Contact your System Administrator.</p>';		
		} else {
			this.body.innerHTML = '<p>Click link below to download file.</p>'
				+ '<a href="' + url + '" onclick="if (window.downloaded) { window.close(); window.event.returnValue = false; } else { this.innerHTML = \'Close Window\'; window.downloaded = true; }">Download</a>';
		}
		this.window.focus();
	} else {
		if (url != 'unsupported format') {
			var iframe = document.createElement('iframe');
			iframe.setAttribute('src', url);
			iframe.style.display = 'none';
			document.body.appendChild(iframe);
		}
		//ice.ace.DataExporters[this.id] = null;
	}
};
ice.ace.DataExporter.shouldOpenPopUp = function() {
	if (ice.ace.jq.browser.msie) 
		if (ice.ace.jq.browser.version < 8)
			if (navigator.userAgent.indexOf("Trident/5") < 0) // detects IE9, regardless of compatibility mode
				return true;
	return false;
};