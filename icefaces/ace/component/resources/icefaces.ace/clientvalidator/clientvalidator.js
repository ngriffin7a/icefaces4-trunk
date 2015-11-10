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
    function formOf(id) {
        var cursor = document.getElementById(id);
        while (cursor) {
            if (cursor.nodeName.toLowerCase() == 'form') {
                return cursor;
            }
            cursor = cursor.parentNode;
        }

        return null;
    }

    window.addEventListener('load', function () {
        var old = ice.fullSubmit;
        ice.fullSubmit = function (execute, render, event, element, additionalParameters, callbacks) {
            var form = formOf(element.id);
            var validElements = ice.ace.jq(form).validate().validElements();
            if (validElements) {
                for (var i = 0, l = validElements.length; i < l; i++) {
                    var element = validElements[i];
                    element.className = element.className.replace(' ui-state-error', '');
                    var cleanupValidationMessage = element.cleanupValidationMessage;
                    if (cleanupValidationMessage) {
                        cleanupValidationMessage();
                    }
                }
            }

            if (form && ice.ace.jq(form).valid()) {
                old(execute, render, event, element, additionalParameters, callbacks);
            }
        };
    });

    function clientValidationMessageFor(id, text) {
        return function (parameter, element) {
            var messageId = id + '_msg';
            var node = document.getElementById(messageId);
            if (!node) {
                element.className += ' ui-state-error';
                node.className = 'ui-widget ui-corner-all ui-state-error';
                var icon = node.appendChild(document.createElement('span'));
                icon.className = 'ui-faces-message-icon';
                var iconAlert = icon.appendChild(document.createElement('span'));
                iconAlert.className = 'ui-icon ui-icon-alert';
                var message = node.appendChild(document.createElement('span'));
                message.className = 'ui-faces-message-text';
                message.appendChild(document.createTextNode(text));

                element.cleanupValidationMessage = function () {
                    node.removeChild(icon);
                    node.removeChild(message);
                };
            }

            return '';
        }
    }

    function clientValidationMessagesFor(id, text) {
        return function (parameter, element) {
            var messageId = id + '_msg_' + element.id;
            var container = document.getElementById(id);
            var node = document.getElementById(messageId);
            if (!node) {
                element.className += ' ui-state-error';
                node = container.appendChild(document.createElement('div'));
                node.id = messageId;
                node.className = 'ui-corner-all ui-state-error';
                var icon = node.appendChild(document.createElement('span'));
                icon.className = 'ui-icon ui-icon-alert';
                node.appendChild(document.createTextNode(text));

                element.cleanupValidationMessage = function () {
                    container.removeChild(node);
                };
            } else {
                node.childNodes[1].textContent = text;
            }

            return '';
        }
    }

    ice.ace.setupClientValidation = function(id, rule, config, messageId,  message, multiple) {
        var form = formOf(id);
        if (!form.enabledValidation) {
            ice.ace.jq(ice.ace.escapeClientId(form.id)).validate().settings.showErrors = function(){};
            form.enabledValidation = true;
        }

        function setup() {
            var selector = ice.ace.escapeClientId(id);

            ice.ace.jq(selector).rules('remove', rule);

            var ruleConfig = {};
            ruleConfig[rule] = config;
            var messageConfig = {};
            messageConfig[rule] = (multiple ? clientValidationMessagesFor : clientValidationMessageFor)(messageId, message, rule);
            ruleConfig['messages'] = messageConfig;

            ice.ace.jq(selector).rules('add', ruleConfig);
        }
        ice.onElementUpdate(id, setup);
        setup();
    }
})();