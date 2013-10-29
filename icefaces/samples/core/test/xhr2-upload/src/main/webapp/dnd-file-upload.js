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


function setupDnD() {
    var dropTarget = document.getElementById("drop-target");
    var form = document.getElementById("the-form");
    var fileInput = document.getElementById('the-form:file');
    //hide file input to avoid confusion during testing
    fileInput.style.visibility = 'hidden';

    dropTarget.ondragover = function () {
        dropTarget.className = 'hover'; return false;
    };
    dropTarget.ondragend = function () {
        dropTarget.className = ''; return false;
    };
    dropTarget.ondrop = function(event) {
        dropTarget.className = '';
        event.preventDefault();
        event.stopPropagation();
        //remove file input so that we can submit in it's behalf
        form.removeChild(fileInput);

        var files = event.dataTransfer.files;
        if (files && files.length) {
            for (var i=0, len = files.length; i < len; i++) {
                var f = files[i];
                //send file in behalf of h:inputFile component
                jsf.ajax.request(form, event, {
                    "the-form:file": f,
                    "javax.faces.partial.render": '@all',
                    onevent: function(c) {
                        if (c.status == 'begin') form.appendChild(fileInput);
                        //hide h:inputFile to avoid confusions during testing, we just use its server side to upload content to it
                        if (c.status == 'success') fileInput.style.visibility = 'hidden';
                    }
                });
            }
        } else {
            var data = event.dataTransfer.getData('text');
            //send data in behalf of h:inputFile component
            jsf.ajax.request(form, event, {
                "the-form:file": data,
                "javax.faces.partial.render": '@all',
                onevent: function(c) {
                    if (c.status == 'begin') form.appendChild(fileInput);
                    //hide h:inputFile to avoid confusions during testing, we just use its server side to upload content to it
                    if (c.status == 'success') fileInput.style.visibility = 'hidden';
                }
            });
        }
    }
}

if (window.addEventListener) {
    window.addEventListener('load', setupDnD);
} else {
    window.attachEvent('onload', setupDnD);
}
