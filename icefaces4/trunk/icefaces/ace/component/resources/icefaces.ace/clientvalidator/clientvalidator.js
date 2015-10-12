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


(function() {
    window.addEventListener('load', function () {
        var old = ice.fullSubmit;
        ice.fullSubmit = function (execute, render, event, element, additionalParameters, callbacks) {
            var cursor = element;
            var form;
            while (cursor) {
                if (cursor.nodeName.toLowerCase() == 'form') {
                    form = cursor;
                }
                cursor = cursor.parentNode;
            }

            var validElements = ice.ace.jq(form).validate().validElements();
            if (validElements) {
                for (var i = 0, l = validElements.length; i < l; i++) {
                    var element = validElements[i];
                    element.className = element.className.replace(' ui-state-error', '');
                    var id = element.associatedValidationMessageId;
                    var parent = document.getElementById(id);
                    parent.innerHTML = '';
                    if (parent.children.length == 0) {
                        parent.className = parent.className.replace(' ui-state-error', '');
                    }
                }
            }

            if (form && ice.ace.jq(form).valid()) {
                old(execute, render, event, element, additionalParameters, callbacks);
            }
        };
    });

    ice.ace.clientValidationMessageFor = function (id, text) {
        return function (parameter, element) {
            element.associatedValidationMessageId = id;
            var selector = '#' + id.replace(':', '\\:') + ' .ui-faces-message-text';
            var messageElement = ice.ace.jq(selector)[0];
            if (messageElement) {
                if (messageElement.innerHTML != text) {
                    messageElement.innerHTML = text;
                }
            } else {
                element.className += ' ui-state-error';
                var node = document.getElementById(id);
                node.className = 'ui-widget ui-corner-all ui-state-error';
                var icon = node.appendChild(document.createElement('span'));
                icon.className = 'ui-faces-message-icon';
                var iconAlert = icon.appendChild(document.createElement('span'));
                iconAlert.className = 'ui-icon ui-icon-alert';
                var message = node.appendChild(document.createElement('span'));
                message.className = 'ui-faces-message-text';
                message.appendChild(document.createTextNode(text));
            }
            return '';
        }
    };
})();