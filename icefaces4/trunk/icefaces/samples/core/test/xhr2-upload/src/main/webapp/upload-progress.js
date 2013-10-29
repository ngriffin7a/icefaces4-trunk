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

function setupUploadProgress() {
    var progressDiv = document.getElementById('progress');
    jsf.ajax.addOnEvent(function(event) {
        var t = progressDiv.appendChild(document.createElement('div')).appendChild(document.createTextNode(event.status));
        if (event.status == 'upload-progress') {
            t.appendData(' > ' + (event.uploaded / event.uploadTotal))
        }
        if (event.status == 'success') {
            progressDiv.appendChild(document.createElement('div')).appendChild(document.createTextNode('------------'));
        }
    });
}

if (window.addEventListener) {
    window.addEventListener('load', setupUploadProgress);
} else {
    window.attachEvent('onload', setupUploadProgress);
}

