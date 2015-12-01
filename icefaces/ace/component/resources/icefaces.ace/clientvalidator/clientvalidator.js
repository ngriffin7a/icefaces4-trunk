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

    function isParent(parent, element) {
        var cursor = element;
        while (cursor) {
            if (cursor == parent) {
                return true;
            }
            cursor = cursor.parentNode;
        }

        return false;
    }

    window.addEventListener('load', function () {
        var old = ice.fullSubmit;
        ice.fullSubmit = function (execute, render, event, element, additionalParameters, callbacks) {
            var form = formOf(element.id);
            var jqForm = ice.ace.jq(form);
            var isValidForm = jqForm.valid();
            var validationResult = jqForm.validate();
            var validElements = validationResult.validElements();
            if (validElements) {
                for (var i = 0, l = validElements.length; i < l; i++) {
                    var validElement = validElements[i];
                    validElement.className = validElement.className.replace(' ui-state-error', '');
                    var cleanupValidationMessage = validElement.cleanupValidationMessage;
                    if (cleanupValidationMessage) {
                        try {
                            cleanupValidationMessage();
                        } catch (ex) {
                            //the node is cleared a second time
                        }
                    }
                }
            }

            var skipValidation = false;
            var invalidElements = validationResult.invalidElements();
            for (var j = 0, k = invalidElements.length; j < k; j++) {
                var invalidElement = invalidElements[j];
                if (isParent(element, invalidElement) && invalidElement.immediate) {
                    skipValidation = true; break;
                }
            }

            if (skipValidation || (form && isValidForm)) {
                old(execute, render, event, element, additionalParameters, callbacks);
            }
        };
    });

    function clientValidationMessageFor(id, text) {
        return function (parameter, element) {
            var messageId = id + '_msg';
            var container = document.getElementById(messageId);
            if (container.childNodes.length == 0) {
                element.className += ' ui-state-error';
                container.className = 'ui-widget ui-corner-all ui-state-error';
                var icon = container.appendChild(document.createElement('span'));
                icon.className = 'ui-faces-message-icon';
                var iconAlert = icon.appendChild(document.createElement('span'));
                iconAlert.className = 'ui-icon ui-icon-alert';
                var message = container.appendChild(document.createElement('span'));
                message.className = 'ui-faces-message-text';
                message.appendChild(document.createTextNode(text));

                element.cleanupValidationMessage = function () {
                    container.className = '';
                    container.removeChild(icon);
                    container.removeChild(message);
                };
            } else {
                container.childNodes[1].innerHTML = text;
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

    ice.ace.setupClientValidation = function(id, rule, config, messageId,  message, multiple, immediate) {
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
            document.getElementById(id).immediate = immediate;
        }
        ice.onElementUpdate(id, setup);
        setup();
    }
})();